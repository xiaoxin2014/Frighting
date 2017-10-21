package my.tlol.com.frighting.viewholder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import my.tlol.com.frighting.R;
import my.tlol.com.frighting.activity.MoneyHisVos;
import my.tlol.com.frighting.bean.MessageItem;


/**
 * Created by Mr.Jude on 2015/2/22
 */
public class WalletListViewHolder extends BaseViewHolder<MoneyHisVos.MoneyHisVosEntity> {
    private TextView money;
    private TextView time;

    public WalletListViewHolder(ViewGroup parent) {
        super(parent, R.layout.wallet_list_item);
        money = $(R.id.money);
        time = $(R.id.time);
    }

    @Override
    public void setData(final MoneyHisVos.MoneyHisVosEntity person){
        money.setText(person.getMoney()+".00");
        time.setText(person.getSendDate().substring(0,10));
        //name.setText(person.getName());
    }
}
