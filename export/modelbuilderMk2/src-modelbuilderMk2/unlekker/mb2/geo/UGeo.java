/*
 * modelbuilderMk2
 */
package unlekker.mb2.geo;

import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PImage;
import unlekker.mb2.externals.UPoly2Tri;
import unlekker.mb2.util.UMB;
import unlekker.mb2.util.UTask;

public class UGeo extends UMB  {
  /**
   * Master vertex list, contains all vertices for the face geometry
   * contained in the UGeo instance. Use <code>enable(NODUPL)</code>
   * to ensure no duplicate vertices exist. 
   */
  protected UVertexList vl,vlNormal;
  
  /**
   * ArrayList of triangle faces. 
   */
  protected ArrayList<UFace> faces;

  private UVertexList vltmp;
  private int shapeType,shapeGroup[];
  protected ArrayList<UGeoGroup> groups;
  
  protected UEdgeList edges;
  
  protected boolean tainted=true;
  
  // initialize group type names
  static {
    if(groupTypeNames==null) {
      groupTypeNames=new HashMap<Integer, String>();
      groupTypeNames.put(TRIANGLE_FAN, "TRIANGLE_FAN");
      groupTypeNames.put(TRIANGLES, "TRIANGLES");
      groupTypeNames.put(QUAD_STRIP, "QUAD_STRIP");
      groupTypeNames.put(QUADS, "QUADS");
    }
  }
  

  
  public UGeo() {
    vl=new UVertexList();
    setOptions(NODUPL);
    
    faces=new ArrayList<UFace>();
    groups=new ArrayList<UGeoGroup>();
  }

  public UGeo(UGeo v) {
    this();

    set(v);
    log("UGeo(UGeo v)UGeo(UGeo v) "+str());
  }

  /** Returns a copy of this UGeo instance.
   *  
   * @return
   */
  public UGeo copy() {
    log("copy "+str());
    return new UGeo(this);
  }

  public UGeo clear() {
    vl.clear();
    faces.clear();
    groups.clear();
    edges=null;
    return this;
  }

  public UGeo taint() {
    tainted=true;
    return this;
  }

  /**
   * Regenerate face data (normals, centroid, vertices) for a 
   * specific face.
   * @param id
   * @return
   */
  public UGeo regenerateFaceData(int id) {
    getF().get(id).reset();
    return this;
  }

  /**
   * Regenerate face data (normals, centroid, vertices) for all faces.
   * @return
   */
  public UGeo regenerateFaceData() {
    taint();
    for(UFace ff:getF()) {
      ff.reset();
      ff.normal();
      ff.centroid();
    }
    
    if(vlNormal!=null) {
      vlNormal=null;
      vertexNormals();
    }
    return this;
  }

  /**
   * <p>Checks the validity of connected {@link UGeoGroup} and {@link UEdgeList} 
   * instances, removing any edges or faces that are no longer found in this UGeo.</p>
   * 
   * <p><code>UGeo</code> uses an internal flag ({@link #tainted}) to track operations that might
   * cause its internal state to need updating. If this flag is set, some methods 
   * (such as {@link #writeSTL(String)} and all {@link #draw()} / <code>drawXXX()</code>)
   * will call <code>check()</code> automatically.</p>
   * 
   * <p>If <code>check()</code> is called by the user when <code>tainted==false</code>, 
   * no operations will take place. To force an update, use {@link #taint()} or 
   * {@link #regenerateFaceData()}.</p> 
   * @return
   */
  public UGeo check() {
    if(!tainted) return this;
    
    UTask task=new UTask("UGeo.check "+str());
    
    task.update(25,"Check edges");
    
    if(edges!=null) edges.check();
    int cnt=0,n=groups.size();
    for(UGeoGroup gr:groups) {
      gr.check();
      if(cnt%n==0) {
        float perc=map(cnt,0,groups.size()-1, 25,100);
        task.update(perc,"Check group");
      }
    }

    
    regenerateFaceData();

    
    tainted=false;
    return this;
  }

//  public UGeo fixOrientation() {
//    for(UFace ff:getF()) if(ff.isClockwise()) ff.reverse();
//    taint();
//    return this;
//  }

  
  public UGeo remove(int faceID) {
    if(faceID<sizeF()) {
      remove(getF(faceID));
      vlNormal=null;
    }
    return this;
  }

  public UGeo remove(ArrayList<UFace> fl) {
    for(UFace ff:fl) remove(ff);
    return this;
  }

  public UGeo remove(UFace ff) {
    if(contains(ff)) {
      faces.remove(ff);
      tainted=true;
    }
    
    return this;
  }
  
  public UGeo removeDuplV() {
    int old=vl.size();
    int id[]=vl.removeDuplID();
//    log("removeDuplV:"+id.length+" "+old+" "+vl.size()+" max "+max(id));
    for(UFace ff:faces) {
      ff.vID[0]=id[ff.vID[0]];
      ff.vID[1]=id[ff.vID[1]];
      ff.vID[2]=id[ff.vID[2]];
      ff.getV(true);
    }

    vlNormal=null;
    taint();
    return this;
  }
  
