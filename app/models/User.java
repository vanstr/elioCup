package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import play.Logger;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "user_table")
public class User extends Model {

  @Id
  public Long id;

  @Constraints.Required
  public String name;

  @Constraints.Required
  public boolean active;

  public static final Finder<Long, User> FIND = new Finder<Long, User>(Long.class, User.class);

  public User(JsonNode userInJson) {
    id = userInJson.get("id").asLong();
    name = userInJson.get("name").asText();
    active = true;
  }

  public static List<User> getAllActive() {
    return User.FIND.where().eq("active", true).findList();
  }

  public void printStatForToday() {

    DayStatistic stat = getDayStatisticForToday();

    long availableSecondsForToday = 0L;
    if (stat != null) {
      availableSecondsForToday = stat.availabilityInSeconds;
    }
    Logger.info("Name: {}, available: {} sec", name, availableSecondsForToday);
  }

  public DayStatistic getDayStatisticForToday() {
    return DayStatistic.getToday(this);
  }

  public long getAvailabilityInSecondsForToday() {
    DayStatistic stat = getDayStatisticForToday();
    if (stat == null) {
      return 0;
    }
    else {
      return stat.availabilityInSeconds;
    }
  }

  public String availabilityForToday(){
    return LocalTime.MIN.plusSeconds(getAvailabilityInSecondsForToday()).toString();
  }
}
