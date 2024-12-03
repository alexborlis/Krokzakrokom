package com.example.krokzakrokom.presentation.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.krokzakrokom.data.model.Item;
import com.example.krokzakrokom.data.preferences.AppSharedPreferences;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ToDoListViewModel extends ViewModel {

    private MutableLiveData<List<Item>> _items = new MutableLiveData<List<Item>>();
    public LiveData<List<Item>> items = _items;

    private final AppSharedPreferences preferences;

    @Inject
    public ToDoListViewModel(AppSharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void getAllItems() {
        _items.setValue(preferences.loadAllItems());
    }

    public void replaceItem(Item item) {
        List<Item> items = preferences.loadAllItems();
        Optional<Item> firstMatchingItem = items.stream()
                .filter(it -> it.getId().equals(item.getId()))
                .findFirst();
        if (firstMatchingItem.isPresent()) {
            deleteItem(item.getId());
            saveItem(item);
        }
        _items.setValue(preferences.loadAllItems());
    }

    public void removeItem(int position) {
        List<Item> currentItems = _items.getValue();
        if (currentItems != null && position >= 0 && position < currentItems.size()) {
            Item itemToDelete = currentItems.get(position);
            deleteItem(itemToDelete.getId());
            _items.setValue(preferences.loadAllItems());
        }
    }

    private void deleteItem(String key) {
        preferences.removeObject(key, Item.class);
    }

    private void saveItem(Item item) {
        preferences.saveObject(item.getId(), item);
    }

}
