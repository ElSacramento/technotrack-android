package com.example.clay.first_task;

/**
 * Created by kate on 23.03.16.
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class RussianNumbers {

    private static final String EDINICHI[] = {"ноль", "один", "два", "три", "четыре",
            "пять", "шесть", "семь", "восемь", "девять"};
    private static final String DESYAT[] = {"десять", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать",
            "пятнадцать", "шестнадцать", "семнадцать", "восемнадцать", "девятнадцать"};
    private static final String DESYATKI[] = {"", "десять", "двадцать", "тридцать", "сорок", "пятьдесят", "шестьдесят",
            "семьдесят", "восемьдесят", "девяносто"};
    private static final String SOTNI[] = {"", "сто", "двести", "триста", "четыреста", "пятьсот", "шестьсот",
            "семьсот", "восемьсот", "девятьсот"};

    public String toString(int number) {
        if (number == 0) {
            return EDINICHI[0];
        }
        if (number == 1000000) {
            return "миллион";
        }
        String str_tys = "тысяч";
        ArrayList<String> sb = new ArrayList<>();

        int ed = 0, des = 0, stn = 0, tys = 0, tys10 = 0, tys100 = 0;
        ed = number % 10;
        if (number / 10 != 0) {
            des = (number % 100 - ed) / 10;
            if (des == 1)
                sb.add(DESYAT[ed]);
            else {
                sb.add(EDINICHI[ed]);
                sb.add(DESYATKI[des]);
            }
        }
        else sb.add(EDINICHI[ed]);
        if (number / 100 != 0) {
            stn = (number % 1000 - des * 10 - ed) / 100;
            sb.add(SOTNI[stn]);
        }
        if (number / 1000 != 0) {
            tys = (number % 10000 - stn * 100 - des * 10 - ed) / 1000;
            if (tys == 1) str_tys = "тысяча";
            if (tys > 1 && tys < 5) str_tys = "тысячи";
            if (tys > 4) str_tys = "тысяч";
        }
        if (number / 10000 != 0) {
            tys10 = (number % 100000 - tys * 1000 - stn * 100 - des * 10 - ed) / 10000;
            if (tys10 == 1) str_tys = "тысяч";
        }
        if (number / 100000 != 0) {
            tys100 = (number % 1000000 - tys10 * 10000 - tys * 1000 - stn * 100 - des * 10 - ed) / 100000;
        }
        if (tys != 0 || tys10 != 0 || tys100 != 0) {
            sb.add(str_tys);
        }
        if (tys10 == 1)
            sb.add(DESYAT[tys]);
        else {
            switch (tys) {
                case 1:
                    sb.add("одна");
                    break;
                case 2:
                    sb.add("две");
                    break;
                default:
                    sb.add(EDINICHI[tys]);
                    break;
            }
            sb.add(DESYATKI[tys10]);
        }
        sb.add(SOTNI[tys100]);
        Collections.reverse(sb);
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < sb.size(); i++) {
            if (sb.get(i) != "") {
                result.append(sb.get(i));
                if (i != sb.size() - 1) result.append(" ");
            }
        }
        return result.toString();
    }

    public String[] createMillion(){
        String[] result = new String[1000001];
        for (int i = 1; i <= 1000000; i++)
            result[i] = this.toString(i);
        return result;
    }


}
