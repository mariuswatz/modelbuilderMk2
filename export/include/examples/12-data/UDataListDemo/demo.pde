  void datalistDemo() {
    println("\n-----------------------");
    println("datalistDemo()");

    data=new UDataList();  

    // randomfill list with randomized data points  
    for(int i=0; i<100; i++) data.add(randomDataPoint());
    
    // Show information about the data we've parsed
    println("\nData:\t"+data.size()+" entries.");
    println(data.getTimeRange().str());
    
    for(UDataPoint tmp : data) println(tmp.str());
    
    println();
    float[] bb=data.boundsFloatList("rad");
    println("'rad' bounds: "+data.boundsToString(bb));

    ArrayList<PVector> o=data.getObjectList("pos",PVector.class);
    for(PVector pv : o) println(pv.toString());

  }
  
  UDataPoint randomDataPoint() {
    if (time==null) { // initialize time
      time=new UTime();
      time.setDate(2014, 1, 1);
      time.setStartOfDay();
    }
    
    UDataPoint pt=new UDataPoint();
    pt.timeSet(time.get());

    float T=pt.time().get();

    pt.addFloat("rad", noise(T*0.0001f)*100);
    pt.addFloat("strokeWeight", random(1,10));
    pt.addInt("col", color(255,0,random(50,255)));
    
    // add random PVector position
    PVector pos=new PVector(random(0.05f,0.33f)*(float)width,0);
    pos.rotate(random(TWO_PI));
    pt.addObject("pos", pos); 

    // randomly increment time
    time.addHour((int)random(1, 13));
    time.add((int)random(3600)*1000);
    
    return pt;
  }
