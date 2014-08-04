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
import unlekker.mb2.geo.UGeoIO;
import unlekker.mb2.geo.UIntersections;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.USubdivision;
import unlekker.mb2.geo.UTriangulate;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UFile;
import unlekker.mb2.util.UMB;

public class UTestPrimitives extends UTest {
  UGeo geo,geo2;
  long last=0;
  int level=0;
  UFace fn;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();

//    geo=UGeo.box(100);
//    UMB.log(geo.bb().str());
    

    if(rndBool()) 
      geo2=UGeo.box(100).translate(-200,0,0);

    else geo2=UGeo.cyl(100, 200, 20).setColor(pcolor(0,255,255));
    geo2.translate(0,450,0);

    
    geo=UGeoGenerator.geodesicSphere(100, level);
    logf("level %d - f %d",level,geo.sizeF());
    log(geo.str()+" "+geo.bb().str());
    level=(level+1)%5;
    
    geo=UGeoGenerator.sphere(100, rndInt(10)*2+4);
//    USubdivision.subdivide(geo, SUBDIVCENTROID);
//    geo.vertexNormals();
//    
//    for(int i=0; i<geo.sizeF(); i+=3) {
//      int id=geo.getF(i).vID[2];
//      UVertex v=geo.getV(id);
//      UVertex vn=geo.getVNormal(id);
//      v.add(vn.copy().mult(rnd(2,15)));
//    }
  
    log(UGeoGenerator.listPrototypes());
//    geo=UGeoGenerator.get("icosahedron");
  }
  
  public void draw() {
    p.fill(255);
    p.lights();
    
    
    p.translate(p.width/2, p.height/2);
    main.nav.doTransforms();
    
    

    p.noFill();
    p.stroke(255,150,0);

    p.fill(200);
//    p.noStroke();
    geo.draw();
    if(p.mousePressed) {
      p.stroke(255,100,0);

      geo.drawVertexNormals(5).drawNormals(2);
    }
    
    
    //    if(p.keyPressed) {
//      if(p.millis()-last>1000) {
//        geo=USubdivision.subdivide(geo, UMB.SUBDIVMIDEDGES);
//        for(UFace f:geo.getF()) {
//          int col=p.lerpColor(0xFF330000, 0xFF00FFFF, p.random(1));
//          f.setColor(col);
//        }
//        last=p.millis();
//        geo.enable(geo.COLORFACE);
//      }
//    }

  }
  
  public void save(String filename) {
    geo.writeSTL(filename+".stl");
  }


  public void keyPressed(char key) {
    if(key=='r') geo.reverse(); 
    if(key=='R') {
      UFace f=rnd(geo.getF());
      f.translate(new UVertex(f.normal().copy().mult(rnd(10,30))));
    }
  }
  
}
