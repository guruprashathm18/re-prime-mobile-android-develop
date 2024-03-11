package com.royalenfield.reprime.models.request.proxy.service.servicebooking;


public class ServiceBookingRequest {

    private String Mobile;
    private String AppointmentDate;
    private String AttachmentKey;
    private String BranchID;
    private String CityName;
    private String CompanyID;
    private String CustomerRemarks;
    private String DropAddress;
    private String HomeAddress;
    private String Email;
    private String EntityType;
    private String FirstName;
    private String Engine;
    private String LastName;
    private String IsPickUpDrop;
    private String RegNo;
    private String ServiceType;
    private String Source;
    private String StateName;
    private String UserID;
    private String Chassis;
    private String UserName;
    private String services;
    private String PickUpAddress;
    private String OfficeAddress;
    private String ResourceIdentifier;
    private String SlotID;
    private String Gender;
    private String PickUpTime;
    private String Schedule;
    private String ScheduleResource;
    private boolean isDummySlots;
    private String ServiceBookingType;



    public ServiceBookingRequest() {

    }

    public ServiceBookingRequest(String Mobile, String AppointmentDate, String AttachmentKey, String BranchID,
                                 String CityName, String CompanyID, String CustomerRemarks, String DropAddress,
                                 String HomeAddress, String Email, String EntityType, String FirstName, String Engine,
                                 String LastName, String IsPickUpDrop, String RegNo, String ServiceType, String Source,
                                 String StateName, String UserID, String Chassis, String UserName, String services,
                                 String PickUpAddress, String OfficeAddress, String ResourceIdentifier, String SlotID,
                                 String Gender, String PickUpTime, String schedule, String scheduleResource, boolean isdummyslots, String serviceBookingType) {

        this.Mobile = Mobile;
        this.AppointmentDate = AppointmentDate;
        this.AttachmentKey = AttachmentKey;
        this.BranchID = BranchID;
        this.CityName = CityName;
        this.CompanyID = CompanyID;
        this.CustomerRemarks = CustomerRemarks;
        this.DropAddress = DropAddress;
        this.HomeAddress = HomeAddress;
        this.Email = Email;
        this.EntityType = EntityType;
        this.FirstName = FirstName;
        this.Engine = Engine;
        this.LastName = LastName;
        this.IsPickUpDrop = IsPickUpDrop;
        this.RegNo = RegNo;
        this.ServiceType = ServiceType;
        this.Source = Source;
        this.StateName = StateName;
        this.UserID = UserID;
        this.Chassis = Chassis;
        this.UserName = UserName;
        this.services = services;
        this.PickUpAddress = PickUpAddress;
        this.OfficeAddress = OfficeAddress;
        this.ResourceIdentifier = ResourceIdentifier;
        this.SlotID = SlotID;
        this.Gender = Gender;
        this.PickUpTime = PickUpTime;
        this.Schedule = schedule;
        this.ScheduleResource = scheduleResource;
        this.isDummySlots = isdummyslots;
        this.ServiceBookingType = serviceBookingType;


    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        AppointmentDate = appointmentDate;
    }

    public String getAttachmentKey() {
        return AttachmentKey;
    }

    public void setAttachmentKey(String attachmentKey) {
        AttachmentKey = attachmentKey;
    }

    public String getBranchID() {
        return BranchID;
    }

    public void setBranchID(String branchID) {
        BranchID = branchID;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(String companyID) {
        CompanyID = companyID;
    }

    public String getCustomerRemarks() {
        return CustomerRemarks;
    }

    public void setCustomerRemarks(String customerRemarks) {
        CustomerRemarks = customerRemarks;
    }

    public String getDropAddress() {
        return DropAddress;
    }

    public void setDropAddress(String dropAddress) {
        DropAddress = dropAddress;
    }

    public String getHomeAddress() {
        return HomeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        HomeAddress = homeAddress;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getEntityType() {
        return EntityType;
    }

    public void setEntityType(String entityType) {
        EntityType = entityType;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getEngine() {
        return Engine;
    }

    public void setEngine(String engine) {
        Engine = engine;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getIsPickUpDrop() {
        return IsPickUpDrop;
    }

    public void setIsPickUpDrop(String isPickUpDrop) {
        IsPickUpDrop = isPickUpDrop;
    }

    public String getRegNo() {
        return RegNo;
    }

    public void setRegNo(String regNo) {
        RegNo = regNo;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getChassis() {
        return Chassis;
    }

    public void setChassis(String chassis) {
        Chassis = chassis;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getPickUpAddress() {
        return PickUpAddress;
    }

    public void setPickUpAddress(String pickUpAddress) {
        PickUpAddress = pickUpAddress;
    }

    public String getOfficeAddress() {
        return OfficeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        OfficeAddress = officeAddress;
    }

    public String getResourceIdentifier() {
        return ResourceIdentifier;
    }

    public void setResourceIdentifier(String resourceIdentifier) {
        ResourceIdentifier = resourceIdentifier;
    }

    public String getSlotID() {
        return SlotID;
    }

    public void setSlotID(String slotID) {
        SlotID = slotID;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getPickUpTime() {
        return PickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        PickUpTime = pickUpTime;
    }

    public String getSchedule() {
        return Schedule;
    }

    public void setSchedule(String schedule) {
        Schedule = schedule;
    }

    public String getScheduleResource() {
        return ScheduleResource;
    }

    public void setScheduleResource(String scheduleResource) {
        ScheduleResource = scheduleResource;
    }
    public boolean isDummySlots() {
        return isDummySlots;
    }
    public void setDummySlots(boolean dummySlots) {
        isDummySlots = dummySlots;
    }
    public String getServiceBookingType() {
        return ServiceBookingType;
    }

    public void setServiceBookingType(String serviceBookingType) {
        this.ServiceBookingType = serviceBookingType;
    }

}
