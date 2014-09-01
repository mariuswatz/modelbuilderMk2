package unlekker.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import processing.core.*;
import processing.data.*;
import unlekker.mb2.util.UMB;

public class UData extends UMB {

  static public Table tableInit(Table table,String headers) {
    String tok[]=PApplet.split(headers, ',');
    for(String s : tok) table.addColumn(s.trim());
    return table;
  }
  

}
