package nepxpress.model;

public class PickupRequest {
    private int id;
    private String senderName;
    private String senderAddress;
    private String senderPhone;
    private String itemDescription;
    private String status;
    
    public PickupRequest() {}
    
    public PickupRequest(String senderName, String senderAddress, String senderPhone, String itemDescription, String status) {
        this.senderName = senderName;
        this.senderAddress = senderAddress;
        this.senderPhone = senderPhone;
        this.itemDescription = itemDescription;
        this.status = status;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getSenderName() {
        return senderName;
    }
    
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    
    public String getSenderAddress() {
        return senderAddress;
    }
    
    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }
    
    public String getSenderPhone() {
        return senderPhone;
    }
    
    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }
    
    public String getItemDescription() {
        return itemDescription;
    }
    
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
} 