<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ page import="datebase.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>购买历史记录</title>
    <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>
    <!-- Font Awesome -->
    <link href="https://cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" rel="stylesheet">

    <!-- 配置Tailwind自定义颜色和字体 -->
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    colors: {
                        primary: '#3B82F6',
                        secondary: '#6B7280',
                        success: '#10B981',
                        warning: '#F59E0B',
                        danger: '#EF4444',
                    },
                    fontFamily: {
                        sans: ['Inter', 'system-ui', 'sans-serif'],
                    },
                }
            }
        }
    </script>

    <!-- 自定义工具类 -->
    <style type="text/tailwindcss">
        @layer utilities {
            .content-auto {
                content-visibility: auto;
            }
            .shadow-custom {
                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
            }
            .transition-custom {
                transition: all 0.3s ease;
            }
        }
    </style>
</head>
<body class="bg-gray-50 font-sans text-gray-800">
<!-- 顶部导航栏 -->
<header class="bg-white shadow-sm sticky top-0 z-50 transition-all duration-300">
    <div class="container mx-auto px-4 py-4 flex justify-between items-center">
        <div class="flex items-center space-x-2">
            <i class="fa fa-history text-primary text-2xl"></i>
            <h1 class="text-xl md:text-2xl font-bold text-gray-800">购买历史记录</h1>
        </div>

        <div class="flex items-center space-x-4">
            <div class="relative hidden md:block">
                <form method="get" action="historyProducts" class="flex items-center">
                    <input type="text" name="search" placeholder="搜索订单号或商品名称..." value="${search}"
                           class="pl-10 pr-4 py-2 rounded-full border border-gray-200 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-custom w-64">
                    <input type="hidden" name="status" value="${status}">
                    <input type="hidden" name="date" value="${date}">
                    <i class="fa fa-search absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
                </form>
            </div>

            <button class="md:hidden text-gray-600 hover:text-primary transition-custom">
                <i class="fa fa-search text-xl"></i>
            </button>

            <div class="relative">
                <button class="flex items-center space-x-1 focus:outline-none group">
                    <span class="hidden md:inline font-medium">管理员</span>
                    <i class="fa fa-user-circle text-xl text-gray-600 group-hover:text-primary transition-custom"></i>
                </button>
            </div>
        </div>
    </div>
</header>

