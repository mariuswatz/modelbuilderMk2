/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.awt.Frame;

import processing.core.PApplet;
import processing.core.PGraphics;
import unlekker.mb2.geo.UCurve;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UIntersections;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.UTriangulate;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMB;

public class UTestMath extends UTest {
  
  public void init() {
  }
  
  public void draw() {
    p.fill(255);
    float y=p.map(p.mouseY, 0, p.height-1, -1, 1);
    float x=circleXforY(y);
    p.text(nf(x)+" "+nf(y), 10,30);
    p.translate(p.width/2, p.height/2);
    p.lights();
//    main.nav.doTransforms();
    
    
    UVertex vv=new UVertex(x,y).mult(100);
    
    p.stroke(255,255,0);
    p.ellipse(0,0, 200,200);
    p.noFill();
    UMB.pellipse(vv, 10, 10);
    
    x=p.map(p.mouseX, 0, p.width-1, -1,1);
    y=circleYforX(x);
    vv.set(x,y).mult(100f);
    UMB.pellipse(vv, 10, 10);
  }

  
}
