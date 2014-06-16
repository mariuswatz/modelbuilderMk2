package unlekker.mb2.test.externals;

import java.io.File;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.XML;
import unlekker.mb2.externals.UXML;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMB;

public class UAHOPolarXML extends PApplet {
  String path="d:/Users/Marius/Dropbox/40 Teaching/AHO/201401 You are Big Data/AHO Dropbox/AHO Dropbox share/Data samples/";
  String filename="monakleven_22.01.2014_export.xml";

  
  public void setup(){
    size(500,500,OPENGL);
     smooth();
    
     loadPolar(path+filename);
  }

   private void loadPolar(String filename) {
     XML rss = loadXML(filename);

     ArrayList<XML> l=new ArrayList<XML>();
     UXML.findNode(rss, "sample", l);
     println(UXML.cnt+" "+ l.get(0).getChild("values"). getContent());
     
     //     rss=rss.getChild("calendar-items/exercise");

     
     println(rss.listChildren());
     // Get all items
//     XML[] items = rss.getChildren("exercise")1;
//     for (int i = 0; i < items.length; i++) {
//       println(items[i].getName());
//       println(items[i].listChildren());
//     }
//       XML data=items[i];
//       
//       XML geoRSSXML = data.getChild("georss:point");
//       String tok[]=split(geoRSSXML.getContent()," ");
//       
//       String str[]=new String[] {
//           data.getChild("title").getContent(),
//           data.getChild("pubDate").getContent(),
//           tok[1],tok[0]
//       };
//       
//       dat.add(str);
  }

  public void draw(){
    background(0);
  }

  public void keyPressed( ) {
    //if(key==' ') newMap();
//    if(key!=CODED && key!=ESC) build();
  }
  
  public static void main(String[] args) {
    PApplet.main("unlekker.mb2.test.externals.UExtTestMain");

  }

}
