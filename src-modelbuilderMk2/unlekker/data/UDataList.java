package unlekker.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import processing.core.PVector;
import processing.data.Table;
import processing.data.TableRow;
import unlekker.mb2.util.UMB;


// - calc start/end
// - get in interval
// - get tagged
// - get float range
// - normalize float range
// - get list of 

public class UDataList extends UMB implements Iterable<UDataPoint> {
  public static int MIN=0,MAX=1,AVG=2,MEDIAN=3;
  
  UFilter<UDataPoint> comparator=null;
  UFilter<UDataPoint> filter=null;
  
  
  boolean UFilterWarning=false;
  
  private ArrayList<UDataPoint> data;
  private ArrayList<UDataPoint> dataFiltered=null;
  
  public String name="none";
  public String description="No description";
  
  
  public UDataList() {
    data=new ArrayList<UDataPoint>();
  }
  
  public UDataList(String name) {
    this();
    this.name=name;
  }

  public UDataList(String name,String description) {
    this(name);
    this.description=description;
  }

  public UDataList add(UDataPoint p) {
    dataFiltered=null;
    data.add(p);
    return this;
  }
  
  public UDataList add(int index,UDataPoint p) {
    dataFiltered=null;
    data.add(index, p);
    return this;
  }

  

  ////////////// TO TABLE

    
  public Table toTable() {
    return toTable(null);
  }

    public Table toTable(String keyOrder[]) {
    Table tab=new Table();
    
    UDataPoint first=get(0);
    boolean hasTime= first.time()!=null ? true : false;
    
    String time="time",timestamp="timestamp";
    
    if(hasTime) {
      tab.addColumn(time);
      tab.addColumn(timestamp);
    }
    
    if(keyOrder==null) {
      for(String col : first.keys()) {
        tab.addColumn(col);
      }
    }
    else {
      for(String key : keyOrder) tab.addColumn(key);
    }
    
    for(UDataPoint tmp : data) {
      TableRow row=tab.addRow();
      if(hasTime && tmp.time()!=null) {
        row.setString(time, tmp.time().str());
        row.setString(timestamp, ""+tmp.time().get());
      }
      
      for(String key : tab.getColumnTitles()) {
        row.setString(key,tmp.getValue(key));
      }      
    }
    
    return tab;
  }
  

  ////////////// COMPARISON / SORT / FILTER

  public UDataList setComparator(UFilter<UDataPoint> comp) {
    comparator=comp;
    return this;
  }

  public UDataList setFilter(UFilter<UDataPoint> comp) {
    filter=comp;
    return this;
  }
  
  public boolean contains(UDataPoint cmp) {
    return indexOf(cmp)>-1;
  }

  public int indexOf(UDataPoint cmp) {
    ArrayList<UDataPoint> theData=get();
      
    if(comparator==null) {      
      if(!UFilterWarning) {
        log("UDataList: No UFilter set (use setUFilter())");
        UFilterWarning=true;
      }
      return -1;
    }
    
    int index=0;
    
    for(UDataPoint tmp : theData) {
      if(comparator.compare(cmp, tmp)==0) return index;
      index++;
    }
    
    return -1;
  }
  
  public UDataList sort() {
    if(comparator!=null) {
      Collections.sort(data,comparator);
    }
    else {
      Collections.sort(data);
    }
    
    return this;
  }
  
  ////////////// NUMERIC ANALYSIS OF LISTS OF VALUES

  public static ArrayList<Float> normalizeFloatList(ArrayList<Float> val,boolean maxOnly) {
    float bb[]=boundsFloatList(val);
    
    if(maxOnly) {
      float m=1f/bb[1];
      
      for(int i=0; i<val.size(); i++) {
        float f=val.get(i);
        val.set(i,f*m);
      }
    }
    else {
      float m=1f/(bb[1]-bb[0]);
      for(int i=0; i<val.size(); i++) {
        float f=val.get(i)-bb[0]; // subtract min
        val.set(i,f*m);
      }
    }
    
    return val;
  }
  
