/*
  ModelbuilderMk2 - UGeo_Deform_01.pde
 Marius Watz - http://workshop.evolutionzone.com
 
 Builds cylinder-like structure and applies a 
 magnetic force deformation.
 
 */


import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

UVertexList path, prof;
UNav3D nav;
ArrayList<UVertexList> sweep;

UGeo geo;

public void setup() {
  size(600, 600, OPENGL);

  UMB.setPApplet(this);
  nav=new UNav3D();

  build();
}

public void draw() {
  background(0);
  drawCredit();
  text("SPACE = apply deformation", 10, height-5);

  lights();
  translate(width/2, height/2);
  nav.doTransforms();

  fill(255);
  geo.draw();
}

public void keyPressed() {
  if (key==' ') {
    geo.bb(true); // force recalculation of bounding box

    UVertex rv=new UVertex(
    random(geo.bb().min.x, geo.bb().max.x), 
    random(geo.bb().min.y, geo.bb().max.y), 
    random(geo.bb().min.z, geo.bb().max.z)
      );
    Deformer def=new Deformer(rv);
    def.deform(geo);
  }
}
