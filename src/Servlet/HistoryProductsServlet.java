package Servlet;

import datebase.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 历史购买记录列表页控制器。
 *
 * 功能概述：
 * 1) 连接 SQL Server 数据库（库名 Wuyi），联表查询 Reservation/Works/Trade 三张表
 * 2) 支持条件筛选：关键字（订单号/商品名）、商品状态（available|frozen|sold）、时间范围（交易时间）
 * 3) 支持分页（SQL Server OFFSET/FETCH）
 * 4) 将结果集映射为领域模型 Order/Product/User，并计算基础统计数据
 * 5) 将数据放入 Request Attribute，转发到 JSP 进行展示
 */
@WebServlet("/historyProducts")
public class HistoryProductsServlet extends HttpServlet {

    /**
     * 前端 <input type="datetime-local"> 的时间格式（示例：2025-09-25T14:30）
     */
    private static final DateTimeFormatter INPUT_DT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    /** 数据库存储在 Reservation 表中的字符串时间格式（优先包含秒） */
    private static final DateTimeFormatter DB_DT_SECOND = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /** 数据库存储在 Reservation 表中的字符串时间格式（仅到分钟） */
    private static final DateTimeFormatter DB_DT_MINUTE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * 处理 GET：读取查询条件，执行统计与分页查询，转发到 JSP。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        // 读取筛选与分页参数（全部可选）
        String search = request.getParameter("search"); // 订单号或商品名称
        String status = request.getParameter("status"); // available|frozen|sold
        String startTimeParam = request.getParameter("startTime"); // yyyy-MM-dd'T'HH:mm
        String endTimeParam = request.getParameter("endTime");   // yyyy-MM-dd'T'HH:mm
        String pageParam = request.getParameter("page");
        String pageSizeParam = request.getParameter("pageSize");
        
        // 解析分页参数（默认第1页，每页10条）
        int page = 1;
        int pageSize = 10;
        
