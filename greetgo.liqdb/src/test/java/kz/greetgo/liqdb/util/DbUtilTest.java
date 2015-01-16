package kz.greetgo.liqdb.util;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Set;

import kz.greetgo.liqdb.ConnectionSourcer;
import kz.greetgo.liqdb.impl.DbType;

import org.testng.annotations.Test;

public class DbUtilTest extends ConnectionSourcer {
  
  @Test
  public void defineDbType_Hsqldb() throws Exception {
    
    createConnectionHsqldb();
    
    DbType dbType = DbUtil.defineDbType(connectionHsqldb);
    
    assertThat(dbType).isEqualTo(DbType.Hsqldb);
  }
  
  @Test
  public void loadTables_Hsqldb() throws Exception {
    
    createConnectionHsqldb();
    
    exec(connectionHsqldb, "create table test_wow_001(id int)");
    exec(connectionHsqldb, "create table test_wow_002(id int)");
    
    Set<String> set = DbUtil.loadTables(connectionHsqldb);
    
    System.out.println(set);
    
    assertThat(set).contains("TEST_WOW_001");
    assertThat(set).contains("TEST_WOW_002");
  }
  
  @Test
  public void hasTable_Hsqldb() throws Exception {
    
    createConnectionHsqldb();
    
    exec(connectionHsqldb, "create table has_table_001(id int)");
    exec(connectionHsqldb, "create table has_table_002(id int)");
    
    assertThat(DbUtil.hasTable(connectionHsqldb, "has_table_001")).isTrue();
    assertThat(DbUtil.hasTable(connectionHsqldb, "has_table_001")).isTrue();
    assertThat(DbUtil.hasTable(connectionHsqldb, "dfsfdsgfdghfdsh")).isFalse();
    
  }
}
