/*
 ModelbuilderMk2 - UVertexList_Interpolate.pde
 Marius Watz - http://workshop.evolutionzone.com
 
 Code for ITP: Parametric Design for Digital Fabrication, Fall 2013
 
 Demo of linear interpolation between two UVertexLists,
 producing a sequence of new vertex lists that represent a "morph"
 between the original shapes. These can then be used to generate 
 mesh geometry, similar to the "lofting" functions found in CAD
 software.
 
*/

import processing.event.*;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

boolean doDrawModel=true;

ArrayList<UVertexList> vvl;
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

  translate(width/2, height/2);
  lights();

  // execute navigation transforms
  nav.doTransforms();

  // draw individual lists
  stroke(0xFF00FFFF);
  noFill();
  for (UVertexList tmp : vvl) tmp.draw();

  // draw model
  if (doDrawModel) {
    fill(255, 100, 0, 200);
    noStroke();
    geo.draw();
  }
}

