package kz.greetgo.liqdb;

import java.sql.Connection;

public interface Changelog {
  String md5sum();
  
  String group();
  
  String id();
  
  String author();
  
  String identityInfo();
  
  void apply(Connection con) throws Exception;
}
