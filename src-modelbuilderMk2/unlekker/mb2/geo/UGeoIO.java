/*
 * modelbuilderMk2
 */
package unlekker.mb2.geo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import processing.core.PApplet;
import unlekker.mb2.util.UMB;
import unlekker.mb2.util.UFile;


public class UGeoIO extends UMB {
  public static int STLCOLORDEFAULT=0,STCOLORMATERIALISE=1;
  
  private static ByteBuffer buf= null;
  private static FileOutputStream out;
  
  
  
  /////////////////////////////////////////////
  // FUNCTIONS FOR STL INPUT
  
  public static UGeo readSTL(String path) {
    if(checkPAppletSet()) {
      return readSTL(papplet,path);
    }
    return null;
  }

  public static UGeo readSTL(PApplet p,String path) {
    byte [] header,byte4;
    ByteBuffer buf;
    int num=0,step,stepMult;
    float vv[]=new float[12];
    File file=null;
    String filename;
    UGeo geo=null;
    geo=new UGeo();//.enable(NODUPL);

//    UProgressInfo progress=new UProgressInfo();
    float lastPerc=-5;
    
    timerStart(99);

    header=new byte[80];
    byte4=new byte[4];
    FileInputStream in= null;
    
    try { 
      if (path != null) {
        filename=path;
        file = new File(path);
        
        if(!file.exists()) {
          UMB.logErr("UGeoIO.readSTL: File not found "+file.getName());
          return null;
        }
        
        if (!file.isAbsolute() && checkPAppletSet()) file=new File(p.savePath(path));
        if (!file.isAbsolute()) 
          throw new RuntimeException("readSTL requires an absolute path " +
          "for the location of the input file.");
      }
      
      in=new FileInputStream(file);
      logDivider("\n\nReading "+file.getName()+" ("+fileSizeStr(file)+")");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    } 

    try { 
      in.read(header);
      
      // test if ASCII STL
      String asciiTest="";
      for(int i=0; i<5; i++) asciiTest+=(char)header[i];
      
      if(asciiTest.startsWith("solid")) { // IS ASCII STL
        String dat;
        
        in.close();
        
        in=new FileInputStream(file);
        BufferedReader read = new BufferedReader(new InputStreamReader(in, "UTF-8"));       
        dat=read.readLine();
//        UMB.log(dat);
        
        UVertex v[]=new UVertex[] {new UVertex(),new UVertex(),new UVertex()};
        String tok[],FACET="facet",SPACESTR=" ";

        int tokid=0;
        while(dat!=null) { 
          dat=read.readLine(); // should be "facet"
          if(dat==null || !dat.contains(FACET)) { // error or "endsolid"
//            UMB.log(dat);
            dat=null;
            read.close();
          }
          else {
            dat=read.readLine(); // "outer loop"
            
            for(int i=0; i<3; i++) {
              dat=read.readLine().trim();  // "vertex x y z"
              tok=dat.split(SPACESTR);
              
//              UMB.log(tok);
              tokid=1;
//              if(tok[tokid].length()<1 || !Character.isDigit(tok[tokid].charAt(0))) tokid++;
              v[i].set(
                  UMB.parseFloat(tok[tokid++]),
                  UMB.parseFloat(tok[tokid++]),
                  UMB.parseFloat(tok[tokid++]));
            }
            
            if(geo.sizeF()%1000==0) UMB.log(geo.sizeF()+" faces read. "+timerElapsed(99)+" msec");
            geo.addFace(v);
            
            dat=read.readLine(); // "end loop"
            dat=read.readLine(); // "end facet"
          }         
        }
      }
      else { // BINARY STL
        in.read(byte4);
        buf = ByteBuffer.wrap(byte4);
        buf.order(ByteOrder.nativeOrder());
        num=buf.getInt();
        
        UMB.log("Polygons to read: "+num);

        header=new byte[50];      
        
//        progress.start();
        int id=0;
        
        for(int i=0; i<num; i++) {
          in.read(header);
          buf = ByteBuffer.wrap(header);
          buf.order(ByteOrder.nativeOrder());
          buf.rewind();
          
          for(int j=0; j<12; j++) vv[j]=buf.getFloat();
          id=3;
          if(geo.sizeF()%1000==0) UMB.log(geo.sizeF()+" faces read. "+timerElapsed(99)+" msec");
          geo.addFace(
            new UVertex[] {
              new UVertex(vv[id++],vv[id++],vv[id++]),
              new UVertex(vv[id++],vv[id++],vv[id++]),
              new UVertex(vv[id++],vv[id++],vv[id++])
            });


//          progress.update(p, 100f*(float)i/(float)(num-1));
//          if(progress.perc-lastPerc>5 || progress.perc>99.9f) {
//            lastPerc=progress.perc;
//            log(geo.faceNum+" faces read, "+progress.lastUpdate);
//          }
        }
      } // END READ BINARY

      String elapseStr="Time: "+timerEnd(99)+" msec";
      if(num>0)
        UMB.log(geo.sizeF()+" faces read ("+num+" reported in file) - "+elapseStr);
      else 
        UMB.log(geo.sizeF()+" faces read - "+elapseStr);

      UMB.log(geo.sizeV()+" vertices | "+ geo.bb().str());
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
    return geo;
    
  }

  public static boolean writeSTL(String filename,UGeo model) {
    return writeSTL(filename, model,-1);
  }

  public static boolean writeSTL(String filename,UGeo model,int colorType) {
    boolean res=false;
    int faceNum=model.sizeF();
    
    if(model.tainted) model.check();

    try {
      getSTLOut(filename, faceNum);
      writeSTLFaces(model.getF(),colorType);

      out.flush();
      out.close();
      UMB.log("Closing '"+filename+"'. "+faceNum+" triangles written.\n");
    } catch (Exception e) {
      res=false;
      e.printStackTrace();
    }
    
    return res;
  }

  public static boolean writeSTL(String filename,ArrayList<UGeo> models) {
    return writeSTL(filename,models,-1);
  }

  public static boolean writeSTL(String filename,ArrayList<UGeo> models,int colorType) {
    boolean res=false;
    int faceNum=UGeo.sizeF(models);
    
    try {
      getSTLOut(filename, faceNum);
      for(UGeo theModel:models) {
        if(theModel.tainted) theModel.check();

        writeSTLFaces(theModel.getF(),colorType);
      }

      out.flush();
      out.close();
      UMB.log("Closing '"+filename+"'. "+faceNum+" triangles written.\n");
    } catch (Exception e) {
      res=false;
      e.printStackTrace();
    }
    
    return res;
  }
  
  

  private static String getSTLOut(String filename,int faceNum) throws IOException {
    if(!filename.toLowerCase().endsWith("stl")) filename+=".stl";


    out=(FileOutputStream)UFile.getOutputStream(filename);
    writeSTLHeader(faceNum);
    UMB.logDivider("Writing STL '"+filename+"' "+faceNum);
    
    return filename;
  }

  private static void writeSTLFaces(ArrayList<UFace> ff,int colorType) throws IOException {
    byte[] header=new byte[50];
    
    for(UFace f:ff) {
      UVertex v[]=f.getV();
      UVertex fn=f.normal();
      
      buf.rewind();
      buf.putFloat(fn.x);
      buf.putFloat(fn.y);
      buf.putFloat(fn.z);
      
//      for(int j=0; j<3; j++) {
//        buf.putFloat(v[j].x);
//        buf.putFloat(v[j].y);
//        buf.putFloat(v[j].z);
//      }
      for(int j=2; j>-1; j--) {
        buf.putFloat(v[j].x);
        buf.putFloat(v[j].y);
        buf.putFloat(v[j].z);
      }
      
      if(colorType>-1) {
        int col=formatRGB(f.col,colorType);
        byte a=(byte)(col&0xff);
        byte b=(byte)(col>>8 &0xff);
        short cshort=(short)((b<<8) | a);
        buf.putShort(cshort);
      }
      
      buf.rewind();
      buf.get(header);
      out.write(header);
    }
  }

  private static int formatRGB(int rgb,int type) {
    if(type==STLCOLORDEFAULT) {
      int col15bits = (rgb >> 3 & 0x1f);
      col15bits |= (rgb >> 11 & 0x1f) << 5;
      col15bits |= (rgb >> 19 & 0x1f) << 10;
      col15bits |= 0x8000;
      return col15bits;
    }
    
    int col15bits = (rgb >> 19 & 0x1f);
    col15bits |= (rgb >> 11 & 0x1f) << 5;
    col15bits |= (rgb >> 3 & 0x1f) << 10;
    return col15bits;

    
}


  private static void writeSTLHeader(int faceNum) throws IOException {
    byte[] header;
    buf = ByteBuffer.allocate(200);
    
    header=new byte[80];
    buf.get(header,0,80);
    out.write(header);
    buf.rewind();

    buf.order(ByteOrder.LITTLE_ENDIAN);
    
    buf.putInt(faceNum);
    buf.rewind();
    buf.get(header,0,4);
    out.write(header,0,4);
    buf.rewind();
    buf.clear();
  }
}
