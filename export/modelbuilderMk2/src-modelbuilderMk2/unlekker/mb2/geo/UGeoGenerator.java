package unlekker.mb2.geo;

import java.util.ArrayList;
import java.util.HashMap;

import unlekker.mb2.util.UMB;
import unlekker.mb2.util.UTask;

public class UGeoGenerator extends UMB {
  static protected HashMap<String, UGeo> proto;
  static protected ArrayList<String> prototypeNames;
  
  static {
    proto=new HashMap<String, UGeo>();
    prototypeNames=new ArrayList<String>();
    
    initPlatonics();
  }
  
  public static UGeo getPrototype(String name) {
    name=name.toLowerCase();
    if(proto.containsKey(name)) {
//      logf("Prototype '%s' found.",name);
      return proto.get(name).copy();
    }
    
    return null;
  }
  
  /**
   * <p>Returns a geodesic sphere, which starts out as an
   * icosahedron that is subdivided as many times as specified by
   * <code>level</code>. All vertices are then normalized so that they
   * lie on the surface of a sphere.</p>
   * 
   * <p>The number of faces generated for levels [0..4] are 
   * respectively [20, 80, 320, 1280,5120]. Levels above 4 are
   * not recommended.</p>
   * @param diam The diameter of the resulting geometry.
   * @param level
   * @return
   */
  public static UGeo geodesicSphere(float diam,int level) {
    String key="geodesicSph"+level;
    UGeo geo=getPrototype(key);
    if(geo!=null) return geo.scaleToDim(diam);

    geo=getPrototype("icosahedron");
    
    while(level>0) {
      geo=USubdivision.subdivide(geo, UMB.SUBDIVMIDEDGES);
      for(UVertex vv:geo.getV()) vv.norm();
      level--;
    }
    
    geo.taint().check();
    addPrototype(key, geo);

    return geo.copy().scaleToDim(diam);
  }
  
  private static void initPlatonics() {
    float [] vert;
    int vID[];
    UGeo geo;
    
 // [dodecahedron.stl f=36 v=20]
    vert=new float[] {-20.7933f,-18.0276f,17.7034f,-12.8987f,-11.0452f,0.7057f,-6.1863f,-26.8468f,28.1368f,6.5874f,-15.5492f,0.6340f,-14.6365f,8.8788f,0.8111f,-27.4103f,-2.4188f,28.3139f,-16.8928f,-1.5913f,45.3050f,10.7359f,-25.3151f,17.5874f,-3.7757f,-16.6887f,45.1956f,16.8927f,1.5913f,0.6950f,3.7757f,16.6887f,0.8044f,-23.6051f,14.2103f,17.8739f,-10.7359f,25.3151f,28.4126f,-6.5874f,15.5492f,45.3660f,14.6365f,-8.8788f,45.1890f,12.8987f,11.0453f,45.2943f,23.6051f,-14.2102f,28.1261f,6.1863f,26.8468f,17.8632f,27.4103f,2.4188f,17.6861f,20.7933f,18.0276f,28.2966f};
    vID=new int[] {0,1,2,1,3,2,4,3,1,5,4,1,5,1,0,5,0,6,0,2,6,2,7,8,2,3,7,6,2,8,3,9,7,10,9,3,4,10,3,11,10,4,5,11,4,5,12,11,5,6,12,6,8,13,6,13,12,8,7,14,13,8,15,8,14,15,7,9,16,7,16,14,17,18,9,10,17,9,9,18,16,12,17,10,11,12,10,12,13,17,13,15,19,13,19,17,15,14,16,15,18,19,15,16,18,17,19,18};

    geo=new UGeo();
    for(int i=0; i<vert.length; i+=3) {
      geo.addVertex(new UVertex(vert[i],vert[i+1],vert[i+2]));
    }
    for(int i=0; i<vID.length; i+=3) {
      geo.addFace(vID[i],vID[i+2],vID[i+1]);
    }

    addPrototype("dodecahedron",geo);
    
    
    // [icosahedron.stl f=20 v=12]
    vert=new float[] {7.5381f,-15.5002f,-0.2372f,9.5297f,14.4336f,-0.1917f,27.9378f,-1.8504f,17.0122f,12.3956f,-25.1569f,27.7477f,17.3893f,-1.1913f,45.0888f,15.6180f,23.2770f,27.8213f,-17.3892f,1.1913f,-0.0888f,-15.6180f,-23.2770f,17.1787f,-12.3956f,25.1569f,17.2523f,-9.5297f,-14.4336f,45.1917f,-7.5381f,15.5002f,45.2372f,-27.9378f,1.8504f,27.9878f};
    vID=new int[] {0,1,2,0,2,3,3,2,4,5,4,2,1,5,2,6,0,7,7,0,3,6,1,0,6,8,1,8,5,1,7,3,9,9,3,4,9,4,10,10,4,5,8,10,5,11,6,7,11,8,6,11,7,9,11,10,8,11,9,10};
    
    geo=new UGeo();
    for(int i=0; i<vert.length; i+=3) {
      geo.addVertex(new UVertex(vert[i],vert[i+1],vert[i+2]));
    }
    for(int i=0; i<vID.length; i+=3) {
      geo.addFace(vID[i],vID[i+2],vID[i+1]);
    }

    addPrototype("icosahedron",geo);

    
    
    // [octahedron.stl f=8 v=6]
    vert=new float[] {11.5470f,-20f,0f,11.5470f,20f,0f,23.0940f,0f,32.6598f,-11.5470f,20f,32.6598f,-23.0940f,0f,0f,-11.5470f,-20f,32.6598f};
    vID=new int[] {0,1,2,3,2,1,4,3,1,4,1,0,5,0,2,4,0,5,5,2,3,4,5,3};
    
    geo=new UGeo();
    for(int i=0; i<vert.length; i+=3) {
      geo.addVertex(new UVertex(vert[i],vert[i+1],vert[i+2]));
    }
    for(int i=0; i<vID.length; i+=3) {
      geo.addFace(vID[i],vID[i+2],vID[i+1]);
    }

    addPrototype("octahedron",geo);
    
    // [tetrahedron.stl f=4 v=4]
    vert=new float[] {-17.2510f,-30.0620f,0.0064f,-17.4484f,29.9377f,-0.0182f,34.6114f,0.1089f,-0.1305f,0.0880f,0.0153f,48.9423f};
    vID=new int[] {0,1,2,3,2,1,0,3,1,0,2,3};

    geo=new UGeo();
    for(int i=0; i<vert.length; i+=3) {
      geo.addVertex(new UVertex(vert[i],vert[i+1],vert[i+2]));
    }
    for(int i=0; i<vID.length; i+=3) {
      geo.addFace(vID[i],vID[i+2],vID[i+1]);
    }

    addPrototype("tetrahedron",geo);

  }

