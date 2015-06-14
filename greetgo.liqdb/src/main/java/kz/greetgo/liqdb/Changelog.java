package kz.greetgo.liqdb;

import java.sql.Connection;

public interface Changelog {
  String md5sum();
  
  String uniqueId();
  
  void apply(Connection con) throws Exception;
}
