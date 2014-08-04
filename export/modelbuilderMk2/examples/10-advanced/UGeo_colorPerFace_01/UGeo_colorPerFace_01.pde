/*
 ModelbuilderMk2 - UGeo_colorPerFace_01.pde
 Marius Watz - http://workshop.evolutionzone.com
 
 Assign color to individual faces based on UV coordinates
 stored in the vertices. UGeo will draw the model with
 the per-face color once UGeo.enable(UGeo.COLORFACE) is called. 
 
 */
 
import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

UNav3D nav;

public void setup() {
  size(600, 600, OPENGL);
  UMB.setPApplet(this);
  nav=new UNav3D();
  nav.rot.set(radians(15),radians(30),0);

  build();
  colorMesh();
}

public void draw() {
  background(0);
  drawCredit();

  translate(width/2, height/2);
  lights();
  nav.doTransforms();

  stroke(255);
  fill(255);

  geo.draw();
}

