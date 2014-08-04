package unlekker.mb2.util;

import java.util.ArrayList;

import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;

//
public class UColorHSP extends UMB {
  public double R, G, B, H, S, P;

  public static final double HSP_Pr=.299;//.241;//.299;
  public static final double HSP_Pg=.587;//.691;//587;
  public static final double HSP_Pb=0.114;//0.68;//0.114

  double D1=1d/6d,D2=2d/6d,D3=3d/6d,D4=4d/6d,D5=5d/6d;
  
  double lastHSP[]=new double[] {-1,-1,-1};
  int lastRGB;

  public UColorHSP() {
  }

  public UColorHSP(int col) {
    toHSP(col);
  }

  // public domain function by Darel Rex Finley, 2006
  //
  // This function expects the passed-in values to be on a scale
  // of 0 to 1, and uses that same scale for the return values.
  //
  // See description/examples at alienryderflex.com/hsp.html

  public UColorHSP toHSP(int col) {
    R=(double)(col>>16&0xff)/255d;
    G=(double)((col>>8)&0xff)/255d;
    B=(double)((col)&0xff)/255d;

    // Calculate the Perceived brightness.
    P=Math.sqrt(R*R*HSP_Pr+G*G*HSP_Pg+B*B*HSP_Pb);

    // Calculate the Hue and Saturation. (This part works
    // the same way as in the hsp/B and HSL systems???.)
    if (equals(R, G)&&equals(R, B)) {
      H=0.;
      S=0.;
//      P=P>1 ? 1 : P;
//      log("equals "+hex(col)+" | "+ str());
      return this;
    }

    if (R>=G&&R>=B) { // R is largest
      if (B>=G) {
        H=6d/6d-D1*(B-G)/(R-G);
        S=1d-G/R;
      } else {
        H=0d/6d+D1*(G-B)/(R-B);
        S=1d-B/R;
      }
    } else if (G>=R&&G>=B) { // G is largest
      if (R>=B) {
        H=D2-D1*(R-B)/(G-B);
        S=1d-B/G;
      } else {
        H=D2+D1*(B-R)/(G-R);
        S=1d-R/G;
      }
    } else { // B is largest
      if (G>=R) {
        H=D4-D1*(G-R)/(B-R);
        S=1d-R/B;
      } else {
        H=D4+D1*(R-G)/(B-G);
        S=1d-G/B;
      }
    }

//    P=P>1 ? 1 : P;

    return this;
  }

  public ArrayList<UColorHSP> lerp(UColorHSP hsp2, int n) {
    ArrayList<UColorHSP> l=new ArrayList<UColorHSP>();
    
    for(int i=0; i<n; i++) {
      double t=map(i,0,n-1, 0,1);
      UColorHSP tmp=new UColorHSP();
      tmp.H=map(t,0,1, H,hsp2.H);
      tmp.S=map(t,0,1, S,hsp2.S);
      tmp.P=map(t,0,1, P,hsp2.P);
      l.add(tmp);
    }
    
    
    return l;
    
  }
  
  
  public UColorHSP mult(double HH,double SS,double PP) {
    H*=HH;
    S*=SS;
    P*=PP;
    
    return checkLimits();
  }

  public UColorHSP add(double HH,double SS,double PP) {
    H+=HH;
    S+=SS;
    P+=PP;
    
    return checkLimits();
  }
  
  private UColorHSP checkLimits() {
    if(H>=1) H-=Math.floor(H);
    else if(H<0) H-=Math.ceil(H);
    if(S>=1) S=1;
    else if(S<0) S=0;
    if(P>=1) P=1;
    else if(P<0) P=0;
    
    return this;
  }

  public UColorHSP set(double HH,double SS,double PP) {
    H=(HH<0 ? H : HH);
    S=(SS<0 ? S : SS);
    P=(PP<0 ? P : PP);
    
    return checkLimits();
  }
  
