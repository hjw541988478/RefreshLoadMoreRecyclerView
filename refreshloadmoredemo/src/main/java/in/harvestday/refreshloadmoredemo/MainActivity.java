package in.harvestday.refreshloadmoredemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import in.harvestday.library.HarvestRecyclerView;

public class MainActivity extends AppCompatActivity {

    Toolbar mToolBar;
    HarvestRecyclerView mRecyclerView;
    TestAdapter adapter;
    LinearLayoutManager manager;
    HarvestRecyclerView.OnRefreshListener onRefreshListener;
    HarvestRecyclerView.OnLoadMoreListener onLoadMoreListener;
    int refreshNum = 0;
    int loadMoreNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        // 再进行网络请求的时候，可设置Loading视图
        mRecyclerView.showLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bindData();
            }
        }, 1500);
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        mToolBar = (Toolbar) findViewById(R.id.id_toolbar);
        if (mToolBar != null)
            setSupportActionBar(mToolBar);
        mRecyclerView = (HarvestRecyclerView) findViewById(R.id.id_harvest_recyclerview);
        manager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setEmptyView(getResources().getDrawable(R.drawable.ic_results_empty), "No data Here.", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.data.add("onRefresh" + refreshNum++);
                adapter.notifyDataSetChanged();
            }
        });
        onRefreshListener = new HarvestRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (refreshNum >= 8) {
                    adapter.data.clear();
                } else
                    adapter.data.add(0, "onRefresh" + refreshNum++);
                adapter.notifyDataSetChanged();
            }
        };
        onLoadMoreListener = new HarvestRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (loadMoreNum >= 8) {
                    mRecyclerView.setLoadMoreEnabled(false, null);
                } else
                    adapter.data.add("onLoadMore" + loadMoreNum++);
                adapter.notifyDataSetChanged();
            }
        };
    }

    /**
     * 初始化数据
     */
    private void bindData() {
        List<String> mListData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mListData.add("data" + i);
        }
        adapter = new TestAdapter();
        mRecyclerView.setCustomHeader(LayoutInflater.from(this).inflate(R.layout.header_text, null));
        adapter.data.clear();
        adapter.data.addAll(mListData);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_refresh:
                refreshOnly();
                break;
            case R.id.menu_load_more:
                loadMoreOnly();
                break;
            case R.id.menu_both_refresh_load_more:
                enableAll();
                break;
            case R.id.menu_common_recyclerview:
                disableAll();
                break;
        }
        adapter.notifyDataSetChanged();
        return true;
    }


    private void disableAll() {
        mRecyclerView.setRefreshEnabled(false, null);
        mRecyclerView.setLoadMoreEnabled(false, null);
    }

    private void loadMoreOnly() {
        mRecyclerView.setRefreshEnabled(false, null);
        mRecyclerView.setLoadMoreEnabled(true, onLoadMoreListener);
        LayoutInflater inflater = LayoutInflater.from(this);
        mRecyclerView.setCustomFooter(inflater.inflate(R.layout.footer_loadmore, null), inflater.inflate(R.layout.footer_loadend, null));
    }

    private void refreshOnly() {
        mRecyclerView.setRefreshEnabled(true, onRefreshListener);
        mRecyclerView.setLoadMoreEnabled(false, null);
    }

    private void enableAll() {
        mRecyclerView.setRefreshEnabled(true, onRefreshListener);
        mRecyclerView.setLoadMoreEnabled(true, onLoadMoreListener);
        LayoutInflater inflater = LayoutInflater.from(this);
        mRecyclerView.setCustomFooter(inflater.inflate(R.layout.footer_loadmore, null), inflater.inflate(R.layout.footer_loadend, null));
    }
}
