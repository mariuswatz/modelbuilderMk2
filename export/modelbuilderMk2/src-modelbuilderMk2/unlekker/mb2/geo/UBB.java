/*
 * modelbuilderMk2
 */
package unlekker.mb2.geo;

import java.util.ArrayList;

import unlekker.mb2.util.UMB;

public class UBB extends UMB {
  public UVertex min,max;
  protected UVertex centroid,dim;
  private UVertexList vl;
  private boolean tainted=true;
  
  
  public UBB() {
    centroid=new UVertex();
    dim=new UVertex();
    
    max=new UVertex(
        Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY);
    min=new UVertex(
        Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY);
  }

  public UBB(float x1,float y1,float z1, float x2,float y2,float z2) {
    this();
    add(x1,y1,z1).add(x2,y2,z2);
  }
  
  public UBB(ArrayList<UVertexList> stack) {
    this();
    for(UVertexList l:stack) add(l);
  }

  /**
   * <p>Produces a bounding box that uses <code>yLimit</code> as either its
   * minimum (<code>above==true</code>) or maximum ((<code>above==false</code>) 
   * bounds in the Y axis. In all directions the bounding box extends to
   * near-infinity. (<code>Float.POSITIVE_INFINITY-100</code>)</p>
   * 
   * <p>Useful for defining bounds based on an absolute Y plane, 
   * for instance for using {@link UGeo#getFacesInBB(UBB)} to select
   * faces that lie above or below a given Y value.</p>
   *  
   * @param yLimit Y value defining upper or lower boundary
   * @param above If true, <code>yLimit</code> is used as the minimum Y value,
   * otherwise it becomes the maximum value. 
   * 
   * @return
   */
  public static UBB getPlaneY(float yLimit,boolean above) {
    UBB bb=null;
    float inf=Float.POSITIVE_INFINITY-100;
    
    if(above) bb=new UBB(-inf,yLimit,-inf, inf,inf,inf);
    else bb=new UBB(-inf,-inf,-inf, inf,yLimit,inf);
    
    return bb;
  }

  public UBB add(UGeo geo) {
    for(UVertex vv:geo.getV()) add(vv);
    return this;
  }

  public UBB add(UBB in) {
    return add(in.min).add(in.max);
  }

  public UBB add(UVertexList vl) {
    for(UVertex vv:vl) add(vv);
    return this;
  }

  public UBB add(UVertex v[]) {
    if(v!=null && v.length<1) {
      for(UVertex vv:v) {
        add(vv);
      }
    }
    
    return this;    
  }

  public UBB add(float x,float y,float z) {
    min.set(min(x,min.x),min(y,min.y),min(z,min.z));
    max.set(max(x,max.x),max(y,max.y),max(z,max.z));
    tainted=true;
    
    return this;
  }

  public UBB add(UVertex v) {
    min.set(min(v.x,min.x),min(v.y,min.y),min(v.z,min.z));
    max.set(max(v.x,max.x),max(v.y,max.y),max(v.z,max.z));
    tainted=true;
    
    return this;
  }
  
  public boolean inBB(UVertex vv) {
    if(vv.x<min.x || vv.x>max.x) return false;
    if(vv.y<min.y || vv.y>max.y) return false;
    if(vv.z<min.z || vv.z>max.z) return false;
    return true;
  }
  
  public UVertex centroid() {
    if(tainted) check();
    return centroid;
  }

  public UVertex dim() {
    if(tainted) check();
    return dim;
  }

  public UVertex maxV() {
    if(tainted) check();
    return max;
  }

  public UVertex minV() {
    if(tainted) check();
    return min;
  }

  public float dimX() {
    if(tainted) check();
    return dim.x;
  }
  
  public float dimY() {
    if(tainted) check();
    return dim.y;
  }

  public float dimZ() {
    if(tainted) check();
    return dim.z;
  }

  public float minX() {
    if(tainted) check();
    return min.x;
  }
  
  public float minY() {
    if(tainted) check();
    return min.y;
  }

  public float minZ() {
    if(tainted) check();
    return min.z;
  }

  public float maxX() {
    if(tainted) check();
    return max.x;
  }
  
  public float maxY() {
    if(tainted) check();
    return max.y;
  }

  public float maxZ() {
    if(tainted) check();
    return max.z;
  }
  
  public float dimMax() {
    if(tainted) check();
    return max(dim.x,max(dim.y,dim.z));
  }

  /**
   * Returns the plane (XY / XZ / YZ) that provides the greatest
   * extension for this bounding box.
   * @return Integer constant == XY/XZ/YZ
   */
  public int dimBiggestPlane() {
    if(tainted) check();

    float dimAxis[]=new float[] {
        dimX()*dimY(),
        dimX()*dimZ(),
        dimY()*dimZ()
    };
    
    int biggest=(dimAxis[0]>dimAxis[1] ? 0 : 1);
    biggest=(dimAxis[biggest]>dimAxis[2] ? biggest : 2);
    
//    logf("%.2f %.2f %.2f",dimAxis[0],dimAxis[1],dimAxis[2]);
    if(biggest==XY) log("XY "+str());
    if(biggest==XZ) log("XZ "+str());
    if(biggest==YZ) log("YZ "+str());
    
    return biggest;
    

  }
  public UBB check() {
    if(!tainted) return this;
    
    dim.set(max).sub(min);
    dim.set(dim.abs());
    
    centroid.set(max).add(min).mult(0.5f);
//    log("calc\t"+centroid.str()+" "+str());
    tainted=false;
    
    return this;
  }

  public UBB draw() {
    if(!checkGraphicsSet()) return this;
    if(tainted) check();

    
    if(vl==null) vl=new UVertexList();
    if(vl.size()<1) {
      vl.add(min.x,min.y,min.z);
      vl.add(min.x,max.y,min.z);
      vl.add(min.x,max.y,max.z);
      vl.add(min.x,min.y,max.z);
      vl.add(min.x,min.y,min.z);
      
      vl.add(max.x,min.y,min.z);
      vl.add(max.x,max.y,min.z);
      vl.add(max.x,max.y,max.z);
      vl.add(max.x,min.y,max.z);
      vl.add(max.x,min.y,min.z);
      
      // centroid and "crosshairs"
      vl.add(centroid);
      vl.add(centroid.copy().add(-20,0,0));
      vl.add(centroid.copy().add(20,0,0));
      vl.add(centroid.copy().add(0,-20,0));
      vl.add(centroid.copy().add(0,20,0));
    }
    
    if(isGraphics3D) {
      g.beginShape(QUAD_STRIP);
      for(int i=0; i<5; i++) {
        pvertex(vl.get(i)).pvertex(vl.get(i+5));
      }
      g.endShape();
    }
    else {
      g.beginShape();
      pvertex(vl.get(0)).pvertex(vl.get(5));
      pvertex(vl.get(6)).pvertex(vl.get(1));
      pvertex(vl.get(0));
      g.endShape();
    }
    
    ppush().ptranslate(centroid);
    g.beginShape(LINES);
    pline(vl.get(11), vl.get(12));
    pline(vl.get(13), vl.get(14));
    g.endShape();

    ppop();
    
    return this;
  }
  
  public UBB clear() {
    centroid.set(0,0,0);
    max.set(
        Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY);
    min.set(
        Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY);
    dim.set(0,0,0);
    if(vl!=null) vl.clear();
    
    return this;
  }
  
  public String str() {
    StringBuffer buf=strBufGet();
    buf.append("[BB dim=").append(dim.str()).
      append(SPACE).append(min.str()).
      append(SPACE).append(max.str()).append(']');
    
    return strBufDispose(buf);
  }
}
