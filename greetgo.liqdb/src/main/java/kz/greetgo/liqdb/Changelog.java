package kz.greetgo.liqdb;

import java.sql.Connection;

public interface Changelog {
  String md5sum();
  
  String author();
  
  String id();
  
  String identityInfo();
  
  void apply(Connection con) throws Exception;
}
