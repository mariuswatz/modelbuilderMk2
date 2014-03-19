/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.awt.Frame;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.UFile;
import unlekker.mb2.util.UMB;
import unlekker.mb2.util.UTask;

public class UTestSmooth02 extends UTest {
  ArrayList<UVertexList> stack,stackNoSmooth,stackSmooth;
  UVertexList vl,vlNoSmooth,vlSmooth;
  UGeo geo,geoSmooth;
  int smoothLevel;
  
  String buildStr="";
  
  boolean nodupl=true;
  int doInit=2;
  
  UVertex a,b,ii;
  float R=400;
  
  int drawType=0;
  
  public void init() {
    if(main.nav==null) main.nav=new UNav3D();
    doInit=2;
  }

  private void doBuild() {
    doInit=0;
    
    UTask task=new UTask("doBuild");
    
    vl=new UVertexList();
    int n=rndInt(9,37)*2;
    n=15;
    
    float m=UMB.rnd(100,300);
    for(int i=0; i<n; i++) {
      m=m*0.2f+UMB.rnd(100,300)*0.8f;
      float t=-UMB.map(i, 0, n, 0, TWO_PI);
      vl.add(new UVertex(m,0).rotY(t));
    }

    task.addLog("vl");
    
//    exportPDF();
    vlNoSmooth=vl.copy().close();

    vl=UVertexList.smooth(vl.close(), 2);

    stack=new ArrayList<UVertexList>();
    stackNoSmooth=new ArrayList<UVertexList>();
    
    n=16;
    m=p.random(0.25f,0.5f);
    for(int i=0; i<n; i++) {
      m=m*0.2f+p.random(0.5f,2f)*0.8f;
      if(i==n-1) m=p.random(0.25f,0.5f);
      
      float y=UMB.map(i, 0, n-1, 0,1000);
      stack.add(vl.copy().scale(m).translate(0,y,0));
      stackNoSmooth.add(
          vlNoSmooth.copy().scale(m).translate(0,y,0));
    }
    
    task.addLog("vl stacks");

    UVertexList.center(stack);    
    UVertexList.center(stackNoSmooth);
    
    float dim=new UBB(stack).dimMax();
    dim=(float)p.height/dim;
    UVertexList.scale(stack, dim);
    UVertexList.scale(stackNoSmooth, dim);
    
    stackSmooth=UVertexList.smooth(stack,2);
    
    task.addLog("stackSmooth");

    logDivider("pre geo");

    geo=new UGeo().setOptions((nodupl ? NODUPL : 0));
    geo.quadstrip(stack);    

    if(UVertex.eqTask!=null) log(UVertex.eqTask.strData());
    log(NEWLN).logDivider("pre geoSmooth");

    task.addLog("geo 1 | "+geo.str());

    
    log(NEWLN).logDivider("NODUPL");
    geoSmooth=new UGeo().setOptions((nodupl ? NODUPL : 0));
    geoSmooth.quadstrip(stackSmooth);    
    log("geo: "+geoSmooth.str());
    if(UVertex.eqTask!=null) log(UVertex.eqTask.strData());

    task.addLog("geo 2 | "+geoSmooth.str()+" "+geoSmooth.optionStr());
    
//    log(NEWLN).logDivider("NODUPL=false");
//    if(UVertex.eqTask!=null) UVertex.eqTask.clear();
//    geoSmooth=new UGeo().setOptions(0);
//    geoSmooth.quadstrip(stackSmooth);
//    log("geo: "+geoSmooth.str());
//    geoSmooth.removeDupl();
//    task.addLog("geo removeDupl");
//    
//    task.addLog("geo 3 | "+geoSmooth.str());

    task.done();
    logDivider("\n");
    log(task.log);
    
    buildStr="Build time: "+task.elapsed()+"|"+geoSmooth.str();
  }

  private void exportPDF() {
    PGraphics pdf=p.createGraphics(1000, 1000,p.PDF,this.getClass().getSimpleName()+".pdf");
    UMB.setGraphics(pdf);
    pdf.beginDraw();
    pdf.stroke(0);
    pdf.translate(vl.bb().dimX(), vl.bb().dimZ());
    vl.copy().rotX(HALF_PI).draw();
    
    pdf.endDraw();
    pdf.flush();
    pdf.dispose();

    UMB.setGraphics(p);
  }
  
  public void draw() {
    main.stroke(255,200,0);
//    main.drawGrid();
    p.stroke(0);
    p.noStroke();

    if(geo==null) {
      p.fill(255);
      p.text("build",p.width/2,p.height/2);
    }
    else drawModel();

    if(doInit==1) doBuild();    
    doInit--;
    
    if(main.saveFilename!=null) export();
  }

  private void export() {
    if(geo!=null) {
      log("main.saveFilename "+main.saveFilename);
      
      String fname=UFile.noExt(main.saveFilename)+".stl";
      log("fname "+fname);

      
      ArrayList<UGeo> l=new ArrayList<UGeo>();
      l.add(geo);
      l.add(geoSmooth);
      
      UGeoIO.writeSTL(fname, l);
      UGeoIO.writeSTL(fname+"-1.stl", geo);
      main.saveFilename=null;
    }
  }

  private void drawModel() {
    if(geo==null) return;
    
    p.pushMatrix();
    p.translate(p.width/2, p.height/2);
    p.lights();
    main.nav.doTransforms();


    int colOutline=p.color(255,100,0);
    
    
    int cnt=0;
    
    if(drawType==(cnt++)) {
      UMB.pstroke(colOutline).pnoFill();
      UVertexList.draw(stackNoSmooth);
    }
    else if(drawType==(cnt++)) {
      UMB.pstroke(colOutline).pnoFill();
      UVertexList.draw(stack);
    }
    else if(drawType==(cnt++)) {
      UMB.pstroke(colOutline).pnoFill();
      UVertexList.draw(stackSmooth);
    }
    else if(drawType==(cnt++)) {
      UMB.pfill(p.color(255)).pnoStroke();
      geo.draw();
      
//      geo.pstroke(0xffff0000);
//      geo.drawNormals(20);

      UMB.pstroke(colOutline).pnoFill();
      UMB.draw(stack);

    }
    else if(drawType==(cnt++)) {
      UMB.pfill(p.color(255)).pnoStroke();
      geoSmooth.draw();
      
      UMB.pstroke(colOutline).pnoFill();
      UMB.draw(stackSmooth);
    }

    p.popMatrix();
    
    String str="Closed? "+(vl.isClosed()+
        " smoothLevel"+smoothLevel+" nodupl="+nodupl);
    p.text(str, 10, p.height-5);
    p.text(buildStr, 10, p.height-5-12);
  }

  public void keyPressed(char key) {
    if(key=='d') nodupl=!nodupl;
    if(key==p.TAB) {
      if(main.keyEvent.isShiftDown()) 
        drawType=(drawType>0 ? drawType-1 : 4);
      else drawType=(drawType+1)%5;
    }
  }
  
}
