package com.example.krokzakrokom.data.preferences;
import android.content.Context;
import android.content.SharedPreferences;
import com.example.krokzakrokom.data.model.Item;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppSharedPreferences {

    private static final String PREF_NAME = "com.example.krokzakrokom.PREFERENCE_FILE_KEY";

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    private static final String  ID_KEY = "ID_KEY";

    @Inject
    public AppSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public <T> void saveObject(String key, T object) {
        String json = gson.toJson(object);
        sharedPreferences.edit().putString(key, json).apply();
        if (object instanceof Item) {
            saveId(((Item) object).getId());
        }
    }

    public <T> T getObject(String key, Class<T> clazz) {
        String json = sharedPreferences.getString(key, null);
        if (json != null) {
            return gson.fromJson(json, clazz);
        }
        return null;
    }

    public <T>void removeObject(String key, Class<T> clazz) {
        if (clazz != null && Item.class.isAssignableFrom(clazz)) {
            deleteId(key);
        }
        sharedPreferences.edit().remove(key).apply();
    }

    public List<Item> loadAllItems() {
        Set<String> set = sharedPreferences.getStringSet(ID_KEY, new HashSet<>());
        List<String> list = new ArrayList<>(set);
        Collections.sort(list);
        Set<String> sortedSet = new LinkedHashSet<>(list);
        List<Item> mutableList = new ArrayList<>();
        Item internal;
        for (String id : sortedSet) {
            internal = getObject(id, Item.class);
            if (internal != null) {
                mutableList.add(internal);
            }
        }
        return mutableList;
    }

    private void saveId(String id) {
        Set<String> set = sharedPreferences.getStringSet(ID_KEY, new HashSet<>());
        Set<String> mutableSet = new HashSet<>(set);
        mutableSet.add(id);
        sharedPreferences.edit().putStringSet(ID_KEY, mutableSet).apply();
    }

    private void deleteId(String id) {
        Set<String> set = sharedPreferences.getStringSet(ID_KEY, new HashSet<>());
        Set<String> mutableSet = new HashSet<>(set);
        if (mutableSet.contains(id)) {
            mutableSet.remove(id);
            sharedPreferences.edit().putStringSet(ID_KEY, mutableSet).apply();
        }
    }
}
