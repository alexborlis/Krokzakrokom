package com.example.krokzakrokom;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.krokzakrokom.data.model.Item;
import com.example.krokzakrokom.data.preferences.AppSharedPreferences;
import com.example.krokzakrokom.presentation.list.ToDoListViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

public class ToDoListViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private AppSharedPreferences mockPreferences;  // Mock the shared preferences

    @InjectMocks
    private ToDoListViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize the mock objects
    }

    @Test
    public void testGetAllItems() {
        // Arrange: Create a list of items to return
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item("1", "Item 1", "Description 1", false, false));
        itemList.add(new Item("2", "Item 2", "Description 2", false, false));

        when(mockPreferences.loadAllItems()).thenReturn(itemList);  // Mock the response of loadAllItems

        // Act: Call the ViewModel method
        viewModel.getAllItems();

        // Assert: Verify that LiveData was updated correctly
        assertNotNull(viewModel.items.getValue());
        assertEquals(2, viewModel.items.getValue().size());
    }

    @Test
    public void testReplaceItem() throws InterruptedException {
        // Arrange: Prepare the items to mock the loadAllItems method
        List<Item> itemList = new ArrayList<>();
        Item existingItem = new Item("1", "Old Item", "Old Description", false, false);
        itemList.add(existingItem);

        // Mock preferences
        when(mockPreferences.loadAllItems()).thenReturn(itemList);
        doNothing().when(mockPreferences).removeObject(Mockito.anyString(), Mockito.any(Class.class));
        doNothing().when(mockPreferences).saveObject(Mockito.anyString(), Mockito.any());

        // Prepare the new item to replace the old one
        Item newItem = new Item("1", "New Item", "New Description", false, false);

        // Act: Set up an observer on the LiveData
        final CountDownLatch latch = new CountDownLatch(1);
        final List<Item> observedItems = new ArrayList<>();
        viewModel.items.observeForever(new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                observedItems.clear();
                observedItems.addAll(items);
                latch.countDown();  // Signal that the LiveData has been updated
            }
        });

        // Act: Call replaceItem on the ViewModel
        viewModel.replaceItem(newItem);

        // Wait for the LiveData update (with a timeout)
        if (!latch.await(2, TimeUnit.SECONDS)) {
            fail("LiveData update timed out.");
        }

        // Use ArgumentCaptor to capture the updated list of items
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(mockPreferences).removeObject(existingItem.getId(), Item.class);
        verify(mockPreferences).saveObject(newItem.getId(), newItem);

        // Assert: Verify that LiveData has been updated
        assertNotNull(observedItems);  // Ensure the observer was triggered
        assertEquals(1, observedItems.size());  // We should only have one item after replacement
        assertEquals(newItem.getTitle(), observedItems.get(0).getTitle());  // Check that the new item is present
    }


    @Test
    public void testRemoveItem() {
        // Arrange: Prepare the items list
        List<Item> itemList = new ArrayList<>();
        Item itemToRemove = new Item("1", "Item 1", "Description 1", false, false);
        itemList.add(itemToRemove);

        when(mockPreferences.loadAllItems()).thenReturn(itemList);

        doNothing().when(mockPreferences).removeObject(Mockito.anyString(), Mockito.any(Class.class));

        // Observer for LiveData
        Observer<List<Item>> observer = Mockito.mock(Observer.class);
        viewModel.items.observeForever(observer);

        // Act: Call removeItem
        viewModel.removeItem(0);

        // Assert: Verify removeObject was called with correct parameters
        verify(mockPreferences).removeObject(itemToRemove.getId(), Item.class);

        // Verify that LiveData was updated after removing the item
        List<Item> updatedItems = viewModel.items.getValue();
        assertNotNull(updatedItems);
        assertTrue(updatedItems.isEmpty());

        // Verify that LiveData was updated by the observer
        verify(observer).onChanged(updatedItems);
    }
}
