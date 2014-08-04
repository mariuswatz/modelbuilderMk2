/*
  ModelbuilderMk2 - UGeo_Primitives.pde
 Marius Watz - http://workshop.evolutionzone.com
 
 How to draw the normals of meshes using UGeo.drawMormals().  
 
 */


import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

ArrayList<UGeo> models;
UNav3D nav;

void setup() {
  size(600, 600, OPENGL);  

  // initialize ModelbuilderMk2 and add navigation
  UMB.setPApplet(this);
  nav=new UNav3D();

  build();
}    

  
void build() {
    models=new ArrayList<UGeo>();

  int n=8;
  // add cylinder primitive
  models.add(UGeo.cyl(150, 300, n));

  // create randomized mesh form from a stack of edges
  ArrayList<UVertexList> stack=new ArrayList<UVertexList>();

  n=5;
  for (int i=0; i<n; i++) {
    UVertexList tmp=new UVertexList();

    for (int j=0; j<n*2; j++) {
      float a=map(j, 0, n*2, 0, TWO_PI);
      tmp.add(new UVertex(random(50, 100), 0).rotY(a));
    }

    tmp.translate(0,map(i, 0,n-1,0,300));
    stack.add(tmp.close());
  }

  // center stacked edges as a single entity  
  UVertexList.center(stack);
  
  // add UGeo created from quadstrips of the stacked edges 
  models.add(new UGeo().quadstrip(stack));

  // offset models to the right and left of center
  models.get(0).translate(-150,0);    
  models.get(1).translate(150,0);    
}

void draw() {
  background(0);
  drawCredit();

  translate(width/2, height/2);
  nav.doTransforms();
  lights();

  // UMB offers chainable shorthand versions of PApplet functions 
  UMB.pstroke(color(0)).pfill(color(255));
  
  for (UGeo geo:models) geo.draw();
  
  // draw face normals in red
  stroke(255,0,0);
  float len=50;
  for (UGeo geo:models) geo.drawNormals(len);
}

void keyPressed() {
  
}
