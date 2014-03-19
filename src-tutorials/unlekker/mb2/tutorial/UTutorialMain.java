/*
 * modelbuilderMk2
 */
package unlekker.mb2.tutorial;

import java.util.ArrayList;

import processing.core.*;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;

public class UTutorialMain extends PApplet {
  String path;
  ArrayList<UTutorial> tutorials;  
  UTutorial current;
  
  UNav3D nav;
  
  int cBackground,cGrid;
  int cText,cOutline,cHighlight;
  PImage bg2D;
  
  public void setup() {
    size(600,400, P3D);
    UMB.setPApplet(this);    
    UTutorial.p=this;
    
    prep();
  }

  public void draw() {
    background(bg2D);
    noSmooth();
    
    drawCredit();
    
    UMB.ppush();
    noFill();
    translate(width/2,height/2);
    current.draw();
    UMB.ppop();
    
  }

  public void keyPressed() {
    if(key=='s') {
      String filename=UFile.nextFile(sketchPath, current.getClass().getSimpleName(), "png");
      println(filename);
      saveFrame(filename);
    }
    else if(key==' ') {
      current.init();
    }
    else if(key==TAB) {
      int id=(tutorials.indexOf(current)+1)%tutorials.size();
      current=tutorials.get(id);
      current.init();
    }
    else if(key!=CODED && key=='n') {
//      if(keyCode==java.awt.event.KeyEvent.VK_N)     
        current.init();
    }

  }

  public void prep() {
    cBackground=0xFFFFFFF;
    cGrid=0xFF8ddbff;
    cText=0xFF0050b4;
    cOutline=0xFF666666;
    cHighlight=0xFFFF0000;
    println(PFont.list());
    textFont(createFont("Monospaced.plain", 11, false));
    
    tutorials=new ArrayList<UTutorial>();
    tutorials.add(new UTutorialVertexList());
    tutorials.add(new UTutorialVertex());
    
    current=tutorials.get(0);
    current.init();

    
    nav=new UNav3D();
    PGraphics gg=createGraphics(width, height,JAVA2D);
    gg.beginDraw();
    gg.background(cBackground);
    gg.stroke(cGrid);

    int gridsz=50;
    int nx=width/gridsz;
    int ny=height/gridsz;
    
    for(int i=1; i<nx; i++) {
      float t=map(i,0,nx,0,width);
      gg.line(t,0,t,height);

      if(i<ny) {
        t=map(i,0,ny,0,height);
        gg.line(0,t,width,t);
      }
    }
    gg.endDraw();
    bg2D=gg.get();
  }

  void drawCredit() {
    fill(cText);
    textAlign(RIGHT);
    text("modelbuilderMk2", width-5, 15);
    textAlign(LEFT);
    text(
        (tutorials.indexOf(current)+1)+"/"+tutorials.size()+
        "  "+current.getClass().getSimpleName(), 5, 15);
  }
  
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "unlekker.mb2.tutorial.UTutorialMain" });
  }

  
}
