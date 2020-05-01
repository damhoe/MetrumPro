package com.stho.metrum;

import android.graphics.Color;

/*
   To define the "next" color to be displayed when playing sounds...
 */
public class ColorManager {

    private static final int[] colors = new int[] {
            Color.rgb(78, 0, 205),
            Color.rgb(13, 62, 248),
            Color.rgb(2, 138, 233),
            Color.rgb(0, 204, 124),
            Color.rgb(0, 183, 0),
            Color.rgb(146, 215, 0),
            Color.rgb(236, 195, 0),
            Color.rgb(255, 97, 0),
            Color.rgb(178, 8, 0),
            Color.rgb(94, 1, 0),
    };

    private static int colorIndex = 0;

    public static int getColor() {
        if (colorIndex > 0)
            colorIndex--;
        else
            colorIndex = colors.length - 1;

        return colors[colorIndex];
    }
}
