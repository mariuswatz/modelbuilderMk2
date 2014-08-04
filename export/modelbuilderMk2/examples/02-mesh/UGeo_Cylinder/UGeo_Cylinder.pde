/*
  ModelbuilderMk2 - UGeo_Cylinder.pde
  Marius Watz - http://workshop.evolutionzone.com
 
  Creates a cylinder mesh using UGeo.quadstrip() and 
  UGeo.triangleFan(), then draws the mesh with face normals
  
 */


import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

UGeo geo;
UNav3D nav;

void setup() {
  size(600,600,OPENGL);  

  // initialize ModelbuilderMk2 and add navigation
  UMB.setPApplet(this);
  nav=new UNav3D();
  
  UVertexList vl,vl2;
    
  // add vertices representing a circular base to vl. note
  // that the map() function does not actually close the list
  // since the last vertex does end up at 360 degrees.
  int n=24;
  vl=new UVertexList();
  
  for(int i=0; i<n; i++) {
    float deg=map(i, 0,n, 0,TWO_PI);
    vl.add(new UVertex(100, 0, 0).rotY(-deg));
  }
  
  // create vl2 as a copy of vl, translated to the desired height
  vl2=vl.copy().translate(0,600,0);
  
  
  // create quadstrip mesh of vl and vl2, calling
  // UVertexList.close() on each before the call
  geo=new UGeo().quadstrip(vl.close(),vl2.close());
  
  // cap the cylinder with triangle fans. 
  geo.triangleFan(vl2);
  
  // to ensure correct vertex order the bottom list is 
  // added in reverse order
  geo.triangleFan(vl,true);
  
  // center and scale the mesh
  geo.center().scale(0.5);    
}  

void draw() {
  background(0);
  drawCredit();
  
  translate(width/2,height/2);
  nav.doTransforms();
  lights();
  
  // UMB has chainable shorthand versions of PApplet functions 
  UMB.pstroke(color(0)).pfill(color(255));
  geo.draw();
  
  stroke(255,0,0);
  // get the ArrayList<UFace> stored in geoto draw the face normals
  // The parameter 10 is the desired length of the drawn normals
  for(UFace f:geo.getF()) f.drawNormal(10);
}
