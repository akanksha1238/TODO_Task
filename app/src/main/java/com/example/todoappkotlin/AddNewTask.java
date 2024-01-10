package com.example.todoappkotlin;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todoappkotlin.Model.ToDoModel;
import com.example.todoappkotlin.Utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "AddNewTask";
    private EditText mEditText;
    private EditText mDueDateEditText;
    private EditText mPriorityEditText;
    private EditText mCategoryEditText;
    private EditText mDescriptionEditText;
    private Button mSaveButton;
    private DataBaseHelper myDb;

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_newtask, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditText = view.findViewById(R.id.edittext);
        mDueDateEditText = view.findViewById(R.id.edit_due_date);
        mPriorityEditText = view.findViewById(R.id.edit_priority);
        mCategoryEditText = view.findViewById(R.id.edit_category);
        mDescriptionEditText = view.findViewById(R.id.edit_description);
        mSaveButton = view.findViewById(R.id.button_save);
        myDb = new DataBaseHelper(getActivity());
        boolean isUpdate = false;
        Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            mEditText.setText(task);
            String dueDate = bundle.getString("dueDate"); // Retrieve due date from bundle
            String priority = bundle.getString("priority"); // Retrieve priority from bundle
            String category = bundle.getString("category"); // Retrieve category from bundle
            String description = bundle.getString("description"); // Retrieve description from bundle

            mEditText.setText(task);
            mDueDateEditText.setText(dueDate);
            mPriorityEditText.setText(priority);
            mCategoryEditText.setText(category);
            mDescriptionEditText.setText(description);
            if (task.length() > 0) {
                mSaveButton.setEnabled(false);
            }
        }

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.toString().equals("")) {
                    mSaveButton.setEnabled(false);
                    mSaveButton.setBackgroundColor(Color.GRAY);
                } else {
                    mSaveButton.setEnabled(true);
                    mSaveButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        final boolean finalIsUpdate = isUpdate;
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString();
                String dueDate = mDueDateEditText.getText().toString();
                String priority = mPriorityEditText.getText().toString();
                String category = mCategoryEditText.getText().toString();
                String description = mDescriptionEditText.getText().toString();

                if (finalIsUpdate) {
                    myDb.updateTask(bundle.getInt("id"), text, dueDate, priority, category, description);
                } else {
                    ToDoModel item = new ToDoModel();
                    item.setTask(text);
                    item.setDueDate(dueDate);
                    item.setPriority(priority);
                    item.setCategory(category);
                    item.setDescription(description);
                    item.setStatus(0);
                    myDb.insertTask(item);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener) {
            ((OnDialogCloseListener) activity).onDialogClose(dialog);
        }
    }
}
