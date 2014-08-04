package unlekker.data;

import java.util.*;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;
import unlekker.mb2.util.UMB;

public class UJSON extends UMB {
  TreeMap<String,Class> typeMap;
  
  JSONObject o;
  ArrayList<JSONObject> obj;
  ArrayList<JSONArray> arr;
  
//  UDa
  
  public UJSON(JSONObject o) {
    this.o=o;
    
    obj=new ArrayList<JSONObject>();
    arr=new ArrayList<JSONArray>();
    
    getTypes();
    
    for(String key : typeMap.keySet()) {
      Class cl=typeMap.get(key);
      if(cl==JSONObject.class) obj.add(o.getJSONObject(key));
      if(cl==JSONArray.class) arr.add(o.getJSONArray(key));
    }
    
    String s="";
    for(String key : typeMap.keySet()) {
      if(s.length()>0) s+=", ";
      s+=key+"|"+typeMap.get(key).getSimpleName();
    }

//    log("["+s+"]");
  }
  
  public UJSON getChild(String id) {
    try {
      if(o.hasKey(id) && typeMap.get(id)==JSONObject.class) {
        log("getChild '"+id+"'");
        return new UJSON(o.getJSONObject(id));
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return null;
  }

  public ArrayList<UJSON> getChildArray(String id) {
    ArrayList<UJSON> l=new ArrayList<UJSON>();
    
    try {
      if(o.hasKey(id) && typeMap.get(id)==JSONArray.class) {
        JSONArray arr=o.getJSONArray(id);
        for(int i=0; i<arr.size(); i++) l.add(new UJSON(arr.getJSONObject(i)));
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return l;
  }
  
  public UDataList toDataList(String id) {
    ArrayList<UJSON> arr=getChildArray(id);
//    log(arr.size());
    
    UDataList l=new UDataList();    
    for(UJSON j : arr) l.add(j.toData());
    
    return l;
  }

  public UDataPoint toData() {
    UDataPoint pt=new UDataPoint();
    
    for(String key : typeMap.keySet()) {
      Class cl=typeMap.get(key);
      
      if(cl==String.class) {
        pt.addString(key, o.getString(key)); 
      }
      else if(cl==Integer.class) {
        pt.addInt(key, o.getInt(key)); 
      }
      else if(cl==Long.class) {
        pt.addLong(key, o.getLong(key)); 
      }
      else if(cl==Float.class) {
        pt.addFloat(key, o.getFloat(key)); 
      }
      else if(cl==Double.class) {
        pt.addDouble(key, o.getDouble(key)); 
      }
      else if(cl==Boolean.class) {
        pt.addBoolean(key, o.getBoolean(key)); 
      }
      else {
        if(cl==JSONObject.class) pt.addObject(key, o.getJSONObject(key));
        if(cl==JSONArray.class) pt.addObject(key, o.getJSONArray(key));
      }
      
    }
    
    return pt;
  }
  
  
  public String str() {
    return "";
  }

  public Map<String,Class> getTypes() {
    if(typeMap!=null) return typeMap;
    
    typeMap=new TreeMap<String, Class>();
    
    for(String key : (Set<String>)o.keys()) {
      typeMap.put(key, getType(key));
    }
    
    
    return typeMap;
  }
  
  public Class getType(String key) {
    String type="null";
    
//    try {
//      String cnv=o.getString(key);      
//      return String.class;
//    } catch (Exception e) {
//    }
    
    try {
      long l=o.getLong(key);
      return Long.class;
    } catch (Exception e) {
    }

    try {
      int i=o.getInt(key);
      return Integer.class;
    } catch (Exception e) {
    }

    try {
      float f=o.getFloat(key);
      return Float.class;
    } catch (Exception e) {
    }

    try {
      double d=o.getDouble(key);
      return Double.class;
    } catch (Exception e) {
    }

    try {
      JSONObject j=o.getJSONObject(key);
      return JSONObject.class;
    } catch (Exception e) {
    }

    try {
      JSONArray j=o.getJSONArray(key);
      return JSONArray.class;
    } catch (Exception e) {
    }

    try {
      boolean b=o.getBoolean(key);
      return Boolean.class;
    } catch (Exception e) {
    }

    return String.class;
  }
  
/*  public void printJSON(JSONObject o) {
    ArrayList<String> str=new ArrayList<String>();
    str.add("ROOT");
    
    printJSON(o,str,"  ");
    
    for(String s : str) log(s);
//    PApplet.printArray(str);
  }

*/  /*public void printJSON(JSONObject o,ArrayList<String> str,String indent) {
    ArrayList<JSONArray> arr=new ArrayList<JSONArray>();
    ArrayList<JSONObject> obj=new ArrayList<JSONObject>();
    
    StringBuffer buf=strBufGet();
    
    Map<String, Class> types=getTypes(o);
    for(String key : types.keySet()) {
      if(buf.length()>0) buf.append(", ");
      buf.append(key+":"+types.get(key).getSimpleName());
    }
    log(indent+"["+strBufDispose(buf)+"]");
    
    buf=strBufGet();
    for(String key : (Set<String>)o.keys()) {
      String type=getType(o,key).getName();
      if(type.lastIndexOf('.')>-1)
        type=type.substring(type.lastIndexOf('.')+1);
      if(!(type.contains("JSONArray") || type.contains("JSONObject"))) {
        str.add(indent+key+" : "+type);
//        log(str.get(str.size()-1));
      }
    }

    for(String key : (Set<String>)o.keys()) {
      String type=getType(o,key).getName();
      if(type.lastIndexOf('.')>-1)
        type=type.substring(type.lastIndexOf('.')+1);
      
      JSONObject theObj=null;
//      if(type.contains("JSONArray")) theObj=o.getJSONArray(key).getJSONObject(0);
      if(type.contains("JSONObject")) {
        theObj=o.getJSONObject(key);
        str.add(indent+key+" : "+type);
//      log(str.get(str.size()-1));
      if(theObj!=null) printJSON(theObj,str,indent+"  ");
      }
      
    }

//    buf.append(' ').append('[').append(obj.size()).append(',').append(arr.size()).append(']');
//    str.add(indent+strBufDispose(buf));
//    
//    indent+="  ";
//    for(JSONObject tmp : obj) printJSON(tmp, str,indent);
//    for(JSONArray tmp : arr) printJSON(arr.get(0).getJSONObject(0), str,indent);
  }*/
  

}
