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

public class UTestVertexList extends UTest {
  UVertexList vl,vl2;
  
  
  public void init() {
    vl=UVertexList.circle(100, 20);
    log(vl.str());
    float a=rnd(HALF_PI,PI)*0.5f;
    vl.rotZ(a);
    log(vl.str());
    
    vl2=UVertexList.circle(200, 20).rotZ(rnd(HALF_PI,PI));
    
  }
  
  public void draw() {
    p.translate(p.width/2, p.height/2);
    
    p.noFill();
    
    p.stroke(255,255,0);
    vl.draw();
    p.stroke(255,0,0);
    vl2.draw();
    
    for(int i=0; i<vl.size(); i++) {
      float r=i;
      r=r*0.5f+3;
      
      p.stroke(255,255,0);
      UMB.pellipse(vl.get(i), r);
      p.stroke(255,0,0);
      UMB.pellipse(vl2.get(i), r);      
      
      pline(vl.get(i),vl2.get(i));
    }
  }

  public void keyPressed(char key) {
    if(key=='r') vl2.rotZ(rnd(TWO_PI));
    if(key=='q') vl2.reorderToPoint(vl.first());
    if(key=='w') vl2.reorderToAngle(vl.first().angle2D(XY),XY);
  }
  
}
