package unlekker.data;

import java.util.ArrayList;

import unlekker.mb2.util.UMB;


// - calc start/end
// - get in interval
// - get tagged
// - get float range
// - normalize float range
// - get list of 

public class UDataList extends UMB {
  private ArrayList<UDataPoint> data;
  
  
  
  public UDataList() {
    data=new ArrayList<UDataPoint>();
  }
  
  public UDataList add(UDataPoint p) {
    data.add(p);
    return this;
  }
  
  public int size() {
    return data.size();
  }
  
  public ArrayList<UDataPoint> get() {
    return data;
  }

  
  
}
