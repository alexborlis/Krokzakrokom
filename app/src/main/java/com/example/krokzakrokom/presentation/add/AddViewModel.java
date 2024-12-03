package com.example.krokzakrokom.presentation.add;

import androidx.lifecycle.ViewModel;

import com.example.krokzakrokom.data.model.Item;
import com.example.krokzakrokom.data.preferences.AppSharedPreferences;

import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddViewModel extends ViewModel {

    private final AppSharedPreferences preferences;

    @Inject
    public AddViewModel(AppSharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void addNewItem(String title, String description) {
        String newKey = generateUniqueKey();
        Item newItem = new Item(newKey, title, description, false, false);
        preferences.saveObject(newItem.getId(), newItem);
    }

    private static String generateUniqueKey() {
        long timestamp = System.currentTimeMillis();
        String randomPart = UUID.randomUUID().toString().replace("-", "");
        return timestamp + "-" + randomPart;
    }
}
