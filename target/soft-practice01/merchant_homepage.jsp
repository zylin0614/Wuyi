<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>商家功能首页</title>
<style>
    body {
        font-family: "Microsoft YaHei", Arial, sans-serif;
        margin: 0;
        padding: 0;
        background: #f5f7fa;
    }

    /* 顶部导航栏 */
    .navbar {
        background: #2c3e50;
        color: #fff;
        padding: 15px 30px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        box-shadow: 0 2px 6px rgba(0,0,0,0.2);
    }
    .navbar .title {
        font-size: 20px;
        font-weight: bold;
    }
    .navbar .actions a {
        color: #fff;
        text-decoration: none;
        margin-left: 20px;
        font-size: 14px;
    }
    .navbar .actions a:hover {
        text-decoration: underline;
    }

    /* 主体功能区 (2x2 网格) */
    .container {
        width: 70%;
        margin: 50px auto;
        display: grid;
        grid-template-columns: repeat(2, 1fr); /* 固定两列 */
        grid-gap: 40px 60px; /* 行间距40px，列间距60px */
        justify-items: center; /* 水平居中 */
    }

    .card {
        background: #fff;
        border-radius: 10px;
        padding: 30px;
        width: 220px;
        text-align: center;
        box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        transition: transform 0.2s ease, box-shadow 0.2s ease;
    }
    .card:hover {
        transform: translateY(-5px);
        box-shadow: 0 6px 16px rgba(0,0,0,0.15);
    }
    .card h3 {
        margin-bottom: 20px;
        font-size: 18px;
        color: #2c3e50;
    }
    .card a button {
        padding: 10px 20px;
        border: none;
        border-radius: 6px;
        background: #3498db;
        color: #fff;
        font-size: 14px;
        cursor: pointer;
        transition: background 0.3s ease;
    }
    .card a button:hover {
        background: #2980b9;
    }
</style>
</head>
<body>

<!-- 顶部导航栏 -->
<div class="navbar">
    <div class="title">商家功能首页</div>
    <div class="actions">
        <a href="javascript:location.reload();">刷新</a>
        <a href="${pageContext.request.contextPath}/LogoutServlet">注销</a>
    </div>
</div>

<!-- 功能卡片区 (2x2) -->
<div class="container">
    <!-- 查看意向购买人信息 -->
    <div class="card">
        <h3>查看意向购买人信息</h3>
        <a href="${pageContext.request.contextPath}/BuyServlet">
            <button>进入</button>
        </a>
    </div>

    <!-- 修改密码 -->
    <div class="card">
        <h3>修改密码</h3>
        <a href="${pageContext.request.contextPath}/merchantpassword_change.jsp">
            <button>进入</button>
        </a>
    </div>

    <!-- 查看历史商品 -->
    <div class="card">
        <h3>查看历史商品</h3>
        <a href="${pageContext.request.contextPath}/HistoryProductsServlet">
            <button>进入</button>
        </a>
    </div>

    <!-- 修改商品状态 -->
    <div class="card">
        <h3>修改商品状态</h3>
        <a href="${pageContext.request.contextPath}/get_workServlet">
            <button>进入</button>
        </a>
    </div>
</div>

</body>
</html>