  public UGeo removeDupl() {
    UTask task=new UTask("removeDupl | "+str());
    task.addLog("Before: "+str());
    
    task.update(0,"getV "+sizeV());
    for(UFace ff:faces) ff.getV();
    task.update(25,"getV "+sizeV());
    
    removeDuplV();
    task.update(50,"getV.removeDupl "+sizeV());
    
    int cnt=0,nf=faces.size();
    int modulus=nf/20;
    
    for(UFace ff:faces) {
      if((cnt++)%modulus==0) {
        task.update(
            map(cnt,0,nf-1,75,100),"getV.remapVID");

      }
      ff.remapVID();
    }
    
    ArrayList<UFace> remove=new ArrayList<UFace>();
    for(UFace ff:faces) {
      boolean dupl=false;
      for(UFace f2:faces) if(!dupl && f2!=ff) {
        dupl=f2.equalsVID(ff);
      }
      if(dupl) remove.add(ff);
    }
    
    if(remove.size()>0) {
      task.addLog("Duplicate faces: "+remove.size());
      for(UFace ff:remove) faces.remove(ff);
    }
    
    task.addLog("After: "+str());
    task.done();
//    log(task.log);
    
    return this;
  }

  
  public UGeo setOptions(int opt) {
    super.setOptions(opt);
    if(vl!=null) vl.setOptions(opt);
    return this;
  }

  public UGeo enable(int opt) {
    super.enable(opt);
    vl.enable(opt);
    return this;
  }

  public UGeo disable(int opt) {
    super.disable(opt);
    vl.disable(opt);
    return this;
  }

  
  public UGeo setV(UVertexList vl) {
    this.vl=vl;
    if(faces.size()>0) {
      for(UFace ff:faces) ff.remapVID();
      regenerateFaceData();
    }
    return this;
  }

  /**
   * Copies the mesh data contained <code>model</code> to this UGeo instance,
   * replacing any existing data. The <code>model.vl</code> vertex list
   * is copied using {@see UVertexList#copy()}, then face data is copied
   * by creating new UFace instances using the {@see UFace#vID} vertex indices. 
   *  
   * @param model
   * @return
   */
  public UGeo set(UGeo model) {
    UTask task=new UTask("UGeo.set()");
    
   vl=model.getV().copy();
   task.update(10,"set(UGeo model) "+model.str()+" "+model.getV().str()+" "+ vl.str());
   
   faces=new ArrayList<UFace>();
   int cnt=0,n=max(1,model.sizeF()/20);
   for(UFace ff:model.getF()) {
     addFace(ff.vID);     
     last(faces).setColor(ff.col);
     if((cnt++)%n==0) 
       task.update(
           map(cnt,0,model.sizeF(),10,100),
           "Adding faces");
   }
   task.done();
   
   return this;
  }
  
  public UGeo setColor(int col) {
    for(UFace ff:getF()) ff.setColor(col);
    return this;
  }

  public UGeo unregisterGroup(UGeoGroup group) {
    if(groups.indexOf(group)>-1) groups.remove(group);
    return this;    
  }

  public UGeo registerGroup(UGeoGroup group) {
    if(groups.indexOf(group)<0) groups.add(group);
    return this;    
  }
  
  private UGeo groupBegin() {
    return groupBegin(TRIANGLES);
  }

  public UGeo groupBegin(int type) {
    shapeGroup=new int[]{type,sizeF(),-1};
//    groups.add(new UGeoGroup(this,type).begin());
//    log("groupBegin "+
//        groupTypeNames.get(type)+" "+groups.size()+" "+sizeF());
    return this;
  }

  public UGeo groupEnd() {
    shapeGroup[2]=sizeF();
    UGeoGroup gr=new UGeoGroup(this,
        shapeGroup[0],shapeGroup[1],shapeGroup[2]);
    groups.add(gr);
    
//    log("groupEnd: "+sizeGroup()+": "+gr.str());
    return this;
  }

  public ArrayList<UGeoGroup> getGroups() {
    return groups;
  }

  /**
   * Returns a list of UFace instances belonging to a given 
   * group of faces. 
   * 
   * When groups of triangles are added to UGeo by methods like
   * triangleFan(), quadstrip() or add(UGeo), the start and end IDs
   * for the faces produced by that operation is stored internally so
   * that the group can later be retrieved.
   * 
   * @param id
   * @return
   */
  
  public ArrayList<UFace> getGroupF(int id) {
    return groups.get(id).getF();
  }

  public UGeoGroup getGroup(int id) {
    return groups.get(id);
  }

  public int sizeGroup() {
    return groups.size();
  }



  public UGeo add(UGeo model) {
    UTask task=new UTask("UGeo.add(UGeo)");
    task.update(0,"UGeo.add(UGeo) "+str()+" < "+model.str());
    
    int ID[]=addID(model.getV());
    
    int n=model.sizeGroup();
    for(int i=0; i<n; i++) {
      UGeoGroup gr=model.getGroup(i);
      groupBegin(gr.type);
      
      for(UFace ff:gr.getF()) {
        addFace(ID[ff.vID[0]], ID[ff.vID[1]], ID[ff.vID[2]]);
      }
      
//      addFace(gr.getF());
      groupEnd();
      if(i%2==0) {
        float perc=map(i,0,n-1,0,50);
        task.update(perc,"add groups");
      }
    }
    
    
    ArrayList<UFace> noGroup=model.getFNoGroup();
    int cnt=0;
    if(noGroup.size()>0) {
      groupBegin(TRIANGLES);
      addFace(noGroup);
      groupEnd();
      if((cnt++)%10==0) {
        float perc=map(cnt,0,noGroup.size(),50,100);
        task.update(perc,"add faces");
      }
      groupEnd();
    }

//    groupBegin(TRIANGLES);
//    int cnt=0;
//    ArrayList<UFace> theFaces=model.getF();
//    for(UFace ff:theFaces) {
//      ff.v=null;
//      
//      UVertex vv[]=ff.getV();
//      addFace(vv);
//      getF(sizeF()-1).setColor(ff.col);
//      taskTimerUpdate(map(cnt++,0,theFaces.size()-1, 0,100));
//    }    
//    groupEnd();
    
    task.done();
    
/*    if(model.sizeGroup()>0) {
      int gn=model.sizeGroup();
      for(int i=0; i<gn; i++) {
        int id[]=model.getGroupID(i);
        groupBegin(id[2]);
        ArrayList<UFace> gr=model.getGroup(i);
        for(UFace ff:gr) {
          UVertex vv[]=ff.getV();
          addFace(vv);
        }    
        groupEnd();
      }
    }
    
    else {
      for(UFace ff:model.getF()) {
        UVertex vv[]=ff.getV();
        addFace(vv);
      }    
    }
*/    
    return this;
    
  }
  
