/*
  Code for ITP: Parametric Design for Digital Fabrication, Fall 2013
  Marius Watz - http://workshop.evolutionzone.com

  Example showing integration with Geomerative library by Ricard Marxer
  (http://www.ricardmarxer.com/geomerative/)  
 
  Reads vector data from SVG using Geomerative, calculating points on the
  paths found and finally converting path vertices to a UVertexList for use
  with ModelbuilderMk2. 
  
  See "svgimport" tab for the conversion function svgToVL(), which can be 
  copied to your sketch for use. This demo also shows how to get a list of 
  any SVG files in "data" folder and switch between them as input. 

  SVG code based on Tutorial_22_GetPointPaths.pde from Geomerative.
 
 */


import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

import geomerative.*;

UVertexList vl;

void setup() {
  size(600, 600, P3D);
  UMB.setPApplet(this);

  // get list of SVG files in "data" folder
  getFilelist();
  
  // convert random SVG to UVertexList
  randomSVG();
}

void draw() {
  background(0);
  noFill();

  pushMatrix();
  translate(width/2, height/2);
  stroke(100);
  vl.draw();

  noStroke();
  fill(255);
  for (UVertex vv:vl) ellipse(vv.x, vv.y, 3, 3);
  popMatrix();
  
  drawCredit();
  text(lastFile+" "+vl.size()+" pts",10,height-10);
}



