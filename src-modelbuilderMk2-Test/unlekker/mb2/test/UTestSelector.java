/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PGraphics;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.UMB;

public class UTestSelector extends UTest {
  PGraphics gg;
  UVertexList vl;
  UGeo geo;
  UGeoGroup sel;
  UEdgeList l;
  
  UVertex lastSel;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();

    geo=UGeoGenerator.meshPlane(100, 100, 16);
    l=geo.getEdgeList();
    sel=new UGeoGroup(geo);
    
//    sel.add(geo.getGroup(rndInt(geo.sizeGroup())));
    log(geo.strGroup());
  }

  public void draw() {
    p.pushMatrix();
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();
    
    sel.draw();
    if(lastSel!=null) UMB.pellipse(lastSel, 10);
    
    String s= null;
    s=sel.size()+" ";
    p.popMatrix();
    
    
    p.text(s,5,24);

  }

  public void keyPressed(char key) {
    if(key==TAB) {
      UFace rf=sel.getRndF();
      sel.add(rf);
//      sel.addConnected(rf);
    }
    if(key=='g') {
      sel=geo.getGroup(rndInt(geo.sizeGroup()));
    }

    if(key=='a') sel.addConnected();
    if(key=='v') {
      lastSel=geo.getV(rndInt(geo.sizeV()));
      int tries=100;
      while(sel.contains(lastSel) && tries>0) {
        lastSel=geo.getV(rndInt(geo.sizeV()));
        tries--;
      }
      sel.addConnected(lastSel);
    }
    if(key=='d') geo.remove(rndInt(geo.sizeF()));
    
    if(key==ENTER) sel.subdivide(SUBDIVCENTROID, true);

//    else if(key==TAB) sel=(sel+1)%l.size();
  }
}
