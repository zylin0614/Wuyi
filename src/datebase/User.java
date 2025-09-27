package datebase;

/**
 * 用户信息模型类
 */
public class User {
    private String name;
    private String phone;
    private String address;
    
    public User() {}
    
    public User(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    // 获取脱敏手机号
    public String getMaskedPhone() {
        if (phone != null && phone.length() >= 11) {
            return phone.substring(0, 3) + "****" + phone.substring(7);
        }
        return phone;
    }
}

