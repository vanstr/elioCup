package models;

import com.avaje.ebean.Model;
import org.apache.commons.lang3.time.DateUtils;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Calendar;
import java.util.Date;

@Entity
public class DayStatistic extends Model{
  @Id
  public Long id;

  @ManyToOne
  @Constraints.Required
  public User user;

  @Constraints.Required
  public boolean online;

  public Date date;

  public Date firstTimeOnline;

  public Date lastTimeOnline;

  public long availabilityInSeconds;

  public static final Finder<Long, DayStatistic> FIND = new Finder<Long, DayStatistic>(Long.class, DayStatistic.class);


  public DayStatistic(User user, boolean isOnline){
    this.user = user;
    this.online = isOnline;
    this.date = getTodayDate();
  }

  public static void updateUserStatistic(User user, boolean isOnline, long secondsOnline){
    DayStatistic today = getToday(user);
    if(today != null){ // Exist statistic entrie for this day
      if(today.online && isOnline){ // User was online and right now is online
        today.availabilityInSeconds += secondsOnline;
        today.lastTimeOnline = new Date();
      }else{
        today.online = isOnline;
      }
      today.save();
    }else if(isOnline){
      today = new DayStatistic(user, true);
      today.firstTimeOnline = new Date();
      today.save();
    }
  }

  public static DayStatistic getToday(User user) {
    Date todayDate = getTodayDate();
    return FIND.where().eq("user", user).eq("date", todayDate).findUnique();
  }

  private static Date getTodayDate() {
    return DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
  }
}
