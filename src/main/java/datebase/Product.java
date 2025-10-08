package datebase;

/**
 * 商品信息模型类
 */
public class Product {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private double price;
    private String status;
    
    public Product() {}
    
    public Product(String id, String name, String description, String imageUrl, double price, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.status = status;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    // 格式化价格显示
    public String getFormattedPrice() {
        return String.format("¥%.2f", price);
    }
    
    // 获取状态对应的CSS类
    public String getStatusClass() {
        switch (status) {
            case "已售罄":
                return "bg-green-100 text-green-800";
            case "已冻结":
                return "bg-yellow-100 text-yellow-800";
            case "可预订":
                return "bg-blue-100 text-blue-800";
            default:
                return "bg-gray-100 text-gray-800";
        }
    }
}

