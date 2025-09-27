package datebase;

import java.sql.SQLException;
import java.util.List;

import datebase.Buyer;

public interface BuyerDao {
	public List<Buyer> showAllBuyers()throws SQLException;
	public Buyer getBuyerByOrderId(String orderId) throws SQLException;
	public int Trade(String orderId) throws SQLException;
	public List<String> getFrozenOrderIds() throws SQLException;
	public List<String> getFrozenTradeIds() throws SQLException;

}
