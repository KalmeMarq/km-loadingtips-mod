package me.kalmemarq.loadingtips;

import com.google.gson.JsonArray;

import me.kalmemarq.loadingtips.LMParser.LMParsed;

public class LMParserTest {
    @Test
    public void test() {
        JsonArray arr = new JsonArray();
        arr.add(20);
        arr.add(50);

        LMParser parser = new LMParser();
        LMParsed cont = parser.parse(arr);

        System.out.println(cont.x.toString());
    }
}