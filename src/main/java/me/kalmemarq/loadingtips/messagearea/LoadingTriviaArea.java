package me.kalmemarq.loadingtips.messagearea;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.kalmemarq.loadingtips.LMParser;
import me.kalmemarq.loadingtips.LoadingTipsMod;
import me.kalmemarq.loadingtips.LMParser.LMParsed;
import me.kalmemarq.loadingtips.message.LoadingMessage;
import me.kalmemarq.loadingtips.message.LoadingTrivia;
import me.kalmemarq.loadingtips.utils.Alignment;
import me.kalmemarq.loadingtips.utils.Anchor;
import me.kalmemarq.loadingtips.utils.ILoadingMessage;
import me.kalmemarq.loadingtips.utils.ILoadingMessageAreaSerializer;
import me.kalmemarq.loadingtips.utils.LoadingMessageType;
import net.minecraft.text.Text;
import net.minecraft.util.JsonHelper;

public class LoadingTriviaArea extends LoadingMessageArea {
    public static ILoadingMessageAreaSerializer<LoadingTriviaArea> SERIALIZER = new Serializer();

    protected Anchor anchorFrom = Anchor.TOP_RIGHT;
    protected Anchor anchorTo = Anchor.TOP_RIGHT;

    public LoadingTriviaArea(Integer spaceGap) {
        this(spaceGap, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public LoadingTriviaArea(Integer spaceGap, List<ILoadingMessage> messages, List<String> visible, List<String> ignored) {
        super(spaceGap, messages, visible, ignored);
    }

    private static final class Serializer implements ILoadingMessageAreaSerializer<LoadingTriviaArea> {
        public LoadingTriviaArea fromJson(JsonObject obj) {
            List<ILoadingMessage> list = new ArrayList<>();

            List<String> vis = new ArrayList<>();
            List<String> ig = new ArrayList<>();
            @Nullable
            Integer spaceGap = null;
            @Nullable
            Anchor ancFrom = null;
            @Nullable
            Anchor ancTo = null;
            @Nullable
            Alignment align = null;
            @Nullable
            LMParsed off = null;
            @Nullable
            LMParsed sz = null;
            
            if (JsonHelper.hasJsonObject(obj, "trivia_area")) {
                JsonObject areaObj = JsonHelper.getObject(obj, "trivia_area");

                if (JsonHelper.hasArray(areaObj, "visible")) {
                    JsonArray visArr = JsonHelper.getArray(areaObj, "visible");

                    for (JsonElement visIt : visArr) {
                        vis.add(visIt.getAsString());
                    }
                }

                if (JsonHelper.hasArray(areaObj, "ignored")) {
                    JsonArray igArr = JsonHelper.getArray(areaObj, "ignored");

                    for (JsonElement igIt : igArr) {
                        ig.add(igIt.getAsString());
                    }
                }

                if (JsonHelper.hasString(areaObj, "anchor_from")) {
                    String from = JsonHelper.getString(areaObj, "anchor_from");
                    if (Anchor.isValid(from)) ancFrom = Anchor.getFromName(from);
                }

                if (JsonHelper.hasString(areaObj, "anchor_to")) {
                    String from = JsonHelper.getString(areaObj, "anchor_to");
                    if (Anchor.isValid(from)) ancTo = Anchor.getFromName(from);
                }

                if (JsonHelper.hasString(areaObj, "text_alignment")) {
                    String al = JsonHelper.getString(areaObj, "text_alignment");
                    if (Alignment.isValid(al)) align = Alignment.getFromName(al);
                }

                LMParser parser = new LMParser();

                if (JsonHelper.hasArray(areaObj, "size")) {
                    JsonArray szArr = JsonHelper.getArray(areaObj, "size");

                    if (szArr.size() == 2) {
                        sz = parser.parse(szArr, false);
                    }
                }

                if (JsonHelper.hasArray(areaObj, "offset")) {
                    JsonArray offArr = JsonHelper.getArray(areaObj, "offset");

                    if (offArr.size() == 2) {
                        off = parser.parse(offArr, true);
                    }
                }

                if (JsonHelper.hasNumber(areaObj, "space_gap")) {
                    spaceGap = JsonHelper.getInt(areaObj, "space_gap");
                }
            }

            if (JsonHelper.hasArray(obj, "trivia")) {
                JsonArray tipsArr = JsonHelper.getArray(obj, "trivia");

                for (JsonElement item : tipsArr) {
                    if (JsonHelper.isString(item)) {
                        list.add(new LoadingTrivia(LoadingMessage.DEFAULT_TRIVIA_TITLE, Text.translatable(item.getAsString()), LoadingMessage.DEFAULT_STAY_TIME));
                    } else if (item.isJsonObject()) {
                        JsonObject itemObj = item.getAsJsonObject();

                        LoadingTrivia msg = (LoadingTrivia)LoadingTipsMod.getMsgSerializer(LoadingMessageType.TIP).fromJson(itemObj);

                        list.add(msg);
                    }
                }
            }

            LoadingTriviaArea triviaArea = new LoadingTriviaArea(spaceGap, list, vis, ig);
            triviaArea.setAnchor(ancFrom, ancTo);
            triviaArea.setTextAlignment(align);
            triviaArea.setOffset(off);
            triviaArea.setSize(sz);
            return triviaArea;
        }
    }
}
