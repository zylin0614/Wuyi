package Servlet;

import datebase.Merchant;
import datebase.MerchantDao;
import datebase.MerchantDaoImpl;
import datebase.MerchantDao;
import datebase.MerchantDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "MerchantLoginServlet", urlPatterns = "/merchantLogin")
public class MerchantLoginServlet extends HttpServlet {
    private final MerchantDao merchantDao = new MerchantDaoImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String name = req.getParameter("merchantName");
        String password = req.getParameter("merchantPassword");
        Merchant m = merchantDao.findByName(name);
        if (m != null && m.getMerchantPassword().equals(password)) {
            HttpSession session = req.getSession(true);
            session.setAttribute("merchant", m);
            resp.sendRedirect(req.getContextPath() + "/merchant_homepage.jsp");
        } else {
            req.setAttribute("error", "用户名或密码错误");
            req.getRequestDispatcher("/merchant_login.jsp").forward(req, resp);
        }
    }
}




