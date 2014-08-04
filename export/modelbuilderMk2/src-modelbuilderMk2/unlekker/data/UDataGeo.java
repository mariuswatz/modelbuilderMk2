package unlekker.data;

import java.util.ArrayList;

import processing.core.PVector;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.util.UMB;

public class UDataGeo extends UMB {
  public static final float EARTH_RADIUS_KM = 6371.01f;

  public String name="",description="";
  private UVertex loc=new UVertex();
  private PVector locPV=new PVector();
  
  public UDataGeo(float lat,float lon) {
    set(lat,lon);
  }
  
  public UDataGeo(String name,float lat,float lon) {
    this.name=name;
    set(lat,lon);
  }
  
  public String name() {
    return name;
  }

  public UDataGeo set(float lat, float lon) {
    if(name.length()<1 || name.startsWith("loc=")) {
      name=strf("loc=%s,%s", nf(lon,8),nf(lat,8));
    }
    
    loc.set(lat,lon);
    locPV.set(lat, lon);
    return this;
  }

  public PVector get() {
    return locPV;
  }

  public UVertex getVertex() {
    return loc;
  }
  
  /**
   * Taken from https://github.com/tillnagel/unfolding/blob/master/src/de/fhpotsdam/unfolding/utils/GeoUtils.java 
   * @param lat1
   * @param lon1
   * @param lat2
   * @param lon2
   * @return Distance in km
   */
  public static float distance(float lat1, float lon1, float lat2, float lon2) {
    float lat1Rad = lat1;
    float lon1Rad = lon1;
    float lat2Rad = lat2;
    float lon2Rad = lon2;

    double r = EARTH_RADIUS_KM;
    return (float)(r
        * Math.acos(Math.sin(lat1Rad) * Math.sin(lat2Rad) + Math.cos(lat1Rad) * Math.cos(lat2Rad)
            * Math.cos(lon2Rad - lon1Rad)));
  }

  
  /**
   * Compares this UDataTag to the input by calling {@see #nameEquals(String)}.
   */
  public boolean equals(Object obj) {
    UDataGeo cmp=(UDataGeo)obj;
    return cmp.getVertex().equals(loc);
  }

  public int hashCode() {
    return name.hashCode();
  }

}