  public static boolean hasPrototype(String name) {
    return proto.containsKey(name.toLowerCase());    
  }

  public static void addPrototype(String name, UGeo model) {
    if(hasPrototype(name)) {
      logf("Already contains prototype '%s'",name);
    }
    else {
      prototypeNames.add(name);
      proto.put(name.toLowerCase(), model.center().scaleToDim(100));
    }
  }
  
  /**
   * Returns a list of the names prototype geometries currently 
   * available. The platonic solids can be found using "dodecahedron",
   * "icosahedron","octahedron" and "tetrahedron". (Yes, cube has been omitted.)
   * @return
   */
  static public ArrayList<String> listPrototypes() {
    return prototypeNames;
  }

  public static UGeo sphere(float diam,int res) {
    String key="sph"+res;
    
    UGeo geo=getPrototype(key);
    if(geo!=null) return geo.scaleToDim(diam);
    
    geo=new UGeo();
    UVertex top,bottom;
    
    UVertexList l=UVertexList.arc(1, -HALF_PI, HALF_PI, res);
    
    l.clear();
    
    
    float ext=1f-1f/(float)((res-1));
    for(int i=0; i<res; i++) {
      float t=map(i,0,res-1,-ext,ext);
      
      float x=circleXforY(t);
      log(nf(t)+" "+nf(x)+" "+nf(RAD_TO_DEG*Math.asin(t)));
      l.add(x,t);
    }

    top=new UVertex(0,-1,0);
    bottom=new UVertex(0,1,0);
//    top=l.first();
//    bottom=l.last();
//    
//    l.remove(0).remove(l.size()-1);
    
    ArrayList<UVertexList> stack=new ArrayList<UVertexList>();
    for(int i=0; i<res; i++) {
      float t=map(i,0,res, 0,TWO_PI);
      stack.add(l.copy().rotY(-t));
    }
    stack.add(l.copy());
    
    geo.quadstrip(stack);
    geo.triangleFan(UVertexList.crossSection(0, stack),top);
    geo.triangleFan(UVertexList.crossSection(
        stack.get(0).size()-1, stack),bottom,true);
    
    addPrototype(key, geo);
    return geo.copy().scaleToDim(diam);
  }
  
