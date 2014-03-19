/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.util.UColorHSP;
import unlekker.mb2.util.UMB;

public class UTestHSP extends UTest {
  ArrayList<HSPNode> n;
  
  ArrayList<UColorHSP> hsp;
  ArrayList<UColorHSP> pal;
  int palRGB[];
  
  int gridSize=250;
  float Wgrid=500;
  float W=2;//Wgrid/(float)gridSize;
  double T=0;
  
  boolean doUpdate;
  int cnt=0;
  
  HSPNode rot;
  HSPNode test[];
  
  public UTestHSP() {
    super();
  }
  
  public void init() {
    n=new ArrayList<UTestHSP.HSPNode>();
    hsp=new ArrayList<UColorHSP>();
    
    for(double i=0; i<51; i++) {
      hsp.add(new UColorHSP().set(i/50d, rnd(0.5f,1), rnd(0.5f,1)));
    }

    for(int x=0; x<gridSize; x++) {
      for(int y=0; y<gridSize; y++) {
        HSPNode nn=new HSPNode(x, y);
        n.add(nn);
      }
    }
    
    
    rot=new HSPNode(gridSize/2,gridSize/2);
    rot.hsp.H=0.5;
    rot.v.set(p.width-100,100);
    
    
    test=new HSPNode[1000];
    for(int i=0; i<test.length; i++) {
      double t=map(i,0,test.length-1,0,1);
      test[i]=new HSPNode(gridSize-1,gridSize-1);
      test[i].v.set((float)(t*1000d),p.height-60);
      test[i].hsp.H=t;//set(T,1,1);
      test[i].hsp.P=1;//set(T,1,1);
//      if(i%20==0 || i==test.length-1) log(i+"\ttest[i].hsp.H "+test[i].hsp.H);
    }
  }
  
  
  
  public void draw() {
    double PP=p.map(p.mouseX, 0, p.width-1, 0,1);
    
    if(doUpdate) {
      cnt+=20*PP+1;
      T=(double)(cnt%1000)/999d;
      if(T>0) T-=Math.floor(T);
    }
    
    p.background(100);
    
    p.pushMatrix();
    p.translate(20,20);
    p.noStroke();

    double SS=min(0.99999f,map(p.mouseY,0,p.height-1,0,1));
    
    double tmpH=rot.hsp.H;
    rot.hsp.S=SS;
    rot.hsp.P=T;
    
    p.fill(255);
    p.text(rot.hsp.str(), p.width-200,rot.v.y-20);
    
    UVertex vv=rot.v.copy();
    
    double tmp[]=new double[] {0,0,0};
    for(UColorHSP chsp:hsp) {
      tmp[1]=chsp.S;
      tmp[2]=chsp.P;
      
      chsp.S*=SS;
      chsp.P*=PP;
      p.fill(chsp.toRGB());
      p.rect(vv.x,vv.y, 50,3);
      vv.y+=4;
      
      chsp.S=tmp[1];
      chsp.P=tmp[2];


//      rot.hsp.rotateRYB(72);
    }
    
    rot.hsp.H=tmpH;
    
    for(HSPNode node : n) {
      node.hsp.H=T;
      node.hsp2.S=T;
      
      node.draw();
    }
    
    p.fill(255);
    p.rect((float)T*Wgrid, Wgrid, 1, 10);
    
    p.popMatrix();

    if(pal!=null) {
      float y=200;
      
      p.textAlign(RIGHT);
      
      int cnt=0;
      for(UColorHSP cc:pal) {
//        cc.
        p.fill(cc.toRGB());
        p.rect(p.width-200,y,24,12);
        
        float t=map(cnt,0,pal.size()-1, 0,1);
        p.fill(p.lerpColor(palRGB[0], palRGB[1], t));
        p.rect(p.width-175,y,10,12);
        
        p.fill(p.lerpColor(pal.get(0).toRGB(),pal.get(pal.size()-1).toRGB(), t));
        p.rect(p.width-160,y,10,12);
        
        if(cnt==0) p.fill(palRGB[0]);
        else if(cnt==pal.size()-1) p.fill(palRGB[1]);
        if(cnt==0 || cnt==pal.size()-1) p.rect(p.width-145,y,10,12);
        
        p.fill(255);
        p.text(cc.str(),p.width-205, y+10);
        
        cnt++;
        y+=13;
      }
      
      p.textAlign(LEFT);

    }
    
    
    for(int i=0; i<test.length; i++) {
      double Htmp=test[i].hsp.H;
      double H=Htmp+T;
      if(H>1) H-=1d;
      test[i].hsp.H=H;
      if(!p.mousePressed) test[i].hsp.S=SS;

      p.fill(test[i].hsp.toRGB());
      p.rect(20+i,test[i].v.y, 1,10);
//      if(i%20==0 || i==test.length-1) log(i+"\ttest[i].hsp.H "+test[i].hsp.H);
      test[i].hsp.H=Htmp;
      
    }
    p.fill(255);
    p.text(strf("H=%.3f D%d SS=%.3f",
        (float)T,(int)(T*6),(float)SS), 
        10,p.height-10);
    p.text(strf("RGB=%s",hex(p.get(p.mouseX, p.mouseY))),200,p.height-10);
  }
  
  public void keyPressed(char key) {
    if(key=='A') {
      for(UColorHSP chsp:hsp) {
        chsp.add(0.1,0,0);
//        chsp.rotateRYB(rndInt(15,31));
      }
    }
    else if(key=='a') {
      for(UColorHSP chsp:hsp) {
//        chsp.add(rndSigned(5,10)/100d,rndSigned(5,10)/100d,rndSigned(5,10)/100d);
        chsp.rotateRYB(rndSigned(5,10));
      }
    }
    else if(key=='n') {
      palRGB=new int[] {
          p.color(p.color(rnd(255f),rnd((float)(255f)),rnd(255f))),
          p.color(p.color(rnd(200,255f),rnd(200,255f),rnd(200,255f)))
//          p.color(p.color(rnd((float)(255f*T))),rnd((float)(255f*T)),rnd((float)(255f*T))),
//          p.color(p.color(rnd((float)(255f*T))),rnd((float)(255f*T)),rnd((float)(255f*T)))
      };
      UColorHSP ch1=new UColorHSP(palRGB[0]);
      UColorHSP ch2=new UColorHSP(palRGB[1]);
      log(hex(palRGB[0])+" "+ch1.str());
      log(hex(palRGB[1])+" "+ch2.str());
      
      pal=ch1.lerp(ch2, 20);
    }
    else doUpdate=!doUpdate;
  }

  class HSPNode {
    UColorHSP hsp;
    UColorHSP hsp2;
    UVertex v;
    int col;
    
    public HSPNode(float x,float y) {
      v=new UVertex(x,y).div(gridSize);
      v.x=x/(float)(gridSize);
      v.y=y/(float)(gridSize);
      
      hsp=new UColorHSP(p.color(0));
      hsp.S=min(0.99999f,v.y);
      hsp.P=v.x;
      hsp.H=0;

      hsp2=new UColorHSP(p.color(255));
      hsp2.P=1;
      hsp2.H=v.x;
      hsp2.S=min(0.99999f,v.y);

      v.mult(Wgrid);
    }
    
    public void draw() {
      int c;
//      c=p.mousePressed ? hsp2.toRGB() : hsp.toRGB();
      c=p.mousePressed ? p.color((int)(255f*hsp2.H*hsp2.S)) : hsp.toRGB();
      if(p.alpha(c)<255) log("a "+p.alpha(c));
      p.fill(c);
      p.rect((int)v.x,(int)v.y,W,W);
    }
  }
}
