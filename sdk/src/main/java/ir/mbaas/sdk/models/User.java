package ir.mbaas.sdk.models;

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

    @Override
    public String toString() {
        String retVal = firstName != null && !firstName.isEmpty() ? "FirstName=" + firstName : "";
        retVal += lastName != null && !lastName.isEmpty() ? "&LastName=" + lastName : "";
        retVal += phoneNumber != null && !phoneNumber.isEmpty() ? "&PhoneNumber=" + phoneNumber : "";

        return retVal;
    }
}
