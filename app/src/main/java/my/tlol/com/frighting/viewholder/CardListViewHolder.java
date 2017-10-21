package my.tlol.com.frighting.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.Map;

import my.tlol.com.frighting.Contants;
import my.tlol.com.frighting.R;
import my.tlol.com.frighting.bean.CardList;
import my.tlol.com.frighting.bean.Msg;
import my.tlol.com.frighting.bean.TaoCan;
import my.tlol.com.frighting.http.OkHttpHelper;
import my.tlol.com.frighting.http.SpotsCallBack;
import my.tlol.com.frighting.listener.MyButtenClickListener;


/**
 * Created by Mr.Jude on 2015/2/22
 */
public class CardListViewHolder extends BaseViewHolder<CardList.GasCardVoEntity> {
    private TextView name;
    public MyButtenClickListener mItemClickListener;
    private TextView number;
    private TextView title;
    ImageView delete,logo;
    LinearLayout ll;
    public CardListViewHolder(ViewGroup parent, MyButtenClickListener mItemClickListener) {
        super(parent, R.layout.item_card);
        this.mItemClickListener=mItemClickListener;
        name = $(R.id.name);
        number = $(R.id.number);
        title = $(R.id.title);
        delete = $(R.id.delete);
        logo = $(R.id.logo);
        ll = $(R.id.ll);
    }

    @Override
    public void setData(final CardList.GasCardVoEntity person){

        number.setText(person.getCardNum()+"");
        if (person.getFlag().equals("zgsy")) {
            ll.setBackgroundResource(R.mipmap.zgsy_bg);
            logo.setBackgroundResource(R.mipmap.card_zgsy_logo);
            title.setText("中国石油");
        }else {
            ll.setBackgroundResource(R.mipmap.zgsh_bg);
            logo.setBackgroundResource(R.mipmap.card_zgsh_logo);
            title.setText("中国石化");
        }
        name.setText(person.getUserName());
        final int position=getAdapterPosition();
        // 如果设置了回调，则设置点击事件
        if (mItemClickListener != null)
        {
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onButtenClick(delete, position);
                }
            });
        }
    }



}
