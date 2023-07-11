package android.text;

import java.util.ArrayList;

public class TextUtils {
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    public static <E extends Object> String join(CharSequence delimiter, ArrayList<E> array) {
        Object[] tokens = new Object[array.size()];
        for (int i = 0; i < array.size(); i++) {
            tokens[i] = array.get(i);
        }
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }
}
