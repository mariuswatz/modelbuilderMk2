import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

UVertex v,vm;

void setup() {
  size(600,600);
  
  v=new UVertex(width/2,height/2);
  vm=new UVertex();
}

void draw() {
  background(0);
  
  vm.set(mouseX,mouseY);
  
  // calculate the delta vector from v to vm
/*  UVertex vD=new UVertex(vm);
  vD.sub(v);
  vD.mult(0.05); // divide by 100
  
  // to move v along vD
  v.add(vD);
  */
  
  // hacky operator-chaining version of the above
  v.add(vm.copy().sub(v).mult(0.05));
  
  noFill();
  
  stroke(255,0,0);
  ellipse(vm.x,vm.y, 50,50);
  
  stroke(255);
  ellipse(v.x,v.y, 75,75);
}
