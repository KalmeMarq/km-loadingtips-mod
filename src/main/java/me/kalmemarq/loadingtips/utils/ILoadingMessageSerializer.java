package me.kalmemarq.loadingtips.utils;

import com.google.gson.JsonObject;

public interface ILoadingMessageSerializer<T extends ILoadingMessage> {
    T fromJson(JsonObject json);
}