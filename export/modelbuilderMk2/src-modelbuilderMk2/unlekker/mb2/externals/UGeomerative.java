package unlekker.mb2.externals;

import java.util.ArrayList;

import unlekker.mb2.util.UMB;
import unlekker.mb2.geo.*;
import geomerative.*;


/**
 * <p>Utility methods to interface ModelbuilderMk2 with the excellent 
 * <a href="https://github.com/rikrd/geomerative/">Geomerative library</a> by Ricard Marxer.</p>
 * @author marius
 *
 */
public class UGeomerative extends UMB {

  /**
   * <p>Performs a triangulation of a <code>RPolygon</code>, 
   * producing a valid triangle mesh as a UGeo instance. Supports 
   * polygons with holes. Useful for converting SVG files to
   * UGeo meshes.</p>
   * 
   * <p>Triangulation is done using <a href="http://sites-final.uclouvain.be/mema/Poly2Tri/">Poly2Tri-java</a>,
   * an excellent library by Wu Liang. {@link UPoly2Tri} is used as an
   *     interface between the libraries.</p>
   * 
   * @param poly
   * @return
   */
  static public UGeo meshFromRPolygon(RPolygon poly) {
    ArrayList<UVertexList> cont=fromRContour(poly.contours);
    return UPoly2Tri.triangulate(cont);
  }

  /**
   * Converts contours stored in RPolygon to <code>ArrayList<UVertexList></code>.
   * @param poly
   * @return
   */
  static public ArrayList<UVertexList> fromRPolygon(RPolygon poly) {
    ArrayList<UVertexList> cont=fromRContour(poly.contours);
    return cont;
  }
  

  static public ArrayList<UVertexList> fromRContour(RContour contour[]) {
    ArrayList<UVertexList> sl=new ArrayList<UVertexList>();

    for(RContour tmp:contour) {
      sl.add(fromRPoint(tmp.points));
    }
    
    return sl;
  }

  static public UVertexList fromRContour(RContour contour) {
    return fromRPoint(contour.points);
  }

  static public UVertexList fromRPoint(RPoint pt[]) {
    UVertexList vl=new UVertexList();
    for(RPoint tmp:pt) vl.add(tmp.x,tmp.y);  
    return vl;
  }

  static public UVertex fromRPoint(RPoint pt) {
    return new UVertex(pt.x,pt.y);
  }

}
