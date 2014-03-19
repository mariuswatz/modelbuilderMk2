/*
  ModelbuilderMk2 - UGeo_Primitives.pde
  Marius Watz - http://workshop.evolutionzone.com
 
  How to use UVertexList.smooth() on ArrayList of UVertexList, so that
  it can be used to create a smoothed UGeo mesh. 
 
 */


import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

UNav3D nav;
UVertexList vl, vlSmooth;

ArrayList<UVertexList> stack, stackSmooth;
UGeo geo, geoSmooth;

int drawType=0;
int colOutline;

void setup() {
  size(600, 600, OPENGL);  

  // initialize ModelbuilderMk2 and add navigation
  UMB.setPApplet(this);
  nav=new UNav3D();

  colOutline=color(255, 100, 0);
  build();
}    

void draw() {
  background(0);

  pushMatrix();
  lights();
  translate(width/2, height/2);
  nav.doTransforms();

  noFill();
  noStroke();
  int cnt=0;

  if (drawType==(cnt++)) { // just stack
    stroke(colOutline);
    UVertexList.draw(stack);
  }
  else if (drawType==(cnt++)) { // just smoothed stack
    stroke(colOutline);
    UVertexList.draw(stackSmooth);
  }
  else if (drawType==(cnt++)) { // draw geometry of stack
    fill(255);
    geo.draw();

    noFill();
    stroke(colOutline);
    UVertexList.draw(stack);
  }
  else if (drawType==(cnt++)) { // draw smoothed geometry of stack
    fill(255);
    geoSmooth.draw();

    noFill();
    stroke(colOutline);
    UVertexList.draw(stackSmooth);
  }


  popMatrix();
  
  hint(DISABLE_DEPTH_TEST);
  fill(100,100,100, 240);
  noStroke();

  rect(0, 0, width, 31);
  drawCredit();
  text("n=rebuild | Any key: Step through draw type", 5, 27);
  
    hint(ENABLE_DEPTH_TEST);

}


void keyPressed() {
  if (key==CODED) return;
  if (key=='n') build();
  else drawType=(drawType+1)%4;
}
