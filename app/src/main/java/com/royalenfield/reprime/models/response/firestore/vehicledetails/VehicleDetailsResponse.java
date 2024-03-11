package com.royalenfield.reprime.models.response.firestore.vehicledetails;

import com.royalenfield.reprime.models.response.firestore.servicehistory.ServiceHistoryResponse;
import com.royalenfield.reprime.utils.REFirestoreConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Vehicle Details Response List containing vehicle details and corresponding service history.
 */
public class VehicleDetailsResponse {

    private String ActiveCustomerName = "";
    private String ChassisNo = "";
    private String DateOfMfg = "";
    private String EngineNo = "";
    private String ID = "";
    private String MobileNo = "";
    private String ModelCode = "";
    private String ModelName = "";
    private String RegistrationNo = "";
    private String PurchaseDate = "";
    private ArrayList<HashMap<String, String>> ServiceHistoryInfo;
    //ServiceHistory List of a vehicle
    private List<ServiceHistoryResponse> mServiceHistoryList = new ArrayList<>();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);


    public VehicleDetailsResponse(Object activeCustomerName, Object chassisNo, Object dateOfMfg,
                                  Object engineNo, Object id,
                                  Object mobileNo, Object modelCode, Object modelname,
                                  Object registrationNo, Object purchaseDate, ArrayList<HashMap<String, String>> serviceHistoryInfo) {
//        if (mServiceHistoryList != null) mServiceHistoryList.clear();
//        if (activeCustomerName != null && !activeCustomerName.equals(""))
//            this.ActiveCustomerName = activeCustomerName.toString();
//        if (chassisNo != null && !chassisNo.equals("")) this.ChassisNo = chassisNo.toString();
//        if (dateOfMfg != null && !dateOfMfg.equals("")) this.DateOfMfg = dateOfMfg.toString();
//        if (engineNo != null && !engineNo.equals("")) this.EngineNo = engineNo.toString();
//        if (id != null && !id.equals("")) this.ID = id.toString();
//        if (mobileNo != null && !mobileNo.equals("")) this.MobileNo = mobileNo.toString();
//        if (modelCode != null && !modelCode.equals("")) this.ModelCode = modelCode.toString();
//        if (modelname != null && !modelname.equals("")) this.ModelName = modelname.toString();
//        if (registrationNo != null && !registrationNo.equals("")) this.RegistrationNo = registrationNo.toString();
//        if (purchaseDate != null && !purchaseDate.equals("")) this.PurchaseDate = purchaseDate.toString();
//        if (serviceHistoryInfo != null) this.ServiceHistoryInfo = serviceHistoryInfo;
//        //Binding ServiceHistory data to list
//        if (serviceHistoryInfo != null && serviceHistoryInfo.size() > 0) {
//            for (int i = 0; i < serviceHistoryInfo.size(); i++) {
//                String lastUpdatedOn = serviceHistoryInfo.get(i).get(REFirestoreConstants.LAST_UPDATED_ON);
//                ServiceHistoryResponse serviceHistoryResponse = null;
//                try {
//                    serviceHistoryResponse = new ServiceHistoryResponse(ServiceHistoryInfo.get(i).
//                            get(REFirestoreConstants.BILL_AMOUNT), ServiceHistoryInfo.
//                            get(i).get(REFirestoreConstants.DEALERID), ServiceHistoryInfo.get(i).
//                            get(REFirestoreConstants.INVOICE_DATE), ServiceHistoryInfo.get(i).
//                            get(REFirestoreConstants.PAYMENT_METHOD), ServiceHistoryInfo.get(i).
//                            get(REFirestoreConstants.SERVICE_INVOICENUM), ServiceHistoryInfo.get(i).
//                            get(REFirestoreConstants.USERID), lastUpdatedOn != null ?format.parse(lastUpdatedOn) : null);
//                } catch (ParseException e) {
//                    RELog.e(e);
//                }
//                mServiceHistoryList.add(serviceHistoryResponse);
//            }
//            Collections.sort(mServiceHistoryList, new Comparator<ServiceHistoryResponse>() {
//                @Override
//                public int compare(ServiceHistoryResponse serviceHistoryResponse, ServiceHistoryResponse t1) {
//                    try {
//                        return t1.getLastUpdatedOn().compareTo(serviceHistoryResponse.getLastUpdatedOn());
//                    } catch (Exception e) {
//                    }
//                    return 0;
//                }
//            });
//        }
    }

    public String getActiveCustomerName() {
        return ActiveCustomerName;
    }

    public void setActiveCustomerName(String activeCustomerName) {
        ActiveCustomerName = activeCustomerName;
    }

    public String getChassisNo() {
        return ChassisNo;
    }

    public void setChassisNo(String chassisNo) {
        ChassisNo = chassisNo;
    }

    public String getDateOfMfg() {
        return DateOfMfg;
    }

    public void setDateOfMfg(String dateOfMfg) {
        DateOfMfg = dateOfMfg;
    }

    public String getEngineNo() {
        return EngineNo;
    }

    public void setEngineNo(String engineNo) {
        EngineNo = engineNo;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getModelCode() {
        return ModelCode;
    }

    public void setModelCode(String modelCode) {
        ModelCode = modelCode;
    }

    public String getModelName() {
        return ModelName;
    }

    public void setModelName(String modelName) {
        ModelName = modelName;
    }

    public String getRegistrationNo() {
        return RegistrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        RegistrationNo = registrationNo;
    }

    public String getPurchaseDate() {
        return PurchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        PurchaseDate = purchaseDate;
    }

    public ArrayList<HashMap<String, String>> getServiceHistoryInfo() {
        return ServiceHistoryInfo;
    }

    public void setServiceHistoryInfo(ArrayList<HashMap<String, String>> serviceHistoryInfo) {
        ServiceHistoryInfo = serviceHistoryInfo;
    }

    public List<ServiceHistoryResponse> getServiceHistoryList() {
        return mServiceHistoryList;
    }

    public void setServiceHistoryList(List<ServiceHistoryResponse> mServiceHistoryList) {
        this.mServiceHistoryList = mServiceHistoryList;
    }
}
