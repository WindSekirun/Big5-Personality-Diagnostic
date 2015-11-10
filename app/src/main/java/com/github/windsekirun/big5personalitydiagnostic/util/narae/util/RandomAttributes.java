package com.github.windsekirun.big5personalitydiagnostic.util.narae.util;

import java.util.Random;

/**
 * NaraeTask
 * Class: RandomAttributes
 * Created by WindSekirun on 15. 6. 22..
 */
@SuppressWarnings({"CanBeFinal", "WeakerAccess"})
public class RandomAttributes {
    protected static Random random = new Random();

    public static String getRandomTaskType() {
        StringBuilder buffer = new StringBuilder();

        String chars[] = "a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z".split(",");

        for (int i = 0; i < 5; i++)
            buffer.append(chars[random.nextInt(chars.length)]);

        return buffer.toString();
    }

    public static int getRandomPoolSize() {
        return random.nextInt(20);
    }
}
