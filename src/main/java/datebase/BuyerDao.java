package datebase;

import java.sql.SQLException;
import java.util.List;


public interface BuyerDao {
	public List<Buyer> showAllBuyers(List<String> frozenavailableOrderIds) throws SQLException;
	public Buyer getBuyerByOrderId(String orderId) throws SQLException;
	public int Trade(String orderId) throws SQLException;
	public List<String> getFrozenAndAvailableOrderIds() throws SQLException;
	public List<String> getFrozenAndAvailableTradeIds() throws SQLException;
	public String getWorkStatusByOrderId(List<String> frozenavailableOrderIds) throws SQLException;
}
