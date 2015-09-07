package utils;

import com.fasterxml.jackson.databind.JsonNode;
import models.DayStatistic;
import models.User;
import play.Logger;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;

import java.util.List;


public class HipChat {

  private static final String HIPCHAT_HOST = "https://api.hipchat.com";
  private static final String AUTH_TOKEN = "7xa08bQVnt5wXRnOe9DnvtGeBhxcHkKgpLuELwhQ";
  public static final long CRONJOB_INTERVAL_SECONDS = 10L;

  // user_id 1154180, 1154277, 1282848
  public static void updateAllUsersAvailability() {
    List<User> users = User.getAllActive();
    Logger.info("Update {} users statistic" , users.size());

    users.parallelStream().forEach(HipChat::updateUserAvailability);
  }

  private static void updateUserAvailability(User user) {
    JsonNode userResult = getUserInJson(user.id);
    boolean online = isOnlineAndActive(userResult);
    DayStatistic.updateUserStatistic(user, online, CRONJOB_INTERVAL_SECONDS);
  }

  private static boolean isOnlineAndActive(JsonNode userResult) {
    JsonNode onlineNode = userResult.get("presence").get("is_online");
    JsonNode showNode = userResult.get("presence").get("show");
    return onlineNode != null && onlineNode.asBoolean() && showNode != null && showNode.asText().contentEquals("chat");
  }

  public static WSResponse getRequest(String url) {
    WSResponse response = WS.url(url).get().get(2000L);

    Logger.trace("send request. url: " + url + " response: " + response.getStatus());
    return response;
  }

  public static JsonNode getUserInJson(long id) {
    String request = HIPCHAT_HOST + "/v2/user/" + id + "?auth_token=" + AUTH_TOKEN;
    WSResponse result = getRequest(request);
    if (result.getStatus() == 200) {
      JsonNode json = result.asJson();
      Logger.trace("Get user : " + json);
      return json;
    }
    else {
      Logger.warn("Bad getUserInJson");
      throw new RuntimeException("Bad getUserInJson");
    }

  }
}
