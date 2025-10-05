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



public class modify_goodStatusServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
    public modify_goodStatusServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		List<Works> wklist = (List<Works>)session.getAttribute("wklist");
		int id = Integer.parseInt(request.getParameter("work_id"));
		String workstatus = request.getParameter("work_status1");
		String workname = request.getParameter("work_name1");
		String workdes = request.getParameter("work_description1");
		String workprice = request.getParameter("work_price1");
		String workimg = request.getParameter("work_image1");
		Works wk = new Works(id,workstatus,workname, workdes, workimg, workprice);
		Connection con  = null;   
		WorksDaoIplm wkdi = new WorksDaoIplm();
		try {
			con = DbUtil.getCon();
			wkdi.update(con, wk);
			wklist = wkdi.serachAll(con); 
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		request.getSession().setAttribute("wklist", wklist);
		response.sendRedirect("modify_good_status.jsp");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
