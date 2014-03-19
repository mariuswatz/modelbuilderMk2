/*
 * modelbuilderMk2

 */
package unlekker.mb2.test;

import java.awt.Frame;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PGraphics;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UGeoGenerator;
import unlekker.mb2.geo.UIntersections;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.USubdivision;
import unlekker.mb2.geo.UTriangulate;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMB;

public class UTestNormal extends UTest {
  UGeo geo,geo2,geoFan;
  long last=0;
  
  UFace fn;
  int cnt=2;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();

//    geo=UGeo.box(100);
//    UMB.log(geo.bb().str());
    

    geo=new UGeo();
    UVertexList vl=UVertexList.circle(200, 10);
    UVertexList vl2=vl.copy().translate(0,0,-50);
        
    
    geoFan=new UGeo().triangleFan(vl,true).triangleFan(vl2);
    geoFan.quadstrip(vl,vl2);
    geoFan.center().translate(0,0,-300);
    geoFan.setColor(pcolor(255,0,0)).enable(COLORFACE);
    
    geo.quadstrip(vl, vl2);
    
    
    
    ArrayList<UVertexList> stack=new ArrayList<UVertexList>();
    stack.add(vl);
    stack.add(vl.copy().translate(0,0,-50));
//    stack.add(vl.copy().translate(0,0,-150));
    
    geo2=new UGeo().quadstrip(stack);
    geo2.translate(0,0,-100).setColor(pcolor(255,0,0)).enable(COLORFACE);
    
    
    
    geo=UGeo.cyl(150, 300, 9);
    geo=UGeoGenerator.meshBox(150, 300,150, 9);
    geo.add(geoFan);
    
    geo.writeSTL(p.sketchPath("Prim.stl"));
  }
  
  public void draw() {
    p.fill(255);
    p.lights();
    p.translate(p.width/2, p.height/2);
    main.nav.doTransforms();

    p.noFill();
    p.stroke(255,150,0);
    
    if(cnt==0) geo.drawNormals(10);
    if(cnt==1) geoFan.drawNormals(10);
    if(cnt==2) {
//      geo.drawNormals(10);
      
      p.stroke(255,0,0);
      geo.drawVertexNormals(10);
    }
//    geo2.drawNormals(10);
//    geoFan.drawNormals(10);

    p.stroke(100);
    p.fill(200);
    if(cnt==0 || cnt==2) geo.draw();
    if(cnt==1) geoFan.draw();
//    geo2.draw();
//    p.noStroke();
//    geoFan.draw();
    

  }

  public void keyPressed(char key) {
    if(key=='r') geo.reverse(); 
    if(key=='o') {
      for(int i=0; i<10; i++) {
        int vid=(int)p.random(geo.sizeV());
        UVertex rnd=geo.getV(vid);
        UVertex n=geo.getVNormal(vid).copy();
        n.rotX(rndSigned(0.2f,1)*DEG_TO_RAD*10);
        n.rotY(rndSigned(0.2f,1)*DEG_TO_RAD*10);
        
        n.mult(10);
        rnd.add(n);
//        rnd.add(p.random(-10,10),p.random(-10,10),p.random(-10,10));
//        log(vid+" "+rnd.str());
      }
      
      long t=System.currentTimeMillis();
      geo.regenerateFaceData();
      logDivider("ttt"+(System.currentTimeMillis()-t));
    }

    if(key==TAB) cnt=(cnt+1)%3;
  }
  
}
