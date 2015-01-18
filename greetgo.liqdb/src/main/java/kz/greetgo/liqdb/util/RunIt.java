package kz.greetgo.liqdb.util;

public abstract class RunIt implements Runnable {
  
  @Override
  public void run() {
    try {
      runit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  protected abstract void runit() throws Exception;
  
}
