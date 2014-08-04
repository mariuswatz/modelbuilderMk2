package unlekker.mb2.geo;

import unlekker.mb2.util.UMB;

public class UCurve extends UMB {
  public UVertexList input,output;
  
  
  public UCurve set(UVertexList input) {
    this.input=input;
    return this;
  }

  
/*  public float point(float t) {
    return t;
  }
  
  public UVertexList calc(int n) {
    if(output==null) output=new UVertexList();
    else output.clear();
    
    return output;
  }
  
  public UVertexList catmullRom(int n) {
    float prec=1f/(float)n;
    UVertex p0,p1,p2,p3;

    int ptn=input.size();
    p0=input.last();
    
    for(int i=0; i<ptn; i++) {
      
    }
    
    return output;
  }*/

  public static float bezier(float t, float a, float b, float c, float d) {
    float t1 = 1.0f - t;
    float t1sq=t1*t1,tsq=t*t;
    return a*t1sq*t1 + 3*b*t*t1sq + 3*c*tsq*t1 + d*t*tsq;
  }

  
  public static UVertexList bezier(UVertexList cp,int n) {
    UVertexList res=new UVertexList();
    int nseg=(cp.size()-1)/3;
    
    for(int i=0; i<nseg; i++) {
      UVertexList tmp=bezier(cp.extractArray(i*3,i*3+3),n);
      if(i<nseg-1) tmp.remove(tmp.size()-1);
      res.add(tmp);
    }
    
    return res;
  }

  public static UVertexList bezier(UVertex v[],int n) {
    UVertexList l=new UVertexList();
    
    for(int i=0; i<n; i++) {
      float t=map(i,0,n-1,0,1);
      float t1 = 1.0f - t;      
      float t1sq=t1*t1,tsq=t*t;
      float c1=t1sq*t1;
      float c2=3*t*t1sq;
      float c3=3*tsq*t1;
      float c4=t*tsq;
      
      UVertex vv=new UVertex(
          v[0].x*c1+v[1].x*c2+v[2].x*c3+v[3].x*c4,
          v[0].y*c1+v[1].y*c2+v[2].y*c3+v[3].y*c4,
          v[0].z*c1+v[1].z*c2+v[2].z*c3+v[3].z*c4
      );
      l.add(vv);
    }
    
    return l; 
  }


  public static UVertex catmullRom(float t, UVertex p0, UVertex p1, UVertex p2, UVertex p3)  {
    UVertex vv=new UVertex(
        0.5f * ((          2*p1.x) +
            t * (( -p0.x           +p2.x) +
            t * ((2*p0.x -5*p1.x +4*p2.x -p3.x) +
            t * (  -p0.x +3 * p1.x -3 * p2.x +p3.x)))),

            0.5f * ((          2*p1.y) +
            t * (( -p0.y           +p2.y) +
            t * ((2*p0.y -5*p1.y +4*p2.y -p3.y) +
            t * (  -p0.y +3 * p1.y -3 * p2.y +p3.y)))),

            0.5f * ((          2*p1.z) +
            t * (( -p0.z           +p2.z) +
            t * ((2*p0.z -5*p1.z +4*p2.z -p3.z) +
            t * (  -p0.z +3 * p1.z -3 * p2.z +p3.z))))  
        );
    return vv;    
  }

  public static float catmullRom(float t, float p0, float p1, float p2, float p3)  {
    float tsq=t*t;
    return 0.5f * (
        (p1+ p1) +
        (-p0 + p2) * t +
        (p0 + p0 - (p1+p1+p1+p1+p1) + (p2+p2+p2+p2) - p3) * tsq +
        (-p0 + (p1+p1+p1) - (p2+p2+p2) + p3) * t * tsq
        );
   }
}
