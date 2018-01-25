package crab.kit;


public class AssertKit {

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }

    public static void isTrue(boolean expression, String message, Object... objects) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, objects));
        }
    }

}
