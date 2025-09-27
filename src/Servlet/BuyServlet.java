package Servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import datebase.BuyerDaoimpl;
import datebase.Buyer;

/**
 * 购买交易Servlet
 * 核心功能：查询所有购买人列表→按orderId查询用户→生成tradeId→存入数据表Trade→Works里的状态变为冻结→Session存储→跳转页面
 */
@WebServlet("/BuyServlet")
public class BuyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// 初始化BuyerDaoimpl（数据库操作层）
	private BuyerDaoimpl buyerDao = new BuyerDaoimpl();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BuyServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 统一设置请求/响应编码（避免中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		// 2. 获取请求参数method
		String method = request.getParameter("method");

		// 3. 先查询所有购买人列表并放入Session（无论是否为trade操作都执行，确保列表数据最新）
		try {
			List<Buyer> buyerList = buyerDao.showAllBuyers();
			HttpSession session = request.getSession();
			session.setAttribute("buyerList", buyerList);
			System.out.println("已将" + buyerList.size() + "条购买人数据存入Session");
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("errorMsg", "获取购买人列表失败：" + e.getMessage());
			request.getRequestDispatcher("/check_buyers.jsp").forward(request, response);
			return;
		}

		// 4. 判断是否为trade操作
		if (method == null || !"trade".equals(method)) {
			// 若method参数错误，直接跳转回列表页（已在Session中存入buyerList）
			request.getRequestDispatcher("/check_buyers.jsp").forward(request, response);
			return;
		}

		// 5. 执行trade核心逻辑
		try {
			trade(request, response);
		} catch (Exception e) {
			// 6. 全局异常处理：捕获所有异常，提示用户并打印日志
			e.printStackTrace();
			request.setAttribute("errorMsg", "交易处理失败：" + e.getMessage());
			request.getRequestDispatcher("/check_buyers.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Post请求复用Get逻辑（统一处理）
		doGet(request, response);
	}

	/**
	 * 核心交易逻辑：按orderId查用户→生成tradeId→存入数据库→Session存储→跳转页面
	 * @param request 请求对象（获取orderId参数）
	 * @param response 响应对象（跳转页面）
	 * @throws SQLException 数据库操作异常
	 * @throws ServletException 页面跳转异常
	 * @throws IOException 流操作异常
	 */
	protected void trade(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		// 1. 获取前端传入的orderId参数（从JSP的"与ta交易"按钮传递）
		String orderId = request.getParameter("orderId");
		// 校验orderId是否为空（避免空指针）
		if (orderId == null || orderId.trim().isEmpty()) {
			throw new IllegalArgumentException("订单编号不能为空！");
		}

		// 2. 步骤1：根据orderId查询对应用户（调用Dao层方法）
		Buyer selectedBuyer = buyerDao.getBuyerByOrderId(orderId);
		// 校验是否查询到用户
		if (selectedBuyer == null) {
			throw new RuntimeException("未找到订单编号为【" + orderId + "】的购买人，请检查订单号是否正确！");
		}

		// 3. 步骤2：生成TradeID，填写Trade表信息（调用Dao层方法）
		int affectRows = buyerDao.Trade(orderId);
		// 校验tradeId是否保存成功（affectRows=1表示成功）
		if (affectRows != 1) {
			throw new RuntimeException("交易ID生成失败，请重试！");
		}

		// 4. 步骤3：重新查询用户（获取最新的tradeId，因数据库已更新）
		selectedBuyer = buyerDao.getBuyerByOrderId(orderId);

		// 5. 新增：查询所有冻结状态的trade_id和order_id
		List<String> frozenTradeIds = buyerDao.getFrozenTradeIds();
		List<String> frozenOrderIds = buyerDao.getFrozenOrderIds();

		// 6. 步骤4：将数据存入Session
		HttpSession session = request.getSession();
		session.setAttribute("selected_buyer", selectedBuyer);
		session.setAttribute("selectedOrderId", orderId); // 存储当前选中的订单ID
		session.setAttribute("frozenTradeIds", frozenTradeIds); // 存储冻结的trade_id列表
		session.setAttribute("frozenOrderIds", frozenOrderIds); // 存储冻结的order_id列表
		System.out.print(frozenTradeIds);
		System.out.print(frozenOrderIds);

		// 转发跳转到check_buyers.jsp
		request.getRequestDispatcher("/check_buyers.jsp").forward(request, response);
	}
    

}