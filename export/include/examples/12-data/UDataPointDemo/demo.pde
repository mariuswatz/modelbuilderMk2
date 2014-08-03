void datapointDemo() {
  println("-----------------------");
  println("datapointDemo()");

  UDataPoint pt=new UDataPoint();
  
  // we can set timestamps either in milliseconds or by
  // using UTime to parse a human-readable timestamp
  
  Calendar cal=Calendar.getInstance();
  pt.timeSet(cal.getTimeInMillis());
  println(pt.time().str());
  
  // to use a custom timestamp pattern we need to add it
  // to UTime. See the javadoc for java.text.SimpleDateFormat
  // for an overview of pattern codes:
  //
  // http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
  
  UTime.addDateFormat("yyyy/MM/dd HH:mm");
  
  UTime myTime=new UTime("2014/07/21 10:00");
  pt.timeSet(myTime);
  println(pt.time().str());
  
  
  // let's add some data. each value is mapped to a String key,
  // which means only one value can be mapped to each unique key
  
  pt.addFloat("temperature",25);
  pt.addFloat("humidity",80);
  pt.addString("conditions","excellent");

  // we can also add any type of Java object  
  pt.addObject("obj",new int[] {10,20,30});

  println("\nData field keys:");  
  printArray(pt.getKeys());
 
  // all values are stored internally as Objects, with no additional
  // information about what type of value they actually represent.
  //   
  // we can use getFloat(), getString() etc. to get values of specific 
  // types:
  
  println("\ntemperature: "+pt.getFloat("temperature"));
  
  // another option is to rely on getValue(), which tries to identify
  // if a value is a float,int,string or some other type of object:
  
  for(String key : pt.getKeys()) {
     println(pt.getValueType(key)+"\t"+key+"\t\t"+pt.getValue(key));   
  } 
  
  // since getValue() can only figure out the most basic object types,
  // we need to handle most objects ourselves with explicit type casting: 
  
  println("\nobj(int[])");
  int array[]=(int[])pt.getObject("obj");
  printArray(array);

}
