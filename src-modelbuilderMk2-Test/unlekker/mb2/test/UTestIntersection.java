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

public class UTestIntersection extends UTest {
  UVertexList vl;
  UGeo mesh,cyl,plane;
  
  UVertexList lines,inter;
  UVertex fv[];
  
  UFace f,fxz;
  
  UVertex a,b,ii;
  float R=400;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();
    UVertex v=new UVertex(1000,0,0);
    f=new UFace().set(
        v.copy(),
        v.copy().rotY(120*DEG_TO_RAD),
        v.copy().rotY(240*DEG_TO_RAD));
    f.reverse();
    UVertex.rotX(f.getV(), PI/6);
    UVertex.rotZ(f.getV(),PI*0.33f);
    
  
    
    UMB.log(f.getV());
    f.rotX(f.rnd(TWO_PI)).rotZ(f.rnd(TWO_PI));
    fv=UIntersections.faceYPlane(f, 0);
    lines=null;
    UMB.log(UMB.str(fv,' ',null));
    
    plane=UGeoGenerator.meshPlane(2000, 2000, 20).rotX(HALF_PI);
  }
  
  public void draw() {
    
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();

    
    p.stroke(255);
//    UMB.ppush().protX(HALF_PI).prect(1000,1000).ppop();
    p.noFill();
    
    if(f!=null) {
      UMB.ppush();      
      f.draw().drawNormal(100);
//      f.pline(f.getV()[0], f.centroid());
//      f.pline(f.getV()[1], f.centroid());
//      f.pline(f.getV()[2], f.centroid());
      UMB.ppop();
    }
    
    
    if(fv!=null ){      
      int cnt=0;
      for(UVertex vv:fv) if(vv!=null) {
        UMB.pstroke(p.color(255,(cnt++)%2*255,0));
        UMB.pline(vv,new UVertex());

        UMB.ppush().pstroke(0xffffffff).pfill(0xffff000).
        pellipse(vv,R,R).ppop();
      }
    }
    
    plane.draw();
    if(lines!=null) drawLines();
    else {
      
    }

 
  }

  private void drawLines() {

    
    for(int i=0; i<inter.size(); i++) {
      ii=inter.get(i);
      a=lines.get(i*2);
      b=lines.get(i*2+1);
      
      if (ii!=null) {        
        UMB.pnoFill().pstroke(p.color(255, 255, 0)).pline(a,ii);
        UMB.pstroke(p.color(255, 0, 128)).pline(b, ii);
        p.text(ii.str(), ii.x, 20);
        UMB.ppush().ptranslate(ii);
        p.box(10);
        p.box(1);
        UMB.ppop();
        
        UMB.ppush().ptranslate(b);
        p.box(5);
        UMB.ppop();
        
      }
      else 
        UMB.pstroke(0xffff0000).pline(a, b);
      
    }
  }

  
}
