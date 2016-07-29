package ir.mbaas.sdk.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mahdi on 5/29/2016.
 */
public class User {
    public String firstName;
    public String lastName;
    public String phoneNumber;

    public User(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        try {
            object.put("FirstName", firstName != null ? firstName : "");
            object.put("LastName", lastName != null ? lastName : "");
            object.put("PhoneNumber", phoneNumber != null ? phoneNumber : "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }
}
