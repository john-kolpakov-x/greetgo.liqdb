package kz.greetgo.liqdb.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DbUtilHsqldb {
  
  static Set<String> loadTables(Connection con) throws SQLException {
    StringBuilder sql = new StringBuilder();
    sql.append("select table_name from INFORMATION_SCHEMA.TABLES");
    sql.append("  where table_type = 'BASE TABLE'");
    sql.append("  and table_schema = 'PUBLIC'");
    PreparedStatement ps = con.prepareStatement(sql.toString());
    try {
      ResultSet rs = ps.executeQuery();
      try {
        Set<String> ret = new HashSet<>();
        while (rs.next()) {
          ret.add(rs.getString(1));
        }
        return ret;
      } finally {
        rs.close();
      }
    } finally {
      ps.close();
    }
  }
  
  static boolean hasTable(Connection con, String tableName) throws SQLException {
    StringBuilder sql = new StringBuilder();
    sql.append("select count(1) from INFORMATION_SCHEMA.TABLES");
    sql.append("  where table_type = 'BASE TABLE'");
    sql.append("  and table_schema = 'PUBLIC'");
    sql.append("  and table_name = ?");
    
    PreparedStatement ps = con.prepareStatement(sql.toString());
    ps.setString(1, tableName.toUpperCase());
    try {
      ResultSet rs = ps.executeQuery();
      try {
        if (rs.next()) return rs.getInt(1) > 0;
        throw new UnknownError();
      } finally {
        rs.close();
      }
    } finally {
      ps.close();
    }
  }
}
