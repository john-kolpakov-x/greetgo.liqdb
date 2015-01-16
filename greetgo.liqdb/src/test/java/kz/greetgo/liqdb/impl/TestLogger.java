package kz.greetgo.liqdb.impl;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import kz.greetgo.liqdb.Changelog;
import kz.greetgo.liqdb.log.Logger;

public class TestLogger implements Logger {
  
  private PrintStream out;
  
  public TestLogger(PrintStream out) {
    this.out = out;
  }
  
  public final List<String> list = new ArrayList<>();
  
  @Override
  public void executionTime(long millisPeriod, Changelog change) {
    list.add(pr("executionTime: millis " + millisPeriod + ", author " + change.author() + ", id "
        + change.id()));
  }
  
  @Override
  public void executionError(long millisPeriod, Changelog change, Throwable error) {
    list.add(pr("executionError: millis " + millisPeriod + ", author " + change.author() + ", id "
        + change.id() + ", errorClass " + error.getClass().getSimpleName() + ", errorMessage "
        + error.getMessage()));
    
  }
  
  private String pr(String message) {
    if (out != null) out.println(message);
    return message;
  }
  
  @Override
  public void disastrousLock(String changesetLockTableName) {
    list.add(pr("disastrousLock " + changesetLockTableName));
  }
  
  @Override
  public void successLock(String changesetLockTableName) {
    list.add(pr("successLock " + changesetLockTableName));
  }
  
  @Override
  public void successUnlock(String changesetLockTableName) {
    list.add(pr("successUnlock " + changesetLockTableName));
  }
  
  @Override
  public void disastrousUnlock(String changesetLockTableName, Throwable e) {
    list.add(pr("disastrousUnlock " + changesetLockTableName + ", err = " + e.getMessage()));
  }
}
