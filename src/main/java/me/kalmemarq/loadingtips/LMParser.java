package me.kalmemarq.loadingtips;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import me.kalmemarq.loadingtips.utils.Vector2;
import net.minecraft.util.JsonHelper;

public class LMParser {
    public LMParsed parse(JsonArray arr, boolean isOffset) {
        return new LMParsed(parseX(arr.get(0), isOffset), parseY(arr.get(1), isOffset));
    }

    private List<Token> parseX(JsonElement element, boolean isOffset) {
        List<Token> list = new ArrayList<>();

        if (JsonHelper.isNumber(element)) {
            list.add(new Token(TokenType.PIXEL, JsonHelper.asInt(element, "x")));
        } else {
            String cont = element.getAsString();
            int cursor = 0;
            char curr = cont.charAt(cursor);

            while (cursor < cont.length()) {
                curr = cont.charAt(cursor);

                if (curr == '\n' || curr == '\r' || curr == '\t' || curr == ' ') {
                    cursor++;
                } else if (curr == '+') {
                    list.add(new Token(TokenType.PLUS));
                    cursor++;
                } else if (curr == '-') {
                    list.add(new Token(TokenType.MINUS));
                    cursor++;
                } else if ("0123456789.".indexOf(curr) >= 0) {
                    String v = "";

                    do {
                        v += curr;
                        ++cursor;
                        curr = cont.charAt(cursor);
                    } while ("0123456789.".indexOf(curr) >= 0 && cursor < cont.length());

                    if (cursor + 1 < cont.length()) {
                        if (curr == 'p' && cont.charAt(cursor + 1) == 'x') {
                            list.add(new Token(TokenType.PIXEL, Float.parseFloat(v)));
                            cursor += 2;
                            continue;
                        } else if (curr == '%' && cont.charAt(cursor + 1) == 'x') {
                            if (isOffset) list.add(new Token(TokenType.SELF_HEIGHT, Float.parseFloat(v) / 100));
                            cursor += 2;
                            continue;
                        } else if (curr == '%' && cont.charAt(cursor + 1) == 'y') {
                            list.add(new Token(TokenType.SELF_HEIGHT, Float.parseFloat(v) / 100));
                            cursor += 2;
                            continue;
                        } else if (curr == '%' && cont.charAt(cursor + 1) == 'c') {
                            if (!isOffset) list.add(new Token(TokenType.SELF_SIZE, Float.parseFloat(v) / 100));
                            cursor += 2;
                            continue;
                        }
                    }
                    
                    if (curr == '%') {
                        list.add(new Token(TokenType.PARENT_SIZE, Float.parseFloat(v) / 100));
                        cursor += 1;
                    }
                    
                } else {
                    cursor++;
                }
            }
        }

        return list;
    }

    private List<Token> parseY(JsonElement element, boolean isOffset) {
        List<Token> list = new ArrayList<>();

        if (JsonHelper.isNumber(element)) {
            list.add(new Token(TokenType.PIXEL, JsonHelper.asInt(element, "y")));
        } else {
            String cont = element.getAsString();
            int cursor = 0;
            char curr = cont.charAt(cursor);

            while (cursor < cont.length()) {
                curr = cont.charAt(cursor);

                if (curr == '\n' || curr == '\r' || curr == '\t' || curr == ' ') {
                    cursor++;
                } else if (curr == '+') {
                    list.add(new Token(TokenType.PLUS));
                    cursor++;
                } else if (curr == '-') {
                    list.add(new Token(TokenType.MINUS));
                    cursor++;
                } else if ("0123456789.".indexOf(curr) >= 0) {
                    String v = "";

                    do {
                        v += curr;
                        ++cursor;
                        curr = cont.charAt(cursor);
                    } while ("0123456789.".indexOf(curr) >= 0 && cursor < cont.length());

                    if (cursor + 1 < cont.length()) {
                        if (curr == 'p' && cont.charAt(cursor + 1) == 'x') {
                            list.add(new Token(TokenType.PIXEL, Float.parseFloat(v)));
                            cursor += 2;
                            continue;
                        } else if (curr == '%' && cont.charAt(cursor + 1) == 'x') {
                            list.add(new Token(TokenType.SELF_WIDTH, Float.parseFloat(v) / 100));
                            cursor += 2;
                            continue;
                        } else if (curr == '%' && cont.charAt(cursor + 1) == 'y') {
                            if (isOffset) list.add(new Token(TokenType.SELF_HEIGHT, Float.parseFloat(v) / 100));
                            cursor += 2;
                            continue;
                        } else if (curr == '%' && cont.charAt(cursor + 1) == 'c') {
                            if (!isOffset) list.add(new Token(TokenType.SELF_SIZE, Float.parseFloat(v) / 100));
                            cursor += 2;
                            continue;
                        }
                    }
                    
                    if (curr == '%') {
                        list.add(new Token(TokenType.PARENT_SIZE, Float.parseFloat(v) / 100));
                        cursor += 1;
                    }
                   
                } else {
                    cursor++;
                }
            }
        }
        
        return list;
    }

