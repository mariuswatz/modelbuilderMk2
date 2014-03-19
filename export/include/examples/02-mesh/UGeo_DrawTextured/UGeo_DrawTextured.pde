/*
 ModelbuilderMk2 - UGeo_DrawTextured
 Marius Watz - http://workshop.evolutionzone.com
 
 Code for ITP: Parametric Design for Digital Fabrication, Fall 2013
 
 Draws a textured UGeo instance using UV coordinates taken 
 from its UVertex instances. UV is calculated during build().
 
 */

import processing.event.*;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

UVertexList prof;
UNav3D nav;
ArrayList<UVertexList> sweep;

UGeo geo;
PImage tex;

public void setup() {
  size(600, 600, OPENGL);

  UMB.setPApplet(this);
  nav=new UNav3D();

  // the texture file to use.
  String filename="Paper_Texture_by_Spiteful_Pie_Stock.jpg";

  tex=loadImage(filename);

  build();
}

public void draw() {
  background(0);
  drawCredit();

  lights();
  translate(width/2, height/2);
  nav.doTransforms();

  noStroke();
  if (mousePressed) {
    fill(255);
    geo.draw();
  }
  else {
    geo.drawTextured(tex);
  }
}
