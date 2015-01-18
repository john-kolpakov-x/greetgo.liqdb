package kz.greetgo.liqdb.impl;

import kz.greetgo.liqdb.LiqdbConfig;

public class TestLiqdbConfig implements LiqdbConfig {
  
  @Override
  public String changesetTableName() {
    return "db_changeset";
  }
  
  @Override
  public String changesetLockTableName() {
    return "db_changeset_lock";
  }
  
  @Override
  public int changesetAuthorTypeLen() {
    return 500;
  }
  
  @Override
  public int changesetIdTypeLen() {
    return 200;
  }
  
  @Override
  public int changesetGroupTypeLen() {
    return 200;
  }
}
