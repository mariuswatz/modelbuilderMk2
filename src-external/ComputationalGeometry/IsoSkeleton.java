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

import java.util.ArrayList;
import processing.core.*;
import quickhull3d.*;

public class IsoSkeleton implements PConstants{
	
  PApplet theParent;
  private ArrayList[] adj;
  private float[] VW;
  private float defaultWeight = 1.f;
  private float jointRatio = 0.25f;
  private PVector[] nodes;  
  private boolean[] marked;

  public IsoSkeleton (PApplet _theParent) {
  theParent = _theParent;
    adj = new ArrayList[0];
    VW  = new float[0];
    nodes  = new PVector[0];
    marked = new boolean[0];
  }

  /* ================================================
   Methods to create and manage the parts
   ================================================ */

  public void addEdge(PVector node1, PVector node2) {
    // Check if there either node exist in array of nodes.
    int overlap = -1;
    int node1Index = -1;
    int node2Index = -1;

    overlap = overlapCheck(node1);
    if (overlap < 0) {
      nodes = (PVector[])PApplet.append(nodes, node1);
      adj = (ArrayList[])PApplet.append(adj, new ArrayList());
      VW = (float[])PApplet.append(VW, defaultWeight);
      node1Index = nodes.length - 1;
    } 
    else {
      node1Index = overlap;
    }

    overlap = -1;
    overlap = overlapCheck(node2);
    if (overlap < 0) {
      nodes = (PVector[])PApplet.append(nodes, node2);
      adj = (ArrayList[])PApplet.append(adj, new ArrayList());
      VW = (float[])PApplet.append(VW, defaultWeight);
      node2Index = nodes.length - 1;
    } 
    else {
      node2Index = overlap;
    }

    adj[node1Index].add(node2Index);
    adj[node2Index].add(node1Index);
  }

  private int overlapCheck (PVector test) {  
    float tolerance = 1.f;
    for (int i = 0; i < nodes.length; i++) {
      PVector n = nodes[i];
      if (PApplet.abs(test.x - n.x) < tolerance && PApplet.abs(test.y - n.y) < tolerance && PApplet.abs(test.z - n.z) < tolerance) return i;
    }
    return -1;
  }

  public void clear() {
    adj = new ArrayList[0];
    VW = new float[0];
    nodes = new PVector[0];
    marked = new boolean[0];
  }


  /* ================================================
   Methods to compute the bmesh
   ================================================ */

  public void plot(float thickness, float _jointRatio) {
    
    // Check if the thickness is good > 0 just incase
    float minVal = 0.01f;
    thickness = (thickness < minVal) ? minVal : thickness ;
    for (int i = 0; i < VW.length; i++) {
      float adjWeight = 0.8f + adj[i].size() * 0.2f;
      //adjWeight = (adjWeight < 1) ? 1 : adjWeight;
      VW[i] = thickness * adjWeight;
    }
    
    // Check if jointRatio is a valid number
    jointRatio = (_jointRatio < 0.1f) ? 0.1f : (_jointRatio > 1.0f) ? 1.0f : _jointRatio;
    
    // Reset recursion markers    
    this.marked = new boolean[nodes.length];

    // Compute mesh only if there are nodes.
    if (nodes.length > 0)  this.computeMesh(0);
  }

  private void computeMesh(int v) {
    int count = adj[v].size();
    // Anything above 3 needs to be a joint.
    count = (count > 2) ? 3 : count;
    switch(count) {
    case 0:
      break;
    case 1:
      polygonizeEnd(v);
      break;
    case 2:
      polygonizeElbow(v);
      break;
    case 3:
      polygonizeJoint(v);
      break;
    }

    this.marked[v] = true;

    for (int i = 0; i < adj[v].size(); i++) {
      int w = (Integer)adj[v].get(i);
      if (! marked[w])  computeMesh(w);
    }
  }

  //----------------------------------------------------------------------------------------
  private void polygonizeEnd(int v) {
    PVector pPos = nodes[v];

    // Grab the other end of the one edge node and compute the average cross
    IsoPlane[] planes = new IsoPlane[0];

    for (int i = 0; i < adj[v].size(); i++) {
      int w = (Integer)adj[v].get(i);
      PVector otherEndAvgCross = avgNodeCross(w);
      PVector pt = nodes[w];
      PVector ptPos = new PVector(pt.x, pt.y, pt.z);
      PVector dir = new PVector().sub(ptPos, pPos);

      dir.div(2);
      PVector midPt = new PVector().add(pPos, dir);
      PVector localY = dir.cross(otherEndAvgCross);
      PVector localZ = dir.cross(localY);
      IsoPlane p1 = new IsoPlane(midPt, localZ, localY);
      planes = (IsoPlane[]) PApplet.append(planes, p1);

      IsoPlane p2 = new IsoPlane();
      p2.set(p1);
      p2.setOrigin(pPos);

      this.loft(p2, p1, VW[v], VW[v], (VW[v] + VW[w]) * 0.5f, (VW[v] + VW[w]) * 0.5f);

      // Cap ends
      PVector[] cap = p2.quadPts(VW[v], VW[v]);
      theParent.beginShape();
      for (int j = 0; j < cap.length; j++)  theParent.vertex(cap[j].x, cap[j].y, cap[j].z);
      theParent.endShape(CLOSE);
    }
  }

