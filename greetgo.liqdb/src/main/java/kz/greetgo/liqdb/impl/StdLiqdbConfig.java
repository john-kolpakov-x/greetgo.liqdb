package kz.greetgo.liqdb.impl;

import kz.greetgo.liqdb.LiqdbConfig;

public class StdLiqdbConfig implements LiqdbConfig {
  
  @Override
  public String changesetTableName() {
    return "db_changeset";
  }
  
  @Override
  public int changesetUniqueIdTypeLen() {
    return 500;
  }
  
  @Override
  public String lockingId() {
    return "lock";
  }
  
}
