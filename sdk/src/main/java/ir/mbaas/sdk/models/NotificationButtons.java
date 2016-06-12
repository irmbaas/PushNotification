package ir.mbaas.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import ir.mbaas.sdk.dfapi.ApiException;
import ir.mbaas.sdk.dfapi.ApiInvoker;

public class NotificationButtons {
    @JsonProperty("Buttons")
    public List<NotificationButton> records = new ArrayList<>();

    public static NotificationButtons fromJson(String buttonsStr) {
        try {
            buttonsStr = "{\"Buttons\":" + buttonsStr + "}";
            return (NotificationButtons) ApiInvoker.deserialize(buttonsStr, "", NotificationButtons.class);
        } catch (ApiException e) {
            e.printStackTrace();
        }

        return null;
    }
}