  public static UGeo meshPlane(float w,float h,int steps) {
    UGeo geo=new UGeo();
    UVertexList ll=UVertexList.line(w,steps);
    ArrayList<UVertexList> meshl=new ArrayList<UVertexList>();
    
    for(int i=0; i<steps; i++) {
      float t=map(i,0,steps-1,-0.5f,0.5f);
      meshl.add(ll.copy().translate(0,t*h));
    }
    return geo.quadstrip(meshl);
  }

  public static UGeo meshBox(float w,float h,float d,int steps) {
    w*=0.5f;
    h*=0.5f;
    d*=0.5f;

    UGeo geo=new UGeo();
    UVertexList ll=UVertexList.line(100,steps);
    ArrayList<UVertexList> meshl=new ArrayList<UVertexList>();

    for(int i=0; i<steps; i++) {
      float t=map(i,0,steps-1,-1,1);
      meshl.add(ll.copy().translate(0,t*50,-50));
    }

    
    for(int i=1; i<steps; i++) {
      UVertexList vl=meshl.get(i).copy().rotX(HALF_PI);
      meshl.add(vl);
    }

    for(int i=1; i<steps; i++) {
      UVertexList vl=meshl.get(i).copy().rotX(PI);
      meshl.add(vl);
    }

    for(int i=1; i<steps; i++) {
      UVertexList vl=meshl.get(i).copy().rotX(HALF_PI*3);
      meshl.add(vl);
    }

    UVertexList.scale(meshl, w/50f,h/50f,d/50f);
    
    geo.quadstrip(meshl);
//    log(geo.bb().str());
    
    meshl.clear();
    ll=UVertexList.line(d*2,steps).rotY(HALF_PI);
    for(int i=0; i<steps; i++) {
      float t=map(i,0,steps-1,-1,1);
      meshl.add(ll.copy().translate(w,t*h,0));
    }
    geo.quadstrip(meshl);
    
    meshl.clear();
    ll.scale(-1,1,1);
    for(int i=0; i<steps; i++) {
      float t=map(i,0,steps-1,1,-1);
      meshl.add(ll.copy().translate(-w,t*h,0));
    }
    geo.quadstrip(meshl);
//    log(geo.bb().str());
    
    return geo;
  }

  public static UGeo extrude(UGeo geo,float offs,boolean makeSolid) {
    return extrude(geo,offs,makeSolid,true);
  }

  public static UGeo extrude(UGeo geo,float offs,boolean makeSolid,boolean addOriginal) {
    boolean force=false;
    int cnt=0,n=0;
    UVertex vn=new UVertex();

    geo.check();

    if(addOriginal) offs*=0.5f;
    
    UTask task=new UTask("extrude - "+geo.str(),force);

    if(addOriginal) {
      
      UGeo g2=geo.copy();
      cnt=0;
      for(UVertex tmp:g2.getV()) {
        vn.set(g2.getVNormal(cnt++)).mult(offs);
        tmp.add(vn);
      }
      geo=g2;
    }

    UGeo extr=geo.copy();
    n=extr.sizeF()/10;
    cnt=0;
    n=max(3,extr.sizeF()/10);
    for(UFace tmp:extr.getF()) {
      tmp.reverse();
      if((cnt++)%n==0) 
        task.update(map(cnt,0,extr.sizeF()-1,0,25),"reverse",force);
    }
    
//    extr.vertexNormals();
//    task.update(40,"vertexNormals",force);

    cnt=0;
    n=max(3,extr.sizeV()/10);
    
    for(UVertex tmp:extr.getV()) {
      vn.set(extr.getVNormal(cnt++)).mult(offs);
      tmp.add(vn);
      if(cnt%n==0) 
        task.update(map(cnt,0,extr.sizeV()-1,25,40),"offset vertex",force);
    }

    
    if(makeSolid) {
      task.update(40,"boundary - before | "+geo.str(),force);
      UEdgeList border=geo.getEdgeList();
      task.update(40,"boundary - edgelist",force);
      
      border=border.getBoundary();
      task.update(40,"boundary",force);
      
      int last=-1;
      
      UVertex v1=new UVertex(),v2=new UVertex(),
          v3=new UVertex(),v4=new UVertex();
      UVertex tmp=new UVertex();
      
 
      extr.beginShape(QUADS);
      
      cnt=0;
      n=max(3,border.size()/10);
      
      
      for(UEdge ed:border) {
        v1.set(ed.v[1]);
        v2.set(ed.v[0]);
        
//        if(v2.x>v1.x) {
//          UVertex tmpv=v1;
//          v1=v2;
//          v2=tmpv;
//        }
        
        int id1=geo.getVID(v1);
        int id2=geo.getVID(v2);
        
        if(id1>-1 && id2>-1) {
          v3.set(extr.getV(id2));
          v4.set(extr.getV(id1));
//          tmp.set(geo.getVNormal(id2)).mult(-offs);
//          v3.set(v2).add(tmp);
//
//          tmp.set(geo.getVNormal(id1)).mult(-offs);
//          v4.set(v1).add(tmp);

          extr.vertex(v1);
          extr.vertex(v2);
          extr.vertex(v3);
          extr.vertex(v4);          
        }
        else {
          logf("Extrude: No ID found for %s and %s",
              ed.v[0].str(),ed.v[1].str());
        }
        if((cnt++)%n==0) task.update(map(cnt,0,border.size()-1,50,90),"extruding edges.",force);

      }
      extr.endShape();
      
    }
    
    if(addOriginal) {
      task.update(91,"Final: add to geo",force);
    extr.add(geo);
      task.update(100,"added to geo",force);        
    }
    task.update(99,"RemoveDuplV",force);

    extr.removeDuplV();
    task.done();
    
    return extr;
    
  }

