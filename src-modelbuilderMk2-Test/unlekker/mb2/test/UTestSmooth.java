/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.awt.Frame;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.UMB;

public class UTestSmooth extends UTest {
  UVertexList vl,vlSmooth;
  int smoothLevel;
  
  UVertex a,b,ii;
  float R=400;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();
    
    vl=new UVertexList();
    int n=30;
    for(int i=0; i<n; i++) {
      float t=UMB.map(i, 0, n, 0, TWO_PI);
      vl.add(new UVertex(UMB.rnd(100,150),0).rotZ(t));
    }
    
    if(UMB.rndBool()) vl.close();
    smoothLevel=UMB.rndInt(1,8);
    vlSmooth=UVertexList.smooth(vl, smoothLevel);
    UMB.log(vl.size()+" "+vl.isClosed()+" "+vlSmooth.size());
  }
  
  public void draw() {
    main.background(255f);
    
    main.stroke(255,200,0);
//    main.drawGrid();
    
    p.pushMatrix();
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();

    p.noFill();
    p.stroke(0);
    vl.draw();
    p.stroke(255,0,0);
    vlSmooth.draw();

    p.popMatrix();
    
    String str="Closed? "+(vl.isClosed()+
        " smoothLevel"+smoothLevel);
    p.text(str, 10, p.height-5);
  }


  
}
