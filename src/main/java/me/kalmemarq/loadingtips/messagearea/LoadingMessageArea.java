package me.kalmemarq.loadingtips.messagearea;

import java.util.ArrayList;
import java.util.List;

import org.checkerframework.common.subtyping.qual.Bottom;
import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonArray;
import com.ibm.icu.text.BidiTransform.Order;

import me.kalmemarq.loadingtips.LMParser;
import me.kalmemarq.loadingtips.LoadingTipsClientMod;
import me.kalmemarq.loadingtips.LoadingTipsManager;
import me.kalmemarq.loadingtips.LMParser.LMParsed;
import me.kalmemarq.loadingtips.utils.Alignment;
import me.kalmemarq.loadingtips.utils.Anchor;
import me.kalmemarq.loadingtips.utils.ILoadingMessage;
import me.kalmemarq.loadingtips.utils.ILoadingMessageArea;
import me.kalmemarq.loadingtips.utils.LoadingMessageVariables;
import me.kalmemarq.loadingtips.utils.Rectangle;
import me.kalmemarq.loadingtips.utils.Vector2;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextHandler;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

public abstract class LoadingMessageArea implements ILoadingMessageArea {
    public static final Integer DEFAULT_SPACE_GAP = 2;

    @Nullable
    private LMParsed offset;
    @Nullable
    private LMParsed size;
    @Nullable
    private Integer spaceGap;
    @Nullable
    protected Anchor anchorFrom = null;
    @Nullable
    protected Anchor anchorTo = null;

    @Nullable
    protected Alignment textAlignment = null;

    private List<String> visible;
    private List<String> ignored;

    public List<ILoadingMessage> messages;

