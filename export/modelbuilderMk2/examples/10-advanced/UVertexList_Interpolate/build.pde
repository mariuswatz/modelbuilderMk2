
void build() {  
  vvl=new ArrayList<UVertexList>();

  UVertexList vl=new UVertexList();
  UVertexList vl2=new UVertexList();
  UVertexList vl3=new UVertexList();
  
  // create 3 circular vertex lists and fill them random 
  // radial offset vertices
  int n=(int)random(24,60)*2;
  float r=1; // radius multiplier
  
  for(int i=0; i<n; i++) {
    float deg=map(i,0,n,0,TWO_PI);
    
    // dampen r with fraction of last value
    r=1+abs(sin(map(i,0,n, 0,12*PI)))*0.25f;
    vl.add(new UVertex(r,0).rotY(deg)); 
  }

  vl2=vl.copy();
  vl3=vl.copy();
  
  // scale the three lists randomly
  float w=60;
  float h=600; 
  
  vl.scale(w*(float)((int)random(2,9))*0.25);
  vl2.scale(w*(float)((int)random(2,9))*0.25);  
  vl3.scale(w*(float)((int)random(2,9))*0.25);

  // vl2 is translated to a random fraction of "h"
  vl2.translate(0,random(0.4,0.6)*h,0);
  
  // vl3 is translated to "h" in the Y axis
  vl3.translate(0,h,0);

  // rotate vl2 and vl3 for a cheap "twist" effect
  vl2.rotY(random(0.2,0.4)*PI);
  vl3.rotY(random(0.6,0.8)*PI);
  
  // fill ArrayList vvl with interpolated versions of
  // the three vertex lists
 
  n=20; // # of interpolated edges
  
  // interpolate vl to vl2
  for(int i=0; i<n; i++) {
    float t=map(i,0,n,0,1);
    
    // optional: use a shaper to modify t
    //  t=(1-sin(sq(1-t)*HALF_PI))*0.9+t*0.1;
    UVertexList tmp=vl.lerp(t,vl,vl2);

    // close the tmp vertex list and add it to vvl 
    vvl.add(tmp.close());
    vl.log(vvl.get(vvl.size()-1).str());
  }
  
  // interpolate vl2 to vl3
  for(int i=0; i<n; i++) {
    float t=map(i,0,n,0,1);
    // optional: use a shaper to modify t
    //    t=sin(t*t*HALF_PI)*0.9+t*0.1;
    
    UVertexList tmp=vl2.lerp(t,vl2,vl3);
    vvl.add(tmp.close());
    vl.log(vvl.get(vvl.size()-1).str());
  }

  geo=new UGeo().quadstrip(vvl);
  
  // get centroid to center arraylist
  UVertex c=geo.bb().centroid;
  
  // center model and arraylist
  geo.center();  
  for(UVertexList tmp : vvl) tmp.translateNeg(c);

}
