Table goog;
String dataStr;

// array to store stock prices
float val[];
float minval,maxval;

void importData() {
// columns: Date,Open,High,Low,Close,Volume,Adj Close

  // load CSV using processing.data.Table
  String filename="google2008.csv";
  goog=loadTable(filename,"header");

  println(goog.getRowCount() + " rows in table.");
  
  // get values of column 4 (closing value)
  val=goog.getFloatColumn(4);
  
  // get min/max
  maxval=max(val);
  minval=min(val);
  
  // normalize values to [0..1] range by dividing by maxval
  for(int i=0; i<val.length; i++) val[i]/=maxval;

  // create debug string to display
  dataStr=filename+" | "+
    goog.getRowCount() + " rows | "+
    "values: "+nf(minval,0,2)+"  >  "+nf(maxval,0,2);
  
}

