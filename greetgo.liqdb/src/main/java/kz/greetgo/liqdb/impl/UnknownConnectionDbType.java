package kz.greetgo.liqdb.impl;

import java.sql.Connection;

public class UnknownConnectionDbType extends RuntimeException {
  
  public UnknownConnectionDbType(Connection con) {
    super(con.toString());
  }
}
