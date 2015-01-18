package kz.greetgo.liqdb.util;

import static org.fest.assertions.Assertions.assertThat;

import java.sql.Connection;
import java.util.Set;

import kz.greetgo.liqdb.ConnectionSourcer;
import kz.greetgo.liqdb.impl.DbType;

import org.testng.annotations.Test;

public class DbUtilTest extends ConnectionSourcer {
  
  @Test
  public void defineDbType_Hsqldb() throws Exception {
    
    DbType dbType = DbUtil.defineDbType(connectionHsqldb().create());
    
    assertThat(dbType).isEqualTo(DbType.Hsqldb);
  }
  
  @Test
  public void loadTables_Hsqldb() throws Exception {
    
    Connection con = connectionHsqldb().create();
    
    exec(con, "create table test_wow_001(id int)");
    exec(con, "create table test_wow_002(id int)");
    
    Set<String> set = DbUtil.loadTables(con);
    
    System.out.println(set);
    
    assertThat(set).contains("TEST_WOW_001");
    assertThat(set).contains("TEST_WOW_002");
  }
  
  @Test
  public void hasTable_Hsqldb() throws Exception {
    
    Connection con = connectionHsqldb().create();
    
    exec(con, "create table has_table_001(id int)");
    exec(con, "create table has_table_002(id int)");
    
    assertThat(DbUtil.hasTable(con, "has_table_001")).isTrue();
    assertThat(DbUtil.hasTable(con, "has_table_001")).isTrue();
    assertThat(DbUtil.hasTable(con, "dfsfdsgfdghfdsh")).isFalse();
    
  }
}
