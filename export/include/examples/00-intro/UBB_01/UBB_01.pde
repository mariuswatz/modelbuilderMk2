/*
  ModelbuilderMk2 - UBB_01.pde
 Marius Watz - http://workshop.evolutionzone.com
 
 unlekker.mb2.geo.UBB class:
 Calculate the bounding box of a UVertexList, center
 it around origin and scale its dimensions to a given
 value.
 
 */

import processing.event.*;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;


UVertexList vl;
UGeo geo;
UNav3D nav;

public void setup() {
  size(600, 600, OPENGL);

  // set PApplet reference
  UMB.setPApplet(this);

  // create navigation tool
  nav=new UNav3D();

  build();
}

public void draw() {
  background(0);
  drawCredit();

  // draw bounding box values
  text(vl.bb().str(), 5, height-5);

  translate(width/2, height/2);
  lights();

  // execute navigation transforms
  nav.doTransforms();

  // draw model
  stroke(255, 255, 0);
  fill(255, 255, 0, 100);
  geo.draw();

  // mark center of sketch
  noFill();
  stroke(255);
  ellipse(0, 0, 5, 5);

  // mark centroid of vl
  vl.pline(vl.centroid(), new UVertex());    
  vl.draw();

  // get and draw bounding box (UBB) of model
  stroke(100, 100, 100, 100);
  geo.bb().draw();
}

