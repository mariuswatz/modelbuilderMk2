package unlekker.mb2.geo;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import unlekker.mb2.util.*;

public class UVertex extends UMB  {
  public static int globalID;

  public int ID;  
  public float x,y,z;
  
  public int col;
  public float U=UNAN,V=UNAN;
  public UVertex normal;
  
  public UVertex() {
    this(0,0,0);
  }

  public UVertex(float xx,float yy) {
    this(xx,yy,0);
  }

  public UVertex(double xx,double yy,double zz) {
    this((float)xx,(float)yy,(float)zz);
  }

  public UVertex(float xx,float yy,float zz) {
    ID=globalID++;
    
    x=xx;
    y=yy;
    z=zz;
  }
  
  public UVertex(UVertex v) {
    this();
    set(v);
  }

  public UVertex(PVector pv) {
    this(pv.x,pv.y,pv.z);
  }

  public UVertex copy() {
    return new UVertex(this);
  }
  
  public UVertex draw() {
    return (UVertex)pvertex(this);
  }

  public UVertex ptranslate() {
    return (UVertex)ptranslate(this);
  }
  
  public UVertex set(float xx,float yy) {
    x=xx;
    y=yy;
    return this;
  }

  public UVertex set(float xx,float yy,float zz) {
    x=xx;
    y=yy;
    z=zz;
    return this;
  }

  public UVertex set(double xx,double yy,double zz) {
    x=(float)xx;
    y=(float)yy;
    z=(float)zz;
    return this;
  }

  public UVertex set(UVertex v) {
    this.x=v.x; 
    this.y=v.y; 
    this.z=v.z; 
    this.col=v.col;
    this.U=v.U;
    this.V=v.V;
    if(v.normal!=null) {
      if(normal!=null) normal.set(v.normal.x,v.normal.y,v.normal.x);
    }
   return this;
  }

  public UVertex setAngleXY(float angle,float rad) {
    return set(Math.cos(angle),Math.sin(angle),0);
  }

  public static UTask eqTask;
  
  public boolean equals(Object o) {
    long t=0;
    
    if(eqTask!=null) {
      t=System.currentTimeMillis();
//      if(eqTask==null) {
//        eqTask=new UTask("UVertex.equals");
//        eqTask.addData("equals", 0);
//        eqTask.addData("equalsTime", 0);
//      }
          
      eqTask.increment("equals");

//      eqTask.getStackInfo(true);      
//      String s="";
//      for(String tmp:eqTask.stack) s+=tmp+TAB;
//      eqTask.addLogUnique(s);
      
    }
    boolean equals=true;
    
    UVertex v=(UVertex)o;
    if(this==v || v.ID==ID) equals=true;
    else {
      float res=v.x-x;
      if((res<0 ? -res:res)>EPSILON) equals=false;
      else {
        res=v.y-y;
        if((res<0 ? -res:res)>EPSILON) equals=false;
        else {
          res=v.z-z;
          if((res<0 ? -res:res)>EPSILON) equals=false;
        }
      }
    }


    if(eqTask!=null) 
      eqTask.increment("equalsTime",(int)(System.currentTimeMillis()-t));

//    if(dist(v)<EPSILON) return true;
//    res=abs(v.y-y);
//    if(res>EPSILON) return false;
//    res=abs(v.z-z);
//    if(res>EPSILON) return false;
    
//    if(distSimple(v)<EPSILON) return true;
    return equals;
  }


  //////////////////////////////////////////
  // VECTOR MATH

  public UVertex add(float vx,float vy) {x+=vx; y+=vy; return this;}

  public UVertex add(float vx,float vy,float vz) {x+=vx; y+=vy; z+=vz; return this;}

  public UVertex add(UVertex v) {
    x+=v.x; y+=v.y; z+=v.z; 
    return this;
  }
  
  public UVertex sub(float vx,float vy) {x-=vx; y-=vy; return this;}

