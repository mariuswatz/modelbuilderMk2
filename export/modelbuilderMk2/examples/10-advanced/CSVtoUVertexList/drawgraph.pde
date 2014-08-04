void drawRegular() {
  pushMatrix();

  translate(-width/4, 0);
  stroke(120);
  fill(50);
  vlGraph.draw();

  popMatrix();
}

void drawRadial() {
pushMatrix();
translate(width/4, 0);
  float sz=vlRadial.bb().dim.mag();
  
  stroke(80);
  fill(50);
  sz=W;
  ellipse(0,0, sz,sz);
  
  fill(120);
  noStroke();

  vlRadial.draw();
  
  noFill();
  stroke(200);
  
  // cheap hack to approximate division lines per week.
  // these lines will not be correct, since they don't
  // use the actual dates, just position in array  
  int cnt=0;
  int nn=val.length/52;
  
  for(UVertex vv:vlRadial) if((cnt++)%nn==0) {
    UVertex vv2=vv.copy().norm().mult(sz/2);
    line(0,0, vv.x,vv.y);
  }  
  
  noStroke();
  fill(0);
  sz=W*(minval/maxval);
  ellipse(0,0, sz,sz);

popMatrix();
}
