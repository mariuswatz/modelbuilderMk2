/*
 Code for ITP: Parametric Design for Digital Fabrication, Fall 2013
 Marius Watz - http://workshop.evolutionzone.com
 
 Example of how to import data from a CSV file and use it to
 construct UVertexList data as a regular XY graph or a 
 radial plot.
 
 */

import processing.event.*;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;


UNav3D nav;

public void setup() {
  size(800, 600);

  // set PApplet reference
  UMB.setPApplet(this);

  importData();
  build();
}

public void draw() {
  background(0);
  drawCredit();

  translate(width/2, height/2);
  drawRegular();
  drawRadial();
  
}

