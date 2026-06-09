package ua.foxminded.school.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
  private static ConnectionManager instance;
  private final String url;
  private final String user;
  private final String password;
  private ConnectionManager(){
      Properties props=new Properties();
      try(InputStream in=getClass().getClassLoader().
              getResourceAsStream("database.properties")){
          props.load(in);
      } catch (IOException e) {
          throw new RuntimeException(e);
      }
      this.url =props.getProperty("db.url");
      this.user =props.getProperty("db.user");
      this.password = props.getProperty("db.password");
  }
  public static ConnectionManager getInstance(){
      if (instance == null) {
          instance = new ConnectionManager();
      }
      return instance;
  }
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }



}
