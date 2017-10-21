package my.tlol.com.frighting.viewholder;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import my.tlol.com.frighting.R;
import my.tlol.com.frighting.bean.HePage;
import my.tlol.com.frighting.bean.NewCar;

/**
 * Created by tlol20 on 2017/6/5
 */
public class NewCarItemHolder extends BaseViewHolder<NewCar.CarMainVoEntity> {

    private TextView title;
    private TextView money;
    private TextView scan;
    private TextView data;
    private ImageView ivImage;
    DisplayImageOptions options;
    public NewCarItemHolder(ViewGroup parent, DisplayImageOptions options) {
        super(parent, R.layout.cardlayout);
        this.options=options;
        title = $(R.id.hot_message);
        money = $(R.id.money);
        ivImage = $(R.id.imge);
        data = $(R.id.data);
        scan = $(R.id.km);
    }

    @Override
    public void setData(final NewCar.CarMainVoEntity person) {
        ImageLoader.getInstance().displayImage(person.getCarImgUrl(),ivImage,options,null);
        title.setText(person.getTitle());
        money.setText(person.getBPrice()+"万");
        data.setText(person.getProductData().substring(0, 4) +"年");
        scan.setText(person.getParam().substring(0,person.getParam().indexOf("|")));
        /*int position=getAdapterPosition();
        int width = ((Activity) ivImage.getContext()).getWindowManager().getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams params = ivImage.getLayoutParams();
        //设置图片的相对于屏幕的宽高比
        params.width = width/2-10;

        if (position%2==1){
            params.height = 500 ;
        }else {
            params.height = 300 ;
        }
        ivImage.setLayoutParams(params);*/




    }


}
