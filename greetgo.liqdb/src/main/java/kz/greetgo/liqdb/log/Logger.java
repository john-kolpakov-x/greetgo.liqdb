package kz.greetgo.liqdb.log;

import kz.greetgo.liqdb.Changelog;

public interface Logger {
  void executionTime(long millisPeriod, Changelog change);
  
  void executionError(long millisPeriod, Changelog change, Throwable error);
  
  void disastrousLock(String changesetLockTableName);
  
  void successLock(String changesetLockTableName);
  
  void successUnlock(String changesetLockTableName);
  
  void disastrousUnlock(String changesetLockTableName, Throwable e);
}
