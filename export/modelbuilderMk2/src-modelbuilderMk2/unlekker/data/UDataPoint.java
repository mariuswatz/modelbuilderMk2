package unlekker.data;

import java.util.*;

import unlekker.mb2.util.UMB;

public class UDataPoint extends UMB implements Comparable<UDataPoint> {
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

  public UDataPoint addDouble(String key,double value) {
    data.put(key,value);    
    return this;    
  }

  public UDataPoint addInt(String key,int value) {
    data.put(key,value);    
    return this;
  }

  public UDataPoint addLong(String key,long value) {
    data.put(key,value);    
    return this;
  }

  public UDataPoint addBoolean(String key,boolean value) {
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

  public double getDouble(String key) {
    return (Double)data.get(key);
  }

  public long getLong(String key) {        
    return (Long)data.get(key);
  }
  
  public int getInt(String key) {        
    return (Integer)data.get(key);
  }

  public boolean getBoolean(String key) {        
    return (Boolean)data.get(key);
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

  public String getValueType(String key) {
    String cl=getClassName(key);
    if(cl.contains(DATAFLOAT)) return DATAFLOAT;
    if(cl.contains(DATAINT)) return DATAINT;
    if(cl.contains(DATASTRING)) return DATASTRING;
    
    return DATAOBJ;
  }
  
  public String getValue(String key) {
    try {
      String type=getValueType(key);
      
      if(type==DATAFLOAT) return ""+getFloat(key);
      if(type==DATAINT) return ""+getInt(key);
      if(type==DATASTRING) return getString(key);
      
      Object o=getObject(key);
      return strf("%s[%s]", o.toString(),o.getClass().getName());
      
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return null;
  }

  public boolean hasKey(String key) {
    for(String tmp : keys()) if(key.compareTo(tmp)==0) return true;
    
    return false;
  }

  public Set<String> keys() {
    TreeSet<String> k=new TreeSet<String>();
    for(String key : data.keySet()) k.add(key);
    
    return k;//data.keySet();
  }
  
  public int size() {
    return data.size();
  }
  
  /////////////// TIME
  
  public UTime time() {
    return time;
  }

  public UTime timeMin() {
    return time;
  }

  public UTime timeMax() {
    return time;
  }

  public UDataPoint timeSet(UTime t) {
    this.time=new UTime(t);
    return this;
  }

  public UDataPoint timeSet(long t) {
    this.time=new UTime(t);
    return this;
  }

//  public boolean inInterval(UTimestamp t) {
//    return (time!=null && time.inInterval(t) ? true : false);
//  }

  public boolean before(UTime t) {
    return (time!=null && time.before(t) ? true : false);
  }
 
  public String str() {
    StringBuffer buf=strBufGet();
    
//    buf.append('[');
    for(String key : keys()) {
      buf.append('|');
      buf.append(key).append('=').append(getValue(key));      
    }
    buf.append(']');
    buf.insert(0, '['+(time==null ? "" : time.str()));
    
    return strBufDispose(buf);
  }

  @Override
  public int compareTo(UDataPoint o) {
    if(time()!=null) {
     return (int)(time.get()-o.time.get()); 
    }
    
    return -1; 
  }
}
