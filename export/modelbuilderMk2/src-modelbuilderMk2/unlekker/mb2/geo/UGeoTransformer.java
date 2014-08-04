/*
 * modelbuilderMk2
 */
package unlekker.mb2.geo;

import java.util.ArrayList;
import java.util.Arrays;

import unlekker.mb2.util.UMB;


public class UGeoTransformer extends UMB  {
  protected static UVertexList vl;
  protected static UVertex [] vD=new UVertex[] {
      new UVertex(),new UVertex(),new UVertex()
  };

  public static ArrayList<UFace> window(UFace f,float offs,
      float normalOffs,boolean hasHole) {
    return window(f,offs,normalOffs,hasHole,null);
  }

  public static ArrayList<UFace> window(UFace f,float offs,
      float normalOffs,boolean hasHole,ArrayList<UFace> output) {
    UVertex vf[]=f.getV();
        
    if(output==null) output=new ArrayList<UFace>();
    UGeo geo=f.parent;

    
    vD[0].set(vf[1].copy()).sub(vf[0]);
    vD[1].set(vf[2].copy()).sub(vf[1]);
    vD[2].set(vf[0].copy()).sub(vf[2]);
    
    for(UVertex tmp:vD) tmp.norm(offs);
    
    int cnt=0;
    
    if(vl==null) {
      vl=new UVertexList();
      for(int i=0; i<9; i++) vl.add(0,0);
    }
    
    
    vl.get(cnt++).set(vf[0]).add(vD[0]);
    vl.get(cnt++).set(vf[1]).sub(vD[0]);
    vl.get(cnt++).set(vf[1]).add(vD[1]);
    vl.get(cnt++).set(vf[2]).sub(vD[1]);
    vl.get(cnt++).set(vf[2]).add(vD[2]);
    vl.get(cnt++).set(vf[0]).sub(vD[2]);
    
    vl.get(cnt++).set(vl.get(0)).add(vl.get(5)).mult(0.5f);
    vl.get(cnt++).set(vl.get(1)).add(vl.get(2)).mult(0.5f);
    vl.get(cnt++).set(vl.get(3)).add(vl.get(4)).mult(0.5f);

//    if(normalOffs < -EPSILON || normalOffs>EPSILON) {
//      UVertex vn=f.normal().copy().mult(normalOffs);
//      for(int i=0; i<3; i++) vl.get(i+6).add(vn);
//    }
    
    UVertex v1=vl.get(6);
    UVertex v2=vl.get(7);
    UVertex v3=vl.get(8);
    
    if(!hasHole) output.add(new UFace(geo, v1,v2,v3));
    
    
    output.add(new UFace(geo, vf[0],vf[1],v1));
    output.add(new UFace(geo, v1,vf[1],v2));
    output.add(new UFace(geo, vf[1],vf[2],v2));
    output.add(new UFace(geo, v2,vf[2],v3));
    output.add(new UFace(geo, vf[2],vf[0],v3));
    output.add(new UFace(geo, v3,vf[0],v1));

    return output;
  }

  
}
