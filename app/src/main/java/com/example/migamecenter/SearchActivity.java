package com.example.migamecenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.migamecenter.adapter.GameAdapter;
import com.example.migamecenter.adapter.HistoryAdapter;
import com.example.migamecenter.bean.BaseGameBean;
import com.example.migamecenter.bean.GameInfoPage;
import com.example.migamecenter.bean.GameInfo;
import com.example.migamecenter.httpUtils.HttpManager;
import com.example.migamecenter.httpUtils.NetCallBack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchActivity extends FragmentActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private GameAdapter adapter;

    LinearLayoutManager layoutManager;

    private Button btnSearchGame;
    private EditText etGameKeywords;

    private String searchKeywords = "";

    private String keywords;

//    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new GameAdapter();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        btnSearchGame = findViewById(R.id.btn_homework1);
        etGameKeywords = findViewById(R.id.et_game);


        // 搜索记录

        // 加载历史搜索记录

        // 绑定监听
        bindListener();
//        fetchData();

        keywords = getIntent().getStringExtra("keywords");
        String hint_ =  getIntent().getStringExtra("hint");
        etGameKeywords.setHint(hint_);

        // 执行搜索操作
        if (keywords != null && !keywords.isEmpty()) {

            searchKeywords = keywords;

            fetchData();
        }
    }

    private void bindListener(){
        btnSearchGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchKeywords = etGameKeywords.getText().toString();
                fetchData();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fetchData();
                    }
                });
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                Log.i("layoutManager.itemCount","条目总数量为："+totalItemCount);
                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    loadMoreData();
                }
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchData() {

        currentPage = 1;

        HttpManager manager = new HttpManager();
        manager.searchGame(searchKeywords, 1, 10, new NetCallBack<BaseGameBean<GameInfoPage>>() {
            @Override
            public void onSuccess(BaseGameBean<GameInfoPage> data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (data.data != null) {
                            adapter.setGameInfoList(data.data.records);
                        } else {
                            Toast.makeText(SearchActivity.this, "数据为空", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(int code, String errMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SearchActivity.this, "网络请求失败: " + errMsg, Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    private int currentPage = 1;
    private boolean isLoading = false;

    private void loadMoreData() {
        if (!isLoading) {
            isLoading = true;

            HttpManager manager = new HttpManager();
            manager.searchGame(searchKeywords, currentPage, 10, new NetCallBack<BaseGameBean<GameInfoPage>>() {
                @Override
                public void onSuccess(BaseGameBean<GameInfoPage> data) {
                    runOnUiThread(new Runnable() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void run() {
                            isLoading = false;
                            if (data.data != null && !data.data.records.isEmpty()) {
                                currentPage++;
                                List<GameInfo> newGameInfoList = data.data.records;

                                // 添加新数据到适配器
                                adapter.addGameInfoList(newGameInfoList);

                                // 去重处理
                                removeDuplicate();

                                // 刷新列表
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(SearchActivity.this, "已加载完所有搜索结果", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                @Override
                public void onFailure(int code, String errMsg) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isLoading = false;
                            Toast.makeText(SearchActivity.this, "网络请求失败: " + errMsg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    // 去重处理方法
    private void removeDuplicate() {
        List<GameInfo> gameInfoList = adapter.getCurrentGameInfoList();
        Set<String> gameNames = new HashSet<>();
        List<GameInfo> result = new ArrayList<>();
        for (GameInfo info : gameInfoList) {
            if (!gameNames.contains(info.gameName)) {
                result.add(info);
                gameNames.add(info.gameName);
            }
        }
        adapter.setGameInfoList(result);
    }

}
