package utils;

import akka.actor.Props;
import akka.actor.UntypedActor;

public class StatisticUpdateActor extends UntypedActor {

  public static final String MSG_UPDATE_USER_STATISTIC = "updateUserStatistic";

  public static Props props = Props.create(StatisticUpdateActor.class);

  @Override
  public void onReceive(Object message) throws Exception {
    if (message.equals(MSG_UPDATE_USER_STATISTIC)) {
      HipChat.updateAllUsersAvailability();
    }
  }
}