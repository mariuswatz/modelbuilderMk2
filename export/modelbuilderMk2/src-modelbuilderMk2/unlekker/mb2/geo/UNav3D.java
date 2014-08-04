/*
 * modelbuilderMk2
 */
package unlekker.mb2.geo;

import java.awt.event.MouseWheelListener;

import processing.event.MouseEvent;
import processing.event.KeyEvent;
import unlekker.mb2.util.UMB;

public class UNav3D extends UMB {
  public UVertex trans,rot;
  public float guiSpeed=5;
  public boolean enabled=true;
   
  public processing.event.MouseEvent theMouseEvent;
  public KeyEvent theKeyEvent;
  public boolean shiftIsDown=false,altIsDown,ctrlIsDown;

  public UNav3D() {
    rot=new UVertex(0,0,0);
    trans=new UVertex(0,0,0);   

    if(papplet!=null) {
      registerEvents();
    }
    else {
      log("UNav3D: No PApplet reference found, event handlers not registered.");
    }
  }
  
  public void registerEvents() {
    MouseWheelInput mw=new MouseWheelInput();
    papplet.addMouseWheelListener(mw);
    
    papplet.registerMethod("mouseEvent",this);
    papplet.registerMethod("keyEvent",this);
    
    
    // 1.5.1 code
//    papplet.registerKeyEvent(this);
//    papplet.registerMouseEvent(this);
  }

  public void unregisterMouseEvents() {
    papplet.unregisterMethod("mouseEvent",this);
//    papplet.registerMethod("keyEvent",this);
  }

  public void unregisterKeyEvents() {
    papplet.unregisterMethod("keyEvent",this);

//    papplet.unregisterKeyEvent(this);
  }

  public void unregisterEvents() {
//    papplet.unregisterKeyEvent(this);
//    papplet.unregisterMouseEvent(this);
      papplet.unregisterMethod("mouseEvent",this);
    papplet.unregisterMethod("keyEvent",this);
  }

  public void keyEvent(KeyEvent ev) {
    if(!enabled) return;
    theKeyEvent=ev;
    
    if(ev.getAction() == KeyEvent.PRESS) keyPressed(ev);
    else if(ev.getAction() == KeyEvent.RELEASE) keyReleased(ev);
  }
  
  public void keyReleased(KeyEvent ev) {
    
    if(ev.getKeyCode()==papplet.SHIFT) {
      shiftIsDown=false;
    }
    if(ev.getKeyCode()==papplet.CONTROL) {
      ctrlIsDown=false;
    }
    if(ev.getKeyCode()==papplet.ALT) {
      altIsDown=false;
    }
  }
  
  public UNav3D reset() {
//  if(UUtil.DEBUGLEVEL>3) 
//    p.println("Reset transformations. "+transReset);
  trans.set(0,0,0);
  rot.set(0,0,0);
  return this;
}

  public void doTransforms(boolean doCenter) {
    if(doCenter) ptranslate(papplet.width/2, papplet.height/2);
    doTransforms();
  }

  /**
   * Executes the needed transformations (provided a
   * PApplet reference exists.) 
   */
  public void doTransforms() { 
    if(!checkGraphicsSet()) return;
    
    papplet.translate(trans.x,trans.y,trans.z);
    if(rot.y!=0) papplet.rotateY(rot.y);
    if(rot.x!=0) papplet.rotateX(rot.x);
    if(rot.z!=0) papplet.rotateZ(rot.z);
  }

