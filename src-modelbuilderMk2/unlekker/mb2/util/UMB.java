package unlekker.mb2.util;

import java.io.File;
import java.lang.Character.Subset;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

import processing.core.PApplet;
import processing.core.PGraphics;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;

/**
 * 
 * UMB is the base class for most of the classes in ModelbuilderMk2, meaning that
 * they all extend this class and thus inherit the capabilities it provides. This
 * includes convenient tools for common computational tasks (math, string formatting etc), 
 * as well as a mechanism to set and share a PApplet instance provided by the user.
 * 
 * Call {@see UMB#setPApplet(PApplet)} or {@see UMB#setGraphics(PGraphics)} to provide a PApplet 
 * or PGraphics instance,which can then be used by geometry classes like {@see UGeo} and {@see UVertexList}
 * for rendering etc.
 * 
 * 
 * 
 * @author <a href="https://github.com/mariuswatz">Marius Watz</a>
 *
 */
public class UMB implements UConst {

  /**
   * Options like <code>NODUPL</code>, <code>NOCOPY</code> etc. encoded as bit flags stored as an int. Options can be
   * toggled and checked with <code>enable()</code> and <code>isEnabled()</code>
   */
  public int options;
  
  private static String[] optionNames;
  protected static UMB UMB;
  protected static PApplet papplet=null;
  protected static PGraphics g;
  protected static PGraphics g3d;
  protected static boolean isGraphics3D;
  
  protected static int gErrorCnt=0;
  
  protected static long timerData[]=new long[300],timerTask[];
  protected static String taskName;
  
  public static HashMap<Integer, String> groupTypeNames;
  public static SimpleDateFormat dateStrFormat;
  
  protected static boolean libraryPrinted=false;
  
  public static int DEBUGLEVEL=10;
  
  static {
    if(!libraryPrinted) {
      UMBStartTime=System.currentTimeMillis();
//      UMB.logDivider(VERSION);
//      UMB.log(CREDIT);
//      UMB.logDivider();
      
      libraryPrinted=true;
      dateStrFormat=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss",Locale.US);
    }
    
  }

  //////////////////////////////////////////////////
  
  public static HashMap<Integer,String> keyNames;
  

