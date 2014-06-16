package unlekker.data;

import java.util.*;

import unlekker.mb2.util.UMB;

public class UDataPoint extends UMB {
  private UTime time;
  private ArrayList<UDataTag> tags;
  private HashMap<String,Object> data;

  public static String DATAFLOAT="Float";
  public static String DATAINT="Integer";
  public static String DATASTRING="String";
  public static String DATAOBJ="Object";
  
  public String title,description;

  public UDataPoint(String title,String description) {
    this();
    this.title=title;
    this.description=description;
  }

  public UDataPoint() {
    tags=new ArrayList<UDataTag>();
    data=new HashMap<String, Object>();
  }
  
  /////////////// TAGS
  
  public UDataPoint tag(UDataTag tag) {
    if(!tags.contains(tag)) tags.add(tag);
    return this;
  }

  public boolean isTagged(UDataTag tag) {
    return tags.contains(tag);
  }

  public boolean isTagged(String s) {
    for(UDataTag tmp:tags) if(tmp.nameEquals(s)) return true;
    return false;
  }
  
  public ArrayList<UDataTag> tags() {
    return tags;
  }
  
  
  /////////////// DATA FIELDS
  
  public UDataPoint addFloat(String key,float value) {
    data.put(key,value);    
    return this;    
  }

  public UDataPoint addInt(String key,int value) {
    data.put(key,value);    
    return this;
  }

  public UDataPoint addString(String key,String value) {
    data.put(key,value);    
    return this;
  }

  public UDataPoint addObject(String key,Object value) {
    data.put(key,value);    
    return this;
  }

  public float getFloat(String key) {
    return (Float)data.get(key);
  }

  public int getInt(String key) {        
    return (Integer)data.get(key);
  }

  public String getString(String key) {
    return (String)data.get(key);
  }

  public Object getObject(String key) {        
    return data.get(key);
  }

  public String getClassName(String key) {
    Object o=getObject(key);
    return o.getClass().getName();
  }
  
  public String getValue(String key) {
    try {
      String cl=getClassName(key);
      if(cl.contains(DATAFLOAT)) return ""+getFloat(key);
      if(cl.contains(DATAINT)) return ""+getInt(key);
      if(cl.contains(DATASTRING)) return getString(key);
      
      return strf("%s[%s]", DATAOBJ,cl);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return null;
  }

  public Set<String> getKeys() {
    return data.keySet();
  }
  
  /////////////// TIME
  
  public UTime getTime() {
    return time;
  }

  public UDataPoint setTime(UTime t) {
    this.time=new UTime(t);
    return this;
  }

  public UDataPoint setTime(long t) {
    this.time=new UTime(t);
    return this;
  }

//  public boolean inInterval(UTimestamp t) {
//    return (time!=null && time.inInterval(t) ? true : false);
//  }

  public boolean before(UTime t) {
    return (time!=null && time.before(t) ? true : false);
  }
  
}
