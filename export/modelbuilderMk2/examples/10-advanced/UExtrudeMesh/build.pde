UGeo geo, geo2;
UEdgeList border;

void build() {      
  geo=UGeoGenerator.meshPlane(400, 400, 20);

  float m1=geo.bb().min.x;
  float m2=geo.bb().max.x;

  // distort geometry
  float rot=random(100,360);
  for (UVertex vv:geo.getV()) {      
    float a=map(vv.x, m1, m2, 0, 1);
    float b=map(vv.y, geo.bb().min.y, geo.bb().max.y, 0, 1);
    vv.add(0, 0, sin(a*b*PI)*rot);
  }

  rot=random(0.1f,0.6f);
  for (UVertex vv:geo.getV()) {
    float a=map(vv.x, m1, m2, -rot, rot)*HALF_PI;
    vv.rotY(a);
  }
  geo.taint();

  geo.rotX(-HALF_PI).center();

  // get border edges
  border=geo.getEdgeList();
  border=border.getBoundary();

  // extrude mesh to a create a solid form
  geo2=UGeoGenerator.extrude(geo, 10, true);
}
