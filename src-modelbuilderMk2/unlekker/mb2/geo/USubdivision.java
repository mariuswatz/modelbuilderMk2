/*
 * modelbuilderMk2
 */
package unlekker.mb2.geo;

import java.util.ArrayList;
import java.util.Arrays;

import unlekker.mb2.util.UMB;


public class USubdivision extends UMB  {

  /**
   * Subdivides a single face according to the specified strategy. 
   * @param f
   * @param type Type of subdivision (currently SUBDIVCENTROID or SUBDIVMIDEDGES) 
   * @return
   */
  public static ArrayList<UFace> subdivide(UFace f,int type) {
    return subdivide(f, type, null);
  }
  
  /**
  /**
   * Subdivides a single face according to the specified strategy. New faces
   * are added to a <code>ArrayList<UFace></code> instance, if <code>output</code> 
   * contains a valid instance then that will be used and returned.
   * @param f
   * @param type Type of subdivision (currently SUBDIVCENTROID or SUBDIVMIDEDGES) 
   * @param output Optional existing <code>ArrayList</code>, if null a new instance is created
   * @return
   */
  public static ArrayList<UFace> subdivide(UFace f,int type,ArrayList<UFace> output) {
    if(output==null) output=new ArrayList<UFace>();
    
    UGeo geo=f.parent;
    UVertex vv[]=f.getV();      
    
    if(type==SUBDIVCENTROID) {
      f.reset();
      UVertex c=f.centroid();
      
      output.add(new UFace(geo, vv[0], vv[1], c));
      output.add(new UFace(geo, vv[1], vv[2], c));
      output.add(new UFace(geo, vv[2], vv[0], c));
    }
    else if(type==SUBDIVMIDEDGES) {
      UVertex mid[]=f.getMidEdges();
      output.add(new UFace(geo, vv[0], mid[0], mid[2]));
      output.add(new UFace(geo, mid[0], vv[1], mid[1]));
      output.add(new UFace(geo, mid[2], mid[0],mid[1]));
      output.add(new UFace(geo, mid[1], vv[2],mid[2]));      
    }
    
    return output;    
  }

  public static ArrayList<UFace> subdivide(ArrayList<UFace> input,int type) {
    ArrayList<UFace> subf=new ArrayList<UFace>();

    for(UFace ff:input) subf=subdivide(ff, type,subf);
    return subf;
  }

  
  public static UGeo subdivide(UGeo geo,int type) {
    ArrayList<UFace> newf=new ArrayList<UFace>();
    
    for(UFace ff:geo.getF()) {
      ArrayList<UFace> l=subdivide(ff, type);
      for(UFace nf:l) {
        newf.add(nf);
//        log(str(nf.vID));
      }
    }
    
//    log(geo.sizeF()+" newf "+newf.size());
    geo.getF().clear();
    
    geo.addFace(newf);
//    log(geo.str());
    return geo;
  }
}
