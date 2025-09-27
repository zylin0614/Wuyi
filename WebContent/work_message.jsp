<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>作品详情</title>
    <style>
        body { margin: 0; font-family: sans-serif; }
        /* 统一导航栏样式 */
        .navbar {
            background-color: #f2f2f2;
            padding: 0 20px;
            border-bottom: 1px solid #ccc;
            overflow: hidden; /* 确保背景色填充整个区域 */
        }
        .navbar a {
            float: left; /* 从左侧开始排列 */
            display: block;
            color: #333;
            text-align: center;
            padding: 14px 20px;
            text-decoration: none;
            font-size: 16px;
            border: 1px solid transparent; /* 占位边框 */
            border-bottom: none;
        }
        .navbar a:hover {
            background-color: #ddd;
        }
        /* 当前选中页面的样式，实现 "框选" 效果 */
        .navbar a.active {
            background-color: white;
            color: black;
            border: 1px solid #ccc;
            border-bottom: 1px solid white; /* 关键：让边框与下方内容区融为一体 */
            position: relative;
            top: 1px;
            border-radius: 5px 5px 0 0;
        }
    </style>
</head>
<body>
    
    <!-- 更新后的导航栏 -->
    <div class="navbar">
        <a href="Work_message.jsp" class="active">查询商品</a>
        <a href="check_order.jsp">查询订单</a>
        <a href="merchant_login.jsp">商家登录</a>
    </div>

    <%--
        说明:
        1. 此页面用于展示从商家后端 (Servlet) 传递过来的单个作品信息。
    --%>
    <%
        // 从 request 中获取属性，如果不存在则使用默认值
        String work_name = (String) request.getAttribute("work_name");
        if (work_name == null) {
            work_name = "示例作品名称";
        }

        String work_description = (String) request.getAttribute("work_description");
        if (work_description == null) {
            work_description = "作品的详细描述，介绍作品的材质、尺寸、创作背景等信息。";
        }

        String work_image = (String) request.getAttribute("work_image");
        if (work_image == null) {
            work_image = "https://placehold.co/300x400?text=作品图片";
        }

        String work_price = (String) request.getAttribute("work_price");
        if (work_price == null) {
            work_price = "¥9,999.00";
        }

        String work_status = (String) request.getAttribute("work_status");
        if (work_status == null) {
            work_status = "available"; 
        }
    %>


    <table style="width:100%; max-width: 800px; border-spacing: 20px; margin: 20px auto; border-top: 1px solid #ccc; padding-top: 20px;">
        <tr>
            <!-- 左侧：作品图片 -->
            <td style="width: 300px; vertical-align: top;">
                <img src="<%= work_image %>" alt="作品图片" style="width: 100%; height: auto; display: block; border-radius: 8px;">
            </td>

            <!-- 右侧：作品信息和按钮 -->
            <td style="vertical-align: top;">
                <!-- 作品名称: 加粗黑体 -->
                <div style="font-weight: bold; font-family: 'SimHei', '黑体', sans-serif; font-size: 24px; color: black; margin-bottom: 10px;">
                    <%= work_name %>
                </div>

                <!-- 作品价格: 加粗红体 -->
                <div style="font-weight: bold; color: red; font-size: 20px; margin-bottom: 15px;">
                    <%= work_price %>
                </div>

                <!-- 作品描述: 正常大小灰体 -->
                <div style="color: grey; margin-bottom: 25px; line-height: 1.6;">
                    <%= work_description %>
                </div>

                <!-- 状态按钮 -->
                <div>
                    <%
                        // 根据 work_status 的值显示不同的按钮
                        if ("available".equals(work_status)) {
                    %>
                            <!-- 正常情况: 红底白字“可预订”，点击后进入用户信息填写页面 -->
                            <a href="buyer_message.jsp" style="text-decoration: none; background-color: #E53935; color: white; padding: 10px 25px; border-radius: 5px; font-size: 16px; display: inline-block;">
                                可预订
                            </a>
                    <%
                        } else if ("frozen".equals(work_status)) {
                    %>
                            <!-- 冻结: 浅灰底白字“不可预订”，不可点击 -->
                            <button style="background-color: #BDBDBD; color: white; padding: 10px 25px; border: none; border-radius: 5px; font-size: 16px; cursor: not-allowed;" disabled>
                                不可预订
                            </button>
                    <%
                        } else if ("sold".equals(work_status)) {
                    %>
                             <!-- 交易成功: 深灰底白字“售罄”，不可点击 -->
                            <button style="background-color: #616161; color: white; padding: 10px 25px; border: none; border-radius: 5px; font-size: 16px; cursor: not-allowed;" disabled>
                                售罄
                            </button>
                    <%
                        }
                    %>
                </div>
            </td>
        </tr>
    </table>

</body>
</html>

