package unlekker.mb2.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import processing.core.PApplet;
import unlekker.mb2.geo.UEdge;

public class UFileStructure extends UMB implements Comparable {
  public boolean isFile;
  public String path;
  public ArrayList<UFileStructure> dir,files;
  public UFileThread thread;
  
  public Date modified;
  public String filename,ext;
  public long b,kb;
  public static String strDiv="-----------",strDirHead="--",strTab="\t";
  public static String strD="D",strF="F";

  public UFileStructure() {
  }

  public UFileStructure(String path) {
    this(new File(path));
  }
  

  public UFileStructure(File file) {
    this();
    isFile=file.isFile();
    path=file.getAbsolutePath().replace('\\', '/');
    
    long t=file.lastModified();
    modified=new Date(t);
    
    if(isFile) {
      filename=UFile.noPath(path);
//      path=UFile.getPath(path);
      path=path.substring(0,path.length()-filename.length());
      ext=UFile.getExt(filename);
      
      b=file.length();
      kb=b/1024;
    }
  }
  
  public String getFullName() {
    return isFile ? path+filename : path;
  }
  
  public String getParentDir() {
      if(isFile) return path;
    
    String tmp=path;
    if(tmp.endsWith(DIRSTR)) tmp=tmp.substring(0,tmp.length()-1);
    tmp=tmp.substring(0,tmp.lastIndexOf(DIRCHAR));
    
    return tmp;
  }
  
  public UFileStructure findDir(String path) {
    UFileStructure d=null;
    
    if(!isFile && this.path.compareTo(path)==0) return this;
    
    if(dir==null || dir.size()<1) return d;
    
    for(UFileStructure df:dir) {
      if(df.path.compareTo(path)==0) return df;
    }
    
    for(UFileStructure df:dir) {
      d=df.findDir(path);
      if(d!=null) return d;
    }
    
    return d;
  }
  
  public UFileStructure add(UFileStructure f) {
    checkLists();
        
    if(f.isFile) {
      f.path=path;
      files.add(f);
      
    }
    else {
      int id=dir.indexOf(f);
      if(id>-1) {
        log("Replacing entry: "+dir.get(id).toString()+" >> "+f.toString());
        dir.set(id, f);
      }
      else dir.add(f);
    }
    
    return this;
  }
  
  public static UFileStructure parse(String s) {
    UFileStructure f=null;
    
    if(s.compareTo(strDiv)==0) return null;
    
    f=new UFileStructure();
    
    s=s.trim();
    
    if(s.startsWith(strDirHead)) {
      s=s.substring(strDirHead.length(),s.indexOf(TAB));
      f.path=s;      
      f.isFile=false;
    }
    else {
      String tok[]=PApplet.split(s,"\t");
      
      //D /Users/david/Dropbox/ 2012.12.13 08:45:31 1355388331000

      int id=0;
      if(tok[0].compareTo(strD)==0) f.isFile=false;
      else if(tok[0].compareTo(strF)==0) f.isFile=true;
      else {
        log("No can parse: "+tok[0]+"\t"+s);
        return null;
      }
      
      id++;
      f.path=tok[id++];
      
//      if(!f.isFile) log(f.path+" isFile="+f.isFile);
      
      if(f.isFile) {
        f.b=Long.parseLong(tok[id++]);      
        f.filename=f.path;
        f.ext=UFile.getExt(f.path);
      }
      Calendar cal=dateStrParse(tok[id++]);
      f.modified=new Date(Long.parseLong(tok[id++]));
      
//      log("cal "+dateStr(cal)+" "+dateStr(f.modified));
    }
    
    return f;
  }
  
  public boolean equals(Object o) {
    if(path==null) {
      log("THIS PATH NULL");
      return false;
    }
    UFileStructure d2=(UFileStructure)o;
    return (d2.path.compareTo(path)==0);
  }