  //////////////////////////////////////////
  // BOUNDING BOX, DIMENSIONS 


  public UBB bb() {
    return bb(false);
  }

  public UBB bb(boolean force) {
    return vl.bb(force);
  }

  /** 
   * @return The centroid of this mesh, found by calling
   * <code>vl.bb().centroid()</code>.
   */
  public UVertex centroid() {
    return bb().centroid;
  }

  public UVertexList vertexNormals() {
    if(vlNormal!=null) return vlNormal;
        
    UTask task=new UTask("UGeo.vertexNormals()");

    UVertexList vertNormal=new UVertexList();
    UVertexList tmp=new UVertexList();
    tmp.enable(NODUPL);

    boolean debug=false;
    
    int n=0,nmod=max(1,sizeV()/30);
    UVertex vn=new UVertex();
    for(UVertex vv:getV()) {
      n=0; 
      vn=new UVertex();
      String str="";
      
      tmp.clear();
      for(UFace ff:getF()) {
        if(ff.contains(vv)) {
          tmp.add(ff.normal());
        }
      }

      n=tmp.size();
      float nd=1f/(float)n;

      for(UVertex vt:tmp) {
        vn.add(vt.copy().mult(nd));
        if(debug) str+=" "+vt.str();
      }
      
//      vn.norm();
      if(debug) log(vv.ID+" "+n+" "+str+" >>>> "+vn.str());
      if(n%nmod==0) task.update(
          map(n,0,sizeV()-1,0,100),
          "vertexNormals");
      
      vertNormal.add(vn);
    }
    
    task.done();
    
    vlNormal=vertNormal;
    return vlNormal;
  }
  
  /** 
   * Translates mesh so that its centroid lies at
   * the origin.
   */
  public UGeo center() {
    return translateNeg(bb().centroid);
  }

  public static void center(ArrayList<UGeo> models) {
    UBB btmp=new UBB();
    for(UGeo geo:models) btmp.add(geo.bb());
    btmp.check();
    UVertex c=btmp.centroid;
    for(UGeo geo:models) geo.translateNeg(c);
    
  }

  /**
   * Translates mesh so that its centroid lies at
   * at the provided point in space.
   * @param v1 Point where mesh will be centered
   * @return
   */
  public UGeo centerAt(UVertex v1) {
    return center().translate(v1);
  }

  /**
   * Translate mesh by the negative of the given vertex,
   * equivalent to <code>translate(-v1.x,-v1.y,-v.z)</code>.
   * @param v1
   * @return
   */
  public UGeo translateNeg(UVertex v1) {
    return translate(-v1.x,-v1.y,-v1.z);    
  }

  public UGeo translate(UVertex v1) {    
    return translate(v1.x,v1.y,v1.z);    
  }

  public UGeo translate(float mx,float my) {
    return translate(mx,my,0);
  }
  
  public UGeo translate(float mx,float my,float mz) {
    vl.translate(mx, my, mz);
    return this;    
  }
  
  public UGeo rotX(float deg) {
    vl.rotX(deg);
    return this;
  }

  public UGeo rotY(float deg) {
    vl.rotY(deg);
    return this;
  }

  public UGeo rotZ(float deg) {
    vl.rotZ(deg);
    return this;
  }

  public UGeo scaleToDim(float max) {
    float m=max/bb().dimMax();
    return scale(m,m,m);
  }

  public UGeo scale(float m) {return scale(m,m,m);}

  public UGeo scale(float mx,float my,float mz) {
    vl.scale(mx,my,mz);
    return this;
  }

  /**
   * Returns UVertex instance where x,y,z represent the
   * size of this mesh in the X,Y,Z dimensions.  
   * @return
   */
  public UVertex dim() {
    return bb().dim;
  }


  public float dimX() {return bb().dimX();}
  public float dimY() {return bb().dimY();}
  public float dimZ() {return bb().dimZ();}
  public float dimMax() {return bb().dimMax();}


  //////////////////////////////////////////
  // LIST TOOLS


  /**
   * @param models List of UGeo instances.
   * @return The total number of faces contained in all
   * meshes in the list. 
   */
  public static int sizeF(ArrayList<UGeo> models) {
    int n=0;
    for(UGeo gg:models) if(gg!=null) n+=gg.sizeF();
    return n;
  }

  /**
   * @return Number of triangle faces contained in this mesh.
   */
  public int sizeF() {
    return faces.size();
  }

  /**
   * @return Number of vertices contained in this mesh.
   */
  public int sizeV() {
    return vl.size();
  }

  public UEdgeList getEdgeList() {
    if(tainted) check();
    if(edges==null) edges=new UEdgeList(this);
    
    return edges;
  }
  
  public ArrayList<UFace> getF() {
    return faces;
  }

  public ArrayList<UQuad> getQ() {
    ArrayList<UQuad> quad=new ArrayList<UQuad>();
    
    for(UGeoGroup g:groups) {
      ArrayList<UQuad> tmp=g.getQ();
      if(tmp!=null) quad.addAll(tmp);
    }
    
    return quad;
  }

//  public ArrayList<UFace> getF() {
//    return faces;
//  }

