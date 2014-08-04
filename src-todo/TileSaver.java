package unlekker.util;

import processing.core.*;
import java.io.*;
import unlekker.app.*;
import unlekker.data.IO;

/**
 * Class for rendering high-resolution images by splitting them into
 * tiles using the OpenGL viewport. See the <a
 * href="http://workshop.evolutionzone.com/2007/03/24/code-tilesaverpde/">original
 * entry on Code & Form</a> for details.
 * <p>Builds heavily on an <a href="http://processing.org/discourse/yabb_beta/YaBB.cgi?board=OpenGL;action=display;num=1159148942">original 
 * solution</a> by Processing forum user "surelyyoujest".
 * Modifications by Dave Bollinger allows larger images by flushing rows to Targa file upon completion. Thanks, Dave!</p>
 * @author <a href="http://workshop.evolutionzone.com/">Marius Watz</a>

 */


public class TileSaver {
  public boolean isTiling=false,done=true;
  public boolean doSavePreview=true;
 
  PApplet p;
  float FOV=60; // initial field of view
  float cameraZ, cameraNear,cameraFar,width, height;
  int tileNum=10,tileNumSq; // number of tiles
  int tileImgCnt, tileX, tileY, tilePad;
  boolean firstFrame=false, secondFrame=false;
  String tileFilename,tileFileextension=".tga";
  PImage tileImg;
  float perc,percMilestone;
 
// The constructor takes a PApplet reference to your sketch.
  public TileSaver(PApplet _p) {
    p=_p;
  }
 
  /**
   * Initialize using a filename to output to and the number of tiles to use.
   * @param _filename Name of file to output.
   * @param _num Number of tiles
   */  public void init(String _filename,int _num) {
    
    tileFilename=_filename;
    tileNum=_num;
    tileNumSq=(tileNum*tileNum);
 
    width=p.width;
    height=p.height;
    FOV=p.g.cameraFOV;
    cameraZ=p.g.cameraZ; 
    cameraNear=p.g.cameraNear;
    cameraFar=p.g.cameraFar;
    
    Util.log("\nTileSaver: "+tileNum+" tiles: "+
      (p.width*tileNum)+"x"+(p.height*tileNum));
 
    // remove extension from filename
    if(!new java.io.File(tileFilename).isAbsolute())
      tileFilename=p.dataPath(tileFilename);
    tileFilename=noExt(tileFilename);
    p.createPath(tileFilename);
 
    // save preview
    if(doSavePreview) p.g.save(tileFilename+"_preview.png");
 
    // set up off-screen buffer for saving tiled images
    tileImg=new PImage(p.width*tileNum, p.height);
 
    
    // start tiling
    done=false;
    isTiling=false;
    perc=0;
    percMilestone=0;
    tileInc();
  }
 
  // set filetype, default is TGA. pass a valid image extension as parameter.
  public void setSaveType(String extension) {
    tileFileextension=extension;
    if(tileFileextension.indexOf(".")==-1) tileFileextension="."+tileFileextension;
  }
  
  public void setFilename(String name) {
  	tileFilename=noExt(name);
  }
 
  // pre() handles initialization of each frame.
  // It should be called in draw() before any drawing occurs.
  public void pre() {
    if(!isTiling) return;
    if(firstFrame) firstFrame=false;
    else if(secondFrame) {
      secondFrame=false;
      tileInc();
    }
    setupCamera();
  }
 
  // post() handles tile update and image saving.
  // It should be called at the very end of draw(), after any drawing.
  public void post() {
    // If first or second frame, don't update or save.
    if(firstFrame||secondFrame|| (!isTiling)) return;
 
    // Find image ID from reverse row order
    // DAVE:  in forward row order
    int imgid = tileImgCnt;
    int idx=(imgid%tileNum);
//    int idy=(imgid/tileNum);
 
    // Get current image from sketch and draw it into buffer
    p.loadPixels();
    tileImg.set(idx*p.width, 0, p.g); // DAVE: always output to y=0
 
    // DAVE: time to stream output image?
    if (idx==tileNum-1) {
    	appendTGA(tileImg.pixels);
    }
 
    // Increment tile index
    tileImgCnt++;
    perc=100*((float)tileImgCnt/(float)tileNumSq);
    if(perc-percMilestone>5 || tileImgCnt==tileNumSq-1) {
      perc=perc-perc%5;
      Util.log(p.nf(perc,3,2)+"% completed. "+tileImgCnt+"/"+tileNumSq+" images saved.");
      percMilestone=perc;
    }
 
    if(tileImgCnt==tileNumSq) tileFinish();
    else tileInc();
  }
 
