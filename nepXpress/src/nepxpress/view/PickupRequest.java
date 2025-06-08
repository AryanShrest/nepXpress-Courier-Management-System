package nepxpress.view;

import java.sql.Date;

public class PickupRequest {
    private int id;
    private String name;
    private String address;
    private String mobileNo;
    private Date pickupDate;
    private String password;
    private String requestType;
    private String status;
    
    // Default constructor
    public PickupRequest() {}
    
    // Constructor with parameters
    public PickupRequest(String name, String address, String mobileNo, Date pickupDate, String password) {
        this.name = name;
        this.address = address;
        this.mobileNo = mobileNo;
        this.pickupDate = pickupDate;
        this.password = password;
        this.requestType = "PICKUP";
        this.status = "PENDING";
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getMobileNo() {
        return mobileNo;
    }
    
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
    
    public Date getPickupDate() {
        return pickupDate;
    }
    
    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRequestType() {
        return requestType;
    }
    
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "PickupRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", pickupDate=" + pickupDate +
                ", requestType='" + requestType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}