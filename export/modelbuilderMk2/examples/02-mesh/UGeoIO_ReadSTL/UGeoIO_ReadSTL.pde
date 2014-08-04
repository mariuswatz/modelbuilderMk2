/*
  ModelbuilderMk2 - UGeoIO_ReadSTL.pde
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
  
  geo=UGeoIO.readSTL(dataPath("dodecahedron.stl" ));  
  // center and scale the mesh
  geo.center().scaleToDim(width-100 );
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
  // draw face normals
  geo.drawNormals(50);
}
