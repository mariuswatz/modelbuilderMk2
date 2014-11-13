package unlekker.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import unlekker.mb2.util.UMB;

public class UTime extends UMB implements Comparable<UTime> {
  public static long TSEC=1000;
  public static long TMIN=60*TSEC;
  public static long THR=60*TMIN;
  public static long T24HR=24*THR;

  public static SimpleDateFormat dateMonthDayShortF=new SimpleDateFormat("MMM d",Locale.US);
  public static SimpleDateFormat dateF=new SimpleDateFormat("EEE, d MMM",Locale.US);
  public static SimpleDateFormat timeF=new SimpleDateFormat("HH:mm:ss",Locale.US);
  public static SimpleDateFormat datetimeF=new SimpleDateFormat("yyyyMMdd HH:mm:ss",Locale.US);
  
  static ArrayList<SimpleDateFormat> dateFormats=new ArrayList<SimpleDateFormat>();
  static String[] dateFormatsDefault = {
    "yyyyMMdd HH:mm:ss",
    "yyyyMMdd'T'hhmmss",
    "EEE, d MMM yy HH:mm:ss Z",
    "yyyy-MM-dd'T'HH:mm:ss",    
    "yyyy-MM-dd HH:mm:ss",
    "yyyy-MM-dd HH:mm:ss.SSS",
    "dd.MM.yy HH:mm:ss",
    "dd.MM.yy",
    "dd.MM"

  };
  
  // fields
  
  private long t;
  private Calendar cal;
  

  public UTime() {
    cal=Calendar.getInstance();
    set(cal.getTimeInMillis());
  }

  public UTime(String timestr) {
    this();
    set(parseDate(timestr));    
  }

  public UTime(long t) {
    this();
    this.set(t);    
  }

  public UTime copy() {
    return new UTime(this);
  }
  
  public UTime(UTime timestamp) {
    this(timestamp.get());
  }

  public long get() {
    return t;
  }

  public Calendar getCalendar() {
    Calendar c=Calendar.getInstance();
    c.setTimeInMillis(get());
    return c;
  }

  //////////////// DATE FORMATS
  
  public static void addDefaultDateFormats() { 
    for(String s:dateFormatsDefault) addDateFormat(s);
  };

  public static void addDateFormat(String s) {
    for(SimpleDateFormat tmp : dateFormats) if(s.compareTo(tmp.toPattern())==0) return;
    
    dateFormats.add(new SimpleDateFormat(s,Locale.US));
  }
  
  public static ArrayList<String> getDateFormats() {
    ArrayList<String> l=new ArrayList<String>();
    
    for(SimpleDateFormat f:dateFormats) l.add(f.toPattern());
    
    return l;
  }
  
  public static void printDateFormats() {
    log(getDateFormats());
  }
  
  
  static public long IOSOFFSET=-1;

  // parameter "t" is a iOS timestamp, stored as seconds since Jan 1, 2001.
  static public long fromiOS(long t) {
    
    // calculate IOSOFFSET if necessary
    if (IOSOFFSET<0) {
      // Objective-C timestamps are in seconds since Jan 1, 2001, while 
      // Java timestamps are in milliseconds since Jan 1, 1970. Hence we
      // need to calculate a timestamp IOSOFFSET (in msec) between those two
      // dates.
      Calendar cal=Calendar.getInstance();
      cal.clear();
      
      // get start value
      cal.set(1970, Calendar.JANUARY, 1);  
      long tmp=cal.getTimeInMillis();

      // get final value
      cal.set(2001, Calendar.JANUARY, 1);
      
      // offset is the difference
      IOSOFFSET=cal.getTimeInMillis()-tmp;
    }

    // t is in seconds, so multiply x1000 to get millis,
    // then add IOSOFFSET to get. "1000l" means "1000 as
    // a long"
    return t*1000l+IOSOFFSET;
  }


  ////////////////
  

  public static UTime getDay(int year,int month,int day) {
    UTime d=new UTime(0);
    return d.setDate(year, month, day);
  }
  
