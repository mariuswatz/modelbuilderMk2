package unlekker.mb2.geo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import processing.core.PVector;
import processing.opengl.*;
import unlekker.mb2.util.UMB;
import unlekker.mb2.util.UTask;

/**
 * 
 * Auto-growing list of UVertex objects that are internally stored as an <code>ArrayList<UVertex></code>.. 
 * The default behavior is to add vertices by copy rather than reference, without checking for duplicate vertices. 
 * This is convenient for most cases but can cause problems for per-vertex transformations and STL output.
 * 
 * Adding by reference can be activated by calling <code>enable(NOCOPY);</code>, duplicate checking is activated by
 * <code>enable(NODUPL);</code> 
 * 
 * TODO
 * - NODUPL / NOCOPY is not consistently implemented.
 * 
 * @author marius
 *
 */
public class UVertexList extends UMB implements Iterable<UVertex> {
  public ArrayList<UVertex> v;
  public int options;
  public UBB bb;

  
  public UVertexList() {
    v=new ArrayList<UVertex>();
    disable(NOCOPY);
    disable(NODUPL);
  }

  public UVertexList(int options) {
    this();
    this.options=options;
  }
  
//  public UVertexList setOptions(int opt) {
//    UMB.setOptions(opt);
//    return this;
//  }
//
  
  public UVertexList copy() {
    UVertexList cvl=new UVertexList();
    cvl.setOptions(options);
    
    for(UVertex vv:v) cvl.add(vv);
    if(isClosed()) { // handle a closed list
//      log(cvl.first().ID+" "+cvl.first().str()+
//          " "+cvl.last().ID+" "+cvl.last().str());
      cvl.remove(cvl.size()-1); // remove duplicate vertex
      cvl.close();
    }
    return cvl;
  }
  
  public UVertexList set(UVertexList vl) {
    clear();
    return add(vl);
  }

  public UVertexList copyNoDupl() {
    UVertexList cvl=new UVertexList();
    cvl.enable(NODUPL);
    for(UVertex vv:v) cvl.add(vv);
    return cvl;
  }
  
  
  public UVertexList setOptions(int opt) {
    options=opt;
//    log(optionStr());
    return this;
  }

  public UVertexList enable(int opt) {
    options=options|opt;
    return this;
  }

  // Sorting the ArrayList
  /**
   * <p>Returns a new UVertexList containing the UVertex instances
   * from this list, sorted by the angle between each vertex and the centroid.
   * The result should be a valid polygon. The sorting is done in 2D in the XY
   * plane.</p>  
   * 
   * <p>Code based on solution by Daniel Shiffman, http://www.shiffman.net.</p>
   *
   * @return
   */
  public UVertexList sortAsPolygon() {
    UVertexList vl=new UVertexList();//.enable(NODUPL);
    
    UVertex c=centroid(),tmp=new UVertex();
    
    ArrayList<Float> a=new ArrayList<Float>();
    for(UVertex vv:v) {
      tmp.set(vv).sub(c);
      a.add(tmp.angleXY()+PI);
    }
    
    log(min(a)*RAD_TO_DEG+" "+max(a)*RAD_TO_DEG);
    
    int n=a.size();
    
    // As long as it's not empty
    while (n>0) {
      // Let's find the one with the highest angle
      float biggestAngle = UNAN;
      
      int cnt=0,id=0;
      for(float f:a) {
        if(f>biggestAngle || biggestAngle==UNAN) {
          biggestAngle=f;
          id=cnt;
        }
        cnt++;
      }
      
      vl.add(v.get(id));
      a.set(id, -1000f);
      n=0; 
      for(float f:a) n+=(f>-0.5f ? 1 : 0);
//      log("n "+n+" "+id+" "+nf(biggestAngle*RAD_TO_DEG));
    }
    
    if(isClosed()) vl.close();
    
    return vl;
  }


  /**
   * <p>Determines the winding order of this vertex list by calculating the area of the polygon
   * it represents.Only works in the XY plane. The area calculation is <code>Area = Area + (X2 - X1) * (Y2 + Y1) / 2)</code>.</p>
   * 
   *  <p>Code by Jonathan Cooper, found at <a href="http://forums.esri.com/Thread.asp?c=2&f=1718&t=174277#513372">
   *  http://forums.esri.com/Thread.asp?c=2&f=1718&t=174277#513372</a>.</p>
   * @return
   */
   public boolean isClockwise(){
     if(size()<1) return true;
     
     UVertex pt1 = first();
     UVertex firstPt = pt1;
     UVertex lastPt = null;
     double area = 0.0;
     for(int i=1; i<size(); i++) {
       
       UVertex pt2 = v.get(i);
       area += (((pt2.x - pt1.x) * (pt2.y + pt1.y)) / 2);
       pt1 = pt2;
       lastPt = pt1;
     }
     area += (((firstPt.x - lastPt.x) * (firstPt.y + lastPt.y)) / 2);
     return area < 0;
   }
 
