package com.example.migamecenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.migamecenter.bean.BaseGameBean;
import com.example.migamecenter.bean.GameInfo;
import com.example.migamecenter.bean.GameInfoPage;
import com.example.migamecenter.httpUtils.HttpManager;
import com.example.migamecenter.httpUtils.NetCallBack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchActivity extends FragmentActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText etGameKeywords;
    private Button btnSearchGame;

    private ImageView ivBackToHomePage;

    private String searchKeywords = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        etGameKeywords = findViewById(R.id.et_game);
        btnSearchGame = findViewById(R.id.btn_homework1);
        ivBackToHomePage = findViewById(R.id.iv_back_to_home_page);

        etGameKeywords.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.drawable_padding));
        etGameKeywords.setInputType(InputType.TYPE_CLASS_TEXT);
        etGameKeywords.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        bindListeners();

        // 检查是否有传递的关键字，如果有，则执行搜索操作
//        String keywords = getIntent().getStringExtra("keywords");
//        if (keywords != null && !keywords.isEmpty()) {
//            etGameKeywords.setText(keywords);
//            searchKeywords = keywords;
//            fetchData();
//        } else {
//            // 默认显示搜索记录
//            showResultHistoryFragment();
//        }
        // 默认显示搜索记录
        showResultHistoryFragment();
    }

    private void bindListeners() {

        ivBackToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        etGameKeywords.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchKeywords = etGameKeywords.getText().toString().trim();
                addToSearchHistory(searchKeywords);
                fetchData();
                return true;
            }
            return false;
        });
        btnSearchGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchKeywords = etGameKeywords.getText().toString();
                addToSearchHistory(searchKeywords);
                fetchData();

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });
    }

    private void fetchData() {
        HttpManager manager = new HttpManager();
        manager.searchGame(searchKeywords, 1, 10, new NetCallBack<BaseGameBean<GameInfoPage>>() {
            @Override
            public void onSuccess(BaseGameBean<GameInfoPage> data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        if (data.data != null && !data.data.records.isEmpty()) {
                            // 显示搜索结果不为空的Fragment
                            showResultIsNotNullFragment(data.data.records);
                        } else {
                            // 显示搜索结果为空的Fragment
                            showResultIsNullFragment();
                        }
                    }
                });
            }

            @Override
            public void onFailure(int code, String errMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(SearchActivity.this, "网络请求失败: " + errMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showResultIsNotNullFragment(List<GameInfo> gameInfoList) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // 创建 ResultIsNotNullFragment 并传递数据
        ResultIsNotNullFragment resultIsNotNullFragment = ResultIsNotNullFragment.newInstance(gameInfoList, searchKeywords);

        // 替换当前显示的 Fragment 为 ResultIsNotNullFragment
        fragmentTransaction.replace(R.id.search_result_fragment_container, resultIsNotNullFragment);
        fragmentTransaction.commit();
    }

    private void showResultIsNullFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // 创建 ResultIsNullFragment
        ResultIsNullFragment resultIsNullFragment = new ResultIsNullFragment();

        // 替换当前显示的 Fragment 为 ResultIsNullFragment
        fragmentTransaction.replace(R.id.search_result_fragment_container, resultIsNullFragment);
        fragmentTransaction.commit();
    }

    private void showResultHistoryFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // 创建 ResultHistoryFragment
        ResultHistoryFragment resultHistoryFragment = new ResultHistoryFragment();

        // 替换当前显示的 Fragment 为 ResultHistoryFragment
        fragmentTransaction.replace(R.id.search_result_fragment_container, resultHistoryFragment);
        fragmentTransaction.commit();
    }

    private void addToSearchHistory(String keywords) {
        Set<String> historySet = getSearchHistory();
        historySet.add(keywords);
        saveSearchHistory(historySet);
    }

    // 从 SharedPreferences 中获取搜索历史记录
    public Set<String> getSearchHistory() {
        SharedPreferences preferences = getSharedPreferences("SearchHistory", Context.MODE_PRIVATE);
        return preferences.getStringSet("history", new HashSet<>());
    }

    // 将搜索历史记录保存到 SharedPreferences 中
    private void saveSearchHistory(Set<String> historySet) {
        SharedPreferences preferences = getSharedPreferences("SearchHistory", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet("history", historySet);
        editor.apply();
    }
}

