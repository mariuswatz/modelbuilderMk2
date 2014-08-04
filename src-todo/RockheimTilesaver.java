package rockheim;

//import codeanticode.glgraphics.GLConstants;
import java.awt.event.KeyEvent;
import java.io.File;

import unlekker.app.*;
import unlekker.util.*;
import unlekker.data.*;
import unlekker.modelbuilder.*;

import processing.core.*;
import processing.opengl.*;
//import processing.opengl2.*;
import processing.video.*;
import unlekker.app.input.*;
import unlekker.app.save.QuicktimeSaver;
import unlekker.app.save.Saver;

public class RockheimTilesaver extends Rockheim {

	public TileSaver tiler;
 
	public RockheimTilesaver() {
		super();
		
//		setupWindow(1012,382, 0,0, true);
//		setupWindow(2044,783,(-1680),0, false);
		filePrefix=conf.getProperty("prefix","");
		mmFolder=conf.getProperty("mmFolder","");
		appDataRoot=mmFolder;
		if(new File(mmFolder+"/IsCopy.txt").exists()) isCopy=true;
		if(isCopy) filePrefix+="C";
    filePrefix="RT";

		log(filePrefix);

//		if(isCopy) setupWindow(2044,784,40,0, false);
//		else setupWindow(2044,784,(1920+1280-2044),0, false);
//		setupWindow(2560,1600,0,0, false);
		setupWindow(1000,1000,0,0, false);
		
		appRenderer=OPENGL;
		
		scaleMod=(float)screen.width/(float)appWidth;
		log("scaleMod "+scaleMod);
	}
	
	public void setup() {
		super.setup();
		glSmooth(4);
		smooth();
		noScreensaver();
		fnt=createFont("Arial", 12, false);
//		mask=loadImage("data/Pixel Layout - MaskSmall.png");
		mask=new PImage[3];
		mask[0]=loadImage(sketchPath+"/data/Pixel Layout - Mask 2044k Expanded.png");
		mask[1]=loadImage(sketchPath+"/data/Pixel Layout Actual - Mask 2044K.png");
		maskId=0;		
		maskCol=mask[maskId].get(0,mask[maskId].height-1);
		maskId=2;		

		mmW=width/4;
		mmH=height/4;
		

	}
	
	public void doPerspective() {
		if(doScale) scale(scaleMod);
		if(trans.isRunning && tiler==null) {
		  trans.update();		
	    perspective(FOV,ASP,NEAR,FAR);
		}
		
		ambientLight(110,110,110);
		
		float l=globalLight;
		directionalLight(l*1.5f,l*1.5f,l*1.5f, 
				-0.1f,-0.1f,-1f);
		directionalLight(l,l,l, -0.5f,-0.5f,-0.5f);
		directionalLight(l,l,l, 0.25f,0.25f,-0.5f);
		directionalLight(l,l,l, 0.5f,-0.55f,-0.1f);
		directionalLight(l*0.6f,l*0.6f,l*0.6f, 0.1f,0.6f,1);

		nav.doTransforms();
//		scale(scaleMod);
	}
	

	public void doInit() {
		nav = new UNav3D(this);
		nav.setTranslation(width / 2, height / 2, 0);
		nav.rotSpeed=2;
		nav.transSpeed=8;
		
		trans=new Transition(this);
		trans.setDuration(200f/10f);
		trans.setDuration(10);
		trans.setCam(0);
		trans.setCam(1);
		trans.stop();
		
		add(new RockArcs06ATiler(this));


		doSwitch(sysNum-1, -1);
		
		PGraphics3D pg = (PGraphics3D) g;
		FOV = pg.cameraFOV;
		ASP = pg.cameraAspect;
		FAR = pg.cameraFar;
		FAR=-300;
		NEAR =1;
		log("cam " + Str.nf(FOV * DEG_TO_RAD) + " " + Str.nf(ASP) + " "
				+ Str.nf(NEAR) + " " + Str.nf(FAR));
		frameRate(50);

//		registerPost(this);
		appDoneInit=true;
	}
	
