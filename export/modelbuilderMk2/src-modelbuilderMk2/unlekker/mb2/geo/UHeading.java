package unlekker.mb2.geo;

import java.util.ArrayList;

import org.apache.commons.math.geometry.CardanEulerSingularityException;
import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.RotationOrder;
import org.apache.commons.math.geometry.Vector3D;

import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;

/**
 * Represents a 3D heading vector, providing methods to apply that heading to vertex data. 
 * Typical uses include aligning a vertex list with an arbitrary axis or "sweeping" a 2D profile
 * along a 3D path. Headings are calculated relative to the base vector <0,0,1>.  
 * 
 * This code uses an extract of the Apache Commons Mathematics Library, specifically the
 * <code>org.apache.commons.math.geometry</code> package and its Rotation class.
 * {@link http://commons.apache.org/proper/commons-math/}
 * 
 * @author marius
 *
 */
public class UHeading extends UMB {
  public Rotation rot;  
  public UVertex dir;
  public boolean is2D;
  public float angle2D;
  
  public UHeading(UVertex thedir,boolean is2D) {
    this.is2D=is2D;
    initHeading(thedir);
  }
  
  public UHeading(UVertex thedir) {
    initHeading(thedir);
  }

  private void initHeading(UVertex thedir) {
    this.dir=thedir.copy().norm();
    if(is2D) {
      angle2D=dir.angleXY();
      return;
    }
    
    Vector3D v=toVector(dir);
//    System.out.println(v.getX()+" "+v.getY()+" "+v.getZ());
    rot=new Rotation(v,new Vector3D(0,0,1));//-UConst.HALF_PI);
//    rot=new Rotation(order,dir.x,dir.y,dir.z);
  }

  private Vector3D toVector(UVertex vv) {
    return new Vector3D(vv.x, vv.y,vv.z);
  }

  public static ArrayList<UVertexList> sweep(UVertexList path,UVertexList prof) {
    return sweep(path, prof, 0.2f);
  }

  /**
   * Using <code>path</code> as a motion path and <code>prof</code> as the outline of
   * a sweep geometry, this function calculates the heading transforms for all vertices in 
   * <code>path</code> and places correctly aligned copies of <code>prof</code> at each position.
   *  
   * @param path
   * @param prof vertex list defining outline shape to be swept along path, assumed to lie in XY plane and 
   * facing along positive Z axis.
   * @param headingDamper damping factor applied to heading vectors, a suggested default would be 0.2f;
   * @return
   */
  public static ArrayList<UVertexList> sweep(UVertexList path,UVertexList prof,float headingDamper) {
    ArrayList<UVertexList> res=new ArrayList<UVertexList>();
    ArrayList<UHeading> h;
    
//    ArrayList<UHeading> h=getHeadings(path,headingDamper);
//    if(rndBool()) {
////      log("getHeadings2");
////      h=getHeadings2(path);
//    }
    h=getHeadings(path);
    int n=path.size();
    
    for(int i=0; i<n; i++) {
      UVertexList tmp=h.get(i).align(prof.copy());
      res.add(tmp.translate(path.get(i)));
    }
    
    return res;
  }
  
  public static ArrayList<UVertexList> sweep3(UVertexList path,UVertexList prof) {
    ArrayList<UVertexList> res=new ArrayList<UVertexList>();
    ArrayList<UHeading> h;
    
//    ArrayList<UHeading> h=getHeadings(path,headingDamper);
//    if(rndBool()) {
////      log("getHeadings2");
////      h=getHeadings2(path);
//    }
    h=getHeadings3(path);
    int n=path.size();
    
    for(int i=0; i<n; i++) {
      UVertexList tmp=h.get(i).align(prof.copy());
      res.add(tmp.translate(path.get(i)));
    }
    
    return res;
  }

  public static ArrayList<UHeading> getHeadings2D(UVertexList input) {
    ArrayList<UHeading> h=new ArrayList<UHeading>();
    
    
    
    return h;

  }

  public UVertex getAngles() {
    double[] a=null;
    try {
      a=rot.getAngles(RotationOrder.XYX);
    } catch (CardanEulerSingularityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }    
    return new UVertex(a[0],a[1],a[2]);
  }
  
  public static ArrayList<UHeading> getHeadings(UVertexList input) {
    ArrayList<UHeading> h=new ArrayList<UHeading>();
    UVertexList delta=deltaVectors2(input);
    for(UVertex hv:delta) h.add(new UHeading(hv));
    return h;
  }

