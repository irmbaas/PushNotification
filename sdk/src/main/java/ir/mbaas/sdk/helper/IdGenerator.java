package ir.mbaas.sdk.helper;

import java.util.Random;
import java.util.UUID;

/***
 * A helper class to generate UUID and random Integer
 */
public class IdGenerator {
    private static Random random = new Random();

    /***
     * generate UUID
     *
     * @param length UUID length
     * @return generated UUID
     */
    public static String generateUUID(int length) {
        return UUID.randomUUID().toString().substring(0, length);
    }

    /***
     * generate random Integer
     *
     * @return generated random Integer
     */
    public static int generateIntegerId() {
        return random.nextInt();
    }
}
