package unlekker.mb2.test.externals;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.pdf.PGraphicsPDF;
import unlekker.mb2.util.UMB;
import unlekker.mb2.externals.UGeomerative;
import unlekker.mb2.externals.UPoly2Tri;
import unlekker.mb2.geo.*;
import geomerative.*;

public class UExtTestMain extends PApplet {
  RShape shp;

  RShape polyshp;
  RPolygon poly;
  RMesh mesh;

  String path="C:/Users/marius/Dropbox/03 Code/ITP2013Parametric/nosync/data/";
  String filename="testB.svg";

  UNav3D nav;
  ArrayList<UVertexList> sl=new ArrayList<UVertexList>();
  UGeo geo,geo2;
  
  public void setup(){
    size(600, 600,OPENGL);
    smooth();
    
    UMB.setPApplet(this);
    nav=new UNav3D();
    
    // VERY IMPORTANT: Allways initialize the library before using it
    RG.init(this);


    if(geo==null) build();
    
    
  }

  public void draw(){
    background(255);

    translate(width/2,height/2);
    nav.doTransforms();


    noFill();
    stroke(0);
    
    if(mousePressed) {
      int cnt=0;
      geo2.draw().drawVertexNormals(10);
    }
    else {
      fill(0,255,255);
      
      geo.draw().drawVertexNormals(10);
    }
//    RG.shape(polyshp);
  }

  public void keyPressed( ) {
    if(key!=CODED && key!=ESC) build();
  }

  void build() {
    filename="my_tile2.svg";
    shp = RG.loadShape(path+filename);
    shp = RG.centerIn(shp, g, 100);
    UVertexList vl,vl2;
    vl=new UVertexList();
    for(int i=0; i<5; i++) vl.add(i,0);
    
    vl.close();
    vl.insert(2, vl.get(1));
    println(UMB.str(vl.removeDuplID()));

    println(vl.str());

      // We decided the separation between the polygon points dependent of the mouseX
    float pointSeparation = map(constrain(mouseX, 100, width-100), 
        100, width-100, 4, 20);

//     We create the polygonized version
    RG.setPolygonizer(RG.UNIFORMLENGTH);
    RG.setPolygonizerLength(pointSeparation);

    polyshp = RG.polygonize(shp);
    poly=polyshp.toPolygon();
    
    
    ArrayList<UVertexList> cont=UGeomerative.fromRContour(poly.contours);
    println("contours "+cont.size());
    
//    for(UVertexList tmp:cont) {
//      tmp.removeDupl(true);
////      println("isClockwise "+tmp.isClockwise());
//    }
    
   
//    PGraphicsPDF pdf=(PGraphicsPDF)createGraphics(1000,1000,PDF,path+"test1.pdf");
//  UMB.setGraphics(pdf); 
//  pdf.beginDraw();
//  pdf.translate(500,500);
//  pdf.stroke(0);
//  geo.draw();
//  pdf.endDraw();
//  pdf.flush();
//  pdf.dispose();
    
    geo2=UPoly2Tri.triangulate(cont);

    geo=geo2.copy();
    geo.extrudeSelf(50, true);

//    geo.removeDuplV().regenerateFaceData();
    geo.writeSTL(path+"test1.stl");


  }
  
  public static void main(String[] args) {
//    PApplet.main("unlekker.mb2.test.externals.UExtTestGeo");
//    PApplet.main("unlekker.mb2.test.externals.UExtTestTaxonomy");
    PApplet.main("unlekker.mb2.test.externals.UAHOPolarXML");
    
  }

}
