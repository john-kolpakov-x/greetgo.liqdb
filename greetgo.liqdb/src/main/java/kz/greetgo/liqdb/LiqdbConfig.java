package kz.greetgo.liqdb;

public interface LiqdbConfig {
  
  String changesetTableName();
  
  String changesetLockTableName();
  
  int changesetAuthorTypeLen();
  
  int changesetIdTypeLen();
  
}
