ArrayList<UVertexList> vvl;

UVertexList vl, vl2;
UGeo geo;

void build() {
  if (random(100)>50) {
    geo=UGeoGenerator.geodesicSphere(300, (int)random(2, 5));
  } else {
    geo=UGeoGenerator.meshBox(300, 500, 250, (int)random(6, 20));
  }
  //  geo=

  for (int i=0; i<geo.sizeF ()/5; i++) deform(5);
  geo.regenerateFaceData();
  USubdivision.subdivide(geo, UMB.SUBDIVCENTROID);
  for (int i=0; i<geo.sizeF ()/2; i++) deform(2);
  geo.regenerateFaceData();
  USubdivision.subdivide(geo, UMB.SUBDIVMIDEDGES);
}

int cnt=0;

void deform(float multiplier) {
  UFace f=UMB.rnd(geo.getF());

  UVertex fv=f.centroid().copy().mult(0.1);
  fv.add(frameCount/100, 0, 0);

  float force=sin(noise(fv.x, fv.y, fv.z)*PI);
  force*=multiplier;

  force*=random(100)>90 ? -random(5, 10) : random(5, 10);

  f.translate(new UVertex(f.normal().copy().mult(force)));
  //    geo.regenerateFaceData(geo.getF().indexOf(f));

  cnt++;
  //    if(cnt>0 && cnt%100==0) USubdivision.subdivide(geo,UMB.SUBDIVMIDEDGES);
}
