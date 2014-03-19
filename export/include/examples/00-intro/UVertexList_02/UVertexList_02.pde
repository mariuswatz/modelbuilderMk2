import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

  UVertexList vl,vl2;
  UGeo geo;
  
  public void setup() {
    size(600,600, OPENGL);
    UMB.setGraphics(this);

    build();    
  }

  public void draw() {
    background(0);
    
    translate(width/2,height/2);

    if(mousePressed) build();
    
    float ry=map(width/2-mouseX,-width/2,width/2, PI,-PI);
    float rx=map(height/2-mouseY,-height/2,height/2, PI,-PI);
    rotateY(ry+radians(frameCount));
    rotateX(rx);
    
    
    stroke(255,50,0);
    noFill();
    
    // draw line strip of all points in vl
    vl.draw();
    
    // draw lines from each point towards center
    stroke(255,255,0);
    beginShape(LINES);
    for(UVertex vv:vl) {
       vertex(vv.x,vv.y,vv.z);
       vertex(vv.x/4,vv.y/4,vv.z);
    }
    endShape();
    
    int id=0;
    UVertex vv2;
    
    stroke(0,255,255);
    for(UVertex vv:vl) {
       vv2=vl2.get(id++);
       vertex(vv.x/4,vv.y/4,vv.z);
       vertex(vv2.x/4,vv2.y/4,vv2.z);

       vertex(vv2.x/4,vv2.y/4,vv2.z);
       vertex(vv.x,vv.y,vv.z);

       vertex(vv2.x/4,vv2.y/4,vv2.z);
       vertex(0,0,0);

       vertex(0,0,0);
       vertex(vv.x/4,vv.y/4,vv.z);
    }
    endShape();
  }

void build() {
      
    int n=(int)map(mouseX, 0,width-1,6,30)*2;
    if(vl==null)           vl=new UVertexList();
    if(vl!=null && n==vl.size()-1) return;
    
    vl.clear();
    
    println(vl.size()+" "+n);

    for(int i=0; i<n; i++) {
      UVertex vv=new UVertex(100,0,0);
      vv.rot(map(i,0,n, 0,TWO_PI));
      if(i%2==0) vv.mult(random(1.5,4));
     
      vl.add(vv); 
    }

    vl.close();
        println(vl.size()+" "+n);

    
    vl2=vl.copy();
    for(UVertex vv:vl2) vv.z=UMB.rndSigned(50,100);
}
