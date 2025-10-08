package datebase;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtil {
	private static String  jdbcName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	
	public static Connection getCon()throws Exception{
		Class.forName(jdbcName);
		Connection con=DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=Wuyi;encrypt=true;trustServerCertificate=true","sa","123456");//�������û���������
		return con;
	}
	
	public void closeCon(Connection con)throws Exception{
		if(con!=null) {
			con.close();
		}
	}
}