  static {
    keyNames=new HashMap<Integer,String>();
    keyNames.put(KEY_ENTER,"KEY_ENTER");
    keyNames.put(KEY_BACK_SPACE,"KEY_BACK_SPACE");
    keyNames.put(KEY_TAB,"KEY_TAB");
    keyNames.put(KEY_CANCEL,"KEY_CANCEL");
    keyNames.put(KEY_CLEAR,"KEY_CLEAR");
    keyNames.put(KEY_SHIFT,"KEY_SHIFT");
    keyNames.put(KEY_CONTROL,"KEY_CONTROL");
    keyNames.put(KEY_ALT,"KEY_ALT");
    keyNames.put(KEY_PAUSE,"KEY_PAUSE");
    keyNames.put(KEY_CAPS_LOCK,"KEY_CAPS_LOCK");
    keyNames.put(KEY_ESCAPE,"KEY_ESCAPE");
    keyNames.put(KEY_SPACE,"KEY_SPACE");
    keyNames.put(KEY_PAGE_UP,"KEY_PAGE_UP");
    keyNames.put(KEY_PAGE_DOWN,"KEY_PAGE_DOWN");
    keyNames.put(KEY_END,"KEY_END");
    keyNames.put(KEY_HOME,"KEY_HOME");
    keyNames.put(KEY_LEFT,"KEY_LEFT");
    keyNames.put(KEY_UP,"KEY_UP");
    keyNames.put(KEY_RIGHT,"KEY_RIGHT");
    keyNames.put(KEY_DOWN,"KEY_DOWN");
    keyNames.put(KEY_COMMA,"KEY_COMMA");
    keyNames.put(KEY_MINUS,"KEY_MINUS");
    keyNames.put(KEY_PERIOD,"KEY_PERIOD");
    keyNames.put(KEY_SLASH,"KEY_SLASH");
    keyNames.put(KEY_0,"KEY_0");
    keyNames.put(KEY_1,"KEY_1");
    keyNames.put(KEY_2,"KEY_2");
    keyNames.put(KEY_3,"KEY_3");
    keyNames.put(KEY_4,"KEY_4");
    keyNames.put(KEY_5,"KEY_5");
    keyNames.put(KEY_6,"KEY_6");
    keyNames.put(KEY_7,"KEY_7");
    keyNames.put(KEY_8,"KEY_8");
    keyNames.put(KEY_9,"KEY_9");
    keyNames.put(KEY_SEMICOLON,"KEY_SEMICOLON");
    keyNames.put(KEY_EQUALS,"KEY_EQUALS");
    keyNames.put(KEY_A,"KEY_A");
    keyNames.put(KEY_B,"KEY_B");
    keyNames.put(KEY_C,"KEY_C");
    keyNames.put(KEY_D,"KEY_D");
    keyNames.put(KEY_E,"KEY_E");
    keyNames.put(KEY_F,"KEY_F");
    keyNames.put(KEY_G,"KEY_G");
    keyNames.put(KEY_H,"KEY_H");
    keyNames.put(KEY_I,"KEY_I");
    keyNames.put(KEY_J,"KEY_J");
    keyNames.put(KEY_K,"KEY_K");
    keyNames.put(KEY_L,"KEY_L");
    keyNames.put(KEY_M,"KEY_M");
    keyNames.put(KEY_N,"KEY_N");
    keyNames.put(KEY_O,"KEY_O");
    keyNames.put(KEY_P,"KEY_P");
    keyNames.put(KEY_Q,"KEY_Q");
    keyNames.put(KEY_R,"KEY_R");
    keyNames.put(KEY_S,"KEY_S");
    keyNames.put(KEY_T,"KEY_T");
    keyNames.put(KEY_U,"KEY_U");
    keyNames.put(KEY_V,"KEY_V");
    keyNames.put(KEY_W,"KEY_W");
    keyNames.put(KEY_X,"KEY_X");
    keyNames.put(KEY_Y,"KEY_Y");
    keyNames.put(KEY_Z,"KEY_Z");
    keyNames.put(KEY_OPEN_BRACKET,"KEY_OPEN_BRACKET");
    keyNames.put(KEY_BACK_SLASH,"KEY_BACK_SLASH");
    keyNames.put(KEY_CLOSE_BRACKET,"KEY_CLOSE_BRACKET");
    keyNames.put(KEY_NUMPAD0,"KEY_NUMPAD0");
    keyNames.put(KEY_NUMPAD1,"KEY_NUMPAD1");
    keyNames.put(KEY_NUMPAD2,"KEY_NUMPAD2");
    keyNames.put(KEY_NUMPAD3,"KEY_NUMPAD3");
    keyNames.put(KEY_NUMPAD4,"KEY_NUMPAD4");
    keyNames.put(KEY_NUMPAD5,"KEY_NUMPAD5");
    keyNames.put(KEY_NUMPAD6,"KEY_NUMPAD6");
    keyNames.put(KEY_NUMPAD7,"KEY_NUMPAD7");
    keyNames.put(KEY_NUMPAD8,"KEY_NUMPAD8");
    keyNames.put(KEY_NUMPAD9,"KEY_NUMPAD9");
    keyNames.put(KEY_MULTIPLY,"KEY_MULTIPLY");
    keyNames.put(KEY_ADD,"KEY_ADD");
    keyNames.put(KEY_SEPARATER,"KEY_SEPARATER");
    keyNames.put(KEY_SEPARATOR,"KEY_SEPARATOR");
    keyNames.put(KEY_SUBTRACT,"KEY_SUBTRACT");
    keyNames.put(KEY_DECIMAL,"KEY_DECIMAL");
    keyNames.put(KEY_DIVIDE,"KEY_DIVIDE");
    keyNames.put(KEY_DELETE,"KEY_DELETE");
    keyNames.put(KEY_NUM_LOCK,"KEY_NUM_LOCK");
    keyNames.put(KEY_SCROLL_LOCK,"KEY_SCROLL_LOCK");
    keyNames.put(KEY_F1,"KEY_F1");
    keyNames.put(KEY_F2,"KEY_F2");
    keyNames.put(KEY_F3,"KEY_F3");
    keyNames.put(KEY_F4,"KEY_F4");
    keyNames.put(KEY_F5,"KEY_F5");
    keyNames.put(KEY_F6,"KEY_F6");
    keyNames.put(KEY_F7,"KEY_F7");
    keyNames.put(KEY_F8,"KEY_F8");
    keyNames.put(KEY_F9,"KEY_F9");
    keyNames.put(KEY_F10,"KEY_F10");
    keyNames.put(KEY_F11,"KEY_F11");
    keyNames.put(KEY_F12,"KEY_F12");
    keyNames.put(KEY_F13,"KEY_F13");
    keyNames.put(KEY_F14,"KEY_F14");
    keyNames.put(KEY_F15,"KEY_F15");
    keyNames.put(KEY_F16,"KEY_F16");
    keyNames.put(KEY_F17,"KEY_F17");
    keyNames.put(KEY_F18,"KEY_F18");
    keyNames.put(KEY_F19,"KEY_F19");
    keyNames.put(KEY_F20,"KEY_F20");
    keyNames.put(KEY_F21,"KEY_F21");
    keyNames.put(KEY_F22,"KEY_F22");
    keyNames.put(KEY_F23,"KEY_F23");
    keyNames.put(KEY_F24,"KEY_F24");
    keyNames.put(KEY_PRINTSCREEN,"KEY_PRINTSCREEN");
    keyNames.put(KEY_INSERT,"KEY_INSERT");
    keyNames.put(KEY_HELP,"KEY_HELP");
    keyNames.put(KEY_META,"KEY_META");
    keyNames.put(KEY_BACK_QUOTE,"KEY_BACK_QUOTE");
    keyNames.put(KEY_QUOTE,"KEY_QUOTE");
    keyNames.put(KEY_KP_UP,"KEY_KP_UP");
    keyNames.put(KEY_KP_DOWN,"KEY_KP_DOWN");
    keyNames.put(KEY_KP_LEFT,"KEY_KP_LEFT");
    keyNames.put(KEY_KP_RIGHT,"KEY_KP_RIGHT");
    keyNames.put(KEY_DEAD_GRAVE,"KEY_DEAD_GRAVE");
    keyNames.put(KEY_DEAD_ACUTE,"KEY_DEAD_ACUTE");
    keyNames.put(KEY_DEAD_CIRCUMFLEX,"KEY_DEAD_CIRCUMFLEX");
    keyNames.put(KEY_DEAD_TILDE,"KEY_DEAD_TILDE");
    keyNames.put(KEY_DEAD_MACRON,"KEY_DEAD_MACRON");
    keyNames.put(KEY_DEAD_BREVE,"KEY_DEAD_BREVE");
    keyNames.put(KEY_DEAD_ABOVEDOT,"KEY_DEAD_ABOVEDOT");
    keyNames.put(KEY_DEAD_DIAERESIS,"KEY_DEAD_DIAERESIS");
    keyNames.put(KEY_DEAD_ABOVERING,"KEY_DEAD_ABOVERING");
    keyNames.put(KEY_DEAD_DOUBLEACUTE,"KEY_DEAD_DOUBLEACUTE");
    keyNames.put(KEY_DEAD_CARON,"KEY_DEAD_CARON");
    keyNames.put(KEY_DEAD_CEDILLA,"KEY_DEAD_CEDILLA");
    keyNames.put(KEY_DEAD_OGONEK,"KEY_DEAD_OGONEK");
    keyNames.put(KEY_DEAD_IOTA,"KEY_DEAD_IOTA");
    keyNames.put(KEY_DEAD_VOICED_SOUND,"KEY_DEAD_VOICED_SOUND");
    keyNames.put(KEY_DEAD_SEMIVOICED_SOUND,"KEY_DEAD_SEMIVOICED_SOUND");
    keyNames.put(KEY_AMPERSAND,"KEY_AMPERSAND");
    keyNames.put(KEY_ASTERISK,"KEY_ASTERISK");
    keyNames.put(KEY_QUOTEDBL,"KEY_QUOTEDBL");
    keyNames.put(KEY_LESS,"KEY_LESS");
    keyNames.put(KEY_GREATER,"KEY_GREATER");
    keyNames.put(KEY_BRACELEFT,"KEY_BRACELEFT");
    keyNames.put(KEY_BRACERIGHT,"KEY_BRACERIGHT");
    keyNames.put(KEY_AT,"KEY_AT");
    keyNames.put(KEY_COLON,"KEY_COLON");
    keyNames.put(KEY_CIRCUMFLEX,"KEY_CIRCUMFLEX");
    keyNames.put(KEY_DOLLAR,"KEY_DOLLAR");
    keyNames.put(KEY_EURO_SIGN,"KEY_EURO_SIGN");
    keyNames.put(KEY_EXCLAMATION_MARK,"KEY_EXCLAMATION_MARK");
    keyNames.put(KEY_INVERTED_EXCLAMATION_MARK,"KEY_INVERTED_EXCLAMATION_MARK");
    keyNames.put(KEY_LEFT_PARENTHESIS,"KEY_LEFT_PARENTHESIS");
    keyNames.put(KEY_NUMBER_SIGN,"KEY_NUMBER_SIGN");
    keyNames.put(KEY_PLUS,"KEY_PLUS");
    keyNames.put(KEY_RIGHT_PARENTHESIS,"KEY_RIGHT_PARENTHESIS");
    keyNames.put(KEY_UNDERSCORE,"KEY_UNDERSCORE");
    keyNames.put(KEY_WINDOWS,"KEY_WINDOWS");
    keyNames.put(KEY_CONTEXT_MENU,"KEY_CONTEXT_MENU");
    keyNames.put(KEY_FINAL,"KEY_FINAL");
    keyNames.put(KEY_CONVERT,"KEY_CONVERT");
    keyNames.put(KEY_NONCONVERT,"KEY_NONCONVERT");
    keyNames.put(KEY_ACCEPT,"KEY_ACCEPT");
    keyNames.put(KEY_MODECHANGE,"KEY_MODECHANGE");
    keyNames.put(KEY_KANA,"KEY_KANA");
    keyNames.put(KEY_KANJI,"KEY_KANJI");
    keyNames.put(KEY_ALPHANUMERIC,"KEY_ALPHANUMERIC");
    keyNames.put(KEY_KATAKANA,"KEY_KATAKANA");
    keyNames.put(KEY_HIRAGANA,"KEY_HIRAGANA");
    keyNames.put(KEY_FULL_WIDTH,"KEY_FULL_WIDTH");
    keyNames.put(KEY_HALF_WIDTH,"KEY_HALF_WIDTH");
    keyNames.put(KEY_ROMAN_CHARACTERS,"KEY_ROMAN_CHARACTERS");
    keyNames.put(KEY_ALL_CANDIDATES,"KEY_ALL_CANDIDATES");
    keyNames.put(KEY_PREVIOUS_CANDIDATE,"KEY_PREVIOUS_CANDIDATE");
    keyNames.put(KEY_CODE_INPUT,"KEY_CODE_INPUT");
    keyNames.put(KEY_JAPANESE_KATAKANA,"KEY_JAPANESE_KATAKANA");
    keyNames.put(KEY_JAPANESE_HIRAGANA,"KEY_JAPANESE_HIRAGANA");
    keyNames.put(KEY_JAPANESE_ROMAN,"KEY_JAPANESE_ROMAN");
    keyNames.put(KEY_KANA_LOCK,"KEY_KANA_LOCK");
    keyNames.put(KEY_INPUT_METHOD_ON_OFF,"KEY_INPUT_METHOD_ON_OFF");
    keyNames.put(KEY_CUT,"KEY_CUT");
    keyNames.put(KEY_COPY,"KEY_COPY");
    keyNames.put(KEY_PASTE,"KEY_PASTE");
    keyNames.put(KEY_UNDO,"KEY_UNDO");
    keyNames.put(KEY_AGAIN,"KEY_AGAIN");
    keyNames.put(KEY_FIND,"KEY_FIND");
    keyNames.put(KEY_PROPS,"KEY_PROPS");
    keyNames.put(KEY_STOP,"KEY_STOP");
    keyNames.put(KEY_COMPOSE,"KEY_COMPOSE");
    keyNames.put(KEY_ALT_GRAPH,"KEY_ALT_GRAPH");
    keyNames.put(KEY_BEGIN,"KEY_BEGIN");
    keyNames.put(KEY_UNDEFINED,"KEY_UNDEFINED");
    
  }
  
