package me.kalmemarq.loadingtips.utils;

import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;

public class Alignment {
    private static Map<String, Alignment> ALIGNMENTS = Maps.newHashMap();
    
    public static Alignment LEFT = new Alignment("left");
    public static Alignment CENTER = new Alignment("center");
    public static Alignment RIGHT = new Alignment("right");

    private Alignment(String name) {
        ALIGNMENTS.put(name, this);
    }

    public static boolean isValid(String Alignment) {
        return ALIGNMENTS.keySet().contains(Alignment);
    }

    @Nullable
    public static Alignment getFromName(String name) {
        return ALIGNMENTS.get(name);
    }

    public static Alignment getFromName(Anchor anchor) {
        if (anchor == Anchor.TOP_LEFT || anchor == Anchor.LEFT_MIDDLE || anchor == Anchor.BOTTOM_LEFT) {
            return Alignment.LEFT;
        } else if (anchor == Anchor.TOP_RIGHT || anchor == Anchor.RIGHT_MIDDLE || anchor == Anchor.BOTTOM_RIGHT) {
            return Alignment.RIGHT;
        }

        return Alignment.CENTER;
    }
}
