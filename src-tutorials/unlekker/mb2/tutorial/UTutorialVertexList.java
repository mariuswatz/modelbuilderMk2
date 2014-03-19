package unlekker.mb2.tutorial;

import unlekker.mb2.geo.*;

public class UTutorialVertexList extends UTutorial {
  UVertexList vl;
  UVertex origin=new UVertex();
  
  public void init() {
    vl=new UVertexList();
    
    int n=rndInt(6,20)*2;
    for(int i=0; i<n; i++) {
      float a=map(i, 0,n,0,TWO_PI);
      float x=rnd(50,175);
      vl.add(new UVertex(x,0).rot(a));
    }
    
    vl.close();
  }
  
  public void draw() {
//    p.nav.doTransforms();
    p.strokeWeight(2);
    p.stroke(p.cHighlight);
    vl.draw();

    p.strokeWeight(1);
    p.stroke(p.cOutline);
    
    for(UVertex v:vl) {
      UMB.pcross(v, 6);
      UMB.pline(origin,v);
    }
  }
}
