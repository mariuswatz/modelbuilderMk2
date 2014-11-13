package unlekker.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.*;

import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import unlekker.mb2.util.*;
import unlekker.mb2.geo.*;

public class UApp extends UMB {
  public static String CONFW="width",CONFH="height",
      CONFWINX="winX",CONFWINY="winY",
      CONFPATH="path",CONFDATAPATH="datapath",
      CONFDECORATED="decorated",CONFONTOP="ontop",CONFRENDERER="renderer";
  public static int APPSWITCH=0,APPINITCOLORS=1,
      APPREINIT=2;
      
  public PApplet p;
  public ArrayList<UAppSys> systems;  
  public UAppSys sys;
  
  public int width,height;
  boolean appInitialized=false;
  protected HashMap<Integer,Integer> appFlags;
    
  public String name;
  
  public UConfig conf;
  public UNav3D nav;
  
  public MouseEvent mouseEvent;
  public KeyEvent keyEvent;
  public char key;
  public int keyCode;
  public boolean shiftIsDown=false,altIsDown,ctrlIsDown;

  public UColor colors;
  
  public UApp() {
    systems=new ArrayList<UAppSys>();
    initConfig();

    name=className(this);
  }

  public UApp init() {
    p=getPApplet();
    registerEvents();
    
    logDivider(name+": init");
    if(p.frame!=null && 
        (!conf.getBool(CONFDECORATED) || !conf.getBool(CONFONTOP))) try {
      p.frame.dispose();
      p.frame.setUndecorated(!conf.getBool(CONFDECORATED));
      p.frame.setAlwaysOnTop(conf.getBool(CONFONTOP));
    }
    catch(Exception e) {
      log(e.toString());
    }       
    return this;
  }

  public UApp setupWindow() {
    if(appInitialized) return this;
    
    logDivider(name+": setupWindow");
    appInitialized=true;
    
    p=getPApplet();
    p.sketchPath=conf.get(CONFPATH);
    width=conf.getInt(CONFW);
    height=conf.getInt(CONFH);
    p.size(width,height,conf.get(CONFRENDERER));
    p.textFont(p.createFont("Arial", 11));

//  p.noCursor();
  
    return this;
  }


  public UApp setup(int w,int h) {
    conf.put(CONFW,w).put(CONFH,h);
    setupWindow();
    return this;
  }

  public UApp setup(int w,int h,int locx,int locy,String renderer,boolean hasFrame) {
    return setup(w,h,locx,locy,renderer,hasFrame,false);
  }

  public UApp setup(int w,int h,int locx,int locy,
      String renderer,boolean hasFrame,boolean isOnTop) {
    conf.put(CONFW,w).put(CONFH,h);
    conf.put(CONFWINX,locx).put(CONFWINY,locy);
    conf.put(CONFDECORATED,hasFrame).put(CONFRENDERER, renderer);
    conf.put(CONFONTOP,isOnTop);
     

    setupWindow();
    return this;
  }

  public UApp setPath(String path) {
    conf.put(CONFPATH, path);
    p.sketchPath=path;
    return this;
  }
  
  public UApp initConfig() {
    if(conf==null) conf=new UConfig();
    conf.put(CONFW,1024).put(CONFH,768);
    conf.put(CONFWINX,0).put(CONFWINY,0);
    conf.put(CONFDECORATED,false).put(CONFRENDERER, OPENGL);
    conf.put(CONFPATH,UFile.getCurrentDir());
    conf.put(CONFDATAPATH,UFile.getCurrentDir());
    
//    conf.list();
    appFlags=new HashMap<Integer,Integer>();
    return this;
  }

  public UApp draw() {
    p=getPApplet();
    if(p.frameCount<5) moveWindow(conf.getInt(CONFWINX),conf.getInt(CONFWINY));
    return this;
  }

