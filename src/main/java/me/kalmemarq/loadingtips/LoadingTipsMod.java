package me.kalmemarq.loadingtips;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import me.kalmemarq.loadingtips.message.LoadingTip;
import me.kalmemarq.loadingtips.message.LoadingTrivia;
import me.kalmemarq.loadingtips.messagearea.LoadingTipArea;
import me.kalmemarq.loadingtips.messagearea.LoadingTriviaArea;
import me.kalmemarq.loadingtips.utils.ILoadingMessageAreaSerializer;
import me.kalmemarq.loadingtips.utils.ILoadingMessageSerializer;
import me.kalmemarq.loadingtips.utils.LoadingMessageType;

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