  /**
   * Produces a new UVertexList containing the delta vector for each position in this
   * list, so that for a given index==[0..n-2] the delta equals <code>get(index+1).copy().sub(get(index));</code>
   * For <code>index==n-1</code> the delta is the same as for <code>index==n-2</code>.
   * @return
   */
  public UVertexList calcDelta() {
    return calcDelta(-1);
  }

  /**
   * Produces a new UVertexList containing the delta vector for each position in this
   * list, so that for a given index==[0..n-2] the delta equals <code>get(index+1).copy().sub(get(index));</code>
   * <p>For <code>index==n-1</code> the delta is the same as for <code>index==n-2</code>. The damping parameters
   * causes damping between the previous and current delta, so that the previous value is weighted by
   * <code>(1.0-damping)</code> and the new value by <code>damping</code>. This is useful to "slow down" changes
   * in the heading along a path.</p>  
   * @param damping
   * @return
   */
  public UVertexList calcDelta(float damping) {
    UVertexList dl=new UVertexList();
    for(int i=0; i<size(); i++) {
      if(i<size()-1) dl.add(get(i+1).copy().sub(get(i)));
      else dl.add(dl.last().copy());
    }
    
    if(damping>0) {
      damping=damping>1 ? 1:1;
      UVertex last=null;
      for(UVertex vv:dl) {
        if(last!=null) vv.mult(damping).add(last.copy().mult(1-damping));
        last=vv;
      }
    }
    
    return dl;
  }


  /**
   * Returns a new UVertexList that resamples the vertex data of 
   * this list to a specified number of vertices. Resampling is 
   * currently done by simple interpolation, using {@see #point(float)}. 
   * @param n Number of points in the new list.
   * @return
   */
  public UVertexList resample(int n) {
    UVertexList cvl=new UVertexList();
    for(int i=0; i<n; i++)
      cvl.add(point(map(i,0,n-1,0,1)));
    return cvl;
  }

  /**
   * Extracts a cross section of an ArrayList<UVertexList>, creating
   * a new list that contains vertices extracted from the ArrayList
   * at the position indicated by <code>index</code>.     
   * @param index Position of the vertices to extract 
   * @param stack List of vertex lists representing a stack
   * @return
   */
  public static UVertexList crossSection(int index,ArrayList<UVertexList> stack) {
    UVertexList vl=(UVertexList)new UVertexList().setOptions(NOCOPY);
    
    for(UVertexList l:stack) vl.add(l.get(index));
    return vl;
  }
  
  /**
   * <p>Smooths a list of vertices, with the number of smoothing iterations
   * specified by <code>smoothLevel</code>.</p>
   * 
   * <p>The algorithm consists of
   * taking line segments (v1,v2) from the input vertex list, calculating 
   * two new vertices interpolated at 33% and 66% (respectively, 
   * <code>v1.lerp(0.33f,v2)</code> and <code>v1.lerp(0.66f,v2)</code> along each segment. 
   * The new points are added to a new list, omitting the original vertices.</p> 
   * 
   * <p>If the input list is closed (<code>UVertexList.isClosed()==true</code>),
   * the first and last segments are handled so that the interpolation "wraps",
   * if not the original first and last vertices are added to the new list.</p>
   *   
   * @param in
   * @param smoothLevel
   * @return
   */
  public static UVertexList smooth(UVertexList in, int smoothLevel) {
    float perc=0.25f;
    UVertexList inOld=in,out= null;
    
    boolean isClosed=in.isClosed();
    
    for(int j=0; j<smoothLevel; j++) {
      out=new UVertexList();

      int nn=(isClosed ? in.size()-1 : in.size()-1);
      for(int i=0; i<nn; i++) {        
        int i2=(i+1);//%in.size();
        UVertex v1=UVertex.lerp(perc,in.get(i),in.get(i2));
        UVertex v2=UVertex.lerp(1-perc,in.get(i),in.get(i2));
        
        out.add(v1).add(v2);
      } 
      if(!isClosed) {
        out.insert(0,in.first());
        out.add(in.last());
      }
      else {
        
        out.close();
      }
      
      log(j+"/"+smoothLevel+" | "+out.size()+" "+isClosed);
      in=out;
    }

    log("smooth "+inOld.size()+" > "+ out.size());
    
    return out;
   }
  
  public static ArrayList<UVertexList> smooth(ArrayList<UVertexList> in,int smoothLevel) {
    while(smoothLevel-->0) {
      in=smooth(in);
    }
    return in;
  }
  