  public ArrayList<UFace> getFNoGroup() {
    if(tainted) check();
    ArrayList<UFace> fl=new ArrayList<UFace>();
    
    for(UFace ff:getF()) {
      boolean found=false;
      for(UGeoGroup gr:groups) {
        if(!found) {
          found=gr.contains(ff);
        }
      }
      
      if(!found) {
        fl.add(ff);
      }
    }
    
    if(fl.size()>0) {
      logf("UGeo: Found %d faces with no group.",fl.size());
    }
    return fl;
  }

  public UFace getF(int id) {
    if(tainted) check();
    return faces.get(id);
  }

  /**
   * Returns list of faces whose vertices are completely contained
   * inside the provided {@link UBB} bounding box.
   * @param bb
   * @return
   */
  public ArrayList<UFace> getFacesInBB(UBB bb) {
    if(tainted) check();
    return getFacesInBB(bb,true);
  }

  /**
   * <p>Returns list of faces whose vertices are contained
   * inside the provided {@link UBB} bounding box.</p>
   *  
   * <p>If <code>allVertices==false</code>, a face will be included
   * if any of its vertices lie within the bounding box, 
   * otherwise all vertices must be contained in order to
   * be included.</p> 
   * @param bb
   * @return
   */
  public ArrayList<UFace> getFacesInBB(UBB bb,boolean allVertices) {
    ArrayList<UFace> ff=new ArrayList<UFace>();
    
    for(UFace f:faces) if(f.inBB(bb, allVertices)) ff.add(f);
    return ff;
  }

  /**
   * @return A direct reference to the {@link UVertexList} that
   * is the master vertex list for this mesh.
   */
  public UVertexList getV() {
    return vl;
  }
  
  public UVertex getV(int id) {
    return vl.get(id);
  }

  /**
   * Returns the vertex normal for a specific vertex. Calls 
   * {@link #vertexNormals()} to calculate the vertex normals if 
   * needed.
   * @param id
   * @return
   */
  public UVertex getVNormal(int id) {    
    return vertexNormals().get(id);
  }

  public static UMB drawModels(ArrayList<UGeo> models) {
    for(UGeo geo:models) geo.draw();
    return UMB.UMB;

  }

  public UGeo drawTextured(PImage texture) {
    if(checkGraphicsSet()) {
      if(tainted) check();
      
      g.beginShape(TRIANGLES);
      g.texture(texture);
      g.textureMode(g.NORMAL);
      
      for(UFace f:faces) {
        UVertex vv[]=f.getV();
        pvertex(vv, true);
      }
      
      g.endShape();
    }
    return this;
  }
    
  public UGeo draw() {
    if(tainted) check();
    return draw(options);
  }

  public UGeo drawNormals(float w) {
    if(tainted) check();
    for(UFace ff:faces) ff.drawNormal(w);
    return this;
  }

  public UGeo drawVertexNormals(float w) {
    if(tainted) check();
    if(vlNormal==null || vl.size()!=vlNormal.size()) vertexNormals();
    
    UVertex v1,v2=new UVertex();
    for(int i=0; i<vl.size(); i++) {
      v1=vl.get(i);
      v2.set(vlNormal.get(i)).mult(w).add(v1);
      pline(v1,v2);
    }
    return this;
  }

  /**
   * Flips all face normals by calling {@link UFace#reverse()} 
   * @return
   */
  public UGeo reverse() {
//    log("reverseNormals "+getF(0).normal().str());
    for(UFace ff:faces) ff.reverse();
//    log("reverseNormals "+getF(0).normal().str());
    return this;
  }
  
  public UGeo draw(int theOptions) {
    if(checkGraphicsSet()) {
      g.beginShape(TRIANGLES);      
      
      boolean colorFace=isEnabled(theOptions,COLORFACE);
      boolean colorVertex=isEnabled(theOptions,COLORVERTEX);
      boolean isSmooth=isEnabled(theOptions,SMOOTHMESH);
      
      UVertex[] vv,vn;

      // SMOOTH WITH NORMALS
      if(isSmooth) {
        if(colorFace) {
          for(UFace f:faces) {
            vv=f.getV();
            vn=f.getVNormals();
            
            g.fill(f.col);
            g3d.normal(vn[0].x, vn[0].y, vn[0].z);
            pvertex(vv[0]);
            g3d.normal(vn[1].x, vn[1].y, vn[1].z);
            pvertex(vv[1]);
            g3d.normal(vn[2].x, vn[2].y, vn[2].z);
            pvertex(vv[2]);
          }
        }
        else if(colorVertex) {
          for(UFace f:faces) {
            vv=f.getV();
            vn=f.getVNormals();
            
            g.fill(vv[0].col);
            g3d.normal(vn[0].x, vn[0].y, vn[0].z);
            pvertex(vv[0]);
            
            g.fill(vv[1].col);
            g3d.normal(vn[1].x, vn[1].y, vn[1].z);
            pvertex(vv[1]);
            
            g.fill(vv[2].col);
            g3d.normal(vn[2].x, vn[2].y, vn[2].z);
            pvertex(vv[2]);
          }
        }
        else {
          for(UFace f:faces) pvertex(f.getV());
        }
      }
      // NO NORMALS
      else {
        if(colorFace) {
          for(UFace f:faces) {
            g.fill(f.col);
            pvertex(f.getV());
          }
        }
        else if(colorVertex) {
          for(UFace f:faces) {
            vv=f.getV();
            g.fill(vv[0].col);
            pvertex(vv[0]);
            g.fill(vv[1].col);
            pvertex(vv[1]);
            g.fill(vv[2].col);
            pvertex(vv[2]);
          }
        }
        else {
          for(UFace f:faces) pvertex(f.getV());
        }
      }
      
      g.endShape();
    }
    return this;
  }

