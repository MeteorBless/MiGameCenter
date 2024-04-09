package com.example.migamecenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    private EditText editText;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 找到 EditText 控件
        editText = view.findViewById(R.id.edittext1);

        // 设置触摸事件监听器
//        editText.setOnTouchListener((v, event) -> {
//            // 检查触摸事件是否在右侧图标上
//            if (event.getAction() == MotionEvent.ACTION_UP &&
//                    event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[2].getBounds().width())) {
//                // 在这里执行页面跳转的逻辑
//                startActivity(new Intent(getContext(), SearchActivity.class));
//                return true;
//            }
//            return false;
//        });

        editText = view.findViewById(R.id.edittext1);
        editText.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.drawable_padding));
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String keywords = editText.getText().toString().trim();
                Intent intent = new Intent(getContext(), SearchActivity.class);
                String hint_ = editText.getHint().toString().trim();
                intent.putExtra("keywords", keywords);
                intent.putExtra("hint",hint_);
                requireActivity().startActivity(intent);
                return true;
            }
            return false;
        });


        return view;
    }
}
