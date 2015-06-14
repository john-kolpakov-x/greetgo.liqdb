package kz.greetgo.liqdb;

public interface LiqdbConfig {
  
  String changesetTableName();
  
  int changesetUniqueIdTypeLen();
  
  String lockingId();
}
