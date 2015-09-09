package in.harvestday.library;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.vlonjatg.progressactivity.ProgressActivity;

/**
 * Created by hjw on 2015/8/27.
 */
public class HarvestRecyclerView extends FrameLayout {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressActivity progressLayout;
    LinearLayoutManager manager;
    private int mLastVisibleItemPosition;
    private Drawable emptyDrawable;
    private String emptyText;
    private OnClickListener emptyClickListener;

    private HarvestRecyclerViewAdapter adapter;

    private RecyclerView.OnScrollListener mOnLoadMoreListener;
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener;

    public HarvestRecyclerView(Context context) {
        this(context, null);
    }

    public HarvestRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    /**
     * refresh and loadmore are disabled by default
     */
    private void initViews() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.base_swipe_refresh_layout, this);
        progressLayout = (ProgressActivity) rootView.findViewById(R.id.base_progress_layout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.base_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.base_swipe_layout);
        disableRefresh();
        disableLoadMore();
    }


    /**
     * enable refresh
     *
     * @param onRefreshListener refresh callback
     */
    private void enableRefresh(final OnRefreshListener onRefreshListener) {
        swipeRefreshLayout.setEnabled(true);
        mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (onRefreshListener != null)
                    onRefreshListener.onRefresh();
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
            }
        };
        swipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
    }

    /**
     * disable refresh
     */
    private void disableRefresh() {
        swipeRefreshLayout.setEnabled(false);
        if (manager != null) {
            if (adapter != null && adapter.getItemCount() != 0)
                manager.scrollToPosition(0);
        }
    }

    /**
     * enable load more
     *
     * @param onLoadMoreListener
     */
    private void enableLoadMore(final OnLoadMoreListener onLoadMoreListener) {
        if (mOnLoadMoreListener != null)
            recyclerView.removeOnScrollListener(mOnLoadMoreListener);
        if (adapter != null)
            adapter.setIsNeedLoadMore(true);
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        mOnLoadMoreListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    if (swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            }
        };

        recyclerView.addOnScrollListener(mOnLoadMoreListener);
    }

    /**
     * disable loadmore
     */
    private void disableLoadMore() {
        if (adapter != null) {
            adapter.setIsNeedLoadMore(false);
            adapter.setFooterView(null, null);
        }
        if (mOnLoadMoreListener != null)
            recyclerView.removeOnScrollListener(mOnLoadMoreListener);
    }

    public void setHasFixedSize(boolean hasFixedSize) {
        recyclerView.setHasFixedSize(hasFixedSize);
    }

    public void setLayoutManager(LinearLayoutManager manager) {
        if (manager != null) {
            this.manager = manager;
            recyclerView.setLayoutManager(manager);
        }
    }

    public void setAdapter(HarvestRecyclerViewAdapter adapter) {
        if (this.adapter != null)
            this.adapter = null;
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkIfEmpty();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                checkIfEmpty();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkIfEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkIfEmpty();
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                checkIfEmpty();
            }

        });
        checkIfEmpty();
    }

    /**
     * show empty view if dataset is empty
     */
    private void checkIfEmpty() {
        if (adapter.getAdapterItemCount() == 0) {
            showExceptionViewWithListener(emptyDrawable, emptyText, "", "retry", emptyClickListener);
        } else {
            showContent();
        }
    }

    /**
     * show contentview
     */
    public void showContent() {
        progressLayout.showContent();
    }

    /**
     * show Loadingview
     */
    public void showLoading() {
        progressLayout.showLoading();
    }

    /**
     * show exception view with listener
     *
     * @param exceptionDrawable      exception res
     * @param exceptionTitle         exception title
     * @param exceptionText          exception description
     * @param exceptionButtonTitle   exception retry button
     * @param exceptionClickListener exception button listener
     */
    public void showExceptionViewWithListener(Drawable exceptionDrawable,
                                              String exceptionTitle, String exceptionText,
                                              String exceptionButtonTitle, OnClickListener exceptionClickListener) {
        progressLayout.showError(exceptionDrawable, exceptionTitle, exceptionText, exceptionButtonTitle, exceptionClickListener);

    }

    public void showExceptionViewWithoutListener(Drawable exceptionDrawable,
                                                 String exceptionTitle, String exceptionText) {
        progressLayout.showEmpty(exceptionDrawable, exceptionTitle, exceptionText);
    }

    /**
     * enable or disable refresh
     *
     * @param flag
     */
    public void setRefreshEnabled(boolean flag, OnRefreshListener mRefreshListener) {
        if (flag)
            enableRefresh(mRefreshListener);
        else
            disableRefresh();
    }

    /**
     * enable or disable loadmore
     *
     * @param flag
     */
    public void setLoadMoreEnabled(boolean flag, OnLoadMoreListener mLoadMoreListener) {
        if (flag)
            enableLoadMore(mLoadMoreListener);
        else
            disableLoadMore();
    }

    /**
     * optional setting
     *
     * @param itemDecoration
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        if (itemDecoration != null)
            recyclerView.addItemDecoration(itemDecoration);
    }

    /**
     * optional setting
     *
     * @param colors
     */
    public void setRefreshColorScheme(int... colors) {
        swipeRefreshLayout.setColorSchemeColors(colors);
    }

    /**
     * optional setting
     *
     * @param header
     */
    public void setCustomHeader(View header) {
        if (adapter != null)
            adapter.setHeaderView(header);
    }

    public void setCustomFooter(View loadMoreFooter, View endFooter) {
        if (adapter != null)
            adapter.setFooterView(loadMoreFooter, endFooter);
    }

    public void setEmptyView(Drawable emptyDrawable, String emptyText, OnClickListener emptyClickListener) {
        this.emptyDrawable = emptyDrawable;
        this.emptyText = emptyText;
        this.emptyClickListener = emptyClickListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

}
