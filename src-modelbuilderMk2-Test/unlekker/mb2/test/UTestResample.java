/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.awt.Frame;

import processing.core.PApplet;
import processing.core.PGraphics;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UIntersections;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.UTriangulate;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMB;

public class UTestResample extends UTest {
  UVertexList vl,vlResampled;
  UGeo geo;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();

    vl=new UVertexList();
    int n=UMB.rndInt(40,100);
    for(int i=0; i<n; i++) {
      float deg=p.map(i,0,n,0,TWO_PI);
      float r=p.random(100,200);
      vl.add(new UVertex(r,0).rotZ(deg));
    }
    vl.close();
    
    vlResampled=vl.resample(UMB.rndInt(10,n-5));
    
    p.fill(255);
    geo=new UGeo().triangulation(vlResampled).translate(0,0,-10);
    for(UFace ff:geo.getF()) ff.setColor(p.color(p.random(255)));
    geo.enable(geo.COLORFACE);

  }
  
  public void draw() {
    p.fill(255);
    p.text("vl "+vl.size()+" resampled "+vlResampled.size(), 10,p.height-10);
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();

    p.noFill();
    p.stroke(255);
    vl.draw();

    p.stroke(255,100,0);
    vlResampled.draw();

    p.noStroke();
    geo.draw();
  }

  
}
