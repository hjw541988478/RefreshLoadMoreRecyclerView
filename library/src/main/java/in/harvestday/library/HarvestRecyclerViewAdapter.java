package in.harvestday.library;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * HarvestRecyclerView's abstract adapter
 */

public abstract class HarvestRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {


    // 各种视图类型
    public static final int TYPE_HEADER = 0x100;
    public static final int TYPE_LOADMORE_FOOTER = 0x101;
    public static final int TYPE_END_FOOTER = 0x103;
    public static final int TYPE_NORMAL = 0x102;
    private View headerView;
    private View loadMoreView;
    private View endFooterView;
    // 默认禁止下拉刷新
    private boolean isNeedLoadMore = false;

    /**
     * set footer voew
     *
     * @param loadMoreView
     * @param endFooterView
     */
    public void setFooterView(View loadMoreView, View endFooterView) {
        this.loadMoreView = loadMoreView;
        this.endFooterView = endFooterView;
    }

    /**
     * set header view
     *
     * @param headerView header view
     */
    public void setHeaderView(View headerView) {
        this.headerView = headerView;
    }

    public void setIsNeedLoadMore(boolean isLoadMore) {
        this.isNeedLoadMore = isLoadMore;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getAdapterItemCount() && hasLoadMoreFooterView() && isNeedLoadMore) {
            return TYPE_LOADMORE_FOOTER;
        } else if (position == getAdapterItemCount() && hasEndFooterView() && !isNeedLoadMore) {
            return TYPE_END_FOOTER;
        } else if (position == 0 && hasHeaderView()) {
            return TYPE_HEADER;
        } else
            return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return getAdapterItemCount() + (hasHeaderView() ? 1 : 0) + (isNeedLoadMore ? hasLoadMoreFooterView() ? 1 : 0 : hasEndFooterView() ? 1 : 0);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOADMORE_FOOTER) {
            return (VH) new RecyclerView.ViewHolder(loadMoreView) {

            };

        } else if (viewType == TYPE_END_FOOTER) {
            return (VH) new RecyclerView.ViewHolder(endFooterView) {

            };
        } else if (viewType == TYPE_HEADER) {
            return (VH) new RecyclerView.ViewHolder(headerView) {

            };
        } else {

            return onCreateViewHolder(parent);
        }
    }


    public abstract VH onCreateViewHolder(ViewGroup parent);

    public abstract int getAdapterItemCount();

    public boolean hasHeaderView() {
        return headerView != null;
    }

    public boolean hasEndFooterView() {
        return endFooterView != null;
    }

    public boolean hasLoadMoreFooterView() {
        return loadMoreView != null;
    }

}
