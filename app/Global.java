import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import play.Application;
import play.GlobalSettings;
import play.libs.Akka;
import scala.concurrent.duration.Duration;
import utils.StatisticUpdateActor;

import java.util.concurrent.TimeUnit;

import static utils.HipChat.CRONJOB_INTERVAL_SECONDS;
import static utils.StatisticUpdateActor.*;

public class Global extends GlobalSettings {


  @Override
  public void onStart(Application app) {

    ActorSystem system = Akka.system();
    ActorRef statisticUpdateActor = system.actorOf(Props.create(StatisticUpdateActor.class), "StatisticUpdateActor");

    system.scheduler().schedule(
        Duration.create(10L, TimeUnit.SECONDS),
        Duration.create(CRONJOB_INTERVAL_SECONDS, TimeUnit.SECONDS),
        statisticUpdateActor,
        MSG_UPDATE_USER_STATISTIC,
        system.dispatcher(),
        null);

  }
}
