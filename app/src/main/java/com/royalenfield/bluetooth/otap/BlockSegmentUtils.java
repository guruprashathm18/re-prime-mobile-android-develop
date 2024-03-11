package com.royalenfield.bluetooth.otap;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.bluetooth.client.CRCCalculator.calculateCRC;
import static com.royalenfield.bluetooth.otap.OtapProcessDisplayResponse.otapMessageQueue;
import static com.royalenfield.bluetooth.otap.OtapProcessDisplayResponse.writeOtapBytes;


public class BlockSegmentUtils {

    static String TAG = "BlockSegmentUtils";
    static int blockSize;
    static int segmentSize;

    static byte[] leftOver;

    private String dataBytes;
    private String startAddress;

    private ArrayList<Block> blockArrayList2;
    private OTAPUtils otapUtils = new OTAPUtils();
    private RELogger reLogger;

    public BlockSegmentUtils(Context mContext) {
        this.reLogger = new RELogger(mContext);
    }

    /**
     * return the final partial OTAP blocks
     *
     * @param context
     * @param srecFilePath
     * @param infoFilePath
     * @return
     */
    ArrayList<Block> getDataBlocks(Context context, File srecFilePath, File infoFilePath, boolean isPartialOTAP) {
        ArrayList<Block> entireBlocks = getFullDataBlocks(context, Uri.fromFile(srecFilePath));
     //   Log.e( "infoFilePath >>>>>>>> srecFilePath: ",srecFilePath.toString() );
        if (isPartialOTAP)
            return getPartialDataBlocks(entireBlocks, context, Uri.fromFile(infoFilePath));
        else
            return entireBlocks;
    }

    /**
     * Filter the partial OTAP blocks
     *
     * @param aBlocks
     * @param context
     * @param infoFilePath
     * @return
     */
    private ArrayList<Block> getPartialDataBlocks(ArrayList<Block> aBlocks, Context context, Uri infoFilePath) {
        RELog.d("FullOTAPBlocksSize:", ""+aBlocks.size());
        Log.e( "infoFilePath >>>>>>>>>>>: ", infoFilePath.toString());
        ArrayList<Block> aResultBlocks = new ArrayList<>();
        RegionInfo aRegionInfo = getAddressRegions(context, infoFilePath);
        ArrayList<RegionMapping> aRegionMappingArrayList = aRegionInfo.getRegionMappingArrayList();
        RELog.d("RegionMappingSize", ""+aRegionMappingArrayList.size());
        for (int i = 0; i < aRegionMappingArrayList.size(); i++) {
            RegionMapping aRegionMapping = aRegionMappingArrayList.get(i);
            for (int k = 0; k < aBlocks.size(); k++) {
                Block aBlock = aBlocks.get(k);
                if (Arrays.equals(aBlock.getStartAddress(), aRegionMapping.getRegionStartAddress())) {
                    RELog.d("MatchingAddresses","" + Arrays.toString(aBlock.getStartAddress()) + " - " + Arrays.toString(aRegionMapping.getRegionStartAddress()));
                    int aRangeSize = aRegionMapping.getRegionSizeInt();
                    ArrayList<Block> aTempBlock = new ArrayList<>(aBlocks.subList(k, k + aRangeSize));
                    aResultBlocks.addAll(aTempBlock);
                    RELog.d("SubListRange:","From " + k + " To " + k + aRangeSize);
                    break;
                }
            }
        }
        RELog.d("PartialOTAPBlocksSize:",""+ aResultBlocks.size());
        return aResultBlocks;
    }

