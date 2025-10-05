package datebase;

/**
 * 统计数据模型类
 */
public class Statistics {
    private int totalTransactions;
    private int soldProducts;
    private double totalAmount;
    private double monthlyGrowth;
    private double soldGrowth;
    private double amountGrowth;
    
    public Statistics() {}
    
    public Statistics(int totalTransactions, int soldProducts,
                     double totalAmount, double monthlyGrowth, double soldGrowth,
                     double amountGrowth) {
        this.totalTransactions = totalTransactions;
        this.soldProducts = soldProducts;
        this.totalAmount = totalAmount;
        this.monthlyGrowth = monthlyGrowth;
        this.soldGrowth = soldGrowth;
        this.amountGrowth = amountGrowth;
    }
    
    // Getters and Setters
    public int getTotalTransactions() {
        return totalTransactions;
    }
    
    public void setTotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;
    }
    
    public int getSoldProducts() {
        return soldProducts;
    }
    
    public void setSoldProducts(int soldProducts) {
        this.soldProducts = soldProducts;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public double getMonthlyGrowth() {
        return monthlyGrowth;
    }
    
    public void setMonthlyGrowth(double monthlyGrowth) {
        this.monthlyGrowth = monthlyGrowth;
    }
    
    public double getSoldGrowth() {
        return soldGrowth;
    }
    
    public void setSoldGrowth(double soldGrowth) {
        this.soldGrowth = soldGrowth;
    }
    
    public double getAmountGrowth() {
        return amountGrowth;
    }
    
    public void setAmountGrowth(double amountGrowth) {
        this.amountGrowth = amountGrowth;
    }
    
    // 格式化金额显示
    public String getFormattedTotalAmount() {
        return String.format("¥%.0f", totalAmount);
    }
    
    // 格式化增长率显示
    public String getFormattedMonthlyGrowth() {
        return String.format("%.1f%%", monthlyGrowth);
    }
    
    public String getFormattedSoldGrowth() {
        return String.format("%.1f%%", soldGrowth);
    }
    
    public String getFormattedAmountGrowth() {
        return String.format("%.1f%%", amountGrowth);
    }
    
    // 判断增长率是否为正值
    public boolean isMonthlyGrowthPositive() {
        return monthlyGrowth > 0;
    }
    
    public boolean isSoldGrowthPositive() {
        return soldGrowth > 0;
    }
    
    public boolean isAmountGrowthPositive() {
        return amountGrowth > 0;
    }
}

