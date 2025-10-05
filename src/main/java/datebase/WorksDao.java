package datebase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public interface WorksDao  {
	public void update(Connection con,Works wk)throws SQLException;
	public List<Works> serachAll(Connection con)throws SQLException;
	public void insert(Connection con,Works wk)throws SQLException;

}

