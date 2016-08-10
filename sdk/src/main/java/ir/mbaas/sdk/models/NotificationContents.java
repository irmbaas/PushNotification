package ir.mbaas.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mehdi on 8/10/2016.
 */
public class NotificationContents {
    @JsonProperty("PushNotifications")
    public List<NotificationContent> records = new ArrayList<>();
}