  public static ArrayList<UHeading> getHeadings3(UVertexList input) {
    ArrayList<UHeading> h=new ArrayList<UHeading>();
    UVertexList delta=deltaVectors3(input);
    for(UVertex hv:delta) h.add(new UHeading(hv));
    return h;
  }

//  public static ArrayList<UHeading> getHeadings(UVertexList input,float headingDamper) {
//    ArrayList<UHeading> h=new ArrayList<UHeading>();
//    UVertexList delta=input.deltaVectors(headingDamper);
//    if(rndBool()) delta=deltaVectors2(input);
//    for(UVertex hv:delta) h.add(new UHeading(hv));
//    return h;
//  }
//
  protected static UVertexList deltaVectors2(UVertexList input) {
    UVertexList dl=new UVertexList();
    for(int i=0; i<input.size(); i++) {
      UVertex tmp=null;
      if(i<input.size()-1) {
        tmp=input.get(i+1).copy().sub(input.get(i));
        if(i>0) tmp.add(input.get(i).copy().sub(input.get(i-1))).mult(0.5f);
      }
      else tmp=input.get(i).copy().sub(input.get(i-1));
      dl.add(tmp);
    }
    
    return dl;
  }

  protected static UVertexList deltaVectors3(UVertexList input) {
    UVertexList dl=new UVertexList();
    
    boolean isClosed=input.isClosed();
    
    for(int i=0; i<input.size(); i++) {
      UVertex vv=null,vn=null,vp=null;
      vv=input.get(i);
      
      if(i>0) vp=input.get(i-1);
      if(i==0 && isClosed) vp=input.last();

      if(i<input.size()-1) vn=input.get(i+1);
      else if(isClosed) vn=input.first();
      
      if(vn!=null) vn=vn.copy().sub(vv);//.norm();
      if(vp!=null) vp=vv.copy().sub(vp);//.norm();
      
      if(vp!=null && vn!=null) vv=vn.add(vp).mult(0.5f);
      else if(vn==null) vv=vp;
      else vv=vn;
      
//      log(i+"/"+input.size()+" "+
//          isClosed+" vp="+(vp!=null)+
//          " vn="+(vn!=null)+" "+vv.str());
      
      dl.add(vv);
      
//      if(i<input.size()-1) {
//        tmp=input.get(i+1).copy().sub(input.get(i)).norm();
//        if(i>0) tmp.add(
//            input.get(i).copy().sub(input.get(i-1)).norm()).mult(0.5f);
//      }
//      else tmp=input.get(i).copy().sub(input.get(i-1));
//      dl.add(tmp);
    }
    
    return dl;
  }

  public UVertex[] align(UVertex vv[]) {
    for(UVertex tmp:vv) align(tmp);
    return vv;
  }
  
  public UVertex align(UVertex vv) {
    if(is2D) {
      return vv.rotZ(angle2D);
    }
    
    Vector3D v=toVector(vv);
    v=rot.applyInverseTo(v);
    return vv.set(v.getX(),v.getY(),v.getZ());
  }

  public UVertex alignInverse(UVertex vv) {
    if(is2D) {
      return vv.rotZ(angle2D);
    }
    
    Vector3D v=toVector(vv);
    v=rot.applyTo(v);
    return vv.set(v.getX(),v.getY(),v.getZ());
  }

  public UVertexList align(UVertexList vl) {
    Vector3D v;
    for(UVertex vv:vl) {
      align(vv);
      
    }
    return vl;
  }

  public UGeo align(UGeo geo) {
    align(geo.getV());
    return geo;
  }
  
  public static UGeo align(UGeo geo,UVertex v1,UVertex v2) {
    return align(geo,v1,v2,-1);
  }


  /**
   * Aligns and scales a UGeo instance so that the result geometry lies along the
   * vector given by the input vertices. <code>geo</code> is assumed to be facing 
   * "forward" along the positive Z axis and will be scaled along that axis to match
   * the length of the input vector, leaving the X and Y dimensions unchanged.
   * @param geo
   * @param v1
   * @param v2
   * @return
   */
  public static UGeo align(UGeo geo,UVertex v1,UVertex v2,float extend) {
    UVertex dir=v2.copy().sub(v1);
    UHeading h=new UHeading(dir);
    
//    UMB.log(dir.mag()+" "+geo.dimZ());
    float m=dir.mag();
    if(extend>0) m+=extend*2;
    
    geo.center().scale(1,1,m/geo.dimZ());
        
    h.align(geo.getV());
    geo.translate(dir.mult(0.5f).add(v1));
    return geo;
  }

  public static UGeo boxVector(UVertex v1,UVertex v2,float thickness,float extend) {
    return align(UGeo.box(thickness),v1,v2,extend);
  }

  public static UGeo boxVector(UVertex v1,UVertex v2,float thickness) {
    return boxVector(v1, v2, thickness, -1);
  }

  
  public static UGeo cylVector(UVertex v1,UVertex v2,float thickness,int steps) {
    return cylVector(v1, v2, thickness, -1,steps);
  }

  public static UGeo cylVector(UVertex v1,UVertex v2,float thickness,float extend,int steps) {
    return align(UGeo.cyl(thickness, 100, steps).rotX(HALF_PI),v1,v2,extend);
  }

}
 