    public static class LMParsed {
        public List<Token> x;
        public List<Token> y;

        public LMParsed(List<Token> x, List<Token> y) {
            this.x = x;
            this.y = y;
        }

        public int[] evaluate(Vector2 parent, Vector2 size, int[] defaultV, boolean isOffset) {
            int[] res = defaultV;

            boolean needsX = needsToken(y, TokenType.SELF_WIDTH);
            boolean needsY = needsToken(x, TokenType.SELF_HEIGHT);

            if (needsX) {
                res[0] = evaluateX(parent, size, 0);
                res[1] = evaluateY(parent, size, isOffset ? size.getX() : res[0]);
            } else if (needsY) {
                res[1] = evaluateY(parent, size, 0);
                res[0] = evaluateX(parent, size, isOffset ? size.getY() : res[1]);
            } else {
                res[0] = evaluateX(parent, size, 0);
                res[1] = evaluateY(parent, size, 0);
            }

            return res;
        }

        public int evaluateX(Vector2 parent, Vector2 size, int neededY) {
            int res = 0;

            int cursor = 0;
            Token curr = x.get(cursor);

            while (cursor < x.size()) {
                curr = x.get(cursor);

                if (curr.tokenType == TokenType.PLUS) {
                    if (x.get(cursor + 1) != null) {
                        Token next = x.get(cursor + 1);

                        res += getVX(parent, size, neededY, next);

                        cursor += 2;
                    } else {
                        cursor++;
                    }
                } else if (curr.tokenType == TokenType.MINUS) {
                    if (x.get(cursor + 1) != null) {
                        Token next = x.get(cursor + 1);

                        res -= getVX(parent, size, neededY, next);

                        cursor += 2;
                    } else {
                        cursor++;
                    }
                } else {
                    res += getVX(parent, size, neededY, curr);
                    cursor++;
                }
            }

            return res;
        }

        public int getVX(Vector2 parent, Vector2 size, int neededY, Token token) {
            int res = 0;
            
            if (token.tokenType == TokenType.PIXEL) {
                res = (int)token.value;
            } else if (token.tokenType == TokenType.PARENT_SIZE) {
                res = (int)(token.value * parent.getX());
            } else if (token.tokenType == TokenType.SELF_WIDTH) {
                res = (int)(token.value * size.getX());
            } else if (token.tokenType == TokenType.SELF_HEIGHT) {
                res = (int)(token.value * neededY);
            } else if (token.tokenType == TokenType.SELF_SIZE) {
                res = (int)(token.value * size.getX());
            }

            return res;
        }

        public int getVY(Vector2 parent, Vector2 size, int neededX, Token token) {
            int res = 0;
            
            if (token.tokenType == TokenType.PIXEL) {
                res = (int)token.value;
            } else if (token.tokenType == TokenType.PARENT_SIZE) {
                res = (int)(token.value * parent.getX());
            } else if (token.tokenType == TokenType.SELF_WIDTH) {
                res = (int)(token.value * neededX);
            } else if (token.tokenType == TokenType.SELF_HEIGHT) {
                res = (int)(token.value * size.getY());
            } else if (token.tokenType == TokenType.SELF_SIZE) {
                res = (int)(token.value * size.getY());
            }

            return res;
        }

        public int evaluateY(Vector2 parent, Vector2 size, int neededX) {
            int res = 0;

            int cursor = 0;
            Token curr = y.get(cursor);

            while (cursor < y.size()) {
                curr = y.get(cursor);

                if (curr.tokenType == TokenType.PLUS) {
                    if (y.get(cursor + 1) != null) {
                        Token next = y.get(cursor + 1);

                        res += getVY(parent, size, neededX, next);

                        cursor += 2;
                    } else {
                        cursor++;
                    }
                } else if (curr.tokenType == TokenType.MINUS) {
                    if (y.get(cursor + 1) != null) {
                        Token next = y.get(cursor + 1);

                        res -= getVY(parent, size, neededX, next);

                        cursor += 2;
                    } else {
                        cursor++;
                    }
                } else {
                    res += getVY(parent, size, neededX, curr);
                    cursor++;
                }
            }

            return res;
        }

        public boolean needsToken(List<Token> list, TokenType tokenType) {
            boolean needs = false;

            for (Token to : list) {
                if (to.tokenType == tokenType) {
                    needs = true;
                    break;
                }
            }

            return needs;
        }
    }

    public class Token {
        public float value;
        public TokenType tokenType;

        public Token(TokenType tokenType) {
            this.tokenType = tokenType;
        }

        public Token(TokenType tokenType, float value) {
            this.value = value;
            this.tokenType = tokenType;
        }

        public String toString() {
            return "Token[type=" + this.tokenType.toString() + ",value=" + this.value + "]";
        }
    }

    public enum TokenType {
        PIXEL,
        SELF_WIDTH,
        SELF_HEIGHT,
        PARENT_SIZE,
        SELF_SIZE,
        PLUS,
        MINUS,
        DEFAULT
    }
}