  public static ArrayList<UVertexList> smooth(ArrayList<UVertexList> in) {
    int n=in.size();
    float perc=0.25f;
    
    if(n<3) return in;
    
    ArrayList<UVertexList> out=new ArrayList<UVertexList>();
    out.add(in.get(0));
    
    for(int i=0; i<n-1; i++) {
      UVertexList vl1=in.get(i);      
      UVertexList vl2=in.get(i+1);      
      UVertexList res1=new UVertexList();      
      UVertexList res2=new UVertexList();      
      
      int cnt=0;
      for(UVertex v1 : vl1) {
        UVertex v2=vl2.get(cnt++);
        res1.add(v1.lerp(perc, v2));
        res2.add(v1.lerp(1-perc, v2));
      }
      
//      out.add(in.get(i));
      out.add(res1);
      out.add(res2);
    }
    
    out.add(in.get(in.size()-1));
    return out;
  }

  public UVertex point(float t) {
    if(t<EPSILON) return v.get(0);
    if(t>1-EPSILON) return last();
    
    t*=(float)size();
    int id=(int)Math.floor(t);
    t=t-(float)id;
    
    return UVertex.lerp(t, v.get(id), v.get(id+1));
  }
  
  /**
   * Returns vertex data as array of <code>UVertex</code> instances.
   * @return vertices stored as <code>UVertex[]</code> array.
   */
  public UVertex[] toArray() {
    UVertex[] vv=v.toArray(new UVertex[size()]);
    return vv;
  }

  /**
   * Returns vertex data as an ArrayList<PVector>. 
   * @return ArrayList of vertices converted to PVector
   */
  public ArrayList<PVector> toPVectorList() {
    ArrayList<PVector> pv=new ArrayList<PVector>();
    for(UVertex vv:v) pv.add(vv.toPVector());
    return pv;
  }
  
  public static UVertexList lerp(float t,UVertexList l1,UVertexList l2) {
    UVertexList res=new UVertexList();
    
    int id=0;
    for(UVertex v1:l1) {
      res.add(v1.lerp(t, l2.get(id++)));
    }
    
    return res;
  }


  public static UVertexList line(float w,int steps) {
    UVertexList cl=new UVertexList();
    for(int i=0; i<steps; i++) {
      cl.add(map(i,0,steps-1,-0.5f,0.5f)*w,0,0);
    }
    return cl;
  }

  public static UVertexList line(UVertex v1,UVertex v2,int steps) {
    UVertexList cl=new UVertexList();    
    for(int i=0; i<steps; i++) {
      float t=map(i,0,steps-1,0,1);
      UVertex vv=UVertex.lerp(t, v1, v2);
      cl.add(vv);
    }
    return cl;
  }

  public static ArrayList<UVertexList> grid2D(int nx,int ny,float w,float h) {
    ArrayList<UVertexList> res=new ArrayList<UVertexList>();

    UVertexList tmp=line(w,nx);
    for(int i=0; i<ny; i++) {
      res.add(tmp.copy().translate(0,map(i,0,ny-1,0,h)));
    }

    return center(res);
  }

  

  public static UVertexList circle(float w,int nn) {
    UVertexList cl=new UVertexList();
    for(int i=0; i<nn; i++) {
      float t=map(i,0,nn,0,TWO_PI);
      cl.add(new UVertex(w,0,0).rotZ(t));
    }
    return cl.close();
  }

  public static UVertexList arc(float w,float a,float b,int nn) {
    UVertexList cl=new UVertexList();
    for(int i=0; i<nn; i++) {
      float t=map(i,0,nn-1,a,b);
      cl.add(new UVertex(w,0,0).rotZ(t));
    }
    return cl;
  }

  
  public static UVertexList rect(float w,float h) {
    return new UVertexList().
        add(0,0).add(1,0).
        add(1,1).add(0,1).center().scale(w,h,1).close();
  }
  
  /**
   * Generates an ArrayList containing a series of <code>n</code> vertex lists that
   * are linear interpolations of <code>l1</code> and <code>l2</code>. 
   *  
   * @param n Number of interpolations to generate
   * @param l1
   * @param l2
   * @return ArrayList of interpolated vertex lists
   */
  public static ArrayList<UVertexList> lerpSeries(int n,UVertexList l1,UVertexList l2) {
    ArrayList<UVertexList> l=new ArrayList<UVertexList>();
    
    for(int i=0; i<n; i++) {
      float t=map(i,0,n-1,0,1);
      l.add(UVertexList.lerp(t, l1, l2));
    }
    
    return l;
  }

