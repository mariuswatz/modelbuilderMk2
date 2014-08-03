package unlekker.data;

import java.util.ArrayList;

import unlekker.mb2.util.UMB;

public class UDataTagList extends UMB {
  private ArrayList<UDataTag> tags;
  private ArrayList<String> tagNames;
  private String name;
  
  public UDataTagList(String name) {
    this.name=name;
    tags=new ArrayList<UDataTag>();
  }
  
  public String name() {
    return name;
  }

  public boolean nameEquals(String name) {
    return this.name.compareTo(name)==0;
  }

  public int size() {
    return tags.size();
  }

  public ArrayList<UDataTag> tags() {
    return tags;
  }

  public ArrayList<String> tagNames() {
    if(tagNames==null || tagNames.size()!=tags.size()) {
      tagNames=new ArrayList<String>();
      for(UDataTag t:tags) tagNames.add(t.name());
    }
    return tagNames;
  }

  public UDataTag get(String name) {
    for(UDataTag t:tags) {
      if(t.nameEquals(name)) return t;
    }
    
    return add(name);
  }

  public UDataTag add(String name) {
    UDataTag tmp=new UDataTag(name);
    tags.add(tmp);
    return tmp;
  }

  public String str() {
    return strf("%s\t%d",
        name,size());
  }
}
