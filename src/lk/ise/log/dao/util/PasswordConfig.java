package lk.ise.log.dao.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordConfig {
    public String encryptPassword(String rowPassword) {
        return BCrypt.hashpw(rowPassword, BCrypt.gensalt());
    }
}