  public boolean checkStatus() {
    return isTiling;
  }
 
 
  // tileFinish() handles saving of the tiled image
  public void tileFinish() {
    isTiling=false;
 
    restoreCamera();

    // DAVE: stop output streaming
    finishTGA();
    Util.log("Done tiling.\nWrote: "+IO.noPath(tileFilename));
 
    // clear buffer for garbage collection
    tileImg=null;
    done=true;
  }
 
  // Increment tile coordinates
  public void tileInc() {
    if(!isTiling) {
      isTiling=true;
      firstFrame=true;
      secondFrame=true;
      tileImgCnt=0;
    } else {
      if(tileX==tileNum-1) {
        tileX=0;
        tileY=(tileY+1)%tileNum;
      } else
        tileX++;
    }
  }
 
  // set up camera correctly for the current tile
  public void setupCamera() {
    p.camera(width/2.0f, height/2.0f, cameraZ,
      width/2.0f, height/2.0f, 0, 0, 1, 0);
 
    if(isTiling) {
      float mod=1f/10f;
      p.frustum(width*((float)tileX/(float)tileNum-.5f)*mod,
        width*((tileX+1)/(float)tileNum-.5f)*mod,
        height*((float)(tileNum-1-tileY)/(float)tileNum-.5f)*mod, // DAVE: reverse y order, top-first
        height*((float)(tileNum-tileY)/(float)tileNum-.5f)*mod, // DAVE: reverse y order, top-first
        cameraZ*mod, 10000);
    }
 
  }
 
  // restore camera once tiling is done
  public void restoreCamera() {
    float mod=1f/10f;
    p.camera(width/2.0f, height/2.0f, cameraZ,
      width/2.0f, height/2.0f, 0, 0, 1, 0);
    p.frustum(-(width/2)*mod, (width/2)*mod,
      -(height/2)*mod, (height/2)*mod,
      p.min(cameraZ*mod,cameraNear),p.max(cameraFar,10000));
    
//      cameraZ*mod,10000);
      //cameraNear, cameraFar);
  }
 
  // strip extension from filename
  String noExt(String name) {
    int last=name.lastIndexOf(".");
    if(last>0)
      return name.substring(0, last);
 
    return name;
  }
  
  // DAVE:  added tga stuff below, stolen from PApplet and PImage
  
  BufferedOutputStream bos=null;

  void startTGA(String filename) {
    try {
      //println("pathfilename = " + p.savePath(filename));
      File file = new File(p.savePath(filename));
      bos = new BufferedOutputStream(new FileOutputStream(file), 32768);
      byte header[] = new byte[18];
      header[2] = 0x0A;
      header[16] = 24;
      header[17] = 0x20;
      header[12] = (byte) ((int)(width*tileNum) & 0xff);
      header[13] = (byte) ((int)(width*tileNum) >> 8);
      header[14] = (byte) ((int)(height*tileNum) & 0xff);
      header[15] = (byte) ((int)(height*tileNum) >> 8);
      bos.write(header);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  void appendTGA(int [] pixels) {
  	
    // DAVE: start streaming output image
  	//    tileFilename+="_"+(p.width*tileNum)+"x"+
  	//            (p.height*tileNum)+tileFileextension;
  	if(bos==null)
  		startTGA(tileFilename+tileFileextension);

    try {
      int maxLen = (int)(height * width * tileNum);
      int index = 0;
      int col;
      int[] currChunk = new int[128];
      while (index < maxLen) {
        boolean isRLE = false;
        currChunk[0] = col = pixels[index];
        int rle = 1;
        // try to find repeating bytes (min. len = 2 pixels)
        // maximum chunk size is 128 pixels
        while (index + rle < maxLen) {
          if (col != pixels[index + rle] || rle == 128) {
            isRLE = (rle > 1); // set flag for RLE chunk
            break;
          }
          rle++;
        }
        if (isRLE) {
          bos.write(128 | (rle - 1));
          bos.write(col & 0xff);
          bos.write(col >> 8 & 0xff);
          bos.write(col >> 16 & 0xff);
        } else {  // not RLE
          rle = 1;
          while (index + rle < maxLen) {
            if ((col != pixels[index + rle] && rle < 128) || rle < 3) {
              currChunk[rle] = col = pixels[index + rle];
            } else {
              // check if the exit condition was the start of
              // a repeating colour
              if (col == pixels[index + rle]) rle -= 2;
              break;
            }
            rle++;
          }
          // write uncompressed chunk
          bos.write(rle - 1);
          for (int i = 0; i < rle; i++) {
            col = currChunk[i];
            bos.write(col & 0xff);
            bos.write(col >> 8 & 0xff);
            bos.write(col >> 16 & 0xff);
          }
        }
        index += rle;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  void finishTGA() {
    try {
      bos.flush();
      bos.close();
      bos=null;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
}