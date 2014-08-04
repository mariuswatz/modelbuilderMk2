void build() {
  vl=new UVertexList();
  UVertex v=new UVertex(100, 0);
  
  int n=UMB.rndInt(10,50);
  
  for (int i=0; i<n; i++) {
    vl.add(v.copy().rotZ(map(i, 0, n, 0, TWO_PI)).
      mult(vl.rnd(1, 2)).
      add(0, 0, random(-200, 200)));

    if(i>0) {
      // set Z of vertex to 80% of last Z value (if any)
      // and 20% of the new random value 
      vl.get(i).z=(vl.get(i).z*0.3f+vl.get(i-1).z*0.7f);
    }
  }
  
  vl.close();

  // center the vertex list around origin
  vl.center();
  
  // scale the vertex list so that its width is 500
  vl.scale(300f/vl.bb().dimX());

  vl.log("Centroid: "+vl.centroid().str());
  vl.log("UBB: "+vl.bb().str());
  vl.log("Vertices: "+vl.bb.str());

  // create quadstrip mesh from vl and a copy of vl
  // scaled to 20%  
  geo=new UGeo().quadstrip(vl,vl.copy().scale(0.2f));
}
