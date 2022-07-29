package me.kalmemarq.loadingtips.utils;

import com.google.gson.JsonObject;

public interface ILoadingMessageAreaSerializer<T extends ILoadingMessageArea> {
    T fromJson(JsonObject json);
}