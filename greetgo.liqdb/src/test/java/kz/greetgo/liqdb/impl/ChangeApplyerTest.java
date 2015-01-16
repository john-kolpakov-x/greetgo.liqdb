package kz.greetgo.liqdb.impl;

import static org.fest.assertions.Assertions.assertThat;

import java.sql.Connection;

import kz.greetgo.liqdb.ConnectionSourcer;
import kz.greetgo.liqdb.util.DbUtil;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class ChangeApplyerTest extends ConnectionSourcer {
  
  private TestLogger logger = new TestLogger(System.out);
  private TestLiqdbConfig config = new TestLiqdbConfig();
  
  @Test(dataProvider = "connectionDataProvider")
  public void applyChangelog_ok(Connection con) throws Exception {
    
    ChangeApplyer ca = new ChangeApplyer(con, logger, config);
    
    String tableName = "test_fdsafdsafdsaf";
    
    CreateTableChange c = new CreateTableChange(tableName);
    c.author = "авыа";
    c.id = "dsпапa";
    c.md5sum = "dsaавыаывdsd";
    
    ca.applyChangelog(c);
    
    ca.applyChangelog(c);
    
    assertThat(DbUtil.hasTable(con, tableName)).isTrue();
  }
  
  @Test(dataProvider = "connectionDataProvider", expectedExceptions = LeftMd5sum.class)
  public void applyChangelog_err(Connection con) throws Exception {
    
    ChangeApplyer ca = new ChangeApplyer(con, logger, config);
    
    CreateTableChange c = new CreateTableChange("test_001");
    c.author = "asd";
    c.id = "dsa";
    c.md5sum = "dsadsd";
    
    ca.applyChangelog(c);
    
    c.md5sum = "other";
    
    ca.applyChangelog(c);
  }
  
  @AfterMethod
  public void afterMethod() {
    for (String m : logger.list) {
      System.out.println(m);
    }
  }
}