  public UVertexList draw() {
    if(checkGraphicsSet()) {
      g.beginShape();
      if(isGraphics3D) {
        for(UVertex vv:v) g3d.vertex(vv.x,vv.y,vv.z);        
      }
      else {
        for(UVertex vv:v) g.vertex(vv.x,vv.y);
      }
      g.endShape();
    }
    return this;

  }

  public UBB bb() {
    return bb(false);
  }

  public UBB bb(boolean force) {
    if(bb==null) bb=new UBB();
    else {
      if(!force) return bb;
      
      bb.clear();
    }
    
    for(UVertex vv:v) bb.add(vv);
    
    return bb.check();
  }
  
  /**
   * Returns UVertex instance where x,y,z represent the
   * size of this mesh in the X,Y,Z dimensions.  
   * @return
   */
  public UVertex dimensions() {
    return bb().dim;
  }

  /**
   * Returns UVertex instance where x,y,z represent the
   * size of this mesh in the X,Y,Z dimensions.  
   * @return
   */
  public UVertex dim() {
    return bb().dim();
  }

  /**
   * Reorders the list so that whichever vertex is closest to 
   * <code>vv</code> becomes the first point in the list.
   * @param vv
   * @return
   */
  public UVertexList reorderToPoint(UVertex vv) {
    int closest=closestID(vv);
    
    shiftOrder(-closest);
    
    return this;
  }

  public UVertexList reorderToAngle(float a,int plane) {
    float minD=Float.MAX_VALUE;
    int closest=-1,cnt=0;
    
    cnt=isClosed() ? v.size()-1 : v.size();
    
    for(int i=0; i<cnt; i++) {
      float cmp=abs(a-v.get(i).angle2D(plane));
      if(cmp<minD) {
        minD=cmp;
        closest=i;
      }
    }
    
    
    shiftOrder(-closest);
    
    return this;
  }

  
  public UVertexList shiftOrder(int shift) {
    ArrayList<UVertex> tmp=new ArrayList<UVertex>();
    boolean wasClosed=isClosed();
    if(wasClosed) unclose();
    
    if(shift<0) {
      shift=-shift;
      while((shift--)>0) {
        tmp.add(first());
        v.remove(0);
      }
      for(UVertex vt:tmp) v.add(vt);
    }
    else {
      while((shift--)>0) {
        tmp.add(last());
        v.remove(size()-1);
      }
      for(UVertex vt:tmp) v.add(vt);
    }
    
    if(wasClosed) close();

    return this;
  }
  
  public UVertex closest(UVertex pt) {
    return get(closestID(pt));
  }

  public int closestID(UVertex pt) {
    int closest=-1;
    float minDist=Float.MAX_VALUE;
    
    int cnt=0;
    for(UVertex tmp:v) {
      float d=tmp.distSq(pt);
      if(d<minDist) {
        closest=cnt;
        minDist=d;
      }
      cnt++;
    }

    return closest;
  }

  
  /**
   * Returns new UVertexList containing a list of the delta vectors for each vertex in the
   * current list (calculated by <code>deltaN=this.get(N+1).sub(this.get(N))</code> etc.) Since
   * there is no valid delta vector for the last position in the list, a copy of the delta vector for the
   * penultimate position is given instead.
   * @return
   */
  public UVertexList deltaVectors() {
    return deltaVectors(-1);
  }

  /**
   * Returns new UVertexList containing a list of the delta vectors for each vertex in the
   * current list. Values are dampened (interpolated) with the value of the previous vertex as specified
   * by the <code>damper</code> parameter, providing control of the rate of change. The calculation
   * is given by <code>deltaN=this.get(N+1).sub(this.get(N)).mult(damper).add(deltaPrev.copy().mult(1-damper));</code>. 
   * 
   * @param damper
   * @return
   */
  public UVertexList deltaVectors(float damper) {
    UVertexList dl=new UVertexList();
    for(int i=0; i<size(); i++) {
      if(i<size()-1) dl.add(get(i+1).copy().sub(get(i)));
      else dl.add(dl.last().copy());
    }

    if(damper>0) {
      damper=constrain(damper, 0, 1);
      UVertex last=null;
      for(UVertex vv:dl) {
        if(last!=null) vv.mult(damper).add(last.copy().mult(1-damper));
        last=vv;
      }
    }
    
    return dl;
  }

  public float dimX() {return dim().x;}
  public float dimY() {return dim().y;}
  public float dimZ() {return dim().z;}
  public float dimMax() {return bb().dimMax();}

  public UVertex centroid() {
    bb();
    UVertex cv=bb.centroid();
    
    if(v.get(0).U>UNAN) {
      float U=0;
      float V=0;
      for(UVertex vv:v) {
        U+=(vv.U>UNAN ? vv.U : 0);
        V+=(vv.V>UNAN ? vv.V : 0);
      }
      
      float nf=1f/(float)size();
      cv.setUV(U*nf, V*nf);
    }

    return cv;
  }
  