  public int toRGB() {
    if(this.H>=1) this.H-=Math.floor(this.H);
    else if(this.H<0) this.H-=Math.ceil(this.H);
    
    
    double H=this.H,S=this.S,P=this.P;
    
//    if(equals(lastHSP[0],H) &&
//        equals(lastHSP[1],S) &&
//        equals(lastHSP[2],P)) {
//      return lastRGB;
//    }
//    
    lastHSP[0]=H;
    lastHSP[1]=S;
    lastHSP[2]=P;
        
    
    double part, minOverMax=1d-S;
    
    if (minOverMax>0) {
      if (H<D1) { // R>G>B
        H=6d*(H-0d/6d);
        part=1d+H*(1d/minOverMax-1d);
        B=P/Math.sqrt(HSP_Pr/minOverMax/minOverMax+HSP_Pg*part*part+HSP_Pb);
        R=B/minOverMax;
        G=B+H*(R-B);
      } 
      else if (H<D2) { // G>R>B
        H=6d*(-H+D2);
        part=1d+H*(1d/minOverMax-1d);
        B=P/Math.sqrt(HSP_Pg/minOverMax/minOverMax+HSP_Pr*part*part+HSP_Pb);
        G=B/minOverMax;
        R=B+H*(G-B);
      } 
      else if (H<D3) { // G>B>R
        H=6d*(H-D2);
        part=1d+H*(1d/minOverMax-1d);
        R=P/Math.sqrt(HSP_Pg/minOverMax/minOverMax+HSP_Pb*part*part+HSP_Pr);
        G=R/minOverMax;
        B=R+H*(G-R);
      } 
      else if (H<D4) { // B>G>R
        H=6d*(-H+D4);
        part=1d+H*(1d/minOverMax-1d);
        R=P/Math.sqrt(HSP_Pb/minOverMax/minOverMax+HSP_Pg*part*part+HSP_Pr);
        B=R/minOverMax;
        G=R+H*(B-R);
      } 
      else if (H<D5) { // B>R>G
        H=6d*(H-D4);
        part=1d+H*(1d/minOverMax-1d);
        G=P/Math.sqrt(HSP_Pb/minOverMax/minOverMax+HSP_Pr*part*part+HSP_Pg);
        B=G/minOverMax;
        R=G+H*(B-G);
      } 
      else { // R>B>G
        H=6d*(-H+6d/6d);
        part=1d+H*(1d/minOverMax-1d);
        G=P/Math.sqrt(HSP_Pr/minOverMax/minOverMax+HSP_Pb*part*part+HSP_Pg);
        R=G/minOverMax;
        B=G+H*(R-G);
      }
    } else {
      if (H<D1) { // R>G>B
        H=6d*(H-0d/6d);
        R=Math.sqrt(P*P/(HSP_Pr+HSP_Pg*H*H));
        G=R*H;
        B=0.;
      } else if (H<D2) { // G>R>B
        H=6d*(-H+D2);
        R=G*H;
        G=Math.sqrt(P*P/(HSP_Pg+HSP_Pr*H*H));
        B=0.;
      } else if (H<D3) { // G>B>R
        H=6d*(H-D2);
        R=0.;
        G=Math.sqrt(P*P/(HSP_Pg+HSP_Pb*H*H));
        B=G*H;
      } else if (H<D4) { // B>G>R
        H=6d*(-H+D4);
        R=0.;
        G=B*H;
        B=Math.sqrt(P*P/(HSP_Pb+HSP_Pg*H*H));
      } else if (H<D5) { // B>R>G
        H=6d*(H-D4);
        R=B*H;
        G=0.;
        B=Math.sqrt(P*P/(HSP_Pb+HSP_Pr*H*H));
      } else { // R>B>G
        H=6d*(-H+1);
        R=Math.sqrt(P*P/(HSP_Pr+HSP_Pb*H*H));
        G=0.;
        B=R*H;
      }
    }
    
    R=min(1,R);
    G=min(1,G);
    B=min(1,B);
    
    lastRGB=pcolor((float)R*255,(float)G*255,(float)B*255);
    
    return lastRGB;
  }

  ///////////////////////////////
  // RYB
  
  protected static UVertexList RYB_WHEEL;
  
