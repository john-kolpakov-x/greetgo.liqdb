package kz.greetgo.liqdb.impl;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Md5 {
  private static final String ZEROS = "0000000000000000000000000000000000";
  
  public static String sum(String str) {
    if (str == null) return "";
    
    try {
      MessageDigest m = MessageDigest.getInstance("MD5");
      m.reset();
      m.update(str.getBytes("UTF-8"));
      
      BigInteger bi = new BigInteger(1, m.digest());
      String hash = bi.toString(16);
      
      if (hash.length() >= 32) return hash;
      return ZEROS.substring(0, 32 - hash.length()) + hash;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    
  }
}
