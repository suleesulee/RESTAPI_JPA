package com.example.jpa.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCrypt;

@UtilityClass
public class PasswordUtils {


    public static String encryptedPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


    //패스워드를 암호화 해서 리턴하는 메서드, 입력받은 패스워드와 해시 패스워드와 비교
    public static boolean equalPassword(String password, String encryptedPassword) {
        return BCrypt.checkpw(password, encryptedPassword);
    }

}
