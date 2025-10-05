package datebase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;




public class WorksDaoIplm implements WorksDao {
			@Override
			public List<Works> serachAll(Connection con)throws SQLException {
				List<Works> wkList = new ArrayList<Works>();
				if (con == null) {
		            throw new SQLException("数据库连接不能为null");
		        }
				String sql = "select work_id,work_status,work_price,work_name,work_description,work_image from Works";
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					Works wk = new Works(rs.getInt("work_id"),rs.getString("work_status"), rs.getString("work_name"), rs.getString("work_description"),rs.getString("work_image"), rs.getString("work_price"));
					wkList.add(wk);
				}
				return wkList;
			}
			@Override
			public void update(Connection con,Works wk) throws SQLException {
				 if (con == null) {
			            throw new SQLException("数据库连接不能为null");
			        }
	        String sql = "UPDATE Works SET work_name=?,work_description=?,work_image=?,work_price=?,work_status=? WHERE work_id = ?";
		        PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, wk.getWork_name());
				ps.setString(2, wk.getWork_description());
				ps.setString(3, wk.getWork_image());
				ps.setString(4, wk.getWork_price());
				ps.setString(5, wk.getWork_status());
				ps.setInt(6, wk.getId());
				ps.executeUpdate();
			String sql2 = "UPDATE Trade SET work_status=? WHERE work_id = ?";
				PreparedStatement ps2 = con.prepareStatement(sql2);
				ps2.setString(1, wk.getWork_status());
				ps2.setInt(2, wk.getId());
				ps2.executeUpdate();
	       }
			@Override
			public void insert(Connection con, Works wk) throws SQLException {
				if (con == null) {
		            throw new SQLException("数据库连接不能为null");
		        }
				String sql = "INSERT INTO Works(work_name,work_description,work_image,work_price,work_status) VALUES(?,?,?,?,?)";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, wk.getWork_name());
				ps.setString(2, wk.getWork_description());
				ps.setString(3, wk.getWork_image());
				ps.setString(4, wk.getWork_price());
				ps.setString(5, wk.getWork_status());
				ps.executeUpdate();
			}
}
