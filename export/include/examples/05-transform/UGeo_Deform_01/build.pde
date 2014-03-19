public void build() {
  prof=new UVertexList();
  int n=36;
  for (int i=0; i<n; i++) {
    prof.add(new UVertex(50, 0, 0).
      rotY(map(i, 0, n-1, 0, TWO_PI)));

    // U indicates the rotational order
    prof.last().U=map(i, 0, n-1, 0, 1);
  }
  prof.close();

  n=30;
  sweep=new ArrayList<UVertexList>();

  for (int i=0; i<n; i++) {
    sweep.add(prof.copy().
      translate(0, map(i, 0, n-1, 0, 400), 0));
  }

  // create a quadstripped mesh of the profiles 
  geo=new UGeo().quadstrip(sweep);
  geo.center();
}
