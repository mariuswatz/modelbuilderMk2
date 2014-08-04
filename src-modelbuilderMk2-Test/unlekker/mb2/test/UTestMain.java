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

public class UTestMain extends PApplet {
  public String path,saveFilename;
  public ArrayList<UTest> tests;
  
  public UTest theTest;
  public UNav3D nav;
  
  public void setup() {
    size(1200,600, OPENGL);
    UTest.p=this;
    UTest.main=this;
    
    UMB.setPApplet(this);
    sketchPath=UFile.getCurrentDir().charAt(0)+":/Users/marius/Dropbox/03 Code/ITP2013Parametric/nosync/data";
    UMB.log(sketchPath);
    
    tests=new ArrayList<UTest>();    
    
    cycleTest(1);
    
  }

  public void draw() {
    background(0);
     color(255,255,255);
    drawCredit();
    
    
    
    try {
      if(theTest!=null) theTest.draw();
    } catch (Exception e) {
      e.printStackTrace();
      exit();
    }
  }

  public void drawCredit() {
    if(theTest==null) return;

    if(frameCount<2)   textFont(createFont("courier", 11, false));

    fill(255);
    textAlign(RIGHT);
    text(UMB.version(), width-5, 15);
    textAlign(LEFT);
    text(theTest.getClass().getSimpleName(), 5, 15);
  }
  
  public void cycleTest(int mod) {
    if(tests.size()==0) return;
    
    int id=0;    
    if(tests.size()>1) {
      if(theTest!=null) id=tests.indexOf(theTest);
      if(mod>0) {
        id=(id+mod)%tests.size();      
      }
      else if(mod<0) {
        id-=mod;
        if(id<0) id=tests.size()+mod;
      }
    }
    
    theTest=tests.get(id);
    initTest();
  }
  
  public void keyPressed() {
    if(key==' ' || key=='+' ) cycleTest(1);
    else if(key=='-' ) cycleTest(-1);
    else if(key!=CODED && key=='n') {
        initTest();
    }

    if(theTest!=null) theTest.keyPressed(key);

//    saveStrings("UTestMain.dat",
//        new String [] {""+tests.indexOf(theTest)});
  }

  public void initTest() {
    if(theTest==null) return;
    
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
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "unlekker.mb2.test.UTestMain" });
  }

  
}
