package unlekker.mb2.test.externals;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.Table;
import processing.data.TableRow;
import unlekker.mb2.data.UDataCategory;
import unlekker.mb2.data.UDataPoint;
import unlekker.mb2.data.UDataTaxonomy;
import unlekker.mb2.data.UTimestamp;
import unlekker.mb2.externals.UMapping;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMB;

public class UExtTestTaxonomy extends PApplet {
  PImage img;
  
  String path="c:/Users/marius/Dropbox/03 Code/ITP2013Parametric/nosync/data/";
  String filename="20140101-20140120-rescuetime.csv";

  UNav3D nav;
  ArrayList<UVertexList> sl=new ArrayList<UVertexList>();
  UGeo geo,geo2;
  
  UMapping geodata;
  UVertexList vl;
  
  UVertex geobb[];
  ArrayList<String> mapNames;
  
  UDataTaxonomy activity=new UDataTaxonomy();
  UDataTaxonomy category=new UDataTaxonomy();
  
  
  public void setup(){
    size(1000,1000,OPENGL);
    smooth();
    
    UMB.setPApplet(this);
    nav=new UNav3D();
    
    smooth();

    loadData();
  }

  private void loadData() {
    // Date,Time Spent (seconds),Number of People,Activity,Category,Productivity
    
    Table tab=loadTable(path+filename,"header");
    for(TableRow r:tab.rows()) {
      
      UDataPoint pt=new UDataPoint(UTimestamp.parseLong(r.getString(0)));      
      pt.add(r.getInt(1));// time
      pt.add(r.getInt(5));// productivity
      
      UDataCategory a=activity.add(r.getString(3)).add(pt);
      UDataCategory c=category.add(r.getString(4)).add(pt);
      
      
      
    }
    
    int cnt=0;
    println("-----------activity");
    for(UDataCategory c:activity) {
      println((cnt++)+"\t"+c.name+" "+c.data.size()+
          "\t"+c.data.get(0).categories().get(0).name+
          "\t"+c.data.get(0).categories().get(1).name);
    }

    cnt=0;
    println("-----------category");
    for(UDataCategory c:category) println((cnt++)+"\t"+c.name+" "+c.data.size());
  }

  public void draw(){
    background(0);

  }

  public void keyPressed( ) {
//    if(key==' ') newMap();
//    if(key!=CODED && key!=ESC) build();
  }
  
  public static void main(String[] args) {
    PApplet.main("unlekker.mb2.test.externals.UExtTestMain");

  }

}