    private ArrayList<Block> getFullDataBlocks(Context context, Uri srecFilePath) {
        ArrayList<Block> blockArrayList = new ArrayList<>();
        int linecounter = 0;
        try {

            InputStream fileInputStream = context.getContentResolver().openInputStream(srecFilePath);
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(fileInputStream));

            RELog.d("linecounter before", ""+linecounter);
            Block block = null;
            String line;
            while ((line = bufReader.readLine()) != null) {
                switch (line.substring(0, 2).toUpperCase()) {
                    case "S19":
                    case "S5":
                    case "S6":
                    case "S7":
                        dataBytes = null;
                        break;
                    case "S3":
                        dataBytes = line.substring(12, line.length() - 2); // S3 15 12345678 00C4FF1FD1040000352F0200B9CD0200 45 // 00C4FF1FD1040000352F0200B9CD0200
                        startAddress = line.substring(4, 12); // S3 15 12345678 00C4FF1FD1040000352F0200B9CD0200 45 // 12345678
                        break;

                }
                if (dataBytes == null) continue;
                if (block == null) {

                    //TODO if the
                    // if leftover is not null
                    // if leftover is not null
                    // start add = start- lef.len

                    block = new Block();
                    byte[] stAddBytes = otapUtils.toByteArray(startAddress);
                    if (leftOver != null) {
                        int i = stAddBytes[3];
                        i = i - leftOver.length;
                        stAddBytes[3] = (byte) i;
                    }

                    block.setStartAddress(stAddBytes);
                }

                if (!block.isFilled()) {
                    if (leftOver != null) { // insert this
                        block.append3Buffer(leftOver);
                        leftOver = null;
                    }

                    block.append3Buffer(otapUtils.toByteArray(dataBytes));
                    if (block.isFilled()) {
                        blockArrayList.add(block);
                        block = null;
                    }
                } else {
                    blockArrayList.add(block);
                    block = null;
                }
                ++linecounter;
            }
            if (block != null && (!block.isFilled())) {
                blockArrayList.add(block);
            }
            RELog.d("TAG","linecounter after"+ linecounter);

        } catch (IOException e) {
            RELog.e(e);
        }

        blockArrayList2 = new ArrayList<>();
        for (Block block : blockArrayList) {
            blockArrayList2.add(prepareSegments(block));
        }

