import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import unlekker.mb2.externals.*;
import unlekker.mb2.data.*;
import ec.util.*;

import geomerative.*;

RShape shp;
RShape polyshp;
RPolygon poly;

String filename="my_tile2.svg";

UNav3D nav;
ArrayList<UVertexList> sl=new ArrayList<UVertexList>();
UGeo geo, geo2;

public void setup() {
  size(600, 600, OPENGL);
  smooth();

  UMB.setPApplet(this);
  nav=new UNav3D();

  // Initialize Geomerative
  RG.init(this);

  // load SVG with Geomerative
  shp = RG.loadShape(dataPath(filename));
  shp = RG.centerIn(shp, g, 100);

  build();
}

public void draw() {
  background(255);

  translate(width/2, height/2);
  nav.doTransforms();
  
  noFill();
  stroke(0);

  if (mousePressed) {
    // draw flat triangulated mesh
    fill(255, 255, 0);
    geo2.draw().drawVertexNormals(10);
  }
  else {
    // draw 3D solid extrusion of triangulation
    fill(0, 255, 255);
    geo.vertexNormals();
    geo.draw();
  }
}

public void keyPressed( ) {
  if (key!=CODED && key!=ESC) build();
}

void build() {
  // We decided the separation between the polygon points dependent of the mouseX
  float pointSeparation = map(constrain(mouseX, 100, width-100), 
    100, width-100, 4, 20);

  //     We create the polygonized version
  RG.setPolygonizer(RG.UNIFORMLENGTH);
  RG.setPolygonizerLength(pointSeparation);

  polyshp = RG.polygonize(shp);
  poly=polyshp.toPolygon();

  // triangulate polygon to UGeo
  ArrayList<UVertexList> cont=UGeomerative.fromRContour(poly.contours);
  println("contours "+cont.size());
  for(UVertexList vl:cont) if(vl.hasDuplicates()) println("dupl");
  geo2=UPoly2Tri.triangulate(cont);

  // make a copy of triangulated mesh and extrude it to make solid 3D form
  geo=geo2.copy();
  geo.extrudeSelf(50, true);

  geo.writeSTL(sketchPath(filename+".stl"));
  geo2.writeSTL(sketchPath(filename+"-flat.stl"));
}
