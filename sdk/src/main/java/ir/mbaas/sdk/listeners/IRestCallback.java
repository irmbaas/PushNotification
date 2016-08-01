package ir.mbaas.sdk.listeners;

/**
 * Created by Mehdi on 8/1/2016.
 */
public interface IRestCallback {
    public void onSuccess(String result);
    public void onError(Exception e);
}
