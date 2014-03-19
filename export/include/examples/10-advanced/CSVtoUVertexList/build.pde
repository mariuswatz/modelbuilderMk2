UVertexList vlGraph,vlRadial;
float W=350;

void build() {
  int n=val.length;
  
  // vertices representing a conventional XY value graph
  vlGraph=new UVertexList();
  
  // vertices representing values plotted radially
  vlRadial=new UVertexList();
  
  // build vertex lists
  for (int i=0; i<n; i++) {
    vlGraph.add(new UVertex(i,(val[i])*W,0));
    
    vlRadial.add(
      new UVertex((val[i])*W*0.5,0).
      rotZ(map(i,0,n,0,TWO_PI)));
  }
  
  // add vertices to make vlGraph a closed graph
  // with a defined baseline  
  vlGraph.add(vlGraph.last().copy().mult(1,0,1));
  vlGraph.insert(0, new UVertex(0,0)).close();

  // scale graph to proportions of n vs maxval  
  vlGraph.scale(1,(float)n/maxval,1).center();
  
  // scale to absolute width W  
  vlGraph.scale(W/vlGraph.bb().dimX());
  
  // scale Y by -1 so graph will look correct on screen   
  vlGraph.scale(1,-1,1);
  
  vlGraph.log(vlGraph.str());
}
