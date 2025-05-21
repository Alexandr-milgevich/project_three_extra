package gigabank.accountmanagement.utility;

import java.util.Objects;

public class Utility {
    public static boolean isFilled(String value) {
        return !Objects.isNull(value) && !value.isBlank();
    }

    public static boolean isFilled(Long value) {
        return value != null;
    }
}
