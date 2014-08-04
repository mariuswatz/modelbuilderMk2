/*
 Code for ITP: Parametric Design for Digital Fabrication, Fall 2013
 Marius Watz - http://workshop.evolutionzone.com
 
 Imports stock market data from a CSV and constructs a solid
 geometry representation of it.
 
 */

import processing.data.*;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

UNav3D nav;

ArrayList<Float> values;
float minVal, maxVal;
String filename="aapl2008.csv";

UGeo dataGeo;

public void setup() {
  size(800, 600,P3D);

  // set PApplet reference
  UMB.setPApplet(this);
  nav=new UNav3D();
  
  importData();
  build();
}

public void draw() {
  background(0);
  drawCredit();

  lights();
  translate(width/2, height/2);
  nav.doTransforms();
  fill(255);
  noStroke();

  dataGeo.draw();
}
