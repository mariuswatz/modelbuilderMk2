ArrayList<UVertexList> vvl;

UVertexList vl, vl2;
UGeo geo;

void build() {
  geo=UGeoGenerator.geodesicSphere(300, 2);
  geo=UGeoGenerator.meshBox(200,300,200, 6);
  
  for(int i=0; i<geo.sizeF()/2; i++) deform();
    geo.regenerateFaceData();
  USubdivision.subdivide(geo,UMB.SUBDIVMIDEDGES);
  for(int i=0; i<geo.sizeF()/3; i++) deform();
}

int cnt=0;

void deform() {
    UFace f=UMB.rnd(geo.getF());
    
    UVertex fv=f.centroid().copy().mult(0.1);
    fv.add(frameCount/100,0,0);
    
    float force=sin(noise(fv.x,fv.y,fv.z)*PI);
    force*=random(100)>80 ? -random(10,30) : random(10,30);
    
    f.translate(new UVertex(f.normal().copy().mult(force)));
//    geo.regenerateFaceData(geo.getF().indexOf(f));
    
    if(cnt>0 && (cnt++)%30==0) USubdivision.subdivide(geo,UMB.SUBDIVMIDEDGES);
}