  public static UMB taskTimerStart(String name) {
    taskName=name;
    if(timerTask==null) timerTask=new long[3];
    
    timerTask[0]=System.currentTimeMillis();
    timerTask[1]=timerTask[0];
    timerTask[2]=-1;
    
    return UMB.UMB;
  }

  public static UMB taskTimerUpdate(float perc) {
    long tNow=System.currentTimeMillis();
    long tD=tNow-timerTask[1];      
    
//    log("update "+tD+" "+(System.currentTimeMillis()-timerTask[0]));
    if(tD>1000) {    
      if(timerTask[2]<0) {
        logDivider(taskName+"\t");
        timerTask[2]=1;
      }
      tD=tNow-timerTask[0];
      if(perc<1) perc=perc*100f;
      log(taskName+": "+(int)perc+"% - "+
          nf((float)tD/1000f,1,1)+" sec");
      timerTask[1]=tNow;
    }      
    
    return UMB.UMB;    
  }

  public static UMB taskTimerDone() {
    if(taskName!=null) {
      long tD=System.currentTimeMillis()-timerTask[0];
      if(tD>1000) {
        log(taskName+": Done - "+
        nf((float)tD/1000f,1,1)+" sec");
        logDivider();
      }
      taskName=null;
    }

    return UMB.UMB;        
  }

  public static long timerStart(int id) {
    long t=System.currentTimeMillis();
    id*=3;
    timerData[id]=t;
    return t;
  }
  
  public static long timerElapsed(int id) {
    long t=System.currentTimeMillis();
    id*=3;
    
    return t-timerData[id];
  }
  

  public static long timerEnd(int id) {
    long t=System.currentTimeMillis();
    id*=3;
    timerData[id+1]=t;
    timerData[id+2]=t-timerData[id];
    return timerData[id+2];
  }

  public static String version() {
    return VERSION;
  }


  ///////////////////////////////////////////////
  // PGRAPHICS CONVENIENCE METHODS
  
  /**
   * Static method to call PGraphics.translate() with a UVertex instance as
   * input.
   * 
   * @param v
   * @return
   */
  public static UMB ptranslate(UVertex v) {
    return ptranslate(v.x,v.y,v.z);
  }

  /**
   * Static method to call PGraphics.translate() with a UVertex instance as
   * input.
   * 
   * @param v
   * @return
   */
  public static UMB ptranslate(float x,float y,float z) {
    if (checkGraphicsSet()) {
      if(isGraphics3D) g.translate(x, y, z);
      else g.translate(x, y);
    }
    return UMB.UMB;
  }

  /**
   * Static method to call PGraphics.translate() with a UVertex instance as
   * input.
   * 
   * @param v
   * @return
   */
  public static UMB ptranslate(float x,float y) {
    if (checkGraphicsSet()) g.translate(x, y);
    return UMB.UMB;
  }

  public static UMB pscale(float m) {return pscale(m,m,m);}

  public static UMB prect(float r,float r2) {
    return prect(-r*0.5f,-r2*0.5f, r, r2);
  }

  public static UMB pcross(UVertex loc,float w) {
    return ppush().ptranslate(loc).pline(-w,0,w,0).pline(0,-w,0,w).ppop();
  }
  
  public static UMB prect(UVertex loc,float r) {
    return prect(loc,r,r);
  }

  public static UMB prect(UVertex loc,float r,float r2) {
    if (checkGraphicsSet()) 
      ppush().ptranslate(loc).prect(0,0, r, r2).ppop();
    return UMB.UMB;
  }

  public static UMB prect(float mx,float my,float r,float r2) {
    if (checkGraphicsSet()) g.rect(mx, my, r, r2);
    return UMB.UMB;
  }

  public static UMB pquad(UVertex[] vv) {
    if (checkGraphicsSet()) {
      g.beginShape(QUADS);
      pvertex(vv);
      g.endShape();
    }
    return UMB.UMB;
  }


  public static UMB pellipse(float r,float r2) {
    return pellipse(0,0, r, r2);
  }

  public static UMB pellipse(UVertex loc,float r) {
    return pellipse(loc, r,r);
  }

  public static UMB pellipse(UVertex loc,float r,float r2) {
    if (checkGraphicsSet()) 
      ppush().ptranslate(loc).pellipse(0,0, r, r2).ppop();
    return UMB.UMB;
  }

  public static UMB pellipse(float mx,float my,float r,float r2) {
    if (checkGraphicsSet()) g.ellipse(mx, my, r, r2);
    return UMB.UMB;
  }

  public static UMB pscale(float mx,float my,float mz) {
    if (checkGraphicsSet()) g.scale(mx,my,mz);
    return UMB.UMB;
  }
  
  public static UMB protX(float deg) {
    if (checkGraphicsSet()) g.rotateX(deg);
    return UMB.UMB;
  }

