/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PGraphics;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UTask;

public class UTestTriangleFanBug extends UTest {
  PGraphics gg;
  ArrayList<UVertexList> vvl;

  UVertexList vl, vl2;
  UGeo geo;
  
  public void init() {
//    gg=p.createGraphics(600, 600,JAVA2D);
//    UMB.setGraphics(gg);
    if(main.nav==null) main.nav=new UNav3D();


    vl=UVertexList.circle(100, 12).rotX(-HALF_PI);
    vl2=vl.copy().translate(0,50,0);
    
    UTask task=new UTask(name);
    UGeo geo=new UGeo();
//    int nn=100;
//    for(int i=0; i<nn; i++) {
//      geo.quadstrip(vl, vl2);
//      vl.translate(0,50,0);
//      vl2.translate(0,50,0);
//    }
    
    build();
    task.done();
  }
  
  void build() {
    vvl=new ArrayList<UVertexList>();

    UVertexList.setGraphics(g);
    vl=new UVertexList();

    int n=UMB.rndInt(4, 9)*2;
    for (int i=0; i<n; i++) {
      float w=25+(i%2)*50;
      vl.add(new UVertex(w, 0, 0).
        rotZ(map(i, 0, n, 0, TWO_PI)));
    }
//    vl.close();
    
      // vertices were added clock-wise, so reverse
      vl.reverse().close();

      vl.translate(-50, -150, 0);
      for (int i=0; i<30; i++) {
        // chained commands:
        // #1 copy vertex list
        // #2 rotate and translate copy
        // #3 add copy to vvl
        // Remember: The original "vl" is left unchanged
        vvl.add(vl.copy().
          scale(map(i,0,29f,0.25f,1)).
          rotX(map(i,0,29,0.1f,0.9f)*TWO_PI).
          translate(0, 0, 20).rotY(HALF_PI));

        //      println(vl.str());
      }

      // set UV coordinates for list of UVertexList
      UVertexList.setUV(vvl);

      geo=new UGeo();
      // make quadstrips of all vertex lists in vvl
      geo.quadstrip(vvl);

      // fill first and last vertex lists w/ triangle fans
      geo.triangleFan(vvl.get(vvl.size()-1));

      // the first fan needs to have its order reversed to face
      // the right way
      geo.triangleFan(vvl.get(0), true);
      geo.center();

      // write STL using incremental file naming
      // "data/" is the root dir, "test" is the file prefix
      //    UGeoIO.writeSTL(UFile.nextFilename("data\\", "test"), geo);
    }
  
  
  public void draw() {
    p.translate(p.width/2, p.height/2);
    main.nav.doTransforms();

//    p.noStroke();
//    p.fill(180);
//    UMB.drawRoundedTube(100, 100, 400, 400, 20, false);
//    
//    UMB.pstroke(255).pnoFill();
//    pline(100,0, 100,100);
//    pline(400,0, 400,400);
    
    vl.draw();
    vl2.draw();
    vvl.get(vvl.size()/2).draw();
    UMB.last(vvl).draw();
    
    if(p.mousePressed) geo.draw(vvl);
    vl.bb().draw();
    
//    geo.draw();
  }

  
}
