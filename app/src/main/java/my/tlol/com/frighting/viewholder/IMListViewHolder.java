package my.tlol.com.frighting.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;
import my.tlol.com.frighting.R;
import my.tlol.com.frighting.application.MyApplication;
import my.tlol.com.frighting.bean.Mess;
import my.tlol.com.frighting.bean.TaoCan;


/**
 * Created by Mr.Jude on 2015/2/22
 */
public class IMListViewHolder extends BaseViewHolder<Mess.CustomerVoEntity> {
    private TextView right_message;
    private TextView left_message;
    private CircleImageView left_icon;
    private CircleImageView right_icon;
    public IMListViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_im);
        left_message = $(R.id.left_message);
        right_message = $(R.id.right_message);
        left_icon = $(R.id.left_icon);
        right_icon = $(R.id.right_icon);
    }

    @Override
    public void setData(final Mess.CustomerVoEntity person){
        if (person.getStatus().equals("Y")) {
            left_icon.setVisibility(View.GONE);
            left_message.setVisibility(View.GONE);
            right_icon.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(MyApplication.getInstance().getUser().getUserVoLog().getPhotoUrl(),right_icon);
            right_message.setVisibility(View.VISIBLE);
            right_message.setText(person.getContent());
        }else {
            right_icon.setVisibility(View.GONE);
            right_message.setVisibility(View.GONE);
            left_icon.setVisibility(View.VISIBLE);
            left_message.setVisibility(View.VISIBLE);
            left_message.setText(person.getContent());
        }
        //name.setText(person.getName());
    }
}
