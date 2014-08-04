
void build() {  
    // n=resolution of control point grid
    int n=(int)random(3,10);
    stack=UVertexList.grid2D(n,n, 400,400);
    println("stack "+stack.get(0).size()+" "+stack.size());
    
	// randomize "inside" control points
    for(int i=0; i<n; i++) {
      for(int j=0; j<n; j++) {
        stack.get(j).get(i).z=random(-200,200);
      }
    }
	
    // initialize patch
    bez=new UBezierPatch(stack);
    
    // calculate the resulting mesh with a resolution u x n
    int u=(int)random(2,7)*4;  
    int v=u;
    geo=bez.eval(u,v);
    
 }
