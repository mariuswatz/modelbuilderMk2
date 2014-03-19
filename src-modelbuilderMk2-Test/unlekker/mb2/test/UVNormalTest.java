/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PApplet;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMB;

public class UVNormalTest extends PApplet {
  ArrayList<UVertexList> vvl;
  
  UVertexList vl,vl2;
  UGeo geo;

  public void setup() {
    size(600,600,OPENGL);
    
    UMB.setPApplet(this);
    UVertexList tri=new UVertexList();
    UVertex v=new UVertex(200,0,0);
    
    tri.add(v).
      add(v.rotY(-HALF_PI)).
      add(v.rotY(-HALF_PI)).
      add(v.rotY(-HALF_PI)).
      close();
    
    geo=new UGeo().triangleFan(tri,new UVertex(0,300,0)); 
    geo.triangleFan(tri,true);
    
    geo.enable(geo.COLORFACE);
    geo.center();
    
    int id=0,n=geo.sizeF();
    for(UFace ff:geo.getF()) {
      if((id++)%2==0)
        ff.setColor(color(100));
      else ff.setColor(color(200));   
      ff.log(ff.centroid().str());
      ff.log(ff.v[0].str());
    }
  }

  public void draw() {
    background(0);
    translate(width/2,height/2,-300);
    rotateX(map(mouseY, 0,height-1, -PI,PI));
    rotateY(map(mouseX, 0,width-1, -PI,PI));
    
    noStroke();
    geo.draw();
    
    noFill();
    for(UFace ff:geo.getF()) {
      drawFace(ff);
    }
  }

  private void drawFace(UFace ff) {
    UVertex c=new UVertex();
    UVertex n=new UVertex();
    
     c.set(ff.centroid());
     n.set(ff.normal()).mult(150);
     
     pushMatrix();
     translate(c.x,c.y,c.z);

     stroke(0,255,0);
     box(8);
     translate(n.x,n.y,n.z);
     stroke(0,255,255);     
     box(16);
     popMatrix();
     
     n.add(c);
     line(c.x,c.y,c.z,
       n.x,n.y,n.z);
//         c.log(c.x+" "+n.str());
  }
  
  
  static public void main(String args[]) {
    String sk[]=new String[] {
        "UVNormalTest",
        "UFileTest"
    };
    
    PApplet.main(new String[] {"unlekker.mb2.test."+sk[0]});
  }

  
}
