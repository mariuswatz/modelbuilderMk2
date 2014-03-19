/*
 * modelbuilderMk2

 */
package unlekker.mb2.test;

import java.awt.Frame;

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

public class UTestPrimitives02 extends UTest {
  UGeo geo,geo2;
  long last=0;
  
  UFace fn;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();

//    geo=UGeo.box(100);
//    UMB.log(geo.bb().str());
    
    int n=rndInt(4,11);
    geo=UGeoGenerator.meshBox(100, rndInt(100,200), rndInt(100,200), n);
//    geo=UGeoGenerator.meshBox(100, 150, 50, 4);
  }
  
  public void draw() {
    p.fill(255);
    p.lights();
    p.translate(p.width/2, p.height/2);
    main.nav.doTransforms();

    p.noFill();
    p.stroke(255,150,0);
    geo.drawNormals(40);

    p.stroke(100);
    p.fill(200);
//    p.noStroke();
    geo.draw();
  }

  public void keyPressed(char key) {
    if(key=='r') geo.reverse(); 
  }
  
}
