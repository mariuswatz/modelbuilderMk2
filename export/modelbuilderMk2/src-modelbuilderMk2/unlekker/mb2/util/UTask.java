package unlekker.mb2.util;

import java.util.ArrayList;
import java.util.HashMap;

public class UTask extends UMB {
  public ArrayList<UTaskEvent> ev;
  public String name;
  public long start,elapsed;
  public int startFrame,elapsedFrames;
  int tD;
  long tLast;
  public float fps;
  boolean firstMsg=true;
  
  public Thread thread;
  public ArrayList<String> stack,log;
  
  public HashMap<String, Integer> data;
  
  
  public UTask(String taskName) {
    name=taskName;
    ev=new ArrayList<UTask.UTaskEvent>();
    log=new ArrayList<String>();
    update(0);
    start=ev.get(0).t;
//    tLast=start;
    
    if(papplet!=null) startFrame=papplet.frameCount;
  }
  
  public UTask(String taskName,boolean threadInfo) {
    this(taskName);
    if(threadInfo) getStackInfo();
  }

  public ArrayList<String> getStackInfo() {
    return getStackInfo(true);
  }

  public ArrayList<String> getStackInfo(boolean includeClassName) {
    if(stack==null) stack=new ArrayList<String>();
    else stack.clear();
    
    try {
      thread=Thread.currentThread();
      
      StackTraceElement[] el = thread.getStackTrace();

      int cnt=0;
      for(StackTraceElement st:el) {
        if(cnt++>1) {
          String cl=null;
          if(includeClassName) {
            cl=st.getClassName();
            cl=cl.substring(cl.lastIndexOf(DOTSTR)+1);
          }
          stack.add(
              cl+DOTSTR+
              st.getMethodName());
        }
      }
     }
     catch (Exception e) {
      // TODO Auto-generated catch block
      thread=null;
      e.printStackTrace();
    }
    
    return stack;
  }
  
  public void clear() {
    log.clear();
    for(String key:data.keySet()) {
      data.put(key,0);
    }
  }

  public void addLogUnique(String s) {
    for(String tmp:log) if(s.compareTo(tmp)==0) return;
    log.add(s);
  }

  public void addLog(String s) {
    log.add(elapsed()+" "+s);    
  }

  public void addData(String key,int val) {
    if(data==null) data=new HashMap<String, Integer>();
    data.put(key, val);
  }

  public long elapsed() {
    return System.currentTimeMillis()-start;
  }

  public int getData(String key) {
    if(data==null || !data.containsKey(key)) return -1;
    return data.get(key);
  }
  
  public String strData() {
    if(data==null) return "No data.";
    
    String s=name+" data: ";
    for(String key:data.keySet()) {
      s+=key+"="+getData(key)+TAB;
      
    }
    return s;
    
  }

  public void increment(String key) {
    increment(key,1);
  }

  public void increment(String key,int inc) {
    if(data==null) {
      addData(key, 0);
    }
    else {
      int val=data.containsKey(key) ? data.get(key)+inc : 0;
//      if(val%50000==0) log(key+"="+val);
      data.put(key, val);
    }
  }

  public void update(float perc) {
    update(perc, null,false);
  }

  public void update(float perc,String msg) {
    update(perc,null,false);
  }
  
  public void update(float perc,String msg,boolean force) {
    ev.add(new UTaskEvent(perc,msg));
    
    long tt=ev.get(ev.size()-1).t;
    elapsed=tt-start;
    
    if(perc<EPSILON) return;
    
    float tSinceLast=elapsed-tLast;
    
    if(tSinceLast>500 || force) {
      
      if(firstMsg) {
        logDivider(NEWLN+name+": "+ev.get(0).str());
        firstMsg=false;
      }
      
      if(msg==null) 
        logf("%s: %d%% - %.1f sec",name,(int)(perc),
          elapsed/1000f);
      else logf("%s: %d%% - %.1f sec | %s",
          name,(int)(perc),
          elapsed/1000f,
          msg);      
      
      tLast=elapsed;
    }
    
    if(papplet!=null) {
      elapsedFrames=papplet.frameCount-startFrame;
    }
  }

  public void done() {
    ev.add(new UTaskEvent(1));
    elapsed=ev.get(ev.size()-1).t-start;
    
    if(elapsed>1000) {
      
      if(papplet!=null) {
        elapsedFrames=papplet.frameCount-startFrame;
        fps=elapsed;
        fps=1000f/(fps/(float)elapsedFrames);
        
      }
      
      if(elapsedFrames>0) {
        logf(name+": Done - Elapsed: %.1f sec | frames= %d, fps= %f",
            elapsed/1000f,elapsedFrames,fps);
      }
      else {
        logf(name+": Done - Elapsed: %.1f sec",
            elapsed/1000f);
        logDivider();

      }
    }
  }
  
  public class UTaskEvent {
    public long t,tRel,tD=-1;
    public float perc;
    public int frame=0;
    public String msg=null; 

    public UTaskEvent(float f,String msg) {
      this(f);
      this.msg=msg;
    }

    public UTaskEvent(float f) {
      t=System.currentTimeMillis();
      tRel=elapsed;
      
      if(ev.size()>0) {
        tD=t-ev.get(ev.size()-1).t;
      }
      perc=f;
      if(papplet!=null) frame=papplet.frameCount;
    }

    public String str() {
      // TODO Auto-generated method stub
        return strf("%s %d%% t=%d tD=%d frame=%d",
            (msg==null ? "" : msg+": "),
            (int)perc,(int)(tRel/1000f),(int)tD,frame); 
    }
    
  }
}