  ///////////////////////////////////////////////////
  // BEGINSHAPE / ENDSHAPE METHODS
  
  
  /**
   * Starts building a new series of faces, using the same logic 
   * as <a href="http://processing.org/reference/beginShape_.html">PApplet.beginShape()</a>.
   * Currently supports the following types: TRIANGLE_FAN, TRIANGLE_STRIP, TRIANGLES, QUADS, QUAD_STRIP
   * 
   * While shape is being built vertices are stored in a temporary 
   * array, and only the ones that are used are copied to the vert vertexlist.
   * @param _type Shape type (TRIANGLE_FAN, TRIANGLE_STRIP, TRIANGLES, QUADS, QUAD_STRIP)
   */
  public UGeo beginShape(int _type) {
    
    if(vltmp==null) vltmp=new UVertexList();
    else vltmp.clear();
    vl.bb=null;
    
//    shapeRecord=new UStrip(_type);

    shapeType=_type;
    
    return this;
  }

  public UGeo endShape() {
    groupBegin(shapeType);

    
    int[] vID=vl.addID(vltmp);
//    log(str(vID));
    
    switch (shapeType) {
      case TRIANGLE_FAN: {
        UVertex cp=vltmp.first();
        int n=(vltmp.size()-1)-1;
        int id=1;
        
        
        for(int i=0; i<n; i++) {
          addFace(new int[] {vID[0],vID[id++],vID[id]});

//          addFace(cp,vltmp.get(id++),vltmp.get(id));
        }
      }
      break;

      case TRIANGLES: {
        int n=(vltmp.size())/3;
        int id=0;
        
        for(int i=0; i<n; i++) {
          addFace(new int[] {
              vID[id++],
              vID[id++],
              vID[id++]
          });
        }
      }
      break;

      case TRIANGLE_STRIP: {
        int n=(vltmp.size())-2;
        int id=0;
        
        for(int i=0; i<n; i++) {
          if(i%2==0) {
            addFace(vID[id],vID[id+2],vID[id+1]);
          }
          else {
            addFace(vID[id],vID[id+1],vID[id+2]);
          }
          id++;
        }
        // PROCESSING
//        int stop = shapeLast - 2;
//        for (int i = shapeFirst; i < stop; i++) {
//          // have to switch between clockwise/counter-clockwise
//          // otherwise the feller is backwards and renderer won't draw
//          if ((i % 2) == 0) {
//            addTriangle(i, i+2, i+1);
//          } else {
//            addTriangle(i, i+1, i+2);
//          }
//        }

//        int stop = bvCnt - 2;
//        for (int i = 0; i < stop; i++) {
//          // HANDED-NESS ISSUE
////          if(i%2==1) addFace(bv[i], bv[i+2], bv[i+1]);
////          else addFace(bv[i], bv[i+1], bv[i+2]);
//          if(i%2==1) addFace(new UVertex[] {bv[i], bv[i+2], bv[i+1]});
//          else addFace(new UVertex[] {bv[i], bv[i+1], bv[i+2]});
//        }
      }
      break;

      // Processing order: bottom left,bottom right,top right,top left
//      addTriangle(i, i+1, i+2);
//      addTriangle(i, i+2, i+3);
      case QUADS: {
        int n=(vltmp.size())/4;
        int id=0;
        UVertex v0,v2;
        
        int vvID[];
        
        for(int i=0; i<n; i++) {
          vvID =new int[] {
              vID[id],vID[id+1],vID[id+2]
          };
          addFace(vvID);

          vvID =new int[] {
              vID[id],vID[id+2],vID[id+3]
          };
          addFace(vvID);

//          v0=vltmp.get(id);
//          v2=vltmp.get(id+2);          
//          addFace(v0,vltmp.get(id+1),v2);
//          addFace(v0,v2,vltmp.get(id+3));
          id+=4;
        }
      }
      break;

      /* From PGraphics3D.java, 1.5.1
       for (int i = shapeFirst; i < stop; i += 2) {
        // first triangle
        addTriangle(i+0, i+2, i+1);
        // second triangle
        addTriangle(i+2, i+3, i+1);
      }

       */
      case QUAD_STRIP: {
        
        int n=vltmp.size()/2;
        int id=0;
        UVertex v0=null,v1=null,v2=null,v3=null;
        
/*        for(int i=0; i<n; i++) {
          v2=vltmp.get(id);          
          v3=vltmp.get(id+1);         
          if(i>0) {
//            addFace(new int[] {vID[id],vID[id+2],vID[id+1]});
//            addFace(new int[] {vID[id+3],vID[id+1],vID[id+2]});
            addFace(v0,v2,v1);
            addFace(v3,v1,v2);
          }
          v0=v2;
          v1=v3;
          id+=2;
        }
*/
        int vid0= 0,vid1= 0,vid2,vid3;
        
        for(int i=1; i<n; i++) {
          addFace(new int[] {vID[id],vID[id+2],vID[id+1]});
          addFace(new int[] {vID[id+3],vID[id+1],vID[id+2]});
          id+=2;
        }

        
//        for(int i=0; i<n; i++) {
//          vid2=vID[id];
//          vid3=vID[id+1];
//          if(i>0) {
//            addFace(new int[] {vid0,vid2,vid1});
//            addFace(new int[] {vid2,vid3,vid1});
//          }
//          
//          vid0=vid2;
//          vid1=vid3;
////          addFace(new int[] {vID[id],vID[id+2],vID[id+1]});
////          addFace(new int[] {vID[id+3],vID[id+1],vID[id+2]});
////          addFace(new int[] {vID[id+2],vID[id+1],vID[id]});
////          addFace(new int[] {vID[id+2],vID[id+3],vID[id+1]});
//          id+=2;
//        }

//        for(int i=0; i<n; i++) {
//          v2=vltmp.get(id);          
//          v3=vltmp.get(id+1);         
//          if(i>0) {
//            addFace(v0,v2,v1);
//            addFace(v3,v1,v2);
//          }
//          v0=v2;
//          v1=v3;
//          id+=2;
//        }
      }
      break;

      case POLYGON:{
        log("UGeo: POLYGON currently unsupported.");
      }
      break;
    }
    
    vltmp.clear();
    tainted=true;
    
    groupEnd();
    
//    UUtil.log("Faces: "+faceNum);
    return this;

  }
  
