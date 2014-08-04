package unlekker.mb2.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import processing.core.PApplet;
import processing.core.PImage;
import unlekker.mb2.geo.UTileRenderer;

/**
 * <p>Progressive Targa image encoder. Used for writing
 * large images to disk without the overhead of
 * keeping a massive image in memory.</p>
 * 
 *    <code>
 *    UTargaProgressive tga=new UTargaProgressive("test.tga",8000,8000);
 *    PImage img=createImage(8000,1000);
 *    for(int i=0; i<8; i++) tga.append(img);
 *    tga.close();
 *    </code>
 *  
 * <p>Code contributed by Dave Bollinger for use with
 * {@link UTileRenderer}, allowing for super-highres 
 * output (10k x 10k and beyond.) Thanks, Dave!</p>
 *
 * @author marius
 *
 */
public class UTargaProgressive extends UMB {
  PApplet p;

  public String filename;
  public int width,height;
  boolean isRLE = true;
  
  public UTargaProgressive(String filename,int width,int height) {
    p=getPApplet();
    this.filename=filename;
    this.width=width;
    this.height=height;
    
    startTGA();
  }
  
  public void close() {
    try {
      bos.flush();
      bos.close();
      bos=null;
    } catch (IOException e) {
      e.printStackTrace();
    }

    logf("Done: %s [%dx%d]",
        UFile.noPath(filename),
        width,height);
  }
  
  // DAVE:  added tga stuff below, stolen from PApplet and PImage
  
  BufferedOutputStream bos=null;

  void startTGA() {
    try {
      //println("pathfilename = " + p.savePath(filename));
      
      File file = new File(p.savePath(filename));
      bos = new BufferedOutputStream(new FileOutputStream(file), 32768);
      byte header[] = new byte[18];
      header[2] = 0x0A;
      header[16] = 24;
      header[17] = 0x20;
      header[12] = (byte) ((int)(width) & 0xff);
      header[13] = (byte) ((int)(width) >> 8);
      header[14] = (byte) ((int)(height) & 0xff);
      header[15] = (byte) ((int)(height) >> 8);
      bos.write(header);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void append(PImage img) {
    img.loadPixels();
    append(img.pixels);
  }

  public void append(int [] pixels) {
    
    // DAVE: start streaming output image
    //    tileFilename+="_"+(p.width*tileNum)+"x"+
    //            (p.height*tileNum)+tileFileextension;
//    if(bos==null)
//      startTGA(tileFilename+tileFileextension);

    try {
      int maxLen = pixels.length;//(int)(height * width);
      int index = 0;
      int col;
      int[] currChunk = new int[128];
      while (index < maxLen) {
        
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
}
