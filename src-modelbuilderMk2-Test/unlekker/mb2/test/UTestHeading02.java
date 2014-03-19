/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import unlekker.mb2.geo.*;
import unlekker.mb2.util.UMB;

public class UTestHeading02 extends UTest {
  UGeo geo,geoColl;
  UVertex v1,v2;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();

    
    UVertexList cp=new UVertexList();
    for(int i=0; i<7; i++) {
      cp.add(new UVertex(i*100,p.random(-200,200)).rotX(p.random(-1,1)*PI));
    }
    
//    UVertexList path=UCurve.bezier(
//        new UVertex[] {
//            new UVertex(), 
//            new UVertex(100,p.random(-200,200)).rotX(p.random(-1,1)*PI), 
//            new UVertex(200,p.random(-200,200)).rotX(p.random(-1,1)*PI), 
//            new UVertex(300,p.random(-200,200)).rotX(p.random(-1,1)*PI)
//        },UMB.rndInt(10,50));
    
    UVertexList path=UCurve.bezier(cp,UMB.rndInt(10,50));
    UVertexList prof=UVertexList.circle(20, 8);
    geo=new UGeo();
    geo.quadstrip(UHeading.sweep(path, prof,0.4f));
  }
  
  public void draw() {
    p.fill(255);
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();

    p.noFill();
    p.stroke(255,255,0);
    p.line(-100, 0, 100, 0);
    p.line(0,-100, 0, 100);
    
//    UMB.pline(v1,v2);
    
    p.fill(255,0,0);
    geo.draw();
    
  }

  
}