  public boolean contains(UFace ff) {
    return faces.indexOf(ff)>-1;
  }

  public int[] addID(UVertexList v1) {
    return vl.addID(v1);
  }

  public UGeo add(UVertexList v1) {
    vl.add(v1);    
    return this;
  }

  public UGeo add(UVertex v1) {
    vl.add(v1);    
    return this;
  }
  
  public UGeo addFace(UVertex vv[]) {
    int vID[]=new int[] {
        addVertex(vv[0]),
        addVertex(vv[1]),
        addVertex(vv[2])
    };
    
    return addFace(vID);
  }

  public UGeo addFace(ArrayList<UFace> f) {
    for(UFace ff:f) {
      if(ff.parent==null || ff.parent!=this) {
        ff=ff.copy(this);
      }
      addFace(ff);
    }
    return this;
  }

  public UGeo addFace(UFace f) {
    tainted=true;
    
    
    
    f.setParent(this);
    faces.add(f);
    return this;
  }

  public UGeo addFace(int id1,int id2,int id3) {
    return addFace(new int[] {id1,id2,id3});
  }

  public UGeo addFace(int vID[]) {
    tainted=true;
    
    edges=null;
    faces.add(new UFace(this,vID));
//    addFace(vl.get(vID[0]),vl.get(vID[1]),vl.get(vID[2]));
    return this;
  }

  public UGeo addFace(UVertex v1, UVertex v2, UVertex v3) {
    return addFace(
        addVertex(v1),
        addVertex(v2),
        addVertex(v3)
        );
  }

//  public UGeo addFace(UVertex v1, UVertex v2, UVertex v3) {
//    edges=null;
//    if(!UFace.check(v1,v2,v3)) {
//      log("Invalid face");
//      return this;
//    }
//    
//    UFace ff=new UFace(this, v1, v2, v3);
//    if(duplicateF(ff)) {
//      log("Duplicate face");
//      return this;
//    }
//    
////    faces.add(new UFace(this, v1,v2,v3));
//    faces.add(ff);
//    tainted=true;
//
//    vl.bb=null;
//    return this;
//  }

  public boolean duplicateF(UFace ff) {
    int cnt=0;
    
    for(UFace theFace:faces) if(theFace.equals(ff)) return true;
    
    // TODO Auto-generated method stub
    return false;
  }

  /**
   * Add vertex to shape being built by <code>beginShape() / endShape()</code>
   * @param x
   * @param y
   * @param z
   * @return 
   */
  public UGeo vertex(float x,float y,float z) {
    vertex(new UVertex(x,y,z));
    return this;
  }

  /**
   * Add UVertex vertex to shape being built by <code>beginShape() / endShape()</code>
   * The vertex information is copied, leaving the original UVertex instance unchanged.
   * @param v
   * @return 
   */
  public UGeo vertex(UVertex v) {
    vltmp.add(v);
    return this;
  }

  /**
   * Add vertex list to shape being built by <code>beginShape() / endShape()</code>. 
   * All vertices are copied, leaving the original instances unchanged.
   * @param vvl Vertex list
   * @param reverseOrder Add in reverse order?
   * @return UGeo
   */
  public UGeo vertex(UVertexList vvl,boolean reverseOrder) {
    if(reverseOrder) {
      for(int i=vvl.size()-1; i>-1; i--) vertex(vvl.get(i));
    }
    else {
      for(UVertex vv:vvl) vertex(vv);
    }
    
    return this;
  }

  /**
   * Adds vertex list to shape being built by <code>beginShape() / endShape()</code>. 
   * All vertices are copied, leaving the original instances unchanged.
   * @return 
   */
  public UGeo vertex(UVertexList vvl) {
    return vertex(vvl, false);
  }

  public int[] getVID(UVertex vv[]) {    
    return getVID(vv,null);
  }

  public int[] getVID(UVertex vv[],int vid[]) {    
    return vl.getVID(vv);
  }

  /**
   * Get the list index of the provided vertex using 
   * {@link UVertexList.getVID()}. Returns -1 if vertex is not
   * found in list.
   * @param vv
   * @return
   */
  public int getVID(UVertex vv) {    
    return vl.getVID(vv);
  }

  /**
   * Get the list indices of an array of provided vertices using 
   * {@link UVertexList.getVID()}. Index values may include -1 for
   *  vertices that are not found.
   * @param vv
   * @return
   */
  public UVertex[] getVByID(int vID[]) {
    return vl.get(vID);
  }
  
  public UVertex[] getVByID(int vID[],UVertex tmp[]) {
    return vl.get(vID,tmp);
  }

  public UVertex getVertex(int vID) {    
    return vl.get(vID);
  }

  public int addVertex(UVertex v1) {    
    int id=vl.indexOf(v1);
    if(id<0) id=vl.addID(v1);
    return id;
  }

