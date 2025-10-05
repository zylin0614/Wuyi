<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>意向购买人信息</title>
<style>
  body {
    font-family: Arial, sans-serif;
    background: #f4f6f9;
    margin: 0;
    padding: 0;
  }

  /* 顶部导航栏 */
  .navbar {
    background: #2c3e50;
    color: white;
    padding: 10px 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .navbar .title {
    font-size: 18px;
    font-weight: bold;
  }
  .navbar .actions a {
    color: white;
    margin-left: 15px;
    text-decoration: none;
  }
  .navbar .actions a:hover {
    text-decoration: underline;
  }

  /* 主体内容 */
  .container {
    margin: 30px auto;
    width: 90%;
    max-width: 1000px;
    background: white;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 4px 10px rgba(0,0,0,0.1);
  }

  h2 {
    text-align: center;
    color: #2c3e50;
    margin-bottom: 20px;
  }

  /* 提示信息样式 */
  .success-msg {
    background: #d5f5e3;
    color: #27ae60;
    padding: 12px;
    border-radius: 4px;
    margin-bottom: 20px;
    text-align: center;
    font-weight: bold;
    font-size: 16px;
  }

  .error-msg {
    background: #fadbd8;
    color: #e74c3c;
    padding: 12px;
    border-radius: 4px;
    margin-bottom: 20px;
    text-align: center;
    font-weight: bold;
  }

  /* 表格 */
  table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 15px;
  }
  thead {
    background: #2c3e50;
    color: white;
  }
  th, td {
    padding: 12px 15px;
    text-align: center;
    border-bottom: 1px solid #ddd;
  }

  /* 隔行变色 */
  tbody tr:nth-child(even) {
    background: #f9f9f9;
  }

  /* 鼠标悬停高亮（仅作用在tbody） */
  tbody tr:hover {
    background: #eaf3ff;
  }

  /* 按钮样式 */
  button {
    background: #3498db;
    border: none;
    color: white;
    padding: 6px 12px;
    border-radius: 4px;
    cursor: pointer;
    transition: background 0.3s;
  }
  button:hover {
    background: #2980b9;
  }

  /* 选中订单的高亮样式 */
  .selected-row {
    background: #d5f5e3 !important;
    border: 2px solid #27ae60;
  }
</style>
</head>
<body>

<!-- 顶部导航栏 -->
<div class="navbar">
  <div class="title">意向购买人系统</div>
  <div class="actions">
    <!-- 添加返回商家首页的链接 -->
    <a href="${pageContext.request.contextPath}/merchant_homepage.jsp">返回商家首页</a>
    <a href="${pageContext.request.contextPath}/LogoutServlet">注销</a>
    <a href="javascript:location.reload();">刷新</a>
  </div>
</div>


frozenavailableTradeIds：${sessionScope.buyerDao.getFrozenAndAvailableTradeIds()}
frozenavailableOrderIds：${sessionScope.buyerDao.getFrozenAndAvailableOrderIds()}
workstatus：${sessionScope.buyerDao.getWorkStatusByOrderId(sessionScope.buyerDao.getFrozenAndAvailableOrderIds())}



<!-- 内容区 -->
<div class="container">
  <!-- frozen/available -->
  <c:if test="${not empty sessionScope.buyerDao.getFrozenAndAvailableTradeIds()}">
  	<!-- 冻结 -->
	<div class="success-msg">您已选中订单${sessionScope.buyerDao.getFrozenAndAvailableOrderIds()}的购买人</div>
	<div class="success-msg">交易ID：${sessionScope.buyerDao.getFrozenAndAvailableTradeIds()}</div>
  	

  </c:if>
  

  <c:if test="${empty sessionScope.buyerDao.getFrozenAndAvailableTradeIds()}">
    <!-- 售罄 -->
  	<c:if test="${empty sessionScope.buyerDao.getFrozenAndAvailableOrderIds()}">
  		<h2 style="text-align:center; color:#e74c3c;">暂无购买人信息</h2>
  	</c:if>
  	
  	<!-- 可预订 -->
  	<c:if test="${not empty sessionScope.buyerDao.getFrozenAndAvailableOrderIds()}">
		<h2>意向购买人信息</h2>
      <table>
        <thead>
          <tr>
            <th>用户名</th>
            <th>订单编号</th>
            <th>交易地址</th>
            <th>联系电话</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${sessionScope.buyerList}" var="buyer">
            <tr>
              <td>${buyer.buyerName}</td>
              <td>${buyer.order_id}</td>
              <td>${buyer.address}</td>
              <td>${buyer.buyerPhoneNumber}</td>
              <td>
                <a href="${pageContext.request.contextPath}/BuyServlet?method=trade&orderId=${buyer.order_id}">
                  <button>与ta交易</button>
                </a>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
  	</c:if>
  </c:if>


</div>

</body>
</html>