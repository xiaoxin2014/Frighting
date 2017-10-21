package my.tlol.com.frighting.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import my.tlol.com.frighting.bean.Message;
import my.tlol.com.frighting.viewholder.MessageListViewHolder;


public class FansListAdapter extends RecyclerArrayAdapter<Message.DayMesVoEntity> {
    public FansListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageListViewHolder(parent);
    }

}
