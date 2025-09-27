package Servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import datebase.DbUtil;
import datebase.Works;
import datebase.WorksDaoIplm;



@WebServlet("/get_workServlet")
public class get_workServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public get_workServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		WorksDaoIplm wkdi = new WorksDaoIplm();
		Connection con  = null;   
		List<Works> wklist = new ArrayList<Works>();
		try {
			con = DbUtil.getCon();
			wklist = wkdi.serachAll(con);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		request.getSession().setAttribute("wklist", wklist);
		response.sendRedirect("modify_good_status.jsp");
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
