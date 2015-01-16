package kz.greetgo.liqdb.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Set;

import kz.greetgo.liqdb.impl.DbType;
import kz.greetgo.liqdb.impl.UnknownConnectionDbType;

public class DbUtil {
  public static DbType defineDbType(Connection con) throws SQLException {
    
    DatabaseMetaData metaData = con.getMetaData();
    
    //    String databaseProductName = metaData.getDatabaseProductName();
    //    System.out.println("databaseProductName = " + databaseProductName);
    //    
    //    String databaseProductVersion = metaData.getDatabaseProductVersion();
    //    System.out.println("databaseProductVersion = " + databaseProductVersion);
    
    if ("HSQL Database Engine".equals(metaData.getDatabaseProductName())) return DbType.Hsqldb;
    
    throw new UnknownConnectionDbType(con);
  }
  
  public static Set<String> loadTables(Connection con) throws SQLException {
    switch (defineDbType(con)) {
    case Hsqldb:
      return DbUtilHsqldb.loadTables(con);
    }
    throw new NeedImplement(con.getClass());
  }
  
  public static boolean hasTable(Connection con, String tableName) throws SQLException {
    switch (defineDbType(con)) {
    case Hsqldb:
      return DbUtilHsqldb.hasTable(con, tableName);
    }
    throw new NeedImplement(con.getClass());
  }
  
  public static String strType(DbType dbType, int len) {
    switch (dbType) {
    case Hsqldb:
      return "varchar(" + len + ")";
    }
    throw new NeedImplement("strType");
  }
  
  public static String timeType(DbType dbType) {
    switch (dbType) {
    case Hsqldb:
      return "timestamp";
    }
    throw new NeedImplement("timeType");
  }
}
