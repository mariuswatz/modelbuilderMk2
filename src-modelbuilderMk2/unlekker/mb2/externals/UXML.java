package unlekker.mb2.externals;

import java.util.ArrayList;

import processing.data.XML;
import unlekker.mb2.util.UMB;

public class UXML extends UMB {

  public static int cnt=0;
  
  public static void findNode(XML xml,String name,ArrayList<XML> l) {
    cnt++;
    if(xml.getName().compareTo(name)==0) {
      l.add(xml);
      log(cnt+" | "+name+" found. l="+l.size());
    }
    if(xml.hasChildren()) {
      for(XML x:xml.getChildren()) findNode(x, name, l);
    }
  }
  
}
