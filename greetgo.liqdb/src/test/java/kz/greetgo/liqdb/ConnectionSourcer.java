package kz.greetgo.liqdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;

public class ConnectionSourcer {
  
  private static abstract class ConnectionContainer implements ConnectionSource {
    
    final Set<Connection> connections = new HashSet<Connection>();
    
    @Override
    public Connection create() throws Exception {
      Connection ret = innerCreate();
      connections.add(ret);
      return ret;
    }
    
    @Override
    public Connection get() throws Exception {
      if (connections.size() > 0) connections.iterator().next();
      return create();
    }
    
    protected abstract Connection innerCreate() throws Exception;
    
    void close() throws SQLException {
      for (Connection c : connections) {
        c.close();
      }
      connections.clear();
    }
    
  }
  
  @DataProvider
  public Object[][] connectionDataProvider() throws Exception {
    
    return new Object[][] {
    
    new Object[] { connectionHsqldb() },
    
    };
  }
  
  private ConnectionContainer connectionHsqldb = null;
  
  protected ConnectionSource connectionHsqldb() throws Exception {
    if (connectionHsqldb == null) createConnectionHsqldb();
    return connectionHsqldb;
  }
  
  private void createConnectionHsqldb() throws Exception {
    if (connectionHsqldb != null) return;
    
    final Random rnd = new Random();
    
    connectionHsqldb = new ConnectionContainer() {
      String url = "jdbc:hsqldb:file:build/test" + rnd.nextLong();
      
      //String url = "jdbc:hsqldb:mem:test" + rnd.nextLong();
      
      @Override
      protected Connection innerCreate() throws Exception {
        Class.forName("org.hsqldb.jdbcDriver");
        return DriverManager.getConnection(url, "sa", "");
      }
    };
  }
  
  @AfterClass
  public void afterClass_43214j32k2jh14() throws Exception {
    if (connectionHsqldb != null) {
      connectionHsqldb.close();
      connectionHsqldb = null;
    }
  }
  
  protected static void exec(Connection con, String sql) throws SQLException {
    PreparedStatement ps = con.prepareStatement(sql);
    ps.execute();
    ps.close();
  }
}
