/*
 * modelbuilderMk2
 */
package unlekker.mb2.geo;

import java.util.ArrayList;
import java.util.Iterator;

import unlekker.mb2.util.UMB;


/**
 * WORK IN PROGRESS - NOT FUNCTIONAL
 * @author marius
 *
 */
public class UEdgeList extends UMB implements Iterable<UEdge> {
  public UGeo parent;
  ArrayList<UEdge> edges;  
  ArrayList<UFace> facesDone;
  
  
  public UEdgeList() {
    edges=new ArrayList<UEdge>();
    facesDone=new ArrayList<UFace>();
  }
  
  public UEdgeList(UGeo model) {
    this();
    if(model!=null) parent=model;    
    edges=new ArrayList<UEdge>();
    for(UFace ff:model.getF()) {
      add(ff);
//      UVertex vv[]=ff.getV();
//      add(vv[0],vv[1]).add(ff);
//      add(vv[1],vv[2]).add(ff);
//      add(vv[2],vv[0]).add(ff);
    }
    
  }

  public void add(UFace f) {
//    UVertex vv[]=f.getV();
//
//    UEdge ed[]=new UEdge[3];
//    
//    for(int i=0; i<3; i++) {
//      UEdge e=add(vv[i],vv[(i+1)%3]);
//      e.add(f);
//      ed[i]=e;
//    }
    
    int vID[]=f.vID;
  UEdge ed[]=new UEdge[3];
    for(int i=0; i<3; i++) {
      UEdge e=add(vID[i],vID[(i+1)%3]);
      e.add(f);
      ed[i]=e;
    }
    f.setEdges(ed);
  }

  public UEdge add(int id1,int id2) {
    UEdge e=get(id1,id2);
    
    if(e==null) {
      e=new UEdge(parent,parent.getV(id1),parent.getV(id2));      
      
      edges.add(e);
    }
    
    return e;
  }

  public UEdge add(UVertex v1,UVertex v2) {
    if(v1==null || v2==null) log(v1+" "+v2);
    UEdge e=get(v1,v2);

    if(e==null) {
      if(parent!=null) e=new UEdge(parent,v1,v2);
      else e=new UEdge(v1,v2);
      
      edges.add(e);
    }

    
    return e;
  }
  
  public int size() {
    return edges.size();
  }
  
  public Iterator<UEdge> iterator() {
    // TODO Auto-generated method stub
    return edges.iterator();
  }
  
  public boolean contains(UVertex v1,UVertex v2) {
    int id1=-1,id2=-1;
    
    if(parent!=null) {
      id1=parent.getVID(v1);
      id2=parent.getVID(v2);      
    }
    
    if(id1<0 || id2<0) {
      for(UEdge e:edges) if(e.equals(v1,v2)) return true;
    }
    else {
      for(UEdge e:edges) if(e.equals(id1,id2)) return true;
    }
    
    return false;
  }

  public UVertexList getV() {
    UVertexList vl=new UVertexList();
    vl.enable(NOCOPY);
    
    for(UEdge ed:edges) vl.add(ed.v);
    vl.removeDupl(true);
    
    return vl;
    
  }

  public UEdgeList getBoundary() {
    UEdgeList ed=new UEdgeList();
    ed.parent=parent;
    parent.check();
    
    int cnt=0;
    for(UEdge e:edges) {
      if(e.isBoundary()) {
        ed.add(e.v[0], e.v[1]);
      }
//      log((cnt++)+" "+e.sizeF()+" "+e.isBoundary()+" "+ed.size());
    }
    
    log("getBoundary: "+ed.size());
    return ed;
  }
  
  public UEdge get(UVertex v1,UVertex v2) {
    UEdge e=null;
    int id=indexOf(v1, v2);
    if(id>-1) e=edges.get(id);
    return e;
  }

  public UEdge get(int id1,int id2) {
    UEdge e=null;
    int id=indexOf(id1, id2);
    if(id>-1) e=edges.get(id);
    return e;
  }

  public int indexOf(UVertex v1,UVertex v2) {
    int id1=-1,id2=-1;
    
    if(parent!=null) {
      id1=parent.getVID(v1);
      id2=parent.getVID(v2);      
    }

    int cnt=0;
    if(id1<0 || id2<0) {
      for(UEdge e:edges) {
        if(e.equals(v1,v2)) return cnt;
        cnt++;
      }
    }
    else {
      cnt=0;
      for(UEdge e:edges) {
        if(e.equals(id1,id2)) return cnt;
        cnt++;
      }
    }
    
    return -1;
  }

  public int indexOf(int id1,int id2) {

    int cnt=0;
    for(UEdge e:edges) {
      if(e.equals(id1, id2)) return cnt;
      cnt++;
    }
    
    return -1;
  }

  public void check() {
    if(parent==null) return;
    
    ArrayList<UEdge> remove= null;
    
    // add missing faces
    for(UFace ff:parent.getF()) {
      if(facesDone.indexOf(ff)<0) add(ff);
    }
    
    // check faces
    for(UEdge e:edges) {
      if(e.sizeF()!=-1) { // sizeF==-1 means edge is just vertices 
        if(e.sizeF()>0) {
          ArrayList<UFace> f=e.getF();
          for(int i=0; i<f.size(); i++) {
            UFace ff=f.get(i);
            if(!parent.contains(ff)) f.remove(ff) ;
          }
        }
        
        // no faces connected to edge?
        if(e.sizeF()<1) {
          if(remove==null) remove=new ArrayList<UEdge>();
          remove.add(e);
        }
      }
    }

    if(remove!=null) {
      for(UEdge e:remove) edges.remove(e);
    }
  }
}
