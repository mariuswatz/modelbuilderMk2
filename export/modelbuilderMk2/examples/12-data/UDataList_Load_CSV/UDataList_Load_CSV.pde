/*

  Marius Watz, April 2014
  http://workshop.evolutionzone.com

  Loads CSV file using the unlekker.data library.
  
*/

import java.util.Calendar;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import unlekker.data.*;
import unlekker.mb2.externals.*;
import ec.util.*;

String DATAPATH;

void setup() {
  // We use DATAPATH to point to an absolute path, so that
  // we can keep all our data in one place instead of
  // saving endless copies with each sketch
  DATAPATH="D:/Users/Marius/Dropbox/40 Teaching/2014 CIID/00 CIID Share/Processing Sketchbook/data-CIID";
  
  parse("yunTest1 - Sheet1 - fixed.csv");
}