  public UVertex sub(float vx,float vy,float vz) {x-=vx; y-=vy; z-=vz; return this;}

  public UVertex sub(UVertex v) {
    x-=v.x; y-=v.y; z-=v.z; 
    return this;
  }

  public UVertex mult(float m) {
    x*=m; y*=m; z*=m;
    return this;
  }
  
  public UVertex mult(float vx,float vy) {x*=vx; y*=vy; return this;}

  public UVertex mult(float vx,float vy,float vz) {x*=vx; y*=vy; z*=vz; return this;}

  public UVertex mult(UVertex v) {
    x*=v.x; y*=v.y; z*=v.z; 
    return this;
  }
  
  public UVertex div(float m) {
    return mult(1f/m);
  }


  public static UVertex lerp(float t,UVertex v1,UVertex v2) {
    return v2.copy().sub(v1).mult(t).add(v1);
  }


  /**
   * Returns a new UVertex instance that is the
   * linear interpolation between this vertex and
   * the input <code>v2</code> at the fraction <code>t</code>.  
   * @param t Fraction of interpolated values (normally [0..1])
   * @param v2
   * @return New UVertex instance with the interpolated result
   */
  public UVertex lerp(float t,UVertex v2) {
    return lerp(t,this,v2);
  }

  /**
   * Calculates the linear interpolation between this vertex and
   * the input <code>v2</code> at the fraction <code>t</code>.
   * The value of this UVertex instance is set to the interpolated result. 
   * @param t Fraction of interpolated values (normally [0..1])
   * @param v2
   * @return Reference to this UVertex, updated with the 
   * interpolated result
   */
  public UVertex lerpSelf(float t,UVertex v2) {
    float xx=x,yy=y,zz=z;
    set(v2).sub(xx,yy,zz).mult(t).add(xx,yy,zz);
    return this;
  }

  public UVertex cross(UVertex v){
//    float crossX=y*v.z-v.y*z;
//    float crossY=z*v.x-v.z*x;
//    float crossZ=x*v.y-v.x*y;
//    return(new UVertex(crossX,crossY,crossZ));
    return cross(this,v);
  }

  public static UVertex cross(UVertex v,UVertex v2){
    float crossX=v.y*v2.z-v2.y*v.z;
    float crossY=v.z*v2.x-v2.z*v.x;
    float crossZ=v.x*v2.y-v2.x*v.y;
    return(new UVertex(crossX,crossY,crossZ));
  }

  public float dot(UVertex v) {
    return x*v.x+y*v.y+z*v.z;
  }

  public static float dot(UVertex v,UVertex v2) {
    return v2.x*v.x+v2.y*v.y+v2.z*v.z;
  }

  /**
   * Simplistic calculation of a vector's heading. Based on
   * Euler angles and  
   * @return Representation of rotations required to produce the heading. 
   * code>x</code> is rotation around Z, <code>y</code> 
   * the rotation around the Y axis. Rotations must be 
   * performed in the Z-then-Y order.
   */
 public UVertex heading3D() {
    float a, b;

    UVertex v2=copy();

    // Y rotation
    a=(float)Math.atan2(v2.z, v2.x);
    v2.rotY(-a);
    // Z rotation
    b=(float)Math.atan2(v2.y, v2.x);

    v2.set(b, a, 0);
    return v2;
  }

 public float angle2D(int plane) {
   if(plane==XY) return PApplet.atan2(-y, x);
   if(plane==XZ) return PApplet.atan2(-z, x);
   return PApplet.atan2(-z, y);
 }

 public float angleXY() {
   return PApplet.atan2(-y, x);
 }

 public float angleXY2() {
   return -PApplet.atan2(-y, x);
 }

  public UVertex abs() {
    x=abs(x);
    y=abs(y);
    z=abs(z);
    return this;
  }