        return blockArrayList2;
    }

    private Block prepareSegments(Block block) {
        byte[] buffer = block.getBuffer();

        List<Segment> segmentList = new ArrayList<>();
        byte[] segData;
        int startIndex = 0;
        for (; startIndex <= block.getFilledSize() / segmentSize; ) {//(block.getFilledSize() - segmentSize-1)

            int currentSegSize = segmentSize;

            if (startIndex == block.getFilledSize() / segmentSize) {
                currentSegSize = block.getFilledSize() % segmentSize;
            }

            Segment currentSegment = new Segment();
            segData = new byte[currentSegSize];
            for (int i = 0; i < currentSegSize; i++) {
                segData[i] = buffer[(startIndex * segmentSize) + i];

            }
            currentSegment.setSegmentData(segData);
            currentSegment.setSegmentFieldSize(currentSegSize);
            segmentList.add(currentSegment);
            startIndex++;
        }
        block.setSegmentArrayList(segmentList);

        return block;
    }

    public void sendBlock(Block block, int blockId) {
        otapMessageQueue.clear();
        boolean isFirst = true;
        ByteBuffer buf = ByteBuffer.allocate(4);

        // step 1 send block Info
        // step 2 send segments
        // step 3 send block verification


        List<Segment> segments = block.getSegmentArrayList();
        for (int segmentId = 0; segmentId < segments.size(); segmentId++) {
            Segment currentSegment = segments.get(segmentId);

            int actualDataLength = currentSegment.getSegmentFilledSize();
            int frameLength = 12 + actualDataLength;
            RELog.d(TAG,"frameLength: "+ frameLength);
            byte[] message = new byte[frameLength]; // New change
            message[0] = 0x07;
            buf.clear();

            byte[] result_i = buf.putInt(blockId).array();
            message[1] = result_i[2];
            message[2] = result_i[3];
            buf.clear();

            byte[] result_j = buf.putInt(segmentId).array();
            message[3] = result_j[2];
            message[4] = result_j[3];

            ByteBuffer buf3 = ByteBuffer.allocate(4);
            byte[] result_k = buf3.putInt(actualDataLength).array();

            RELog.d(TAG,"sendBlock: "+ byteArray2Hex(result_k, ""));
            RELog.d(TAG,"sendBlock: "+ result_k);
            message[5] = result_k[3];

            byte[] data = currentSegment.getSegmentData();
            for (int i = 0; i < actualDataLength; i++) {
                message[10 + i] = data[i];
            }

            byte[] dataArray = Arrays.copyOfRange(message, 0, (message.length - 2));
            try {
                byte[] crcArray = calculateCRC(dataArray);
                message[frameLength - 2] = crcArray[0];
                message[frameLength - 1] = crcArray[1];
            } catch (IOException e) {
                RELog.e(e);
            }
            RELog.d(TAG,"sendBlock: //0x07");

            RELog.d(BlockSegmentUtils.TAG,"sendBlock: "+ otapUtils.byteArray2Hex(message, ""));
            reLogger.appendLog("sendBlock: " + otapUtils.byteArray2Hex(message, ""));
            sendMessagesthroughBLE(true, message); //0x07
            isFirst = false;
        }

        sendBlockVerificationMessage(true, blockId, block.getSha());//0X08

        sendBlockInformationMessage(false, blockId, block); // Block Info 0x06
    }


    String byteArray2Hex(byte[] a, String space) {

        StringBuilder stringBuilder = new StringBuilder(a.length * 2);
        for (int i = 0; i < a.length; i++) {
            stringBuilder.append(String.format("%02x" + space, a[i]));
            if (i == 4 || i == 9 || i == 14) {
//                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public static byte[] getSHABytes(byte[] input) {
        try {
            //Static getInstance method is called with hashing SHA
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // digest() method called
            // to calculate message digest of an input
            // and return array of byte
            byte[] messageDigest = md.digest(input);

            System.out.println();
            RELog.d(TAG,"getSHA: MessageDigest  ");
            for (int i = 0; i < messageDigest.length; i++) {
                System.out.print(messageDigest[i] & 0x000000FF);
                System.out.print(";");
            }
            System.out.println();

            return messageDigest;

        } catch (NoSuchAlgorithmException expection) {
            return null;
        }
    }


    private static void sendMessagesthroughBLE(boolean queueIt, byte[] message) {
        writeOtapBytes(queueIt, message);
    }


    public void sendReflashImageNotification() {//
        reLogger.appendLog("sendReflashImageNotification");
        byte[] reflashmessage = new byte[20];
        reflashmessage[0] = 0X02;

        byte[] temp = new byte[18];
        for (int i = 0; i < 18; i++) {
            temp[i] = reflashmessage[i];
        }

        try {
            byte[] crcArray = calculateCRC(temp);
            reflashmessage[18] = crcArray[0];
            reflashmessage[19] = crcArray[1];
        } catch (IOException e) {
            RELog.e(e);
        }
        reLogger.appendLog("sendReflashImageNotification: " + otapUtils.byteArray2Hex(reflashmessage, ""));
        sendMessagesthroughBLE(false, reflashmessage);

    }

    public void sendSoftwareVersionMessage() { // call this from onOptions
        byte[] softwareVersionmessage = new byte[20];
        softwareVersionmessage[0] = 0X03;

        byte[] temp = new byte[18];
        for (int i = 0; i < 18; i++) {
            temp[i] = softwareVersionmessage[i];
        }
        try {
            byte[] crcArray = calculateCRC(temp);
            softwareVersionmessage[18] = crcArray[0];
            softwareVersionmessage[19] = crcArray[1];
        } catch (IOException e) {
            RELog.e(e);
        }

        sendMessagesthroughBLE(false, softwareVersionmessage);
    }

    //To get Tripper Unique ID
    public void sendTripperUniqueIdMessage() { // call this from onOptions
        byte[] softwareVersionmessage = new byte[20];
        softwareVersionmessage[0] = 0X30;

        byte[] temp = new byte[18];
        for (int i = 0; i < 18; i++) {
            temp[i] = softwareVersionmessage[i];
        }
        try {
            byte[] crcArray = calculateCRC(temp);
            softwareVersionmessage[18] = crcArray[0];
            softwareVersionmessage[19] = crcArray[1];
        } catch (IOException e) {
            RELog.e(e);
        }

        sendMessagesthroughBLE(false, softwareVersionmessage);
    }

    public void payLoadSizeRequest() {
        byte[] payloadSizemessage = new byte[20];
        payloadSizemessage[0] = 0x04;

        byte[] temp = new byte[18];
        for (int i = 0; i < 18; i++) {
            temp[i] = payloadSizemessage[i];
        }

        try {
            byte[] crcArray = calculateCRC(temp);
            payloadSizemessage[18] = crcArray[0];
            payloadSizemessage[19] = crcArray[1];
        } catch (IOException e) {
            RELog.e(e);
        }

        reLogger.appendLog("payLoadSizeRequest: " + otapUtils.byteArray2Hex(payloadSizemessage, ""));
        sendMessagesthroughBLE(false, payloadSizemessage);

    }

    public void initializingFlashRangeRequest(byte[] address, int noOfBlocks) {
        byte[] flashRangemessage = new byte[20];
        flashRangemessage[0] = 0X05;

        //start address byte 1 to byte 4
        flashRangemessage[1] = address[0];
        flashRangemessage[2] = address[1];
        flashRangemessage[3] = address[2];
        flashRangemessage[4] = address[3];

        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(noOfBlocks);
        byte[] bID = b.array();

        //Number of Blocks byte 5 and 6
        flashRangemessage[5] = bID[2];
        flashRangemessage[6] = bID[3];

        byte[] temp = new byte[18];
        for (int i = 0; i < 18; i++) {
            temp[i] = flashRangemessage[i];
        }

        try {
            byte[] crcArray = calculateCRC(temp);
            flashRangemessage[18] = crcArray[0];
            flashRangemessage[19] = crcArray[1];
        } catch (IOException e) {
            RELog.e(e);
        }
        reLogger.appendLog("initializingFlashRangeRequest: " + otapUtils.byteArray2Hex(flashRangemessage, ""));
        sendMessagesthroughBLE(false, flashRangemessage);

    }

    public void sendBlockInformationMessage(boolean queueIt, int blockID, Block block) {
        byte[] blockInformationmessage = new byte[20];
        blockInformationmessage[0] = 0x06;

        //Block Id byte 1 and 2

        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(blockID);
        byte[] bID = b.array();

        blockInformationmessage[1] = bID[2];
        blockInformationmessage[2] = bID[3];


        //Block Address byte3 to Byte6
        byte[] address = block.getStartAddress();

        blockInformationmessage[3] = address[0];
        blockInformationmessage[4] = address[1];
        blockInformationmessage[5] = address[2];
        blockInformationmessage[6] = address[3];

        //number of segments in this block byte 7 and byte8
        ByteBuffer segs = ByteBuffer.allocate(4);
        segs.putInt(block.getSegmentArrayList().size());
        byte[] numOfsegsArray = segs.array();


        blockInformationmessage[7] = numOfsegsArray[2];
        blockInformationmessage[8] = numOfsegsArray[3];

        byte[] temp = new byte[18];
        for (int i = 0; i < 18; i++) {
            temp[i] = blockInformationmessage[i];
        }

        try {
            byte[] crcArray = calculateCRC(temp);
            blockInformationmessage[18] = crcArray[0];
            blockInformationmessage[19] = crcArray[1];
        } catch (IOException e) {
            RELog.e(e);
        }
        reLogger.appendLog("sendBlockInformationMessage: " + otapUtils.byteArray2Hex(blockInformationmessage, ""));
        sendMessagesthroughBLE(queueIt, blockInformationmessage);

    }

    public static boolean isSHA = false;

    private void sendBlockVerificationMessage(boolean queueIt, int blockID, byte[] SHA) {// SHA;

        System.out.println();
        RELog.d(TAG,"getSHA: sendBlockVerificationMessage  ");

        String sha = otapUtils.byteArray2Hex(SHA, "");
        RELog.d(TAG,"sendBlockVerificationMessage:== "+ sha);

        final byte[] blockMessage = new byte[41];
        blockMessage[0] = 0X08;

        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(blockID);
        byte[] bID = b.array();
        blockMessage[1] = bID[2];
        blockMessage[2] = bID[3];


        for (int i = 0; i < SHA.length; i++) {
            blockMessage[i + 7] = SHA[i];
        }

        byte[] temp = new byte[39];
        for (int i = 0; i < 39; i++) {
            temp[i] = blockMessage[i];
        }

        try {
            byte[] crcArray = calculateCRC(temp);
            blockMessage[39] = crcArray[0];
            blockMessage[40] = crcArray[1];
        } catch (IOException e) {
            RELog.e(e);
        }
        reLogger.appendLog("sendBlockVerificationMessage: " + otapUtils.byteArray2Hex(blockMessage, ""));
        sendMessagesthroughBLE(queueIt, blockMessage);
    }

    void sendFileVerificationMessage() {
        byte[] message = new byte[20];
        message[0] = 0x09;

        byte[] temp = new byte[18];
        for (int i = 0; i < 18; i++) {
            temp[i] = message[i];
        }

        try {
            byte[] crcArray = calculateCRC(temp);
            message[18] = crcArray[0];
            message[19] = crcArray[1];
        } catch (IOException e) {
            RELog.e(e);
        }
        reLogger.appendLog("sendFileVerificationMessage: " + otapUtils.byteArray2Hex(message, ""));
        sendMessagesthroughBLE(false, message);

    }

    void sendFlashStausRequestMessage(byte status) {
        byte[] message = new byte[20];
        message[0] = 0x0A;
        message[1] = status;

        byte[] temp = new byte[18];
        for (int i = 0; i < 18; i++) {
            temp[i] = message[i];
        }

        try {
            byte[] crcArray = calculateCRC(temp);
            message[18] = crcArray[0];
            message[19] = crcArray[1];
        } catch (IOException e) {
            RELog.e(e);
        }
        reLogger.appendLog("sendFlashStausRequestMessage: " + otapUtils.byteArray2Hex(message, ""));
        sendMessagesthroughBLE(false, message);

    }

    /**
     * Get the Regions and its corresponding addresses
     *
     * @param context
     * @param infoFilePath
     * @return
     */
    private RegionInfo getAddressRegions(Context context, Uri infoFilePath) {
        String aTotalRegions = null, aRegionNumber, aRegionStartAddress, aRegionSize;
        RegionInfo aRegionInfo = new RegionInfo();
        ArrayList<RegionMapping> regionArrayList = new ArrayList<>();
        int linecounter = 0;
        try {
            InputStream fileInputStream = context.getContentResolver().openInputStream(infoFilePath);
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(fileInputStream));

            RELog.d(TAG,"linecounter before"+ linecounter);
            RegionMapping region = null;
            String line;
            while ((line = bufReader.readLine()) != null) {
                if (linecounter == 0) {
                    aTotalRegions = line.substring(0, 8).toUpperCase();
                    ++linecounter;
                    continue;
                } else {
                    aRegionNumber = line.substring(0, 8).toUpperCase();
                }
                aRegionStartAddress = line.substring(12, 20);
                aRegionSize = line.substring(24, 32);
                if (region == null) {
                    //TODO if the
                    // if leftover is not null
                    // start add = start- lef.len
                    region = new RegionMapping();
                    if (null != aTotalRegions) {
                        byte[] rgTotalBytes = otapUtils.toByteArray(aTotalRegions);
                        aRegionInfo.setTotalRegions(rgTotalBytes);
                        aTotalRegions = null;
                    }
                    byte[] rgNumberBytes = otapUtils.toByteArray(aRegionNumber);
                    region.setRegionNumber(rgNumberBytes);
                    byte[] rgStAddBytes = otapUtils.toByteArray(aRegionStartAddress);
                    region.setRegionStartAddress(rgStAddBytes);
                    byte[] rgSizeBytes = otapUtils.toByteArray(aRegionSize);
                    region.setRegionSize(rgSizeBytes);
                }
                regionArrayList.add(region);
                region = null;
                ++linecounter;
            }
            RELog.d(TAG,"linecounter after"+ linecounter);

        } catch (IOException e) {
            RELog.e(e);
        }
        aRegionInfo.setRegionMappingArrayList(regionArrayList);
        return aRegionInfo;
    }
}
