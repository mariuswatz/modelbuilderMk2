package unlekker.app;

import processing.core.PApplet;
import unlekker.mb2.util.UMB;

public class UAppSys extends UMB {
  protected boolean initialized;
  
  public UApp app;
  public PApplet p;
  public String name;

  public UAppSys(UApp app) {
    this.app=app;
    p=app.getPApplet();
    name=className(this);
  }
  
  public void draw() {
    p.background(0);
    p.text(name+" "+p.frameCount, 20, 60);
    
    String s="";
    if(app.ctrlIsDown) s+="CTRL ";
    if(app.shiftIsDown) s+="SHIFT ";
    if(app.altIsDown) s+="ALT ";
    s=s.trim()+"+"+
        app.keyName(app.keyCode)+" "+app.keyCode+" "+app.key;
    p.text(s, 20, 60+48);
    
  }

  public boolean initialized() {
    return initialized;
  }
  
  public void reinit() {
    initialized=true;
    app.initColors();
  }

  public void initColors() {
    app.initColors();
  }
}
