package Socket;

public class ChatTargetManager {

    private static String targetNameAccount = null; // Client cần gửi tới

    public static void setTargetNameAccount(String nameAccount) {
        targetNameAccount = nameAccount;
    }

    public static String getTargetNameAccount() {
        return targetNameAccount;
    }

    public static boolean hasTarget() {
        return targetNameAccount != null;
    }

    public static void clearTarget() {
        targetNameAccount = null;
    }
}
