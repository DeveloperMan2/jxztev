
package com.jxztev.utils;


import java.io.PrintStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;


public class DataFormatUtils {

    public static String formatString(Object obj) {

        if (obj == null) {

            return "";

        }

        return obj.toString();

    }


    public static Double getDouble(Object obj) {

        Double value = Double.valueOf(0.0D);

        if (obj == null)
            return null;

        if ((obj instanceof BigDecimal)) {

            value = Double.valueOf(((BigDecimal) obj).doubleValue());

        } else {

            try {

                value = Double.valueOf(Double.parseDouble(String.valueOf(obj)));

            } catch (Exception localException) {
            }

        }

        return value;

    }


    public static Float getFloat(Object obj) {

        Float value = Float.valueOf(0.0F);

        if (obj == null)
            return null;

        if ((obj instanceof BigDecimal)) {

            value = Float.valueOf(((Float) obj).floatValue());

        } else {

            try {

                value = Float.valueOf(Float.parseFloat(String.valueOf(obj)));

            } catch (Exception localException) {
            }

        }

        return value;

    }


    public static long getRound(Object obj) {

        long rslt = 0L;

        if (obj == null) {

            return 0L;

        }

        try {

            rslt = Math.round(getDouble(obj).doubleValue());

        } catch (Exception localException) {
        }

        return rslt;

    }


    public static int getRound(String s) {

        int rslt = 0;

        if (s == null) {

            return 0;

        }

        try {

            rslt = Math.round(Float.parseFloat(s));

        } catch (Exception localException) {
        }

        return rslt;

    }


    public static Double getRound(Object obj, int scale) {

        Double value = Double.valueOf(0.0D);

        if (obj == null) {

            return value;

        }


        if (scale < 0) {

            throw new IllegalArgumentException("位数必须大于0");

        }


        BigDecimal b = new BigDecimal(obj.toString());

        BigDecimal one = new BigDecimal("1");


        return Double.valueOf(b.divide(one, scale, 4).doubleValue());

    }


    public static String getRoundString(Object obj, int scale) {

        String rs = "";

        if (obj == null) {

            return rs;

        }

        if (scale < 0) {

            throw new IllegalArgumentException("位数必须大于0");

        }

        BigDecimal b = new BigDecimal(obj.toString());

        BigDecimal one = new BigDecimal("1");

        try {

            rs = b.divide(one, scale, 4).toString();

        } catch (Exception localException) {
        }

        return rs;

    }


    public static String getValidString(Object obj, int scale) {

        if (obj == null) {

            return "";

        }

        if (obj.equals("")) {

            return "";

        }

        return getValid(getDouble(obj).doubleValue(), scale);

    }


    public static String getValid(String input, int bit) {

        if (input == null) {

            return "";

        }

        if (input.equals("")) {

            return "";

        }

        return getValid(Double.parseDouble(input), bit);

    }


    public static String getValid(double input, int bit) {

        StringBuffer bf = new StringBuffer("0.");

        for (int i = 1; i < bit; i++) {

            bf.append("0");

        }

        DecimalFormat df = new DecimalFormat("E00");

        String temp = df.format(input);

        BigDecimal bd = new BigDecimal(temp);

        temp = String.valueOf(bd.doubleValue());

        if (temp.endsWith(".0")) {

            temp = temp.substring(0, temp.length() - 2);

        }

        return temp;

    }


    public static void main(String[] arg) {
        String g = "4254252.11255653";
        System.out.println(g + "==" + getValid(g, 1));

    }
}
 