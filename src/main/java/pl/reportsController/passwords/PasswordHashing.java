/*
    Hashing class

 */

package pl.reportsController.passwords;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;

public class PasswordHashing {
    public static String HashPassword(String password){
        return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    }
}
