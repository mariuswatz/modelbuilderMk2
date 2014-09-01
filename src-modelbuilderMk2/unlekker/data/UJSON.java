package unlekker.data;

import java.util.*;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;
import unlekker.mb2.util.UMB;

public class UJSON extends UMB {
  TreeMap<String,Class> typeMap;
  
  JSONObject o;
  
  public UJSON(JSONObject o) {
    this.o=o;
    
    getTypes();
    
//    String s="";
//    for(String key : typeMap.keySet()) {
//      if(s.length()>0) s+=", ";
//      s+=key+"|"+typeMap.get(key).getSimpleName();
//    }
//
//    log("["+s+"]");
  }
  
  public UDataList toDataList(String id) {
    UDataList l=new UDataList();    
    
    try {
      if(o.hasKey(id) && typeMap.get(id)==JSONArray.class) {
        JSONArray arr=o.getJSONArray(id);
        for(int i=0; i<arr.size(); i++) {
          l.add(new UJSON(arr.getJSONObject(i)).toData());
        }
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return l;
  }

  public UDataPoint toData() {
    return toData(new UDataPoint());
  }

  public UDataPoint toData(String id) {
    try {
      if(o.hasKey(id) && typeMap.get(id)==JSONObject.class) {
        log("toData("+id+")");
        return new UJSON(o.getJSONObject(id)).toData();
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return null;
  }


  public UDataPoint toData(UDataPoint obj) {
    for(String key : typeMap.keySet()) {
      Class cl=typeMap.get(key);
      
      if(cl==String.class) {
        obj.addString(key, o.getString(key)); 
      }
      else if(cl==Integer.class) {
        obj.addInt(key, o.getInt(key)); 
      }
      else if(cl==Long.class) {
        obj.addLong(key, o.getLong(key)); 
      }
      else if(cl==Float.class) {
        obj.addFloat(key, o.getFloat(key)); 
      }
      else if(cl==Double.class) {
        obj.addDouble(key, o.getDouble(key)); 
      }
      else if(cl==Boolean.class) {
        obj.addBoolean(key, o.getBoolean(key)); 
      }
      else {
        if(cl==JSONObject.class) {
          obj.addObject(key, toData(key));
        }
        if(cl==JSONArray.class) {
          obj.addObject(key, toDataList(key));
        }
      }      
    }
    
    return obj;
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
  
  
  ///////////////////////////
  // STATIC CONVENIENCE METHODS
  
  
  static public HashMap<Class, String> jsonTypeMap;
  

  static public JSONObject jsonCreate(String [] input) {
    JSONObject o=new JSONObject();
    for(int i=0; i<input.length; i+=2) {
      o.setString(input[i], input[i+1]);
    }
    return o;
  }
  
  static public Class jsonFieldType(JSONObject o,String key) {
    try {long val=o.getLong(key); return Long.class;} catch (Exception e) {}
    try {int val=o.getInt(key); return Integer.class;} catch (Exception e) {}
    try {float val=o.getFloat(key); return Float.class;} catch (Exception e) {}
    try {Double val=o.getDouble(key); return Double.class;} catch (Exception e) {}
    try {String val=o.getString(key); return String.class;} catch (Exception e) {}
    try {boolean val=o.getBoolean(key); return Boolean.class;} catch (Exception e) {}
    try {JSONArray val=o.getJSONArray(key); return JSONArray.class;} catch (Exception e) {}
    
    return JSONObject.class;
  }
  
  static public String[] jsonKeys(JSONObject o) {
    String res[]=(String[])o.keys().toArray(new String[o.size()]);
    Arrays.sort(res);
    return res;
  }

  static public JSONObject jsonRemove(JSONObject o,String keyList) {
    String tok[]=PApplet.split(keyList, ',');
    
    for(String s : tok) {
      if(o.hasKey(s)) o.remove(s);
    }
    
    return o;
  }

  static public String jsonAttrib(JSONObject o) {
    if(jsonTypeMap==null) {
      jsonTypeMap=new HashMap<Class, String>();
      jsonTypeMap.put(Integer.class, "int");
      jsonTypeMap.put(Long.class, "long");
      jsonTypeMap.put(Double.class, "int");
      jsonTypeMap.put(Float.class, "float");
      jsonTypeMap.put(String.class, "String");
      jsonTypeMap.put(Boolean.class, "bool");
      jsonTypeMap.put(JSONObject.class, "JSONObj");
      jsonTypeMap.put(JSONArray.class, "JSONArr");
    }
    
    StringBuffer buf=strBufGet();

    String keys[]=jsonKeys(o);

    for(String key : keys) {
      if(buf.length()>0) buf.append(',');
      
      Class cl=jsonFieldType(o, key);
            
      buf.append(key).append('=').append(jsonTypeMap.get(cl));
      try {
        if(cl==JSONObject.class && o.isNull(key)) 
          buf.append("[]");
        else if(cl==JSONObject.class) 
          buf.append(strf("[%d]",o.getJSONObject(key).size()));
        else if(cl==JSONArray.class && o.isNull(key))
          buf.append("[]");
        else if(cl==JSONArray.class) 
          buf.append(strf("[%d]",o.getJSONArray(key).size()));
      } catch (Exception e) {
//        e.printStackTrace();
      }
    }
    
    return "["+strBufDispose(buf)+"]";
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
