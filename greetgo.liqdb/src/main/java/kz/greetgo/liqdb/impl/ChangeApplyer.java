package kz.greetgo.liqdb.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import kz.greetgo.liqdb.Changelog;
import kz.greetgo.liqdb.LiqdbConfig;
import kz.greetgo.liqdb.log.Logger;
import kz.greetgo.liqdb.util.DbUtil;

public class ChangeApplyer {
  private final Connection con;
  private final Logger logger;
  private final LiqdbConfig config;
  
  public ChangeApplyer(Connection con, Logger logger, LiqdbConfig config) {
    this.con = con;
    this.logger = logger;
    this.config = config;
  }
  
  public void applyChangelog(Changelog changelog) throws Exception {
    if (wasChangeExecuted(changelog)) return;
    
    long startedAt = System.currentTimeMillis();
    try {
      changelog.apply(con);
      logger.executionTime(System.currentTimeMillis() - startedAt, changelog);
      saveChangelogExecution(changelog);
    } catch (Exception e) {
      logger.executionError(System.currentTimeMillis() - startedAt, changelog, e);
      throw e;
    }
  }
  
  public void lockingApplyChangelogList(List<Changelog> changelogList) throws Exception {
    while (!tryLock()) {
      logger.disastrousLock(config.changesetLockTableName());
      Thread.sleep(1000);
    }
    
    logger.successLock(config.changesetLockTableName());
    try {
      for (Changelog changelog : changelogList) {
        applyChangelog(changelog);
      }
    } finally {
      
      try {
        unlock();
        logger.successUnlock(config.changesetLockTableName());
      } catch (Throwable e) {
        logger.disastrousUnlock(config.changesetLockTableName(), e);
        throw e;
      }
      
    }
  }
  
  private void unlock() throws SQLException {
    executeSql("insert into " + config.changesetLockTableName() + " values (0)");
  }
  
  private boolean tryLock() throws SQLException {
    con.setAutoCommit(false);
    try {
      executeSql("start transaction read write, isolation level serializable");
      executeSql("lock table " + config.changesetLockTableName() + " write");
      int locked = getLockedField();
      if (locked != 0) return false;
      executeSql("insert into " + config.changesetLockTableName() + " values(1)");
      return true;
    } finally {
      executeSql("commit");
      con.setAutoCommit(true);
    }
  }
  
  private boolean wasChangeExecuted(Changelog changelog) throws Exception {
    prepareChangelogTables();
    if (!isExecuted(changelog)) return false;
    md5sumIsOk(changelog);
    return true;
  }
  
  private void prepareChangelogTables() throws SQLException, Exception {
    if (DbUtil.hasTable(con, config.changesetTableName())) return;
    
    createChangesetTable();
    createChangesetLockTable();
  }
  
  private void createChangesetLockTable() throws SQLException {
    executeSql("create table " + config.changesetLockTableName() + "(locked int not null)");
    executeSql("insert into " + config.changesetLockTableName() + " values (0)");
  }
  
  private int getLockedField() throws SQLException {
    String sql = "select locked from " + config.changesetLockTableName();
    PreparedStatement ps = con.prepareStatement(sql);
    try {
      ResultSet rs = ps.executeQuery();
      try {
        if (rs.next()) return rs.getInt(1);
        throw new NoRecordInLockTable(config.changesetLockTableName());
      } finally {
        rs.close();
      }
    } finally {
      ps.close();
    }
  }
  
  private void createChangesetTable() throws Exception {
    DbType dbType = DbUtil.defineDbType(con);
    String authorType = DbUtil.strType(dbType, config.changesetAuthorTypeLen());
    String idType = DbUtil.strType(dbType, config.changesetIdTypeLen());
    String md5sumType = DbUtil.strType(dbType, 50);
    String timeType = DbUtil.timeType(dbType);
    
    StringBuilder sql = new StringBuilder();
    sql.append("create table ").append(config.changesetTableName()).append(" ( ");
    sql.append("  executedAt ").append(timeType).append(" default current_timestamp not null,");
    sql.append("  author ").append(authorType).append(" not null,");
    sql.append("  id ").append(idType).append(" not null,");
    sql.append("  md5sum ").append(md5sumType).append(",");
    sql.append("  ");
    sql.append("  primary key(author, id)");
    sql.append(")");
    
    executeSql(sql);
  }
  
  private void saveChangelogExecution(Changelog changelog) throws SQLException {
    String sql = "insert into " + config.changesetTableName()
        + " (author, id, md5sum) values (?, ?, ?)";
    
    PreparedStatement ps = con.prepareStatement(sql);
    try {
      ps.setString(1, changelog.author());
      ps.setString(2, changelog.id());
      ps.setString(3, changelog.md5sum());
      ps.executeUpdate();
    } finally {
      ps.close();
    }
  }
  
  private boolean isExecuted(Changelog changelog) throws SQLException {
    String sql = "select count(1) from " + config.changesetTableName()
        + " where id = ? and author = ?";
    
    PreparedStatement ps = con.prepareStatement(sql);
    try {
      ps.setString(1, changelog.id());
      ps.setString(2, changelog.author());
      ResultSet rs = ps.executeQuery();
      try {
        rs.next();
        return rs.getInt(1) > 0;
      } finally {
        rs.close();
      }
    } finally {
      ps.close();
    }
  }
  
  private void md5sumIsOk(Changelog changelog) throws SQLException {
    String sql = "select md5sum from " + config.changesetTableName()
        + " where id = ? and author = ?";
    
    PreparedStatement ps = con.prepareStatement(sql);
    try {
      ps.setString(1, changelog.id());
      ps.setString(2, changelog.author());
      ResultSet rs = ps.executeQuery();
      try {
        if (!rs.next()) throw new UnknownError();
        
        String md5sum = rs.getString(1);
        if (md5sum == null) return;
        if (md5sum.equals(changelog.md5sum())) return;
        throw new LeftMd5sum(md5sum, changelog.md5sum(), changelog);
      } finally {
        rs.close();
      }
    } finally {
      ps.close();
    }
  }
  
  private void executeSql(CharSequence sql) throws SQLException {
    PreparedStatement ps = con.prepareStatement(sql.toString());
    try {
      ps.execute();
    } finally {
      ps.close();
    }
  }
}
