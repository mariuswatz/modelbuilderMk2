package unlekker.mb2.doc;


import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import unlekker.mb2.util.UMB;

public class UDocUtil extends UMB {
  static public String path,dataPath;
  static public UDocTemplate html;
  static public String TAGNAME="[name]";
  static public String TAGRETURN="[return]";
  static public String TAGPARAM="[param]";
  
  public HashMap<String, String> params;
  
  public UDocUtil() {
    params=new HashMap<String, String>();
  }
  
  public String get(String key) {
    return params.get(key);
  }
  
  static public ArrayList<String> listPackage(String packageName) {
    ArrayList<String> str=new ArrayList<String>();
    
    List<Class<Object>> commands = new ArrayList<Class<Object>>();
    URL root = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".", "/"));

    UMB.log(root.toString());
    
    // Filter .class files.
    File[] files=null;
    try {
      files=new File(
          URLDecoder.decode(root.getFile(), "UTF-8")).listFiles(new FilenameFilter() {
          public boolean accept(File dir, String name) {
              return name.endsWith(".class");
          }
      });
    } catch (UnsupportedEncodingException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    // Find classes implementing UMB.
    
    for (File file : files) {
        String className = file.getName().replaceAll(".class", "");
        Class<?> cls= null;
        try {
          cls=Class.forName(packageName + "." + className);
          if(cls!=null) str.add(packageName+"."+className);
        } catch (ClassNotFoundException e) {
          // TODO Auto-generated catch block
          System.err.println(packageName + "." + className);
        }
        if (cls!=null && UMB.class.isAssignableFrom(cls)) {
            commands.add((Class<Object>) cls);
        }
    }

    return str;
  }
  
  static public String chop(String s) {
    int pos=s.lastIndexOf('.');
    if(pos>-1) s=s.substring(pos+1);
    
    return s;
  }

  static public String paramType(Method m, int id) {
    String str;

    Class<?>[] pType  = m.getParameterTypes();
    str=pType[id].getClass().getSimpleName();
    
    Type[] gpType = m.getGenericParameterTypes();
    String fmt="%s: %s%n";
    
    for (int i = 0; i < pType.length; i++) if(i==id){
      str=pType[i].getSimpleName();
        logf("%d Param %s<%s> | %s", id,pType[i],gpType[i],str);
    }
    
//    try {
//      Class<?> c = Class.forName(theClass.getCanonicalName());
//      Field f[]=c.getFields();
//      
//      
//      if(f!=null) {
//        for(Field ff:f) {
//          System.out.format("%s: Type: %s\n", str,ff.getType());
////          System.out.format("GenericType: %s%n", ff.getGenericType());
//
//        }
//      }
//
//        // production code should handle these exceptions more gracefully
//    } catch (Exception x) {
//        log("Err: "+theClass);
//    }
    
    if(str.indexOf('[')>-1) log("Has bracket: "+str);
    log(str);
    
    return str;
  }
    
  
  static public ArrayList<UMethod> listMethods(String className) {
    ArrayList<UMethod> meth=new ArrayList<UMethod>();
    
    Method[] methods = null;
    try {
      Class cls=Class.forName(className);
      methods = cls.getMethods();
      for (int i = 0; i < methods.length; i++) {
        String s=methods[i].toGenericString();
        if(s.indexOf("native")<0) {
          meth.add(new UMethod(methods[i]));
        }
      }
    } catch (SecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    Collections.sort(meth, new Comparator() 
    {

      public int compare(Object o1, Object o2) {
          return ((UMethod)o1).name.compareTo(((UMethod)o2).name);

          // it can also return 0, and 1
      }
     }    
);
    
    
    return meth;
  }
  
  static public String fMethod="<div class='method'><a href='%s'>%s</a> %s</div>";
  
  static public String strMethod(UMethod m) {
    String name=m.nameShort;
    
    return strf(fMethod,name,name,m.method.getReturnType().toString());
  }
  
  
  public void output() {
    
  }
  
}