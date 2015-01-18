package kz.greetgo.liqdb.log;

import kz.greetgo.liqdb.Changelog;

public interface Logger {
  void changelogApplyOk(long millisPeriod, Changelog changelog);
  
  void changelogApplyError(long millisPeriod, Changelog changelog, Throwable error);
  
  void cannotLock(String changesetLockTableName);
  
  void successLock(String changesetLockTableName);
  
  void successUnlock(String changesetLockTableName);
  
  void cannotUnlock(String changesetLockTableName, Throwable e);
}
