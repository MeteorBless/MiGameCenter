package com.example.migamecenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.migamecenter.adapter.GameAdapter;
import com.example.migamecenter.bean.BaseGameBean;
import com.example.migamecenter.bean.GameInfo;
import com.example.migamecenter.bean.GameInfoPage;
import com.example.migamecenter.httpUtils.HttpManager;
import com.example.migamecenter.httpUtils.NetCallBack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ResultIsNotNullFragment extends Fragment implements GameAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private GameAdapter adapter;
    private List<GameInfo> gameInfoList;

    private boolean isLoading = false;
    private int currentPage = 1;
    private String searchKeywords = "";

    public static ResultIsNotNullFragment newInstance(List<GameInfo> gameInfoList, String searchKeywords) {
        ResultIsNotNullFragment fragment = new ResultIsNotNullFragment();
        Bundle args = new Bundle();
        args.putSerializable("gameInfoList", new ArrayList<>(gameInfoList));
        args.putString("searchKeywords", searchKeywords);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameInfoList = (List<GameInfo>) getArguments().getSerializable("gameInfoList");
        searchKeywords = getArguments().getString("searchKeywords");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result_is_not_null, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view_search_result);
        adapter = new GameAdapter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setGameInfoList(gameInfoList);

        // 加载更多数据的逻辑
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    loadMoreData();
                }
            }
        });

        return rootView;
    }

    private void loadMoreData() {
        isLoading = true;
//        HttpManager manager = new HttpManager();
        HttpManager.getInstance().searchGame(searchKeywords, currentPage + 1, 10, new NetCallBack<BaseGameBean<GameInfoPage>>() {
            @Override
            public void onSuccess(BaseGameBean<GameInfoPage> data) {
                requireActivity().runOnUiThread(new Runnable() {
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
                            Toast.makeText(requireActivity(), "已加载完所有搜索结果", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(int code, String errMsg) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isLoading = false;
                        Toast.makeText(requireActivity(), "网络请求失败: " + errMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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

    @Override
    public void onItemClick(GameInfo gameInfo) {
        Intent intent = new Intent(requireContext(), DetailActivity.class);
        intent.putExtra("gameInfo", gameInfo);
        requireContext().startActivity(intent);
    }

}

