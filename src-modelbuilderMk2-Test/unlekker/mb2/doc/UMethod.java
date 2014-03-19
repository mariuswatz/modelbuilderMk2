package unlekker.mb2.doc;


import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class UMethod extends UDocUtil {
  String name,nameShort;
  String param;
  Method method;
  
  
  
  public UMethod(Method method) {
    super();
    
    this.method=method;
    name=method.getName();
    nameShort=chop(name);
    params.put(TAGNAME,nameShort);
    
    String s=method.toGenericString();
    String tok[]=s.split(" ");
    int id=-1,cnt=0; 
    while(id<0) {
      if(tok[cnt].indexOf(name)>-1) {
        id=cnt;
      }
      cnt++;
    }
    
    s="";
    id--;
    tok[id]=chop(tok[id]);
//    tok[id+1]=chop(tok[id+1]);
    
    for(int j=id; j<tok.length; j++) s+=tok[j]+"\t";
    
    Class<?>[] par=method.getParameterTypes();
    Type[] types = method.getGenericParameterTypes();
    for(int i=0; i<types.length; i++) {
      ParameterizedType pt;
      try {
        pt=(ParameterizedType)types[i];
//        par[i]=
      } catch (Exception e) {
      }
    }
    
    param="";
    
    cnt=0;
    for(Class c:par) {
      
      param+=(cnt>0 ? ',':"")+paramType(method,cnt);
//      log(types[cnt].getClass().);
      cnt++;
    }
    params.put(TAGPARAM,param);

    String ret=method.getReturnType().getSimpleName();
    params.put(TAGRETURN,ret);
    
    System.out.println((tok.length)+" "+(tok.length-id)+"\t" +s);
    
    
  }
  
}