        try {
            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
            }
            if (pageSizeParam != null && !pageSizeParam.isEmpty()) {
                pageSize = Integer.parseInt(pageSizeParam);
            }
        } catch (NumberFormatException e) {
            // 忽略错误，保留默认分页
        }
        // 解析起止时间（为空则不做时间过滤）
        LocalDateTime startTime = parseDateTime(startTimeParam);
        LocalDateTime endTime = parseDateTime(endTimeParam);

        // 先统计总记录数（用于计算总页数），再按分页查询当前页数据
        int totalRecords = countOrders(search, status, startTime, endTime);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        List<Order> pagedOrders = queryOrders(search, status, startTime, endTime, page, pageSize);

        // 计算统计数据（此处基于当前页数据；如需全量统计，可改为基于无分页查询）
        Statistics statistics = calculateStatistics(pagedOrders, totalRecords);
        
        // 将数据与查询条件回传到前端
        request.setAttribute("orders", pagedOrders);
        request.setAttribute("statistics", statistics);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("search", search != null ? search : "");
        request.setAttribute("status", status != null ? status : "");
        request.setAttribute("startTime", startTimeParam != null ? startTimeParam : "");
        request.setAttribute("endTime", endTimeParam != null ? endTimeParam : "");
        
        // 转发到列表 JSP
        request.getRequestDispatcher("history_products.jsp").forward(request, response);
    }

    /**
     * 处理 POST：将表单条件拼接到查询字符串，重定向到 GET，避免表单重复提交。
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        // 读取表单参数
        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        
        // 构建重定向 URL（仅拼接非空参数）
        StringBuilder redirectUrl = new StringBuilder("historyProducts?");
        if (search != null && !search.isEmpty()) {
            redirectUrl.append("search=").append(java.net.URLEncoder.encode(search, "UTF-8")).append("&");
        }
        if (status != null && !status.isEmpty()) {
            redirectUrl.append("status=").append(status).append("&");
        }
        if (startTime != null && !startTime.isEmpty()) {
            redirectUrl.append("startTime=").append(java.net.URLEncoder.encode(startTime, "UTF-8")).append("&");
        }
        if (endTime != null && !endTime.isEmpty()) {
            redirectUrl.append("endTime=").append(java.net.URLEncoder.encode(endTime, "UTF-8")).append("&");
        }
        
        // 重定向到 GET 方法
        response.sendRedirect(redirectUrl.toString());
    }

    /**
     * 解析前端 datetime-local 字符串为 LocalDateTime。
     * @param param 形如 2025-09-25T14:30
     * @return 解析成功则返回时间，失败或为空返回 null
     */
    private LocalDateTime parseDateTime(String param) {
        if (param == null || param.trim().isEmpty()) return null;
        try {
            return LocalDateTime.parse(param, INPUT_DT);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析数据库中的 varchar 日期时间（优先带秒，其次到分钟）。
     */
    private LocalDateTime parseDbDateTime(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        String v = value.trim();
        try {
            return LocalDateTime.parse(v, DB_DT_SECOND);
        } catch (Exception ignore) { }
        try {
            return LocalDateTime.parse(v, DB_DT_MINUTE);
        } catch (Exception ignore) { }
        // 最后尝试让 SQL Server 驱动输出的 Timestamp 再兜底（若字段类型被改为 datetime）
        try {
            Timestamp ts = Timestamp.valueOf(v);
            return ts.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } catch (Exception ignore) { }
        return null;
    }

    /**
     * 统计满足筛选条件的总记录数。
     */
    private int countOrders(String search, String status, LocalDateTime startTime, LocalDateTime endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) FROM Reservation r ")
           .append("JOIN Works w ON r.work_id = w.work_id ")
           .append("LEFT JOIN Trade t ON r.order_id = t.order_id ")
           .append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();
        appendFilters(sql, params, search, status, startTime, endTime);

        try (Connection conn = DbUtil.getCon();
             PreparedStatement ps = prepare(conn, sql.toString(), params)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            throw new RuntimeException("Count query failed", e);
        }
        return 0;
    }

    /**
     * 分页查询订单明细数据，并映射为领域对象。
     */
    private List<Order> queryOrders(String search, String status, LocalDateTime startTime, LocalDateTime endTime,
                                    int page, int pageSize) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT r.order_id, r.buyer_name, r.buyer_phonenumber, r.trading_address, r.trading_time, r.order_time, r.work_id, ")
           .append("w.work_name, w.work_description, w.work_image, w.work_price, w.work_status, ")
           .append("t.trade_id ")
           .append("FROM Reservation r ")
           .append("JOIN Works w ON r.work_id = w.work_id ")
           .append("LEFT JOIN Trade t ON r.order_id = t.order_id ")
           .append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();
        appendFilters(sql, params, search, status, startTime, endTime);
        sql.append(" ORDER BY r.trading_time DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        int offset = (page - 1) * pageSize;
        params.add(offset);
        params.add(pageSize);

        List<Order> result = new ArrayList<>();
        try (Connection conn = DbUtil.getCon();
             PreparedStatement ps = prepare(conn, sql.toString(), params)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRowToOrder(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Paged query failed", e);
        }
        return result;
    }

    /**
     * 将公共筛选逻辑拼接到 SQL，并按顺序填充参数列表。
     */
    private void appendFilters(StringBuilder sql, List<Object> params, String search, String status,
                               LocalDateTime startTime, LocalDateTime endTime) {
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (r.order_id LIKE ? OR w.work_name LIKE ?) ");
            String like = "%" + search.trim() + "%";
            params.add(like);
            params.add(like);
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND w.work_status = ? ");
            params.add(status.trim());
        }
        if (startTime != null) {
            // 将 varchar 转换为 datetime 再比较，避免直接与 Timestamp 比较导致转换错误
            sql.append(" AND TRY_CONVERT(datetime, r.trading_time, 120) >= ? ");
            params.add(Timestamp.valueOf(startTime));
        }
        if (endTime != null) {
            sql.append(" AND TRY_CONVERT(datetime, r.trading_time, 120) <= ? ");
            params.add(Timestamp.valueOf(endTime));
        }
    }

    /**
     * 根据参数列表顺序绑定 PreparedStatement。
     */
    private PreparedStatement prepare(Connection conn, String sql, List<Object> params) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }
        return ps;
    }

    /**
     * 将一行 ResultSet 映射为领域模型 Order（包含内嵌的 Product 与 User）。
     */
    private Order mapRowToOrder(ResultSet rs) throws SQLException {
        String orderId = rs.getString("order_id");
        String tradeId = rs.getString("trade_id");

        String workName = rs.getString("work_name");
        String workDesc = rs.getString("work_description");
        String workImage = rs.getString("work_image");
        String workStatusEn = rs.getString("work_status");
        String workIdStr = String.valueOf(rs.getInt("work_id"));
        double workPrice; // work_price 可能为 NUMERIC/DECIMAL/字符串，这里做一次安全解析
        try {
            workPrice = rs.getObject("work_price") == null ? 0.0 : Double.parseDouble(rs.getObject("work_price").toString());
        } catch (Exception e) {
            workPrice = 0.0;
        }

        Product product = new Product(workIdStr, workName, workDesc, workImage, workPrice, mapStatusToZh(workStatusEn));

        String buyerName = rs.getString("buyer_name");
        String buyerPhone = rs.getString("buyer_phonenumber");
        String address = rs.getString("trading_address");
        User user = new User(buyerName, buyerPhone, address);

        // 注意：Reservation 中这两个时间字段是 varchar 类型，此处以字符串读取，再尝试按两种常见格式解析
        String orderTimeStr = rs.getString("order_time");
        String tradingTimeStr = rs.getString("trading_time");
        LocalDateTime orderTime = parseDbDateTime(orderTimeStr);
        LocalDateTime tradingTime = parseDbDateTime(tradingTimeStr);

        String statusZh = mapStatusToZh(workStatusEn);
        return new Order(orderId, tradeId, product, user, orderTime, tradingTime, statusZh);
    }

    /**
     * 将库存状态从英文（DB 存储）映射为中文（前端展示）。
     */
    private String mapStatusToZh(String status) {
        if (status == null) return "";
        switch (status) {
            case "available":
                return "可预订";
            case "frozen":
                return "已冻结";
            case "sold":
                return "已售罄";
            default:
                return status;
        }
    }

    /**
     * 计算统计数据：总记录数、当前页的已售数量与总金额。
     * 注意：当前实现以“当前页数据”为基数进行金额与已售数统计，
     * 如需统计全量数据，可改为基于不分页的查询结果。
     */
    private Statistics calculateStatistics(List<Order> pageOrders, int totalRecords) {
        int soldProducts = (int) pageOrders.stream().filter(o -> "已售罄".equals(o.getStatus())).count();
        double totalAmount = pageOrders.stream().mapToDouble(o -> o.getProduct().getPrice()).sum();
        double monthlyGrowth = 0.0; // 未提供基准数据，先设为0
        double soldGrowth = 0.0;
        double amountGrowth = 0.0;
        return new Statistics(totalRecords, soldProducts, totalAmount, monthlyGrowth, soldGrowth, amountGrowth);
    }
}