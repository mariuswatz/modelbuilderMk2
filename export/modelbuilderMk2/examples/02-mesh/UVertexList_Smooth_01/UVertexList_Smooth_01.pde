/*
  ModelbuilderMk2 - UGeo_Primitives.pde
  Marius Watz - http://workshop.evolutionzone.com
  
  How to use UVertexList.smooth() on a UVertexList.
  
  The smoothing algorithm consists of taking line segments (v1,v2) from a vertex list, 
  calculating two new vertices interpolated at 25% and 75% (respectively, 
  v1.lerp(0.25f,v2) and v1.lerp(0.75f,v2) along each segment. The new points are 
  added to a new list, the original v2 is omitted.

  If the input list is closed (UVertexList.isClosed()==true), first and last segments 
  are handled so that the interpolation "wraps".
  
 
 */


import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

UNav3D nav;
UVertexList vl, vlSmooth;
int level=-1;

void setup() {
  size(600, 600, OPENGL);  

  // initialize ModelbuilderMk2 and add navigation
  UMB.setPApplet(this);
  nav=new UNav3D();

  build();
}    


void build() {
  vl=new UVertexList();

  int n=(int)random(9, 37)*2;
  float m=random(100, 300);
  for (int i=0; i<n; i++) {
    m=m*0.2f+random(100, 300)*0.8f;
    float t=-map(i, 0, n, 0, TWO_PI);
    vl.add(new UVertex(m, 0).rotZ(t));
  }  
  vl.close();

  // smooth the path
  level=-1;
  smoothVL();
}

void draw() {
  background(0);

  pushMatrix();
  translate(width/2, height/2);
  nav.doTransforms();

  noFill();
  stroke(100);
  vl.draw();

  stroke(255, 200, 0);
  translate(0, 0, 5);
  vlSmooth.draw();

  for (UVertex vv:vlSmooth) ellipse(vv.x, vv.y, 5, 5);

  popMatrix();
  fill(100, 200);
  noStroke();

  rect(0, 0, width, 42);
  drawCredit();
  text("Smooth level: "+(level+1)+
    " n="+vl.size()+" nSmooth="+vlSmooth.size(), 5, 27);
  text("n= reset | Any key: Increase smooth level", 5, 39);
}

void smoothVL() {
  level=(level+1)%5;
  vlSmooth=UVertexList.smooth(vl, level+1);
}

void keyPressed() {
  if (key==CODED) return;

  if (key=='n') build();
  else smoothVL();
}
