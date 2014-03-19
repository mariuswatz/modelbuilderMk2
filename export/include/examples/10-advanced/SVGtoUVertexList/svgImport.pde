// This function does the real work. Given a valid
// SVG filename it will read the file, convert the
// paths to point lists and return a UVertexList
// version.

UVertexList svgToVL(String filename) {
  // VERY IMPORTANT: Allways initialize the library before using it
  RG.init(this);
  RShape grp;
  RPoint[][] pointPaths;

  RG.setPolygonizer(RG.ADAPTATIVE);
  
  // alternative method providing more control of number of points
  // generated
//  RG.setPolygonizer(RG.UNIFORMLENGTH);
//  RG.setPolygonizerLength(10);
  
  grp = RG.loadShape(dir+filename);
  grp.centerIn(g, 100, 1, 1);
  pointPaths = grp.getPointsInPaths();

  UVertexList path=new UVertexList();

  // transfer Geomerative points to UVertexList
  for (int i = 0; i<pointPaths.length; i++) {
    for (RPoint pt:pointPaths[i]) path.add(pt.x, pt.y);
  }

  // scale vertex list so that its X dimension is width-100
  float sz=width-100;
  sz=sz/path.dimensions().x;
  
  // center and scale vertex list
  path.center().scale(sz);

  return path;
}  

