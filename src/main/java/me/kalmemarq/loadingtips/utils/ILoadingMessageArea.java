package me.kalmemarq.loadingtips.utils;

import net.minecraft.client.gui.screen.Screen;

public interface ILoadingMessageArea {
    public int getTotal();

    public ILoadingMessage getRandom(Screen screen);

    public boolean isVisible(Screen screen);

    public boolean isIgnored(Screen screen);
}