  public static UMB protY(float deg) {
    if (checkGraphicsSet()) g.rotateY(deg);
    return UMB.UMB;
  }

  public static UMB protZ(float deg) {
    if (checkGraphicsSet()) g.rotateZ(deg);
    return UMB.UMB;
  }

  /**
   * Static convenience method to call PGraphics.line() with two UVertex
   * instances as input.
   */
  public static UMB pline(UVertex v, UVertex v2) {
    if (checkGraphicsSet()) {
      if(isGraphics3D)
        g.line(v.x, v.y, v.z, v2.x, v2.y, v2.z);
      else g.line(v.x, v.y, v2.x, v2.y);
    }
    return UMB.UMB;
  }

  /**
   * Static convenience method to call PGraphics.line() between the origin and
   * a single UVertex instance.  
   */
  public static UMB pline(UVertex v) {
    if (checkGraphicsSet()) {
      if(isGraphics3D)
        g.line(0,0,0, v.x, v.y, v.z);
      else g.line(0,0,v.x, v.y);
    }
    return UMB.UMB;
  }

  public static UMB pline(float x1,float y1,float x2,float y2) {
    if (checkGraphicsSet()) {
      g.line(x1,y1,x2,y2);
    }
    return UMB.UMB;
  }

  /**
   * Static convenience method to call both <code>PGraphics.pushMatrix()</code>
   * <code>PGraphics.pushStyle()</code>
   */
  public static UMB ppush() {
    if (checkGraphicsSet()) {
      g.pushMatrix();
      g.pushStyle();
    }
    return UMB.UMB;
  }

  /**
   * Static convenience method to call both <code>PGraphics.popMatrix()</code>
   * <code>PGraphics.popStyle()</code>
   */
  public static UMB ppop() {
    if (checkGraphicsSet()) {
      g.popStyle();
      g.popMatrix();
    }
    return UMB.UMB;
  }

  /**
   * Static convenience method to call <code>PGraphics.vertex()</code> with a
   * UVertex instance as input
   */
  public static UMB pvertex(UVertex v) {
    if (checkGraphicsSet()) {
      if (isGraphics3D) g3d.vertex(v.x, v.y, v.z);
      else g.vertex(v.x, v.y);
    }
    return UMB.UMB;
  }

  /**
   * Static convenience method to iterate through an array of
   * <code>UVertex</code> and call <code>PGraphics.vertex()</code> for each
   * instance.
   */
  public static UMB pvertex(UVertex varr[]) {
    return pvertex(varr,false);
  }

  
  public static UMB pvertex(UVertexList vl) {
    return pvertex(vl,false);
  }

  public static UMB pvertex(UVertexList vl,boolean reverse) {
    if (checkGraphicsSet()) {
      int n=vl.size();
      
      if (isGraphics3D) {
        for(int i=0; i<n; i++) {
          UVertex tmp=vl.get(reverse ? n-1-i : i).draw();
          g3d.vertex(tmp.x, tmp.y, tmp.z);
        }
      }
      else {
        for(int i=0; i<n; i++) {
          UVertex tmp=vl.get(reverse ? n-1-i : i).draw();
          g.vertex(tmp.x, tmp.y);
        }
      }
    }
    
    return UMB.UMB;
  }


  /**
   * Static convenience method to iterate through an array of
   * <code>UVertex</code> and call <code>PGraphics.vertex()</code> for each
   * instance. Set <code>useUV</code> to <code>true</code> to include the UV
   * coordinates stored in UVertex.
   * 
   *   
   * @param varr
   * @param useUV
   * @return
   */
  public static UMB pvertex(UVertex varr[],boolean useUV) {
    if(checkGraphicsSet()) {
      if(useUV) {
        if (isGraphics3D) {
          for(UVertex vv:varr) g3d.vertex(vv.x, vv.y, vv.z,vv.U,vv.V);
        }
        else {
          for(UVertex vv:varr) g.vertex(vv.x, vv.y, vv.U,vv.V);
        }
      }
      else {
        if (isGraphics3D) {
          for(UVertex vv:varr) g3d.vertex(vv.x, vv.y, vv.z);
        }
        else {
          for(UVertex vv:varr) g.vertex(vv.x, vv.y);
        }
      }
    }
    return UMB.UMB;
  }

  /**
   * Static convenience method to call <code>PGraphics.fill()</code>
   */
  public static UMB pfill(int col) {
    if (checkGraphicsSet()) g.fill(col);
    return UMB.UMB;
  }

  public static UMB pfill(float rr,float gg,float bb) {
    if (checkGraphicsSet()) g.fill(pcolor(rr,gg,bb));
    return UMB.UMB;
  }

  /**
   * Static convenience method to call <code>PGraphics.stroke()</code>
   */
  public static UMB pstroke(int col) {
    if (checkGraphicsSet()) g.stroke(col);
    return UMB.UMB;
  }

  public static UMB pstroke(int col,float strokeWeight) {
    if (checkGraphicsSet()) {
      if(strokeWeight>0) g.strokeWeight(strokeWeight);
      g.stroke(col);
    }
    return UMB.UMB;
  }

  /**
   * Static convenience method to call <code>PGraphics.noFill()</code>
   */
  public static UMB pnoFill() {
    if (checkGraphicsSet()) g.noFill();
    return UMB.UMB;
  }

  /**
   * Static convenience method to call <code>PGraphics.noStroke()</code>
   */
  public static UMB pnoStroke() {
    if (checkGraphicsSet()) g.noStroke();
    return UMB.UMB;
  }

  public UMB draw(ArrayList<UFace> faces,int theOptions) {
    if(checkGraphicsSet()) {
      g.beginShape(TRIANGLES);      
      
      int opt=(isEnabled(theOptions,COLORFACE) ? COLORFACE : 0);
      if(opt==COLORFACE) {
        for(UFace f:faces) {
          if(opt==COLORFACE) g.fill(f.col);
          pvertex(f.getV());
        }
      }
      else {
        for(UFace f:faces) pvertex(f.getV());
      }

      g.endShape();
    }
    return UMB.UMB;
  }

  //////////////////////////////// DRAW QUADSTRIP / FAN

  public static UMB drawTriangleFan(UVertexList vl) {
    return drawTriangleFan(vl, null, false);
  }

  public static UMB drawTriangleFan(UVertexList vl,boolean reverse) {
    return drawTriangleFan(vl, null, reverse);
  }

  public static UMB drawTriangleFan(UVertexList vl,UVertex c,boolean reverse) {
    if (checkGraphicsSet()) {
      int n=vl.size();
      if(c==null) c=vl.centroid();
      
      g.beginShape(TRIANGLE_FAN);
      pvertex(c).pvertex(vl,reverse);
      g.endShape();
    }
    return UMB.UMB;    
  }

  /**
   * Draws a quad strip mesh directly without creating a UGeo. 
   * @param vl List of vertex lists.
   * @return
   */
  public static UMB drawQuadstrip(ArrayList<UVertexList> vl) {
    if (checkGraphicsSet()) {
      int n=vl.size();
      
      UVertexList last=null;
      for(UVertexList tmp:vl) {
        if(last!=null) drawQuadstrip(last, tmp);
        last=tmp;
      }
    }    
    
    return UMB.UMB;
  }

