package com.royalenfield.bluetooth.otap;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.royalenfield.bluetooth.ble.BleManager;
import com.royalenfield.reprime.R;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.royalenfield.reprime.utils.RELog;

import static android.bluetooth.BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE;
import static com.royalenfield.bluetooth.ble.GattCallback.characteristicToWrite;
import static com.royalenfield.bluetooth.ble.GattCallback.gattrequestMTU;
import static com.royalenfield.bluetooth.ble.GattCallback.mBluetoothGatt;
import static com.royalenfield.bluetooth.client.DeviceListFragment.cContext;
import static com.royalenfield.bluetooth.otap.BlockSegmentUtils.blockSize;
import static com.royalenfield.bluetooth.otap.BlockSegmentUtils.segmentSize;
import static com.royalenfield.bluetooth.otap.DownloadFirmwareFile.myInfoFileName;
import static com.royalenfield.bluetooth.otap.DownloadFirmwareFile.otapSrecFilename;
import static com.royalenfield.bluetooth.otap.DownloadFirmwareFile.myUpdateType;


public class OtapProcessDisplayResponse {

    private int currentBlockID;
    private BlockSegmentUtils blockSegmentUtils;
    public static int totalBlocks;
    public static ArrayList<Block> blocks;
    private long oldtime;
    private byte[] otapMessageBytes;
    private OTAPUtils otapUtils = new OTAPUtils();
    protected static Queue<byte[]> otapMessageQueue = new LinkedList<>();//if success polling in response chars
    private int SEG_RETRY_COUNT = 0;
    private int BLOCK_RETRY_COUNT = 0;
    private SharedPreferences sharedpreferences;
    private String currentVersion1, currentVersion2;
    private String BLE_PATH = "/Android/data/com.bosch.ble/files/", RE_PRIME_PATH = "/Android/data/com.royalenfield.reprime/files/";
    private boolean isPartialOTAP = false;
    public RELogger reLogger ;
    private static final String TAG = OtapProcessDisplayResponse.class.getSimpleName();

    public OtapProcessDisplayResponse(Context mContext) {
        this.reLogger =new RELogger(mContext);
        this.blockSegmentUtils=new BlockSegmentUtils(mContext);
    }

