/*
 ModelbuilderMk2 - UExtrudeMesh.pde
 Marius Watz - http://workshop.evolutionzone.com
 
 Code for ITP: Parametric Design for Digital Fabrication, Fall 2013

 Takes a UGeo mesh of a flat surface and extrudes it to 
 a solid geometry. Extrusion is done using the vertex normals
 of the input mesh.
 
 */

import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

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

    pushMatrix();
  lights();
  // execute navigation transforms
  translate(width/2, height/2);
  nav.doTransforms();

    if(mousePressed) {
      strokeWeight(1);
      fill(100,200,255);
      
      stroke(0,50,100);
      geo.draw();
      
      stroke(0,255,255);
      geo.drawVertexNormals(10);

      strokeWeight(2);
      for(UEdge e:border) e.draw();

    }
    else {
      drawExtrude();
    }
    
    popMatrix();
}

void drawExtrude() {
    strokeWeight(1);
    stroke(255,0,128);
    
    fill(255,255,0);
    geo2.draw().drawVertexNormals(10);
    
  }
