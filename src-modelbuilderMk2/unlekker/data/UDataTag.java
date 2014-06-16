package unlekker.data;

import java.util.ArrayList;

import unlekker.mb2.util.UMB;

public class UDataTag extends UMB {
  private String name,nameLowercase;
  private ArrayList<UDataTag> tags;
  
  public UDataTag(String name) {
    this.name=name;
    nameLowercase=name.toLowerCase();
    tags=new ArrayList<UDataTag>();
  }
  
  public String name() {
    return name;
  }

  public boolean isTagged(UDataTag tag) {
    return false;
  }

  public UDataTag tag(UDataTag tag) {
    tags.add(tag);
    return this;
  }

  /**
   * Compares the lower case of <code>tagName</code> with the lower case of this tag's name.
   * @param tagName
   * @return
   */
  public boolean nameEquals(String tagName) {
    return tagName.toLowerCase().compareTo(nameLowercase)==0;
  }
  
  /**
   * Compares this UDataTag to the input by calling {@see #nameEquals(String)}.
   */
  public boolean equals(Object obj) {
    UDataTag cmp=(UDataTag)obj;
    return nameEquals(cmp.name());
  }

  public int hashCode() {
    return nameLowercase.hashCode();
  }

}
