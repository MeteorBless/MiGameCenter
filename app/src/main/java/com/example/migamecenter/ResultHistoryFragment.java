package com.example.migamecenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ResultHistoryFragment extends Fragment {

    private LinearLayout historyLayout;
    private Button deleteButton;

    private List<String> searchHistoryList;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result_history, container, false);

        historyLayout = rootView.findViewById(R.id.history_layout);
        deleteButton = rootView.findViewById(R.id.btn_delete_history);

        // 加载搜索历史记录
        loadSearchHistory();

        // 如果搜索历史记录为空，隐藏整个 fragment
        if (searchHistoryList.isEmpty()) {
            rootView.setVisibility(View.GONE);
        } else {
            rootView.setVisibility(View.VISIBLE);
            // 显示搜索历史记录
            showSearchHistory();
        }

        // 点击删除按钮，删除所有搜索记录
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSearchHistory();
            }
        });

        return rootView;
    }

    // 加载搜索历史记录

    private void loadSearchHistory() {
        Set<String> historySet = ((SearchActivity) requireActivity()).getSearchHistory();
        searchHistoryList = new ArrayList<>(historySet);
        // 如果搜索历史记录超过 30 条，则只保留最新的 30 条记录
        if (searchHistoryList.size() > 30) {
            searchHistoryList = searchHistoryList.subList(0, 30);
        }
    }

    // 显示搜索历史记录
    private void showSearchHistory() {
        historyLayout.removeAllViews(); // 清空历史记录布局

        // 循环遍历搜索历史记录，并添加到布局中
        for (String history : searchHistoryList) {
            TextView historyTextView = new TextView(requireContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 0, 0, 16); // 设置每个历史记录之间的间距
            historyTextView.setLayoutParams(layoutParams);
            historyTextView.setText(trimStringIfNeeded(history)); // 如果字符串长度超过 15，截取并加上 "..."
            historyLayout.addView(historyTextView);
        }
    }

    // 清空搜索历史记录
    private void clearSearchHistory() {
        SharedPreferences preferences = requireContext().getSharedPreferences("SearchHistory", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        Toast.makeText(requireContext(), "搜索历史记录已清空", Toast.LENGTH_SHORT).show();
        historyLayout.removeAllViews(); // 清空历史记录布局
        requireView().setVisibility(View.GONE); // 隐藏整个 fragment
    }

    // 如果字符串长度超过 15，截取并加上 "..."
    private String trimStringIfNeeded(String input) {
        if (input.length() > 15) {
            return input.substring(0, 15) + "...";
        } else {
            return input;
        }
    }

}