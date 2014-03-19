package unlekker.mb2.test.externals;

import java.io.File;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import unlekker.mb2.externals.UMapping;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMB;

public class UExtTestGeo extends PApplet {
  PImage img;
  
  String path="d:/Users/marius/Dropbox/03 Code/ITP2013Parametric/nosync/data/";
  String filename="testB.svg";

  UNav3D nav;
  ArrayList<UVertexList> sl=new ArrayList<UVertexList>();
  UGeo geo,geo2;
  
  UMapping geodata;
  UVertexList vl;
  
  UVertex geobb[];
  ArrayList<String> mapNames;
  
  public void setup(){
    size(1000,1000,OPENGL);
    smooth();
    
    UMB.setPApplet(this);
    nav=new UNav3D();
    
    mapNames=new ArrayList<String>();
    String[] s=new File(path).list();
    for(String ss:s) if(ss.endsWith(".png")) {
       ss=ss.substring(0,ss.indexOf('.'));
       if(new File(path+ss+".dat").exists()) {
         mapNames.add(ss);
       }
    }
    
    
    img=loadImage(path+"europe.png");
    geobb=new UVertex[] {
        new UVertex(56.54f, -12.56f),new UVertex(39.88f, 32.34f), 
        new UVertex(52.5f, 13.34f), 
        new UVertex(51.5f, -0.1f),
        new UVertex(45.44f, 12.34f), 
        new UVertex(41.01f, 28.98f)
//        berlin = mercatorMap.getScreenLocation(new PVector(52.5, 13.34));
//        london = mercatorMap.getScreenLocation(new PVector(51.5f, -0.1f));
//        venice = mercatorMap.getScreenLocation(new PVector(45.44, 12.34));
//        istanbul = mercatorMap.getScreenLocation(new PVector(41.01, 28.98));

        
    };

    newMap();
    
    
//    geodata=UGeoData.parseOpenPathsCSV(path+"20140123-openpaths-mariuswatz.csv");
    geodata=UMapping.parseFoursquare(path+"20140122 foursquare_historyMW.xml");
    
    vl=geodata.getPointList();
    smooth();
    
  }

  private void newMap() {
    String prefix=UMB.rnd(mapNames);
    img=loadImage(path+prefix+".png");
    String[] bb=loadStrings(path+prefix+".dat");
    geobb[0].set(Float.parseFloat(bb[0]),Float.parseFloat(bb[1]));
    geobb[1].set(Float.parseFloat(bb[2]),Float.parseFloat(bb[3]));
  }

  public void draw(){
    background(0);

/*    pushMatrix();
    translate(width/2,height/2);
//    nav.doTransforms();
    
    translate(-vl.bb().centroid().x,-vl.bb().centroid().y);
    scale(5);
    strokeWeight(0.2f);
    noFill();
    
    UVertex last=null;
    for(UVertex v:vl) {
      stroke(0);
      v.pellipse(v,0.1f, 0.1f);
      if(last!=null) {
        if(v.dist(last)>15) stroke(200);
        v.pline(v,last);
      }

      last=v;
    }
    popMatrix();
*/    
//    noLights();
    noTint();
    stroke(255,0,0);
//    strokeWeight(2);
    noFill();
//    translate(100,100);
    translate(width/2,height/2);
    nav.doTransforms();
    translate(-img.width/2,-img.height/2,-5);

    fill(img.get(300,300));
    rect(-img.width,-img.height,img.width*3,img.height*3);
    translate(0,0,5);
    image(img,0,0);

    UVertex last=null;
    for(UVertex v:vl) {
      UVertex res=UMapping.mercatorMapping(v, geobb, img.width,img.height);
      fill(255,100,0);
      noStroke();
      UMB.pellipse(res,4);
      stroke(255,100,0);
      noFill();
      res.z=20;
      UMB.pellipse(res,12);
//      strokeWeight(0.6f);
      point(res.x,res.y);
      
      if(last!=null) v.pline(res,new UVertex(res.x,res.y,0));
      last=res;
      
    }
  }

  public void keyPressed( ) {
    if(key==' ') newMap();
//    if(key!=CODED && key!=ESC) build();
  }
  
  public static void main(String[] args) {
    PApplet.main("unlekker.mb2.test.externals.UExtTestMain");

  }

}
