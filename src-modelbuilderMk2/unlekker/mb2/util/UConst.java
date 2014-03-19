package unlekker.mb2.util;

import java.util.HashMap;


/**
 * Constants used by the ModelbuilderMk2 library.
 * 
 * @author marius
 *
 */
public interface UConst extends processing.core.PConstants {
  public static final int NOCOPY=1,NODUPL=2;
  
  /**
   * Version identifier for this release of the Modelbuilder library
   */
  public static final String CREDIT="Marius Watz - http://workshop.evolutionzone.com/";
  public static final String TIMESTR="%2d:%2d:%2d";
  public final static String VERSION = "ModelbuilderMk2-0002c";
  public final static String FSTRXYZ="<%s,%s,%s>";
  public final static String FSTRXY="<%s,%s>";
  
  public static final float EPSILON=0.0001f;
  public static final float UNAN=Float.MIN_VALUE+1;
  
  public static final int KB=1024,MB=KB*KB;
  public static final int XY=0,XZ=1,YZ=2;
  public static final int SECONDMSEC=1000;
  public static final int MINUTEMSEC=60*SECONDMSEC;
  public static final int HOURMSEC=60*MINUTEMSEC;
  public static final int DAYMSEC=24*HOURMSEC;
  
  public static final int XYZ=0,XZY=1;
  public static final int YZX=2, YXZ=3,ZXY=5, ZYX=6;

  public static final char TAB='\t',COMMA=',',SPACE=' ',ZERO='0';
  public static final char DIRCHAR='/',DIRCHARDOS='\\',NEWLN='\n';      
  public static final String DIRSTR="/",DIRSTRDOS="\\",NULLSTR="null",DOTSTR=".";      
  public static final String ENCLSQ="[]",ENCLTAG="<>";
  public static final String LOGDIVIDER="------";
  public static final String LOGDIVIDERNEWNL="------------------";
  
  public static final String UGEO="UGEO",UVERTEXLIST="UVL",UVERTEX="UV",UFACE="UF";
  public static final int SMOOTHSTEP=0;
  
  public static final float PTCM=72f/2.54f,PTMM=72f/25.4f,PTINCH=72f;
 
  public static String STRNONE="NONE",STRNULL="NULL";
  public static int APIFAIL=0,APISUCCESS=1,APIWORKING=2,APINOTSTARTED=3;
  

  public static final int VID4KW=3840,VID4H=2160;
  public static final int VID1080PW=1920,VID1080PH=1080;
  public static final int VID720PW=1280,VID720PH=720;
  public static final int VIDWQXGAW=2560,VIDWQXGAH=1600;
  
  public static final int COLORVERTEX=8,COLORFACE=4;
  public static final int SMOOTHMESH=16;

  public static final int SUBDIVCENTROID=0,SUBDIVMIDEDGES=1;
  public static final int TRANSFORMWINDOW=0;
  
  public static final int FILETYPEIMG=0,FILETYPEMOV=1,
      FILETYPEARCHIVE=2,FILETYPEDOC=3;
  
  public static String [] EXTIMAGES=new String[] {
    "png","jpg","gif","pdf","svg","ai","jpeg","bmp","eps",
    "psd","tga","bmp","indd"
  };
  public static String [] EXTDOCS=new String[] {
    "rtf","doc","xls","docx","xlsx","txt","log",
    "ods","odt","ppt"
  };
  public static String [] EXTARCHIVE=new String[] {
    "zip","dmg","gz","rar","7z","jar"
  };
  public static String [] EXTMOVIES=new String[] {
    "mov","mp4","avi","mpg"
  };
  
  //////////////////////////////////////////////////
  // Import constants from java.awt.event.KeyEvent
  
