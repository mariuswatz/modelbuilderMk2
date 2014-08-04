
void build() {  
  geo=new UGeo();

  geo.add(
  UGeoGenerator.meshBox(200, 300, 200, 6).
    rotZ(radians(45)).rotX(radians(45))).center();

  sel=new UGeoGroup(geo);
}

void moveVertices() {
  for (int i=0; i<50; i++) {
    int vid=(int)random(geo.sizeV());
    UVertex rnd=geo.getV(vid);
    UVertex n=geo.getVNormal(vid).copy();
    n.rotX(UMB.rndSigned(0.2f, 1)*DEG_TO_RAD*10);
    n.rotY(UMB.rndSigned(0.2f, 1)*DEG_TO_RAD*10);

    n.mult(random(2,10));
    rnd.add(n);
  }

  long t=System.currentTimeMillis();
  geo.regenerateFaceData();
}

