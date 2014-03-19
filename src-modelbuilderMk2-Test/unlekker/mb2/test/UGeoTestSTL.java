/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PApplet;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UGeoIO;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UFile;

public class UGeoTestSTL extends PApplet {
  ArrayList<UVertexList> vvl;
  
  UVertexList vl,vl2;
  UGeo geo;
  
  public void setup() {
    size(600,600, OPENGL);
    
    vvl=new ArrayList<UVertexList>();
    
    UVertexList.setGraphics(g);
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
      vvl.add(vl.copy().rotX(radians(10*i)).translate(0, 0, 20));
      
      println(vl.str());
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
    
    
    // randomize face colors
    int id=0,n=geo.getF().size();           
    for(UFace f:geo.getF()) {
      UVertex fv[]=f.getV();
      f.setColor(fv[0].U*255,fv[0].V*155+100,fv[0].V*255);
    }

    geo.enable(geo.COLORFACE); // per-face coloring
    println(geo.optionStr()); // print options string
    
    ArrayList<UGeo> gl=new ArrayList<UGeo>();
    gl.add(geo);
    
    UGeo g2=geo.copy().translate(300, 0, 0);
    gl.add(g2);
    
    // write STL using incremental file naming
    // "data/" is the root dir, "test" is the file prefix
    UGeoIO.writeSTL(UFile.nextFilename("data/", "test"), gl);
    
    geo.add(g2);
    UGeoIO.writeSTL(UFile.nextFilename("data/", "test"), geo);
    
    
    String filename="C:/Users/Marius/Dropbox/40 Teaching/2013 ITP/ITP-Parametric - Resources/Models";
    filename=filename+"/dodecahedron.stl";
    geo=UGeoIO.readSTL(this, filename);
    geo.center().scale(4);
    
  }

  public void draw() {
    background(0);
    
    translate(width/2,height/2);
    lights();
    
    float ry=map(width/2-mouseX,-width/2,width/2, PI,-PI);
    float rx=map(height/2-mouseY,-height/2,height/2, PI,-PI);
    
    rotateY(ry+radians(frameCount));
    rotateX(rx);
    
    
    stroke(255);
    fill(255);
    
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