    private void storeCurrentSoftwareVersion(String cVersion) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("currentSoftwareVersion", cVersion);
        editor.apply();
    }
    private void storeTripperUniqueId(String uniqueId) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("currentTripperUniqueId", uniqueId);
        editor.apply();
    }

    public void processReplyFromDisplay(byte[] reply, BleManager bleManager) {
        RELog.d(TAG,"processReplyFromDisplay: "+ otapUtils.byteArray2Hex(reply, ""));
        //Toast.makeText(cContext.getApplicationContext(), otapUtils.byteArray2Hex(reply, ""),Toast.LENGTH_SHORT).show();
        reLogger.appendLog("processReplyFromDisplay: " + otapUtils.byteArray2Hex(reply, ""));
        sharedpreferences = cContext.getSharedPreferences(cContext.getString(R.string.preference_file_key), cContext.MODE_PRIVATE);


        Log.e("TRRR5"," ID - "+otapUtils.byteArray2Hex(reply, ""));
        // check for success response for flash notification
        if (reply[0] == 0x02) {
            if (reply[1] == 0x01) { // HW accepted to receive the OTAP data
                RELog.d(TAG,"processReplyFromDisplay: SUCCESS 0X02");
                RELog.d(TAG,"processReplyFromDisplay: "+ totalBlocks);
                bleManager.displayOtapProgress(true, 0, totalBlocks);
                bleManager.showHideViews(true);
                //Status In progress
                if (reply[5] == 0x02) {
                    bleManager.progressStatus(2);
                    RELog.d(TAG,"Abort Checksum error! 0x02");
                    reLogger.appendLog("Abort Checksum error! 0x02");
                } else {
                    blockSegmentUtils.payLoadSizeRequest();// 0X04
                }
            } else if (reply[1] == 0x02) {
                RELog.d(TAG,"onCharacteristicWrite:0x02 NEGATIVE ACK");
                reLogger.appendLog("onCharacteristicWrite:0x02 " + "NEGATIVE ACK");
            }
        } else if (reply[0] == 0x03) {
            RELog.d(TAG,"processReplyFromDisplay: SUCCESS 0X03: "+ reply[0]);
            RELog.d(TAG,"processReplyFromDisplay: s/w version received:0x03: " + reply[1] + "." + reply[2]);
            reLogger.appendLog("processReplyFromDisplay: s/w version received:0x03: " + reply[1] + "." + reply[2]);
            bleManager.displayOtapProgress(false, 0, 0);
            //TODO check software current version check
            String b1 = otapUtils.byte2Hex(reply[1]);
            String b2 = otapUtils.byte2Hex(reply[2]);
//            b1="07";
//            b2="07";
            RELog.d(TAG,"processReplyFromDisplay: s/w version decoded:0x03: " + b1 + "." + b2);
            String currentVersion = b1 + "." + b2;
            storeCurrentSoftwareVersion(currentVersion);
            Log.e("TRRR5"," OS "+currentVersion);
            // send os version broadcast
            Intent os_versionReceived = new Intent("didOSVersionReceived");
            os_versionReceived.putExtra("Osversion",currentVersion);
            LocalBroadcastManager.getInstance(cContext).sendBroadcast(os_versionReceived);
            reLogger.appendLog("processReplyFromDisplay: Storing deviceVersion:" + currentVersion);
            // check for success response for payload size request
        } else if (reply[0] == 0x04) { // read block size and segment size
            RELog.d(TAG,"processReplyFromDisplay: SUCCESS 0X04");
            reLogger.appendLog("processReplyFromDisplay: " + "SUCCESS 0X04");
            if (reply[5] == 0x02) {
                bleManager.progressStatus(2);
                RELog.d(TAG,"Abort Checksum error! 0x02");
                reLogger.appendLog("Abort Checksum error! 0x02");
            } else {
                reLogger.appendLog("initialize flash");
                byte[] arr1 = {0X00, 0X00, 0X00, reply[3]}; // TODO check the endianess
                ByteBuffer wrapped1 = ByteBuffer.wrap(arr1); // big-endian by default
                segmentSize = wrapped1.getInt(); // 170
                byte[] arr = {0X00, 0X00, reply[1], reply[2]}; // TODO check the endianess
                // 0x10 0x00
                ByteBuffer wrapped = ByteBuffer.wrap(arr); // big-endian by default
                blockSize = wrapped.getInt(); // 1 4096
                RELog.d(TAG,"processReply:0x04 "+ blockSize);
                RELog.d(TAG,"processReply:0x04 "+ segmentSize);
                requestMTU();
                readSrecFileContent(); // start parsing the file and send 0x05(initialize flash)
            }
        } else if (reply[0] == 0x05) {
            if (reply[5] == 0x01) { // Flash with range is success
                RELog.d(TAG,"processReplyFromDisplay: SUCCESS 0X05");
                reLogger.appendLog("processReplyFromDisplay: " + "SUCCESS 0X05");
                //Status In progress
                //TODO start sending blocks from ID 0
                // initiate with sending the block info for ID 0
                blockSegmentUtils.sendBlock(blocks.get(0), 0);
            } else if (reply[5] == 0x02) {
                bleManager.displayOtapProgress(false, 0, 0);
                //Status failure
                bleManager.progressStatus(0);
                blockSegmentUtils.sendFlashStausRequestMessage((byte) 0x02);
                RELog.d(TAG,"processReplyFromDisplay: Abort Checksum error! 0x02");
                reLogger.appendLog("processReplyFromDisplay:0x05 " + "Abort Checksum error! 0x02");
            }
        } else if (reply[0] == 0x06) {
            if (reply[1] == 0x01) {
                RELog.d(TAG,"processReplyFromDisplay: SUCCESS 0X06");
                reLogger.appendLog("processReplyFromDisplay: " + "SUCCESS 0X06");
                oldtime = System.currentTimeMillis();
                sendTheNextOneinTheQueue();
            } else {
                bleManager.displayOtapProgress(false, 0, 0);
                //Status failure
                bleManager.progressStatus(0);
                blockSegmentUtils.sendFlashStausRequestMessage((byte) 0x02);
                RELog.d(TAG,"processReplyFromDisplay: Abort Checksum error! 0x06");
                reLogger.appendLog("processReplyFromDisplay: " + "Abort Checksum error! 0x06");
            }
        } else if (reply[0] == 0x07) {
            if (reply[5] == 0x01) {
                RELog.d(TAG,"processReplyFromDisplay: SUCCESS 0X07");
                reLogger.appendLog("processReplyFromDisplay: " + "SUCCESS 0X07");
                SEG_RETRY_COUNT = 0;
                sendTheNextOneinTheQueue();
            } else {
                // check the retry count
                if (SEG_RETRY_COUNT < Segment.getRetryCount()) {
                    retryThePrevMessage();
                    SEG_RETRY_COUNT++;
                } else {
                    blockSegmentUtils.sendFlashStausRequestMessage((byte) 0x02);
                    bleManager.displayOtapProgress(false, 0, 0);
                    //Status failure
                    bleManager.progressStatus(0);
                    RELog.d(TAG,"processReplyFromDisplay: Segment retry exceeded! 0x07");
                    reLogger.appendLog("processReplyFromDisplay: " + "Segment retry exceeded! 0x07");
                    SEG_RETRY_COUNT = 0;
                }
            }
        } else if (reply[0] == 0x08) {
            long newTime = System.currentTimeMillis();
            RELog.d(TAG,"processReplyFromDisplay: block time in ms "+ (newTime - oldtime));
            reLogger.appendLog("processReplyFromDisplay: block time in ms " + (newTime - oldtime));
            otapMessageQueue.clear();
            if (reply[3] == 0x01) {
                RELog.d(TAG,"processReplyFromDisplay: SUCCESS 0X08");
                //TODO start sending blocks from currentBlockID 0
                reLogger.appendLog("processReplyFromDisplay: " + "SUCCESS 0X08");
                byte[] arr1 = {0X00, 0X00, reply[1], reply[2]}; // TODO check the endianess
                ByteBuffer wrapped1 = ByteBuffer.wrap(arr1); // big-endian by default
                currentBlockID = wrapped1.getInt();
                bleManager.displayOtapProgress(true, currentBlockID, totalBlocks);
                BLOCK_RETRY_COUNT = 0;
                currentBlockID++;
                if (currentBlockID < blocks.size()) {
                    blockSegmentUtils.sendBlock(blocks.get(currentBlockID), currentBlockID);//TODO resend the previous block
                } else if (currentBlockID == blocks.size()) {
                    blockSegmentUtils.sendFileVerificationMessage();
                }
            } else {
                // check the retry count
                if (BLOCK_RETRY_COUNT < Block.getRetryCount()) {
                    BLOCK_RETRY_COUNT++;
                    blockSegmentUtils.sendBlock(blocks.get(currentBlockID), currentBlockID);//TODO resend the previous block
                } else {
                    bleManager.displayOtapProgress(false, 0, 0);
                    //Status failure
                    bleManager.progressStatus(0);
                    blockSegmentUtils.sendFlashStausRequestMessage((byte) 0x02);
                    RELog.d(TAG,"processReplyFromDisplay: Block retry exceeded! 0x08");
                    reLogger.appendLog("processReplyFromDisplay: " + "Block retry exceeded! 0x08");
                }
            }
        } else if (reply[0] == 0x09) {
            if (reply[1] == 0x01) {
                RELog.d(TAG,"processReplyFromDisplay: SUCCESS 0X09");
                reLogger.appendLog("processReplyFromDisplay: " + "SUCCESS 0X09");
                //send 0X0A
                blockSegmentUtils.sendFlashStausRequestMessage((byte) 0x01);
            } else if (reply[1] == 0x02) {
                blockSegmentUtils.sendFlashStausRequestMessage((byte) 0x02);
                bleManager.displayOtapProgress(false, 0, 0);
                //Status failure
                bleManager.progressStatus(0);
                //TODO send 0X0A with error code
                RELog.d(TAG,"processReplyFromDisplay: Abort Checksum error! 0x09 0x02");
                reLogger.appendLog("processReplyFromDisplay: " + "Abort Checksum error! 0x09 0x02");
            } else if (reply[1] == 0x03) {
                blockSegmentUtils.sendFlashStausRequestMessage((byte) 0x02);
                bleManager.displayOtapProgress(false, 0, 0);
                //Status failure
                bleManager.progressStatus(0);
                RELog.d(TAG,"processReplyFromDisplay: Abort File error! 0x09 0x03");
                reLogger.appendLog("processReplyFromDisplay: " + "Abort File error! 0x09 0x03");
            }
        } else if (reply[0] == 0x0A) {
            if (reply[1] == 0x01) {
                RELog.d(TAG,"processReplyFromDisplay: SUCCESS 0X0A");
                RELog.d(TAG,"run: Flashing complete!");
                reLogger.appendLog("Flashing complete!");
                bleManager.showHideViews(false);
                //bleManager.displayOtapProgress(false, 0, 0);
                //Status Success
                bleManager.progressStatus(1);
            } else if (reply[1] == 0x02) {
                RELog.d(TAG,"processReplyFromDisplay: Checksum error! 0x0A 0x02");
                reLogger.appendLog("processReplyFromDisplay: " + "Checksum error! 0x0A 0x02");
                bleManager.displayOtapProgress(false, 0, 0);
                //Status failure
                bleManager.progressStatus(0);
            } else if (reply[1] == 0x03) {
                RELog.d(TAG,"processReplyFromDisplay: Re-flash update error! 0x0A 0x03");
                reLogger.appendLog("processReplyFromDisplay: " + "Re-flash update error! 0x0A 0x03");
                //TODO send 0X0A with error code
                bleManager.displayOtapProgress(false, 0, 0);
                //Status failure
                bleManager.progressStatus(0);
            }
        }else if (reply[0] == 0x30) {
            String b1 = otapUtils.byte2Hex(reply[1]);
            String b2 = otapUtils.byte2Hex(reply[2]);
            String b3 = otapUtils.byte2Hex(reply[3]);
            String b4 = otapUtils.byte2Hex(reply[4]);
            String b5 = otapUtils.byte2Hex(reply[5]);
            String b6 = otapUtils.byte2Hex(reply[6]);
            String b7 = otapUtils.byte2Hex(reply[7]);
            String b8 = otapUtils.byte2Hex(reply[8]);
            String serialNo = b1+b2+b3+b4+b5+b6+b7+b8;
            storeTripperUniqueId(serialNo);
            Log.e("CHK30","CHK30"+serialNo);
            // send serial number broadcast
            Intent serialNumberReceived = new Intent("didSerialNumberReceived");
            serialNumberReceived.putExtra("SerialNo",serialNo);
            LocalBroadcastManager.getInstance(cContext).sendBroadcast(serialNumberReceived);
            //RELog.tag(TAG).e("processReplyFromDisplay: SUCCESS 0X30: %s", reply[0]);
            //RELog.tag(TAG).e("processReplyFromDisplay: s/w version received:0x30: " + reply[1] + "." + reply[2]);
        }
    }

    public static void writeOtapBytes(boolean queueIt, byte[] blockData) {
        if (characteristicToWrite == null || mBluetoothGatt == null) {
            return;
        }
        if (queueIt) {
            otapMessageQueue.offer(blockData);
        } else {
            characteristicToWrite.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            characteristicToWrite.setValue(blockData);
            boolean isWritten = mBluetoothGatt.writeCharacteristic(characteristicToWrite);
        }
    }

    public void readSrecFileContent() {
        File directory = Environment.getExternalStorageDirectory();
//        String[] fileNAme = Uri.fromFile(filePath).getLastPathSegment().split("/");
//        String selectedFileName = fileNAme[fileNAme.length - 1];
//        Log.d(TAG, "readSrecFileContent: "+selectedFileName);
        //TODO PartialOTAP Path changes needed for release
        totalBlocks = 0;
        RELog.d(TAG,"readSrecFileContent: start");
        File srecFilePath = new File(directory + RE_PRIME_PATH, otapSrecFilename);
        File infoFilePath = null;
        //If partial OTAP
        if (!myUpdateType.equalsIgnoreCase("full")) {
            infoFilePath = new File(directory + RE_PRIME_PATH, myInfoFileName);
            isPartialOTAP = true;
        }
        blocks = blockSegmentUtils.getDataBlocks(cContext, srecFilePath, infoFilePath, isPartialOTAP);
        RELog.d(BlockSegmentUtils.TAG,"readSrecFileContent: end"+ blocks.size());
        System.out.println("blocks.size():: " + blocks.size());
        totalBlocks = blocks.size();
        RELog.d(BlockSegmentUtils.TAG,"readSrecFileContent:totalBlocks "+ totalBlocks);
        if (blocks != null && blocks.size() > 0) {
            blockSegmentUtils.initializingFlashRangeRequest(blocks.get(0).getStartAddress(), totalBlocks);//0X05
        }
    }

    private void sendTheNextOneinTheQueue() {
        otapMessageBytes = otapMessageQueue.poll();
        if (otapMessageBytes != null) {
            RELog.d(BlockSegmentUtils.TAG,"sending.. "+ otapUtils.byteArray2Hex(otapMessageBytes, ""));
            reLogger.appendLog("sending.. " + otapUtils.byteArray2Hex(otapMessageBytes, ""));
            characteristicToWrite.setWriteType(WRITE_TYPE_NO_RESPONSE);
            characteristicToWrite.setValue(otapMessageBytes);
            boolean flag = mBluetoothGatt.writeCharacteristic(characteristicToWrite);
        }
    }

    private void retryThePrevMessage() {
        if (otapMessageBytes != null) {
            RELog.d(BlockSegmentUtils.TAG,"sending.. retry"+ otapUtils.byteArray2Hex(otapMessageBytes, ""));
            reLogger.appendLog("sending.. retry" + otapUtils.byteArray2Hex(otapMessageBytes, ""));
            characteristicToWrite.setWriteType(WRITE_TYPE_NO_RESPONSE);
            characteristicToWrite.setValue(otapMessageBytes);
            mBluetoothGatt.writeCharacteristic(characteristicToWrite);
        }
    }

    private void requestMTU() {
        if (characteristicToWrite != null) {
//                    characteristicToWrite.setWriteType(WRITE_TYPE_DEFAULT);
            characteristicToWrite.setWriteType(WRITE_TYPE_NO_RESPONSE);
            gattrequestMTU.requestMtu(244 + 3);
        }
    }
}
