/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PGraphics;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.UMB;

public class UTestEdgeList02 extends UTest {
  PGraphics gg;
  UVertexList vl;
  UGeo geo;
  UEdgeList l;
  int sel=0,self=0;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();

    geo=UGeoGenerator.meshPlane(100, 100, 5);
    l=geo.getEdgeList();
    
  }

  public void draw() {
    p.pushMatrix();
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();
    
    p.stroke(100);
    p.strokeWeight(1);
//    p.fill(255);
    p.noFill();
    geo.draw();
    
    p.noFill();
    p.strokeWeight(5);
    
    int cnt=0;
//    for(UEdge e:l) if((cnt++)==sel) {
//      
////    e.ppush().ptranslate(0,0,(cnt%10)*3);
//    p.stroke(255,0,0);
//    e.draw();
//    e.pnoStroke().pfill(color(255));
//    for(UFace ff:e.getF()) {
//      pfill(color(255));
//      ff.draw();
//      UFace conn[]=ff.connected();
//      pfill(color(0,255,255));
//      if(conn[0]!=null) conn[0].draw();
//      if(conn[1]!=null) conn[1].draw();
//      if(conn[2]!=null) conn[2].draw();
//    }
////    e.ppop();
//  }
    
    cnt=0;
    String s= null;
    for(UFace f:geo.getF()) if((cnt++)==self) {
      
//    e.ppush().ptranslate(0,0,(cnt%10)*3);
      p.stroke(255,0,0);
//    e.draw();
//    e.pnoStroke().pfill(color(255));
      pfill(pcolor(255));
      f.draw();
      UFace conn[]=f.connected();
      pfill(pcolor(0,255,255));
      if(conn[0]!=null) conn[0].draw();
      if(conn[1]!=null) conn[1].draw();
      if(conn[2]!=null) conn[2].draw();
      
      s="Connected: "+f.ID+" >  n="+f.sizeConnected()+" | ";
      for(UFace fc:conn) s+=" "+(fc==null ? NULLSTR : fc.ID);
//    e.ppop();
      
  }
    p.popMatrix();
    p.text(s,5,24);

  }

  public void keyPressed(char key) {
    if(key==TAB) self=(self+1)%geo.sizeF();
//    else if(key==TAB) sel=(sel+1)%l.size();
  }
}
