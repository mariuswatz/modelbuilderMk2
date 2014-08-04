package unlekker.mb2.geo;

import java.util.ArrayList;

import unlekker.mb2.util.*;

/**
 * Calculates and draws a Bezier patch surface. Direct port of sample code by Paul Bourke. 
 * Original code: http://paulbourke.net/geometry/bezier/
 *
 * @author marius
 *
 */
public class UBezierPatch extends UMB {
  public UVertexList vl,result;
  public int numSeg,nu,nv,resu,resv;
  public UVertex cp[][];
  public UGeo geo;

  
  public UBezierPatch(ArrayList<UVertexList> input) {
    nv=input.get(0).size()-1;
    nu=input.size()-1;
    cp=new UVertex[nv+1][nu+1];
    
    for(int i=0; i<nv+1; i++) {
      UVertexList tmp=input.get(i);
      for(int j=0; j<nu+1; j++) cp[i][j]=tmp.get(j);
    }
  }

  public UBezierPatch(UVertex _cp[][]) {
    set(_cp);
  }
  
  public void set(UVertex _cp[][]) {
    cp=_cp;
    nu=cp.length-1;
    nv=cp[0].length-1; 
  }
  
  public UGeo eval(int _resu,int _resv) {
    ArrayList<UVertexList> surf;
    UVertexList vl;
    double mui, muj, bi, bj;

    surf=new ArrayList<UVertexList>();
    
    resv=_resv;
    resu=_resu;
    
    for(int i=0; i<resu; i++) {
      mui = i / (double)(resu-1);
      vl=new UVertexList();
      for(int j=0; j<resv; j++) {
        muj = j / (double)(resv-1);       
        UVertex vv=new UVertex();
        
        for (int ki=0;ki<=nu;ki++) {
          bi = BezierBlend(ki, mui, nu);
          for (int kj=0;kj<=nv;kj++) {
            bj = BezierBlend(kj, muj, nv);
            vv.add(
                (float)(cp[ki][kj].x * bi * bj),
                (float)(cp[ki][kj].y * bi * bj),
                (float)(cp[ki][kj].z * bi * bj));
          }
        }
        vl.add(vv);
      }

      surf.add(vl);
    }

    if(geo==null) geo=new UGeo();
    else geo.clear();
    geo.quadstrip(surf);
    
    return geo;
  }
  
  private double BezierBlend(int k, double mu, int n) {
    int nn, kn, nkn;
    double blend=1;

    nn = n;
    kn = k;
    nkn = n - k;

    while (nn >= 1) {
      blend *= nn;
      nn--;
      if (kn > 1) {
        blend /= (double)kn;
        kn--;
      }
      if (nkn > 1) {
        blend /= (double)nkn;
        nkn--;
      }
    }
    if (k > 0)
      blend *= Math.pow(mu, (double)k);
    if (n-k > 0)
      blend *= Math.pow(1-mu, (double)(n-k));

    return(blend);
  }

}
