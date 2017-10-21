package my.tlol.com.frighting.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import my.tlol.com.frighting.bean.Mess;
import my.tlol.com.frighting.bean.TaoCan;
import my.tlol.com.frighting.viewholder.IMListViewHolder;
import my.tlol.com.frighting.viewholder.TaoCanListViewHolder;


public class IMListAdapter extends RecyclerArrayAdapter<Mess.CustomerVoEntity> {
    public IMListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new IMListViewHolder(parent);
    }

}