  public UApp handleSystem() {
    int switchId=checkFlag(APPSWITCH, true);
    if(switchId>-1 && switchId<systems.size()) {
      sys=systems.get(switchId);
    }
    
    if(checkFlag(APPREINIT,true)==1) {
      log("reinit");
      if(sys!=null) sys.reinit();
    }

    if(checkFlag(APPINITCOLORS,true)==1) {
      log("initcolors");
      initColors();
      if(sys!=null) sys.initColors();
    }

    if(sys!=null) {
      if(!sys.initialized) sys.reinit();
      sys.draw();
    }
    
    return this;
  }
  
  public UApp moveWindow(int x,int y) {
    p.frame.setLocation(x,y);
    return this;
  }
  
  public UApp reinit() {
    return this;
  }

  public int rndColor() {
    if(colors==null) return pcolor(255);
    else return colors.rndColor();
  }

  public UApp initColors() {
    return this;
  }

  public UApp addNav() {
    nav=new UNav3D();
    return this;
  }
  
  public UApp add(UAppSys sketch) {
    systems.add(sketch);
    if(sys==null) sys=sketch;
    return this;
  }
  
  
  Object controlP5;
  Method guiMouseOverMethod;
  
  public UApp addGUI(Object o) {
    
    Class c;
    try {
      c=Class.forName(o.getClass().getName());
      Method[] meths = c.getMethods( );
      for(Method m:meths) {
        if(m.getName().indexOf("isMouseOver")>-1) guiMouseOverMethod=m;
      }
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    if(guiMouseOverMethod!=null) log(guiMouseOverMethod.getName());
    return this;
  }
  
  ////////// EVENTS
  
  public void registerEvents() {
    papplet.registerMethod("mouseEvent",this);
    papplet.registerMethod("keyEvent",this);    
  }

  public void keyEvent(KeyEvent ev) {
    //if(!enabled) return;
    
    keyEvent=ev;
//    if(key==p.CODED)
    
    boolean isPress=(ev.getAction()==KeyEvent.PRESS);
    if(ev.getKeyCode()==papplet.SHIFT) {
      shiftIsDown=isPress;
    }
    if(ev.getKeyCode()==papplet.CONTROL) {
      ctrlIsDown=isPress;
    }
    if(ev.getKeyCode()==papplet.ALT) {
      altIsDown=isPress;
    }

    if(isPress) keyPressed();
    else keyReleased();
  }
  
  public void keyReleased() {    
//    log(p.frameCount+ " keyReleased "+(int)(p.key)+" "+
//        keyName(p.keyCode));
  }
  

  public void keyPressed() {
    key=p.key;
    keyCode=p.keyCode;
    
//    log(p.frameCount+ " keyPressed "+(int)(p.key)+" "+
//        keyName(p.keyCode)+" "+
//        p.keyCode+" "+keyCode);
    
    if(ctrlIsDown) {
      
      int val=keyCode-KEY_1;
      if(keyCode==KEY_0) val=10;
      if(val>-1 && val<11) {
        debug(2,"appswitch "+val);
        addFlag(APPSWITCH,val);
        return;
      }
      
      if(keyCode==KEY_A) addFlag(APPINITCOLORS, true);
      if(keyCode==KEY_N) addFlag(APPREINIT, true);;
    }
  }

  public int checkFlag(int flag) {
    return checkFlag(flag,false);
  }

  public int checkFlag(int flag,boolean doClear) {
    if(!appFlags.containsKey(flag)) return -1;
    
    int val=appFlags.get(flag);
    if(doClear) appFlags.remove(flag);
    
    return val;
  }

  public UApp clearFlag(int flag) {
    if(appFlags.containsKey(flag)) {
      appFlags.remove(flag);
    }
    
    return this;
  }

  public void addFlag(int flag,int val) {
    appFlags.put(flag, val);
  }

  public void addFlag(int flag,boolean val) {
    appFlags.put(flag, val ? 1 : 0);    
  }

  public void mouseEvent(processing.event.MouseEvent ev) {
//    if(!enabled) return;
    mouseEvent=ev;
    if (ev.getAction() == processing.event.MouseEvent.DRAG) {
      mouseDragged();
    }
  }

  public void mouseDragged() {
  }
}
