UDataList data;

void parse(String filename) {
  filename=DATAPATH+"/"+filename;

  data=new UDataList();
  Table input=loadTable(filename, "header");

  String columns[]=input.getColumnTitles();

  // add common date format patterns to UTime 
  UTime.addDefaultDateFormats();

  for (TableRow row : input.rows ()) {
    UDataPoint pt=new UDataPoint();

    // use UTime to parse time field 
    UTime t=new UTime(row.getString(columns[0]));
    pt.timeSet(t);

    pt.addFloat(columns[1], row.getInt(columns[1]));
    pt.addFloat(columns[2], row.getInt(columns[2]));

    data.add(pt);
  }

  // Show information about the data we've parsed
  
  println("\nData:\t"+data.size()+" entries.");
  
  // print time range
  UTimeRange times=data.getTimeRange(); 
  println("Start:\t"+times.getStart());
  println("End:\t"+times.getEnd());

  // print min/max/average/median data for each sensor
  
  for (int i=1; i<3; i++) {
    String name=columns[i];
    
    println("\n--------- "+name);
    float bounds[]=data.boundsFloatList(name);
    
    println("  min: "+bounds[data.MIN]);    
    println("  max: "+bounds[data.MAX]);    
    println("  average: "+bounds[data.AVG]);    
    println("  median: "+bounds[data.MEDIAN]);    
    println();
    
    // print all values
    ArrayList<Float> l=data.getFloatList(name);
    printArray(l );
  }
}
