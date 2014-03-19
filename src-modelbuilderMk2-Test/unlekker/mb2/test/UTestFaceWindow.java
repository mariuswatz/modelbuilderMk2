/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.awt.Frame;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PGraphics;
import unlekker.mb2.geo.UCurve;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UGeoGenerator;
import unlekker.mb2.geo.UGeoIO;
import unlekker.mb2.geo.UGeoTransformer;
import unlekker.mb2.geo.UIntersections;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.UTriangulate;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMB;

public class UTestFaceWindow extends UTest {
  UVertexList vl,vl2;
  UGeo geo,geo2;
  UVertex vD[];
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();

    UVertex vv=new UVertex(100,0);
    geo=UGeoGenerator.geodesicSphere(100, 0);
    
    
    geo=UGeoGenerator.meshPlane(500, 500, 10);
    float m1=geo.bb().min.x;
    float m2=geo.bb().max.x;
    
    for(UVertex tmp:geo.getV()) {      
      float a=map(tmp.x,m1,m2, 0,1);
      float b=map(tmp.y,geo.bb().min.y,geo.bb().max.y, 0,1);
      tmp.add(0,0,p.sin(a*b*2*TWO_PI-2*PI)*150);
    }

    int n=geo.sizeF(),cnt=0;
    while((cnt++)<n) {
      geo.addFace(
          UGeoTransformer.window(geo.getF(0),20,0,true));//rndBool()));
      geo.remove(0);
    }
      
    
//
//    UVertex c=geo.getF(0).centroid();
//    
//    for(int i=0; i<3; i++) {
//      vD[i].set(c).sub(vf[i]).norm(10);
//      vD[i].add(vf[i]);
//    }
//    
//    geo.addFace(vD);
    
//    for(UFace ff:geo.getF()) ff.setColor(
//        p.color(rnd(100,255),rnd(100,255),rnd(100,255)));
    
    
//    geo.fixOrientation();
    
    n=geo.sizeF();
    for(int i=0; i<n; i++) {//geo.sizeF(); i++) {
      UFace ff=geo.getF(i);
      ff.setColor(p.color(255,255,255));
//      if(ff.isClockwise()) {
//        log("clockwise");
////        geo.remove(ff);
////        ff.reverse().normal(true);
////        ff.translate(ff.normal().copy().mult(10));
//        ff.setColor(p.color(255,0,0));
//      }

    }
    
    
    geo.enable(COLORFACE);
    geo.regenerateFaceData();
    geo.setColor(p.color(255));
    
    geo2=UGeoGenerator.extrude(geo, 10, true);

    geo=new UGeo().quadstrip(
        UVertexList.circle(200, 18),
        UVertexList.circle(220, 18).translate(0,0,50));
    geo=UGeoGenerator.meshBox(400, 400, 400, 6);
    
    cnt=50;
    while((cnt--)>0) {
      int id=rndInt(geo.sizeV());
      geo.getV(id).add(geo.getVNormal(id).copy().mult(rnd(15,50)));
    }
    n=geo.sizeF();
    cnt=0;
    while((cnt++)<n) {
      geo.addFace(
          UGeoTransformer.window(geo.getF(0),20,0,true));//rndBool()));
      geo.remove(0);
    }
    geo.extrudeSelf(-20, true);

    
    geo2.translate(geo.dim().x+50,0,0);
    
  }
  
  public void draw() {
    p.translate(p.width/2, p.height/2);
    p.lights();

    main.nav.doTransforms();
    p.noFill();
    p.fill(255);
    p.stroke(255);

    geo.draw();
//    geo2.draw();

    p.stroke(255,0,0);
    geo.drawVertexNormals(10);
//    geo2.drawVertexNormals(10);

  }
  
  public void window(UFace f,float r) {
    UVertex vf[]=f.getV();
        
    vD = new UVertex[] {
        vf[1].copy().sub(vf[0]),
        vf[2].copy().sub(vf[1]),
        vf[0].copy().sub(vf[2]),
    };

    log(vD);
    
    for(UVertex tmp:vD) tmp.norm(r);
    
    int cnt=0;
    
    vl=new UVertexList();
    vl.add(vf[0].copy().add(vD[0]));
    vl.add(vf[1].copy().sub(vD[0]));
    vl.add(vf[1].copy().add(vD[1]));
    vl.add(vf[2].copy().sub(vD[1]));
    vl.add(vf[2].copy().add(vD[2]));
    vl.add(vf[0].copy().sub(vD[2]));
    
    vl.add(vl.get(0).copy().lerp(0.5f, vl.get(5)));
    vl.add(vl.get(1).copy().lerp(0.5f, vl.get(2)));
    vl.add(vl.get(3).copy().lerp(0.5f, vl.get(4)));

    UVertex vn=f.normal().copy().mult(r);
    for(int i=0; i<3; i++) vl.get(i+6).add(vn);
    
    UVertex v1=vl.get(6);
    UVertex v2=vl.get(7);
    UVertex v3=vl.get(8);
    
    geo.addFace(v1,v2,v3);
    
    geo.addFace(vf[0],vf[1],v1);
    geo.addFace(v1,vf[1],v2);
    geo.addFace(vf[1],vf[2],v2);
    geo.addFace(v2,vf[2],v3);
    geo.addFace(vf[2],vf[0],v3);
    geo.addFace(v3,vf[0],v1);
//    geo.addFace(vl.get(0),vl.get(5),vf[0]);
//    geo.addFace(vl.get(1),vl.get(2),vf[1]);
//    geo.addFace(vl.get(3),vl.get(4),vf[2]);

  }

  public void save(String filename) {
    ArrayList<UGeo> models=new ArrayList<UGeo>();
    models.add(geo);
    models.add(geo2);
    
    UGeoIO.writeSTL(filename+".stl",models);
  }

  public void keyPressed(char key) {
  }
  
}
