package kz.greetgo.liqdb;

import java.lang.reflect.Method;

public class OrderProbe {
  
  public String asd() {
    return "fdsfdsafasfd";
  }
  
  public String dsa() {
    return "fdsfdsafasfd";
  }
  
  public static void main(String[] args) throws Exception {
    OrderProbe oo = new OrderProbe();
    
    for (Method m : oo.getClass().getMethods()) {
      System.out.println(m.getName());
      if ("asd".equals(m.getName())) {
        Object res = m.invoke(oo);
        System.out.println("res = " + res);
      }
    }
    
    System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");
  }
}
