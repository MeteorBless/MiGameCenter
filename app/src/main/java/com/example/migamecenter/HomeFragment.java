package com.example.migamecenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

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

import java.util.List;

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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 下拉刷新
                currentPage = 1;
                fetchData();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if ((!recyclerView.canScrollVertically(1)&&!isLoading)){
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

        editText = view.findViewById(R.id.edittext1);
        editText.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.drawable_padding));
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

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

        // 显示主页
        fetchData();
        return view;
    }

    private void fetchData() {
        isLoading = true;
        HttpManager.getInstance().searchHomePage(currentPage, PAGE_SIZE, new NetCallBack<BaseGameBean<Page<HomePageInfo>>>() {
            @Override
            public void onSuccess(BaseGameBean<Page<HomePageInfo>> data) {
                requireActivity().runOnUiThread(() -> {
                    List<HomePageInfo> homePageInfoList = data.data.records;

                    if (!homePageInfoList.isEmpty()&&currentPage==1){
                        adapter.setHomePageInfoList(homePageInfoList);
                    }else if(currentPage!=1){
                        adapter.addHomePageInfoList(homePageInfoList);
                    }
//                    Log.i("HomeFragment","数据records大小为"+data.data.records.size());
//                    Log.i("HomeFragment","数据gameInfoList0大小为"+data.data.records.get(0).gameInfoList.size());
//                    Log.i("HomeFragment","数据gameInfoList0大小style为"+data.data.records.get(0).style);
//                    Log.i("HomeFragment","数据gameInfoList1大小为"+data.data.records.get(1).gameInfoList.size());
//                    Log.i("HomeFragment","数据gameInfoList1大小style为"+data.data.records.get(1).style);

                    isLoading = false;
                    swipeRefreshLayout.setRefreshing(false);
                    currentPage++;
                });
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {
                Log.e("HomeFragment", "Network request failed: " + errorMsg);
                isLoading = false;
            }
        });
    }

    private void loadMoreData() {
        isLoading = true;
        HttpManager.getInstance().searchHomePage(currentPage, PAGE_SIZE, new NetCallBack<BaseGameBean<Page<HomePageInfo>>>() {
            @Override
            public void onSuccess(BaseGameBean<Page<HomePageInfo>> data) {
                requireActivity().runOnUiThread(() -> {
                    List<HomePageInfo> homePageInfoList = data.data.records;
                    Log.i("HomeFragment","数据为"+data.data.records.get(0).gameInfoList.toArray().length);
                    isLoading = false;
                    currentPage++;
                });
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {
                Log.e("HomeFragment", "Network request failed: " + errorMsg);
                isLoading = false;
            }
        });
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

}
