/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PApplet;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.UTileRenderer;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMB;
import unlekker.mb2.util.UFile;

public class UTestMainGeo extends UTestMain {
  public String path,saveFilename;
  public ArrayList<UTest> tests;
  
  public UTest theTest;
  public UNav3D nav;
  UTileRenderer tiler;
  

  public void setup() {
    size(600,600, P3D);
    UTest.p=this;
    UTest.main=this;
    

    
    UMB.setPApplet(this);
    sketchPath=UFile.getCurrentDir().charAt(0)+":/Users/marius/Dropbox/03 Code/ITP2013Parametric/nosync/data";
    UMB.log(sketchPath);
    
    
//    sketchPath="c:/Users/marius/Dropbox/40 Teaching/2013 ITP/ITP-Parametric - Resources/Code/ITP-Parametric-sketches/exportSketchDocu";
//    
//    String filename=UFile.nextFile(sketchPath, "exportSketchDocu");
//    String str[]=new String[] {"a","b"};
//    saveStrings(filename+".txt",str);
//    println(filename);
//    
//    filename=UFile.nextFile(sketchPath, "exportSketchDocu");
//    saveStrings(filename+".txt",str);
//    println(filename);
//
//    exit();
//    stop();
    
    tests=new ArrayList<UTest>();

    tests.add(new UTestHSP());
    tests.add(new UTestTriangulate());
    tests.add(new UTestFaceWindow());
    tests.add(new UTestExtrude());
    tests.add(new UTestVertexList());
    tests.add(new UTestQuadGroup());
    
    tests.add(new UTestPrimitives());
    tests.add(new UTestHeading03());
    tests.add(new UTestMath());
    tests.add(new UTestGeo());
    tests.add(new UTestTask());
    tests.add(new UTestSmooth02());
    tests.add(new UTestPrimitives02());
    
    tests.add(new UTestSelector02());
    tests.add(new UTestNormal()); 
    tests.add(new UTestBPatch());
    tests.add(new UTestSelector());
    tests.add(new UTestEdgeList02());
    
//  tests.add(new UTestSTL()); 

//    tests.add(new UTestSTLColor()); 

    tests.add(new UTestSmooth());
    tests.add(new UTestTriangleFanBug());
    
    
//    tests.add(new UTestIntersectionCirc());
//    tests.add(new UTestIntersection());
//    tests.add(new UTestHeading02());
//    tests.add(new UTestHeading01());
//    tests.add(new UTestCurve());
//    tests.add(new UTestResample());
//    tests.add(new UTest2D()); 
    
    
    int id;
    
    try {
      String dat[]=loadStrings("UTestMain.dat");
      id=Integer.parseInt(dat[0]);
    } catch (Exception e1) {
    }
    
//    id=6;
    id=tests.size()-1;
    theTest=tests.get(id);
    
      initTest();
  }

  public void draw() {
    background(100);
//    camera();
    
    if(tiler!=null) {
//      tiler.pre();
      if(tiler.done) tiler=null;
    }
    else {
      color(255,255,255);
      drawCredit();
    }
    
    

    
//    pushStyle();
    try {
//      tiler.pre();
      theTest.draw();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      exit();
    }
//    popStyle();
    
  }

  public void drawGrid() {
    int n=width/100;
    for(int i=1; i<n;i++) {
      
      line(i*100,0,i*100,height);
      line(0,i*100,width,i*100);
    }
  }

  public void drawCredit() {
    if(frameCount<2)   textFont(createFont("courier", 11, false));

    fill(255);
    textAlign(RIGHT);
    text(UMB.version(), width-5, 15);
    textAlign(LEFT);
    text(theTest.getClass().getSimpleName(), 5, 15);
  }
  
  public void keyPressed() {
    if(key=='t') {
      String filename=UFile.nextFile(UFile.getCurrentDir(), "Tile");
      filename+=".tga";

      tiler=new UTileRenderer(filename,filename.endsWith("png") ? 6 : 20);
      tiler.start();
//      nav.enabled=false;
    }
    

    if(key=='s') {
      String filename=UFile.nextFile(sketchPath, theTest.getClass().getSimpleName(), "png");
      println(filename);      
      saveFrame(filename);
      saveFilename=filename;
      theTest.save(filename);
    }
    else if(key==' ' || key=='+' ) {
      int id=(tests.indexOf(theTest)+1)%tests.size();
      theTest=tests.get(id);
      initTest();
    }
    else if(key==' ' || key=='-' ) {
      if(keyEvent.isShiftDown() || key=='-') {
        int id=tests.indexOf(theTest);
        id=(id<1 ? id=tests.size()-1 : id-1);
        
        theTest=tests.get(id);
        initTest();
        
      }
    }
    else if(key!=CODED && key=='n') {
//      if(keyCode==java.awt.event.KeyEvent.VK_N)     
      
        initTest();
    }

    theTest.keyPressed(key);

    saveStrings("UTestMain.dat",
        new String [] {""+tests.indexOf(theTest)});
  }

  public void initTest() {
    try {

      UMB.logDivider(theTest.name);
      theTest.init();
  } catch (Exception e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
    exit();
  }
    
  }

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "unlekker.mb2.test.UTestMainGeo" });
  }

  
}