  /*
   * Assumes that January = 1, first day of month = 1
   */
  public UTime setDate(int year,int month,int day) {
    cal.setTimeInMillis(get());
    cal.set(Calendar.YEAR, year);
    cal.set(Calendar.MONTH, Calendar.JANUARY+(month-1));
    cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DAY_OF_MONTH)+(day-1));

    set(cal.getTimeInMillis());
    
    return this;
  }
  
  public UTime setEndOfDay() {
    return setStartOfDay().addHour(24).add(-1000);
  }

  public UTime setStartOfDay() {
    return setTime(0, 0, 0);
    
  }

  public UTime setTime(int hr,int min,int sec) {
    cal.setTimeInMillis(get());
    cal.set(Calendar.HOUR_OF_DAY, hr);
    cal.set(Calendar.MINUTE, min);
    cal.set(Calendar.SECOND, sec);
    cal.set(Calendar.MILLISECOND, 0);

    set(cal.getTimeInMillis());
    
    return this;
  }

  public int dayOfWeek() {
    cal.setTimeInMillis(get());
    return cal.get(Calendar.DAY_OF_WEEK);
  }

  public int dayOfMonth() {
    cal.setTimeInMillis(get());
    return cal.get(Calendar.DAY_OF_MONTH);
  }

  public int month() {
    cal.setTimeInMillis(get());
    return cal.get(Calendar.MONTH)+(1-Calendar.JANUARY);
  }

  public int year() {
    cal.setTimeInMillis(get());
    return cal.get(Calendar.YEAR);
  }

  public int week() {
    cal.setTimeInMillis(get());
    return cal.get(Calendar.WEEK_OF_YEAR);
  }

  public int hour() {
    cal.setTimeInMillis(get());
    return cal.get(Calendar.HOUR_OF_DAY);
  }

  public int minute() {
    cal.setTimeInMillis(get());
    return cal.get(Calendar.MINUTE);
  }

  public int sec() {
    cal.setTimeInMillis(get());
    return cal.get(Calendar.SECOND);
  }

  public long dist(UTime t2) {
    return t2.get()-get();
  }

  public int distInDays(UTime t2) {
    cal.setTimeInMillis(get());
    
    Calendar cal2=Calendar.getInstance();
    cal2.setTimeInMillis(t2.get());
    
    if(t2.before(this)) {
      Calendar tmp=cal;
      cal=cal2;
      cal2=tmp;
    }

    int dayCnt=0;
    while(cal.before(cal2)) {
      cal.add(Calendar.DAY_OF_YEAR, 1);
//      if(t2.isBefore(this)) cal.add(Calendar.DAY_OF_YEAR, -1);
      dayCnt++;
    }
    return dayCnt-1;
  }
  
  public UTime add(long ms) {
    set(get()+ms);
    return this;
  }

  public UTime addHour(int hourD) {
    cal.setTimeInMillis(get());
    cal.add(Calendar.HOUR_OF_DAY, hourD);
    set(cal.getTimeInMillis());
    
    return this;
  }

  public UTime addDay(int dayD) {
    cal.setTimeInMillis(get());
    cal.add(Calendar.DAY_OF_YEAR, dayD);
    set(cal.getTimeInMillis());
    
    return this;
  }

  public boolean isSameDay(UTime t1) {
    cal.setTimeInMillis(get());
    int dat[]=new int[] {
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH)
    };
    
    cal.setTimeInMillis(t1.get());
    return (dat[0]==cal.get(Calendar.YEAR) &&
        dat[1]==cal.get(Calendar.MONTH) &&
        dat[2]==cal.get(Calendar.DAY_OF_MONTH));
  }
  
  /**
   * <p>Compares this timestamp with the input, returning true if 
   * the input time is before this timestamp.</p> 
   * <p>Note that if this 
   * timestamp represents an interval, this function will only return
   * true if the input time occurs before both the start and end times of this
   * interval. To compare only with the start time, use {@link UTime#beforeStart(long)} instead.</p>
   * @param t1 Timestamp to compare
   * @return
   */
  public boolean before(UTime t1) {
    return before(t1.get());
  }

  /**
   * <p>Compares this timestamp with the input, returning true if 
   * the input time is before this timestamp.</p> 
   * <p>Note that if this 
   * timestamp represents an interval, this function will only return
   * true if the input time occurs before both the start and end times of this
   * interval. To compare only with the start time, use {@link UTime#beforeStart(long)} instead.</p>
   * @param msec Time in msec to compare with
   * @return
   */
  public boolean before(long msec) {
    return get()<msec;
  }