  static {
    RYB_WHEEL=new UVertexList();
    float val[]=new float[] {0, 0, 15, 8, 30, 17, 45, 26, 60, 34, 75, 41, 90,
        48, 105, 54, 120, 60, 135, 81, 150, 103, 165, 123, 180, 138, 195, 155,
        210, 171, 225, 187, 240, 204, 255, 219, 270, 234, 285, 251, 300, 267,
        315, 282, 330, 298, 345, 329, 360, 0};

    for (int i=0; i<val.length; i+=2)
      RYB_WHEEL.add(val[i], val[i+1]);

//    RYB_WHEEL.scale(1f/360f); // new range [0..1]
  };

  public UColorHSP rotateRYB(float theta) {
    float h = (float)(H * 360d);
    theta %= 360;

    float res = 0;
    for (int i = 0; i < RYB_WHEEL.size() - 1; i++) {
      UVertex p = RYB_WHEEL.get(i);
      UVertex q = RYB_WHEEL.get(i + 1);
        if (q.y < p.y) q.y += 360;

        if (p.y <= h && h <= q.y) {
            res = p.x + (q.x - p.x) * (h - p.y) / (q.y - p.y);
            break;
        }
    }

    // And the user-given angle (e.g. complement).
    res = (res + theta) % 360;

    // For the given angle, find out what hue is
    // located there on the artistic color wheel.
    for (int i = 0; i < RYB_WHEEL.size()-1; i++) {
        UVertex p = RYB_WHEEL.get(i);
        UVertex q = RYB_WHEEL.get(i + 1);
        if (q.y < p.y) q.y += 360;

        if (p.x <= res && res <= q.x) {
            h = p.y + (q.y - p.y) * (res - p.x) / (q.x - p.x);
            break;
        }
    }

    H = ((double)(h % 360) / 360.0d);
    return checkLimits();
  }

  public String str() {
    String s=strf("HSP=%.2f %.2f %.2f | RGB=%.2f %.2f %.2f : %s",
      (float)this.H,(float)this.S,(float)this.P,
      (float)R,(float)G,(float)B,
      hex(toRGB()));

    return s;
  }
  
