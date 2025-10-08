package datebase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;



public class Buyer {
    // 买家姓名，1-5位，允许中文、英文、数字
    private String buyerName;

    // 买家手机号，11位数字
    private String buyerPhoneNumber;

    // 交易地址，最多20位，允许中文、英文、数字
    private String address;
    // 交易时间，格式：XXXX-XX-XX XX:XX
    private String tradeTime;
    // 5. 订单编号：格式 DD00001、DD00002...（DD为自定义前缀，后5位为递增数字）
    private String order_id;
    // 6. 订单提交时间：格式 "YYYY-MM-DD HH:MM:SS"
    private String order_time;
    // 7. 商品编号：4位数字（1001、1002...）
    private int work_id;

    // 无参构造方法
    public Buyer() {
    }

    // 有参构造方法
    public Buyer(String buyerName, String buyerPhoneNumber, String address, String tradeTime) {
        this.buyerName = buyerName;
        this.buyerPhoneNumber = buyerPhoneNumber;
        this.address = address;
        this.tradeTime = tradeTime;
    }
    public Buyer(String buyerName, String buyerPhoneNumber, String address,
                 String tradeTime, String order_id, String order_time, int work_id) {
        // 对每个字段进行格式校验，不符合规则则抛出异常
        setBuyerName(buyerName);
        setBuyerPhoneNumber(buyerPhoneNumber);
        setaddress(address);
        setTradeTime(tradeTime);
        setOrder_id(order_id);
        setOrder_time(order_time);
        setWork_id(work_id);
    }

    // getter 和 setter 方法
    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerPhoneNumber() {
        return buyerPhoneNumber;
    }

    public void setBuyerPhoneNumber(String buyerPhoneNumber) {
        this.buyerPhoneNumber = buyerPhoneNumber;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }
   
    public String getaddress() {
        return address;
    }


    public void setaddress(String trading_address) {
        if (trading_address == null) {
            throw new IllegalArgumentException("交易地址不能为空");
        }
        // 按字符长度校验（中文/英文/数字均算1个字符）
        if (trading_address.length() > 20) {
            throw new IllegalArgumentException("交易地址格式错误：最多20个字符");
        }
        this.address = trading_address;
    }

    public int getWork_id() {
        return work_id;
    }

    public void setWork_id(int work_id) {
        // 校验商品编号是否为4位数字（1-9999范围）
        if (work_id < 1 || work_id > 9999) {
            throw new IllegalArgumentException("商品编号格式错误：需为4位数字（0001-9999）");
        }
        this.work_id = work_id;
    }

public String getOrder_id() {
        return order_id;
    }


    public void setOrder_id(String order_id) {
        // 正则：前2位为大写/小写字母（DD为示例，实际可自定义），后5位为数字（00001~99999）
        String regex = "^[a-zA-Z]{2}\\d{5}$";
        if (order_id == null || !Pattern.matches(regex, order_id)) {
            throw new IllegalArgumentException("订单编号格式错误：需遵循 DD00001 格式（前2位字母，后5位数字）");
        }
        this.order_id = order_id;
    }

    public String getOrder_time() {
        return order_time;
    }


    public void setOrder_time(String order_time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setLenient(false);
        try {
            sdf.parse(order_time);
        } catch (ParseException e) {
            throw new IllegalArgumentException("订单提交时间格式错误：需遵循 YYYY-MM-DD HH:MM:SS 格式（如 2024-05-20 14:30:00）");
        }
        this.order_time = order_time;
    }

/*    @Override
    public String toString() {
        return "Buyer{" +
                "buyerName='" + buyerName + '\'' +
                ", buyerPhoneNumber='" + buyerPhoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", tradeTime='" + tradeTime + '\'' +
                '}';
    }*/
    @Override
	public String toString() {
		return "Buyer [buyer_name=" + buyerName + ", buyer_phonenumber=" + buyerPhoneNumber + ", trading_address="
				+ address + ", trading_time=" + tradeTime + ", order_id=" + order_id + ", order_time="
				+ order_time + ", work_id=" + String.format("%04d", work_id) + "]";
	}
}