	public void draw() {
		if (frameCount < 10) return;
		if(!appDoneInit) doInit();

		background(0);
		pushMatrix();
		noStroke();
		if(tiler!=null) {
		  tiler.pre();
		}
    nav.doTransforms();
		
    hint(DISABLE_DEPTH_TEST);
		sys[sysActive].draw();
		popMatrix();
		
		if(mask[maskId]!=null && saveFileName==null) {
			noDepth();
			hint(DISABLE_DEPTH_TEST);
			noStroke();
			image(mask[maskId],0,0);
			depth();
		}
		
//		checkRender();
		checkRun();

    if(tiler!=null) {
      tiler.post();
      if(!tiler.isTiling) tiler=null;
    }
//		if(mm!=null) saveFrame("Temp.png");
	}
	
	
//	public void post() {
//		if(frameCount%100==0) log(frameCount+" frames, "+Str.nf(frameRate));
//	}

	public void updateMovie() {
		if(mm!=null) try {
			if(mmCanvas==null) {
				mmCanvas=(PGraphicsJava2D)createGraphics(mmW, mmH, JAVA2D);
			}
			mmCanvas.beginDraw();
			mmCanvas.smooth();
			mmCanvas.background(0);
//			((PGraphicsOpenGL)g).updateTexture();
			log("pixels? "+pixels);
//			if(g.pixels!=null) log("Pixels # "+g.pixels.length+" "+(width*height));
//			log("image? "+g.image);
			loadPixels();
//			g.loadPixels();
//			g.updatePixels();
			
			PImage tmp=get();
			mmCanvas.image(tmp,0,0,mmW,mmH);
			mmCanvas.fill(255);
//			mmCanvas.ellipse(random(mmW), random(mmH), 20, 20);
			mmCanvas.endDraw();
			mmCanvas.loadPixels();
			
			mm.addFrame(mmCanvas.pixels,mmW,mmH);
			mmFrameCount++;
			if(mmFrameCount%100==0) log(mmFrameCount+" frames, "+frameRate);
		} catch(Exception e) {
			logErr("Frame: "+frameCount);
			e.printStackTrace();
		}
	}
	
  public void doSwitch(int which,int param) {
    sysActive=which;
    sys[sysActive].reinit();
		log("Switching to: "+sys[sysActive].sketchName+" "+
				which+" "+sysActive);
//		fadeIn(4000);
  }
  
  public void mousePressed() {
  }

  public void keyPressed() {
  	if(sysActive<0) return;
		if (key == CODED) {
			if (keyCode == KeyEvent.VK_F1) {
				doSave();
				key = 0;
				keyCode = 0;
				return;
			}
		}

//  	super.keyPressed();
  	sys[sysActive].keyPressed(key);
  	
		if(keyCode==KeyEvent.VK_F5) {
			saveFileName=saveFileName = saver.getFilename(savePDF);
		}

  	if(keyEvent.isControlDown()) {  		
  		if(keyCode==KeyEvent.VK_T) {
  		  tiler=new TileSaver(this);
  		  tiler.init(saver.getFilename(savePNG),5);
  		  
//  			maskId=(maskId+1)%mask.length;
//  			maskCol=mask.get(0,mask.height-1);
  		}
  		else 	if (keyCode == KeyEvent.VK_R && keyEvent.isShiftDown()) {
  			nav.setTranslation(width / 2, height / 2, 0);
  			trans.setCam(0);
  			trans.setCam(1);
  		}
  		else 	if (keyCode == KeyEvent.VK_R) {
				log("REINIT");
				sys[sysActive].reinit();
				keyCode = 0;
				return;
			}

  		
			///////////////////// TRANSITIONS
	  	else if(keyCode==keyEvent.VK_N) {
	  		if(keyEvent.isShiftDown()) trans.setCam(0);
	    	else trans.setView(0);
	  	}
	  	else if(keyCode==keyEvent.VK_S) {
	  		doScale=!doScale;
	  		log("doScale "+doScale);
	  	}
	  	else if(keyCode==keyEvent.VK_V) {
	  		if(width>1920) setSize(1920,1080);
	  		else setSize(2560,1600);
	  	}
  		
	  	else if(keyCode==keyEvent.VK_U) {
	  		frame.setLocation(0,0);
	  	}
	  	else if(keyCode==keyEvent.VK_O) {
	  		frame.setLocation(-(appWidth-screen.width),0);
	  	}
	  	else if(keyCode==keyEvent.VK_Y) {
	  		frame.setLocation(0,-(appHeight-screen.height));
	  	}
	  	else if(keyCode==keyEvent.VK_P) {
	  		frame.setLocation(-(appWidth-screen.width),-(appHeight-screen.height));
	  	}
	  	else if(keyCode==keyEvent.VK_I) {
	  		frame.setLocation(frame.getLocationOnScreen().x, frame.getLocationOnScreen().y-20);
	  	}
	  	else if(keyCode==keyEvent.VK_J) {
	  		frame.setLocation(frame.getLocationOnScreen().x-20, frame.getLocationOnScreen().y);
	  	}
	  	else if(keyCode==keyEvent.VK_K) {
	  		frame.setLocation(frame.getLocationOnScreen().x, frame.getLocationOnScreen().y+20);
	  	}
	  	else if(keyCode==keyEvent.VK_L) {
	  		frame.setLocation(frame.getLocationOnScreen().x+20, frame.getLocationOnScreen().y);
	  	}
	  	else if(keyCode==keyEvent.VK_M) {
	  		if(keyEvent.isShiftDown()) trans.setCam(1);
	    	else trans.setView(1);
	  	}
	  	else if(keyCode==keyEvent.VK_B) {
	  		flagRun=true;
	  	}

  	}
  }

