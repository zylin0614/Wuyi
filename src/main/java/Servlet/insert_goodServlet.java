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
import javax.servlet.http.HttpSession;

import datebase.DbUtil;
import datebase.Works;
import datebase.WorksDaoIplm;


public class insert_goodServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public insert_goodServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		
		List<Works> wklist = null;
		String workstatus = request.getParameter("work_status");
		String workname = request.getParameter("work_name");
		String workdes = request.getParameter("work_description");
		String workprice = request.getParameter("work_price");
		String workimg = request.getParameter("work_image");
		Works wk = new Works(workstatus,workname, workdes, workimg, workprice);
		Connection con  = null;   
		WorksDaoIplm wkdi = new WorksDaoIplm();
		try {
			con = DbUtil.getCon();
			wkdi.insert(con, wk);
			wklist = wkdi.serachAll(con); 
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		System.out.println(wklist.size());
		request.getSession().setAttribute("wklist", wklist);
		response.sendRedirect("modify_good_status.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
