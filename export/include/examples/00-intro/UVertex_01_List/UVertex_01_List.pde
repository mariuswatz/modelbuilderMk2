import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

UVertex v,vm;
UVertexList vl;
int maxPts=1000;

void setup() {
  size(600,600);
  
  v=new UVertex(width/2,height/2);
  vm=new UVertex();
  
  vl=new UVertexList();
}

void draw() {
  background(0);
  
  vm.set(mouseX,mouseY);

  addPoint();
  
  // hacky operator-chaining way of getting the delta vector
  v.add(vm.copy().sub(v).mult(0.05));
  
  noFill();

  stroke(255);
  beginShape();

  // iterate through vertex list
  for(UVertex vv:vl) vertex(vv.x,vv.y);
  
  endShape();
  
  stroke(255,0,0);
  ellipse(vm.x,vm.y, 50,50);
  
  stroke(255);
  ellipse(v.x,v.y, 75,75);
  
  fill(255);
  text("Points in list: "+vl.size(), 10,20);
}

void addPoint() {
  // if there is a previous point, check if squared
  // distance is greater than 10
  if(vl.size()>0) {
    if(vl.last().distSq(v)<10) return;
  }
  
  // access the internal ArrayList to remove first point
  // if vl.size()>maxPts
  if(vl.size()>maxPts) vl.v.remove(0);
  
  // add v to the vertex list, which means that a copy is
  // made and stored in vl.
  vl.add(v);
}
