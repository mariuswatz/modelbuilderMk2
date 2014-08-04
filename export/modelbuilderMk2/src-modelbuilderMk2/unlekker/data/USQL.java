package unlekker.data;

import unlekker.mb2.util.UMB;

import java.sql.*;
import java.io.*;
import java.util.*;

import processing.data.Table;
import processing.data.TableRow;

public class USQL extends UMB {
  
  // load the sqlite-JDBC driver using the current class loader
  Connection connection = null;
  ArrayList<String> tableNames;

  long queryTime=0;
  int queryUpdateCount;
  
  public USQL(String databaseFilename) {
    try {
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:"+databaseFilename);

    } catch(Exception e) {
      // if the error message is "out of memory", 
      // it probably means no database file is found
      e.printStackTrace();
      logDivider("Error - SQLite library missing or no database found.");
    }
  }
  
  public ResultSet query(String query) {
    ResultSet res=null;
    Statement statement=null;
    
    queryTime=System.currentTimeMillis();
    queryUpdateCount=-1;
    
    try {
      statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.
      boolean hasResult=statement.execute(query);
      if(hasResult) res=statement.getResultSet();
      
      if(!hasResult) queryUpdateCount=statement.getUpdateCount();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      log("Query: "+query);
    }
//    finally {
//      try {
//        if(statement!=null) statement.close();
//      } catch (SQLException e) {
//        // TODO Auto-generated catch block
//        e.printStackTrace();
//      }
//      try {
//        if(res!=null) res.close();
//      } catch (SQLException e) {
//        // TODO Auto-generated catch block
//        e.printStackTrace();
//      }
//    }
    
    queryTime=System.currentTimeMillis()-queryTime;

    return res;
  }

  public long queryTime() {
    return queryTime;
  }

  public ArrayList<String> tableNames() {
    if(tableNames!=null) return tableNames;
    
    tableNames=new ArrayList<String>();
    
    try {
      ResultSet res=connection.getMetaData().getTables(null,null,null,null);
      ResultSetMetaData resMeta=res.getMetaData();

      while(res.next()) {
        tableNames.add(res.getString("TABLE_NAME"));
      } 

      res.close();

    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return tableNames;
  }

  public Table getTable(String cmd) {
    Table dat=new Table();

    try {
      int pos=cmd.indexOf("FROM ");
      if(pos<0) pos=cmd.indexOf("from ");
      String tok[]=getPApplet().split(cmd.substring(pos), ' ');
      
      log(tok);
      Map<String,Integer> map=tableMap(tok[1],cmd);
      
      ResultSet res;
      
      res=query(cmd);

      ResultSetMetaData meta=res.getMetaData();
      
      
      for(String s : map.keySet()) {
        dat.addColumn(s);
      }
      
      int col=dat.getColumnCount();
      
      while(res.next()) {
        String s="";
        TableRow row=dat.addRow();
        
        for(int i=0; i<dat.getColumnCount(); i++) {
          String str,name;
          
          name=dat.getColumnTitle(i);
          if(map.get(name)==Types.BLOB) str="[BLOB]";
          else str=res.getString(name);
          
          row.setString(i, fix(str));
          s+=str+"\t";
        }
      } 
      
      UMB.log(cmd+" | columns="+col+" rows="+res.getRow());

      res.close();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return dat;
  }
  
  String fix(String s) {
    if(s==null) return ""+s;
    
//    for(char tmp : s.toCharArray()) {
//      if(!Character.isLetterOrDigit(tmp))
//        UMB.logf("\t%d\t[%s]",(int)tmp,""+tmp,Character.getType(tmp));
//    }
    s=s.replaceAll("\r\n", "[EOL]");
    s=s.replaceAll("\n", "[EOL]");
//    s=s.replaceAll(System.getProperty("line.separator"), "[EOL]");
    return s.trim();
  }

  public Map<String, Integer> tableMap(String table) {
    return tableMap(table,null);
  }

  public Map<String, Integer> tableMap(String table,String query) {
    TreeMap<String,Integer> map=null;
    
    try {
      if(query==null) query="SELECT * FROM "+table;
      ResultSet res=query(query+" LIMIT 1;");
      res.next();
      
      ResultSetMetaData resMeta=res.getMetaData();

      int col=resMeta.getColumnCount();    
      map=new TreeMap<String, Integer>();
      
      for(int i=0; i<col; i++) map.put(
          resMeta.getColumnName(i+1),
          resMeta.getColumnType(i+1)
          );
      
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return map;
  }
  
  public void close() {
    if(connection != null) try {
      connection.close();
    } catch (SQLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
  }
}
