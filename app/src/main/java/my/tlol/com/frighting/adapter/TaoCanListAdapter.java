package my.tlol.com.frighting.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import my.tlol.com.frighting.bean.MessageItem;
import my.tlol.com.frighting.bean.TaoCan;
import my.tlol.com.frighting.viewholder.TaoCanListViewHolder;
import my.tlol.com.frighting.viewholder.WalletListViewHolder;


public class TaoCanListAdapter extends RecyclerArrayAdapter<TaoCan.ActivityEntity> {
    public TaoCanListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new TaoCanListViewHolder(parent);
    }

}
