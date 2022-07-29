package me.kalmemarq.loadingtips.utils;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

public class Anchor {
    private static Map<String, Anchor> ANCHORS = Maps.newHashMap();
    
    public static Anchor TOP_LEFT = new Anchor("top_left");
    public static Anchor TOP_MIDDLE = new Anchor("top_middle");
    public static Anchor TOP_RIGHT = new Anchor("top_right");
    public static Anchor LEFT_MIDDLE = new Anchor("left_middle");
    public static Anchor CENTER = new Anchor("center");
    public static Anchor RIGHT_MIDDLE = new Anchor("right_middle");
    public static Anchor BOTTOM_LEFT = new Anchor("bottom_left");
    public static Anchor BOTTOM_MIDDLE = new Anchor("bottom_middle");
    public static Anchor BOTTOM_RIGHT = new Anchor("bottom_right");

    private Anchor(String name) {
        ANCHORS.put(name, this);
    }

    public static boolean isValid(String anchor) {
        return ANCHORS.keySet().contains(anchor);
    }

    @Nullable
    public static Anchor getFromName(String name) {
        return ANCHORS.get(name);
    }
}
