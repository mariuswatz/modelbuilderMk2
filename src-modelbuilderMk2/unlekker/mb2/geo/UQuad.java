/*
 * modelbuilderMk2
 */
package unlekker.mb2.geo;

import java.util.Arrays;

import unlekker.mb2.util.UMB;

/**
 * Represents a quad face. Quads in a {@link UGeo} instance can be accessed through
 * {@link UGeo#getQ()} and {@link UGeoGroup#getQ()}.
 * 
 * @author marius
 *
 */
public class UQuad extends UFace  {
  /**
   * The two UFace instances that make up this quad.
   */
  public UFace f[];
  
  public UQuad() {
    ID=globalID++;
    vertexCount=4;
    vID=new int[] {-1,-1,-1,-1};
    col=Integer.MAX_VALUE;
  }

  public UQuad(UFace f1,UFace f2) {
    this();
    f=new UFace[] {f1,f2};
    
    parent=f1.parent;
    
    // from endShape():
    //    vID[id],vID[id+1],vID[id+2]
    //    vID[id],vID[id+2],vID[id+3]


    vID[0]=f1.vID[0];
    vID[1]=f2.vID[2];
    vID[2]=f2.vID[0];
    vID[3]=f1.vID[2];
    
//    log(str(vID)+" "+str(f1.vID)+" "+str(f2.vID));
  }
  
  public UFace setColor(int a) {
    col=a;
    f[0].setColor(a);
    f[1].setColor(a);
    return this;
  }

  public UQuad(UQuad v) {
    this();
    set(v);
  }

  public UQuad draw() {
    if(checkGraphicsSet()) {
//      if(col!=Integer.MAX_VALUE) g.fill(col);
      if(v==null) getV();
      g.beginShape(QUADS);
      pvertex(v);
      g.endShape();
      
      int cnt=2;
      for(UVertex vv:getV()) pellipse(vv, cnt++);
    }
    
    return this;
  }
  
  public UQuad copy() {
    return new UQuad(this);
  }
}
