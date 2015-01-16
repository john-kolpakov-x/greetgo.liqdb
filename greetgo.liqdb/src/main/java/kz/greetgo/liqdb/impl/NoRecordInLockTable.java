package kz.greetgo.liqdb.impl;

public class NoRecordInLockTable extends RuntimeException {
  
  public final String changesetLockTableName;
  
  public NoRecordInLockTable(String changesetLockTableName) {
    super(changesetLockTableName);
    this.changesetLockTableName = changesetLockTableName;
  }
}
