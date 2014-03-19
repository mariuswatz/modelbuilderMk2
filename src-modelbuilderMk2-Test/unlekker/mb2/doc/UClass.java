package unlekker.mb2.doc;


import java.util.*;

import processing.core.*;
import unlekker.mb2.util.*;
import unlekker.mb2.externals.*;
import unlekker.mb2.geo.*;

public class UClass extends UDocUtil {
  String name,nameShort;
  ArrayList<UMethod> meth;
  
  public UClass(String name) {
    this.name=name;
    nameShort=chop(name);
    
    meth=listMethods(name);
    ArrayList<String> str=new ArrayList<String>();
    for(UMethod m:meth) str.add(m.nameShort);
    Collections.sort(str);
    log(str(str,',',ENCLSQ));
  }
  
  public void output() {
    ArrayList<String> out=new ArrayList<String>();
    
    UDocTemplate temp=new UDocTemplate("UMethod.txt");

    html.add(html.pre, out);
    
    
    for(UMethod m:meth) if(m!=null){
      temp.output(m, out);
    }
    html.add(html.post, out);
    
    papplet.saveStrings(dataPath+nameShort+".html", out.toArray(new String[out.size()]));
  }

 
}