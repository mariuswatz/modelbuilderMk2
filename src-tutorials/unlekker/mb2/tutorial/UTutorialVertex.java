package unlekker.mb2.tutorial;

import unlekker.mb2.geo.*;

public class UTutorialVertex extends UTutorial {
  UVertex a,b,origin;  
  
  public void init() {
    origin=new UVertex();
    
    a=new UVertex(75,0);
    b=new UVertex(125,0);
    
    float deg=p.random(PI);
    a.rot(deg);
    deg+=p.random(0.25f,0.75f)*PI;
    b.rot(deg);
  }
  
  public void draw() {
//    p.nav.doTransforms();
    p.stroke(p.cHighlight);
    p.strokeWeight(2);

    
    UMB.pcross(a, 6);
    UMB.pcross(b, 6);
    
    
    p.strokeWeight(1);
    UMB.pline(a,b);
    
    p.stroke(p.cOutline);
    UMB.pline(origin,a);
    UMB.pline(origin,b);
    
    p.fill(p.cText);
//    p.textAlign(CENTER);
    
    
    float D=sign(b.y-a.y)*14;
    p.text("a="+a.str2D(), (int)a.x-50, (int)(a.y-D));
    p.text("b="+b.str2D(), (int)b.x-50, (int)(b.y+D));

  }
}
