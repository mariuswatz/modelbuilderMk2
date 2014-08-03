/**
 * ComputationalGeometry
 * A simple, lightweight library for generating meshes such as isometric surfaces, boundary hulls and skeletons.
 * http://thecloudlab.org/processing/library.html
 *
 * Copyright (C) 2013 Mark Collins & Toru Hasegawa http://proxyarch.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author      Mark Collins & Toru Hasegawa http://proxyarch.com
 * @modified    07/25/2013
 * @version     2 (2)
 */

package ComputationalGeometry;

import processing.core.*;
import quickhull3d.*;

public class IsoWrap implements PConstants{
	
	PApplet theParent;
  
   Point3d[] ptset;
   QuickHull3D hull;

  public IsoWrap(PApplet _theParent) {
  theParent = _theParent;
    ptset = new Point3d[0];
    hull = new QuickHull3D();
  }

  //----------------------------------------------------------------------------------------
  public void addPt(PVector pt) {
    Point3d temp = new Point3d(pt.x, pt.y, pt.z);
    ptset = (Point3d[]) PApplet.append(ptset, temp);
  }

  //----------------------------------------------------------------------------------------
  public void addPts(PVector[] pts) {
    for (int i = 0; i < pts.length; i++) {
      Point3d temp = new Point3d(pts[i].x, pts[i].y, pts[i].z);
      ptset = (Point3d[]) PApplet.append(ptset, temp);
    }
  }

  //----------------------------------------------------------------------------------------
  public void plot() {
    hull.build (ptset);
    Point3d[] vertices = hull.getVertices();
    int[][] faceIndices = hull.getFaces();
    for (int i = 0; i < faceIndices.length; i++) {
      theParent.beginShape();
      for (int k = 0; k < faceIndices[i].length; k++) {
        Point3d v = vertices[faceIndices[i][k]];
        theParent.vertex((float)v.x, (float)v.y, (float)v.z);
      }
      theParent.endShape(CLOSE);
    }
  }
}
