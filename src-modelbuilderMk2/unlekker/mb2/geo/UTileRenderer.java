package unlekker.mb2.geo;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.opengl.PGraphics3D;
import unlekker.mb2.util.UFile;
import unlekker.mb2.util.UMB;
import unlekker.mb2.util.UTargaProgressive;

/**
 * Tile-based rendering for OpenGL and P3D. Makes it possible to
 * save high-res stills from realtime graphics. See the "10-advanced/UTileRendering"
 * example for how to use.
 * 
 * @author marius
 *
 */
public class UTileRenderer extends UMB {
  PApplet p;
  PGraphics3D g3;
  PGraphics g;
  
  UTargaProgressive tga;
  
  int lastPerc=0;
  
  int cnt=-1,numTiles,numTilesSq;
  UVertex tileSizeV,offsets[];
  
  public boolean done=false;
  
  double tileSize,aspect,cameraFOV,cameraNear,cameraFar,top,bottom,left,right;

  
  String filename;
  
  public UTileRenderer(String filename,int n) {
    p=getPApplet();
    
    numTiles=n;
    numTilesSq=n*n;
    
    this.filename=filename;
    if(filename.endsWith(".tga")) {
      log("Will use progressive Targa output.");
      tga=new UTargaProgressive(filename, p.width*n, p.height*n);
    }  
    
    log("Saving to '"+filename+"'");
  }
  
  public void start() {
    p.registerMethod("pre", this);
    p.registerMethod("post", this);
    
    cnt=-1;
    if(tga!=null) {
      g=p.createGraphics(p.width*numTiles, p.height);//*numTiles);
    }
    else {
      g=p.createGraphics(p.width*numTiles, p.height*numTiles);
    }

    g.beginDraw();
    g.background(0,0);
    
    
    aspect=(double)p.height/(double)p.width;
    tileSize=2.0/(double)numTiles;
    
    double near=0.01f;
    double far=1000f;
    
    offsets = new UVertex[numTiles * numTiles];

    g3=(PGraphics3D)p.g;
    
    near=g3.cameraNear;
    far=g3.cameraFar;
    

    cameraFOV = g3.cameraFOV; //fov;
    cameraNear = near;
    cameraFar = far;
    top = Math.tan(cameraFOV * 0.5) * cameraNear;
    bottom = -top;
    left = aspect * bottom;
    right = aspect * top;

    cameraFOV = g3.cameraFOV;
    aspect = g3.cameraAspect;
    cameraFar = g3.cameraFar;
    cameraNear = g3.cameraNear;
//    cameraFar=-300;
//    cameraNear =1;
    log("cam " + nf(cameraFOV * DEG_TO_RAD) + " " + nf(aspect) + " "
        + nf(cameraNear) + " " + nf(cameraFar));

    int idx = 0;
    tileSizeV = new UVertex((float) (2 * right / numTiles), (float) (2 * top / numTiles));
    double y = top - tileSizeV.y;
    while (idx < offsets.length) {
      double x = left;
      for (int xi = 0; xi < numTiles; xi++) {
        offsets[idx++] = new UVertex((float) x, (float) y);
        x += tileSizeV.x;
      }
      y -= tileSizeV.y;
    }

  }

  public void pre() {
//    if(cnt==0) progress=new UProgressInfo();

    
    if(cnt<0) cnt=0;
    done=cnt==numTilesSq;
    
    if(done && g!=null) {
      log("Tiling done.");
      if(tga!=null) tga.close();
      else g.save(filename);
      
      g=null;
      p.unregisterMethod("pre", this);
      p.unregisterMethod("post", this);
      resetCamera();
      
      return;
    }

    p.pushMatrix();
//    log("pre "+cnt +" "+offsets[cnt].str());
    resetCamera();
    
    p.camera(p.width/2.0f, p.height/2.0f, g3.cameraZ,
        p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);
    
    UVertex o=offsets[cnt];
    p.frustum(o.x, o.x+tileSizeV.x, 
        (float) (o.y * aspect),
        (float) (aspect*(o.y + tileSizeV.y)), 
        (float)cameraNear,(float)cameraFar);
    
  }
  
  public void post() {
    if(cnt<0) return;
    
      int x=cnt%numTiles;
      int y=cnt/numTiles;
      p.loadPixels();
      
      
      if(tga!=null) {
        g.beginDraw();
        g.set(x*p.width, 0, p.g);
        g.endDraw();
        if(cnt%numTiles==(numTiles-1)) tga.append(g);
      }
      else {
        g.beginDraw();
        g.set(x*p.width, y*p.height, p.g);
        g.endDraw();
      }
      
      p.popMatrix();
      
      p.translate(p.width/2, p.height/2);
//      resetCamera();
      
      float perc=map(cnt,0,numTilesSq-1, 0,100);
      if(perc>lastPerc+5) {
        log("Progress: "+(int)perc+"%");
        lastPerc=(int)(perc/5)*5;
      }
      
      cnt++;
  }

  void resetCamera() {
    float mod=1f/10f;

    p.camera(p.width/2.0f, p.height/2.0f, g3.cameraZ,
        p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);
    p.frustum((float)left,(float)right,
        (float)bottom,(float)top,
        (float)cameraNear,(float)cameraFar);
  }


}
