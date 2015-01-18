package kz.greetgo.liqdb.impl;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.Thread.UncaughtExceptionHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import kz.greetgo.liqdb.Changelog;
import kz.greetgo.liqdb.ConnectionSource;
import kz.greetgo.liqdb.ConnectionSourcer;
import kz.greetgo.liqdb.util.DbUtil;
import kz.greetgo.liqdb.util.RunIt;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class ChangeApplyerTest extends ConnectionSourcer {
  
  private TestLogger logger = new TestLogger(System.out);
  private TestLiqdbConfig config = new TestLiqdbConfig();
  
  @Test(dataProvider = "connectionDataProvider")
  public void applyChangelog_ok(ConnectionSource conSource) throws Exception {
    
    Connection con = conSource.get();
    
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
  public void applyChangelog_err(ConnectionSource con) throws Exception {
    ChangeApplyer ca = new ChangeApplyer(con.get(), logger, config);
    CreateTableChangelog c = createTableChangelog("test_001");
    ca.applyChangelog(c);
    c.md5sum = "other";
    ca.applyChangelog(c);
  }
  
  @AfterMethod
  public void afterMethod() {
    //for (String m : logger.list) {
    //System.out.println(m);
    //}
  }
  
  @Test(dataProvider = "connectionDataProvider")
  public void lockingApplyChangelogList_ok(final ConnectionSource conSource) throws Throwable {
    
    final List<Changelog> list = new ArrayList<Changelog>();
    
    list.add(createTableChangelog("test_101"));
    list.add(createTableChangelog("test_102"));
    
    final List<Thread> threadList = new ArrayList<Thread>();
    
    final List<Throwable> errors = new LinkedList<>();
    
    UncaughtExceptionHandler ueh = new UncaughtExceptionHandler() {
      @Override
      public void uncaughtException(Thread t, Throwable e) {
        errors.add(e);
      }
    };
    
    for (int i = 0; i < 10; i++) {
      Thread t = new Thread(new RunIt() {
        @Override
        protected void runit() throws Exception {
          Connection con = conSource.create();
          final ChangeApplyer ca = new ChangeApplyer(con, logger, config);
          ca.lockingApplyChangelogList(list);
        }
      });
      t.setUncaughtExceptionHandler(ueh);
      threadList.add(t);
    }
    
    for (Thread t : threadList) {
      t.start();
    }
    for (Thread t : threadList) {
      t.join();
    }
    
    Throwable last = null;
    
    for (Throwable e : errors) {
      last = e;
      e.printStackTrace();
    }
    
    {
      PreparedStatement ps = conSource.create().prepareStatement(
          "select locked from " + config.changesetLockTableName());
      try {
        ResultSet rs = ps.executeQuery();
        try {
          while (rs.next()) {
            System.out.println("------------|| LOCKED = " + rs.getInt(1));
          }
        } finally {
          rs.close();
        }
      } finally {
        ps.close();
      }
    }
    if (last != null) throw last;
  }
}