  public UDataList normalizeFloatList(String key,boolean maxOnly) {    
    ArrayList<Float> fl=getFloatList(key);    
    fl=normalizeFloatList(fl, maxOnly);
    setFloatList(key, fl);
    
    return this;
  }
  
  /**
   * Calculates minimum/maximum, average and median values for a named series
   * of values, returning them as a float[] array ordered by [MIN,MAX,AVG,MEDIAN]. 
   * @param key
   * @return Array of 4 float values ([MIN,MAX,AVG,MEDIAN])
   */
  public float[] boundsFloatList(String key) {  
    ArrayList<Float> fl=getFloatList(key);    
    return boundsFloatList(fl);
  }

  /**
   * Calculates minimum/maximum, average and median values for an ArrayList<Float>
   * of values, returning them as a float[] array ordered by [MIN,MAX,AVG,MEDIAN]. 
   * @param val
   * @return Array of 4 float values ([MIN,MAX,AVG,MEDIAN])
   */
  public static float[] boundsFloatList(ArrayList<Float> val) {
    float[] bb=new float[] {
        Float.MAX_VALUE,
        Float.MIN_VALUE,
        0,0
    };
    
    ArrayList<Float> fl=new ArrayList<Float>();
    fl.addAll(val);
    Collections.sort(fl);
    
    for(float tmp : fl) {
      bb[0]=(tmp < bb[0] ? tmp : bb[0]); // min
      bb[1]=(tmp > bb[1] ? tmp : bb[1]); // max
      bb[2]+=tmp; // avg
    }
    
    bb[2]=bb[2]/(float)fl.size();
    bb[3]=fl.get(fl.size()/2); // median
    
    return bb;
  }
  
  public static String boundsToString(float bounds[]) {
    return strf("[%.3f > %.3f | Avg %.3f | Median %.3f]",
        bounds[MIN],bounds[MAX],
        bounds[AVG],bounds[MEDIAN]
        );
  }
  
  ////////////// GET PRIMITIVES AS LISTS
  
  
  /**
   * Returns ArrayList<Float> of Float  values stored in the UDataPoints in 
   * the list under the key "key". Note that this is a read-only operation, 
   * modifications to the resulting ArrayList will not apply to the UDataPoint 
   * instances.
   * @param key
   * @return
   */
  public ArrayList<Float> getFloatList(String key) {
    ArrayList<Float> val=new ArrayList<Float>();
    
    for(UDataPoint tmp : data) {
      val.add(tmp.getFloat(key));
    }
    
    return val;
  }

  public UDataList setFloatList(String key,ArrayList<Float> val) {    
    for(int i=0; i<val.size(); i++) {
      UDataPoint pt=data.get(i);
      pt.addFloat(key, val.get(i));
    }
    
    return this;
  }

  /**
   * Returns ArrayList<String> of String values stored in the UDataPoints in 
   * the list under the key "key". Note that this is a read-only operation, 
   * modifications to the resulting ArrayList will not apply to the UDataPoint 
   * instances.
   * @param key
   * @return
   */
  public ArrayList<String> getStringList(String key) {
    ArrayList<String> val=new ArrayList<String>();
    
    for(UDataPoint tmp : data) {
      val.add(tmp.getString(key));
    }
    
    return val;
  }

  /**
   * Returns ArrayList<Object> of Object values stored in the UDataPoints in 
   * the list under "key". Unlike <code>getFloatList()</code> and 
   * <code>getStringList()</code>, object references in the resulting ArrayList 
   * point to the original object instances. Any modification will therefore also
   * apply to the objects stored in the UDataList.
   * @param key
   * @return
   */
  public ArrayList<Object> getObjectList(String key) {
    ArrayList<Object> val=new ArrayList<Object>();
    
    for(UDataPoint tmp : data) {
      val.add(tmp.getObject(key));
    }
    
    return val;
  }

