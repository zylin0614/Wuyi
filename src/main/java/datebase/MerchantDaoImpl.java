package datebase;

import datebase.MerchantDao;
import datebase.Merchant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MerchantDaoImpl implements MerchantDao {
    @Override
    public Merchant findByName(String name) {
        String sql = "SELECT merchant_name, merchant_password FROM Merchant WHERE merchant_name=?";
        try (Connection conn = DbUtil.getCon();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Merchant m = new Merchant();
                    m.setMerchantName(rs.getString("merchant_name"));
                    m.setMerchantPassword(rs.getString("merchant_password"));
                    return m;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean updatePassword(String name, String newPassword) {
        String sql = "UPDATE Merchant SET merchant_password=? WHERE merchant_name=?";
        try (Connection conn = DbUtil.getCon();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setString(2, name);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


