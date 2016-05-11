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
public class Image {

    @JsonProperty("Size")
    public int size;

    @JsonProperty("Url")
    public String url;

    @JsonSetter("Type")
    public void setImageType(int mType) {
        try {
            type = Images.ImageType.values()[mType];
        } catch (Exception exc) {
            type = Images.ImageType.Left;
        }
    }

    public Images.ImageType type;

    public Image() {
    }

    public void setAllNonNull() {
        url = getNonNull(url);
    }

    public String getNonNull(String toCheck){
        // just so we don't go display a null string
        if(toCheck == null){
            return "";
        }
        return toCheck;
    }
}

