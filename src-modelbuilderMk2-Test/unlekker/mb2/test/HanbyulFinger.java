/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PApplet;
import unlekker.mb2.geo.*;
import unlekker.mb2.geo.UGeoIO;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMB;

public class HanbyulFinger extends PApplet {
  ArrayList<UVertexList> vvl;
  
  UVertexList vl,vl2;
  
  
  UGeo geo;
  UNav3D nav;
  
  int currFace=-1;
  ArrayList<Integer> selected;
  
  public void setup() {
    size(600,600, OPENGL);
    
    String filename="C:/Users/Marius/Dropbox/40 Teaching/2013 ITP/ITP-Parametric - Resources/Models";
    filename=filename+"/finger2b.stl";
    geo=UGeoIO.readSTL(this, filename);
    geo.center().scale(4);
    UMB.setPApplet(this);
    nav=new UNav3D();
    selected=new ArrayList<Integer>();
  }

  public void draw() {
    background(0);
    
    translate(width/2,height/2);
    lights();
    nav.doTransforms();
    
    stroke(255);
    fill(255);
    
    geo.draw();
    
    if(currFace>-1) {
      UVertex[] v=geo.getF().get(currFace).getV();
      fill(255,0,0);
      beginShape(TRIANGLE);
      UMB.pvertex(v);
      endShape();
    }
    
    fill(0,255,0);
    if(selected.size()>0) for(int id:selected){
      UVertex[] v=geo.getF().get(id).getV();
      beginShape(TRIANGLE);
      UMB.pvertex(v);
      endShape();
    }

  }

  public void checkSelection() {
    if(selected.contains(currFace)) selected.remove(currFace);
    else selected.add(currFace);
    println("Selected: "+selected.size()+" "+UMB.str(selected));
  }
  
  public void keyPressed() {
    if(key=='+') currFace=(currFace+1)%geo.sizeF();
    if(key==' ') checkSelection();
  }
  
  
  static public void main(String args[]) {
    String sk[]=new String[] {
        "HanbyulFinger",
        "UGeoTest",
        "UFileTest"
    };
    
    PApplet.main(new String[] {"unlekker.mb2.test."+sk[0]});
  }

  
}