  /** 
   * Translates vertices so that their centroid lies at
   * the origin.
   */
  public UVertexList center() {
    translateNeg(centroid());
    return this;
  }

  public static ArrayList<UVertexList> scale(ArrayList<UVertexList> stack,float m) {
    return scale(stack,m,m,m);
  }

  public static ArrayList<UVertexList> scale(ArrayList<UVertexList> stack,float x,float y,float z) {
    for(UVertexList l:stack) l.scale(x,y,z);
    
    return stack;
  }

  public static ArrayList<UVertexList> translate(ArrayList<UVertexList> stack, UVertex vv) {
    return translate(stack,vv.x,vv.y,vv.z);
  }
  
  public static ArrayList<UVertexList> translate(ArrayList<UVertexList> stack,float x,float y,float z) {
    for(UVertexList l:stack) l.translate(x,y,z);
    
    return stack;
  }


  public static ArrayList<UVertexList> center(ArrayList<UVertexList> stack) {
    UVertex c=new UVertex();
    for(UVertexList l:stack) c.add(l.centroid());    
    c.div(stack.size());
    
    for(UVertexList l:stack) l.translateNeg(c);
    
    return stack;
  }

  /**
   * Translates vertices so that their centroid lies at
   * at the provided point in space.
   * @param v1 Point where mesh will be centered
   * @return
   */
  public UVertexList centerAt(UVertex v1) {
    return center().translate(v1);
  }

  public UVertexList clear() {
    bb=null;
    v.clear();
    return this;
  }
  

  /**
  /**
   * Returns new UVertexList that contains all vertices that are closer
   * to the input <code>vv</code> than <code>limit</code> units.
   * See {@link UVertex#dist(UVertex)}.
   * @param vv
   * @param limit
   * @return
   */
  public UVertexList closeTo(UVertex vv,float limit) {
    UVertexList l=(UVertexList)new UVertexList().enable(NOCOPY);
    
    limit=limit*limit;
    for(UVertex vert:v) if(!vv.equals(vert)){
      if(vert.closeSq(vv,limit)) l.add(vert); 
    }
    
    return l;
  }

  /**
   * Returns new UVertexList containing all vertices that lie within
   * the provided {@link UBB} bounding box.
   * @param bb
   * @return
   */
  public UVertexList inBB(UBB bb) {
    UVertexList l=(UVertexList)new UVertexList().enable(NOCOPY);
    
    for(UVertex vv:v) if(bb.inBB(vv)) l.add(vv);
    
    return l;
  }

  
  public UVertex[] get(int vID[]) {
    return get(vID,null);
  }


  public UVertex[] get(int vID[],UVertex tmp[]) {
    if(tmp==null || tmp.length!=vID.length)
      tmp=new UVertex[vID.length];
    
    int id=0;    
    for(int vid:vID) tmp[id++]=get(vid);

    return tmp;
  }


  public int getVID(UVertex vv) {
    int pos=indexOf(vv);
    return pos;
  }

  public int[] getVID(UVertex[] vv) {
    int[] id=new int[vv.length];
    for(int i=0; i<vv.length; i++) {
      id[i]=indexOf(vv[i]);
    }
    return id;
  }

  public int[] getVID(UVertexList l) {
    int[] id=new int[l.size()];
    for(int i=0; i<id.length; i++) {
      UVertex vv=l.get(i);
      
      id[i]=indexOf(vv);
      float dd=0;
      if(id[i]<0) {
        int found=-1;
        for(int j=0; j<v.size() && found<0; j++) {
          dd=v.get(j).dist(vv);
          if(dd<EPSILON) found=j; 
        }
        
//        add(vv);
        log(found+" "+dd+" | "+size()+" "+vv.str()+" | "+
            id[i]+" "+
            l.indexOf(vv));
      }
    }
    return id;
  }

//public int[] getVID(UVertex[] vv) {
//return getVID(vv,null);
//}
//
//public int[] getVID(UVertex[] vv, int[] vid) {
//if(vid==null) vid=new int[vv.length];
//
//int id=0;
//for(UVertex vvv:vv) vid[id++]=getVID(vvv);
//
////Arrays.sort(vid);
//return vid;
//}

//  public int getVID(UVertex vv) {
//    int index=indexOf(vv);
//    if(index<0) index=addID(vv);
//    
//    return index;}

  public UVertex get(int id) {
    return (id>size() ? null : v.get(id));
  }

  public UVertex last() {
    int n=size();
    return (n<1 ? null : v.get(n-1));
  }

  public UVertex first() {
    return (size()<1 ? null : v.get(0));
  }

  public int size() {
    return v.size();
  }