  public UFileStructure process() {
    checkLists();
    
    try {
      File[] f=new File(path).listFiles();
      long filesTotal=0;
      
      if(f!=null) {
        for(File file:f) {
          if(file.isDirectory()) dir.add(new UFileStructure(file));
          else {
            UFileStructure ff=new UFileStructure(file);
            files.add(ff);
            filesTotal+=ff.b;
          }
        }
      }
      
      b=filesTotal;
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
//    logf("processed: %s\n%d %d",path,dir.size(),files.size());
    
//    logDivider(path);
//    if(dir.size()>0) {
//      log("---dir");
//      log(str(dir));
//      
//    }
//    if(files.size()>0) {
//      log("---files");
//      log(str(files));
//    }
    return this;    
  }

  private void checkLists() {
    if(dir!=null) return;
    dir=new ArrayList<UFileStructure>();
    files=new ArrayList<UFileStructure>();
  }
  
  public UFileStructure recurse() {
    thread=new UFileThread(this);
    new Thread(thread).start();
    
    return this;
  }

  public int[] getFileCount() {
    return getFileCount(false);
  }
  
  public int[] getFileCount(boolean inclSubfolders) {
      int cnt[]=new int[] {
        dir==null ? 0 : dir.size(),
        files==null ? 0 : files.size()
    };
    
    if(!inclSubfolders || dir==null) return cnt;
    
    for(UFileStructure d:dir) {
      int[] tmp=d.getFileCount(true);
      cnt[0]+=tmp[0];
      cnt[1]+=tmp[1];
    }
    
    return cnt;
  }

  public long getSize() {
    if(!isFile && files!=null) {
      b=0;
      for(UFileStructure f:files) b+=f.b;
    }
    
    return b;
  }

  public void getData(ArrayList<String> out,int level,int options) {
    String indent="";
    
    getSize();
    if(options==0) { // LIST FOLDER HIERARCHY       
      if(level>-1) {
        for(int i=0; i<level; i++) indent+="  ";
      }
      
      out.add(indent+"-----------");
      out.add(indent+strf("--%s\td=%d\tf=%d\tbytes=%d",
          path,
          dir==null ? 0 : dir.size(),
          files==null ? 0 : files.size(),
          b));
      
      
      if(dir!=null) for(UFileStructure d:dir) out.add(indent+d.toString());
      if(files!=null) for(UFileStructure f:files) out.add(indent+f.toString());      
    }
    else {
      for(UFileStructure f:files) {
        String s=path+TAB+f.toString().substring(2);
        out.add(s);
      }
    }
    
    if(dir!=null) for(UFileStructure d:dir) d.getData(out,level+1,options);    
  }

  
  public ArrayList<UFileStructure> filelist() {
    ArrayList<UFileStructure> l=new ArrayList<UFileStructure>();
    return filelist(l);
  }

  public ArrayList<UFileStructure> filelist(ArrayList<UFileStructure> l) {
    if(files!=null) {
      for(UFileStructure f:files) l.add(f);
    }
    
    if(dir!=null) {
      for(UFileStructure d:dir) d.filelist(l);
    }
    
    return l;
  }

  public ArrayList<String> getData() {
    return getData(0);
  }
  
  public ArrayList<String> getData(int options) {
    ArrayList<String> out=new ArrayList<String>();

    if(options==0) {
      int[] cnt=getFileCount();
      out.add("DIR="+cnt[0]+" FILES="+cnt[1]);
    }
    getData(out,0,options);
    return out;
  }
  
  public String toString() {
    String tstr=null;
    if(modified!=null) tstr=dateStr(modified)+"\t"+modified.getTime();
    if(isFile) {
      return strf("F\t%s\t%d\t%s\t%s\t%s",
          filename,b,UMB.fileSizeStr(b),
          (tstr==null ? "NULL" : tstr),
          path);
//          "F\t"+filename+"\t"+b+"\t"+(tstr==null ? "NULL" : tstr);
      
    }
    
    if(b<1) getSize();
    
    String s=strf("%s\td=%d\tf=%d\tbytes=%d",
        path,
        dir==null ? 0 : dir.size(),
        files==null ? 0 : files.size(),b);
    return "D\t"+s+"\t"+(tstr==null ? "" : tstr);
  }
  
  public boolean done() {
    return thread==null ? false : thread.done;
  }
  

  
  public class UFileThread implements Runnable {
    UFileStructure parent;
    public ArrayList<UFileStructure> queue;
    boolean done;
    public int processed=0;
    
    public UFileThread(UFileStructure parent) {
      this.parent=parent;
      queue=new ArrayList<UFileStructure>();
      add(parent);
    }
    
    public void add(String s) {
      add(new UFileStructure(s));
    }

    public void add(UFileStructure f) {
      if(queue.size()>0 && queue.indexOf(f)>-1) return;
      queue.add(f);
    }
    
    public void run() {
      while(queue.size()>0) {
        UFileStructure curr=queue.get(0);
        
        curr.process();
        if(curr.dir!=null)
          for(UFileStructure d:curr.dir) add(d);
        processed++;
        
        queue.remove(0);
      }
      
      done=true;
    }
    
  }



  public int compareTo(Object o) {
    UFileStructure ff=(UFileStructure)o;
    if(ff.isFile && !isFile) return -1;
    if(isFile && !ff.isFile) return 1;
    
    if(isFile) {
      int cmp=path.compareTo(ff.path);
      if(cmp!=0) return cmp;

      return filename.compareTo(ff.filename);
    }
    
    return path.compareTo(ff.path);
  }

  public File getFile() {
    return new File(getFullName());
  }

}
