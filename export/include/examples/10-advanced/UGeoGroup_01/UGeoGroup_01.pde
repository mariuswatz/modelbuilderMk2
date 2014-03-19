/*
 ModelbuilderMk2 - UGeoGroup_01.pde
 Marius Watz - http://workshop.evolutionzone.com
 
 Code for ITP: Parametric Design for Digital Fabrication, Fall 2013

 Basic use of UGeoGroup to access groups of faces in a UGeo.
 
 UGeo stores a record of all mesh creation operations as a list of UGeoGroup
 instances. UGeoGroup stores the type of operation (QUAD_STRIP / TRIANGLE_FAN 
 for quadstrip() / triangleFan(), TRIANGLES for all other cases) and a list of 
 all the faces generated.
 
 UGeoGroup provides access to sections of a mesh, but can also be used as
 a general tool to select and manipulate groups of faces. 

 */

import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

UNav3D nav;
UGeo geo;

UGeoGroup theGroup;
int sel=-1;

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