<!-- 主内容区 -->
<main class="container mx-auto px-4 py-8">
    <!-- 筛选和统计区域 -->
    <div class="bg-white rounded-xl shadow-custom p-6 mb-8 transform hover:shadow-lg transition-custom">
        <div class="flex flex-col md:flex-row justify-between items-start md:items-center mb-6">
            <div>
                <h2 class="text-lg font-semibold mb-1">交易记录统计</h2>
                <p class="text-gray-500 text-sm">查看和管理所有历史购买记录</p>
            </div>

            <div class="flex flex-wrap gap-3 mt-4 md:mt-0">
                <form method="get" action="historyProducts" class="flex flex-wrap gap-3">
                    <div class="relative">
                        <select name="status" class="pl-4 pr-10 py-2 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary appearance-none bg-white transition-custom">
                            <option value="">所有状态</option>
                            <option value="available" ${status == 'available' ? 'selected' : ''}>可预订</option>
                            <option value="sold" ${status == 'sold' ? 'selected' : ''}>已售罄</option>
                            <option value="frozen" ${status == 'frozen' ? 'selected' : ''}>已冻结</option>
                        </select>
                        <i class="fa fa-chevron-down absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400 pointer-events-none"></i>
                    </div>

                    <div class="relative flex items-center gap-2">
                        <input type="datetime-local" name="startTime" value="${startTime}" class="pl-4 pr-4 py-2 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-custom">
                        <span class="text-gray-400">至</span>
                        <input type="datetime-local" name="endTime" value="${endTime}" class="pl-4 pr-4 py-2 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary transition-custom">
                    </div>

                    <input type="hidden" name="search" value="${search}">
                    <button type="submit" class="bg-primary hover:bg-primary/90 text-white px-4 py-2 rounded-lg flex items-center space-x-1 transition-custom">
                        <i class="fa fa-filter"></i>
                        <span>筛选</span>
                    </button>
                </form>
            </div>
        </div>

    </div>

    <!-- 历史记录表格 -->
    <div class="bg-white rounded-xl shadow-custom overflow-hidden transform hover:shadow-lg transition-custom">
        <div class="overflow-x-auto">
            <table class="w-full">
                <thead>
                <tr class="bg-gray-50 border-b border-gray-200">
                    <th class="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">订单编号</th>
                    <th class="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">商品信息</th>
                    <th class="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">购买者</th>
                    <th class="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">价格</th>
                    <th class="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                    <th class="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">交易时间</th>
                    <th class="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
                </tr>
                </thead>
                <tbody class="divide-y divide-gray-200">
                <%
                    List<Order> orders = (List<Order>) request.getAttribute("orders");
                    if (orders != null && !orders.isEmpty()) {
                        for (Order order : orders) {
                %>
                <tr class="hover:bg-gray-50 transition-custom">
                    <td class="px-6 py-4 whitespace-nowrap">
                        <div class="text-sm font-medium text-gray-900"><%= order.getOrderNumber() %></div>
                        <div class="text-xs text-gray-500"><%= order.getTransactionId() %></div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                        <div class="flex items-center">
                            <div class="h-10 w-10 flex-shrink-0">
                                <img class="h-10 w-10 rounded-md object-cover" src="<%= order.getProduct().getImageUrl() %>" alt="<%= order.getProduct().getName() %>">
                            </div>
                            <div class="ml-4">
                                <div class="text-sm font-medium text-gray-900"><%= order.getProduct().getName() %></div>
                                <div class="text-xs text-gray-500 line-clamp-2 max-w-xs"><%= order.getProduct().getDescription() %></div>
                            </div>
                        </div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                        <div class="text-sm text-gray-900"><%= order.getUser().getName() %></div>
                        <div class="text-xs text-gray-500"><%= order.getUser().getMaskedPhone() %></div>
                        <div class="text-xs text-gray-500 truncate max-w-xs"><%= order.getUser().getAddress() %></div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                        <div class="text-sm font-medium text-gray-900"><%= order.getProduct().getFormattedPrice() %></div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                        <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full <%= order.getProduct().getStatusClass() %>">
                            <%= order.getStatus() %>
                        </span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        <div><%= order.getFormattedTransactionTime() %></div>
                        <div class="text-xs">提交: <%= order.getFormattedOrderTime() %></div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        <button class="text-primary hover:text-primary/80 mr-3 transition-custom view-detail"
                                title="查看详情" data-order-id="<%= order.getOrderNumber() %>">
                            <i class="fa fa-eye"></i>
                        </button>
                        <button class="text-gray-500 hover:text-gray-700 transition-custom" title="打印订单">
                            <i class="fa fa-print"></i>
                        </button>
                    </td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr>
                    <td colspan="7" class="px-6 py-12 text-center text-gray-500">
                        <div class="flex flex-col items-center">
                            <i class="fa fa-inbox text-4xl text-gray-300 mb-4"></i>
                            <p class="text-lg font-medium">暂无数据</p>
                            <p class="text-sm">当前筛选条件下没有找到相关记录</p>
                        </div>
                    </td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>

        <!-- 分页控件 -->
        <%
            Integer currentPage = (Integer) request.getAttribute("currentPage");
            Integer totalPages = (Integer) request.getAttribute("totalPages");
            Integer totalRecords = (Integer) request.getAttribute("totalRecords");
            Integer pageSize = (Integer) request.getAttribute("pageSize");
            String search = (String) request.getAttribute("search");
            String status = (String) request.getAttribute("status");
            String date = (String) request.getAttribute("date");

            if (currentPage == null) currentPage = 1;
            if (totalPages == null) totalPages = 1;
            if (totalRecords == null) totalRecords = 0;
            if (pageSize == null) pageSize = 10;
            if (search == null) search = "";
            if (status == null) status = "";
            if (date == null) date = "";

            int startRecord = (currentPage - 1) * pageSize + 1;
            int endRecord = Math.min(currentPage * pageSize, totalRecords);
        %>
        <div class="px-6 py-4 bg-gray-50 border-t border-gray-200 flex items-center justify-between">
            <div class="flex-1 flex justify-between sm:hidden">
                <% if (currentPage > 1) { %>
                <a href="HistoryProductsServlet?page=<%= currentPage - 1 %>&search=<%= java.net.URLEncoder.encode(search, "UTF-8") %>&status=<%= status %>&date=<%= date %>"
                   class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 transition-custom">
                    上一页
                </a>
                <% } else { %>
                <span class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-400 bg-gray-100 cursor-not-allowed">
                    上一页
                </span>
                <% } %>

                <% if (currentPage < totalPages) { %>
                <a href="HistoryProductsServlet?page=<%= currentPage + 1 %>&search=<%= java.net.URLEncoder.encode(search, "UTF-8") %>&status=<%= status %>&date=<%= date %>"
                   class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 transition-custom">
                    下一页
                </a>
                <% } else { %>
                <span class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-400 bg-gray-100 cursor-not-allowed">
                    下一页
                </span>
                <% } %>
            </div>
            <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
                <div>
                    <p class="text-sm text-gray-700">
                        显示第 <span class="font-medium"><%= startRecord %></span> 到 <span class="font-medium"><%= endRecord %></span> 条，共 <span class="font-medium"><%= totalRecords %></span> 条记录
                    </p>
                </div>
                <div>
                    <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
                        <% if (currentPage > 1) { %>
                        <a href="HistoryProductsServlet?page=<%= currentPage - 1 %>&search=<%= java.net.URLEncoder.encode(search, "UTF-8") %>&status=<%= status %>&date=<%= date %>"
                           class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 transition-custom">
                            <span class="sr-only">上一页</span>
                            <i class="fa fa-chevron-left text-xs"></i>
                        </a>
                        <% } else { %>
                        <span class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-gray-100 text-sm font-medium text-gray-400 cursor-not-allowed">
                            <span class="sr-only">上一页</span>
                            <i class="fa fa-chevron-left text-xs"></i>
                        </span>
                        <% } %>

                        <%
                            int startPage = Math.max(1, currentPage - 2);
                            int endPage = Math.min(totalPages, currentPage + 2);

                            if (startPage > 1) {
                        %>
                        <a href="HistoryProductsServlet?page=1&search=<%= java.net.URLEncoder.encode(search, "UTF-8") %>&status=<%= status %>&date=<%= date %>"
                           class="bg-white border-gray-300 text-gray-500 hover:bg-gray-50 relative inline-flex items-center px-4 py-2 border text-sm font-medium transition-custom">1</a>
                        <% if (startPage > 2) { %>
                        <span class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700">...</span>
                        <% } %>
                        <% } %>

                        <% for (int i = startPage; i <= endPage; i++) { %>
                        <% if (i == currentPage) { %>
                        <span class="z-10 bg-primary text-white relative inline-flex items-center px-4 py-2 border border-primary text-sm font-medium">
                            <%= i %>
                        </span>
                        <% } else { %>
                        <a href="HistoryProductsServlet?page=<%= i %>&search=<%= java.net.URLEncoder.encode(search, "UTF-8") %>&status=<%= status %>&date=<%= date %>"
                           class="bg-white border-gray-300 text-gray-500 hover:bg-gray-50 relative inline-flex items-center px-4 py-2 border text-sm font-medium transition-custom">
                            <%= i %>
                        </a>
                        <% } %>
                        <% } %>

                        <% if (endPage < totalPages) { %>
                        <% if (endPage < totalPages - 1) { %>
                        <span class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700">...</span>
                        <% } %>
                        <a href="HistoryProductsServlet?page=<%= totalPages %>&search=<%= java.net.URLEncoder.encode(search, "UTF-8") %>&status=<%= status %>&date=<%= date %>"
                           class="bg-white border-gray-300 text-gray-500 hover:bg-gray-50 relative inline-flex items-center px-4 py-2 border text-sm font-medium transition-custom"><%= totalPages %></a>
                        <% } %>

                        <% if (currentPage < totalPages) { %>
                        <a href="HistoryProductsServlet?page=<%= currentPage + 1 %>&search=<%= java.net.URLEncoder.encode(search, "UTF-8") %>&status=<%= status %>&date=<%= date %>"
                           class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 transition-custom">
                            <span class="sr-only">下一页</span>
                            <i class="fa fa-chevron-right text-xs"></i>
                        </a>
                        <% } else { %>
                        <span class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-gray-100 text-sm font-medium text-gray-400 cursor-not-allowed">
                            <span class="sr-only">下一页</span>
                            <i class="fa fa-chevron-right text-xs"></i>
                        </span>
                        <% } %>
                    </nav>
                </div>
            </div>
        </div>
    </div>
</main>

<!-- 页脚 -->
<footer class="bg-white border-t border-gray-200 mt-12">
    <div class="container mx-auto px-4 py-6">
        <div class="flex flex-col md:flex-row justify-between items-center">
            <div class="text-gray-500 text-sm mb-4 md:mb-0">
                © 2025 无艺  . 保留所有权利.
            </div>
            <div class="flex space-x-6">
                <a href="#" class="text-gray-400 hover:text-gray-500 transition-custom">
                    <span class="sr-only">使用条款</span>
                    <i class="fa fa-file-text-o"></i>
                </a>
                <a href="#" class="text-gray-400 hover:text-gray-500 transition-custom">
                    <span class="sr-only">隐私政策</span>
                    <i class="fa fa-shield"></i>
                </a>
                <a href="#" class="text-gray-400 hover:text-gray-500 transition-custom">
                    <span class="sr-only">联系我们</span>
                    <i class="fa fa-envelope"></i>
                </a>
            </div>
        </div>
    </div>
</footer>

<!-- 查看详情模态框 (默认隐藏) -->
<div id="detailModal" class="fixed inset-0 bg-black bg-opacity-50 z-50 hidden flex items-center justify-center p-4">
    <div class="bg-white rounded-xl shadow-xl w-full max-w-3xl max-h-[90vh] overflow-y-auto transform transition-all">
        <div class="p-6 border-b border-gray-200 flex justify-between items-center sticky top-0 bg-white z-10">
            <h3 class="text-xl font-bold text-gray-900">订单详情</h3>
            <button id="closeModal" class="text-gray-400 hover:text-gray-500 transition-custom">
                <i class="fa fa-times text-xl"></i>
            </button>
        </div>

        <div class="p-6">
            <div class="flex flex-col md:flex-row gap-6">
                <!-- 商品图片和信息 -->
                <div class="md:w-1/2">
                    <img src="https://picsum.photos/600/400?random=1" alt="山水画作" class="w-full h-64 object-cover rounded-lg mb-4">
                    <h4 class="text-lg font-semibold">山水画作</h4>
                    <p class="text-gray-600 text-sm mt-2">这幅山水画展现了壮丽的自然景观，采用传统水墨技法创作，画家花费三个月时间完成，具有很高的艺术价值和收藏价值。</p>

                    <div class="mt-4 flex items-center">
                            <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800 mr-3">
                                已售罄
                            </span>
                        <span class="text-gray-500 text-sm">作品编号: WK2023080123</span>
                    </div>
                </div>

                <!-- 订单信息 -->
                <div class="md:w-1/2">
                    <h4 class="text-lg font-semibold mb-4 pb-2 border-b border-gray-100">订单信息</h4>

                    <div class="space-y-3">
                        <div class="flex justify-between">
                            <span class="text-gray-500">订单编号:</span>
                            <span class="font-medium">DD00123</span>
                        </div>
                        <div class="flex justify-between">
                            <span class="text-gray-500">交易编号:</span>
                            <span class="font-medium">TD1628453762198456</span>
                        </div>
                        <div class="flex justify-between">
                            <span class="text-gray-500">订单提交时间:</span>
                            <span>2023-08-09 14:28:45</span>
                        </div>
                        <div class="flex justify-between">
                            <span class="text-gray-500">交易时间:</span>
                            <span>2023-08-09 14:30</span>
                        </div>
                        <div class="flex justify-between">
                            <span class="text-gray-500">价格:</span>
                            <span class="font-bold text-lg text-primary">¥2,800.00</span>
                        </div>
                    </div>

                    <h4 class="text-lg font-semibold mb-4 mt-6 pb-2 border-b border-gray-100">购买者信息</h4>

                    <div class="space-y-3">
                        <div class="flex justify-between">
                            <span class="text-gray-500">用户名:</span>
                            <span>张三</span>
                        </div>
                        <div class="flex justify-between">
                            <span class="text-gray-500">手机号:</span>
                            <span>138****5678</span>
                        </div>
                        <div class="flex justify-between">
                            <span class="text-gray-500">交易地址:</span>
                            <span class="text-right max-w-[150px]">北京市朝阳区建国路88号</span>
                        </div>
                    </div>

                    <div class="mt-8">
                        <button class="w-full bg-primary hover:bg-primary/90 text-white py-3 rounded-lg font-medium transition-custom flex items-center justify-center">
                            <i class="fa fa-print mr-2"></i> 打印订单详情
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    // 页面滚动时改变导航栏样式
    window.addEventListener('scroll', function() {
        const header = document.querySelector('header');
        if (window.scrollY > 10) {
            header.classList.add('py-2');
            header.classList.remove('py-4');
            header.classList.add('shadow');
        } else {
            header.classList.add('py-4');
            header.classList.remove('py-2');
            header.classList.remove('shadow');
        }
    });

    // 模态框控制
    document.addEventListener('DOMContentLoaded', function() {
        const modal = document.getElementById('detailModal');
        const closeBtn = document.getElementById('closeModal');
        const viewButtons = document.querySelectorAll('.view-detail');

        viewButtons.forEach(button => {
            button.addEventListener('click', function() {
                modal.classList.remove('hidden');
                document.body.style.overflow = 'hidden'; // 防止背景滚动
            });
        });

        closeBtn.addEventListener('click', function() {
            modal.classList.add('hidden');
            document.body.style.overflow = ''; // 恢复背景滚动
        });

        // 点击模态框外部关闭
        modal.addEventListener('click', function(e) {
            if (e.target === modal) {
                modal.classList.add('hidden');
                document.body.style.overflow = '';
            }
        });
    });
</script>
</body>
</html>
