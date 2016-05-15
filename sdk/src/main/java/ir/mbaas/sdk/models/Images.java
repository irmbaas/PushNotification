package ir.mbaas.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import ir.mbaas.sdk.dfapi.ApiException;
import ir.mbaas.sdk.dfapi.ApiInvoker;

public class Images {
    @JsonProperty("Images")
    public List<Image> records = new ArrayList<>();

    public enum ImageType {
        Small, Large
    }

    public static Images fromJson(String imagesStr) {
        try {
            imagesStr = "{\"Images\":" + imagesStr + "}";
            return (Images) ApiInvoker.deserialize(imagesStr, "", Images.class);
        } catch (ApiException e) {
            e.printStackTrace();
        }

        return null;
    }
}