  /**
   * Draws a quad strip mesh directly without creating a UGeo. 
   * @param vl 
   * @param vl2 
   * @return
   */
  public static UMB drawQuadstrip(UVertexList vl,UVertexList vl2) {
    if(checkGraphicsSet()) {
      g.beginShape(QUAD_STRIP);
      int n=vl.size();
      for(int i=0; i<n; i++) {
        pvertex(vl.get(i)).pvertex(vl2.get(i));
      }
      g.endShape();
    }
    return UMB.UMB;
  }

  public static UMB draw(ArrayList<UVertexList> vl) {
    return draw(vl,false);
  }

  public static UMB draw(ArrayList<UVertexList> vl,boolean drawGrid) {
    for(UVertexList l:vl) l.draw();
    if(drawGrid) {
      int nx=vl.get(0).size();
      int ny=vl.size();
      
      for(int j=0; j<nx; j++) {
        g.beginShape();
          for(int i=0; i<ny; i++) {
          pvertex(vl.get(i).get(j));
        }
        g.endShape();
      }
    }
    return UMB.UMB;
  }



  
  ///////////////////////////////////////////////
  // GEOMETRY OPTIONS 

  public UMB setOptions(int opt) {
    options=opt;
//    log(optionStr());
    return this;
  }

  public UMB enable(int opt) {
    options=options|opt;
    return this;
  }

  public boolean isEnabled(int opt) {
    return (options & opt)==opt;
  }

  public static boolean isEnabled(int theOptions,int opt) {
    return (theOptions & opt)==opt;
  }


  public UMB disable(int opt) {
    options=options  & (~opt);
//    log(optionStr());
    return this;
  }
  
  public String optionStr() {
    if(optionNames==null) {
      optionNames=new String[1000];
      optionNames[COLORVERTEX]="COLORVERTEX";
      optionNames[COLORFACE]="COLORFACE";
      optionNames[NOCOPY]="NOCOPY";
      optionNames[NODUPL]="NODUPL";
    }
    
    StringBuffer buf=new StringBuffer();
    if(isEnabled(NODUPL)) buf.append(optionNames[NODUPL]).append(TAB);
    if(isEnabled(NOCOPY)) buf.append(optionNames[NOCOPY]).append(TAB);
    if(isEnabled(COLORFACE)) buf.append(optionNames[COLORFACE]).append(TAB);
    if(isEnabled(COLORVERTEX)) buf.append(optionNames[COLORVERTEX]).append(TAB);

    if(buf.length()>0) {
      buf.deleteCharAt(buf.length()-1);
      return "Options: "+buf.toString();
    }
    
    return "Options: None "+options;
  }
  
  ///////////////////////////////////////////////
  // COLOR 
  
  public static final int pcolor(int c,float a) {
    return ((int)a<< 24) & c;
  }

  public static final int pcolor(int c) {
    return pcolor(c,c,c);
  }

  public static final int pcolor(float c) {
    return pcolor(c,c,c);
  }

  public static final int pcolor(float r, float g, float b) {
 //   return 0xff000000 | (v1 << 16) | (v2 << 8) | v3;
    int rr=(int)r,gg=(int)g,bb=(int)b;
    return (0xff000000)|
        ((rr)<<16)|((gg)<<8)|(bb);
  }

  public static final int pcolor(int r, int g, int b, int a) {
    return (0xff000000)|((r&0xff)<<16)|((g&0xff)<<8)|(b&0xff);
  }

  public static String hex(int col) {
    String s="",tmp;
    
    int a=(col >> 24) & 0xff;
    if(a<255) s+=strPad(Integer.toHexString(a),2,ZERO);
    
    s+=strPad(Integer.toHexString((col>>16)&0xff),2,ZERO);
    s+=strPad(Integer.toHexString((col>>8)&0xff),2,ZERO);
    s+=strPad(Integer.toHexString((col)&0xff),2,ZERO);
//    s+=(tmp.length()<2 ? "0"+tmp : tmp);
//    tmp=Integer.toHexString((col>>8)&0xff);
//    s+=(tmp.length()<2 ? "0"+tmp : tmp);
//    tmp=Integer.toHexString((col)&0xff);
//    s+=(tmp.length()<2 ? "0"+tmp : tmp);
    
    s=s.toUpperCase();
    return s;
  }

  public static final int pcolor(String hex) {
    int c=0xFFFF0000,alpha=255;
    
    boolean ok=true;
    
    if(hex==null) ok=false; 
    else for(int i=0; ok && i<hex.length(); i++) {
      char ch=hex.charAt(i);
      if(!(
          Character.isLetter(ch) ||
              Character.isDigit(ch)
              )) ok=false;
    }
    if(!ok) {
      log("toColor('"+hex+"') failed.");
      return c;
    }
    
    try {
      if(hex.length()==8) {
        alpha=Integer.parseInt(hex.substring(0,2),16);
//      UUtil.log("hex: "+hex+" alpha: "+alpha);
        hex=hex.substring(2);
      }
      c=(alpha<<24) | Integer.parseInt(hex, 16);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      c=pcolor(255,0,0);
      e.printStackTrace();
    }
    
    return c;
  }

  //////////////////////////////////////////
  // MATH
  // map,lerp,max,constrain code taken from processing.core.PApplet
  

  static public final float circleCircumference(float radius) {
    return PI*radius*2;
  }

  static public final float circleArea(float radius) {
    return PI*radius*radius;
  }

  static public final float circleRadiusByArea(float area) {
    // area=PI*radius*radius;
    // radius=sqrt(area/PI)
    return sqrt(area/PI);
  }

  static public final float circleXforY(float y) {
    float val=0;
    
    if(y<-(1-EPSILON) || y>(1-EPSILON)) return 0;
    val=(float)Math.asin(y);
    
    return (float)Math.cos(val);
  }

  static public final float circleYforX(float x) {
    float val=0;
    
    val=(float)Math.acos(x);
    
    return (float)Math.sin(val);
  }

  static public final float abs(float n) {
    return (n < 0) ? -n : n;
  }

  static public final int abs(int n) {
    return (n < 0) ? -n : n;
  }

  static public final float sq(float a) {
    return a*a;
  }

  static public final float sqrt(float a) {
    return (float)Math.sqrt(a);
  }

  static public final int max(int a, int b) {
    return (a > b) ? a : b;
  }

  static public final float max(float a, float b) {
    return (a > b) ? a : b;
  }

  static public double min(double a, double b) {    
    return a<b ? a : b;
  }

  static public double max(double a, double b) {    
    return a>b ? a : b;
  }


  static public final int min(int a, int b) {
    return (a < b) ? a : b;
  }

  static public final float min(float a, float b) {
    return (a < b) ? a : b;
  }
  

  static public final float[] norm(float val[],float max) {
    for(int i=0; i<val.length; i++) val[i]=val[i]/max;
    return val;
  }

  
  static public final float dampen(
      float val,float valOld,float factor) {
        return val*factor+valOld*(1-factor);
  }
  
  static public final float map(float value,
      float ostart, float ostop) {
        return ostart + (ostop - ostart) * (value);
  }
  
  static public final float map(float value,
      float istart, float istop,
      float ostart, float ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
  }

