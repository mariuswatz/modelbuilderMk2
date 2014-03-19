/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.awt.Frame;

import processing.core.PApplet;
import processing.core.PGraphics;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UHeading;
import unlekker.mb2.geo.UIntersections;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.UTriangulate;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMB;

public class UTestHeading01 extends UTest {
  UGeo geo,geoColl;
  UVertex v1,v2;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();

    geo=UGeo.box(20,20,5);
    for(UVertex vv:geo.getV()) if(vv.z>0) vv.mult(0.25f,0.25f,1);
    
    
    if(v1==null) v1=new UVertex(100,0,0);
    else v1.set(v2);
    v2=new UVertex(100,0,0).rotY(p.random(-1,1)*PI).rotZ(p.random(-1,1)*PI).add(v1);
    
    geo=UHeading.align(geo, v1, v2);
    if(geoColl==null) geoColl=geo.copy();
    else geoColl.add(geo);
  }
  
  public void draw() {
    p.fill(255);
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();

    p.noFill();
    p.stroke(255,255,0);
    UMB.pline(v1,v2);
    
    p.stroke(255,0,0);
    geo.draw();
    
    p.noStroke();
    p.fill(255);
    geoColl.draw();
  }

  
}
