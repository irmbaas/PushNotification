package ir.mbaas.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import ir.mbaas.sdk.MBaaS;
import ir.mbaas.sdk.helper.AppConstants;
import ir.mbaas.sdk.helper.StaticMethods;
import ir.mbaas.sdk.logic.PushActions;

/**
 * Created by Mahdi on 8/10/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NotificationContent {

    @JsonProperty(AppConstants.PN_ID)
    public String id;

    @JsonProperty(AppConstants.PN_SENT_ID)
    public String pushSentId;

    @JsonProperty(AppConstants.PN_TITLE)
    public String title;

    @JsonProperty(AppConstants.PN_BODY)
    public String body;

    @JsonProperty(AppConstants.PN_TICKER)
    public String ticker;

    @JsonProperty(AppConstants.PN_SUMMARY)
    public String summary;

    @JsonProperty(AppConstants.PN_BUTTONS)
    public NotificationButtons buttons;

    @JsonProperty(AppConstants.PN_IMAGES)
    public NotificationImages images;

    @JsonProperty(AppConstants.PN_IS_HIDDEN)
    public boolean isHidden;

    @JsonProperty(AppConstants.PN_IS_SILENT)
    public boolean isSilent;

    @JsonProperty(AppConstants.PN_CUSTOM_DATA)
    public String customData;

    @JsonSetter(AppConstants.PN_ACTION_TYPE)
    public void setActionType(String actionTypeStr) {
        try {
            actionType = PushActions.ContentActionType.values()[Integer.parseInt(actionTypeStr)];
        } catch (NumberFormatException nfe) {
            actionType = PushActions.ContentActionType.None;
        } catch (Exception exc) {
            actionType = PushActions.ContentActionType.None;
        }
    }

    @JsonProperty(AppConstants.PN_ACTION_URL)
    public String actionUrl;

    public PushActions.ContentActionType actionType;

    public NotificationContent() {
    }
}

