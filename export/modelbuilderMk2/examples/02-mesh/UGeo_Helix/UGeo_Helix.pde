
/*
  ModelbuilderMk2 - UGeo_Helix.pde
  Marius Watz - http://workshop.evolutionzone.com
 
  Generates a structure somewhat similar to a 
  sea shell using a circular profile.
  
 */


import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

UVertexList helix;
UNav3D nav;

void setup() {
  size(600,600,OPENGL);
  UMB.setPApplet(this);
  
  nav=new UNav3D();
  
  build();
}

void draw() {
  background(0);
  translate(width/2,height/2);
  lights();
  nav.doTransforms();
  
  noFill();
  stroke(255);

 
  fill(255,255,0);
  helix3D.draw();
}

void keyPressed() {
  build();
}
