package me.kalmemarq.loadingtips.utils;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.gui.screen.MessageScreen;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SleepingChatScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.BeaconScreen;
import net.minecraft.client.gui.screen.ingame.BlastFurnaceScreen;
import net.minecraft.client.gui.screen.ingame.BrewingStandScreen;
import net.minecraft.client.gui.screen.ingame.CartographyTableScreen;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.screen.ingame.FurnaceScreen;
import net.minecraft.client.gui.screen.ingame.Generic3x3ContainerScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.GrindstoneScreen;
import net.minecraft.client.gui.screen.ingame.HopperScreen;
import net.minecraft.client.gui.screen.ingame.HorseScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.LoomScreen;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.client.gui.screen.ingame.SmokerScreen;
import net.minecraft.client.gui.screen.ingame.StonecutterScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;

public class LoadingMessageVariables {
    private static final Map<String, CheckFunc> VARIABLES = Maps.newHashMap();

    static {
        register("$disconnected_screen", (Screen screen) -> screen instanceof DisconnectedScreen);
        register("$connect_screen", (Screen screen) -> screen instanceof ConnectScreen);
        register("$level_loading_screen", (Screen screen) -> screen instanceof LevelLoadingScreen);
        register("$progress_screen", (Screen screen) -> screen instanceof ProgressScreen);
        register("$message_screen", (Screen screen) -> screen instanceof MessageScreen);
        register("$game_menu_screen", (Screen screen) -> screen instanceof GameMenuScreen);
        register("$death_screen", (Screen screen) -> screen instanceof DeathScreen);
        register("$title_screen", (Screen screen) -> screen instanceof TitleScreen);
        register("$options_screen", (Screen screen) -> screen instanceof OptionsScreen);
        register("$world_create_screen", (Screen screen) -> screen instanceof CreateWorldScreen);
        register("$world_edit_screen", (Screen screen) -> screen instanceof EditWorldScreen);
        register("$in_bed_screen", (Screen screen) -> screen instanceof SleepingChatScreen);
        register("$container_screen",
                (Screen screen) ->
                        screen instanceof AnvilScreen
                        || screen instanceof BeaconScreen
                        || screen instanceof BlastFurnaceScreen
                        || screen instanceof BrewingStandScreen
                        || screen instanceof CartographyTableScreen
                        || screen instanceof CraftingScreen
                        || screen instanceof CreativeInventoryScreen
                        || screen instanceof EnchantmentScreen
                        || screen instanceof ForgingScreen
                        || screen instanceof Generic3x3ContainerScreen
                        || screen instanceof GenericContainerScreen
                        || screen instanceof GrindstoneScreen
                        || screen instanceof HopperScreen
                        || screen instanceof HorseScreen
                        || screen instanceof InventoryScreen
                        || screen instanceof LoomScreen
                        || screen instanceof MerchantScreen
                        || screen instanceof ShulkerBoxScreen
                        || screen instanceof SmithingScreen
                        || screen instanceof SmokerScreen
                        || screen instanceof FurnaceScreen
                        || screen instanceof StonecutterScreen
                );
    }

    public static void register(String name, CheckFunc checkFunc) {
        VARIABLES.put(name, checkFunc);
    }

    public static boolean check(String name, Screen screen) {
        if (VARIABLES.containsKey(name)) {
            return VARIABLES.get(name).check(screen);
        } else {
            return false;
        }
    }

    public static boolean canRenderOnScreen(Screen screen) {
        boolean can = false;

        for (Map.Entry<String, CheckFunc> e : VARIABLES.entrySet()) {
            if (e.getValue().check(screen)) {
                can = true;
                break;
            }
        }

        return can;
    }

    interface CheckFunc {
        boolean check(Screen screen);
    }
}
