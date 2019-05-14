package cijoe;

public class Util {

    public static byte[] toByte(Integer[] items) {
        byte rval[] = new byte[items.length];
        for (int i = 0; i < rval.length; i++) {
            rval[i] = items[i].byteValue();
        }
        return rval;
    }

    public static void invariant(boolean cond, String message) {
        if (!cond) {
            throw new RuntimeException(message);
        }
    }

    public static void invariant(boolean cond) {
        invariant(cond, "Invariant failed");
    }
}
