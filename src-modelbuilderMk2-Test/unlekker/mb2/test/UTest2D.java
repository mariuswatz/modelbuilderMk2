/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import processing.core.PApplet;
import processing.core.PGraphics;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMB;

public class UTest2D extends UTest {
  PGraphics gg;
  UVertexList vl;
  
  public void init() {
    gg=p.createGraphics(600, 600,JAVA2D);
    UMB.setGraphics(gg);

    vl=new UVertexList();
    for(int i=0; i<12; i++) {
      float deg=p.map(i, 0, 11, 0, TWO_PI);
      vl.add(new UVertex(200,0,0).rot(deg));      
    }
    
    vl.rotX(HALF_PI/2).rotY(HALF_PI/2);
    vl.translate(-50,0,0);
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
