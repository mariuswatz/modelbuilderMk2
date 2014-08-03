void timeDemo() {
  println("-----------------------");
  println("datapointDemo()");

  /*

   To use a custom timestamp pattern we first need to add it to UTime. UTime will
   cycle through known patterns until a match is found for the input string.
   
   Important: Always add custom patterns in order of their *specificity*, i.e.
   "yyyy.MM.dd HH:mm:ss" is more specific than "yyyy.MM.DD". To avoid false
   positives where general patterns match strings that actually contain
   additional information, add the most specific patterns first.
   
   */
   
  // add custom timestamp patterns and use them to parse time 
  UTime.addDateFormat("yyyy/MM/dd HH:mm");
  UTime.addDateFormat("MMM dd, yyyy HH:mm");

  UTime t1=new UTime("2014/07/19 10:00");
  UTime t2=new UTime("Apr 12, 2014 10:00");
  
  println("\n"+t1.str()+"\n"+t2.str());

  // set t1 and t2 to represent start / end of one day
  t1=new UTime().setDate(2014,6,1).setStartOfDay();
  
  // copy t1, add one day, subtract 1 millisecond  
  t2=t1.copy().addDay(1).add(-1);
  
  println("\n"+t1.str()+"\n"+t2.str());
  
  // calculate distance in days between t1..t2
  t2.addDay((int)random(3,15));

  println("\n"+t1.str()+"\n"+t2.str());
  println("Days: "+t1.distInDays(t2));
}