  //----------------------------------------------------------------------------------------
  private void polygonizeElbow(int v) { 
    // Averge cross vector between edges.
    PVector avgCross = avgNodeCross(v);
    if (avgCross.z < 0) avgCross.mult(-1);
    PVector pPos = nodes[v];

    PVector avgYVec = new PVector();
    IsoPlane[] planes = new IsoPlane[0];
    float[] neiWeight = new float[0];

    float sc = 50.f;
    // Draw the plane for each edge
    for (int i = 0; i < adj[v].size(); i++) {
      int w = (Integer)adj[v].get(i);
      PVector otherNodeAvgCross = avgNodeCross(w);
      if (otherNodeAvgCross.z < 0) otherNodeAvgCross.mult(-1);
      PVector ptPos = nodes[w];
      PVector dir = new PVector().sub(ptPos, pPos);
      dir.div(2);
      PVector midPt = new PVector().add(pPos, dir);

      // average BOTH cross
      PVector blendCross = new PVector().add(avgCross, otherNodeAvgCross);
      blendCross.div(2);
      PVector localY = dir.cross(blendCross);
      PVector localZ = dir.cross(localY);
      IsoPlane p1 = new IsoPlane(midPt, localZ, localY);
      planes = (IsoPlane[]) PApplet.append(planes, p1);
      PVector[] qPts = p1.quadPts(VW[w], VW[w]);

      dir.normalize();
      avgYVec.add(dir);

      neiWeight = (float[]) PApplet.append(neiWeight, VW[w]);
    }

    avgYVec.div(2);
    IsoPlane p2 = new IsoPlane(pPos, avgCross, avgYVec);

    for (int i = 0; i < planes.length; i++) {
      this.loft(p2, planes[i], VW[v], VW[v], (VW[v] + neiWeight[i]) * 0.5f, (VW[v] + neiWeight[i]) * 0.5f);
    }
  }

  //----------------------------------------------------------------------------------------
  private void polygonizeJoint(int v) {
    //Averge cross vector between edges.
    PVector avgCross = avgNodeCross(v);
    PVector pPos = nodes[v];

    ArrayList collectPts = new ArrayList();
    IsoPlane[] jIsoPlanes = new IsoPlane[0];


    // Draw the plane for each edge
    for (int i = 0; i < adj[v].size(); i++) {
      int w = (Integer)adj[v].get(i);
      PVector otherNodeAvgCross = avgNodeCross(w);
      if (otherNodeAvgCross.z < 0) otherNodeAvgCross.mult(-1);
      PVector ptPos = nodes[w];
      PVector dir = new PVector().sub(ptPos, pPos);
      PVector mid = new PVector();
      mid.set(dir);

      // Specifies where to start the joint quads
      float dynVW = ((float)VW[v] / adj[v].size()) * 0.2f;
      dynVW = (dynVW > 0.3f) ? 0.3f : dynVW;
      //dir.mult(dynVW);
      //dir.mult(0.25);
      dir.mult(jointRatio);

      PVector midPt = new PVector().add(pPos, dir);

      // average BOTH cross
      PVector blendCross = new PVector().add(avgCross, otherNodeAvgCross);
      blendCross.div(2);    
      PVector localY = dir.cross(blendCross);

      //PVector localY = dir.cross(avgCross);
      PVector localZ = dir.cross(localY);
      IsoPlane p1 = new IsoPlane(midPt, localZ, localY);

      jIsoPlanes = (IsoPlane[]) PApplet.append(jIsoPlanes, p1);

      PVector[] qPts = p1.quadPts(VW[v], VW[v]);

      collectPts.add(qPts);

      // Loft from 1/10 to mid way
      mid.div(2);
      PVector midPt2 = new PVector().add(pPos, mid);
      IsoPlane temp = new IsoPlane(midPt2, localZ, localY);
      loft(p1, temp, VW[v], VW[v], (VW[v] + VW[w]) * 0.5f, (VW[v] + VW[w]) * 0.5f);
    }

    Point3d[] ptset = new Point3d[0];
    for (int i = 0; i < collectPts.size(); i++) {
      PVector[] ppp = (PVector[])collectPts.get(i);
      for (int j = 0; j < ppp.length; j++) {
        Point3d temp = new Point3d(ppp[j].x, ppp[j].y, ppp[j].z);
        ptset = (Point3d[]) PApplet.append(ptset, temp);
      }
    }

    QuickHull3D hull = new QuickHull3D();
    hull.build (ptset);
    plotHull(hull);
  }

