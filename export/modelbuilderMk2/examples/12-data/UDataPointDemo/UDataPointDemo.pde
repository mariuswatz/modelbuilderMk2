/*

Marius Watz, April 2014
http://workshop.evolutionzone.com
  
Demonstrates how to use UDataList and UDataPoint to store lists of
multi-field data points.

UDataPoint can store primitive values (float, int) as well as Object
references in an internal HashMap<String,Object> map, with String used
as a key to retrieve the value from the map. This makes UDataPoint
useful as a general solution to data point storage. By default, each
UDataPoint instance also contains a UTime timestamp.

Storing series of UDataPoint instances in a UDataList makes it
possible to retrieve lists of all values stored under a given String
key:

  UDataList data; [...] ArrayList<Float>
  l=data.getFloatList("value");

Similarly, bounding calculations and normalization can be executed on
the entire list without manual iteration:

  float bounds[]=data.boundsFloatList(name);
  data.normalizeFloatList("value",true);

*/

import java.util.Calendar;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import unlekker.data.*;
import unlekker.mb2.externals.*;
import ec.util.*;

UDataList data;

void setup() {
  
  datapointDemo();
}
