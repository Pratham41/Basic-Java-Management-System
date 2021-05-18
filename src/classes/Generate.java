package classes;

import java.util.Random;

public class Generate {
    public static String getotp(){
        Random abc = new Random();
        String otp = new String();
            for (int i = 0; i < 6; i++) {
                otp = otp + abc.nextInt(10);
            }
        return otp;
    }
    
}
