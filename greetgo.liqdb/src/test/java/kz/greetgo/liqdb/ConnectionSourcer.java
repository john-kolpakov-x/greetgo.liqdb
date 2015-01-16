package kz.greetgo.liqdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;

public class ConnectionSourcer {
  @DataProvider
  public Object[][] connectionDataProvider() throws Exception {
    
    createConnectionHsqldb();
    
    return new Object[][] {
    
    new Object[] { connectionHsqldb },
    
    };
  }
  
  protected Connection connectionHsqldb = null;
  
  protected void createConnectionHsqldb() throws Exception {
    if (connectionHsqldb != null) return;
    Class.forName("org.hsqldb.jdbcDriver");
    
    String url = "jdbc:hsqldb:mem:aname";
    //String url = "jdbc:hsqldb:file:build/asd";
    
    connectionHsqldb = DriverManager.getConnection(url, "sa", "");
  }
  
  @AfterClass
  public void afterClass_43214j32k2jh14() throws Exception {
    if (connectionHsqldb != null) {
      connectionHsqldb.close();
      connectionHsqldb = null;
    }
  }
  
  protected void exec(Connection con, String sql) throws SQLException {
    PreparedStatement ps = con.prepareStatement(sql);
    ps.execute();
    ps.close();
  }
}
