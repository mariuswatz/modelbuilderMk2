package unlekker.data;

import java.util.ArrayList;

import unlekker.mb2.util.UMB;

public class UDataTaxonomy extends UMB {
  private ArrayList<UDataTagList> tagList;
  
  public UDataTaxonomy() {
    tagList=new ArrayList<UDataTagList>();
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