  public int indexOf(UVertex v2) {
    if(v2==null || size()<1) return -1;
    return v.indexOf(v2);
  }

  /**
   * Returns vertices [n1..n2] from this UVertexList, producing (n2-n1)+1 vertices.
   * Vertices are copied by reference, not value, meaning that the vertices in the 
   * array refer to the same instances as the ones in this list.  
   * @param n1 First vertex ID
   * @param n2 Final vertex ID (inclusive)
   * @return
   */
  public UVertex[] extractArray(int n1,int n2) {
    n2++;
    UVertex[] l=new UVertex[n2-n1];
    
    int cnt=0;
    for(int i=n1; i<n2; i++) l[cnt++]=get(i);
    return l;
  }

  /**
   * <p>Returns a new vertex list containing vertices [n1..n2] of this UVertexList, 
   * producing (n2-n1)+1 vertices.</p>
   * <p>The new vertex list is initialized with the options set for the current UVertexList,
   * so if NOCOPY is enabled the vertices are copied by reference and not by value.</p>  
   * @param n1 First vertex ID
   * @param n2 Final vertex ID (inclusive)
   * @return
   */
  public UVertexList extract(int n1,int n2) {
    UVertexList l=new UVertexList();
    l.setOptions(options);
    n2++;
    for(int i=n1; i<n2; i++) l.add(v.get(i));
    return l;
  }

  public UVertexList insert(int index,UVertex v1) {
    v.add(index,v1);
    return this;
  }

  public UVertexList add(UVertex vv[]) {
    for(UVertex vert:vv) if(vert!=null) add(vert);

    return this;
  }

  public UVertexList add(UVertex v1) {
    
    if(v1==null) {
//      v.add(null);
      return this;
    }
    
    if(isEnabled(NODUPL) && v.contains(v1)) {
      log("Duplicate: "+v1+" "+ v.contains(v1)+" "+str());
      return this;
    }

    v.add(isEnabled(NOCOPY) ? v1 : v1.copy());      
    bb=null;

    return this;
  }

  
  public UVertexList add(float x, float y) {
    add(x,y,0);   
    return this;
  }
  
  public UVertexList add(float x, float y,float z) {
    add(new UVertex(x,y,z));   
    return this;
  }

  public UVertexList add(ArrayList<PVector> pv) {
    for(PVector vv:pv) add(vv.x,vv.y,vv.z);
    return this;
  }

  public UVertexList add(UVertexList v1) {
//    log(optionStr()+" "+size());

    for(UVertex vv:v1) add(vv);
    return this;
  }

  public int addID(float x,float y,float z) {
    return addID(new UVertex(x,y,z));
  }

  public int addID(UVertex v1) {
    bb=null;
    int id=-1;
    
    if(isEnabled(NODUPL)) {
      id=indexOf(v1);
      if(id<0) {
        v1=isEnabled(NOCOPY) ? v1 : v1.copy();
        v.add(v1);
        id=size()-1;
      }      
    }
    else {
      v.add(isEnabled(NOCOPY) ? v1 : v1.copy());
      id=size()-1;
    }
//    add(v);    
    

    return id;
  }

  public int[] addID(UVertexList vl) {
    int[] id=new int[vl.size()];
    for(int i=0; i<id.length; i++) id[i]=addID(vl.get(i));
    return id;
  }

  
  public UVertexList remove(int id) {
    v.remove(id);
    return this;
  }

  public UVertexList remove(int id1,int id2) {
    int n=id2-id1;
    while((n--)>0) v.remove(id1);
    return this;
  }

  public UVertexList scale(float mx,float my,float mz) {
    bb=null;
    if(isClosed()) {
      for(int i=0; i<size()-1; i++) v.get(i).mult(mx,my,mz);
    }
    else for(UVertex vv:v) vv.mult(mx,my,mz);
    
    return this;    
  }

  public UVertexList scale(float m) {
    return scale(m,m,m);    
  }

  public UVertexList scaleInPlace(float m) {
    return scaleInPlace(m,m,m);    
  }

  public UVertexList scaleInPlace(float mx,float my,float mz) {
    UVertex c=centroid();
    translate(-c.x,-c.y,-c.z);
    scale(mx,my,mz);
    translate(c);
    return this;    
  }

  
  public UVertexList translateNeg(UVertex v1) {
    return translate(-v1.x,-v1.y,-v1.z);    
  }

  public UVertexList translate(UVertex v1) {    
    return translate(v1.x,v1.y,v1.z);    
  }

  public UVertexList translate(float mx,float my) {
    return translate(mx,my,0);
  }
  
  public UVertexList translate(float mx,float my,float mz) {
    bb=null;
    boolean closed=isClosed();
    
    if(closed) unclose();
    for(UVertex vv:v) vv.add(mx,my,mz);
    if(closed) close();
    
    return this;    
  }

