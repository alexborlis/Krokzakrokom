package com.example.krokzakrokom.presentation.list;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.krokzakrokom.R;
import com.example.krokzakrokom.data.model.Item;
import com.example.krokzakrokom.databinding.FragmentListBinding;
import com.example.krokzakrokom.presentation.list.adapter.ToDoListAdapter;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ToDoListFragment extends Fragment implements ToDoListAdapter.ClickListener {

    @Inject
    ToDoListAdapter toDoListAdapter;

    private FragmentListBinding binding;
    private ToDoListViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ToDoListViewModel.class);
        viewModel.getAllItems();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvItems.setAdapter(toDoListAdapter);
        toDoListAdapter.clickListener = this;

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    viewModel.removeItem(position);
                    toDoListAdapter.notifyItemRemoved(position);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                if (dX > 0) {
                    c.drawRect(itemView.getLeft(), itemView.getTop(), dX, itemView.getBottom(), paint);
                } else if (dX < 0) {
                    c.drawRect(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom(), paint);
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(binding.rvItems);

        binding.fab.setOnClickListener(v -> {
            NavHostFragment.findNavController(ToDoListFragment.this)
                    .navigate(R.id.action_ListFragment_to_AddFragment);
        });

        viewModel.items.observe(getViewLifecycleOwner(), items -> {
            if (items != null) {
                toDoListAdapter.setItems(items);
            }
            if (items == null || items.isEmpty()) {
                binding.tvMessage.setVisibility(View.VISIBLE);
            } else {
                binding.tvMessage.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        toDoListAdapter.clickListener = null;
    }

    @Override
    public void onCheckClicked(Item item) {
        viewModel.replaceItem(item);
    }

    @Override
    public void onExpand(Item item) {
        viewModel.replaceItem(item);
    }
}