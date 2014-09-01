// set face colors using vertex UV info
void colorMesh() {

  // choose two random RGB colors
  ArrayList<Integer> col=new ArrayList<Integer>();
  
  int colN=(int)random(3,7)*2;
  for(int i=0; i<colN; i++) {
    if(random(100)>50) col.add(
      color(random(100, 255), random(100, 255), random(100, 255))
      );
    else col.add(
      color(random(100), random(50,100), random(50,150))
      );
  }

  //  col2=#ffff00;
  geo.regenerateFaceData();

  // use the UV coords of vertices to do linear interpolation
  // of the RGB channels (rarely pretty, but good enough as a
  // technical demo)
  int id=0, n=geo.getF().size();           
  for (UFace f : geo.getF ()) {
    UVertex fv[]=f.getV();
    //    f.setColor(lerpColor(col1,col2,fv[0].U));
    for (UVertex vv : fv) {
      float T=noise(vv.x*0.5, vv.y*0.5, vv.z*0.5)*0.5+random(0.5);
      T=T*(float)col.size();

      int index=(int)(T);
      
      int col1=col.get(index);
      int col2=col.get(index);
      T=T-(float)index;
      
      vv.setColor(
      lerp(red(col1), red(col2), T), 
      lerp(green(col1), green(col2), T), 
      lerp(blue(col1), blue(col2), 1-T));
      //      lerpColor(col1, col2, T));
    }
  }

  geo.enable(geo.COLORVERTEX); // enable per-face coloring
  println(geo.optionStr()); // print options string
}
