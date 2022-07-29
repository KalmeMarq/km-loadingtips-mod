package me.kalmemarq.loadingtips.utils;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public interface ILoadingMessage {
    public Text getTitle();

    public Text getText();

    public int getStayTime();
    
    public boolean isVisible(Screen screen);

    public boolean isIgnored(Screen screen);
}
