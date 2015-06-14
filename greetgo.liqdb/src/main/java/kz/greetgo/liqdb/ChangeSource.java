package kz.greetgo.liqdb;

import java.util.List;

public interface ChangeSource {
  List<Changelog> changelogList();
}
