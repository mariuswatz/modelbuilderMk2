void build() {
  vl=new UVertexList();

  int n=(int)random(9, 37)*2;
  float w=(float)width*0.33f;
  
  float m=random(0.33,1)*w;
  for (int i=0; i<n; i++) {
    m=m*0.2f+random(0.33,1)*w*0.8f;
    float t=-map(i, 0, n, 0, TWO_PI);
    vl.add(new UVertex(m, 0).rotY(t));
  }  
  vl.close();

  vlSmooth=UVertexList.smooth(vl, 3);

  stack=new ArrayList<UVertexList>();

  n=10;
  m=random(0.25f, 0.5f);
  for (int i=0; i<n; i++) {
    m=m*0.2f+random(0.5f, 1.5f)*0.8f;
    if (i==n-1) m=random(0.25f, 0.5f);

    float y=map(i, 0, n-1, 0, height-100);
    stack.add(vl.copy().scale(m).translate(0, y, 0));
  }

  UVertexList.center(stack);

  // smooth the stack of edges
  stackSmooth=UVertexList.smooth(stack, 3);

  geo=new UGeo().quadstrip(stack);    
  geoSmooth=new UGeo().quadstrip(stackSmooth);
}
