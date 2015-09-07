package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import utils.HipChat;

import java.util.List;

public class Application extends Controller {

  public Result updateStatistic() {
    Logger.info("updateStatistic");

    HipChat.updateAllUsersAvailability();

    return ok();
  }

  public Result addUserById(long id) {
    Logger.info("addUserById id:" + id);
    JsonNode userInJson = HipChat.getUserInJson(id);
    User user = new User(userInJson);
    user.save();
    return ok();
  }

  public Result index() {
    List<User> allActiveUser = User.getAllActive();
    for (User user : allActiveUser){
      user.printStatForToday();
    }

    return ok(views.html.index.render(allActiveUser));
  }
}