  public static UGeo box(float w,float h,float d) {
    return box(1).scale(w,h,d);
  } 
  
  public static UGeo box(float w) {
    UGeo geo=null;
    
    w*=0.5f;
    UVertexList vl,vl2;
    
    vl=UVertexList.circle(w, 4).rotZ(HALF_PI*0.5f).reverse();
//    vl=new UVertexList();
//    vl=new UVertexList().add(-w,-w).add(w,-w).
//        add(w,w).add(-w,w);
    vl2=vl.copy().translate(0,0,w);
    vl.translate(0,0,-w);
    
    log(vl.str());
    log(vl2.str());
    geo=new UGeo();
    geo.beginShape(QUAD_STRIP).
    vertex(vl.get(3)).vertex(vl.get(0)).
    vertex(vl.get(2)).vertex(vl.get(1)).endShape();
    geo.beginShape(QUAD_STRIP).
      vertex(vl2.get(2)).vertex(vl2.get(1)).
      vertex(vl2.get(3)).vertex(vl2.get(0)).endShape();
    geo.quadstrip(vl.close(),vl2.close());
  
    return geo;
//    return geo.groupJoinAll();
  }
  
  public static UGeo cyl(float w,float h,int steps) {
    w*=0.5f;
    h*=0.5f;
    
    UGeo geo=null;
    UVertexList vl,vl2;
//    vl=new UVertexList();
//    for(int i=0; i<steps; i++) {
//      float deg=map(i,0,steps,0,TWO_PI);
//      vl.add(new UVertex(w,0).rotY(deg));
//    }
//    
//    vl2=vl.copy().translate(0,h,0).close();
//    vl.translate(0,-h,0).close();
//    geo=new UGeo().quadstrip(vl,vl2).
//        triangleFan(vl,true)
//        .triangleFan(vl2);
    
    vl=UVertexList.circle(w, steps).rotX(-HALF_PI);
    vl2=vl.copy().translate(0,-h,0);
    vl.translate(0,h,0);
    
    
    geo=new UGeo().triangleFan(vl,true).triangleFan(vl2);
    geo.quadstrip(vl,vl2);

    return geo;//geo.groupJoinAll();
  }

  public static UGeo roundedBox(float w,float h,float d,float r,int steps) {
    return null;
  }
  
  public static UVertexList roundedProfile(float h,float r,int steps) {
    UVertexList vl=new UVertexList();
    UVertexList arc=new UVertexList();
    for(int i=0; i<steps; i++) {
      arc.add(new UVertex(0,r,0).
          rotZ(map(i,0,steps-1,0,HALF_PI)));
    }
    arc.translate(0,h-r,0);
    
    for(int i=0; i<steps; i++) {
      int id=(steps-1)-i;
      arc.add(arc.get(id).copy().mult(1,-1,1));
    }
    
    
    
    
    return arc;
  }
}
