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
    
    CreateTableChangelog c = createTableChangelog(tableName);
    
    ca.applyChangelog(c);
    
    ca.applyChangelog(c);
    
    assertThat(DbUtil.hasTable(con, tableName)).isTrue();
  }
  
  private CreateTableChangelog createTableChangelog(String tableName) {
    CreateTableChangelog c = new CreateTableChangelog(tableName);
    c.author = tableName + "_author";
    c.id = tableName + "_id";
    c.group = tableName + "_group";
    c.md5sum = tableName + "_md5sum";
    return c;
  }
  
  @Test(dataProvider = "connectionDataProvider", expectedExceptions = LeftMd5sum.class)
  public void applyChangelog_err(Connection con) throws Exception {
    ChangeApplyer ca = new ChangeApplyer(con, logger, config);
    CreateTableChangelog c = createTableChangelog("test_001");
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
  
  @Test(dataProvider = "connectionDataProvider")
  public void lockingApplyChangelogList_ok(Connection con) throws Exception {
    
  }
}
