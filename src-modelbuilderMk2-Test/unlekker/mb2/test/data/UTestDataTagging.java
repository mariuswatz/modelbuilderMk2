package unlekker.mb2.test.data;

import java.util.ArrayList;

import unlekker.mb2.test.UTest;
import unlekker.data.*;

public class UTestDataTagging extends UTest {
  
  public void init() {
    UDataPoint pt=new UDataPoint();
    
    pt.addFloat("f1", 123.3f);
    pt.addInt("i1", 1033);

    ArrayList<String> l=new ArrayList<String>();
    l.add("test");
    l.add("test2");
    
    pt.addObject("list", l);
    
    Object o=pt.getObject("list");
    log(o.getClass().getName());
    
    ArrayList<String> l2=(ArrayList<String>)o;
    log(l2);

    
    logDivider();
    for(String tmp : pt.getKeys()) {
      log(tmp+TAB+pt.getValue(tmp));
      
    }
  }
  
  
}