  public <T> ArrayList<T> getUniqueValues(String key, Class<T> classType) {
    ArrayList<T> val=new ArrayList<T>();
    
    for(UDataPoint tmp : data) {
      T pt;
      try {
        pt=classType.cast(tmp.getObject(key));
        if(!val.contains(pt)) val.add(pt);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        log("Error: "+classType.getSimpleName()+" "+tmp.getObject(key));
      }
    }
    
    return val;
  }

  /**
   * <p>EXPERIMENTAL AND UNTESTED: Returns ArrayList<T> of values stored in the UDataPoints in 
   * the list under "key", where T is the class the stored object should be cast to. If
   * the stored object does not match the Class provided an exception is likely to result.</p>
   *  
   * <p>Example:</p>
   * <code>ArrayList<PVector> o=data.getObjectList("pos",PVector.class);</code>
   * <p>Note that the ArrayList entries points to the same object instances as 
   * the UDataList.</p>
   * @param key
   * @return
   */
  public <T> ArrayList<T> getObjectList(String key, Class<T> classType) {
    ArrayList<T> val=new ArrayList<T>();
    
    for(UDataPoint tmp : data) {
//      val.add((T)theClass.cast(tmp.getObject(key)));
      val.add(classType.cast(tmp.getObject(key)));
    }
    
    return val;
  }
  

  public int size() {
    return data.size();
  }
  
  public UTimeRange getTimeRange() {
    UTimeRange range=new UTimeRange();
//    UTime[] t=new UTime[2];
//    
//    long t1=Long.MAX_VALUE,t2=Long.MIN_VALUE;
    
    for(UDataPoint tmp : data) {
      range.addToRange(tmp.time());
//      long theT=tmp.timeMin().get();
//      t1=(theT < t1 ? theT : t1);
//      t2=(theT > t2 ? theT : t2);
//      
//      theT=tmp.timeMax().get();
//      t1=(theT < t1 ? theT : t1);
//      t2=(theT > t2 ? theT : t2);
    }
    
//    t[0]=new UTime(t1);
//    t[1]=new UTime(t2);
//    
//    UTimeRange r=new UTimeRange().set(t1, t2);
    
    return range;
  }
  
  
  public UDataList clear() {
    data.clear();
    return this;
  }
  
  public UDataPoint remove(int index) {
    dataFiltered=null;
    
    return data.remove(index);
  }

  public UDataPoint get(int index) {
    return data.get(index);
  }

  public UDataList getInTimeRange(UTimeRange range) {
    UDataList tmp=new UDataList();
    
    for(UDataPoint pt : data) {
      if(range.inRange(pt.time())) tmp.add(pt); 
    }
    
    return tmp;
  }
  
  public ArrayList<UDataPoint> get() {
    if(filter!=null) {
      if(dataFiltered!=null) {
        dataFiltered=new ArrayList<UDataPoint>();
        for(UDataPoint tmp : data) {
          if(filter.accept(tmp)) dataFiltered.add(tmp);
        }
      }
      return dataFiltered;
    }

    return data;
  }

  public ArrayList<UDataPoint> getInRange(UTimeRange timeRange) {
    ArrayList<UDataPoint> l=new ArrayList<UDataPoint>();
    for(UDataPoint tmp:data) {
      if(timeRange.inRange(tmp.time())) l.add(tmp);
    }
    return data;
  }

  
  public boolean nameEquals(String listname) {
    return this.name.compareTo(name)==0;
  }

  public Iterator<UDataPoint> iterator() {
    // TODO Auto-generated method stub
    return (Iterator<UDataPoint>)data.iterator();
  }

  public String str() {
    return strf("%s\t%d",
        name,size());
  }

  public void sortByTime() {
    Collections.sort(data, new UFilter() 
    {

     public int compare(Object o1, Object o2) 
     {
         return (int)(((UDataPoint)o1).time().get()-
             ((UDataPoint)o2).time().get());

         // it can also return 0, and 1
     }
    }    
);

    
  }

  
  
}
