/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PGraphics;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.UMB;

public class UTestQuadGroup extends UTest {
  UVertexList vl;
  UGeo geo;
  ArrayList<UQuad> quad;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();

    vl=UVertexList.line(500, 11);
    geo=new UGeo().quadstrip(vl,vl.copy().translate(0,100,0));
    geo.center();
    
    quad=geo.getQ();
    log("quads: "+quad.size());
    log("v: "+quad.get(0).getV().length);
    
    for(UQuad q:quad) {
      q.translate(0,rnd(50),0);
      q.setColor(p.color(255,rnd(255),0));
    }

    for(UFace q:geo.getF()) {
      
      q.setColor(p.color(0,255,rnd(255)));
    }

    geo.enable(COLORFACE);
    log(hex(geo.getF(10).col)+" "+geo.getF(10).col);
    
    UMB.log("groups "+geo.sizeGroup()+" f="+geo.sizeF()+
        " v="+geo.sizeV()+
        " | "+geo.getGroup(0).str());
    
    
    
  }

  public void draw() {
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();
    
    
    p.stroke(100);
    p.strokeWeight(1);
    
    for(UQuad q:quad) {
      p.fill(q.col);
      q.draw().drawNormal(20);
    }

    p.noFill();
    p.fill(p.color(255));

    p.translate(0,150,0);
    geo.draw().drawNormals(20);
    
    
  }

  
}
