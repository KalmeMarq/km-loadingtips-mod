package me.kalmemarq.loadingtips.utils;

import java.util.List;

import me.kalmemarq.loadingtips.LoadingTipsManager;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;

public class LoadingMessageDrawer {
    public static ILoadingMessage tipMessage = null;
    public static ILoadingMessage triviaMessage = null;

    private static long initTipTime = System.currentTimeMillis();
    private static long initTriviaTime = System.currentTimeMillis();

    public static void setTip(ILoadingMessage message) {
        tipMessage = message;
        initTipTime = System.currentTimeMillis();
    }

    public static void setTrivia(ILoadingMessage message) {
        triviaMessage = message;
        initTriviaTime = System.currentTimeMillis();
    }
    
    public static void drawTip(MatrixStack matrices, TextRenderer textRenderer, Screen screen) {
        if (tipMessage == null) {
            setTip(LoadingTipsManager.tipArea.getRandom(screen));
        }

        if (tipMessage != null) {
            final long currentTime = System.currentTimeMillis();
            final int currentStayTime = tipMessage.getStayTime();

            if (currentTime - initTipTime > currentStayTime) {
                setTip(LoadingTipsManager.tipArea.getRandom(screen));
            }

            if (tipMessage != null && LoadingTipsManager.tipArea.isVisible(screen)) {
                Rectangle rect = LoadingTipsManager.tipArea.getArea(screen, tipMessage);

                Alignment alignment = LoadingTipsManager.tipArea.getTextAlignment();

                List<OrderedText> tL = LoadingTipsManager.tipArea.getTitle(rect, tipMessage);
                
                int y = rect.getY();
                for (OrderedText otL : tL) {
                    textRenderer.drawWithShadow(matrices, otL, LoadingTipsManager.tipArea.getXFromAlignment(textRenderer, rect.getX(), alignment, otL, rect), y, 0xFFFFFF);
                    y += textRenderer.fontHeight;
                }

                y += LoadingTipsManager.tipArea.getSpaceGap();

                List<OrderedText> mL = LoadingTipsManager.tipArea.getMessage(rect, tipMessage);

                for (OrderedText otL : mL) {
                    textRenderer.drawWithShadow(matrices, otL, LoadingTipsManager.tipArea.getXFromAlignment(textRenderer, rect.getX(), alignment, otL, rect), y, 0xFFFFFF);
                    y += textRenderer.fontHeight;
                }
            }
        }
    }

    public static void drawTrivia(MatrixStack matrices, TextRenderer textRenderer, Screen screen) {
        if (triviaMessage == null) {
            setTrivia(LoadingTipsManager.triviaArea.getRandom(screen));
        }

        if (triviaMessage != null) {
            final long currentTime = System.currentTimeMillis();
            final int currentStayTime = triviaMessage.getStayTime();

            if (currentTime - initTriviaTime > currentStayTime) {
                setTrivia(LoadingTipsManager.triviaArea.getRandom(screen));
            }

            if (triviaMessage != null && LoadingTipsManager.triviaArea.isVisible(screen)) {
                Rectangle rect = LoadingTipsManager.triviaArea.getArea(screen, triviaMessage);

                Alignment alignment = LoadingTipsManager.triviaArea.getTextAlignment();

                List<OrderedText> tL = LoadingTipsManager.triviaArea.getTitle(rect, triviaMessage);
                
                int y = rect.getY();
                for (OrderedText otL : tL) {
                    textRenderer.drawWithShadow(matrices, otL, LoadingTipsManager.triviaArea.getXFromAlignment(textRenderer, rect.getX(), alignment, otL, rect), y, 0xFFFFFF);
                    y += textRenderer.fontHeight;
                }

                y += LoadingTipsManager.triviaArea.getSpaceGap();

                List<OrderedText> mL = LoadingTipsManager.triviaArea.getMessage(rect, triviaMessage);

                for (OrderedText otL : mL) {
                    textRenderer.drawWithShadow(matrices, otL, LoadingTipsManager.triviaArea.getXFromAlignment(textRenderer, rect.getX(), alignment, otL, rect), y, 0xFFFFFF);
                    y += textRenderer.fontHeight;
                }
            }
        }
    }
}