  static public final double map(double value,
      double istart, double istop,
      double ostart, double ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
  }

  static public final int constrain(int amt, int low, int high) {
    return (amt < low) ? low : ((amt > high) ? high : amt);
  }

  static public final float constrain(float amt, float low, float high) {
    return (amt < low) ? low : ((amt > high) ? high : amt);
  }

  static public final float lerp(float start, float stop, float amt) {
    return start + (stop-start) * amt;
  }

  static public final int lerpColor(int c1,int c2,float t) {
    float a1 = ((c1 >> 24) & 0xff);
    float r1 = (c1 >> 16) & 0xff;
    float g1 = (c1 >> 8) & 0xff;
    float b1 = c1 & 0xff;
    float a2 = (c2 >> 24) & 0xff;
    float r2 = (c2 >> 16) & 0xff;
    float g2 = (c2 >> 8) & 0xff;
    float b2 = c2 & 0xff;

    return (((int) (a1 + (a2-a1)*t) << 24) |
        ((int) (r1 + (r2-r1)*t) << 16) |
        ((int) (g1 + (g2-g1)*t) << 8) |
        ((int) (b1 + (b2-b1)*t)));
  }
  
  
  static public boolean equals(double a,double b) {
    a-=b;
    a=(a<0 ? -a : a);
    return (a<EPSILON);
  }
  
  static public boolean equals(float a,float b) {
    a-=b;
    a=(a<0 ? -a : a);
    return (a<EPSILON);
  }

  // extended versions
  
  static public final float mod(float a, float b) { // code from David Bollinger
    return (a%b+b)%b; 
  }

  static public final <T> ArrayList<T> toList(T[] arr) {
    ArrayList<T> l=new ArrayList<T>();
    for(T tmp : arr) l.add(tmp);
    return l;
  }
  static public final float[] toFloat(int[] val,float fval[]) {
    if(fval==null || fval.length!=val.length) fval=new float[val.length];
    
    int cnt=0;
    for(int theVal:val) fval[cnt++]=theVal;
    
    return fval;
  }  
  
  static public final float[] maxima(float val[]) {
    return maxima(val,null);
  }

  static public final float[] maxima(float val[],float dat[]) {
    if(dat==null) dat=new float[3];
    dat[0]=max(val);
    dat[1]=max(val);
    dat[2]=max(abs(dat[0]),abs(dat[1]));
    return dat;
  }
  
  static public final float max(ArrayList<Float> val) {
    float theMax=Float.MIN_VALUE;
    
    for(float v:val) theMax=(v>theMax ? v : theMax);
    return theMax;
  }

  static public final float min(ArrayList<Float> val) {
    float theMin=Float.MAX_VALUE;
    
    for(float v:val) theMin=(v<theMin ? v : theMin);
    return theMin;
  }

  static public final float max(float val[]) {
    float theMax=val[0];
    for(float v:val) theMax=(v>theMax ? v : theMax);
    return theMax;
  }

  static public final int max(int val[]) {
    int theMax=val[0];
    for(int v:val) theMax=(v>theMax ? v : theMax);
    return theMax;
  }


  static public final float mean(float val[]) {
    float fval=sum(val)/(float)val.length;
    return fval;
  }

  /**
   * The median is the "middle number" (in a sorted list of numbers). This function sorts the input
   * and returns the middle value.
   * @param val
   * @return
   */
  static public final float median(float val[]) {
    float valCopy[]=new float[val.length];
    System.arraycopy(val,0, valCopy, 0,val.length);
    Arrays.sort(valCopy);
    
    return valCopy[Math.round((float)val.length*0.5f)];
  }

  static public final int median(int val[]) {
    int valCopy[]=new int[val.length];
    System.arraycopy(val,0, valCopy, 0,val.length);
    Arrays.sort(valCopy);
    
    return valCopy[Math.round((int)val.length*0.5f)];
  }

  static public final float sum(float val[]) {
    float fval=0;
    for(float f:val) fval+=f;
    return fval;
  }

  static public final int sum(int val[]) {
    int ival=0;
    for(int f:val) ival+=f;
    return ival;
  }

  static public final double mapDbl(double value,
      double istart, double istop,
      double ostart, double ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
  }

  public final static float wraplerp(float a, float b, float t, float w) {
    a += (abs(b-a) > w/2f) ? ((a < b) ? w : -w) : 0;
    return lerp(a, b, t);
    }


  //////////////////////////////////////////
  // PARSING VALUES

  public static int parseInt(String s) {
    if(s==null) return Integer.MIN_VALUE;
    return Integer.parseInt(s.trim());
  }

  public static float parseFloat(String s) {
    if(s==null) return Float.NaN;
    return Float.parseFloat(s.trim());
  }

  public static float[] parseFloat(String s[]) {
    if(s==null) return null;
    
    float f[]=new float[s.length];
    int id=0;
    for(String ss:s) f[id++]=parseFloat(ss);
    
    return f;
  }

  
  //////////////////////////////////////////
  // RANDOM NUMBERS

  /**
   * Static copy of unlekker.util.Rnd for easy random number generation.
   */
  public static URnd rnd=new URnd(System.currentTimeMillis());

  private static long UMBStartTime;
  
  public static void setRnd(URnd rnd) {
    UMB.rnd=rnd;
    UMB.UMB=new UMB();
  }
  
  public static float sign(float in) {
    return (in<0 ? -1 : 1);
  }
  
  /**
   * Returns <code>true</code> if <code>rnd(100) > prob</code>.
   * @param prob
   * @return
   */
  public boolean rndProb(float prob) {
    return rnd.prob(prob>100 ? 100 : prob);
  }

  public static boolean rndBool() {
    return rnd.bool();
  }

  public static float rnd() {
    return rnd.random(1);
  }

  public static float rnd(float max) {
    return rnd.random(max);
  }

  public static float rndSign() {
    return (rndBool() ? -1 : 1);
  }

  public static float rnd(float min, float max) {
    return rnd.random(min,max);
  }

  public static float rndSigned(float v) {
    return rnd(v)*rndSign();
  }
  
  /**
   * Generates randomly signed integer numbers in the ranges [min..max] and
   * [-max..-min], with equal chances of getting a negative or
   * positve outcome. Avoids the problem
   * of a call like <code>random(-1,1)</code> generating 
   * values close to zero.  
   * 
   * @param min Minimum absolute value
   * @param max Maximum absolute value
   * @return
   */
  public static float rndSigned(float min, float max) {
     float val=rnd.random(min,max);
      return rndBool() ? val : -val;
    }

  public static <T> T rnd(ArrayList<T> l) {
    return l.get(rndInt(l.size()));
  }

  public static <T> int rndIndex(ArrayList<T> l) {
    return rndInt(l.size());
  }

  public static int rndInt(int max) {
    return rnd.integer(max);
  }

  public static int rndInt(int min, int max) {
    return rnd.integer(min,max);
  }

  /**
   * Generates randomly signed integer numbers in the ranges [min..max] and
   * [-max..-min], with equal chances of getting a negative or
   * positve outcome. Avoids the problem
   * of a call like <code>random(-1,1)</code> generating 
   * values close to zero.  
   * 
   * @param min Minimum absolute value
   * @param max Maximum absolute value
   * @return
   */
  public static int rndIntSigned(float min, float max) {
    int val=rnd.integer(min,max);
     return rndBool() ? val : -val;
   }
  
  
  //////////////////////////////////////////
  // SET + GET PAPPLET AND PGRAPHICS 

