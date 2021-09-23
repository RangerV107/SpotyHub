package com.rangerv.spotyhub.extensions;

public class ColorARGB {
    public int color, a, r, g, b;

    public ColorARGB(int a, int r, int g, int b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
        color = (a & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
    }

    public ColorARGB(int color) {
        this.color = color;
        a = (color >> 24) & 0xff;
        r = (color >> 16) & 0xff;
        g = (color >>  8) & 0xff;
        b = (color      ) & 0xff;
    }
}
