package unlekker.mb2.util;

import java.util.ArrayList;

public class UColor extends UMB {
  public ArrayList<UColorGradient> gradient;
  public ArrayList<Integer> col;
  
  public int[] pal;
  
  public UColor() {
    gradient=new ArrayList<UColor.UColorGradient>();
  }
  
  public UColor set(int[] pal) {
    if(col==null) col=new ArrayList<Integer>();
    else col.clear();

    for(int cc:pal) {
      col.add(cc);
//      log(hex(cc));
    }
    return this;
  }
  
  public UColor draw(int w) {
    int x=0;
    for(int cc:col) {
      papplet.fill(cc);
      prect((x++)*w,0,w-1,w-1);
    }
    return this;
  }

  public UColor add(String hex1, String hex2) {
    return add(pcolor(hex1),pcolor(hex2));
  }

  public UColor add(int c1,int c2) {
    gradient.add(new UColorGradient(c1, c2));
    return this;
  }

  public int rndColor() {
    if(col!=null && col.size()>1) {
      return col.get(rndIndex(col));
    }
    
    if(gradient==null) return pcolor(0,255,0);
    
    return gradient.get(rndIndex(gradient)).rnd();
  }

  public UColor generate(int n) {
    if(col==null) col=new ArrayList<Integer>();
    else col.clear();
    
    while(col.size()<n) {
      int cc=gradient.get(rndIndex(gradient)).rnd();
      col.add(cc);
    }
    
    return this;
  }
  
  public class UColorGradient {
    public int c[],range[];
    public String hex[];
    
    public UColorGradient(int c1,int c2) {
      set(c1,c2);
    }

    public int get(float t) {
      return lerpColor(c[0], c[1],t);
    }

    public int rnd() {
      float t=UMB.rnd(1000)/1000f;      
      return get(t);
    }

    public int[] range(int n) {
      int[] res=new int[n];
      for(int i=0; i<n; i++) {
        res[i]=get(map(i,0,n-1,0,1));
      }
      
      range=res;
      return res;
    }

    public UColorGradient set(String hex1, String hex2) {
      c=new int[] {pcolor(hex1),pcolor(hex2)};
      hex=new String[] {hex1,hex2};
      
      return this;
    }

    public  UColorGradient set(int c1, int c2) {
      c=new int[] {c1,c2};
      hex=new String[] {hex(c1),hex(c2)};
      
      return this;
    }
    
  }
}
