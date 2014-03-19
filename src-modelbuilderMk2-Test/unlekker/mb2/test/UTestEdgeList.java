/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PGraphics;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.UMB;

public class UTestEdgeList extends UTest {
  PGraphics gg;
  UVertexList vl;
  UGeo geo;
  UEdgeList l;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();

    geo=UGeo.cyl(150, 200, 12);
    UMB.log("cyl "+geo.sizeGroup());
    UGeo in=UGeo.box(150, 200, 100);
    l=new UEdgeList(in);
    UMB.log(in.sizeF()+" "+in.sizeV()+" "+l.size());
    for(UEdge e:l) UMB.log(
        e.getV()[0].ID+" "+e.getV()[1].ID+"   "+
        UMB.str(e.getV()));
    
    outlineBox();

/*    
    geo=new UGeo();//.enable(UMB.NOCOPY);
    for(int i=0; i<geo.sizeGroup(); i++) {
      UVertexList ql=geo.getGroupQuadV(i);
      UMB.log(ql.str());
      for(int j=0; j<ql.size(); j+=2) {
        UVertex v1=ql.get(j);
        UVertex v2=ql.get(j+1);
        float r=UMB.rnd(2,10);
        if((cnt++)%2==0) r=5;


        geo.add(UHeading.boxVector(v1,v2, r));
      }
    }
    
    UMB.log(geo.str());
*/    
    geo.enable(geo.COLORFACE);
  }

  private int outlineBox() {
    UHeading rot=new UHeading(new UVertex(100,100,0));
    UMB.log(rot.getAngles().strDeg());
    
    geo=new UGeo().enable(UMB.NOCOPY);
    int cnt=0;
    for(UEdge e:l) {
      UMB.logDivider("cnt "+cnt);
      float r=UMB.rnd(8,12);
      if((cnt++)%2==0) r=5;
      
      
      UVertex v1=e.v[0].copy();
      UVertex v2=e.v[1].copy();
//      UVertex D=v1.delta(v2);
//      float m=D.mag();
//      m=r/m;
      float rr=10;
//      v1.add(D.copy().norm(-rr));
//      v2.add(D.copy().norm(rr));
//      
      
      geo.add(UHeading.boxVector(v1,v2, r,rr));
//      geo.add(UHeading.boxVector(e.v[0], e.v[1], r));
    }
    
    UMB.logDivider("groups: "+geo.sizeGroup());
//    for(int i=0; i<geo.sizeGroup(); i++) {
//      ArrayList<UFace> ff=geo.getGroup(i);
//      int col=p.color((i*30)%255);
//      for(UFace f:ff) f.setColor(col);
//      int id[]=geo.getGroupID(i);
//      UMB.logf("%d | %s",ff.size(),UMB.str(id));
//      UVertexList vl=geo.getGroupV(i);
////      for(UVertex vv:vl) UMB.log(vv.ID);
//    }
    
    return cnt;
  }
  
  public void draw() {
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();
    
    p.stroke(100);
    p.strokeWeight(1);
    p.fill(255);
    geo.draw();
    
    p.noFill();
    p.strokeWeight(5);
    
    int cnt=0;
    for(UEdge e:l) if((cnt++)<((p.frameCount/5)%l.size())) {
      p.stroke((cnt*20)%255,cnt%255,0);
      e.draw();
    }
    
    
    p.strokeWeight(1);
    p.stroke(255,255,0);
    UVertexList pr=UGeoGenerator.roundedProfile(200, 50, 10);//.close();
    pr.center();
    pr.draw();
    for(UVertex vv:pr) UMB.pellipse(vv, 5,5);
    p.rect(-50,-200,50,50);
    p.rect(-50,150,50,50);
    pr.bb().draw();
  }

  
}
