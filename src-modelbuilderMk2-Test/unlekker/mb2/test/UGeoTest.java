/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PApplet;
import unlekker.mb2.geo.UCurve;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMB;

public class UGeoTest extends PApplet {
  UNav3D nav;
  
  UVertex dir,rot;
  UVertexList cp,cprot,vl,vl2;
  
  UVertexList bez;
  ArrayList<UVertexList> bezvl;
  Heading h;
  ArrayList<Heading> head;
  
  UGeo geo;
  boolean drawGeo;

  private UVertexList cpD;
  float TT;
  int TCNT;
  
  public void setup() {
    size(600,600, OPENGL);
    
    UMB.setPApplet(this);
    nav=new UNav3D();

//    exit();
    
    reinit();
  }

  private void reinit() {
    if(cp==null) getCP();
    
    TT=map((TCNT++)%100,0,99,0.04f,0.96f);
    cpD=delta(cp);
    head=new ArrayList<Heading>();
    for(UVertex hv:cpD) head.add(new Heading(hv));
    
    bezvl=new ArrayList<UVertexList>();
    int id=0;
    vl=UVertexList.rect(50, 50);
    
   
    for(Heading hh:head) {
      vl2=hh.applyInverse(vl.copy());
      float m=bezierPoint(0.2f, 1, 1, 0.2f, map(id,0,head.size()-1,0,1));
      bezvl.add(vl2.copy().scale(m).
          translate(cp.get(id++)));
//      bezvl.add(vl2.copy().
//          rotX(cv.x).rotZ(cv.y).rotX(cv.z).
//          translate(cp.get(id++)));
    }
    
    geo=new UGeo().quadstrip(bezvl);
  }

  private void getCP() {
    cp=new UVertexList();
    for(int i=0; i<4; i++) {
      UVertex vv=new UVertex(i*100,
          random(-300,300),
          random(-300,300));
      
//      if(i>0) {
//        UVertex last=cp.get(i-1);
//        vv.add(0,last.y,last.z);
//        vv.mult(1,0.5f,0.5f);
//      }
      
      cp.add(vv);
    }
    cp.translateNeg(cp.first());

    cp=UCurve.bezier(cp.toArray(), 40).center();
  }

  public void draw() {
    background(0);
//    if(rot==null) return;
    
    fill(255);
    text(nf(TT,0,2),10,10);
    
    translate(width/2,height/2);
    nav.doTransforms();
    stroke(255,0,0);
    noFill();
    
    cp.draw();
    vl2.draw();
    
    stroke(100);
    for(UVertexList l:bezvl) {
      l.draw();
    }
    geo.bb().draw();
    
    if(drawGeo) {
      fill(255);
      geo.draw();
    }
    

//    draw1();
//    
//    vl.draw();
//    vl2.draw();
//    dir.pline(new UVertex(), dir);
//    
//    vl.ptranslate(dir);
//    vl.draw();
    
  }

  private void draw1() {
    
    line(-100,0,100,0);
    line(0,100, 0,-100);
    
    lights();
    nav.doTransforms();
    
    stroke(255,0,0);
    vl2.draw();
    stroke(255,255,0);
    cp.draw();
//    fill(255);
    
    stroke(100);
    for(UVertexList l:bezvl) {
//      l.draw();
    }
    
    if(drawGeo) {
      fill(255);
      geo.draw();
    }
  }
  
  public UVertexList delta(UVertexList l) {
    UVertexList dl=new UVertexList();
    for(int i=0; i<l.size(); i++) {
      if(i<l.size()-1) dl.add(l.get(i+1).copy().sub(l.get(i)));
      else dl.add(dl.last().copy());
    }
    
    UVertex last=null;
    for(UVertex vv:dl) {
      if(last!=null) vv.mult(TT).add(last.copy().mult(1-TT));
      last=vv;
    }
    
    return dl;
  }
  
  public void keyPressed() {
    if(key==' ') drawGeo=!drawGeo;
    else if(key!=CODED) {
      if(keyEvent.isControlDown()) cp=null;
      reinit();
    }
  }

  static public void main(String args[]) {
    String sk[]=new String[] {
        "UGeoTest",
        "UFileTest",
        "UGeoTest"
    };
    
    PApplet.main(new String[] {"unlekker.mb2.test."+sk[0]});
  }

  
}
