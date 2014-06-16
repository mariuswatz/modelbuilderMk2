package unlekker.data.test;

import processing.core.PApplet;
import unlekker.mb2.util.*;
import unlekker.mb2.test.*;
import java.util.*;

public class UDataTest extends UTestMain {
  String path,saveFilename;
  ArrayList<UTest> tests;

  public void setup() {    
    size(1200,600, OPENGL);
    UTest.p=this;
    UTest.main=this;
    
    UMB.setPApplet(this);
    sketchPath=UFile.getCurrentDir().charAt(0)+":/Users/marius/Dropbox/03 Code/ITP2013Parametric/nosync/data";
    UMB.log(sketchPath);
    
    
  }

  public void draw() {
    
    exit(); 
  }

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    PApplet.main("unlekker.data.test.UDataTest");
  }

}
