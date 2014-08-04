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
UGeoGroup sel;


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

  // draw the group
    if(sel.size()<1) geo.draw();
    
  if(sel.size()>0) {
    fill(100);
    stroke(color(0));
    sel.draw(true);
     stroke(color(255));
     sel.drawNormals(20);
  }
  
  
  // draw the geometry as wireframe
  noFill();
  geo.draw();
//  stroke(255,0,0);
}

void keyPressed() {
  if(key=='o') {
    for(int i=0; i<10; i++) {
      UVertex rnd=geo.getV((int)random(geo.sizeV()));
      rnd.add(random(-10,10),random(-10,10),random(-10,10));
      println(rnd.str());
    }
  }
  
  if(key=='r') {
      UFace rf=sel.getRndF();
      sel.add(rf);
    }

    if(key=='n') sel.clear();
    
    if(key=='g') {
      sel=geo.getGroup((int)random(geo.sizeGroup()));
    }

    if(key=='a') sel.addConnected();
    if(key=='v') {
      UVertex vv=geo.getV((int)random(geo.sizeV()));
      int tries=100;
      
      while(sel.contains(vv) && tries>0) {
        vv=geo.getV((int)random(geo.sizeV()));
        tries--;
      }
      sel.addConnected(vv);
    }
    if(key=='d') geo.remove((int)random(geo.sizeF()));
    
    if(key==ENTER) sel.subdivide(UMB.SUBDIVCENTROID, true);
}