  public void keyPressed(KeyEvent ev) {
    if(ev.getKeyCode()==papplet.SHIFT) {
      shiftIsDown=true;
    }
    if(ev.getKeyCode()==papplet.CONTROL) {
      ctrlIsDown=true;
    }
    if(ev.getKeyCode()==papplet.ALT) {
      altIsDown=true;
    }
    
    float mult=1;
    if(ctrlIsDown) mult=10; 

//    if(papplet.key==papplet.CODED) {
      // check to see if CTRL is pressed
      if(papplet.keyEvent.isControlDown()) {
        // do zoom in the Z axis
        if(ev.getKeyCode()==papplet.UP) trans.z+=guiSpeed;
        if(ev.getKeyCode()==papplet.DOWN) trans.z-=guiSpeed;
        if(ev.getKeyCode()==papplet.LEFT) rot.z+=DEG_TO_RAD*(guiSpeed);
        if(ev.getKeyCode()==papplet.RIGHT) rot.z-=DEG_TO_RAD*(guiSpeed);
      }
      // check to see if CTRL is pressed
      else if(papplet.keyEvent.isShiftDown()) {
        // do translations in X and Y axis
        if(ev.getKeyCode()==papplet.UP) trans.y-=guiSpeed;;
        if(ev.getKeyCode()==papplet.DOWN) trans.y+=guiSpeed;;
        if(ev.getKeyCode()==papplet.RIGHT) trans.x+=guiSpeed;;
        if(ev.getKeyCode()==papplet.LEFT) trans.x-=guiSpeed;;
      }
      else {
        // do rotations around X and Y axis
        if(ev.getKeyCode()==papplet.UP) 
          rot.x+=DEG_TO_RAD*(guiSpeed);
        if(ev.getKeyCode()==papplet.DOWN) rot.x-=DEG_TO_RAD*(guiSpeed);
        if(ev.getKeyCode()==papplet.RIGHT) rot.y+=DEG_TO_RAD*(guiSpeed);
        if(ev.getKeyCode()==papplet.LEFT) rot.y-=DEG_TO_RAD*(guiSpeed);
      }
      
//    }
    
    if(ctrlIsDown && 
        ev.getKeyCode()==java.awt.event.KeyEvent.VK_HOME) reset();

  }

  public void mouseEvent(processing.event.MouseEvent ev) {
    if(!enabled) return;
    theMouseEvent=ev;
    if (ev.getAction() == processing.event.MouseEvent.DRAG) {
      mouseDragged();
    }
  }

  public void mouseDragged() {
//    if(gui!=null && gui.isMouseOver()) return;
    
//    if(papplet.mouseButton==papplet.RIGHT && papplet.) {
//      rot.z+=DEG_TO_RAD*(papplet.mouseX-papplet.pmouseX)*1*guiSpeed;
//    }
//    else 
      if(papplet.mouseButton==papplet.RIGHT) {
      trans.z+=DEG_TO_RAD*
          (papplet.mouseY-papplet.pmouseY)*30*guiSpeed;
      return ;
    }
//    if(isParentPApplet) {
//      rot.y+=DEG_TO_RAD*(papplet.mouseX-papplet.pmouseX);
//      rot.x+=DEG_TO_RAD*(papplet.mouseY-papplet.pmouseY);
//      return;
//    }
    
    // if shift is down do pan instead of rotate
    if(shiftIsDown) {
      trans.x+=(papplet.mouseX-papplet.pmouseX)*guiSpeed;
      trans.y+=(papplet.mouseY-papplet.pmouseY)*guiSpeed;
    }
    // calculate rot.x and rot.Y by the relative change
    // in mouse position
    else {
      rot.y+=DEG_TO_RAD*(papplet.mouseX-papplet.pmouseX);
      rot.x+=DEG_TO_RAD*(papplet.mouseY-papplet.pmouseY);
    }
  }
  
  public void mouseWheel(float step) {
    if(!enabled) return;
    
//    if(papplet.keyEvent!=null && papplet.keyEvent.isControlDown())
//      trans.z=trans.z+step*50;
//    else 
      trans.z=trans.z+step*guiSpeed;
  }
  
  public String toStringData() {
    return "[UNav3D "+trans.toString()+" "+rot.toString()+"]";
}

  // utility class to handle mouse wheel events
  class MouseWheelInput implements MouseWheelListener{
    public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
      mouseWheel(e.getWheelRotation());
    } 
  }

}