  //----------------------------------------------------------------------------------------
  private PVector avgNodeCross(int v) {
    PVector p = nodes[v];
    PVector[] edgeVecs = new PVector[0];

    for (int i = 0; i < adj[v].size(); i++) {
      int w = (Integer)adj[v].get(i);
      PVector pt = nodes[w];
      PVector vec = new PVector(p.x-pt.x, p.y-pt.y, p.z-pt.z); 
      vec.normalize();
      edgeVecs = (PVector[]) PApplet.append(edgeVecs, vec);
    }

    PVector avgCrossVec = new PVector();
    PVector[] cVecs = new PVector[0];
    for (int i = 0; i < edgeVecs.length; i++) {
      // can't use this for below 2 edges because it cancels out
      PVector cVec = edgeVecs[i].cross(edgeVecs[(i + 1) % edgeVecs.length]);
      cVecs = (PVector[]) PApplet.append(cVecs, cVec);
      avgCrossVec.add(cVec);
    }
    avgCrossVec.div(edgeVecs.length);

    if (avgCrossVec.z < 0) avgCrossVec.mult(-1);

    if (cVecs.length > 2) {
      return avgCrossVec;
    }
    else {
      return cVecs[0];
    }
  }

  //----------------------------------------------------------------------------------------
  private void plotHull(QuickHull3D hull) {
    Point3d[] vertices = hull.getVertices();
    int[][] faceIndices = hull.getFaces();
    for (int i = 0; i < faceIndices.length; i++)
    {
      //colorPairs(i);
      theParent.beginShape();
      for (int k = 0; k < faceIndices[i].length; k++)
      {
        Point3d v = vertices[faceIndices[i][k]];
        theParent.vertex((float)v.x, (float)v.y, (float)v.z);
      }
      theParent.endShape(CLOSE);
    }
  }

  //----------------------------------------------------------------------------------------
  private void loft(IsoPlane p1, IsoPlane p2, float sw, float sh, float ew, float eh) {

    //float dn = p1.nVec.dot(p2.nVec);
    float dx = p1.localXVec.dot(p2.localXVec);
    float dy = p1.localYVec.dot(p2.localYVec);
    //println(dx + " " + dy);

    if (dx < 0) p2.localXVec.mult(-1);
    if (dy < 0) p2.localYVec.mult(-1);
    p2.computeNormal();

    PVector[] sSq = p1.quadPts(sw, sh);
    PVector[] eSq = p2.quadPts(ew, eh);
    for (int i = 0; i < sSq.length; i++) {
      quad3D(sSq[i], sSq[(i+1) % sSq.length], eSq[(i+1) % sSq.length], eSq[i]);
    }
  }

  //----------------------------------------------------------------------------------------
  private void quad3D(PVector p1, PVector p2, PVector p3, PVector p4) {
    theParent.beginShape();
    theParent.vertex(p1.x, p1.y, p1.z); 
    theParent.vertex(p2.x, p2.y, p2.z);
   theParent.vertex(p3.x, p3.y, p3.z); 
   theParent.vertex(p4.x, p4.y, p4.z);
    theParent.endShape(CLOSE);
  }


  /* ============================================================
   Note: IsoPlane
   Author: Toru Hasegawa 
   ============================================================ */
  private class IsoPlane implements PConstants {
    PVector origin;
    PVector nVec;
    PVector localXVec;
    PVector localYVec;

    private IsoPlane() {
      origin = new PVector();
      localXVec = new PVector();
      localYVec = new PVector();
      nVec = new PVector();
    }

    private IsoPlane(PVector originIn, PVector localXVecIn, PVector localYVecIn) {
      origin = originIn;
      localXVec = localXVecIn;
      localYVec = localYVecIn;
      localXVec.normalize();
      localYVec.normalize();
      nVec = localXVec.cross(localYVec);
      nVec.normalize();
    }

    public void set(IsoPlane p) {
      origin.set(p.origin);
      localXVec.set(p.localXVec);
      localYVec.set(p.localYVec);
      nVec.set(p.nVec);
    }

    public void setOrigin(PVector newOrigin) {
      origin.set(newOrigin);
    }

    private void computeNormal() {
      localXVec.normalize();
      localYVec.normalize();
      nVec = localXVec.cross(localYVec);
      nVec.normalize();
    }

    public PVector[] quadPts(float w, float h) {
      PVector[] p = new PVector[4];
      for (int i = 0; i < p.length; i++) {
        p[i] = new PVector();
        p[i].set(origin);
      }
      PVector addX = new PVector();
      PVector addY = new PVector();
      for (int i = 0; i < p.length; i++) {
        addX.set(localXVec);
        addY.set(localYVec);
        addX.mult(w * (PApplet.cos(PApplet.radians(45 + (i * 360 / p.length)))));
        addY.mult(h * (PApplet.sin(PApplet.radians(45 + (i * 360 / p.length)))));
        p[i].add(addX);
        p[i].add(addY);
      }

      return p;
    }
  }
}
