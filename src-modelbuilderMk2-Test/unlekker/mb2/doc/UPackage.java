package unlekker.mb2.doc;


import java.util.*;

import processing.core.*;
import unlekker.mb2.util.*;
import unlekker.mb2.externals.*;
import unlekker.mb2.geo.*;

public class UPackage extends UDocUtil {
  String path,name,nameShort;
  ArrayList<UClass> classes;
  
  public UPackage(String path) {
    this.path=path;
    name=path;
    name=name.substring(path.indexOf("unlekker"));
    name=name.replace('/', '.');
    nameShort=chop(name);
    logDivider(name);
    
    classes=new ArrayList<UClass>();
    ArrayList<String> cl=listPackage(path);
    for(String classStr:cl) {
      if(classStr.indexOf('$')<0) {
        classes.add(new UClass(classStr));
        log(last(classes).nameShort);
      }
    }
     
  }
  
  public void output() {
    for(UClass c:classes) c.output();
  }
  
}