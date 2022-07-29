package me.kalmemarq.loadingtips;

import me.kalmemarq.loadingtips.utils.LoadingMessageDrawer;
import me.kalmemarq.loadingtips.utils.LoadingMessageVariables;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

public class LoadingTipsClientMod implements ClientModInitializer {
    public static String[] DEFAULT_VISIBLE = new String[]{ "$disconnected_screen", "$connect_screen", "$level_loading_screen", "$progress_screen", "$message_screen", "$game_menu_screen", "$death_screen" };
    
    @Override
    public void onInitializeClient() {
        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (LoadingMessageVariables.canRenderOnScreen(screen)) {
                ScreenEvents.afterRender(screen).register((scrn, matrices, mouseX, mouseY, delta) -> {
                    if (LoadingTipsManager.loadingMessagesEnabled) {
                        if (LoadingTipsManager.loadingTipsEnabled) LoadingMessageDrawer.drawTip(matrices, client.textRenderer, screen);
                        if (LoadingTipsManager.loadingTriviaEnabled) LoadingMessageDrawer.drawTrivia(matrices, client.textRenderer, screen);
                    }
                });
            }
        });
    }
}
