package unlekker.mb2.externals;

import java.util.ArrayList;

import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UFile;
import unlekker.mb2.util.UMB;
import unlekker.mb2.util.UTask;

/**
 * <p>Utility class to interface with the <a href="http://sites-final.uclouvain.be/mema/Poly2Tri/">Poly2Tri-java</a> 
 * triangulation engine by Wu Liang, producing valid UGeo meshes from simple or compound polygons.</p> 
 * 
 * <p>Poly2Tri handles both simple polygons and 
 * polygons with holes, the latter being represented as a list of vertex lists, 
 * the first of which defines the outside contour, the rest  
 * representing hole boundaries.</p>
 * 
 * <p>This class is especially useful in conjunction with the <a href="https://github.com/rikrd/geomerative/">Geomerative library</a> by Ricard Marxer.
 * Geomerative can read SVG input and generate contour data for complex polygons, which
 * can then be used as input for triangulation.</p> 
 * 
 * <p>See {@link UGeomerative#meshFromRPolygon(geomerative.RPolygon)} for the easiest way
 * to convert SVGs to UGeo.</p>
 *  
 * 
 * 
 *  
 * @author marius
 *
 */
public class UPoly2Tri extends UMB {
  
  /**
   * <p>Triangulates a simple polygon defined by a single contour outline.</p>
   * 
   * @param vl
   * @return
   */
  static public UGeo triangulate(UVertexList vl) {
    ArrayList<UVertexList> stack=new ArrayList<UVertexList>();
    stack.add(vl);
    return triangulate(stack);
  }

  /**
   * <p>Triangulates a complex polygon defined by multiple contours represented
   * as UVertexList. The first entry in the <code>ArrayList</code> defines the 
   * outside contour of the polygon, and must be in clockwise order. Subsequent 
   * <code>UVertexLists</code> describe holes in the polygon, and must be ordered
   * anti-clockwise.</p>
   * 
   * @param stack
   * @return
   */
  static public UGeo triangulate(ArrayList<UVertexList> stack) {
    ArrayList<UVertexList> tmp=new ArrayList<UVertexList>();
    UTask task=new UTask("UPoly2Tri.triangulate");
    
    poly2Tri.Triangulation.debug=true;
    poly2Tri.Triangulation.debugFileName=UFile.getCurrentDir()+"/debug.txt";
    int numVL=stack.size();
    int numV[]=new int[numVL];
    int numVTotal=0;
    
    int cnt=0;
    for(UVertexList vl:stack) {      
      if(vl.hasDuplicates()) {
        UVertexList tl=new UVertexList().enable(NOCOPY);
        tl.add(vl).removeDupl(true);
        log("dupl "+tl.size()+" "+vl.size()+" "+tl.isClockwise());
        tmp.add(tl);
      }
      else tmp.add(vl);
      
      
      
      UVertexList last=last(tmp);
      numV[cnt++]=last.size();
      numVTotal+=last.size();
    }
    
    double[][] v=new double[numVTotal][2];
    
    task.update(5, "preparing data");
    
    
    UVertexList master=new UVertexList().enable(NOCOPY);
    
    cnt=0;
    for(UVertexList vl:tmp) {
      for(UVertex vv:vl) {
        master.add(vv);
        v[cnt][0]=vv.x;
        v[cnt++][1]=vv.y;
      }      
    }

    log("master "+master.hasDuplicates()+" "+master.bb().dim().str());

    task.update(5, "preparing data");

    log("numVL "+numVL+" numVTotal "+numVTotal);
    task.update(60, "Triangulation - start..");
    ArrayList<ArrayList<Integer>> tri=poly2Tri.Triangulation.triangulate(numVL, numV, v);
    
    task.update(60, "Triangulation done.");

    UGeo geo=new UGeo();
    
    if(tri!=null && tri.size()>0) {
      geo.setV(master);
      cnt=0;
      int n=tri.size()/10;
      for(ArrayList<Integer> l:tri) {
        geo.addFace(l.get(0), l.get(1), l.get(2));
        if((cnt++)%n==0) task.update(
            map(cnt,0,tri.size()-1,60,95), "Adding faces.");

//      log(str(l,true));
    }
      cnt=0;
      n=geo.sizeF()-1;
      for(UFace ff:geo.getF()) {
        boolean cw=ff.isClockwiseXY();
        if(cw) {
          ff.reverse();
          cnt++;
          if(cnt%10==0) task.update(map(cnt,0,n,95,100), "Fixing triangle order.");
        }
      }
    }
    else {
      log("Error in UPoly2Tri.triangulate");
    }
    
    task.done();
    
    return geo;
  }

  
}