  /**
   * Negates x,y,z so that x=-x etc., reversing the direction
   * of the vector.
   * @return
   */
  public UVertex neg() {
    x=-x;
    y=-y;
    z=-z;
    return this;
  }

  /**
   * Returns the XYZ values of this vertex as a PVector instance
   * @return
   */
  public PVector toPVector() {
    return new PVector(x,y,z);
  }

  /**
   * Returns the XYZ values of this vertex as a PVector instance
   * @return
   */
  public static ArrayList<PVector> toPVectorList(UVertex[] vv) {
    ArrayList<PVector> pv=new ArrayList<PVector>();
    for(UVertex vert:vv) if(vert!=null) pv.add(vert.toPVector());
    return pv;
  }

  public static String str(UVertex vv[]) {
    StringBuffer buf=strBufGet();
    
    int id=0,n=vv.length;
    for(UVertex vert:vv) {
      if(id++>0) buf.append(TAB);
      buf.append(vert.str());
    }
    
    return "["+strBufDispose(buf)+"]";
  }
  
  public String str() {
    return toString(2);
  }

  public String str2D() {
    return String.format(FSTRXY,nf(x,2),nf(y,2));
  }

  public UVertex set(String s) {
    try {
      if(s.charAt(0)=='<') s=s.substring(1,s.length()-1);
      String tok[]=getPApplet().split(s, ',');
      if(tok.length>1) set(x,y,parseFloat(tok[2]));
      if(tok.length>0) set(x,parseFloat(tok[1]),z);
      if(tok.length>0) set(parseFloat(tok[0]),y,z);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return this;
  }
  
  public String strData() {
    return toString(4);
  }

  public String strDeg() {
    return String.format("<%s,%s,%s>",
        nf(x*RAD_TO_DEG,1),
        nf(y*RAD_TO_DEG,1),
        nf(z*RAD_TO_DEG,1));  }

  public String toString() {
    return strData();
  }
  public String toString(int prec) {
    return String.format(FSTRXYZ,
        nf(x,prec),nf(y,prec),nf(z,prec));
  }

  public String toStringDegrees(int prec) {
    return String.format("<%s,%s,%s>",
        nf(x*RAD_TO_DEG,prec),
        nf(y*RAD_TO_DEG,prec),
        nf(z*RAD_TO_DEG,prec));
  }

  public boolean closeSq(UVertex vv,float limitSq) {
    return (distSq(vv)<limitSq);
  }

  public boolean close(UVertex vv,float limit) {
    return (dist(vv)<limit);
  }
  
  /**
   * Distance between XYZ of this vertex and XYZ of v1 
   * @param v1
   * @return Distance according to Pythagoras theorem
   */
  public float dist(UVertex v1) {
    return mag(v1.x-x,v1.y-y,v1.z-z);
  }

  /**
   * Calculate the squared distance between XYZ of this vertex and XYZ of v1.
   * Omits the sqrt() step of dist() for efficiency
   * @param v1
   * @return Returns <code>sq(v1.x-x)+sq(v1.y-y)+sq(v1.z-z)</code>
   */
  public float distSq(UVertex v1) {
    float a=(v1.x-x),b=(v1.y-y),c=(v1.z-z);
    return a*a+b*b+c*c;
  }

  /**
   * Simplistic distance calculation, also known as the 
   * <a href="http://en.wiktionary.org/wiki/Manhattan_distance">Manhattan distance</a>.
   * 
   * Useful in many cases where real distance calculation is overkill
   * @param v1
   * @return Absolute value <code>abs(v1.x-x)+abs(v1.y-y)+abs(v1.z-z)</code>
   */
  public float distSimple(UVertex v1) {
    return abs(v1.x-x)+abs(v1.y-y)+abs(v1.z-z);
  }
  
  
  public UVertex norm() {
    float l;

    l=mag();
    if(abs(l)>0) {x/=l; y/=l; z/=l;}
    return this;
   }

  public UVertex norm(float m) {
    return norm().mult(m);
  }

  public static float mag(float xx,float yy,float zz) {
    return (float)Math.sqrt(xx*xx+yy*yy+zz*zz);
  }

  public float mag() {return (float)Math.sqrt(x*x+y*y+z*z);}

  public UVertex delta(UVertex vGoal) {
    return new UVertex(vGoal).sub(this);
  }

  public static UVertex delta(UVertex v1,UVertex v2) {
    return new UVertex(v2).sub(v1);
  }

  public UVertex rotAxis(int axis,float deg) {
    double sindeg,cosdeg;
    double a,b;
    float a2,b2;
    
    sindeg=Math.sin(deg); cosdeg=Math.cos(deg);

    switch (axis) {
      case X: a=y; b=z; break;
      case Y: a=x; b=z; break;
      default:a=x; b=y; break;
    }
    
    a2=(float)(a*cosdeg-b*sindeg);
    b2=(float)(a*sindeg+b*cosdeg);
    
    switch (axis) {
      case X: y=a2; z=b2; break;
      case Y: x=a2; z=b2; break;
      default:x=a2; y=b2; break;
    }
    
    return this;
  }

  public static void rotX(UVertex vv[],float deg) {
    for(UVertex vert:vv) vert.rotX(deg);
  }

  public static void rotY(UVertex vv[],float deg) {
    for(UVertex vert:vv) vert.rotY(deg);
  }

  public static void rotZ(UVertex vv[],float deg) {
    for(UVertex vert:vv) vert.rotZ(deg);
  }

  public static void scale(UVertex vv[],float m) {
    scale(vv,m,m,m);
  }

  public static void scale(UVertex vv[],float mx,float my,float mz) {
    for(UVertex vert:vv) vert.mult(mx,my,mz);
  }
  
  public static void translate(UVertex vv[],UVertex vt) {
    translate(vv,vt.x,vt.y,vt.z);
  }
  
  public static void translate(UVertex vv[],float mx,float my) {
    translate(vv,mx,my,0);
  }
  
  public static void translate(UVertex vv[],float mx,float my,float mz) {
    for(UVertex vert:vv) vert.add(mx,my,mz);    
  }
  
  
  public UVertex rotX(float deg) {
    return rotAxis(X,deg);
  }

  public UVertex rotY(float deg) {
    return rotAxis(Y,deg);
  }

  public UVertex rot(float deg) {
    return rotAxis(Z,deg);
  }

  public UVertex rotZ(float deg) {
    return rotAxis(Z,deg);
  }

  public int compareTo(Object o) {
    UVertex v=(UVertex)o;
    if(equals(v)) return 0;
    
    return (int)((v.x-x)*100f);
  }

  public UVertex setUV(float UU,float VV) {
    U=UU;
    V=VV;
    return this;
  }

  public UVertex setColor(int c) {
    col=c;
    return this;
  }

  public UVertex setColor(int c, int a) {
    setColor(pcolor(c, a));
    return this;
  }

  public UVertex setColor(float a,float b,float c) {
    return setColor(pcolor(a,b,c));
  }

  public static UVertex centroid(UVertex[] v2) {        
    if(v2==null || v2.length==0) return null;

//    UBB bb=new UBB().add(v2).calc();
//    log("centroid "+bb.centroid.str()+" bb "+bb.str());
    UVertex v=new UVertex();
    for(UVertex vv:v2) v.add(vv);
    v.div(v2.length);
    
    if(v2[0].U>UNAN) {
      float U=0;
      float V=0;
      for(UVertex vv:v2) {
        U+=(vv.U>UNAN ? vv.U : 0);
        V+=(vv.V>UNAN ? vv.V : 0);
      }
      
      float nf=1f/(float)v2.length;
      v.setUV(U*nf, V*nf);
    }
    
    return v;//bb.centroid;
  }
  
}
