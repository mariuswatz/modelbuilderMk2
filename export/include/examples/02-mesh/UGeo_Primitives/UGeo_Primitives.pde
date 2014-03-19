/*
  ModelbuilderMk2 - UGeo_Primitives.pde
  Marius Watz - http://workshop.evolutionzone.com
 
  Creates a list of pre-defined geometric primitives,
  centering and coloring them. 
  
 */


import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

ArrayList<UGeo> models;
UNav3D nav;

void setup() {
  size(600,600,OPENGL);  

  // initialize ModelbuilderMk2 and add navigation
  UMB.setPApplet(this);
  nav=new UNav3D();
  
  int n=8;
  float w=(float)(width-n*10)/(float)(n*2+1);
  
  models=new ArrayList<UGeo>();
  for(int i=0; i<n; i++) {
    float t=map(i, 0,n-1, 0,1);
    float h=t*(height-150)+100;
    int col=lerpColor(color(255,0,0),color(255,255,0), t);
    models.add(UGeo.box(w,h,w).setColor(col));   
  }    

  for(int i=0; i<n; i++) {
    float t=map(i, n-1,0, 0,1);
    float h=t*(height-150)+100;
    int col=lerpColor(color(255,255,0),color(0,255,0), 1-t);
    models.add(UGeo.cyl(w,h,12).setColor(col));   
  }    
  
  
  float x=0;
  for(UGeo geo:models) {
    geo.enable(UGeo.COLORFACE);
    
    UMB.log(geo.bb().str());
//    if(x>0) x+=geo.bb().dim.x;
    geo.translate(x,0);
    x+=geo.bb().dim.x+5;
  }
  
  UGeo.center(models);
}  

void draw() {
  background(0);
  drawCredit();
  
  translate(width/2,height/2);
  nav.doTransforms();
  lights();
  
  // UMB has chainable shorthand versions of PApplet functions 
  UMB.pstroke(color(0)).pfill(color(255));
  for(UGeo geo:models) geo.draw();
  
}