  public static void setPApplet(PApplet papplet) {
    setPApplet(papplet,true);
  }

  public static void setPApplet(PApplet papplet,boolean useGraphics) {
    UMB.papplet=papplet;    
    if(useGraphics) setGraphics(papplet);
  }

  public static PApplet  getPApplet() {
    return UMB.papplet;    
  }

  public static String className(Object o) {
    return o.getClass().getSimpleName();
  }
  
  public static String keyName(int key) {
    if(keyNames.containsKey(key)) {
      return keyNames.get(key);
    }
    return NULLSTR;
  }
  
  public static boolean checkGraphicsSet() {
    if(g==null) {
      if(gErrorCnt%100==0) logErr("ModelbuilderMk2: No PGraphics set. Use UMB.setGraphics(PApplet).");
      gErrorCnt++;
      return false;
    }
    return true;
  }

  public static boolean checkPAppletSet() {
    if(papplet==null) {
      if(gErrorCnt%100==0) logErr("ModelbuilderMk2: No PApplet set. Use UMB.setPApplet(PApplet).");
      gErrorCnt++;
      return false;
    }
    return true;
  }

  public static void saveStrings(String filename,ArrayList<String> out) {
    if(papplet!=null) {
      String s[]=out.toArray(new String[out.size()]);
      papplet.saveStrings(filename, s);
      logDivider("Saved strings: "+filename+
          " ("+s.length+" strings)");
    }
  }

  
  public static PGraphics getGraphics() {
    return g;
  }

  public static void setGraphics(PApplet papplet) {
    setGraphics(papplet.g);
  }

  public static void setGraphics(PGraphics gg) {
    try {
      UMB.g=gg;
      if((g==null)) return;
      
      if(PApplet.P3D.indexOf("core")>-1) {
        isGraphics3D=false;
      }
      
      else if(gg.is3D()) {
        UMB.g3d=(processing.opengl.PGraphics3D)gg;
        isGraphics3D=true;
      }
      else isGraphics3D=false;
    } catch (Exception e) {
      isGraphics3D=false;
      e.printStackTrace();
    }
    
    log("UMB.setGraphics: "+
        g.getClass().getSimpleName()+
        " (is3D="+isGraphics3D+")");
  }

  public static UMB depth() {
    if(checkGraphicsSet()) {
      papplet.hint(DISABLE_DEPTH_TEST);
    }
    return UMB.UMB;
  }

  public static UMB noDepth() {
    if(checkGraphicsSet()) {
      papplet.hint(DISABLE_DEPTH_TEST);
    }
    return UMB.UMB;
  }

  //////////////////////////////////////////
  // LOGGING
  
  public static UMB log(String s) {
    if(s.indexOf(NEWLN)>-1) {
      int pos=s.indexOf(NEWLN);
//      log("pos "+pos+" "+s.substring(0,pos)+"|");
      while(pos>-1) {
        log(s.substring(0,pos));
        s=s.substring(pos+1);
        pos=s.indexOf(NEWLN,pos+1);
      }
      if(s.length()>0) log(s);
    }
    else System.out.println(timeStr()+" "+s);
    
    return UMB.UMB;
  }

  public static void logf(String s,Object... arg) {
    log(String.format(Locale.US,s, arg));
  }

  
  public static <T> void log(T s[]) {
    StringBuffer buf=strBufGet();
    for(T ss:s) {
      if(buf.length()>0) buf.append(COMMA);
      buf.append(ss.toString());
    }
    log("["+strBufDispose(buf)+"]");
  }

  public static <T> T first(ArrayList<T> input) {
    return input.get(0);
  }

  public static <T> T last(ArrayList<T> input) {
    return input.get(input.size()-1);
  }

  public static <T> T extract(ArrayList<T> input,int index) {
    if(input.size()<index) return null;
    
    T res=input.get(index);
    input.remove(index);
    return res;
  }
  
  public static UMB debug(int level,String s) {
    if(DEBUGLEVEL>=level) log(s);
    return UMB.UMB;
  }
  
  public static <T> ArrayList<T> removeDupl(ArrayList<T> l) {
    HashSet<T> set = new HashSet<T>(l);
    l.clear();
    for(T tmp:set) l.add(tmp);

    return l;
  }

  
  
  public static <T> void log(ArrayList<T> input) {
    if(input==null) log(NULLSTR);
    else {
//    log("log "+input.getClass().getName()+" "+input.size());
      for(T tmp: input) {
        log(tmp.toString());
      }
    }
  }

  public static UMB log(char c) {
    return log(""+c);
  }

  public static UMB log(int i) {
    return log(""+i);
  }

  public static UMB log(float f) {
    return log(""+f);
  }

  public static UMB logErr(String s) {
    System.err.println(s);
    return UMB.UMB;
  }

  public static UMB logDivider() {
    return log(LOGDIVIDERNEWNL);
  }

  public static UMB logDivider(String s) {
    if(s!=null && s.length()>0 && s.charAt(0)=='\n') {
      log(LOGDIVIDERNEWNL);
      s=s.substring(1);
      return log(s);
    }
    else return log(LOGDIVIDER+' '+s);
  }

  public static String timeStr(long t) {
    int tmp;
    StringBuffer buf=strBufGet();
    t=t-UMBStartTime;
    
    int hr=(int)(t/HOURMSEC);
    t-=hr*HOURMSEC;
    int m=(int)(t/MINUTEMSEC);
    t-=m*MINUTEMSEC;
    int s=(int)(t/SECONDMSEC);
    
    String str=strf(TIMESTR,hr,m,s);
    str="";
    
    if(hr>0) str+=nf(hr,2)+":";
      str+=strf("%s:%s",nf(m,2),nf(s,2));
    
//      buf.append(nf(hr,2)).append(':').append(nf(m,2)).append(':').append(nf(s,2));
//      return buf.toString();
    return str;
  }

  public static String timeStr() {
    return timeStr(System.currentTimeMillis());
//    return timeStr2(Calendar.getInstance());//.getTimeInMillis());
  }
  
  public static String timeStr2(Calendar c) {
    int tmp;
    StringBuffer buf=strBufGet();
    SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss",Locale.US);

    buf.append(sdf.format(c.getTime()));
    return strBufDispose(buf);
  }

  public static Calendar dateStrParse(String s) {
    Calendar cal=Calendar.getInstance();
    try {
      cal.setTime(dateStrFormat.parse(s));
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      logErr(e.getMessage());
      return null;
    }
    return cal;
  }

  public static String dateStr(Date d) {
    Calendar cal=Calendar.getInstance();
    cal.setTime(d);
    return dateStr(cal);
  }
  
  public static String dateStr(Calendar c) {
    int tmp;
    StringBuffer buf=strBufGet();

    buf.append(dateStrFormat.format(c.getTime()));
    return strBufDispose(buf);
  }
  
  //////////////////////////////////////////
  // FILE TOOLS
  
  public static String nextFile(String path,String pre) {
    return nextFile(path, pre,null);
  }

