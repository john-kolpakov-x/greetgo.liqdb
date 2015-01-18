package kz.greetgo.liqdb;

public interface LiqdbConfig {
  
  String changesetTableName();
  
  String changesetLockTableName();
  
  int changesetGroupTypeLen();
  
  int changesetIdTypeLen();
  
  int changesetAuthorTypeLen();
  
}
