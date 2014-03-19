/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PGraphics;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.UFile;
import unlekker.mb2.util.UMB;

public class UTestSelector02 extends UTest {
  PGraphics gg;
  UVertexList vl;
  UGeo geo;
  UGeoGroup sel;  
  UVertex lastSel;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();

    geo=UGeo.cyl(100, 300, 12).translate(-150,0,0);
    log("1 "+geo.str());
    
    for(UVertex vv:geo.getV()) {
      UVertexList close=geo.getV().closeTo(vv, 1);
      if(close.size()>0) {
        logDivider(vv.ID+" "+vv.str()+" "+close.size());
        for(UVertex cv:close) {
          boolean eq=cv.equals(vv);
          log(cv.ID+" "+eq+" "+cv.distSimple(vv)+" "+ cv.str());        
        }
      }
    }

    log("\n");
    
    geo=new UGeo();
    geo.add(UGeoGenerator.meshBox(200,200,200, 10).translate(150,0,0));
    geo.center().scaleToDim(200);
    
    logDivider("\n2 "+geo.str());
    
    int cnt=0;
    for(UVertex vv:geo.getV()) {
      UVertexList close=geo.getV().closeTo(vv, 1);
      if(close.size()>0) {
        cnt+=close.size();
        logDivider((cnt)+" "+vv.ID+" "+vv.str()+" "+close.size());
        for(UVertex cv:close) {
          boolean eq=cv.equals(vv);
          UVertex dv=cv.delta(vv);
          eq=cv.distSimple(vv)>EPSILON;
          String ss=nf(cv.distSimple(vv),20);
          log(cv.ID+" "+eq+" "+
          ss+" "+
          nf(cv.distSimple(vv),6)+" "+ cv.str()+" "+dv.str());        
        }
      }
    }

    
    sel=new UGeoGroup(geo);
    
//    sel.add(geo.getGroup(rndInt(geo.sizeGroup())));
//    log(geo.strGroup());
  }

  public void draw() {
    p.pushMatrix();
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();

    p.stroke(255,0,0);
    if(sel.size()<1) geo.draw().drawNormals(10);
    else {
      pstroke(pcolor(0));
      sel.draw(true);
      pstroke(pcolor(255));
      sel.drawNormals(10);
    }
    if(lastSel!=null) UMB.pellipse(lastSel, 10);
    
    String s= null;
    s=sel.size()+" ";
    p.popMatrix();
    
    
    p.text(s,5,24);

  }

  public void keyPressed(char key) {
    if(key=='i') {
      sel.getVID();
    }

    if(key=='o') {
      ArrayList<Integer> id=sel.getVID();
      if(id!=null && id.size()>0) {
        for(int i:id) {
          UVertex vv=geo.getV(i);
          UVertex vn=geo.getVNormal(i).copy();
          
          float m=rnd(2,5);
            float rx=rndSigned(0.2f,1)*DEG_TO_RAD*30;
            float ry=rndSigned(0.2f,1)*DEG_TO_RAD*30;

          vn.mult(m).rotZ(rx).rotY(ry);
          vv.add(vn);
        }
      }
    }
    if(key=='p') {
      float m=rnd(2,5);
      for(UFace ff:sel.getF()) {
        float rx=rndSigned(0.2f,1)*DEG_TO_RAD*30;
        float ry=rndSigned(0.2f,1)*DEG_TO_RAD*30;
        ff.translate(ff.normal().copy().mult(m).
            rotZ(rx).rotY(ry));
      }
      
    }
    if(key=='P') geo.regenerateFaceData();

    if(key=='O') {
      for(int i=0; i<10; i++) {
        UFace rf=sel.getRndF();
        float m=rnd(2,10);
        rf.translate(rf.normal().copy().mult(m));
//        rnd.add(p.random(-10,10),p.random(-10,10),p.random(-10,10));
//        log(vid+" "+rnd.str());
      }
    }

    if(key==TAB) {
      UFace rf=sel.getRndF();
      sel.add(rf);
//      sel.addConnected(rf);
    }
    if(key=='g') {
      sel=geo.getGroup(rndInt(geo.sizeGroup()));
    }

    if(key=='r') {
      sel.clear();
    }

    if(key=='s') {
      geo.writeSTL(UFile.nextFile(p.sketchPath, name, "stl"));
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
