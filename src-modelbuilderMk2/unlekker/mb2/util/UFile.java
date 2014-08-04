package unlekker.mb2.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.GZIPOutputStream;

import processing.core.PApplet;

public class UFile implements UConst {
  public static String currDir;
  
  /**
   * Finds the canonical path of the current directory.
   * @return Name of current directoy
   */
  public static String getCurrentDir() {
    try {
      currDir=new File (".").getCanonicalPath();
      currDir=currDir.replace('\\',DIRCHAR);
    }
    catch(Exception e) {
      e.printStackTrace();
      currDir=null;
    }
    return currDir;
  }

  
  
  ///////////////////////////////
  // PApplet methods
  
  /**
   * Returns path prefixed with dataPath from PApplet reference if set,
   * if not returns path unmodified.  
   * @param path
   * @return
   */
  public static String dataPath(String path) {
    PApplet papplet=UMB.getPApplet();
    if(papplet!=null) return papplet.dataPath(path);
    return path;
  }

  /**
   * Returns path prefixed with sketchPath from PApplet reference if set,
   * if not returns path unmodified.
   * @param path
   * @return
   */
  public static String sketchPath(String path) {
    PApplet papplet=UMB.getPApplet();
    if(papplet!=null) return papplet.sketchPath(path);
    return path;
  }
  
  ///////////////////////////////
  // Directory tools
  
  
  public static ArrayList<String> list(String path) {
    return list(path,null,null);
  }

  public static ArrayList<String> list(String path,String pre) {
    return list(path,pre,null);
  }

  public static ArrayList<String> listByExtension(String path,String ext) {
    return list(path,null,ext);
  }

