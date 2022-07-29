package me.kalmemarq.loadingtips;

import java.io.BufferedReader;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import me.kalmemarq.loadingtips.messagearea.LoadingTipArea;
import me.kalmemarq.loadingtips.messagearea.LoadingTriviaArea;
import me.kalmemarq.loadingtips.utils.LoadingMessageType;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class LoadingTipsManager implements SimpleSynchronousResourceReloadListener {
    public static final Gson GSON = new GsonBuilder().setLenient().setPrettyPrinting().create();
    public static final Random RANDOM = new Random();

    private static final Identifier RESOURCE = new Identifier(LoadingTipsMod.MOD_ID, "texts/loading_messages.json");

    public static boolean loadingMessagesEnabled = true;
    public static boolean loadingTipsEnabled = true;
    public static boolean loadingTriviaEnabled = true;

    public static LoadingTipArea tipArea = new LoadingTipArea(null);
    public static LoadingTriviaArea triviaArea = new LoadingTriviaArea(null);

    @Override
    public Identifier getFabricId() {
        return new Identifier(LoadingTipsMod.MOD_ID, "loading_tips");
    }

    @Override
    public void reload(ResourceManager manager) {
        LoadingTipArea tipA = new LoadingTipArea(null); 
        LoadingTriviaArea triviaA = new LoadingTriviaArea(null); 
        
        for (Resource res : manager.getAllResources(RESOURCE)) {
            try {
                BufferedReader reader = res.getReader();
                JsonObject obj = GSON.fromJson(reader, JsonObject.class);
                
                if (JsonHelper.hasBoolean(obj, "enabled")) {
                    loadingMessagesEnabled = JsonHelper.getBoolean(obj, "enabled");
                }

                if (JsonHelper.hasJsonObject(obj, "loading_tips")) {
                    JsonObject tipsObj = JsonHelper.getObject(obj, "loading_tips");

                    if (JsonHelper.hasBoolean(tipsObj, "enabled")) {
                        loadingTipsEnabled = JsonHelper.getBoolean(tipsObj, "enabled");
                    }

                    boolean replace = JsonHelper.getBoolean(tipsObj, "replace", false);
                    LoadingTipArea area = (LoadingTipArea)LoadingTipsMod.getAreaSerializer(LoadingMessageType.TIP).fromJson(tipsObj);

                    if (replace) {
                        tipA = area;
                    } else {
                        tipA.append(area);
                    }
                }

                if (JsonHelper.hasJsonObject(obj, "loading_trivia")) {
                    JsonObject triviaObj = JsonHelper.getObject(obj, "loading_trivia");

                    if (JsonHelper.hasBoolean(triviaObj, "enabled")) {
                        loadingTriviaEnabled = JsonHelper.getBoolean(triviaObj, "enabled");
                    }

                    boolean replace = JsonHelper.getBoolean(triviaObj, "replace", false);
                    LoadingTriviaArea area = (LoadingTriviaArea)LoadingTipsMod.getAreaSerializer(LoadingMessageType.TRIVIA).fromJson(triviaObj);

                    if (replace) {
                        triviaA = area;
                    } else {
                        triviaA.append(area);
                    }
                }

            } catch (Exception e) {
                LoadingTipsMod.LOGGER.info("Could not load " + RESOURCE.toString() + " from " + res.getResourcePackName());
            }
        }

        tipArea = tipA;
        triviaArea = triviaA;

        LoadingTipsMod.LOGGER.info("Loaded " + tipA.getTotal() + " tips");
        LoadingTipsMod.LOGGER.info("Loaded " + triviaA.getTotal() + " trivia");
    }

    // public static ILoadingMessage getRandomTip() {
    //     try {
    //         ILoadingMessage lm = tips.get(random.nextInt(tips.size()));
    //         return lm;
    //     } catch (Exception e) {
    //         return null;
    //     }
    // }

    // public static ILoadingMessage getRandomTrivia() {
    //     try {
    //         ILoadingMessage lm = trivia.get(random.nextInt(trivia.size()));
    //         return lm;
    //     } catch (Exception e) {
    //         return null;
    //     }
    // }

    // public static boolean isTipVisible(Screen screen) {
    //     boolean is = false;

    //     if (tipsVisible.size() == 0) {
    //         is = true;
    //     } else {
    //         for (String var : tipsVisible) {
    //             if (LoadingMessageVariables.check(var, screen)) {
    //                 is = true;
    //                 break;
    //             }
    //         }

    //         for (String var : tipsIgnored) {
    //             if (LoadingMessageVariables.check(var, screen)) {
    //                 is = false;
    //                 break;
    //             }
    //         }
    //     }

    //     return is;
    // } 

    // public static boolean isTriviaVisible(Screen screen) {
    //     boolean is = false;

    //     if (triviaVisible.size() == 0) {
    //         is = true;
    //     } else {
    //         for (String var : triviaVisible) {
    //             if (LoadingMessageVariables.check(var, screen)) {
    //                 is = true;
    //                 break;
    //             }
    //         }

    //         for (String var : triviaIgnored) {
    //             if (LoadingMessageVariables.check(var, screen)) {
    //                 is = false;
    //                 break;
    //             }
    //         }
    //     }

    //     return is;
    // } 

    // public static ILoadingMessage getRandomTip(Screen screen) {
    //     List<ILoadingMessage> t = new ArrayList<>();

    //     for (ILoadingMessage it : tips) {
    //         if (it.isVisible(screen) && !it.isIgnored(screen)) {
    //             t.add(it);
    //         }
    //     }

    //     if (t.size() == 0) {
    //         return null;
    //     } else {
    //         return t.get(random.nextInt(t.size()));
    //     }
    // }

    // public static ILoadingMessage getRandomTrivia(Screen screen) {
    //     List<ILoadingMessage> t = new ArrayList<>();

    //     for (ILoadingMessage it : trivia) {
    //         if (it.isVisible(screen) && !it.isIgnored(screen)) {
    //             t.add(it);
    //         }
    //     }

    //     if (t.size() == 0) {
    //         return null;
    //     } else {
    //         return t.get(random.nextInt(t.size()));
    //     }
    // }
}
