package my.tlol.com.frighting.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;
import my.tlol.com.frighting.R;
import my.tlol.com.frighting.application.MyApplication;
import my.tlol.com.frighting.bean.Mess;
import my.tlol.com.frighting.bean.Message;


/**
 * Created by Mr.Jude on 2015/2/22
 */
public class MessageListViewHolder extends BaseViewHolder<Message.DayMesVoEntity> {
    private TextView left_message;
    public MessageListViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_meg);
        left_message = $(R.id.tv);

    }

    @Override
    public void setData(final Message.DayMesVoEntity person){
        left_message.setText(person.getDaysMessage());
        //name.setText(person.getName());
    }
}
