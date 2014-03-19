package unlekker.mb2.doc;


import java.util.*;

import processing.core.*;
import unlekker.mb2.util.*;
import unlekker.mb2.externals.*;
import unlekker.mb2.geo.*;

public class UDocGenerator extends UDocUtil {
  public ArrayList<UPackage> pack=new ArrayList<UPackage>();
  public UConfig conf;
  public String packageNames[];
  
  public UDocGenerator(String confPath) {
    conf=new UConfig(confPath);
    
    path=conf.get("path");
    if(!path.endsWith("/")) path+="/";
    dataPath=conf.get("dataPath");
    if(!dataPath.endsWith("/")) dataPath+="/";
    log(dataPath+" "+path);
    
    String tmp=conf.get("packages");
    packageNames = tmp.split(",");

    for(String name:packageNames) {
      pack.add(new UPackage(name));
    }
    
    html=new UDocTemplate("UDoc.txt");
    
    for(UPackage p:pack) p.output();
    
  }
  
}