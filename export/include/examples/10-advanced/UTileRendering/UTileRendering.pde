import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import unlekker.data.*;
import unlekker.mb2.externals.*;
import ec.util.*;

/*
 ModelbuilderMk2 - UTileRendering.pde
 Marius Watz - http://workshop.evolutionzone.com
 
 Demonstrates how to use UTileRenderer to render 
 high-res images from realtime graphics. 
 
 */

UNav3D nav;


public void setup() {
  size(600, 600, OPENGL);
  UMB.setPApplet(this);
  nav=new UNav3D();
  nav.rot.set(radians(15), radians(30), 0);

  build();
  colorMesh();
}

public void draw() {
  background(0);

  // if tiler exists, see if we are done tiling. if so, set tiler to null. 
  if (tiler!=null && tiler.done) {
    tiler=null;
  } else if(tiler==null) {
    // the credit text should only be drawn when not tiling
    drawCredit();
  }

  translate(width/2, height/2);
  lights();
  nav.doTransforms();

  noStroke();
  fill(255);
  geo.draw();
}
