ArrayList<UVertexList> vvl;

UVertexList vl, vl2;
UGeo geo;

void build() {
  vvl=new ArrayList<UVertexList>();

  UVertexList.setGraphics(g);
  vl=new UVertexList();

  int n=UMb.rndInt(4, 9)*2;
  for (int i=0; i<n; i++) {
    float w=25+(i%2)*50;
    vl.add(new UVertex(w, 0, 0).
      rotZ(map(i, 0, n, 0, TWO_PI)));
  }
  vl.close();
  
    // vertices were added clock-wise, so reverse
    vl.reverse(); 

    vl.translate(-50, -150, 0);
    for (int i=0; i<30; i++) {
      // chained commands:
      // #1 copy vertex list
      // #2 rotate and translate copy
      // #3 add copy to vvl
      // Remember: The original "vl" is left unchanged
      vvl.add(vl.copy().
        scale(map(i,0,29,0.25,1)).
        rotX(map(i,0,29,0.1,0.9)*TWO_PI).
        translate(0, 0, 20).rotY(HALF_PI));

      //      println(vl.str());
    }

    // set UV coordinates for list of UVertexList
    UVertexList.setUV(vvl);

    geo=new UGeo();
    // make quadstrips of all vertex lists in vvl
    geo.quadstrip(vvl);

    // fill first and last vertex lists w/ triangle fans
    geo.triangleFan(vvl.get(vvl.size()-1));

    // the first fan needs to have its order reversed to face
    // the right way
    geo.triangleFan(vvl.get(0), true);
    geo.center();

    // write STL using incremental file naming
    // "data/" is the root dir, "test" is the file prefix
    //    UGeoIO.writeSTL(UFile.nextFilename("data\\", "test"), geo);
  }

