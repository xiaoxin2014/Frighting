package my.tlol.com.frighting.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import my.tlol.com.frighting.R;

/**
 * Created by tlol20 on 2017/6/22
 */
public class SystemActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);
        findViewById(R.id.out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webView =(WebView)findViewById(R.id.wv_page);

        String h5=getIntent().getStringExtra("h5");
        if (TextUtils.isEmpty(h5)) {

        String uri= "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title></title></head><body><h1> 未来零售体验的大趋势</h1><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 如何让现有的零售空间始终成为热门的目的地？近期，CallisonRTKL发布白皮书，其中提到的关于未来中国零售体验的五大趋势值得零售商们关注。<br /><br />体验至上<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 一旦消费者的个人需求得到满足，他们更加愿意购买良好的消费体验而不单是商品。CBRE在亚太地区的11个主要国家中对11000名消费者进行调查发现，消费者在购物商场中最为注重的三个要素是价格、清洁和安全。<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 购物中心美食体验仍然是吸引顾客的主要因素。同时，大型旗舰店仍获青睐，但对何为成功的商店却无法定义。<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 灵活性是商场获得生命力的关键。无论如何，灵活的空间应当是设计中的一部分，以保证购物中心作为全天性&ldquo;娱乐+零售&rdquo;的场所。<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 消费者不仅是商场发展的动力，同时也是开发商产生大胆想法的源泉。动物园、冰雪嘉年华、天空堡垒、攀岩运动、异乎寻常的主题以及全面虚拟现实体验都已被开发商列入提高核心市场竞争力的秘籍中。<br /><br />实体店遇上数字化<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 目前，中国的购物中心已经充满庞大且精准的数字化媒体，室内Wi-Fi已经成为必备服务。<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 具备订购商品触摸屏幕因为数字化与实体消费的结合能激发消费者的好奇心。而虚拟试衣间通过个性化的&ldquo;内容&rdquo;展现使得消费者可以通过社交媒体分享给他们的朋友。<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 品牌与商店融合，通过在线选择样式，并反馈到实体试衣间，既省时，又创造出一种新的购物体验。<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 拿餐厅来说，餐厅应用程序进行在线排队，并为顾客提醒限时折扣和优惠活动。<br /><br />大数据遇上顾客<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 尽管一些年纪偏大的消费者缺乏提供信息动力，但新生代消费者则期待高水准的定制化服务，他们将个人信息视为这种消费体验的交换手段。<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 通过近场服务，蓝牙&ldquo;信号灯&rdquo;向手机推送促销信息和相关体验活动，这对从餐饮、时尚到购物中心本身的所有零售行业都适用。无处无时不在的微信支付同样也提供了便利。<br /><br />不仅是便利，而是超便利<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 快时代快速化，顾客在买单的同时更加追求便利和速度。<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 拿网上订单来说，大部分消费者都希望能够当日配送。而由于商场停车位困难，许多消费者会有疑虑。开发商会开发为顾客提前预订车位的应用程序。便利永远是重要的，人们总是倾向于去那些简单便捷的商业综合体。<br /><br />通过设计彰显品牌主张<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 产品固然重要，设计依然不可或缺。好的商业综合体还必须拥有引人注目的外观和标志性的建筑。因此，不论是室内还是室外，顶级开发商都在寻求大胆设计。外观上，要与地理位置和周围社区契合。室内也要融入灵活性，以满足日新月异的购物偏好。<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 同时，灵活性还是打造综合商业项目的基础。能够灵活地根据市场变化而重新整合功能的开发项目会拥有更大的竞争优势。 &nbsp;</p></body></html>";

        webView.loadDataWithBaseURL("about:blank",uri, "text/html", "utf-8", null);
        }else {
            WebSettings wSet = webView.getSettings();
            wSet.setJavaScriptEnabled(true);
            webView.loadUrl(h5);
        }


    }
}
