package kz.greetgo.liqdb.impl;

import java.sql.Connection;

import kz.greetgo.liqdb.ConnectionSource;
import kz.greetgo.liqdb.ConnectionSourcer;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class ChangeApplyerTest extends ConnectionSourcer {
  
  @SuppressWarnings("unused")
  private TestLogger logger = new TestLogger(System.out);
  @SuppressWarnings("unused")
  private StdLiqdbConfig config = new StdLiqdbConfig();
  
  @Test(dataProvider = "connectionDataProvider")
  public void applyChangelog_ok(ConnectionSource conSource) throws Exception {
    
    @SuppressWarnings("unused")
    Connection con = conSource.get();
    
  }
  
  @AfterMethod
  public void afterMethod() {
    //for (String m : logger.list) {
    //System.out.println(m);
    //}
  }
  
  @Test(dataProvider = "connectionDataProvider")
  public void lockingApplyChangelogList_ok(final ConnectionSource conSource) throws Throwable {
    System.out.println(conSource);
  }
}
