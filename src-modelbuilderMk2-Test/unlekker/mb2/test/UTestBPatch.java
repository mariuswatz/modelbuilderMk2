/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PGraphics;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.UMB;

public class UTestBPatch extends UTest {
  PGraphics gg;
  UGeo geo;
  ArrayList<UVertexList> stack;
  UBezierPatch bez;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();

    int n=rndInt(5,10);
    stack=UVertexList.grid2D(n,n, 400,400);
    log("stack "+stack.get(0).size()+" "+stack.size());
    
    n=rndInt(5,(n-2)*(n-2));
    for(int i=0; i<n; i++) {
      int x=rndInt(1,stack.get(0).size()-1);
      int y=rndInt(1,stack.size()-1);
      stack.get(y).get(x).z=rnd(-50,300);
    }
    
    bez=new UBezierPatch(stack);
    geo=bez.eval(20, 20);
    
    log(geo.strGroup());
  }

  public void draw() {
    p.pushMatrix();
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();

    p.noFill();
    p.stroke(255,0,0);
    UMB.draw(stack,true);

    p.fill(255f,100);
    p.stroke(100);;
    if(p.mousePressed) {
      p.noFill();
      p.stroke(50);
    }

    geo.draw();
    
    
    p.popMatrix();
    
    p.fill(255);

    String s=stack.size()+" x "+stack.get(0).size();
    s+=" | mesh res="+bez.resu;
    p.text(s,5,24);

  }

  public void keyPressed(char key) {
  }
}
