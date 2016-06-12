package ir.mbaas.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import ir.mbaas.sdk.dfapi.ApiException;
import ir.mbaas.sdk.dfapi.ApiInvoker;

public class NotificationImages {
    @JsonProperty("Images")
    public List<NotificationImage> records = new ArrayList<>();

    public enum ImageType {
        Small, Large
    }

    public static NotificationImages fromJson(String imagesStr) {
        try {
            imagesStr = "{\"Images\":" + imagesStr + "}";
            return (NotificationImages) ApiInvoker.deserialize(imagesStr, "", NotificationImages.class);
        } catch (ApiException e) {
            e.printStackTrace();
        }

        return null;
    }
}
