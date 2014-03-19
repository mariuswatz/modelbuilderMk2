package unlekker.mb2.test;

import java.util.ArrayList;

import org.apache.commons.math.geometry.CardanEulerSingularityException;
import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.RotationOrder;
import org.apache.commons.math.geometry.Vector3D;

import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;

public class Heading {
  Rotation rot;
  
  UVertex dir,angles;
  public RotationOrder order;
  public String rotOrderName;
  
  public static ArrayList<RotationOrder> rotOrders;
  static {
    rotOrders=new ArrayList<RotationOrder>();
    rotOrders.add(RotationOrder.ZXZ);
    rotOrders.add(RotationOrder.XYX);
    rotOrders.add(RotationOrder.XYZ);
    rotOrders.add(RotationOrder.XZX);
    rotOrders.add(RotationOrder.XZY);
    rotOrders.add(RotationOrder.YXY);
    rotOrders.add(RotationOrder.YZY);
    rotOrders.add(RotationOrder.ZXY);
    rotOrders.add(RotationOrder.ZYX);
    rotOrders.add(RotationOrder.ZYZ);
  }
  
  public Heading(UVertex thedir) {
    this(thedir,rotOrders.get(0));
  }
  
  public Heading(UVertex thedir,RotationOrder ord) {
    order=ord;
    rotOrderName=order.toString();
    
    this.dir=thedir.copy().norm();
    Vector3D v=toVector(dir);
//    System.out.println(v.getX()+" "+v.getY()+" "+v.getZ());
    rot=new Rotation(v,new Vector3D(0,0,1));//-UConst.HALF_PI);
//    rot=new Rotation(order,dir.x,dir.y,dir.z);
    
    
    try {
      double [] a=rot.getAngles(order);
      angles=new UVertex(-a[0],-a[1],-a[2]);
      angles=new UVertex(a[0],a[1],a[2]);
    } catch (CardanEulerSingularityException e) {

      e.printStackTrace();
    } 
    
  }

  private Vector3D toVector(UVertex vv) {
    return new Vector3D(vv.x, vv.y,vv.z);
  }
  
  public UVertex applyInverse(UVertex vv) {
    Vector3D v=toVector(vv);
    v=rot.applyInverseTo(v);
    return vv.set(v.getX(),v.getY(),v.getZ());
  }

  public UVertexList applyInverse(UVertexList vl) {
    Vector3D v;
    for(UVertex vv:vl) {
      applyInverse(vv);
      
    }
    return vl;
  }

}
 