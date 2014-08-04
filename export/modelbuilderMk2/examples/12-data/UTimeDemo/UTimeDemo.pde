/*

Marius Watz, April 2014 http://workshop.evolutionzone.com
  
Demonstrates how to use UTime as a convenient to store, parse and manipulate
timestamps.

Time and date is surprisingly difficult to manage in Java, with java.util.Date
and java.util.Calendar presenting two different and not entirely compatible
approaches.

Epoch time is a common solution, representing time as the number of
milliseconds elapsed since some fixed starting date (Jan 1, 1970 in case of
Unix time.) While computationally convenient, common sense calendar
manipulation (add a day, a week etc.) are tricky to handle with Epoch time.

UTime uses Epoch time while also providing time manipulation methods close to
the logic of java.util.Calendar. UTime instances can be copied by value and
manipulated. They can also be parsed from a range of timestamp String patterns
(either a default set or provided custom patterns.)

Recommended reading:
http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
http://en.wikipedia.org/wiki/Unix_time

*/

import java.util.Calendar;
import unlekker.mb2.util.*;
import unlekker.data.*;


void setup() {
  
  timeDemo();
}
