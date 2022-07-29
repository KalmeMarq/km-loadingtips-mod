package me.kalmemarq.loadingtips.message;

import java.util.ArrayList;
import java.util.List;

import me.kalmemarq.loadingtips.utils.ILoadingMessage;
import me.kalmemarq.loadingtips.utils.LoadingMessageVariables;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public abstract class LoadingMessage implements ILoadingMessage {
    public static final int DEFAULT_STAY_TIME = 5000;
    public static final Text DEFAULT_TIP_TITLE = Text.translatable("tips.title").formatted(Formatting.GOLD, Formatting.UNDERLINE, Formatting.BOLD);
    public static final Text DEFAULT_TRIVIA_TITLE = Text.translatable("trivia.title").formatted(Formatting.AQUA, Formatting.UNDERLINE, Formatting.BOLD);
    public static final Text EMPTY_MESSAGE = Text.literal("");

    private final Text title;
    private final Text text;
    private final int stayTime;
    private final List<String> visible;
    private final List<String> ignored;

    public LoadingMessage(Text title, Text text, int stayTime) {
        this(title, text, stayTime, new ArrayList<>(), new ArrayList<>());
    }

    public LoadingMessage(Text title, Text text, int stayTime, List<String> visible, List<String> ignored) {
        this.title = title;
        this.text = text;
        this.stayTime = stayTime;
        this.visible = visible;
        this.ignored = ignored;
    }

    public Text getTitle() {
        return this.title;
    }

    public Text getText() {
        return this.text;
    }

    public int getStayTime() {
        return this.stayTime;
    }

    public boolean isVisible(Screen screen) {
        boolean is = false;

        if (this.visible.size() == 0) {
            is = true;
        } else {
            for (String var : this.visible) {
                if (LoadingMessageVariables.check(var, screen)) {
                    is = true;
                    break;
                }
            }
        }

        return is;
    }

    public boolean isIgnored(Screen screen) {
        boolean is = false;

        for (String var : this.ignored) {
            if (LoadingMessageVariables.check(var, screen)) {
                is = true;
                break;
            }
        }

        return is;
    }

    public String toString() {
        return "LoadingMessage[" + this.title.getString() + ",message=" + this.text.getString() + ",stayTime=" + this.stayTime +"]";
    }
}