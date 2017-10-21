package my.tlol.com.frighting.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import my.tlol.com.frighting.activity.MoneyHisVos;
import my.tlol.com.frighting.bean.MessageItem;
import my.tlol.com.frighting.viewholder.WalletListViewHolder;


public class WalletListAdapter extends RecyclerArrayAdapter<MoneyHisVos.MoneyHisVosEntity> {
    public WalletListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new WalletListViewHolder(parent);
    }

}