  public static int KEY_ENTER = java.awt.event.KeyEvent.VK_ENTER;
  public static int KEY_BACK_SPACE = java.awt.event.KeyEvent.VK_BACK_SPACE;
  public static int KEY_TAB = java.awt.event.KeyEvent.VK_TAB;
  public static int KEY_CANCEL = java.awt.event.KeyEvent.VK_CANCEL;
  public static int KEY_CLEAR = java.awt.event.KeyEvent.VK_CLEAR;
  public static int KEY_SHIFT = java.awt.event.KeyEvent.VK_SHIFT;
  public static int KEY_CONTROL = java.awt.event.KeyEvent.VK_CONTROL;
  public static int KEY_ALT = java.awt.event.KeyEvent.VK_ALT;
  public static int KEY_PAUSE = java.awt.event.KeyEvent.VK_PAUSE;
  public static int KEY_CAPS_LOCK = java.awt.event.KeyEvent.VK_CAPS_LOCK;
  public static int KEY_ESCAPE = java.awt.event.KeyEvent.VK_ESCAPE;
  public static int KEY_SPACE = java.awt.event.KeyEvent.VK_SPACE;
  public static int KEY_PAGE_UP = java.awt.event.KeyEvent.VK_PAGE_UP;
  public static int KEY_PAGE_DOWN = java.awt.event.KeyEvent.VK_PAGE_DOWN;
  public static int KEY_END = java.awt.event.KeyEvent.VK_END;
  public static int KEY_HOME = java.awt.event.KeyEvent.VK_HOME;
  public static int KEY_LEFT = java.awt.event.KeyEvent.VK_LEFT;
  public static int KEY_UP = java.awt.event.KeyEvent.VK_UP;
  public static int KEY_RIGHT = java.awt.event.KeyEvent.VK_RIGHT;
  public static int KEY_DOWN = java.awt.event.KeyEvent.VK_DOWN;
  public static int KEY_COMMA = java.awt.event.KeyEvent.VK_COMMA;
  public static int KEY_MINUS = java.awt.event.KeyEvent.VK_MINUS;
  public static int KEY_PERIOD = java.awt.event.KeyEvent.VK_PERIOD;
  public static int KEY_SLASH = java.awt.event.KeyEvent.VK_SLASH;
  public static int KEY_0 = java.awt.event.KeyEvent.VK_0;
  public static int KEY_1 = java.awt.event.KeyEvent.VK_1;
  public static int KEY_2 = java.awt.event.KeyEvent.VK_2;
  public static int KEY_3 = java.awt.event.KeyEvent.VK_3;
  public static int KEY_4 = java.awt.event.KeyEvent.VK_4;
  public static int KEY_5 = java.awt.event.KeyEvent.VK_5;
  public static int KEY_6 = java.awt.event.KeyEvent.VK_6;
  public static int KEY_7 = java.awt.event.KeyEvent.VK_7;
  public static int KEY_8 = java.awt.event.KeyEvent.VK_8;
  public static int KEY_9 = java.awt.event.KeyEvent.VK_9;
  public static int KEY_SEMICOLON = java.awt.event.KeyEvent.VK_SEMICOLON;
  public static int KEY_EQUALS = java.awt.event.KeyEvent.VK_EQUALS;
  public static int KEY_A = java.awt.event.KeyEvent.VK_A;
  public static int KEY_B = java.awt.event.KeyEvent.VK_B;
  public static int KEY_C = java.awt.event.KeyEvent.VK_C;
  public static int KEY_D = java.awt.event.KeyEvent.VK_D;
  public static int KEY_E = java.awt.event.KeyEvent.VK_E;
  public static int KEY_F = java.awt.event.KeyEvent.VK_F;
  public static int KEY_G = java.awt.event.KeyEvent.VK_G;
  public static int KEY_H = java.awt.event.KeyEvent.VK_H;
  public static int KEY_I = java.awt.event.KeyEvent.VK_I;
  public static int KEY_J = java.awt.event.KeyEvent.VK_J;
  public static int KEY_K = java.awt.event.KeyEvent.VK_K;
  public static int KEY_L = java.awt.event.KeyEvent.VK_L;
  public static int KEY_M = java.awt.event.KeyEvent.VK_M;
  public static int KEY_N = java.awt.event.KeyEvent.VK_N;
  public static int KEY_O = java.awt.event.KeyEvent.VK_O;
  public static int KEY_P = java.awt.event.KeyEvent.VK_P;
  public static int KEY_Q = java.awt.event.KeyEvent.VK_Q;
  public static int KEY_R = java.awt.event.KeyEvent.VK_R;
  public static int KEY_S = java.awt.event.KeyEvent.VK_S;
  public static int KEY_T = java.awt.event.KeyEvent.VK_T;
  public static int KEY_U = java.awt.event.KeyEvent.VK_U;
  public static int KEY_V = java.awt.event.KeyEvent.VK_V;
  public static int KEY_W = java.awt.event.KeyEvent.VK_W;
  public static int KEY_X = java.awt.event.KeyEvent.VK_X;
  public static int KEY_Y = java.awt.event.KeyEvent.VK_Y;
  public static int KEY_Z = java.awt.event.KeyEvent.VK_Z;
  public static int KEY_OPEN_BRACKET = java.awt.event.KeyEvent.VK_OPEN_BRACKET;
  public static int KEY_BACK_SLASH = java.awt.event.KeyEvent.VK_BACK_SLASH;
  public static int KEY_CLOSE_BRACKET = java.awt.event.KeyEvent.VK_CLOSE_BRACKET;
  public static int KEY_NUMPAD0 = java.awt.event.KeyEvent.VK_NUMPAD0;
  public static int KEY_NUMPAD1 = java.awt.event.KeyEvent.VK_NUMPAD1;
  public static int KEY_NUMPAD2 = java.awt.event.KeyEvent.VK_NUMPAD2;
  public static int KEY_NUMPAD3 = java.awt.event.KeyEvent.VK_NUMPAD3;
  public static int KEY_NUMPAD4 = java.awt.event.KeyEvent.VK_NUMPAD4;
  public static int KEY_NUMPAD5 = java.awt.event.KeyEvent.VK_NUMPAD5;
  public static int KEY_NUMPAD6 = java.awt.event.KeyEvent.VK_NUMPAD6;
  public static int KEY_NUMPAD7 = java.awt.event.KeyEvent.VK_NUMPAD7;
  public static int KEY_NUMPAD8 = java.awt.event.KeyEvent.VK_NUMPAD8;
  public static int KEY_NUMPAD9 = java.awt.event.KeyEvent.VK_NUMPAD9;
  public static int KEY_MULTIPLY = java.awt.event.KeyEvent.VK_MULTIPLY;
  public static int KEY_ADD = java.awt.event.KeyEvent.VK_ADD;
  public static int KEY_SEPARATER = java.awt.event.KeyEvent.VK_SEPARATER;
  public static int KEY_SEPARATOR = java.awt.event.KeyEvent.VK_SEPARATOR;
  public static int KEY_SUBTRACT = java.awt.event.KeyEvent.VK_SUBTRACT;
  public static int KEY_DECIMAL = java.awt.event.KeyEvent.VK_DECIMAL;
  public static int KEY_DIVIDE = java.awt.event.KeyEvent.VK_DIVIDE;
  public static int KEY_DELETE = java.awt.event.KeyEvent.VK_DELETE;
  public static int KEY_NUM_LOCK = java.awt.event.KeyEvent.VK_NUM_LOCK;
  public static int KEY_SCROLL_LOCK = java.awt.event.KeyEvent.VK_SCROLL_LOCK;
  public static int KEY_F1 = java.awt.event.KeyEvent.VK_F1;
  public static int KEY_F2 = java.awt.event.KeyEvent.VK_F2;
  public static int KEY_F3 = java.awt.event.KeyEvent.VK_F3;
  public static int KEY_F4 = java.awt.event.KeyEvent.VK_F4;
  public static int KEY_F5 = java.awt.event.KeyEvent.VK_F5;
  public static int KEY_F6 = java.awt.event.KeyEvent.VK_F6;
  public static int KEY_F7 = java.awt.event.KeyEvent.VK_F7;
  public static int KEY_F8 = java.awt.event.KeyEvent.VK_F8;
  public static int KEY_F9 = java.awt.event.KeyEvent.VK_F9;
  public static int KEY_F10 = java.awt.event.KeyEvent.VK_F10;
  public static int KEY_F11 = java.awt.event.KeyEvent.VK_F11;
  public static int KEY_F12 = java.awt.event.KeyEvent.VK_F12;
  public static int KEY_F13 = java.awt.event.KeyEvent.VK_F13;
  public static int KEY_F14 = java.awt.event.KeyEvent.VK_F14;
  public static int KEY_F15 = java.awt.event.KeyEvent.VK_F15;
  public static int KEY_F16 = java.awt.event.KeyEvent.VK_F16;
  public static int KEY_F17 = java.awt.event.KeyEvent.VK_F17;
  public static int KEY_F18 = java.awt.event.KeyEvent.VK_F18;
  public static int KEY_F19 = java.awt.event.KeyEvent.VK_F19;
  public static int KEY_F20 = java.awt.event.KeyEvent.VK_F20;
  public static int KEY_F21 = java.awt.event.KeyEvent.VK_F21;
  public static int KEY_F22 = java.awt.event.KeyEvent.VK_F22;
  public static int KEY_F23 = java.awt.event.KeyEvent.VK_F23;
  public static int KEY_F24 = java.awt.event.KeyEvent.VK_F24;
  public static int KEY_PRINTSCREEN = java.awt.event.KeyEvent.VK_PRINTSCREEN;
  public static int KEY_INSERT = java.awt.event.KeyEvent.VK_INSERT;
  public static int KEY_HELP = java.awt.event.KeyEvent.VK_HELP;
  public static int KEY_META = java.awt.event.KeyEvent.VK_META;
  public static int KEY_BACK_QUOTE = java.awt.event.KeyEvent.VK_BACK_QUOTE;
  public static int KEY_QUOTE = java.awt.event.KeyEvent.VK_QUOTE;
  public static int KEY_KP_UP = java.awt.event.KeyEvent.VK_KP_UP;
  public static int KEY_KP_DOWN = java.awt.event.KeyEvent.VK_KP_DOWN;
  public static int KEY_KP_LEFT = java.awt.event.KeyEvent.VK_KP_LEFT;
  public static int KEY_KP_RIGHT = java.awt.event.KeyEvent.VK_KP_RIGHT;
  public static int KEY_DEAD_GRAVE = java.awt.event.KeyEvent.VK_DEAD_GRAVE;
  public static int KEY_DEAD_ACUTE = java.awt.event.KeyEvent.VK_DEAD_ACUTE;
  public static int KEY_DEAD_CIRCUMFLEX = java.awt.event.KeyEvent.VK_DEAD_CIRCUMFLEX;
  public static int KEY_DEAD_TILDE = java.awt.event.KeyEvent.VK_DEAD_TILDE;
  public static int KEY_DEAD_MACRON = java.awt.event.KeyEvent.VK_DEAD_MACRON;
  public static int KEY_DEAD_BREVE = java.awt.event.KeyEvent.VK_DEAD_BREVE;
  public static int KEY_DEAD_ABOVEDOT = java.awt.event.KeyEvent.VK_DEAD_ABOVEDOT;
  public static int KEY_DEAD_DIAERESIS = java.awt.event.KeyEvent.VK_DEAD_DIAERESIS;
  public static int KEY_DEAD_ABOVERING = java.awt.event.KeyEvent.VK_DEAD_ABOVERING;
  public static int KEY_DEAD_DOUBLEACUTE = java.awt.event.KeyEvent.VK_DEAD_DOUBLEACUTE;
  public static int KEY_DEAD_CARON = java.awt.event.KeyEvent.VK_DEAD_CARON;
  public static int KEY_DEAD_CEDILLA = java.awt.event.KeyEvent.VK_DEAD_CEDILLA;
  public static int KEY_DEAD_OGONEK = java.awt.event.KeyEvent.VK_DEAD_OGONEK;
  public static int KEY_DEAD_IOTA = java.awt.event.KeyEvent.VK_DEAD_IOTA;
  public static int KEY_DEAD_VOICED_SOUND = java.awt.event.KeyEvent.VK_DEAD_VOICED_SOUND;
  public static int KEY_DEAD_SEMIVOICED_SOUND = java.awt.event.KeyEvent.VK_DEAD_SEMIVOICED_SOUND;
  public static int KEY_AMPERSAND = java.awt.event.KeyEvent.VK_AMPERSAND;
  public static int KEY_ASTERISK = java.awt.event.KeyEvent.VK_ASTERISK;
  public static int KEY_QUOTEDBL = java.awt.event.KeyEvent.VK_QUOTEDBL;
  public static int KEY_LESS = java.awt.event.KeyEvent.VK_LESS;
  public static int KEY_GREATER = java.awt.event.KeyEvent.VK_GREATER;
  public static int KEY_BRACELEFT = java.awt.event.KeyEvent.VK_BRACELEFT;
  public static int KEY_BRACERIGHT = java.awt.event.KeyEvent.VK_BRACERIGHT;
  public static int KEY_AT = java.awt.event.KeyEvent.VK_AT;
  public static int KEY_COLON = java.awt.event.KeyEvent.VK_COLON;
  public static int KEY_CIRCUMFLEX = java.awt.event.KeyEvent.VK_CIRCUMFLEX;
  public static int KEY_DOLLAR = java.awt.event.KeyEvent.VK_DOLLAR;
  public static int KEY_EURO_SIGN = java.awt.event.KeyEvent.VK_EURO_SIGN;
  public static int KEY_EXCLAMATION_MARK = java.awt.event.KeyEvent.VK_EXCLAMATION_MARK;
  public static int KEY_INVERTED_EXCLAMATION_MARK = java.awt.event.KeyEvent.VK_INVERTED_EXCLAMATION_MARK;
  public static int KEY_LEFT_PARENTHESIS = java.awt.event.KeyEvent.VK_LEFT_PARENTHESIS;
  public static int KEY_NUMBER_SIGN = java.awt.event.KeyEvent.VK_NUMBER_SIGN;
  public static int KEY_PLUS = java.awt.event.KeyEvent.VK_PLUS;
  public static int KEY_RIGHT_PARENTHESIS = java.awt.event.KeyEvent.VK_RIGHT_PARENTHESIS;
  public static int KEY_UNDERSCORE = java.awt.event.KeyEvent.VK_UNDERSCORE;
  public static int KEY_WINDOWS = java.awt.event.KeyEvent.VK_WINDOWS;
  public static int KEY_CONTEXT_MENU = java.awt.event.KeyEvent.VK_CONTEXT_MENU;
  public static int KEY_FINAL = java.awt.event.KeyEvent.VK_FINAL;
  public static int KEY_CONVERT = java.awt.event.KeyEvent.VK_CONVERT;
  public static int KEY_NONCONVERT = java.awt.event.KeyEvent.VK_NONCONVERT;
  public static int KEY_ACCEPT = java.awt.event.KeyEvent.VK_ACCEPT;
  public static int KEY_MODECHANGE = java.awt.event.KeyEvent.VK_MODECHANGE;
  public static int KEY_KANA = java.awt.event.KeyEvent.VK_KANA;
  public static int KEY_KANJI = java.awt.event.KeyEvent.VK_KANJI;
  public static int KEY_ALPHANUMERIC = java.awt.event.KeyEvent.VK_ALPHANUMERIC;
  public static int KEY_KATAKANA = java.awt.event.KeyEvent.VK_KATAKANA;
  public static int KEY_HIRAGANA = java.awt.event.KeyEvent.VK_HIRAGANA;
  public static int KEY_FULL_WIDTH = java.awt.event.KeyEvent.VK_FULL_WIDTH;
  public static int KEY_HALF_WIDTH = java.awt.event.KeyEvent.VK_HALF_WIDTH;
  public static int KEY_ROMAN_CHARACTERS = java.awt.event.KeyEvent.VK_ROMAN_CHARACTERS;
  public static int KEY_ALL_CANDIDATES = java.awt.event.KeyEvent.VK_ALL_CANDIDATES;
  public static int KEY_PREVIOUS_CANDIDATE = java.awt.event.KeyEvent.VK_PREVIOUS_CANDIDATE;
  public static int KEY_CODE_INPUT = java.awt.event.KeyEvent.VK_CODE_INPUT;
  public static int KEY_JAPANESE_KATAKANA = java.awt.event.KeyEvent.VK_JAPANESE_KATAKANA;
  public static int KEY_JAPANESE_HIRAGANA = java.awt.event.KeyEvent.VK_JAPANESE_HIRAGANA;
  public static int KEY_JAPANESE_ROMAN = java.awt.event.KeyEvent.VK_JAPANESE_ROMAN;
  public static int KEY_KANA_LOCK = java.awt.event.KeyEvent.VK_KANA_LOCK;
  public static int KEY_INPUT_METHOD_ON_OFF = java.awt.event.KeyEvent.VK_INPUT_METHOD_ON_OFF;
  public static int KEY_CUT = java.awt.event.KeyEvent.VK_CUT;
  public static int KEY_COPY = java.awt.event.KeyEvent.VK_COPY;
  public static int KEY_PASTE = java.awt.event.KeyEvent.VK_PASTE;
  public static int KEY_UNDO = java.awt.event.KeyEvent.VK_UNDO;
  public static int KEY_AGAIN = java.awt.event.KeyEvent.VK_AGAIN;
  public static int KEY_FIND = java.awt.event.KeyEvent.VK_FIND;
  public static int KEY_PROPS = java.awt.event.KeyEvent.VK_PROPS;
  public static int KEY_STOP = java.awt.event.KeyEvent.VK_STOP;
  public static int KEY_COMPOSE = java.awt.event.KeyEvent.VK_COMPOSE;
  public static int KEY_ALT_GRAPH = java.awt.event.KeyEvent.VK_ALT_GRAPH;
  public static int KEY_BEGIN = java.awt.event.KeyEvent.VK_BEGIN;
  public static int KEY_UNDEFINED = java.awt.event.KeyEvent.VK_UNDEFINED;
  
  //////////////////////////////////////////////////

  
}
