package ru.svetomech.currencyconverter.ui.shared;

public class Numbers {
    public static String FilterDecimal(String str, int maxBeforePoint, int maxDecimal, int maxOverall) {
        if (str.charAt(0) == '.') str = "0" + str;
        int max = str.length();

        String rFinal = "";
        boolean after = false;
        int i = 0, up = 0, decimal = 0;
        char t;
        while (i < max) {
            t = str.charAt(i);
            if (t != '.' && after == false) {
                up++;
                if (up > maxBeforePoint) return rFinal;
            } else if (t == '.') {
                after = true;
            } else {
                decimal++;
                if (decimal > maxDecimal)
                    return rFinal;
            }
            rFinal = rFinal + t;
            i++;
        }
        if (Float.parseFloat(rFinal) > maxOverall) {
            rFinal = String.valueOf(maxOverall);
        }
        return rFinal;
    }
}
