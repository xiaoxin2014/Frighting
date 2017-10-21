package my.tlol.com.frighting.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import my.tlol.com.frighting.bean.HePage;
import my.tlol.com.frighting.bean.NewCar;
import my.tlol.com.frighting.viewholder.NewCarItemHolder;


public class NewCarAdapter extends RecyclerArrayAdapter<NewCar.CarMainVoEntity> {

    private DisplayImageOptions options;
    public NewCarAdapter(Context context, DisplayImageOptions options) {
        super(context);
        this.options=options;
    }
    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {

        return new NewCarItemHolder(parent,options);
    }

    /*@Override
    public void OnBindViewHolder(BaseViewHolder holder, int position) {
        super.OnBindViewHolder(holder, position);
        holder.setData(datas);
    }*/
}
