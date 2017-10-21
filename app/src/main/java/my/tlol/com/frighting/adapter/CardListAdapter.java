package my.tlol.com.frighting.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import my.tlol.com.frighting.bean.CardList;
import my.tlol.com.frighting.bean.TaoCan;
import my.tlol.com.frighting.listener.MyButtenClickListener;
import my.tlol.com.frighting.viewholder.CardListViewHolder;
import my.tlol.com.frighting.viewholder.TaoCanListViewHolder;


public class CardListAdapter extends RecyclerArrayAdapter<CardList.GasCardVoEntity> {
    public MyButtenClickListener mItemClickListener;
    public void setMyButtonClickListener(MyButtenClickListener listener){
        this.mItemClickListener = listener;
    }
    public CardListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardListViewHolder(parent,mItemClickListener);
    }

}
