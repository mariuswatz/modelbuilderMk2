package unlekker.mb2.test.data;

import java.util.ArrayList;

import unlekker.test.UTest;
import unlekker.data.*;
import java.util.*;

public class UTestDataLorem extends UTest {
  Set<String> words;
  String path,filename;
  
  public void init() {
    path="C:/Users/marius/Dropbox/23 Data - optional/";
    filename="Cicero-De finibus bonorum et malorum-liber-primus.txt";
    collect(filename);
    
    filename="names_male.txt";
    collect(filename);
  }

  void collect(String filename) {
    ArrayList<String> tmp=new ArrayList<String>();
    String input[]=p.loadStrings(path+filename);
    
    String data=p.join(input,' ');
    StringBuffer buf=new StringBuffer();
    for(char c:data.toCharArray()) {
      if(c==' ' || Character.isLetter(c)) buf.append(c);  
    }
    
    int counts[]=new int[100];
    String tok[]=p.split(buf.toString().toLowerCase(), " ");
    for(String s : tok) if(s.length()>0){
      if(!tmp.contains(s)) {
        tmp.add(s);
        counts[s.length()]++;
      }
    }

    Collections.sort(tmp);
    
    log(tmp.size()+" "+UMB.str(tmp,true).replace('\t',','));
    for(int i=0; i<counts.length; i++) {
      if(counts[i]>0) log(i+" "+counts[i]); 
    }
    
    
    for(String s:tmp) {
      
    }

  }
  
}
