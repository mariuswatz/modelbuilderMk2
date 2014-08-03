package unlekker.mb2.test.data;

import java.util.ArrayList;

import processing.core.PVector;
import unlekker.data.UDataList;
import unlekker.data.UDataPoint;
import unlekker.data.UTime;
import unlekker.mb2.test.UTest;

public class UTestDataList extends UTest {
  UTime time;

  UDataList data;

  public void init() {
    datalistDemo(); 

  }

  public void draw() {
    
    p.pushMatrix();
    p.translate(p.width/2, p.height/2);
    p.noFill();
    p.strokeWeight(1);
    p.stroke(255,100);
    
    p.beginShape();
    for(UDataPoint pt : data) {
      // we need to use cast since getObject() returns Object
      PVector pv=(PVector)pt.getObject("pos");
      p.vertex(pv.x,pv.y);
    }
    p.endShape();

    for(UDataPoint pt : data) {
      p.strokeWeight(pt.getFloat("strokeWeight"));
      p.stroke(pt.getInt("col"));
      
      PVector pv=(PVector)pt.getObject("pos");
      float rad=pt.getFloat("rad");
      
      p.ellipse(pv.x,pv.y, rad,rad);
    }

    
    p.popMatrix();
  }
  
  void datalistDemo() {
    log("\n-----------------------");
    log("datalistDemo()");

    data=new UDataList();  

    // p.randomfill list with p.randomized data points  
    for(int i=0; i<100; i++) data.add(randomDataPoint());
    
    // Show information about the data we've parsed
    log("\nData:\t"+data.size()+" entries.");
    log(data.getTimeRange().str());
    
    for(UDataPoint tmp : data) log(tmp.str());
    
    float[] bb=data.boundsFloatList("rad");
    log("'rad' bounds: "+data.boundsToString(bb));
    
    ArrayList<PVector> o=data.getObjectList("pos",PVector.class);
    for(PVector pv : o) log(pv.toString());
  }
  
  UDataPoint randomDataPoint() {
    if (time==null) { // initialize time
      time=new UTime();
      time.setDate(2014, 1, 1);
      time.setStartOfDay();
    }
    
    UDataPoint pt=new UDataPoint();
    pt.timeSet(time.get());

    float T=pt.time().get();

    pt.addFloat("rad", p.noise(T*0.0001f)*100);
    pt.addFloat("strokeWeight", p.random(1,10));
    pt.addInt("col", p.color(255,0,p.random(50,255)));
    
    // add p.random PVector position
    PVector pos=new PVector(p.random(0.05f,0.275f)*(float)p.width,0);
    pos.rotate(p.random(TWO_PI));
    pt.addObject("pos", pos); 

    // p.randomly increment time
    time.addHour((int)p.random(1, 13));
    time.add((int)p.random(3600)*1000);
    
    return pt;
  }

}
