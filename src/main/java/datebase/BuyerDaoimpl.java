package datebase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * BuyerDao接口实现类（SQL Server 版本）
 * 适配表结构：Reservation(买家信息)、Trade(交易信息)、Works(商品信息)
 * 核心功能：1.查询所有购买人 2.按order_id查用户 3.生成trade_id存入数据库
 */
public class BuyerDaoimpl implements BuyerDao {
    // ==================== SQL Server 数据库配置 ====================
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Wuyi;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "123456";
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    // ==================== 静态代码块：加载SQL Server驱动 ====================
    static {
        try {
            Class.forName(DRIVER);
            System.out.println("SQL Server 驱动加载成功！");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQL Server 驱动加载失败！请检查驱动包是否引入", e);
        }
    }

    /**
     * 工具方法：获取SQL Server数据库连接
     */
    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("SQL Server 数据库连接成功！");
        return conn;
    }

    /**
     * 修复：关闭数据库资源（仅关闭已使用的资源，避免误关连接）
     * 关键：只关闭传入的非null资源，事务中仅在最后关闭连接
     */
    private void closeResource(ResultSet rs, PreparedStatement ps, Connection conn) {
        try {
            // 1. 先关ResultSet（依赖PreparedStatement）
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            // 2. 再关PreparedStatement（依赖Connection）
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
            // 3. 最后关Connection（仅在事务结束或查询完成后调用）
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("SQL Server 数据库连接已关闭！");
            }
        } catch (SQLException e) {
            System.err.println("关闭数据库资源异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==================== 功能1：查询所有购买人信息 ====================
    @Override
    public List<Buyer> showAllBuyers(List<String> frozenavailableOrderIds) throws SQLException {
        List<Buyer> buyerList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            // 基础SQL（保留原字段和表）
            String sql = "SELECT buyer_name, buyer_phonenumber, trading_address, " +
                         "trading_time, order_id, order_time, work_id " +
                         "FROM Reservation ";
            
            // 处理批量订单ID查询条件
            if (frozenavailableOrderIds != null && !frozenavailableOrderIds.isEmpty()) {
                // 生成与订单ID数量匹配的占位符（例如：? ,? ,?）
                StringBuilder placeholders = new StringBuilder();
                for (int i = 0; i < frozenavailableOrderIds.size(); i++) {
                    if (i > 0) {
                        placeholders.append(", ");
                    }
                    placeholders.append("?");
                }
                sql += "WHERE order_id IN (" + placeholders + ") ";
            }
            sql += "ORDER BY order_time DESC"; // 保留排序
            
            ps = conn.prepareStatement(sql);
            
            // 设置批量参数（为每个订单ID绑定占位符）
            if (frozenavailableOrderIds != null && !frozenavailableOrderIds.isEmpty()) {
                for (int i = 0; i < frozenavailableOrderIds.size(); i++) {
                    ps.setString(i + 1, frozenavailableOrderIds.get(i).trim());
                }
            }
            
            rs = ps.executeQuery();
            
            Buyer buyer = null;
            while (rs.next()) {
                buyer = new Buyer();
                buyer.setBuyerName(rs.getString("buyer_name"));
                buyer.setBuyerPhoneNumber(rs.getString("buyer_phonenumber"));
                buyer.setaddress(rs.getString("trading_address"));
                buyer.setTradeTime(rs.getString("trading_time"));
                buyer.setOrder_id(rs.getString("order_id"));
                buyer.setOrder_time(rs.getString("order_time"));
                buyer.setWork_id(rs.getInt("work_id"));
                buyerList.add(buyer);
            }
            
            // 打印查询结果提示
            if (frozenavailableOrderIds != null && !frozenavailableOrderIds.isEmpty()) {
                System.out.println("查询订单ID列表的购买人信息，共" + buyerList.size() + "条");
            } else {
                System.out.println("查询所有购买人信息，共" + buyerList.size() + "条");
            }
            
            // 保持原有逻辑：无数据时返回null
            if (buyerList.size() == 0) {
                buyerList = null;
            }

        } catch (SQLException e) {
            System.err.println("批量查询购买人异常：" + e.getMessage());
            throw e;
        } finally {
            // 关闭资源
            closeResource(rs, ps, conn);
        }

        return buyerList;
    }

    // ==================== 功能2：根据order_id查询单个购买人信息 ====================
    @Override
    public Buyer getBuyerByOrderId(String orderId) throws SQLException {
        if (orderId == null || !orderId.matches("^[a-zA-Z]{2}\\d{5}$")) {
            throw new IllegalArgumentException("订单编号格式错误！需符合DD00001格式（前2位字母，后5位数字）");
        }

        Buyer buyer = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            // 移除与Trade表的关联和trade_id字段查询
            String sql = "SELECT r.buyer_name, r.buyer_phonenumber, r.trading_address, " +
                         "r.trading_time, r.order_id, r.order_time, r.work_id " +
                         "FROM Reservation r " +
                         "WHERE r.order_id = ?";
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, orderId);
            rs = ps.executeQuery();

            if (rs.next()) {
                buyer = new Buyer();
                buyer.setBuyerName(rs.getString("buyer_name"));
                buyer.setBuyerPhoneNumber(rs.getString("buyer_phonenumber"));
                buyer.setaddress(rs.getString("trading_address"));
                buyer.setTradeTime(rs.getString("trading_time"));
                buyer.setOrder_id(rs.getString("order_id"));
                buyer.setOrder_time(rs.getString("order_time"));
                buyer.setWork_id(rs.getInt("work_id"));
                

                System.out.println("根据order_id=" + orderId + "查询到用户：" + buyer.getBuyerName() +
                                   "，商品编号：" + String.format("%04d", buyer.getWork_id()));
            } else {
                System.out.println("根据order_id=" + orderId + "未查询到任何用户");
            }

        } catch (SQLException e) {
            System.err.println("根据order_id查询用户异常：" + e.getMessage());
            throw e;
        } finally {
            closeResource(rs, ps, conn);
        }

        return buyer;
    }
        

    // ==================== 功能3：生成trade_id并创建Trade表数据 ====================
    @Override
    public int Trade(String orderId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null; // 单个ps复用，避免频繁创建/关闭
        ResultSet rs = null;
        int affectedRows = -1;

        try {
            // 1. 获取连接并开启事务
            conn = getConnection();
            conn.setAutoCommit(false);

            // 2. 根据order_id查work_id（复用ps，不重复关闭）
            String queryWorkIdSql = "SELECT work_id FROM Reservation WHERE order_id = ?";
            ps = conn.prepareStatement(queryWorkIdSql);
            ps.setString(1, orderId);
            rs = ps.executeQuery();

            if (!rs.next()) {
                conn.rollback();
                System.out.println("根据order_id=" + orderId + "未查询到关联订单");
                return 0;
            }
            int workId = rs.getInt("work_id");
            
            // 仅关闭rs（ps后续复用，conn事务中不关闭）
            if (rs != null) rs.close();

            // 3. 根据work_id查商品状态（复用ps，重新赋值SQL）
            String queryStatusSql = "SELECT work_status FROM Works WHERE work_id = ?";
            ps.close(); // 关闭上一个ps，避免资源冲突
            ps = conn.prepareStatement(queryStatusSql);
            ps.setInt(1, workId);
            rs = ps.executeQuery();

            if (!rs.next()) {
                conn.rollback();
                throw new SQLException("根据work_id=" + workId + "未查询到对应商品");
            }
            String originalWorkStatus = rs.getString("work_status");
            if (rs != null) rs.close();

            // 4. 更新商品状态为frozen（复用ps）
            String updateStatusSql = "UPDATE Works SET work_status = 'frozen' WHERE work_id = ?";
            ps.close();
            ps = conn.prepareStatement(updateStatusSql);
            ps.setInt(1, workId);
            int updateRows = ps.executeUpdate();

            if (updateRows != 1) {
                conn.rollback();
                throw new SQLException("更新商品状态失败：work_id=" + workId + "，影响行数=" + updateRows);
            }
            System.out.println("商品状态更新完成：work_id=" + workId + "，原始状态=" + originalWorkStatus + "，新状态=frozen");

            // 5. 生成trade_id（修复：避免随机数重复，增加时间戳唯一性）
            String prefix = "TD";
            long timestamp = System.currentTimeMillis();
            int randomNum = (int) (Math.random() * 1000);
            String tradeId = prefix + timestamp + String.format("%03d", randomNum);
            System.out.println("为order_id=" + orderId + "生成trade_id：" + tradeId);

            // 6. 插入Trade表（确保work_status非NULL）
            String insertTradeSql = "INSERT INTO Trade (trade_id, order_id, work_id, work_status) " +
                                    "VALUES (?, ?, ?, ?)";
            ps.close();
            ps = conn.prepareStatement(insertTradeSql);
            // 明确设置4个参数，确保无NULL
            ps.setString(1, tradeId);
            ps.setString(2, orderId);
            ps.setInt(3, workId);
            ps.setString(4, "frozen"); // 硬编码确保非NULL，与更新后状态一致
            affectedRows = ps.executeUpdate();

            // 7. 事务提交/回滚
            if (affectedRows == 1) {
                conn.commit();
                System.out.println("Trade表插入成功：trade_id=" + tradeId + "，关联order_id=" + orderId + "，状态=frozen");
            } else {
                conn.rollback();
                System.out.println("Trade表插入失败：affectedRows=" + affectedRows + "，已回滚事务");
            }

        } catch (SQLException e) {
            // 异常回滚
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.rollback();
                    System.err.println("事务异常回滚完成");
                } catch (SQLException ex) {
                    System.err.println("事务回滚失败：" + ex.getMessage());
                }
            }
            System.err.println("Trade操作异常：" + e.getMessage());
            throw e;
        } finally {
            // 最终关闭所有资源（事务结束）
            closeResource(rs, ps, conn);
        }

        return affectedRows;
    }
    
 // ==================== 功能4 ====================
    /**
     * 查询Trade表中状态为冻结(frozen)的所有trade_id
     * @return 冻结状态的trade_id列表，无数据则返回空列表
     * @throws SQLException 数据库查询异常
     */
    @Override
    public List<String> getFrozenAndAvailableTradeIds() throws SQLException {
        List<String> tradeIds = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            // 查询状态为frozen或available的所有trade_id
            String sql = "SELECT trade_id FROM Trade WHERE work_status IN ('frozen', 'available')";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            // 遍历结果集，收集trade_id
            while (rs.next()) {
                tradeIds.add(rs.getString("trade_id"));
            }
            System.out.println("查询到" + tradeIds.size() + "条状态为冻结或可用的trade_id");

            // 无数据时返回null，有数据时返回列表（保持原逻辑）
            return tradeIds.isEmpty() ? null : tradeIds;

        } catch (SQLException e) {
            System.err.println("查询冻结或可用状态trade_id异常：" + e.getMessage());
            throw e;
        } finally {
            closeResource(rs, ps, conn);
        }
    }
    
 // ==================== 功能5====================

    /**
     * 查询Trade表中状态为冻结(frozen\available)的所有order_id
     * @return 冻结状态的order_id列表，无数据则返回空列表
     * @throws SQLException 数据库查询异常
     */
    @Override
    public List<String> getFrozenAndAvailableOrderIds() throws SQLException {
        List<String> orderIds = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            // 先从 Works 里找出符合条件的 work_id，再到 Reservation 里查 order_id
            String sql = "SELECT DISTINCT r.order_id " +
                         "FROM Reservation r " +
                         "WHERE r.work_id IN ( " +
                         "    SELECT w.work_id " +
                         "    FROM Works w " +
                         "    WHERE w.work_status IN ('frozen', 'available')" +
                         ")";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                orderIds.add(rs.getString("order_id"));
            }
            System.out.println("查询到" + orderIds.size() + " 条状态为冻结或可用的 order_id");

            return orderIds.isEmpty() ? null : orderIds;

        } catch (SQLException e) {
            System.err.println("查询冻结或可用状态 order_id 异常：" + e.getMessage());
            throw e;
        } finally {
            closeResource(rs, ps, conn);
        }
    }

    
    /**
     * 根据订单ID查询Works表中的商品状态
     * @param orderId 订单ID
     * @return 商品状态，如果未查询到返回null
     * @throws SQLException 数据库操作异常
     */
    @Override
    public String getWorkStatusByOrderId(List<String> frozenavailableOrderIds) throws SQLException {
        // 检查订单ID列表是否为空
        if (frozenavailableOrderIds == null || frozenavailableOrderIds.isEmpty()) {
            System.out.println("订单ID列表为空，无法查询商品状态");
            return null;
        }
        
        // 获取列表中的第一个订单ID
        String firstOrderId = frozenavailableOrderIds.get(0);
        System.out.println("获取第一个订单ID: " + firstOrderId + " 的商品状态");
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String workStatus = null;
        String workId = null;

        try {
            conn = getConnection();
            
            // 第一步：通过order_id在Reservation表查询work_id
            String sqlReservation = "SELECT work_id FROM Reservation WHERE order_id = ?";
            ps = conn.prepareStatement(sqlReservation);
            ps.setString(1, firstOrderId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                workId = rs.getString("work_id");
                System.out.println("订单ID: " + firstOrderId + " 对应的work_id为: " + workId);
                
                // 第二步：通过work_id在Works表查询work_status
                String sqlWorks = "SELECT work_status FROM Works WHERE work_id = ?";
                // 关闭上一个ResultSet和PreparedStatement
                rs.close();
                ps.close();
                
                ps = conn.prepareStatement(sqlWorks);
                ps.setString(1, workId);
                rs = ps.executeQuery();
                
                if (rs.next()) {
                    workStatus = rs.getString("work_status");
                    System.out.println("work_id: " + workId + " 对应的商品状态为: " + workStatus);
                } else {
                    System.out.println("未查询到work_id: " + workId + " 对应的商品状态");
                }
            } else {
                System.out.println("未查询到订单ID: " + firstOrderId + " 对应的work_id");
            }

            return workStatus;

        } catch (SQLException e) {
            System.err.println("查询商品状态异常：" + e.getMessage());
            throw e;
        } finally {
            closeResource(rs, ps, conn); // 复用已有的资源关闭方法
        }
    }
    
        
    
}