  public static ArrayList<Integer> lerpRGB(int c1,int c2, int n) {
    UColorHSP ch1,ch2;
    ch1=new UColorHSP(c1);
    ch2=new UColorHSP(c2);
    
    ArrayList<UColorHSP> l=ch1.lerp(ch2, n);
    
    ArrayList<Integer> rl=new ArrayList<Integer>();
    for(UColorHSP cc:l) rl.add(cc.toRGB());
    
    return rl;    
  }

  
  /*

#define  Pr  .299
#define  Pg  .587
#define  Pb  .114



//  public domain function by Darel Rex Finley, 2006
//
//  This function expects the passed-in values to be on a scale
//  of 0 to 1, and uses that same scale for the return values.
//
//  See description/examples at alienryderflex.com/hsp.html

void RGBtoHSP(
double  R, double  G, double  B,
double *H, double *S, double *P) {

  //  Calculate the Perceived brightness.
  *P=sqrt(R*R*Pr+G*G*Pg+B*B*Pb);

  //  Calculate the Hue and Saturation.  (This part works
  //  the same way as in the HSV/B and HSL systems???.)
  if      (R==G && R==B) {
    *H=0.; *S=0.; return; }
  if      (R>=G && R>=B) {   //  R is largest
    if    (B>=G) {
      *H=6./6.-1./6.*(B-G)/(R-G); *S=1.-G/R; }
    else         {
      *H=0./6.+1./6.*(G-B)/(R-B); *S=1.-B/R; }}
  else if (G>=R && G>=B) {   //  G is largest
    if    (R>=B) {
      *H=2./6.-1./6.*(R-B)/(G-B); *S=1.-B/G; }
    else         {
      *H=2./6.+1./6.*(B-R)/(G-R); *S=1.-R/G; }}
  else                   {   //  B is largest
    if    (G>=R) {
      *H=4./6.-1./6.*(G-R)/(B-R); *S=1.-R/B; }
    else         {
      *H=4./6.+1./6.*(R-G)/(B-G); *S=1.-G/B; }}}



//  public domain function by Darel Rex Finley, 2006
//
//  This function expects the passed-in values to be on a scale
//  of 0 to 1, and uses that same scale for the return values.
//
//  Note that some combinations of HSP, even if in the scale
//  0-1, may return RGB values that exceed a value of 1.  For
//  example, if you pass in the HSP color 0,1,1, the result
//  will be the RGB color 2.037,0,0.
//
//  See description/examples at alienryderflex.com/hsp.html

void HSPtoRGB(
double  H, double  S, double  P,
double *R, double *G, double *B) {

  double  part, minOverMax=1.-S ;

  if (minOverMax>0.) {
    if      ( H<1./6.) {   //  R>G>B
      H= 6.*( H-0./6.); part=1.+H*(1./minOverMax-1.);
      *B=P/sqrt(Pr/minOverMax/minOverMax+Pg*part*part+Pb);
      *R=(*B)/minOverMax; *G=(*B)+H*((*R)-(*B)); }
    else if ( H<2./6.) {   //  G>R>B
      H= 6.*(-H+2./6.); part=1.+H*(1./minOverMax-1.);
      *B=P/sqrt(Pg/minOverMax/minOverMax+Pr*part*part+Pb);
      *G=(*B)/minOverMax; *R=(*B)+H*((*G)-(*B)); }
    else if ( H<3./6.) {   //  G>B>R
      H= 6.*( H-2./6.); part=1.+H*(1./minOverMax-1.);
      *R=P/sqrt(Pg/minOverMax/minOverMax+Pb*part*part+Pr);
      *G=(*R)/minOverMax; *B=(*R)+H*((*G)-(*R)); }
    else if ( H<4./6.) {   //  B>G>R
      H= 6.*(-H+4./6.); part=1.+H*(1./minOverMax-1.);
      *R=P/sqrt(Pb/minOverMax/minOverMax+Pg*part*part+Pr);
      *B=(*R)/minOverMax; *G=(*R)+H*((*B)-(*R)); }
    else if ( H<5./6.) {   //  B>R>G
      H= 6.*( H-4./6.); part=1.+H*(1./minOverMax-1.);
      *G=P/sqrt(Pb/minOverMax/minOverMax+Pr*part*part+Pg);
      *B=(*G)/minOverMax; *R=(*G)+H*((*B)-(*G)); }
    else               {   //  R>B>G
      H= 6.*(-H+6./6.); part=1.+H*(1./minOverMax-1.);
      *G=P/sqrt(Pr/minOverMax/minOverMax+Pb*part*part+Pg);
      *R=(*G)/minOverMax; *B=(*G)+H*((*R)-(*G)); }}
  else {
    if      ( H<1./6.) {   //  R>G>B
      H= 6.*( H-0./6.); *R=sqrt(P*P/(Pr+Pg*H*H)); *G=(*R)*H; *B=0.; }
    else if ( H<2./6.) {   //  G>R>B
      H= 6.*(-H+2./6.); *G=sqrt(P*P/(Pg+Pr*H*H)); *R=(*G)*H; *B=0.; }
    else if ( H<3./6.) {   //  G>B>R
      H= 6.*( H-2./6.); *G=sqrt(P*P/(Pg+Pb*H*H)); *B=(*G)*H; *R=0.; }
    else if ( H<4./6.) {   //  B>G>R
      H= 6.*(-H+4./6.); *B=sqrt(P*P/(Pb+Pg*H*H)); *G=(*B)*H; *R=0.; }
    else if ( H<5./6.) {   //  B>R>G
      H= 6.*( H-4./6.); *B=sqrt(P*P/(Pb+Pr*H*H)); *R=(*B)*H; *G=0.; }
    else               {   //  R>B>G
      H= 6.*(-H+6./6.); *R=sqrt(P*P/(Pr+Pb*H*H)); *B=(*R)*H; *G=0.; }}   */
}
