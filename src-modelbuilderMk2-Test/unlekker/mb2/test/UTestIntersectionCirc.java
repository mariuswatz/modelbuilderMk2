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

public class UTestIntersectionCirc extends UTest {
  UVertexList vl;
  UGeo mesh,cyl,plane;
  
  UVertex v1,v2,fv[];
  
  UFace f,fxz;
  
  UVertex a,b,ii;
  float R=400;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();
    
    v1=new UVertex();
    v2=new UVertex();
  }
  
  public void draw() {
    
    p.pushMatrix();
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();

    
    p.stroke(255);
//    UMB.ppush().protX(HALF_PI).prect(1000,1000).ppop();
    p.noFill();
    
    UMB.pellipse(v1, 400,400);
    UMB.pellipse(v2, 200,200);
    
    v2.set(p.mouseX-p.width/2,p.mouseY-p.height/2);
    fv=UIntersections.circleCircle2D(v1, 200, v2, 100);
    if(fv!=null) {
      UMB.pellipse(fv[0], 25,25);
      UMB.pellipse(fv[1], 25,25);
    }

    float D1= 0,D2= 0;
    float a[]=UIntersections.circleCircle2DRadians(v1, 200, v2, 100);
    if(a!=null) {
      D1=(a[1]-a[0]);
      D2=(UMB.mod(TWO_PI-a[1], TWO_PI)-a[0]);
      
//      if(p.abs(a[1])<p.abs(a[0])) {
//        float tmp=a[0];
//        a[0]=a[1];
//        a[1]=tmp;
//      }
//      a[1]=-a[1];
      UVertexList vl=UVertexList.arc(200, a[0],a[1],60);
      p.stroke(0,255,255);
      vl.close().draw();

      UMB.ppush().ptranslate(fv[0].mult(1.2f));
      p.text("a="+(int)(a[0]*RAD_TO_DEG),0,0);
      UMB.ppop().ppush().ptranslate(fv[1].mult(1.2f));
      p.text("b="+(int)(a[1]*RAD_TO_DEG),0,0);
      UMB.ppop();
    }

    
    p.popMatrix();
    p.fill(255);
    p.text(a==null ? "null" : 
      (int)(a[0]*RAD_TO_DEG)+" "+(int)(a[1]*RAD_TO_DEG)+
      " D "+(int)(D1*RAD_TO_DEG)+" "+(int)(D2*RAD_TO_DEG)
      , 10,p.height-15);
    p.text(fv==null ? "null" : UMB.str(fv), 10,p.height-5);
  }


  
}