  public static String nextFile(String path,String pre,String ext) {
    return UFile.nextFile(path, pre, ext);
  }
  
  //////////////////////////////////////////
  // NUMBER FORMATTING
  
  private static NumberFormat formatFloat, formatInt;
  private static char numberChar[]=new char[] {'0', '1', '2', '3', '4', '5',
      '6', '7', '8', '9', '-', '.'};

  static public void nfInitFormats() {
    formatFloat=NumberFormat.getInstance();
    formatFloat.setGroupingUsed(false);

    formatInt=NumberFormat.getInstance();
    formatInt.setGroupingUsed(false);
  }

  /**
   * Format floating point number for printing
   * 
   * @param num
   *          Number to format
   * @param lead
   *          Minimum number of leading digits
   * @param decimal
   *          Number of decimal digits to show
   * @return Formatted number string
   */
  static public String nf(float num, int lead, int decimal) {
    if (formatFloat==null) nfInitFormats();
    
    if((num-Math.floor(num))<EPSILON) return nf((int)num,lead);
    
    formatFloat.setMinimumIntegerDigits(lead);
    formatFloat.setMaximumFractionDigits(decimal);
    formatFloat.setMinimumFractionDigits(decimal);

    return formatFloat.format(num).replace(",", ".");
  }

  static public String nf(double num, int lead, int decimal) {
    return nf((float)num,lead,decimal);
  }

  /**
   * Format floating point number for printing with maximum 3 decimal points.
   * 
   * @param num
   *          Number to format
   * @return Formatted number string
   */
  static public String nf(float num) {
    return nf(num,1,3);
  }

  static public String nf(float num,int prec) {
    return nf(num,1,prec);
  }

  static public String nf(double num) {
    return nf((float)num);
  }

  /**
   * Format integer number for printing, padding with zeros if number has fewer
   * digits than desired.
   * 
   * @param num
   *          Number to format
   * @param digits
   *          Minimum number of digits to show
   * @return Formatted number string
   */
  static public String nf(int num, int digits) {
    if (formatInt==null) nfInitFormats();
    formatInt.setMinimumIntegerDigits(digits);
    return formatInt.format(num);
  }

  public static String fileSizeStr(File f) {
    long l=f.length();
    return fileSizeStr(l);
  }
  
  public static String  fileSizeStr(long l) {
    String str=null;
    if(l>MB) str=nf((float)l/(float)MB,1,1)+" MB";
    else if(l>KB) str=nf((float)l/(float)KB,1,1)+" KB";
    else str=l+"b";
    return str;
  }

  
  public static String strPad(String s,int len,char c) {
    len-=s.length();
    while(len>0) {
      s+=c;
      len--;
    }
    
    return s;
  }

  public static String strPadLeft(String s,int len,char c) {
    len-=s.length();
    while(len>0) {
      s=c+s;
      len--;
    }
    
    return s;
  }

  public static String strf(String format,Object... args) {
    return String.format(Locale.US,format, args);
  }

  public static <T> String str(ArrayList<T> o) {
    return str(o,NEWLN,null);
  }

  public static <T> String str(ArrayList<T> o,boolean asArray) {
    return str(o,TAB,ENCLSQ);
  }

  public static <T> String str(ArrayList<T> o, char delim,String enclosure) {
    StringBuffer buf=strBufGet();
    if(o==null) buf.append("null");
    else {
      int id=0;
      for(T oo:o) {
        if(buf.length()>0) buf.append(delim); 
//        buf.append(id++).append(' ');
        buf.append(oo.toString());
      }
    }
    
    if(enclosure!=null) {
      buf.insert(0, enclosure.charAt(0));
      buf.append(enclosure.charAt(1));
    }
    
    return strBufDispose(buf);
  }

  public static String str(float[] o) {
    StringBuffer buf=strBufGet();
    if(o==null) buf.append("null");
    else {
      buf.append('[');
      for(float ff:o) buf.append(nf(ff)).append(',');// (buf.length()>1 ? ','+i : ""+i));
      buf.deleteCharAt(buf.length()-1);
      buf.append(']');
    }
    return strBufDispose(buf);
  }

  public static String str(int[] o) {
    StringBuffer buf=strBufGet();
    if(o==null) buf.append("null");
    else {
      buf.append('[');
      for(int i:o) buf.append(i).append(',');// (buf.length()>1 ? ','+i : ""+i));
      buf.deleteCharAt(buf.length()-1);
      buf.append(']');
    }
    return strBufDispose(buf);
  }

  public static <T> String str(T[] o) {
    return str(o,COMMA,ENCLSQ);
  }

  public static <T> String str(T[] o, char delim,String enclosure) {
    StringBuffer buf=strBufGet();
    if(o==null) buf.append("null");
    else {
      int id=0;
      for(T oo:o) {
        if(buf.length()>0) buf.append(delim); 
//        buf.append(id++).append(' ');
        buf.append(oo==null ? NULLSTR : oo.toString());
      }
    }
    
    if(enclosure!=null) {
      buf.insert(0, enclosure.charAt(0));
      buf.append(enclosure.charAt(1));
    }
    
    return strBufDispose(buf);
  }

  
  //////////////////////////////////////////
  // STRING BUFFER POOL
  
  protected static ArrayList<StringBuffer> strBufFree,strBufBusy;

  protected static String strBufDispose(StringBuffer buf) {
    if(strBufBusy!=null) {
      strBufBusy.remove(buf);
      strBufFree.add(buf);
    }
      
    return buf.toString();
  }

  
  protected static StringBuffer strBufGet() {
    try {
      StringBuffer buf;
      
      if(strBufBusy==null) {
        strBufBusy=new ArrayList<StringBuffer>();
        strBufFree=new ArrayList<StringBuffer>();
      }
      
      if(strBufFree.size()>1) {
        buf=strBufFree.remove(0);
        buf.setLength(0);
      }
      else buf=new StringBuffer();
      
      strBufBusy.add(buf);
      
      return buf;
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return new StringBuffer();
  }
  
  //////////////////////////////////////////
  // STRING MANIPULATION
  
  public String strTrim(String s,int newlen) {
    return s.substring(0,newlen);
  }

  public String strStripContainer(String str) {
    String s=str.trim();
    int len=s.length();
    char ch1=s.charAt(0);
    char ch2=s.charAt(len-1);
    
    if((ch1=='[' && ch2==']') ||
        (ch1=='<' && ch2=='>')) {
      s=s.substring(1,len-1);
      return s;
    }
    
    return str;
  }

  public String str() {
    return this.getClass().getName();
  }

  /**
   * Produce hexadecimal SHA1 hash of input string.
   * @param input
   * @return
   */
  public static String toSHA1(String input) {
    MessageDigest md = null;
    
//    input=domain+"-"+input;
    
    byte[] convertme=input.getBytes();
    
    try {
        md = MessageDigest.getInstance("SHA-1");
        
    }
    catch(Exception e) {
        e.printStackTrace();
        return null;
    } 
    
    String result = "";
    byte[] b=md.digest(convertme);
    
    for (int i=0; i < b.length; i++) {
      result +=
            Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
    }

    return result;
  }

  public String toString() {
    return str();
  }

}

