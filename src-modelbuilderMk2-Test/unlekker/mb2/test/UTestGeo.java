/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import processing.core.PGraphics;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UTask;

public class UTestGeo extends UTest {
  PGraphics gg;
  UVertexList vl,vl2;
  
  public void init() {
    gg=p.createGraphics(600, 600,JAVA2D);
    UMB.setGraphics(gg);

    vl=UVertexList.circle(100, 12).rotX(-HALF_PI);
    vl2=vl.copy().translate(0,50,0);
    
    UTask task=new UTask(name);
    UGeo geo=new UGeo();
    int nn=100;
    for(int i=0; i<nn; i++) {
      geo.quadstrip(vl, vl2);
      vl.translate(0,50,0);
      vl2.translate(0,50,0);
    }
    
    task.done();
  }
  
  public void draw() {
    gg.beginDraw();
    gg.background(100);
    UMB.pstroke(255).pnoFill();
    UMB.ptranslate(p.width/2, p.height/2);
    vl.draw();
    vl.bb().draw();
    gg.endDraw();
    
    p.image(gg, 0, 0);
  }

  
}
