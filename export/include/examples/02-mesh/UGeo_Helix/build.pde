float a,ac,r,rc,d,dc;
float z,zc;
int n;
ArrayList<UVertexList> circles;
UGeo helix3D;


void build() {
  circles=new ArrayList<UVertexList>();
  
  n=100;
  a=0;
  ac=radians(360*6)/(float)n;
  r=10;
  rc=0.1;
  z=0; 
  zc=3;

  float circRad=random(10,50);
  
  UVertexList circ=new UVertexList();
  int circn=12;
  for(int i=0; i<circn; i++) {
    circ.add(new UVertex(circRad,0,0).rotY(map(i,0,circn,0,TWO_PI)));
  }
  circ.close();
  
  helix=new UVertexList();
  d=random(1,5)/100f;
  dc=random(5,10)/100f;
  for(int i=0; i<n; i++) {
    helix.add(new UVertex(r,0,0).rotZ(a).add(0,0,z));
    circles.add(circ.copy().
      scale(d).
      rotZ(a).translate(helix.last()));
      
    d=d*(1+dc);
    a=a+ac;
    r=r+rc;
    z=z+zc*d;
  }  
  
  
  helix3D=new UGeo().quadstrip(circles).center();
  helix3D.scaleToDim(500).rotX(-HALF_PI);
}
