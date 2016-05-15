package ir.mbaas.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import ir.mbaas.sdk.dfapi.ApiException;
import ir.mbaas.sdk.dfapi.ApiInvoker;

public class Buttons {
    @JsonProperty("Buttons")
    public List<Button> records = new ArrayList<>();

    public static Buttons fromJson(String buttonsStr) {
        try {
            buttonsStr = "{\"Buttons\":" + buttonsStr + "}";
            return (Buttons) ApiInvoker.deserialize(buttonsStr, "", Buttons.class);
        } catch (ApiException e) {
            e.printStackTrace();
        }

        return null;
    }
}
