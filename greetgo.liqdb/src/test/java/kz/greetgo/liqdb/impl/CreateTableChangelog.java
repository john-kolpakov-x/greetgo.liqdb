package kz.greetgo.liqdb.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;

import kz.greetgo.liqdb.Changelog;

public class CreateTableChangelog implements Changelog {
  
  private final String tableName;
  
  public CreateTableChangelog(String tableName) {
    this.tableName = tableName;
  }
  
  public String md5sum;
  public String author;
  public String id;
  public String group;
  
  @Override
  public String md5sum() {
    return md5sum;
  }
  
  @Override
  public String author() {
    return author;
  }
  
  @Override
  public String id() {
    return id;
  }
  
  @Override
  public String group() {
    return group;
  }
  
  @Override
  public void apply(Connection con) throws Exception {
    PreparedStatement ps = con.prepareStatement("create table " + tableName + " (id int)");
    ps.execute();
    ps.close();
  }
  
  @Override
  public String identityInfo() {
    return getClass().getSimpleName() + ": tableName = " + tableName + ", group:id = " + group
        + ":" + id + ", author = " + author;
  }
  
}
