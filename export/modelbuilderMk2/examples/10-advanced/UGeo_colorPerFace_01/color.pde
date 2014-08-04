// set face colors using vertex UV info
void colorMesh() {
  
  // choose two random RGB colors
  int col1=color(random(255), random(255), random(255));
  int col2=color(random(255), random(255), random(255));

  // use the UV coords of vertices to do linear interpolation
  // of the RGB channels (rarely pretty, but good enough as a
  // technical demo)
  int id=0, n=geo.getF().size();           
  for (UFace f:geo.getF()) {
    UVertex fv[]=f.getV();
    f.setColor(
    lerp(red(col1), red(col2), fv[0].U), 
    lerp(green(col1), green(col2), fv[0].V), 
    lerp(blue(col1), blue(col2), fv[0].V));
  }

  geo.enable(geo.COLORFACE); // enable per-face coloring
  println(geo.optionStr()); // print options string
}

