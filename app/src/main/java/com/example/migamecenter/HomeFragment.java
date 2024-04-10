package com.example.migamecenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.migamecenter.adapter.HomePageAdapter;
import com.example.migamecenter.bean.BaseGameBean;
import com.example.migamecenter.bean.HomePageInfo;
import com.example.migamecenter.bean.Page;
import com.example.migamecenter.httpUtils.HttpManager;
import com.example.migamecenter.httpUtils.NetCallBack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeFragment extends Fragment {
    private EditText editText;

    private RecyclerView recyclerView;

    private HomePageAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private int currentPage = 1;

    private int PAGE_SIZE = 2;

    private  boolean isLoading = false;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 找到控件
        editText = view.findViewById(R.id.edittext1);
        recyclerView = view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        adapter = new HomePageAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        editText = view.findViewById(R.id.edittext1);
        editText.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.drawable_padding));
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);


        bindListener();
        // 显示主页
        fetchData();
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void bindListener(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requireActivity().runOnUiThread(new Runnable() {
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
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                assert layoutManager != null;
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                Log.i("layoutManager.itemCount","条目总数量为："+totalItemCount);
                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    loadMoreData();
                }
            }
        });

        // 设置触摸事件监听器
        editText.setOnTouchListener((v, event) -> {
            // 检查触摸事件是否在右侧图标上
            if (event.getAction() == MotionEvent.ACTION_UP &&
                    event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[2].getBounds().width())) {
                // 在这里执行页面跳转的逻辑
                startActivity(new Intent(requireContext(), SearchActivity.class));
                return true;
            }
            return false;
        });

        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                String keywords = editText.getText().toString().trim();
                Intent intent = new Intent(requireContext(), SearchActivity.class);
//                String hint_ = editText.getHint().toString().trim();
//                intent.putExtra("keywords", keywords);
//                intent.putExtra("hint",hint_);
                requireContext().startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private void fetchData() {
        currentPage = 1;

        HttpManager.getInstance().searchHomePage(currentPage, PAGE_SIZE, new NetCallBack<BaseGameBean<Page<HomePageInfo>>>() {
            @Override
            public void onSuccess(BaseGameBean<Page<HomePageInfo>> data) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (data.data != null) {
                            // 缓存第一页数据到本地
                            cacheFirstPageData(data.data.records);

                            adapter.setHomePageInfoList(data.data.records);
                        } else {
                            Toast.makeText(requireActivity(), "数据为空", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {
                // 在网络请求失败时，尝试从本地缓存加载数据
                List<HomePageInfo> cachedData = loadCachedData();
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (cachedData != null) {
                            // 显示本地缓存数据
                            adapter.setHomePageInfoList(cachedData);
                        } else {
                            // 显示网络请求失败的提示信息
                            Toast.makeText(requireActivity(), "网络请求失败" + errorMsg, Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }


    private void loadMoreData() {
        if (!isLoading && isAdded()) { // 检查 Fragment 是否已经与 Activity 关联
            isLoading = true;

            HttpManager.getInstance().searchHomePage(currentPage+1, PAGE_SIZE, new NetCallBack<BaseGameBean<Page<HomePageInfo>>>() {
                @Override
                public void onSuccess(BaseGameBean<Page<HomePageInfo>> data) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void run() {
                            isLoading = false;
                            if (isAdded()) { // 再次检查 Fragment 是否已经与 Activity 关联
                                if (data.data != null && !data.data.records.isEmpty()) {
                                    currentPage++;
                                    List<HomePageInfo> newHomePageInfoList = data.data.records;

                                    // 添加新数据到适配器
                                    adapter.addHomePageInfoList(newHomePageInfoList);

                                    // 去重处理
//                                    removeDuplicate();

                                    // 刷新列表
                                    adapter.notifyDataSetChanged();
                                } else {
//                                    Toast.makeText(requireActivity(), "已加载完所有搜索结果", Toast.LENGTH_SHORT).show();
                                    Log.i("HomeFragment","data.data.records已经为空");
                                }
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
                            if (isAdded()) { // 再次检查 Fragment 是否已经与 Activity 关联
                                Toast.makeText(requireActivity(), "网络请求失败: " + errMsg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 取消触摸事件监听器
        editText.setOnTouchListener(null);
        // 取消编辑动作监听器
        editText.setOnEditorActionListener(null);

    }


//    private void removeDuplicate() {
//        List<HomePageInfo> homePageInfoList = adapter.getCurrentHomePageList();
//
//        Set<String> uniqueKeys = new HashSet<>();
//        List<HomePageInfo> resultList = new ArrayList<>();
//
//        for (HomePageInfo info : homePageInfoList) {
//            // 拼接主页信息对象的关键字段，以便进行去重判断
//            String key = info.moduleCode + info.moduleName + info.style; // 根据实际情况拼接关键字段
//            if(info.style==3){
//                Log.i("HomeFragment","style=3时的大小"+info.gameInfoList.size());
//            }
//            if(uniqueKeys.add(key)){
//                // 如果当前信息对象的关键字段组合未曾出现过，则添加到结果列表中
//                resultList.add(info);
//            }
//
//        }
//        adapter.setHomePageInfoList(resultList);
//    }

    // 缓存第一页数据到本地
    private void cacheFirstPageData(List<HomePageInfo> data) {
    // 使用SharedPreferences缓存数据
    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("HomePageCache", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    Gson gson = new Gson();
    String jsonData = gson.toJson(data);
    editor.putString("firstPageData", jsonData);
    editor.apply();
}

    // 从本地加载缓存数据
    private List<HomePageInfo> loadCachedData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("HomePageCache", Context.MODE_PRIVATE);
        String jsonData = sharedPreferences.getString("firstPageData", null);
        if (jsonData != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<HomePageInfo>>() {}.getType();
            return gson.fromJson(jsonData, listType);
        } else {
            return null;
        }
    }

}
