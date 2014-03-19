void build() {
  vl=new UVertexList();

  int n=(int)map(mouseX, 0, width-1, 6, 30)*2;

  for (int i=0; i<n; i++) {
    UVertex vv=new UVertex(100, 0, 0);
    vv.rot(map(i, 0, n, 0, TWO_PI));
    if (i%2==0) vv.mult(random(1.5, 4));

    vl.add(vv);
  }

  vl.close();
}

