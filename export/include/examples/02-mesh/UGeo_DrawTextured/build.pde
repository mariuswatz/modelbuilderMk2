public void build() {
    prof=new UVertexList();
    prof.add(10,0,0);
    prof.add(100,0,0);
    prof.add(100,200,0);
    prof.add(80,200,0);
    prof.add(80,20,0);
    prof.add(10,20,0);
    
    
    // we'll set U and V for each vertex, so that they
    // can be used for texturing later
    
    int n=prof.size(),cnt=0;
    for(UVertex vv:prof) {
      float t=map(cnt++, 0,n-1,0,1);
      vv.U=t;
    }
    
    n=30;
    sweep=new ArrayList<UVertexList>();
        
    for(int i=0; i<n; i++) {
      float t=map(i, 0,n-1,0,1);
      UVertexList profCopy=prof.copy().rotY(map(i,0,n, 0,TWO_PI));
      
      for(UVertex vv:profCopy) {
        vv.V=t;
      }
      
      sweep.add(profCopy);
    }
    
    UVertexList vlFirst=new UVertexList();
    for(UVertexList l:sweep) vlFirst.add(l.first());
    
    UVertexList vlLast=new UVertexList();
    for(UVertexList l:sweep) vlLast.add(l.last());
        
    // create a quadstripped mesh of the profiles 
    geo=new UGeo().quadstrip(sweep);
    geo.quadstrip(sweep.get(sweep.size()-1),sweep.get(0));
    
    geo.triangleFan(vlFirst.close());
    geo.triangleFan(vlLast.close().reverse());
    geo.center().scaleToDim(width-200).rotX(HALF_PI);
    
    for(UVertex vv:geo.getV()) {
      println(vv.y+" "+vv.U+" "+vv.V);
    }

  }
