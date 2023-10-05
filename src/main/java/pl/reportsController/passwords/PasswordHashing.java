/*
    Hashing class

 */

package pl.reportsController.passwords;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.random.RandomGenerator;

public class PasswordHashing {
    public static String HashPassword(String password){
        return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    }

    public static String generateRandomPasswordForUser(){
        return UUID.randomUUID().toString().replaceAll("-","").substring(0,10);
    }
}
