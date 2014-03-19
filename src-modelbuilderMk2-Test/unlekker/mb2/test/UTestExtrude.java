/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.awt.Frame;

import processing.core.PApplet;
import processing.core.PGraphics;
import unlekker.mb2.geo.UCurve;
import unlekker.mb2.geo.UEdge;
import unlekker.mb2.geo.UEdgeList;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UGeoGenerator;
import unlekker.mb2.geo.UIntersections;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.UTriangulate;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMB;

public class UTestExtrude extends UTest {
  UGeo geo,geo2;
  UEdgeList border;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();

    geo=UGeoGenerator.meshPlane(400, 400, 20);
    int n=10;
//    while((n--)>0) geo.remove(rndInt(geo.sizeF()));
    
    log(geo.str());
    float m1=geo.bb().min.x;
    float m2=geo.bb().max.x;
    
    for(UVertex vv:geo.getV()) {      
      float a=map(vv.x,m1,m2, 0,1);
      float b=map(vv.y,geo.bb().min.y,geo.bb().max.y, 0,1);
      vv.add(0,0,p.sin(a*b*PI)*150);
    }
    for(UVertex vv:geo.getV()) {
      float a=map(vv.x,m1,m2, -0.2f,0.2f)*HALF_PI;
      vv.rotY(a);
    }
    geo.taint();
    
    geo.rotX(-HALF_PI).center();
    
    border=geo.getEdgeList();
    border=border.getBoundary();
    
    geo2=UGeoGenerator.extrude(geo, 10,true);
    
  }
  
  public void draw() {
    p.pushMatrix();
    
    p.lights();
    p.translate(p.width/2, p.height/2);
    main.nav.doTransforms();

    if(p.mousePressed) {
      p.stroke(255,255,0);
      p.strokeWeight(1);
      p.noFill();
      float r=5;
      
      UVertex last=null;
//      for(UVertex vv:borderV) {
////        pellipse(vv, r);
//        if(last!=null) pline(last,vv);
//        last=vv;
//        r+=0.25f;
//      }
      
      p.stroke(0,50,100);
      geo.draw();
      
      p.stroke(0,255,255);
      geo.drawVertexNormals(10);

      p.strokeWeight(2);
      for(UEdge e:border) e.draw();

    }
    else {
      drawExtrude();
    }
    
    p.popMatrix();
    
    
  }
  

  public void save(String filename) {
    geo2.writeSTL(filename+".stl");
  }


  private void drawExtrude() {
    p.strokeWeight(1);
    p.stroke(50);
    p.fill(255);
//    geo.draw();
//    geo.drawNormals(10);

    
    p.fill(255,255,0);
    geo2.draw().drawNormals(10);
    
  }

  public void keyPressed(char key) {
    if(key=='d')     geo.disable(SMOOTHMESH);

  }
}
