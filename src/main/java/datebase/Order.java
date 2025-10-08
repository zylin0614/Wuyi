package datebase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 订单信息模型类
 */
public class Order {
    private String orderNumber;
    private String transactionId;
    private Product product;
    private User user;
    private LocalDateTime orderTime;
    private LocalDateTime transactionTime;
    private String status;
    
    public Order() {}
    
    public Order(String orderNumber, String transactionId, Product product, User user, 
                 LocalDateTime orderTime, LocalDateTime transactionTime, String status) {
        this.orderNumber = orderNumber;
        this.transactionId = transactionId;
        this.product = product;
        this.user = user;
        this.orderTime = orderTime;
        this.transactionTime = transactionTime;
        this.status = status;
    }
    
    // Getters and Setters
    public String getOrderNumber() {
        return orderNumber;
    }
    
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public LocalDateTime getOrderTime() {
        return orderTime;
    }
    
    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }
    
    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }
    
    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    // 格式化时间显示
    public String getFormattedOrderTime() {
        if (orderTime != null) {
            return orderTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return "";
    }
    
    public String getFormattedTransactionTime() {
        if (transactionTime != null) {
            return transactionTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        return "";
    }
    
    public String getFormattedTransactionDate() {
        if (transactionTime != null) {
            return transactionTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return "";
    }
}

