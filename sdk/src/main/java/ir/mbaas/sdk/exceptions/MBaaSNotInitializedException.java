package ir.mbaas.sdk.exceptions;

/**
 * Created by Mahdi on 6/11/2016.
 */
public class MBaaSNotInitializedException extends RuntimeException {

    public MBaaSNotInitializedException() {
        this("MBaaS sdk is not initialized.");
    }

    public MBaaSNotInitializedException(String detailMessage) {
        super(detailMessage);
    }
}