	public void checkRun() {
		if(!flagRun) return;
		
		flagRun=false;
		if(trans.isRunning) {
			if(keyEvent.isShiftDown()) {
				trans.tCnt=trans.tGoal;
				trans.doCam();
			}
			else {
				trans.stop();
//	  				trans.setCam(0);
			}
		}
		else {
			if(doSave) {
				movieFrameRate=30;
				startRender();
				trans.setDuration(fullDuration);
				frameRate(50);
			}
			else {
				movieFrameRate=10;
				trans.setDuration(fullDuration/4);
				frameRate(10);
			}

			trans.run();
			renderCnt=0;
		}
	}
	public void stop() {
		endRender();
	}
	
	public void checkRender() {
//		if (mm == null && (doSave || renderCnt > 0)) startRender();
		if (mm != null) {
			loadPixels();
			mm.addFrame(pixels, width, height);
			
			if(!doSave) endRender();
//			if (((Exploder01A) sys[sysActive]).stateCnt < 0)
//				endRender();
		}
		else if(saveFileName!=null && !saver.checkStatus(savePDF)) {
			saveFrame(IO.noExt(saveFileName) + ".png");
			saveFileName=null;
		}
		// TODO Auto-generated method stub

	}

	public void startRender() {
		// TODO Auto-generated method stub
//		saveFileName = IO.noExt(saver.getFilename(savePNG));
		if(saveFileName==null) saveFileName = IO.noExt(saver.getFilename(savePNG));
		mm = new MovieMaker(this, width, height, saveFileName + ".mov", movieFrameRate,
				saver.qt.PNG, MovieMaker.BEST);
		maskId=2;
		tStart=millis();
	}

	public void endRender() {
		if (mm == null) return;
		if(renderCnt>0) renderCnt--;
		logDivider("Render done: " + IO.noPath(saveFileName));
		log("Render time: "+Str.timeStr(millis()-tStart)+"\n");

		// doSave=false;
		mm.finish();
		mm = null;
		if(doSave) exit();
		doSave=false;
		maskId=0;
		frameRate(50);

//		exit();
	}

	public void doSave() {
		// TODO Auto-generated method stub
//		super.saveImmediate(savePNG);
		doSave = !doSave;
		log("doSave "+doSave);
	}

	public void endMovie() {
		if(mm==null) return;
		log("Ended MovieMaker. ");
		mm.finish();
		mm=null;
	}
  
  public static void main(String args[]) {
  	PApplet.main(new String[] {"rockheim.RockheimTilesaver"});
  }

}
