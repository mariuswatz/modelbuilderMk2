/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PApplet;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UFileStructure;
import unlekker.mb2.util.UMB;
import unlekker.mb2.util.UFile;

public class UFileTest extends PApplet {
  String path;
  UFileStructure root;
  
  public void setup() {
    size(600,600, OPENGL);
    UMB.setPApplet(this);
    
    
    path="C:\\Users\\marius\\Dropbox\\03 Code\\Novotel\\data";
    println(path);
    
//    UBasic.log(UBasic.str(UFile.list(path)));
    UMB.logDivider(path);
    root=new UFileStructure(path);
    root.recurse();
  }

  public void draw() {
    background(0);

    fill(255);
    UFileStructure.UFileThread r=root.thread;
    text(r.queue.size()+" "+r.processed,20,20);
    
    if(root.done()) {
      ArrayList<String> dat=root.getData();
      String filename=UFile.getCurrentDir()+"/out.txt";
      UMB.saveStrings(filename,dat);
      exit();
      
    }
  }

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "unlekker.mb2.test.UFileTest" });
  }

  
}
