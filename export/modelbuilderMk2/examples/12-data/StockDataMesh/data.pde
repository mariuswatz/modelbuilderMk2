void importData() {
  Table data;
  data=loadTable(filename, "header");

  // get all the values from column id=4, the closing price
  float [] val=data.getFloatColumn(4);
  for (float theVal : val) maxVal=max(maxVal, theVal);
  minVal=min(val);
  maxVal=max(val);

  println(val.length+" "+minVal+" "+maxVal);

  // fill an ArrayList with normalized values
  values=new ArrayList<Float>();
  for (float theVal : val) {
    values.add(theVal/maxVal); // normalize
  }
}