//  public long duration() {
//    if(isInterval()) return end.get()-get();
//    return 0;
//  }
//
//  public boolean isInterval() {
//    return (end!=null);
//  }
//  

//  public boolean beforeStart(long msec) {
//    return get()>msec;
//  }
//
//  public boolean inInterval(UTimestamp t) {
//    if(!t.isInterval()) return false;
//    return inInterval(t.in)
//    if(!beforeStart(msec)) return false;
//    return end.beforeStart(msec);
//  }

  /**
   * Checks this timestamp to see if it lies withing [t1..t2]
   * @param t1 Start of interval
   * @param t2 End of interval
   * @return
   */
//  public boolean inInterval(long t1,long t2) {
//    if(!beforeStart(t1)) return false;
//    return end.beforeStart(t2);
//  }

  /**
   * Assuming that this UTimestamp represents a time interval, 
   * calculates the input time as a fraction [0..1] of that interval.    
   * @param msec Time in milliseconds.
   * @return
   */
//  public float fractionOfInterval(long msec) {
//    if(!isInterval()) return 0;
//    
//    double T=map(msec, get(), end.get(), 0, 1d);
//    return (float)T;
//  }
  
  
  /**
   * Sets this timestamp to represent a time interval [tStart..tEnd].
   * Use {@link #getStart()} and {@link #getEnd()} to retrieve the values of an interval.
   * @param tStart Start of interval
   * @param tEnd End of interval
   * @return
   */
//  public UTimestamp setInterval(long tStart,long tEnd) {
//    this.t = tStart;
//    
//    setEnd(tEnd);
//    return this;
//  }
//
//  public UTimestamp setEnd(long tEnd) {
//    if(end==null) end=new UTimestamp(tEnd); 
//    end.set(tEnd);
//    
//    return this;
//  }
  
  public static long parseDate(String input) {
    long res=Long.MIN_VALUE;
    
    if(input.indexOf(' ')==-1) try {
      res=Long.parseLong(input);
      return res;
    }catch (Exception e) {res=Long.MIN_VALUE;} 

    for(SimpleDateFormat df:dateFormats) {
      try {
        res=df.parse(input).getTime();
        return res;
      }catch (Exception e) {res=Long.MIN_VALUE;} 
    }
    
    if(res==Long.MIN_VALUE) {
      logErr("UTimestamp.parseLong failed: "+input);
      res=0;
    }
    
    return res;
  }
  

  public static void setDateTimeFormatter(SimpleDateFormat df) {
    datetimeF=df;
  }

  public static void setDateFormatter(SimpleDateFormat df) {
    dateF=df;
  }

  public static void setTimeFormatter(SimpleDateFormat df) {
    timeF=df;
  }

  public String strDate() {
    return dateF.format(new Date(get()));
  }

  public String strTime() {
    return timeF.format(new Date(get()));
  }

  public String str() {
    return datetimeF.format(new Date(get()));
  }

  public String toString() {
    return str();
  }

  public long timeOfDayMS() {
    cal.setTimeInMillis(get());
//    cal.set(Calendar.MILLISECOND,0);
    
    long tt=cal.get(Calendar.HOUR_OF_DAY)*THR+
        cal.get(Calendar.MINUTE)*TMIN+
        cal.get(Calendar.SECOND)*TSEC;
    
    return tt;
  }
  
  public float fractionOfDay() {
    return map(timeOfDayMS(),0,T24HR,0,1);
  }

  public UTime set(long t) {
    this.t = t;
    return this;
  }

  public int compareTo(UTime o) {    
    return (int)(o.get()-get());
  }
  

}