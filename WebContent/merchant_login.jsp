<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8" />
    <title>商家登录</title>
    <style>
        body { font-family: Arial, Helvetica, sans-serif; margin: 24px; }
        .container { max-width: 420px; margin: 0 auto; }
        label { display: block; margin-top: 12px; }
        input[type=text], input[type=password] { width: 100%; padding: 8px; }
        button { margin-top: 16px; padding: 8px 14px; background: #1976d2; color: #fff; border: none; border-radius: 4px; }
        .error { color: #d32f2f; }
    </style>
</head>
<body>
<div class="container">
    <h2>商家登录</h2>
    <form action="merchantLogin" method="post">
        <label>商家名：
            <input type="text" name="merchantName" required />
        </label>
        <label>密码：
            <input type="password" name="merchantPassword" required />
        </label>
        <button type="submit">登录</button>
    </form>
    <p class="error">${error}</p>
</div>
</body>
</html>


