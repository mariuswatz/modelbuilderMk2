/*
 ModelbuilderMk2 - UTriangulate_01.pde
 Marius Watz - http://workshop.evolutionzone.com
 
 Code for ITP: Parametric Design for Digital Fabrication, Fall 2013
 
 Uses UTriangulate to create a mesh from a distributed set of vertices.
 The code for UTriangulate wass adapted from code posted by Florian Jenett to the
 Processing.org Wiki, which was again based on C code by Paul Bourke.
 
 See:
 http://wiki.processing.org/w/Triangulation 
 http://paulbourke.net/papers/triangulate/
 
 Triangulation is performed in a 2D plane and intended primarily for use
 with point sets that represent a 2.5D topology. Typical cases include 
 terrain data, height maps and geo-coded locations limited to one hemi-sphere
 of the globe. 
 
 The triangulation determines which of the XY/XZ,YZ planes provides the 
 largest area of point distribution and use that plane for the calculations.
 The resulting mesh will be oriented the same way as  the original input (which 
 is left unchanged).
 
 Point clouds representing actual 3D volumes will give poor results, a better 
 solution would be to use a convex hull or some kind of re-meshing tool. 
 
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
  text("Mesh: "+geo.sizeV()+" vertices, "+
    geo.sizeF()+" faces.", 5, height-5);

  translate(width/2, height/2);
  lights();

  // execute navigation transforms
  nav.doTransforms();

  // draw triangulated mesh
  noStroke();
  noFill();
  if (doDrawModel) fill(255, 100, 0, 200);
  else stroke(100);

  geo.draw();

  if (!doDrawModel) {    
    // draw rectangles at each vertex
    stroke(255);
    for (UVertex vv:geo.getV()) {
      pushMatrix();
      translate(vv.x, vv.y, vv.z);
      rect(-1, -1, 2, 2);
      popMatrix();
    }
  }
}

