package kz.greetgo.liqdb.util;

public class NeedImplement extends RuntimeException {
  
  public NeedImplement(Class<?> class1) {
    super("" + class1);
  }
  
  public NeedImplement(String methodName) {
    super("method " + methodName);
  }
}
