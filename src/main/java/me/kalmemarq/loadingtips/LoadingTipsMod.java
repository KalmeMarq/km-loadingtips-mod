package me.kalmemarq.loadingtips;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.block.SideShapeType;
import net.minecraft.resource.ResourceType;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.ibm.icu.impl.number.UsagePrefsHandler;

import me.kalmemarq.loadingtips.LMParser.LMParsed;
import me.kalmemarq.loadingtips.message.LoadingTip;
import me.kalmemarq.loadingtips.message.LoadingTrivia;
import me.kalmemarq.loadingtips.messagearea.LoadingTipArea;
import me.kalmemarq.loadingtips.messagearea.LoadingTriviaArea;
import me.kalmemarq.loadingtips.utils.ILoadingMessageAreaSerializer;
import me.kalmemarq.loadingtips.utils.ILoadingMessageSerializer;
import me.kalmemarq.loadingtips.utils.LoadingMessageType;
import me.kalmemarq.loadingtips.utils.Vector2;

public class LoadingTipsMod implements ModInitializer {
    public static final String MOD_ID = "kmloadingtips";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static Map<LoadingMessageType, ILoadingMessageSerializer<?>> msgSerializers = Maps.newHashMap();
    private static Map<LoadingMessageType, ILoadingMessageAreaSerializer<?>> areaSerializers = Maps.newHashMap();

    @Override
    public void onInitialize() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new LoadingTipsManager());
        registerMsgSerializer(LoadingMessageType.TIP, LoadingTip.SERIALIZER);
        registerAreaSerializer(LoadingMessageType.TIP, LoadingTipArea.SERIALIZER);
        registerMsgSerializer(LoadingMessageType.TRIVIA, LoadingTrivia.SERIALIZER);
        registerAreaSerializer(LoadingMessageType.TRIVIA, LoadingTriviaArea.SERIALIZER);

        // LMParser parser = new LMParser();
        // Vector2 contentSize = new Vector2(200, 20);

        // JsonArray sizeArray = new JsonArray();
        // sizeArray.add("100%");
        // sizeArray.add("100%c + 4px");

        // Vector2 parent = new Vector2(200, 100);
        // boolean isOffset = false;

        // LMParsed cont = parser.parse(sizeArray, isOffset);

        // LOGGER.info("PARSED X: " + cont.x);
        // LOGGER.info("PARSED Y: " + cont.y);

        // try {
        //     int[] size = cont.evaluate(parent, contentSize, new int[]{parent.getX(), parent.getY()}, isOffset);
        //     LOGGER.info("PARSED SIZE: " + size[0] + ", " + size[1]);
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
    }

    public static void registerMsgSerializer(LoadingMessageType type, ILoadingMessageSerializer<?> serializer) {
        msgSerializers.put(type, serializer);
    }

    public static void registerAreaSerializer(LoadingMessageType type, ILoadingMessageAreaSerializer<?> serializer) {
        areaSerializers.put(type, serializer);
    }

    public static ILoadingMessageSerializer<?> getMsgSerializer(LoadingMessageType type) {
        return msgSerializers.get(type);
    }

    public static ILoadingMessageAreaSerializer<?> getAreaSerializer(LoadingMessageType type) {
        return areaSerializers.get(type);
    }
}
