package ir.mbaas.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Created by Mahdi on 5/11/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Button {

    @JsonProperty("Title")
    public String title;

    @JsonProperty("Icon")
    public String icon;

    @JsonSetter("Action")
    public void setButtonAction(int mAction) {
        try {
            action = Buttons.ButtonAction.values()[mAction];
        } catch (Exception exc) {
            action = Buttons.ButtonAction.None;
        }
    }

    public Buttons.ButtonAction action;

    public Button() {
    }

    public void setAllNonNull() {
        title = getNonNull(title);
        icon  = getNonNull(icon);
    }

    public String getNonNull(String toCheck){
        // just so we don't go display a null string
        if(toCheck == null){
            return "";
        }
        return toCheck;
    }
}

