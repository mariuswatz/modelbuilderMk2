UTileRenderer tiler;

/*

renderTiles() initializes an instance of UTileRenderer with a filename
and the desired number of tiles to render. Since UTileRenderer registers 
"pre" and "post" methods with PApplet to work its magic in the background, 
no further direct action is necessary.

See draw() for a simple if() statement that checks if we're done tiling,
in which case we can dispose of the UTileRenderer instance.

For the tiles to render correctly there should be no changes in camera or
other animation until the image is complete. Similarly, ControlP5 or other
auto-drawing components should be disabled while rendering.

*/


void renderTiles() {
  String filename=UFile.nextFile(sketchPath, "Tile");
  
  // If ".tga" is specified as the file extension the file
  // will be saved using a progressive Targa encoder that 
  // is much more memory-efficient than a single large PImage.
  // (This code was contributed by Dave Bollinger, thanks Dave!)
  // 
  // Using Targa, rendering resolutions above 10k x 10k pixels is rarely 
  // an issue. Similar resolutions when using PNG can often cause Java
  // to abort with a "out of heap memory" failure. You can allocate more
  // memory to Java in the Processing preference panel, but if you need
  // giant file sizes Targa is the preferred option.
  //
  

  int n=6;
//  filename+=".png";

  n=20; // not recommended with PNG
  filename+=".tga";
  
  tiler=new UTileRenderer(filename, n);
  tiler.start();
}
