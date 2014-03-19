/*
 * modelbuilderMk2
 */
package unlekker.mb2.geo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import unlekker.mb2.util.UMB;

import java.util.*;

import processing.core.PImage;

/**
 * <p>Methods to select faces in UGeo, from single faces ({@link #add(UFace)}
 *  to single faces plus their adjacent faces ({@link #addConnected(UFace)}.
 *  Use ({@link #addConnected()} to expand the selection to include all 
 *  faces adjacent to currently selected faces.</p>  
 *      
 * To draw the selection use {@link #draw()}, it will draw selected faces
 * in red and unselected faces 
 * 
 * @author Marius Watz
 *
 */
public class UGeoGroup extends UMB implements Iterable<UFace> {
  public UGeo parent;
  public ArrayList<UFace> faces;
  public UEdgeList edges;
  public int  type=TRIANGLES;

  
  public UGeoGroup(UGeo model) {
    parent=model;
    faces=new ArrayList<UFace>();
    if(parent.edges!=null) edges=parent.getEdgeList();
    type=TRIANGLES;
  }  
  

  public UGeoGroup(UGeo model,int type) {
    this(model);
    this.type=type;
  }

  public UGeoGroup(UGeo model,int type,int start,int end) {
    this(model);    
    for(int i=start; i<end; i++) add(parent.getF(i));
    this.type=type;
  }

  public boolean contains(UVertex v) {
    for(UFace f:faces) if(f.contains(v)) return true;
    return false;
  }

  public boolean contains(UFace f) {
    return !(faces.indexOf(f)<0);
  }

  public UGeoGroup clear() {
    faces.clear();
    edges=null;
    return this;
  }
  /**
   * Iterates through selection and adds all adjacent faces. 
   * @return
   */
  public UGeoGroup addConnected() {
    edges=parent.getEdgeList();
    ArrayList<UFace> fc=new ArrayList<UFace>();
    for(UFace ff:faces) fc.add(ff);
    
    for(UFace cf:fc) addConnected(cf);
    
    return this;
  }

  /**
   * Adds adjacent faces of the provided {@link UFace}, i.e. any face that
   * shares an edge with that face. If the input face is not part of the 
   * current selection, it is added.
   * @param f
   * @return
   */
  public UGeoGroup addConnected(UFace f) {
    edges=parent.getEdgeList();
    if(edges==null) return this;
    
    add(f);
    UFace[] c=f.connected();
    for(UFace cf:c) add(cf);
    
    return this;
  }

  /**
   * Adds any faces that contain the provided {@link UVertex}.
   * @param vv
   * @return
   */
  public UGeoGroup addConnected(UVertex vv) {
    for(UFace ff:parent.getF()) {
      if(ff.contains(vv)) add(ff);
    }
    
    return this;
  }

  
  public UGeoGroup add(UFace f) {    
    if(f==null || contains(f)) return this;

    faces.add(f);
    type=TRIANGLES;
    return this;
  }

  /**
   * Add all faces from a {@link UGeoGroup}.
   * @param group
   * @return
   */
  public UGeoGroup add(UGeoGroup group) {
    for(UFace f:group) add(f);
    return this;
  }
  
  public UGeoGroup remove(UFace ff) {
    if(contains(ff)) {
      faces.remove(ff);
      type=TRIANGLES;
    }
    return this;
  }

  public int size() {
    return faces.size();
  }
  
  public Iterator<UFace> iterator() {
    return faces.iterator();
  }

  /**
   * Get a random face from the parent UGeo.
   * @return
   */
  public UFace getRndF() {
    int n=parent.sizeF();
    return parent.getF(rndInt(n));
  }

  public UFace getF(int id) {
    return faces.get(id);
  }

  public ArrayList<UFace> getF() {
    return faces;
  }

  public ArrayList<UQuad> getQ() {
    if(type!=QUAD_STRIP) return null;
    ArrayList<UQuad> q=new ArrayList<UQuad>();
    
    for(int i=0; i<size(); i+=2) {
      q.add(new UQuad(getF(i), getF(i+1)));
    }
    
    return q;
  }

  public ArrayList<Integer> getVID() {
    long t=System.currentTimeMillis();
    ArrayList<Integer> l=new ArrayList<Integer>();
    for(UFace ff:faces) {
      l.add(ff.vID[0]);
      l.add(ff.vID[1]);
      l.add(ff.vID[2]);
    }
    
    log(l.size());
    l=removeDupl(l);
    log(l.size());
    log("getVID "+(System.currentTimeMillis()-t));
    
    return l;
  }


  
  public UVertexList getV() {
    UVertexList vl=new UVertexList();
    vl.setOptions(NOCOPY|NODUPL);
    
    ArrayList<Integer> id=getVID();
    for(int tmp:id) vl.add(parent.getV(tmp));
    
    return vl;
  }

  public UVertexList getVNormal() {
    UVertexList vl=new UVertexList();
    vl.setOptions(NOCOPY|NODUPL);
    
    ArrayList<Integer> id=getVID();
    for(int tmp:id) vl.add(parent.getVNormal(tmp));
    
    return vl;
  }

  
  public UGeoGroup subdivide(int subdivType,boolean remove) {
    ArrayList<UFace> newf=USubdivision.subdivide(getF(), subdivType);
    if(remove) {
      parent.remove(getF());
      faces.clear();
    }
    
    parent.addFace(newf);
    type=TRIANGLES;
    faces=newf;
    
    return this;
  }

  /**
   * Draws the faces contained in this UGeoGroup. 
   * @return
   */
  public UGeoGroup draw() {
    return draw(false);
  }

  public UGeoGroup drawNormals(float w) {
    if(parent.tainted) check();
    for(UFace ff:faces) ff.drawNormal(w);
    return this;
  }

  /**
   * If <code>asSelection==true</code> the faces in this group is drawn 
   * in red, while the remaining faces from {@link #parent} are drawn in 
   * the current style. 
   * 
   * @return
   */
  public UGeoGroup draw(boolean asSelection) {
    if(parent.tainted) check();
    
    if(asSelection) {
      ppush().pfill(pcolor(255,0,0));
      draw(faces,0);
      ppop();
      
      for(UFace ff:parent.getF()) {
        if(!contains(ff)) ff.draw();
      }
    }
    else {
      draw(faces,parent.options);
    }
    
    return this;
  }

  
  public UGeoGroup setColor(int col) {
    for(UFace ff:parent.getF()) ff.setColor(col);
    return this;
  }
  
  /**
   * Checks validity of current selection. Any faces in the selection
   * that can't be found in the parent UGeo will be removed.
   * @return
   */
  public UGeoGroup check() {
    for(int i=0; i<size(); i++) {
      UFace fc=faces.get(i);
      if(!parent.contains(fc)) remove(fc);
    }
    return this;
  }
  
  
  public String str() {
    return strf("[%s n=%d]",
        typeName(),size()
        );
  }

  public String typeName() {
    if(!groupTypeNames.containsKey(type)) return NULLSTR; 
    return groupTypeNames.get(type);
  }
  
}
