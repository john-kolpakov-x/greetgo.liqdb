package kz.greetgo.liqdb.impl;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
  public void changelogApplyOk(long millisPeriod, Changelog change) {
    list.add(pr("changelogApplyOk: millis " + millisPeriod + ", changelog " + change.identityInfo()));
  }
  
  @Override
  public void changelogApplyError(long millisPeriod, Changelog change, Throwable error) {
    list.add(pr("changelogApplyError: millis " + millisPeriod + ", changelog "
        + change.identityInfo() + ", errorClass " + error.getClass().getSimpleName()
        + ", errorMessage " + error.getMessage()));
    
  }
  
  static class PR {
    final String threadName;
    final Date prDate;
    final String message;
    
    public PR(String threadName, Date prDate, String message) {
      this.threadName = threadName;
      this.prDate = prDate;
      this.message = message;
    }
    
    @Override
    public String toString() {
      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
      return sdf.format(prDate) + ' ' + threadName + ' ' + message;
    }
  }
  
  private String pr(final String message) {
    if (out != null) {
      out.println(new PR(Thread.currentThread().getName(), new Date(), message).toString());
    }
    return message;
  }
  
  @Override
  public void cannotLock(String changesetLockTableName) {
    String threadName = Thread.currentThread().getName();
    list.add(pr("cannotLock " + changesetLockTableName + ", thread = " + threadName));
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
  public void cannotUnlock(String changesetLockTableName, Throwable e) {
    list.add(pr("cannotUnlock " + changesetLockTableName + ", err = " + e.getMessage()));
  }
}