  public UVertexList moveTo(float mx,float my,float mz) {
    mx-=first().x;
    my-=first().y;
    mz-=first().z;
    return translate(mx,my,mz);
  }

  public UVertexList moveTo(UVertex vv) {
    float mx=vv.x-first().x;
    float my=vv.y-first().y;
    float mz=vv.z-first().z;
    return translate(mx,my,mz);
  }

  public Iterator<UVertex> iterator() {
    // TODO Auto-generated method stub
    return v.iterator();
  }
  
  public UVertexList rotXInPlace(float a) {
    UVertex c=centroid();    
    translate(-c.x,-c.y,-c.z);
    rotX(a);
    translate(c);
    return this;    
  }

  public UVertexList rotYInPlace(float a) {
    UVertex c=centroid();    
    translate(-c.x,-c.y,-c.z);
    rotY(a);
    translate(c);
    
    return this;    
  }

  public UVertexList rotZInPlace(float a) {
    UVertex c=centroid();    
    translate(-c.x,-c.y,-c.z);
    rotZ(a);
    translate(c);
    return this;    
  }

  
  public UVertexList rotX(float deg) {
    bb=null;
    if(isClosed()) {
      for(int i=0; i<size()-1; i++) v.get(i).rotAxis(X,deg);
    }
    else for(UVertex vv:v) vv.rotAxis(X, deg);
    return this;
  }

  public UVertexList rotY(float deg) {
    bb=null;
    if(isClosed()) {
      for(int i=0; i<size()-1; i++) v.get(i).rotAxis(Y,deg);
    }
    else for(UVertex vv:v) vv.rotAxis(Y, deg);
    return this;
  }

  public UVertexList rotZ(float deg) {
    bb=null;
    if(isClosed()) {
      for(int i=0; i<size()-1; i++) v.get(i).rotAxis(Z,deg);
    }
    else for(UVertex vv:v) vv.rotAxis(Z, deg);
    return this;
  }

  public UVertexList mergeClose(float dist) {
    int n=size();
    int n2=n/2;
    dist=dist*dist;
    
    int merged=0;
    
    for(int i=0; i<n2; i++) {
      UVertex vv=v.get(i);
      for(int j=i+1; j<n; j++) {
        UVertex v2=v.get(j);
        if(vv!=v2){
          float d=vv.distSq(v.get(j));
          if(d<dist) {
//            v.set(j, vv);
            v2.set(vv);
            merged++;
          }
        }
      }
    }
    
//    log("merged: "+merged+"/"+size());
    return this;
  }
  
  
  public UVertexList unclose() {
    if(isClosed()) v.remove(v.size()-1);
    return this;
  }

  public UVertexList close() {
    if(!isClosed()) v.add(first());
    return this;
  }

  public boolean isClosed() {
    if(size()<1) return false;
    return last().equals(first());
  }
  
  public boolean contains(UVertex vv) {
    return v.contains(vv);
  }
  
  public boolean hasDuplicates() {
    int cnt=0;
    for(UVertex vv:v) {
      int index=indexOf(vv);
      if(index<cnt) return true;
      cnt++;
    }
    return false;
  }
  
  /**
   * Removes or "fixes" duplicate vertices in the list as identified by 
   * the <code>UVertex.equals()</code> method. <code>doDelete</code> controls how 
   * duplicates are handled: 
   * 
   * If <code>doDelete==true</code>, only the first instance is retained while additional 
   * duplicates are deleted from the list. This ensures that the list contains only unique
   * vertices, but may alter the list length and order. 
   * 
   * If <code>doDelete==false</code>, objects representing duplicate entries are replaced by 
   * an object reference to the first matching instance in the list. Vertex order and length of
   * list is preserved, but subsequent changes to any reference to  duplicate entry will 
   * affect all entries as well, as they now point to a single object. This is useful for
   * consistent STL export (where rounding errors can cause identical vertices to become non-identical).
   * 
   * @param doDelete Flag indicating whether duplicates should be deleted or replaced by a reference
   * to a single instance.
   * 
   * @return
   */
  public UVertexList removeDupl(boolean doDelete) {
    int id=0,cnt=0,pre=size();
    
    UTask task=new UTask("removeDupl");
    
//    enable(NODUPL);
    while(id<v.size()) {
      int index=indexOf(v.get(id));
      if(index<id) {
        
        if(doDelete) { // REMOVE FROM LIST
          v.remove(id);
          cnt++;
//          id--;
        }
        else { // REPLACE WITH REFERENCE TO FIRST INSTANCE
          v.set(id, v.get(index));
        }
      }
      else id++;
    }    
//    if(cnt>0) log("cnt "+cnt+" "+pre+" "+str());
    task.done();
    
    return this;
  }
  
