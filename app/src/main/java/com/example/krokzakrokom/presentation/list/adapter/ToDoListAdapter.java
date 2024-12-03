package com.example.krokzakrokom.presentation.list.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.krokzakrokom.R;
import com.example.krokzakrokom.data.model.Item;
import com.example.krokzakrokom.databinding.ListItemToDoBinding;

import java.util.List;

import javax.inject.Inject;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder> {

    public ClickListener clickListener;
    private List<Item> toDoItemList;

    @Inject
    public ToDoListAdapter() {
    }

    public void setItems(List<Item> toDoItemList) {
        this.toDoItemList = toDoItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemToDoBinding binding = ListItemToDoBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item currentItem = toDoItemList.get(position);
        holder.bind(currentItem);
    }

    @Override
    public int getItemCount() {
        return toDoItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemToDoBinding binding;

        public ViewHolder(ListItemToDoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Item item) {
            binding.tvDescription.setText(item.getDescription());
            binding.checkbox.setChecked(item.isDone());
            if (item.getDescription() != null && !item.getDescription().isEmpty()) {
                binding.ivExpand.setVisibility(View.VISIBLE);
            } else {
                binding.ivExpand.setVisibility(View.GONE);
            }
            if (item.isDone()) {
                SpannableString spannableString = new SpannableString(item.getTitle());
                spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                binding.tvTitle.setText(spannableString);
            } else {
                binding.tvTitle.setText(item.getTitle());
            }
            if (item.isExpanded()) {
                binding.tvDescription.setVisibility(View.VISIBLE);
                binding.ivExpand.setImageResource(R.drawable.ic_collapse);
            } else {
                binding.tvDescription.setVisibility(View.GONE);
                binding.ivExpand.setImageResource(R.drawable.ic_expand);
            }
            binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Item newItem = new Item(item.getId(), item.getTitle(), item.getDescription(), !item.isDone(), false);
                    if (clickListener != null) {
                        clickListener.onCheckClicked(newItem);
                        notifyDataSetChanged();
                    }
                }
            });

            binding.ivExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item newItem = new Item(item.getId(), item.getTitle(), item.getDescription(), item.isDone(), !item.isExpanded());
                    if (clickListener != null) {
                        clickListener.onExpand(newItem);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    public interface ClickListener {
        void onCheckClicked(Item item);

        void onExpand(Item item);
    }
}
