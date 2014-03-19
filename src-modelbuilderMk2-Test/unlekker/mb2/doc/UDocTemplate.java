package unlekker.mb2.doc;


import java.lang.reflect.Method;
import java.util.*;

import processing.core.*;
import unlekker.mb2.util.*;
import unlekker.mb2.externals.*;
import unlekker.mb2.geo.*;

public class UDocTemplate extends UDocUtil {
  ArrayList<String> pre,post,body;
  
  public UDocTemplate(String filename) {
    pre=new ArrayList<String>();
    post=new ArrayList<String>();
    body=new ArrayList<String>();
    
    filename=path+"/templ/"+filename;
    
    String str[]=papplet.loadStrings(filename);
    int breakCnt=0;
    
    for(String s:str) {
      if(s.startsWith("--")) breakCnt++;
      else {
        if(breakCnt==0) pre.add(s);
        if(breakCnt==1) body.add(s);
        if(breakCnt==2) post.add(s);
      }
    }
    
//    logDivider("pre "+str(pre));
//    logDivider("body "+str(body));
//    logDivider("post "+str(post));
  }

  public void add(UDocUtil o,ArrayList<String> str,ArrayList<String> out) {
    for(String tmp:str) {
      tmp=process(o,tmp);
      out.add(tmp);
    }
  }

  public String process(UDocUtil o,String s) {
    String old=s;
    
    if(s==null) return "";
    int pos[]=getTag(s);
    try {
      while(pos!=null) {
        String tag=s.substring(pos[0],pos[1]+1);
        String tmp=s.substring(0,pos[0]);
        tmp+=o.get(tag);
//      tmp+="TAG="+tag.substring(1,tag.length()-1);
        tmp+=s.substring(pos[1]+1);
        
        s=tmp;
        pos=getTag(s);
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      log(pos[0]+" "+pos[1]+" "+old);
    }
    return s;
  }
  
  public int[] getTag(String s) {
    int pos=s.indexOf('[');
    if(pos<0) return null;
    int pos2=s.indexOf(']',pos);
    if(pos2<0) {
      log("Fail: "+s);
      pos2=pos+2;
    }
    return new int[] {pos,pos2};
  }
  
  public void add(ArrayList<String> str,ArrayList<String> out) {
    out.addAll(str);
  }
  
  public void output(UDocUtil o,ArrayList<String> out) {
    add(o,pre,out);
    add(o,body,out);
    add(o,post,out);
  }
  
}