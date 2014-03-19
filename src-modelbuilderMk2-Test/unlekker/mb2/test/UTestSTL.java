/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import processing.core.PApplet;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UGeoIO;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UFile;
import unlekker.mb2.util.UMB;

public class UTestSTL extends UTest {
  ArrayList<UVertexList> vvl;
  
  UVertexList vl,vl2;
  UGeo geo;
  
  public void init() {
    
    vvl=new ArrayList<UVertexList>();
    
    vl=new UVertexList();
    vl.add(0,0,0);
    vl.add(100,0,0);
    vl.add(125,50,0);
    vl.add(100,100,0);
    vl.add(0,100,0);
    vl.add(-25,50,0);
    vl.add(0,0,0);
    
    // vertices were added clock-wise, so reverse
    vl.reverse(); 
    
    vl.translate(-50, -150, 0);
    for(int i=0; i<30; i++) {
      // chained commands:
      // #1 copy vertex list
      // #2 rotate and translate copy
      // #3 add copy to vvl
      // Remember: The original "vl" is left unchanged
      vvl.add(vl.copy().rotX(p.radians(10*i)).translate(0, 0, 20));
      
      p.println(vl.str());
    }

    geo=new UGeo();
    
    
    UVertexList.setUV(vvl);
    // make quadstrips of all vertex lists in vvl
    geo.quadstrip(vvl);
    
    // fill first and last vertex lists w/ triangle fans
    geo.triangleFan(vvl.get(vvl.size()-1));
    
    // the first fan needs to have its order reversed to face
    // the right way
    geo.triangleFan(vvl.get(0),true);
    
    ArrayList<UGeo> gl=new ArrayList<UGeo>();
    gl.add(geo);
    
    UGeo g2=geo.copy().translate(300, 0, 0);
    gl.add(g2);
    
    // write STL using incremental file naming
    // "data/" is the root dir, "test" is the file prefix
    UGeoIO.writeSTL(UFile.nextFilename("data/", "test"), gl);
    
    geo.add(g2);
    UGeoIO.writeSTL(UFile.nextFilename("data/", "test"), geo);
    
    
    String filename="C:/Users/marius/Dropbox/05 Makerbot/";
    filename=filename+"testAsc.stl";
    geo=UGeoIO.readSTL(filename);
    geo.center().scale(200f/geo.dimX());
//    geo.getV().removeDupl(true);
    geo.log(geo.getV().str());
    
    if(main.nav==null) main.nav=new UNav3D();
  }

  public void draw() {
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();

    UMB.pstroke(p.color(255)).pfill(p.color(255));
    
    geo.draw();
  }

  static public void main(String args[]) {
    String sk[]=new String[] {
        "UGeoTestSTL",
        "UGeoTest",
        "UFileTest"
    };
    
    PApplet.main(new String[] {"unlekker.mb2.test."+sk[0]});
  }

  
}
