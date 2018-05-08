package rosehulman.edu.pictochat.firebase;

import java.util.HashMap;
import java.util.Map;

public class FirebaseKeyHelper {

    private static final Map<String, String> INVALID_CHARACTERS;
    static {
        INVALID_CHARACTERS = new HashMap<>();
        INVALID_CHARACTERS.put(".", "\\dot\\");
        INVALID_CHARACTERS.put("$", "\\dollar\\");
        INVALID_CHARACTERS.put("[", "\\lbracket\\");
        INVALID_CHARACTERS.put("]", "\\rbracket\\");
        INVALID_CHARACTERS.put("#", "\\hash\\");
        INVALID_CHARACTERS.put("/", "\\slash\\");
    }

    public static String stringToKey(String str) {
        for (String invalidChar : INVALID_CHARACTERS.keySet()) {
            str = str.replace(invalidChar, INVALID_CHARACTERS.get(invalidChar));
        }

        return str;
    }

    public static String keyToString(String key) {
        for (String invalidChar : INVALID_CHARACTERS.keySet()) {
            key = key.replace(INVALID_CHARACTERS.get(invalidChar), invalidChar);
        }

        return key;
    }
}
