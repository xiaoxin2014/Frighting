package my.tlol.com.frighting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import my.tlol.com.frighting.R;
import my.tlol.com.frighting.adapter.FragmentAdapter;
import my.tlol.com.frighting.fragment.BaseHomeFragment;
import my.tlol.com.frighting.fragment.HongbaoFragment;
import my.tlol.com.frighting.fragment.JiangliFragment;
import my.tlol.com.frighting.fragment.WalletFragment;


/**
 * Created by tlol20 on 2017/6/14
 */
public class MyWalletActivity extends AppCompatActivity {
    private String[] mTabTitles = new String[]{"待存钱包","奖励红包","分享红包"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        findViewById(R.id.out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        BaseHomeFragment[] fragments={new WalletFragment(),
                new JiangliFragment(),new HongbaoFragment()};

        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragments, mTabTitles));
        viewPager.setOffscreenPageLimit(0);
        tabLayout.setupWithViewPager(viewPager);
    }

}