  public UGeo quadstrip(ArrayList<UVertexList> stack) {
    return quadstrip(stack,false);
  }

  public UGeo quadstrip(ArrayList<UVertexList> stack,boolean addCaps) {
    UVertexList last=null;  
    
    long tD,t=System.currentTimeMillis();
    long start=t;
    int cnt=0;


    if(UVertex.eqTask!=null) 
      UVertex.eqTask.clear();
    
    UTask task=new UTask("quadstrip(ArrayList n="+
        stack.size()+
        ")");
    
    int optionsOld=options;
    
    boolean nodupl=isEnabled(NODUPL);
    if(nodupl) {
      disable(NODUPL);
    }
    
    ArrayList<int[]> vID=new ArrayList<int[]>();
    
    for(UVertexList vvl:stack) {
      vID.add(vl.addID(vvl));
//      log("vID "+vID.size()+" "+sizeV()+" "+UVertex.eqTask.strData());
      task.update(map(vID.size(),0,stack.size()-1,0,25),"v="+sizeV());

    }
    
//    log("got vID "+papplet.millis());
    
    int n=vID.get(0).length;
    int qID[]=new int[n*2];
    
    
    cnt=0;
    for(UVertexList vvl:stack) {
      if(last!=null) {
        int id=0;
        int id1[]=vID.get(cnt-1);
        int id2[]=vID.get(cnt);
        
        for(int i=0; i<n; i++) {
          qID[id++]=id1[i];
          qID[id++]=id2[i];
        }
        
        quadstrip(last,vvl,qID);
      }
      last=vvl;
      task.update(map(cnt,0,stack.size()-1,25,100),
          "v="+sizeV()+" f="+sizeF());
     
      cnt++;
      
    }
    
    if(addCaps) {
      triangleFan(stack.get(0),true);
      triangleFan(last(stack));
    }
    
    setOptions(optionsOld);
    if(isEnabled(NODUPL)) removeDupl();

    
    task.done();
    if(UVertex.eqTask!=null) log(UVertex.eqTask.log);
    
    return this;
  }

  /**
   * <p>Uses the Poly2Tri library to create a 
   * triangulation of the provided vertex list. Primarily useful for meshing irregular
   * polygons and point sets representing 2.5D topologies,
   * i.e. terrain-like geometry.</p> 
   * 
   * <p>The triangulation logic acts in a 2D plane, hence point clouds 
   * representing true 3D volumes will give poor results, requiring
   * convex hull or other re-meshing strategies.</p>
   *  
   *  <p>See {@link UPoly2Tri} for details. By using UPoly2Tri directly
   *  it is also possible to triangulate polygons with holes.</p> 
   * @param vl
   * @return
   */
  public UGeo triangulation(UVertexList vl) {
    return triangulation(vl,false);
  }

  public UGeo triangulation(UVertexList vl,boolean reverse) {
    int plane=vl.bb().dimBiggestPlane();
    if(plane==XZ) vl.rotX(HALF_PI);
    if(plane==YZ) vl.rotY(HALF_PI);
    
    UGeo tri=UPoly2Tri.triangulate(vl);
    if(plane==XZ) vl.rotX(-HALF_PI);
    if(plane==YZ) vl.rotY(-HALF_PI);    
    if(reverse) tri.reverse();
    
    add(tri);
    
//    int oldSize=sizeF();
//    new UTriangulate(this, vl);
//    if(reverse) {
//      // reverse all new faces
//      for(int i=oldSize; i<sizeF(); i++) faces.get(i).reverse();      
//    }

    
    return this;
  }

  public UGeo triangleFan(UVertex c,UVertexList vl) {
    return triangleFan(c, vl,false);
  }

  public UGeo triangleFan(UVertex c,UVertexList vl,boolean reverse) {
    beginShape(TRIANGLE_FAN);
    vertex(c);
    vertex(vl,reverse);
    endShape();
    
    return this;
  }

  /**
   * Add triangle fan using the vertices in <code>vl</code>, with the centroid of
   * <code>vl</code> as the central vertex.
   * @param vl
   * @return
   */
  public UGeo triangleFan(UVertexList vl) {
    return triangleFan(vl,false);
  }

  /**
   * Add triangle fan using the vertices in <code>vl</code>, with the centroid of
   * <code>vl</code> as the central vertex. 
   * @param vl
   * @param reverse Flag to indicate whether vertices should be added in reverse order
   * @return
   */
  public UGeo triangleFan(UVertexList vl,boolean reverse) {
    UVertex c=vl.centroid();
//    c=UVertex.centroid(vl.toArray());
    return triangleFan(vl,c,reverse);
  }

  /**
   * Add triangle fan using the vertices in <code>vl</code>, using <code>c</code> 
   * as the central vertex.
   * @param vl
   * @param c Central vertex of the fan.
   * @return
   */
  public UGeo triangleFan(UVertexList vl,UVertex c) {
    return triangleFan(vl,c,false);
  }

  /**
   * Add triangle fan using the vertices in <code>vl</code>, using <code>c</code> 
   * as the central vertex.
   * @param vl
   * @param c Central vertex of the fan.
   * @param reverse Flag to indicate whether vertices should be added in reverse order
   * @return
   */
  public UGeo triangleFan(UVertexList vl,UVertex c,boolean reverse) {
    
    beginShape(TRIANGLE_FAN);
    vertex(c);
    vertex(vl,reverse);
    endShape();
    
    return this;

  }