    public LoadingMessageArea(@Nullable Integer spaceGap) {
        this(spaceGap, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public LoadingMessageArea(@Nullable Integer spaceGap, List<ILoadingMessage> messages, List<String> visible, List<String> ignored) {
        this.spaceGap = spaceGap;
        this.messages = messages;
        this.visible = visible;
        this.ignored = ignored;
    }
    
    public int getTotal() {
        return this.messages.size();
    }

    public int getSpaceGap() {
        if (this.spaceGap == null) {
            return 2;
        }

        return this.spaceGap;
    }

    public void setAnchor(@Nullable Anchor from, @Nullable Anchor to) {
        this.anchorFrom = from;
        this.anchorTo = to;
    }

    public void setTextAlignment(@Nullable Alignment alignment) {
        this.textAlignment = alignment;
    }

    public void setOffset(@Nullable LMParsed offset) {
        this.offset = offset;
    }

    public void setSize(@Nullable LMParsed size) {
        this.size = size;
    }

    public LMParsed getOffset() {
        if (this.offset == null) {
            JsonArray arr = new JsonArray();
            arr.add(0);
            arr.add(0);
            this.offset = new LMParser().parse(arr, true);
        }
        return this.offset;
    }

    public LMParsed getSize() {
        if (this.size == null) {
            JsonArray arr = new JsonArray();
            arr.add("100%");
            arr.add("100%");
            this.size = new LMParser().parse(arr, false);
        }
        return this.size;
    }


    public Rectangle getArea(Screen screen, ILoadingMessage message) {
        int x = 0;
        int y = 0;
        int maxWidth = 200;

        Text title = message.getTitle();
        Text msg = message.getText();

        Anchor ancFrom = anchorFrom == null ? Anchor.TOP_LEFT : anchorFrom;
        Anchor ancTo = anchorTo == null ? Anchor.TOP_LEFT : anchorTo;

        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer textRenderer = mc.textRenderer;
    
        int width = 0;
        int height = 0;

        int textWidth = 0;

        if (textRenderer.getWidth(title) > textWidth) {
            textWidth = textRenderer.getWidth(title);
        }

        if (textRenderer.getWidth(msg) > textWidth) {
            textWidth = textRenderer.getWidth(msg);
        }

        List<OrderedText> t = textRenderer.wrapLines(title, maxWidth);
        List<OrderedText> a = textRenderer.wrapLines(msg, maxWidth);

        int evrryH = t.size() * textRenderer.fontHeight + getSpaceGap() + a.size() * textRenderer.fontHeight;

        int fWidth = maxWidth;
        int fHeight = evrryH;

        int[] off = getOffset().evaluate(new Vector2(screen.width, screen.height), new Vector2(fWidth, fHeight), new int[]{0, 0}, true);
        int fX = off[0];
        int fY = off[1];

        if (ancFrom == Anchor.TOP_LEFT) {
            if (ancTo == Anchor.TOP_MIDDLE || ancTo == Anchor.CENTER || ancTo == Anchor.BOTTOM_MIDDLE) {
                fX -= fWidth / 2;
            }

            if (ancTo == Anchor.TOP_RIGHT || ancTo == Anchor.RIGHT_MIDDLE || ancTo == Anchor.BOTTOM_RIGHT) {
                fX -= fWidth;
            }

            if (ancTo == Anchor.LEFT_MIDDLE || ancTo == Anchor.CENTER || ancTo == Anchor.RIGHT_MIDDLE) {
                fY -= fHeight / 2;
            }

            if (ancTo == Anchor.BOTTOM_LEFT || ancTo == Anchor.BOTTOM_MIDDLE || ancTo == Anchor.BOTTOM_RIGHT) {
                fY -= fHeight;
            }
        } else if (ancFrom == Anchor.TOP_MIDDLE) {
            if (ancTo == Anchor.TOP_LEFT || ancTo == Anchor.LEFT_MIDDLE || ancTo == Anchor.BOTTOM_LEFT) {
                fX = screen.width / 2;
            }

            if (ancTo == Anchor.TOP_RIGHT || ancTo == Anchor.RIGHT_MIDDLE || ancTo == Anchor.BOTTOM_RIGHT) {
                fX = screen.width / 2 - fWidth;
            }

            if (ancTo == Anchor.TOP_MIDDLE || ancTo == Anchor.CENTER || ancTo == Anchor.BOTTOM_MIDDLE) {
                fX = screen.width / 2 - fWidth / 2;
            }

            if (ancTo == Anchor.LEFT_MIDDLE || ancTo == Anchor.CENTER || ancTo == Anchor.RIGHT_MIDDLE) {
                fY -= fHeight / 2;
            }

            if (ancTo == Anchor.BOTTOM_LEFT || ancTo == Anchor.BOTTOM_MIDDLE || ancTo == Anchor.BOTTOM_RIGHT) {
                fY -= fHeight;
            }
        } else if (ancFrom == Anchor.TOP_RIGHT) {
            if (ancTo == Anchor.TOP_LEFT || ancTo == Anchor.LEFT_MIDDLE || ancTo == Anchor.BOTTOM_LEFT) {
                fX = screen.width;
            }

            if (ancTo == Anchor.TOP_RIGHT || ancTo == Anchor.RIGHT_MIDDLE || ancTo == Anchor.BOTTOM_RIGHT) {
                fX = screen.width - fWidth;
            }

            if (ancTo == Anchor.TOP_MIDDLE || ancTo == Anchor.CENTER || ancTo == Anchor.BOTTOM_MIDDLE) {
                fX = screen.width - fWidth / 2;
            }

            if (ancTo == Anchor.LEFT_MIDDLE || ancTo == Anchor.CENTER || ancTo == Anchor.RIGHT_MIDDLE) {
                fY -= fHeight / 2;
            }

            if (ancTo == Anchor.BOTTOM_LEFT || ancTo == Anchor.BOTTOM_MIDDLE || ancTo == Anchor.BOTTOM_RIGHT) {
                fY -= fHeight;
            }
        } else if (ancFrom == Anchor.LEFT_MIDDLE) {
            if (ancTo == Anchor.TOP_RIGHT || ancTo == Anchor.RIGHT_MIDDLE || ancTo == Anchor.BOTTOM_RIGHT) {
                fX -= fWidth;
            }

            if (ancTo == Anchor.TOP_MIDDLE || ancTo == Anchor.CENTER || ancTo == Anchor.BOTTOM_MIDDLE) {
                fX -= - fWidth / 2;
            }

            if (ancTo == Anchor.LEFT_MIDDLE || ancTo == Anchor.CENTER || ancTo == Anchor.RIGHT_MIDDLE) {
                fY = screen.height / 2 - fHeight / 2;
            }

            if (ancTo == Anchor.BOTTOM_LEFT || ancTo == Anchor.BOTTOM_MIDDLE || ancTo == Anchor.BOTTOM_RIGHT) {
                fY = screen.height / 2 - fHeight;
            }
        } else if (ancFrom == Anchor.CENTER) {
            if (ancTo == Anchor.TOP_RIGHT || ancTo == Anchor.RIGHT_MIDDLE || ancTo == Anchor.BOTTOM_RIGHT) {
                fX = screen.width / 2 - fWidth;
            }

            if (ancTo == Anchor.TOP_MIDDLE || ancTo == Anchor.CENTER || ancTo == Anchor.BOTTOM_MIDDLE) {
                fX = screen.width / 2 - fWidth / 2;
            }

            if (ancTo == Anchor.LEFT_MIDDLE || ancTo == Anchor.CENTER || ancTo == Anchor.RIGHT_MIDDLE) {
                fY = screen.height / 2 - fHeight / 2;
            }

            if (ancTo == Anchor.BOTTOM_LEFT || ancTo == Anchor.BOTTOM_MIDDLE || ancTo == Anchor.BOTTOM_RIGHT) {
                fY = screen.height / 2 - fHeight;
            }
        } else if (ancFrom == Anchor.RIGHT_MIDDLE) {
            if (ancTo == Anchor.TOP_RIGHT || ancTo == Anchor.RIGHT_MIDDLE || ancTo == Anchor.BOTTOM_RIGHT) {
                fX = screen.width - fWidth;
            }

            if (ancTo == Anchor.TOP_MIDDLE || ancTo == Anchor.CENTER || ancTo == Anchor.BOTTOM_MIDDLE) {
                fX = screen.width - fWidth / 2;
            }

            if (ancTo == Anchor.LEFT_MIDDLE || ancTo == Anchor.CENTER || ancTo == Anchor.RIGHT_MIDDLE) {
                fY = screen.height / 2 - fHeight / 2;
            }

            if (ancTo == Anchor.BOTTOM_LEFT || ancTo == Anchor.BOTTOM_MIDDLE || ancTo == Anchor.BOTTOM_RIGHT) {
                fY = screen.height / 2 - fHeight;
            }
        } else if (ancFrom == Anchor.BOTTOM_LEFT) {
            if (ancTo == Anchor.TOP_RIGHT || ancTo == Anchor.RIGHT_MIDDLE || ancTo == Anchor.BOTTOM_RIGHT) {
                fX -= fWidth;
            }

            if (ancTo == Anchor.TOP_MIDDLE || ancTo == Anchor.CENTER || ancTo == Anchor.BOTTOM_MIDDLE) {
                fX -= - fWidth / 2;
            }

            if (ancTo == Anchor.LEFT_MIDDLE || ancTo == Anchor.CENTER || ancTo == Anchor.RIGHT_MIDDLE) {
                fY = screen.height - fHeight / 2;
            }

            if (ancTo == Anchor.BOTTOM_LEFT || ancTo == Anchor.BOTTOM_MIDDLE || ancTo == Anchor.BOTTOM_RIGHT) {
                fY = screen.height - fHeight;
            }
        } else if (ancFrom == Anchor.BOTTOM_MIDDLE) {
            if (ancTo == Anchor.TOP_RIGHT || ancTo == Anchor.RIGHT_MIDDLE || ancTo == Anchor.BOTTOM_RIGHT) {
                fX = screen.width / 2 - fWidth;
            }

            if (ancTo == Anchor.TOP_MIDDLE || ancTo == Anchor.CENTER || ancTo == Anchor.BOTTOM_MIDDLE) {
                fX = screen.width / 2 - fWidth / 2;
            }

            if (ancTo == Anchor.LEFT_MIDDLE || ancTo == Anchor.CENTER || ancTo == Anchor.RIGHT_MIDDLE) {
                fY = screen.height - fHeight / 2;
            }

            if (ancTo == Anchor.BOTTOM_LEFT || ancTo == Anchor.BOTTOM_MIDDLE || ancTo == Anchor.BOTTOM_RIGHT) {
                fY = screen.height - fHeight;
            }
        } else if (ancFrom == Anchor.BOTTOM_RIGHT) {
            if (ancTo == Anchor.TOP_RIGHT || ancTo == Anchor.RIGHT_MIDDLE || ancTo == Anchor.BOTTOM_RIGHT) {
                fX = screen.width - fWidth;
            }

            if (ancTo == Anchor.TOP_MIDDLE || ancTo == Anchor.CENTER || ancTo == Anchor.BOTTOM_MIDDLE) {
                fX = screen.width - fWidth / 2;
            }

            if (ancTo == Anchor.LEFT_MIDDLE || ancTo == Anchor.CENTER || ancTo == Anchor.RIGHT_MIDDLE) {
                fY = screen.height - fHeight / 2;
            }

            if (ancTo == Anchor.BOTTOM_LEFT || ancTo == Anchor.BOTTOM_MIDDLE || ancTo == Anchor.BOTTOM_RIGHT) {
                fY = screen.height - fHeight;
            }
        }
     
        return new Rectangle(fX, fY, fWidth, fHeight);
    }

    public List<OrderedText> getTitle(Rectangle area, ILoadingMessage message) {
        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer textRenderer = mc.textRenderer;
        return textRenderer.wrapLines(message.getTitle(), area.getWidth());
    }

    public List<OrderedText> getMessage(Rectangle area, ILoadingMessage message) {
        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer textRenderer = mc.textRenderer;
        return textRenderer.wrapLines(message.getText(), area.getWidth());
    }

    public ILoadingMessage getRandom(Screen screen) {
        List<ILoadingMessage> t = new ArrayList<>();

        for (ILoadingMessage it : messages) {
            if (it.isVisible(screen) && !it.isIgnored(screen)) {
                t.add(it);
            }
        }

        if (t.size() == 0) {
            return null;
        } else {
            return t.get(LoadingTipsManager.RANDOM.nextInt(t.size()));
        }
    }

    public Anchor getAnchorFrom() {
        return this.anchorFrom == null ? Anchor.TOP_LEFT : this.anchorFrom;
    }

    public Anchor getAnchorTo() {
        return this.anchorTo == null ? Anchor.TOP_LEFT : this.anchorTo;
    }

    public Alignment getTextAlignment() {
        return this.textAlignment == null ? Alignment.getFromName(this.anchorFrom) : this.textAlignment;
    }

    public int getXFromAlignment(TextRenderer textRenderer, int x, Alignment alignment, OrderedText orderedText, Rectangle area) {
        int tWidth = textRenderer.getWidth(orderedText);
        int x0 = x;

        if (alignment == Alignment.CENTER) {
            x0 = area.getX() + area.getWidth() / 2 - tWidth / 2;
        } else if (alignment == Alignment.RIGHT) {
            x0 = area.getX() + area.getWidth() - tWidth;
        }

        return x0;
    }

    public void append(LoadingMessageArea area) {
        this.visible.addAll(area.visible);
        this.ignored.addAll(area.ignored);
        this.messages.addAll(area.messages);

        if (area.anchorFrom != null) {
            this.anchorFrom = area.anchorFrom;
        }

        if (area.anchorTo != null) {
            this.anchorTo = area.anchorTo;
        }

        if (area.textAlignment != null) {
            this.textAlignment  = area.textAlignment;
        }

        if (area.offset != null) {
            this.offset  = area.offset;
        }

        if (area.size != null) {
            this.size = area.size;
        }

        if (area.spaceGap != null) {
            this.spaceGap = area.spaceGap;
        }
    }

    public boolean isVisible(Screen screen) {
        boolean is = false;

        if (this.visible.size() == 0) {
            for (String str : LoadingTipsClientMod.DEFAULT_VISIBLE) {
                this.visible.add(str);
            }
        } 
        
        for (String var : this.visible) {
            if (LoadingMessageVariables.check(var, screen)) {
                is = true;
                break;
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
}
