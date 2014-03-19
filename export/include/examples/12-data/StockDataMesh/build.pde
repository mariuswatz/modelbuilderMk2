  void build() {
    UVertexList vl=new UVertexList();
    
    int cnt=0;
    float h=200;
     
    // create a vertex list representing the input data   
    for(float theVal : values) {
      // map all values to radial XZ positions, with Y given by value
      vl.add(new UVertex(200,-h*theVal).
          rotY(map(cnt,0,values.size(), 0,TWO_PI)));
      
      cnt++;
    }
    
    // multipliers to create a conical shape  
    float m=0.4f;
    float m2=0.6f;
    
    // vertex lists representing the inner and outer circles,
    // created by copying "vl" and scaling it in XZ by a multiplier.
    // by scaling Y by zero we constrain the vertices to the base plane.
    UVertexList circleInner=vl.copy().scale(m,0,m);
    UVertexList circleOuter=vl.copy().scale(m2,0,m2);

    // create quadstrips between the edges     
    dataGeo=new UGeo().quadstrip(circleInner,vl);
    dataGeo.quadstrip(vl, circleOuter);
    dataGeo.quadstrip(circleOuter,circleInner);
    
    // add a single face at start and end of the three edges to
    // cap the geometry, making it solid
    dataGeo.addFace(circleOuter.first(),circleInner.first(), vl.first());
    dataGeo.addFace(circleInner.last(),circleOuter.last(),vl.last());
    
//    // reverse the last face to fix th
//    dataGeo.getF().get(dataGeo.sizeF()-1).reverse();
    
    // export the STL
    dataGeo.writeSTL(sketchPath+"/"+UFile.noExt(filename)+".stl");
    
    
  }
