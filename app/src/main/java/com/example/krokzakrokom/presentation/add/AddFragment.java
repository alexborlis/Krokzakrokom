package com.example.krokzakrokom.presentation.add;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.krokzakrokom.R;
import com.example.krokzakrokom.databinding.FragmentDetailsBinding;
import com.example.krokzakrokom.presentation.list.ToDoListViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddFragment extends Fragment {

    private FragmentDetailsBinding binding;
    private AddViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AddViewModel.class);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        openKeyboard();

        binding.etTitle.requestFocus();
        binding.etTitle.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                openKeyboard();
            }
        });

        binding.etDescription.setOnFocusChangeListener(((v, hasFocus) -> {
            if (hasFocus) {
                openKeyboard();
            }
        }));

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.etTitle.getText().toString().isEmpty()) {
                    viewModel.addNewItem(binding.etTitle.getText().toString(), binding.etDescription.getText().toString());
                    NavHostFragment.findNavController(AddFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                } else {
                    Toast.makeText(requireContext(), "Заповніть задачу", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(binding.etTitle, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}