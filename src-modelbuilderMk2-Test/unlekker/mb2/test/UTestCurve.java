/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.awt.Frame;

import processing.core.PApplet;
import processing.core.PGraphics;
import unlekker.mb2.geo.UCurve;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UIntersections;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.UTriangulate;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMB;

public class UTestCurve extends UTest {
  UVertexList cp,res;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();
    cp=new UVertexList().add(0,0).add(100,250,100).add(200,250,-100).add(300,0);
    cp.center();
    res=UCurve.bezier(cp.extractArray(0, 3), 20);
  }
  
  public void draw() {
    p.fill(255);
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();

    UMB.pnoFill().pstroke(p.color(255,0,0));
    cp.draw();
    UMB.pnoFill().pstroke(p.color(255,255,0));

    res.draw();
    for(UVertex vv:res) UMB.pellipse(vv, 10, 10);
  }

  
}
