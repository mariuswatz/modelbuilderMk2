/*
 * modelbuilderMk2
 */
package unlekker.mb2.test.data;

import java.util.ArrayList;

import processing.core.PApplet;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.test.*;
import unlekker.mb2.util.UMB;
import unlekker.mb2.util.UFile;

public class UTestDataMain extends UTestMain {
  
  public void setup() {
    size(1200,600, OPENGL);
    UTest.p=this;
    UTest.main=this;
    
    UMB.setPApplet(this);
    sketchPath=UFile.getCurrentDir().charAt(0)+":/Users/marius/Dropbox/03 Code/ITP2013Parametric/nosync/data";
    UMB.log(sketchPath);
    
    tests=new ArrayList<UTest>();    
    tests.add(new UTestDataList());
//    tests.add(new UTestDataLorem());
//    tests.add(new UTestDataTagging());
  }

  public void draw() {
    background(0);
     color(255,255,255);
    drawCredit();
    
    if(theTest==null) return;
    
    try {
      theTest.draw();
    } catch (Exception e) {
      e.printStackTrace();
      exit();
    }
  }

   static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "unlekker.mb2.test.data.UTestDataMain" });
  }

  
}