  public static ArrayList<String> list(String path,String pre,String ext) {
    ArrayList<String> l=null;
    String [] ls=null;
    
    try {
      File f=new File(path);
      l=new ArrayList<String>();

      if(f.exists() && f.isDirectory()) {
        path=fixPath(path);
        
        if(pre!=null || ext!=null) {
          FilenameFilterPreExt filter=new FilenameFilterPreExt(pre, ext);
          
          ls=f.list(filter);
        }
        else ls=f.list();
        
        if(l!=null && ls!=null) for(String fs:ls) l.add(fs);
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      UMB.log(e.getMessage());
      e.printStackTrace();
    }
    
    return l;
  }


  public static String nextFilename(String path,String prefix) {
    return nextFile(path, prefix);
  }
  /**
   * Generates auto-incrementing filename based on the provided
   * path and file prefix (file extension is disregarded.) 
   * @param path
   * @param prefix
   * @return
   */
  public static String nextFile(String path,String prefix) {
    return nextFile(path, prefix,null);
  }

  /**
   * Generates auto-incrementing filename based on the provided
   * path, file prefix and extension (file extension is disregarded if
   * <code>ext==null</code>.) 
   * 
   * It is recommended to call <code>UBase.setPApplet()</code> before use so
   * that <code>dataPath()</code> and {@link sketchPath()} can use 
   * information from PApplet, otherwise the directory where the
   * application is launched is used as a root.

   * @param path
   * @param prefix
   * @param ext
   * @return
   */
  public static String nextFile(String path,String prefix,String ext) {
    
    path=getAbsolutePath(path);
    
    int last=lastIndex(path, prefix, ext)+1;
    
    String s=path+prefix.trim()+" "+UMB.nf(last,4);
    
    if(ext!=null) {
      if(!ext.startsWith(".")) ext="."+ext;
      s+=ext;
    }
    
    return s;
  }

  
  public static String last(String path,String pre,String ext) {
    String s=null;

    ArrayList<String> l=list(path,pre,ext);
    s=l.get(l.size()-1);
    return s;
  }

  public static int lastIndex(String path,String pre) {
    return lastIndex(path, pre, null);
  }

  public static int lastIndex(String path,String pre,String ext) {
    String s=null;
    int i=-1;
    
    try {
      ArrayList<String> l=list(path,pre,ext);
//      UMB.log(l);
      if(l.size()<1) return -1;
        
      int n=l.size()-1;
      int res=-1;
      do {
        res=UMB.max(getNumber(l.get(n--)),res);
      } while(n>-1);
      
      return res;      
    } catch (Exception e) {
      // TODO Auto-generated catch block
//      e.printStackTrace();
      i=-1;
    }
    
    return i;
  }
  
  
  
  ///////////////////////////////
  // File streams
  
  private static int getNumber(String s) {
    s=noExt(s);
    int pos=s.length()-1;
    if(Character.isDigit(s.charAt(pos))) {
      while(pos>0 && Character.isDigit(s.charAt(pos-1))) pos--;
      s=s.substring(pos);
      return UMB.parseInt(s);
    }
    return -1000;
  }



  public static OutputStream getOutputStream(String filename) {
    return getOutputStream(filename,false);
  }

  public static OutputStream getOutputStream(String filename,boolean append) {
    OutputStream out=null;

    try {
      filename=getAbsolutePath(filename);   
          
      UMB.logDivider(filename);
      new File(getPath(filename)).mkdirs();
      
      if(filename.endsWith("gz")) {
        out=new GZIPOutputStream(new FileOutputStream(filename));
      }
      else out=new FileOutputStream(filename,append);
    } catch (Exception e) {   
      e.printStackTrace();
      UMB.logErr("getOutputStream - "+e.toString());
    }
    return out;
  }


  ///////////////////////////////
  // File name manipulation

  /**
   * Returns extension of file name
   * @param name
   * @return Extension
   */
  public static String getExt(String name) {
    String ext=null;
    
    int pos=name.lastIndexOf('.');
    if(pos>0) ext=name.substring(pos+1).toLowerCase();
    
    return ext;
  }

  /**
   * Checks if file has given extension ("jpg","png" etc.)
   * @param name
   * @return
   */
  public static boolean hasExt(String name,String ext) {
    String ext2=getExt(name);
    return ext.toLowerCase().equals(ext2);
  }

  /**
   * Fixes path String: DOS-style '\' becomes '/', also appends '/'
   * if string does not aready have one.
   * @param path
   * @return
   */
  public static String fixPath(String path) {
    if(path==null) return null;
    
    path=path.indexOf(DIRCHARDOS)<0 ? path: path.replace(DIRCHARDOS,DIRCHAR);
    if(isDir(path) && !path.endsWith(DIRSTR)) path+=DIRSTR;
//    if(!path.endsWith(DIRSTR)) path+=DIRSTR;
    
    return path;
  }
  
  public static String [] getPathElements(String filename) {
    String res[]=new String[3];
    res[0]=getPath(filename);
    
    filename=noPath(filename);
    res[1]=noExt(filename);
    res[2]=getExt(filename);

    return res;
  }

  public static String getPath(String path) {
    if(path==null) return null;
    path=fixPath(path);

    if(!isDir(path)) {
      int pos=path.lastIndexOf(DIRCHAR);
      if(pos>-1) path=path.substring(0,path.lastIndexOf(DIRCHAR)); 
    }
    
    if(!path.endsWith(DIRSTR)) path+=DIRSTR;  
    
    return path;
  }

  public static boolean isFiletype(UFileStructure f,int filetype) {
    return isFiletype(f.filename, filetype);
  }

  public static boolean isFiletype(String path,int filetype) {
    String ext[];
    String fileExt=getExt(path);
    if(fileExt==null) return false;
    
    if(filetype==FILETYPEDOC) ext=EXTDOCS;
    else if(filetype==FILETYPEIMG) ext=EXTIMAGES;
    else if(filetype==FILETYPEMOV) ext=EXTMOVIES;
    else if(filetype==FILETYPEARCHIVE) ext=EXTARCHIVE;
    else return false;
    
    for(String s:ext) if(s.compareTo(fileExt)==0) return true;
    
    return false;
  }

  public static boolean isImage(String path) {
    boolean ok=false;
    
    String ext=getExt(path);
    if(ext!=null) {
      for(String s:EXTIMAGES) if(s.compareTo(ext)==0) return true;
    }
    
    return ok;
  }

  public static boolean isMovie(String path) {
    boolean ok=false;
    
    String ext=getExt(path);
    if(ext!=null) {
      for(String s:EXTMOVIES) if(s.compareTo(ext)==0) return true;
    }
    
    return ok;
  }

  public static boolean isDir(String path) {
    boolean ok=false;
    
    try {
      ok=new File(path).isDirectory();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      ok=false;
//      e.printStackTrace();
    }
    
    
    return ok;
  }

  public static boolean isAbsolutePath(String path) {
    boolean ok=false;
    
    try {
      ok=new File(path).isAbsolute();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      ok=false;
//      e.printStackTrace();
    }
    
    
    return ok;
  }

  /**
   * Returns the file name sans extension
   * @param name
   * @return File name sans extension
   */
  public static String noExt(String name) {
    int pos;
    
    name=fixPath(name);
    
    
    pos=name.lastIndexOf(".");
    if(pos==-1) return name;
    return name.substring(0,pos);
  }

  /**
   * Returns filename sans path.
   * @param name
   * @return Filename sans  path
   */
  public static String noPath(String name) {
//    int pos=name.lastIndexOf('\\');
//    int pos2=name.lastIndexOf('/');
//    if(pos<0 || (pos2>-1 && pos2<pos)) pos=pos2;
    
    name=fixPath(name);
    if(isDir(name)) return null;
    
    int pos=name.lastIndexOf(DIRCHAR);
    if(pos<0) return name;
    
//    UMB.log(name+" "+name.substring(pos+1));
    return name.substring(pos+1);
  }
  
  public static String getAbsolutePath(String name) {
    PApplet papplet=UMB.getPApplet();
    String path;
    
    path=fixPath(name);
//    UMB.log("name in: "+name+" > "+path);
    
    if(isAbsolutePath(path)) return path;
    
    name=noPath(path);
    path=getPath(path);
    
    File f=new File(path+name);    
//    UMB.log("path = "+path+" \nname="+name+" "+f.isAbsolute());

    
    if(!f.isAbsolute()) {
      if(papplet!=null) {
        path=papplet.sketchPath(path);
      }
      else {
        if(currDir==null) currDir=getCurrentDir();
        path=currDir+DIRCHAR+path;
      }
    }

    File ff=new File(path);
    ff.mkdirs();
    if(!path.endsWith(DIRSTR)) path+=DIRSTR;
//    UMB.log("ff "+ff.getAbsolutePath()+" "+name);
//    ff.
//    if(debugLevel>2) Util.log("IO.getAbsolutePath "+s);
    return path+name;
  }


  ///////////////////////////////
  // File name filter
  
  static class FilenameFilterPreExt implements FilenameFilter {
    String pre,ext;
    
    public FilenameFilterPreExt(String pre,String ext) {
      this.pre=pre;
      this.ext=(ext==null ? null : ext.toLowerCase());
    }
        
    
    public boolean accept(File dir, String name) {
      boolean ok=true;
//      UBasic.log(">> "+dir.toString()+"|"+name);
      
      if(pre!=null) {
        if(!name.startsWith(pre)) ok=false;
      }
      
      if(ok && ext!=null) {
         if(!name.toLowerCase().endsWith(ext)) ok=false;
      }
      
      return ok;
    }
    
  }

  
}
