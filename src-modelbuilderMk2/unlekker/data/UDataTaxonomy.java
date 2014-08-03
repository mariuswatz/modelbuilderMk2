package unlekker.data;

import java.util.ArrayList;

import unlekker.mb2.util.UMB;

public class UDataTaxonomy extends UMB {
  private ArrayList<UDataTagList> tagList;
  private ArrayList<UDataList> dataList;
  
  public UDataTaxonomy() {
    tagList=new ArrayList<UDataTagList>();
    dataList=new ArrayList<UDataList>();
  }

  public UDataTaxonomy addDataList(UDataList list) {
    
    return this;
  }

  public UDataTaxonomy addDataList(String listname) {
    getDataList(listname);
    return this;
  }

  public UDataList getDataList(String listname) {
    for(UDataList tmp:dataList) {
      if(tmp.nameEquals(listname)) return tmp;
    }
    
    UDataList tmp=new UDataList(listname);
    dataList.add(tmp);
    
    return tmp;
  }
  
  public UDataTaxonomy addTagList(String listname) {
    getTagList(listname);
    return this;
  }
  
  public UDataTagList getTagList(String listname) {
    for(UDataTagList tmp:tagList) {
      if(tmp.nameEquals(listname)) return tmp;
    }
    
    UDataTagList tmp=new UDataTagList(listname);
    tagList.add(tmp);
    return tmp;
  }
  
  
  public UDataTag getTag(String name) {
    for(UDataTagList tmp:tagList) {
      
    }
    
    return null;
  }

}