  public int[] removeDuplID() {
    int[] id=new int[size()];
    int i=0,cnt=0,pre=size();
    ArrayList<UVertex> l=new ArrayList<UVertex>();
    
    while(i<pre) {
      UVertex vv=v.get(i);
      int index=l.indexOf(vv);
      if(index<0) {
        l.add(vv);
        index=l.size()-1;
      }
      id[i++]=index;
    }    
    
    v.clear();
    v.addAll(l);
//    if(cnt>0) log("cnt "+cnt+" "+pre+" "+str());
    

    return id; 
  }

  //////////////////////////////////////////
  // UV methods

  public UVertexList setULinear() {
    int id=0,n=size()-1;
    for(UVertex vv:v) vv.U=map(id++,0,n,0,1);
    return this;
  }

  public UVertexList setVLinear() {
    int id=0,n=size()-1;
    for(UVertex vv:v) vv.V=map(id++,0,n,0,1);
    return this;
  }

  public static void setUV(ArrayList<UVertexList> vl) {
    setVLinear(vl);
    setULinear(vl);
  }

  /**
   * Iterates through list of {@link UVertexList}, calling setULinear() for each.
   * @param vl
   */
  public static void setULinear(ArrayList<UVertexList> vl) {
    for(UVertexList vv:vl) vv.setULinear();
  }

  /**
   * Sets the V coordinate of vertices in a list of {@link UVertexList} so
   * that vl.get(0) has V==0 and vl.get(vl.size()-1) has V==1. Useful for
   * setting V as factor of list index for later coloring, or to keep track
   * of original list index after vertices are used in a mesh.
   * @param vl
   */
  public static void setVLinear(ArrayList<UVertexList> vl) {
    setVLinear(vl,1);
  }

  /**
   * Sets the V coordinate of vertices in a list of {@link UVertexList} so
   * that vl.get(0) has V==0 and vl.get(vl.size()-1) has V==maxV. Useful for
   * setting V as factor of list index for later coloring, or to keep track
   * of which list index a vertex belonged to after vertices are used in a mesh.
   * @param vl
   * @param maxV Value of V for last list.
   */
  public static void setVLinear(ArrayList<UVertexList> vl,float maxV) {
    int id=0,n=vl.size()-1;
    for(UVertexList vv:vl) vv.setV(map(id++,0,n,0,1));
  }

  /**
   * Maps U coordinates for the vertices in this list to the range U=[min..max]  
   * @param min
   * @param max
   * @return
   */
  public UVertexList setU(float min,float max) {
    int cnt=0,n=size();
    for(UVertex vv:v) vv.U=map(cnt++, 0,n-1, min,max);
    return this;
  }

  /**
   * Maps V coordinates for the vertices in this list to the range V=[min..max]  
   * @param min
   * @param max
   * @return
   */
  public UVertexList setV(float min,float max) {
    int cnt=0,n=size();
    for(UVertex vv:v) vv.V=map(cnt++, 0,n-1, min,max);
    return this;
  }

  public UVertexList setU(float U) {
    for(UVertex vv:v) vv.U=U;
    return this;
  }

  public UVertexList setV(float V) {
    for(UVertex vv:v) vv.V=V;
    return this;
  }
  
  //////////////////////////////////////////
  // COLOR

  /**
   * Maps the color for each vertex to a gradient [col1..col2]  
   * @param col1
   * @param col2
   * @return
   */
  public UVertexList setColorGradient(int col1,int col2) {
    int cnt=0,n=size();
    for(UVertex vv:v) vv.setColor(lerpColor(col1, col1, map(cnt++,0,n-1,0,1)));
    return this;
  }

  public UVertexList setColor(int c) {
    for(UVertex vv:v) vv.setColor(c);
    return this;
  }

  public UVertexList setColor(int c, int a) {
    setColor(pcolor(c, a));
    return this;
  }

  public UVertexList setColor(float a,float b,float c) {
    return setColor(pcolor(a,b,c));
  }

  public String str() {
    return "["+size()+TAB+str(v,TAB,null)+"]";
  }

  public String strWithID() {
    StringBuffer buf=strBufGet();
    buf.append(size()).append(TAB);
    for(UVertex vv:v) {
      if(vv!=null) buf.append(vv.ID).append('=').append(vv.str()).append(TAB);
      else buf.append("NULL").append(TAB);
    }
    
    if(size()>0) buf.deleteCharAt(buf.length()-1);
    
    return "["+strBufDispose(buf)+"]";
  }
  
  public String dataCompact() {
    return str(v,',',ENCLSQ);
  }


  public UVertexList reverse() {
    Collections.reverse(v);
    return this;
    
  }


}
