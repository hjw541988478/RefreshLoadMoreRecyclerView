package in.harvestday.refreshloadmoredemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.harvestday.library.HarvestRecyclerViewAdapter;

/**
 * Created by Administrator on 2015/8/28.
 */
public class TestAdapter extends HarvestRecyclerViewAdapter {

    public List<String> data = new ArrayList<>();

    @Override
    public TestHarvHolder onCreateViewHolder(ViewGroup parent) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_card, parent, false);
        return new TestHarvHolder(root);
    }

    @Override
    public int getAdapterItemCount() {
        return data.size();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TestHarvHolder) {
            ((TestHarvHolder) holder).mContentText.setText(data.get(position));
        }
    }


    public class TestHarvHolder extends RecyclerView.ViewHolder {

        TextView mContentText;

        public TestHarvHolder(View itemView) {
            super(itemView);
            mContentText = (TextView) itemView.findViewById(R.id.id_text);
        }
    }
}