  protected UGeo quadstrip(UVertexList vl, UVertexList vl2,int vID[]) {
    groupBegin(QUAD_STRIP);
    int n=vID.length/2;
    int id=0;
    UVertex v0=null,v1=null,v2=null,v3=null;

    
    taint();
    
    for(int i=1; i<n; i++) {
      
      faces.add(new UFace(this,vID[id],vID[id+2],vID[id+1]));
      faces.add(new UFace(this,vID[id+3],vID[id+1],vID[id+2]));
//      addFace(new int[] {vID[id],vID[id+2],vID[id+1]});
//      addFace(new int[] {vID[id+3],vID[id+1],vID[id+2]});
      id+=2;
    }
    groupEnd();    
    
    return this;
  }

  
  public UGeo quadstrip(UVertexList vl, UVertexList vl2) {
    if(vl.size()!=vl2.size()) {
      ArrayList<UVertexList> l=new ArrayList<UVertexList>();
            
      l.add(vl.copy());
      l.add(vl2.copy());
      for(UVertexList tmp:l) {
        log(tmp.bb().str()+" "+tmp.bb().dimBiggestPlane());
      }
      
      UBB bb=new UBB(l);
      UVertex c=bb.centroid().copy();
      c.neg();
      UVertexList.translate(l, c);
      bb=new UBB(l);
      
      int plane=bb.dimBiggestPlane();
      
      if(plane==XZ) {
        for(UVertexList tmp:l) tmp.rotX(HALF_PI);
      }
      if(plane==YZ) {
        for(UVertexList tmp:l) tmp.rotY(HALF_PI);
      }
      
      if(!l.get(0).isClockwise()) l.get(0).reverse();
      if(l.get(1).isClockwise()) l.get(1).reverse();
      UGeo tri=UPoly2Tri.triangulate(l);
          
      if(plane==XZ) {
        for(UVertexList tmp:l) tmp.rotX(-HALF_PI);
      }
      if(plane==YZ) {
        for(UVertexList tmp:l) tmp.rotY(-HALF_PI);
      }
      
      c.neg();
      UVertexList.translate(l, c);

      return add(tri);
    }
    
    
    beginShape(QUAD_STRIP);
    
    int id=0;
    for(int i=0; i<vl.size(); i++) {
      vertex(vl.get(i));
      vertex(vl2.get(i));
//      vertex(vl2.get(id++));
    }
    endShape();

//    ArrayList<UVertexList> stack=new ArrayList<UVertexList>();
//    stack.add(vl);
//    stack.add(vl2);
//    return quadstrip(stack);
    return this;
  }
  
  
  public boolean writeSTL(String filename) {
    return UGeoIO.writeSTL(filename, this);
  }

  
  public static boolean writeSTL(String filename,ArrayList<UGeo> models) {
    return UGeoIO.writeSTL(filename, models);
  }

  public String str() {return str(false);}

  public String str(boolean complete) {
    StringBuffer buf=strBufGet();
    
    buf.append(UGEO).append(TAB).append(" f="+sizeF());
    buf.append(TAB).append(" v="+sizeV());      

    if(complete) {
      buf.append(NEWLN).append(vl.str());
      
      buf.append(NEWLN).append("UFace ID\t");
      int cnt=0;
      for(UFace ff:faces) {
        if(cnt++>0) buf.append(TAB);
        buf.append(ff.vID[0]).append(TAB);
        buf.append(ff.vID[1]).append(TAB);
        buf.append(ff.vID[2]);
      }
    }
      
    
    return "["+strBufDispose(buf)+"]";
  }


  public ArrayList<String> strGroup() {
    ArrayList<String> s=new ArrayList<String>();
    for(int i=0; i<sizeGroup(); i++) {
      s.add(strGroup(i));
    }
    return s;
  }

  public String strGroup(int id) {
    return getGroup(id).str();
  }

  
  ////////////////////////////////
  // GEOMETRY PRIMITIVES
  
  
  public static UGeo box(float w,float h,float d) {
    return UGeoGenerator.box(w,h,d);
  } 
  
  public static UGeo box(float w) {
    return UGeoGenerator.box(w);
  }    
  
  public static UGeo cyl(float w,float h,int steps) {
    return UGeoGenerator.cyl(w, h, steps);
  }
  
  /**
   * Returns a new UGeo where the faces in this mesh are
   * extruded along vertex normals. If <code>makeSolid==true</code>
   * the original faces are added and quads are added to fill in
   * the side edges of the mesh boundary. 
   * @param offs
   * @param makeSolid
   * @return
   */
  public UGeo extrude(float offs,boolean makeSolid) {
    return UGeoGenerator.extrude(this, offs, makeSolid);
  }
  
  /**
   * Extrudes mesh faces and adds the new faces to this instance 
   * instead of creating a new one. 
   * @param offs
   * @param makeSolid
   * @return
   */
  public UGeo extrudeSelf(float offs,boolean makeSolid) {
    UGeo tmp=UGeoGenerator.extrude(this, offs, makeSolid,false);
    add(tmp);
    removeDupl();
    taint();
    return this;
  }

  public ArrayList<String> dataCompact() {
    return dataCompact(null);
  }

  public ArrayList<String> dataCompact(String name) {
    if(name==null) name=UGEO;
    
    ArrayList<String> s=new ArrayList<String>();
    s.add("["+name+" f="+sizeF()+" v="+sizeV()+"]");
    String str=vl.dataCompact();    
    s.add(str);
    
    StringBuffer buf=strBufGet();
    int cnt=0,n=sizeF()-1;
    
    for(UFace ff:getF()) {
      if((cnt++)>0) buf.append(',');
      buf.append(ff.vID[0]).append(',');
      buf.append(ff.vID[1]).append(',');
      buf.append(ff.vID[2]);
    }
    
    s.add("["+strBufDispose(buf)+"]");
    
    return s;
  }
  
}
