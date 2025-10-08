package datebase;

import datebase.Merchant;

public interface MerchantDao {
    Merchant findByName(String name);
    boolean updatePassword(String name, String newPassword);
}


