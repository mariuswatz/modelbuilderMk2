package unlekker.mb2.doc;

import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;

import java.util.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;

import processing.core.PApplet;

public class UClassLister extends PApplet {
  UDocGenerator doc;
  
  public void setup(){
    size(600, 600,OPENGL);
    smooth();
    
    UMB.setPApplet(this);
    
    String path="C:/Users/marius/Dropbox/03 Code/ITP2013Parametric/nosync/docu/";
    path+="ModelBuilderMk2-doc.conf";
    doc=new UDocGenerator(path);
    
    exit();
  }

  public void draw(){
    background(0);
  }

  public void keyPressed( ) {
    if(key!=CODED && key!=ESC) build();
  }

  void build() {
  }
  
  
  static public void main(String arg[]) {
    PApplet.main("unlekker.mb2.doc.UClassLister");

  }
}
