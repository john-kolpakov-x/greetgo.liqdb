package kz.greetgo.liqdb;

import java.sql.Connection;

public interface ConnectionSource {
  Connection create() throws Exception;
  
  Connection get() throws Exception;
}
