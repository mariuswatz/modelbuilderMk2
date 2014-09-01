/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PApplet;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.UMB;

public class UGeoGroup01 extends PApplet {
  UNav3D nav;
  
  UGeo geo;

  UGeoGroup theGroup;
  int sel=-1;
  
  public void setup() {
    size(600,600, OPENGL);
    
    UMB.setPApplet(this);
    nav=new UNav3D();

//    exit();
    
    reinit();
  }

  private void reinit() {
      geo=new UGeo();

      geo.add(UGeo.box(150, 200, 150).
        rotZ(radians(45)).rotX(radians(45)).
        translate(-100, 125));
      geo.add(UGeo.cyl(150, 200, 12).
        rotZ(radians(45)).rotX(radians(45)).
        translate(100, 125));

      geo.add(
      UGeoGenerator.meshPlane(150, 200, 3).
        rotZ(radians(45)).rotX(radians(45)).
        translate(-100, -125));

      geo.add(
      UGeoGenerator.meshBox(150, 200, 150, 3).
        rotZ(radians(45)).rotX(radians(45)).
        translate(100, -125));
  }

  public void draw() {
    background(0);
    lights();
    // execute navigation transforms
    translate(width/2, height/2);
    nav.doTransforms();

    // every 20th frame, get the next group
    if(frameCount%20==0) nextGroup();
    
    // draw the group
    if(theGroup!=null) {
      fill(100);
      stroke(255,255,0);
      theGroup.draw().drawNormals(20);
    }
    
    // draw the geometry as wireframe
    noFill();
    stroke(255,0,0);
    geo.draw();
  }

  void nextGroup() {
    sel=(sel+1)%geo.sizeGroup();
    theGroup=geo.getGroup(sel);
  }
  
  public void keyPressed() {
    nextGroup();
  }

  static public void main(String args[]) {
    String sk[]=new String[] {
        "UGeoGroup01",
        "UFileTest",
        "UGeoTest"
    };
    
    PApplet.main(new String[] {"unlekker.mb2.test."+sk[0]});
  }

  
}
