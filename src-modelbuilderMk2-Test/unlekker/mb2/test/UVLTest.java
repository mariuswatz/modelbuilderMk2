/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PApplet;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UGeoIO;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMB;
import unlekker.mb2.util.UFile;

public class UVLTest extends PApplet {
  ArrayList<UVertexList> vvl;
  
  UVertexList vl,vl2;
  UGeo geo;
  UNav3D nav;
  
  public void setup() {
    size(600,600, OPENGL);
    UMB.setPApplet(this);
    
    // create navigation tool
    nav=new UNav3D();
    
    build();
  }

  void build() {
    vl=new UVertexList();
    UVertex v=new UVertex(100,0);
    for(int i=0; i<12; i++) {
      vl.add(v.copy().rotZ(map(i,0,12,0,TWO_PI)).
          mult(vl.rnd(1,2)).
          add(0,0,random(-100,100)));
    }
    vl.add(vl.first());
    
    vl.removeDupl(false);
    vl.center();
    
    vl.log(vl.bb+" "+vl.str());
    vl.log(vl.strWithID());
    vl.log("NODUPL "+vl.isEnabled(vl.NODUPL));
    vl.log(vl.get(1).equals(vl.last())+" "+ vl.get(1).distSimple(vl.last()));
    
    vl.log(vl.bb().str());
    vl.log(vl.centroid().str());
    vl.log(vl.bb.str()+" "+vl.str());
    
    
    geo=new UGeo().triangleFan(vl);
  }

  public void draw() {
    background(0);
    
    translate(width/2,height/2);
    lights();
    
    // execute navigation transforms
    nav.doTransforms();
    
    
    noStroke();
    fill(255,255,0, 100);
    geo.draw();
    
    noFill();
    stroke(255);
    ellipse(0,0, 5,5);
    
    vl.pline(vl.centroid(), new UVertex());    
    vl.draw();
    
   
    stroke(100,100,100,50);
    geo.bb().draw();
    
  }

  static public void main(String args[]) {
    String sk[]=new String[] {
        "UVLTest",
        "UFileTest"
    };
    
    PApplet.main(new String[] {"unlekker.mb2.test."+sk[0]});
  }

  
}
