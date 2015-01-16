package kz.greetgo.liqdb.impl;

import kz.greetgo.liqdb.Changelog;

public class LeftMd5sum extends RuntimeException {
  
  public final String md5sumFromDB;
  public final String md5sumFromChangelog;
  public final Changelog changelog;
  
  public LeftMd5sum(String md5sumFromDB, String md5sumFromChangelog, Changelog changelog) {
    super("LeftMd5sum in db = " + md5sumFromDB + ", in changelog = " + md5sumFromChangelog
        + ", chnagelog " + changelog.identityInfo());
    this.md5sumFromDB = md5sumFromDB;
    this.md5sumFromChangelog = md5sumFromChangelog;
    this.changelog = changelog;
  }
  
}
