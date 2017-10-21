package my.tlol.com.frighting.viewholder;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import my.tlol.com.frighting.R;
import my.tlol.com.frighting.bean.MessageItem;
import my.tlol.com.frighting.bean.TaoCan;


/**
 * Created by Mr.Jude on 2015/2/22
 */
public class TaoCanListViewHolder extends BaseViewHolder<TaoCan.ActivityEntity> {
    private TextView stage;
    private TextView payM;
    private TextView giveM;
    LinearLayout ll;
    public TaoCanListViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_taocan);
        stage = $(R.id.stage);
        payM = $(R.id.payM);
        giveM = $(R.id.giveM);
        ll = $(R.id.ll);
    }

    @Override
    public void setData(final TaoCan.ActivityEntity person){
        switch (getAdapterPosition() % 3) {
            case 0:{
                ll.setBackgroundResource(R.drawable.taocan01);
            }break;
            case 1:{
                ll.setBackgroundResource(R.drawable.taocan02);
            }break;
            case 2:{
                ll.setBackgroundResource(R.drawable.taocan03);
            }break;
        }
        stage.setText("使用期限"+person.getStage()+"年");
        payM.setText("送"+person.getPayM()+"元");
        giveM.setText("购￥"+person.getBuyM()+"套餐得"+person.getGiveM());
        //name.setText(person.getName());
    }
}
