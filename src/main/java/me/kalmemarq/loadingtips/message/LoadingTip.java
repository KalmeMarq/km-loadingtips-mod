package me.kalmemarq.loadingtips.message;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.kalmemarq.loadingtips.utils.ILoadingMessageSerializer;
import net.minecraft.text.Text;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.MathHelper;

public class LoadingTip extends LoadingMessage {
    public static final ILoadingMessageSerializer<LoadingTip> SERIALIZER = new Serializer();

    public LoadingTip(Text title, Text text, int stayTime) {
        this(title, text, stayTime, new ArrayList<>(), new ArrayList<>());
    }

    public LoadingTip(Text title, Text text, int stayTime, List<String> visible, List<String> ignored) {
        super(title, text, stayTime, visible, ignored);
    }

    public static final class Serializer implements ILoadingMessageSerializer<LoadingTip> {
        @Override
        public LoadingTip fromJson(JsonObject obj) {
            int stayTime = MathHelper.clamp(JsonHelper.getInt(obj, "stay_time", LoadingMessage.DEFAULT_STAY_TIME), 0, Integer.MAX_VALUE);
            Text title = LoadingMessage.DEFAULT_TIP_TITLE;
            Text tip = LoadingMessage.EMPTY_MESSAGE;

            if (JsonHelper.hasString(obj, "title")) {
                title = Text.translatable(JsonHelper.getString(obj, "title"));
            } else if (JsonHelper.hasJsonObject(obj, "title")) {
                title = Text.Serializer.fromJson(JsonHelper.getObject(obj, "title"));
            }

            if (JsonHelper.hasString(obj, "tip")) {
                tip = Text.translatable(JsonHelper.getString(obj, "tip"));
            } else if (JsonHelper.hasJsonObject(obj, "tip")) {
                tip = Text.Serializer.fromJson(JsonHelper.getObject(obj, "tip"));
            }

            if (title == LoadingMessage.DEFAULT_TIP_TITLE && tip == LoadingMessage.EMPTY_MESSAGE) {
                tip = Text.Serializer.fromJson(obj);
            }

            List<String> visibles = new ArrayList<>();

            if (JsonHelper.hasArray(obj, "visible")) {
                JsonArray visArr = JsonHelper.getArray(obj, "visible");

                for (JsonElement visIt : visArr) {
                    visibles.add(visIt.getAsString());
                }
            }

            List<String> ignores = new ArrayList<>();

            if (JsonHelper.hasArray(obj, "ignored")) {
                JsonArray igArr = JsonHelper.getArray(obj, "ignored");

                for (JsonElement igIt : igArr) {
                    ignores.add(igIt.getAsString());
                }
            }

            return new LoadingTip(title, tip, stayTime, visibles, ignores);
        }
    }
}
