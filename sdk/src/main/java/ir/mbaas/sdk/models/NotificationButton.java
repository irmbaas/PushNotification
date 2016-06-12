package ir.mbaas.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import ir.mbaas.sdk.MBaaS;
import ir.mbaas.sdk.helper.StaticMethods;
import ir.mbaas.sdk.logic.PushActions;

/**
 * Created by Mahdi on 5/11/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NotificationButton {

    @JsonProperty("Title")
    public String title;

    @JsonProperty("ActionUrl")
    public String actionUrl;

    @JsonSetter("Icon")
    public void setIcon(String iconName) {
        iconResourceId = StaticMethods.getIconResourceByMaterialName(MBaaS.context, iconName);
        icon = iconName;
    }

    @JsonSetter("Action")
    public void setButtonAction(String actionTypeStr) {
        try {
            actionType = PushActions.ButtonActionType.values()[Integer.parseInt(actionTypeStr)];
        } catch (NumberFormatException nfe) {
            actionType = PushActions.ButtonActionType.OpenApp;
        } catch (Exception exc) {
            actionType = PushActions.ButtonActionType.OpenApp;
        }
    }

    public PushActions.ButtonActionType actionType;
    public String icon;
    public int iconResourceId;

    public NotificationButton() {
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

