package my.tlol.com.frighting.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.squareup.okhttp.Response;

import java.util.List;

import my.tlol.com.frighting.Contants;
import my.tlol.com.frighting.R;
import my.tlol.com.frighting.activity.MoneyHisVos;
import my.tlol.com.frighting.adapter.WalletListAdapter;
import my.tlol.com.frighting.bean.Msg;
import my.tlol.com.frighting.http.OkHttpHelper;
import my.tlol.com.frighting.http.SimpleCallback;


public class JiangliFragment extends BaseHomeFragment{


    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    private EasyRecyclerView recyclerView;
    private WalletListAdapter adapter;
    //private List<MessageItem> list=new ArrayList<>();


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view=inflater.inflate(R.layout.fragment_jiangli,container,false);
        recyclerView= (EasyRecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new WalletListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.showEmpty();
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
        getData();


        /*adapter.setMore(R.layout.view_more, new RecyclerArrayAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });
        adapter.setNoMore(R.layout.view_nomore, new RecyclerArrayAdapter.OnNoMoreListener() {
            @Override
            public void onNoMoreShow() {
                adapter.resumeMore();
            }

            @Override
            public void onNoMoreClick() {
                recyclerView.scrollToPosition(0);
            }
        });
        adapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                adapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                adapter.resumeMore();
                //onLoadMore();
            }
        });*/

        return view;
    }
    List<MoneyHisVos.MoneyHisVosEntity> list;
    private void getData() {
        okHttpHelper.get(Contants.API.RED_LIST, new SimpleCallback<MoneyHisVos>(getActivity()) {
            @Override
            public void onSuccess(Response response, MoneyHisVos o) {
                if (o.getMsg().getCode()==0){
                    list=o.getMoneyHisVos();
                    adapter.addAll(list);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void initData() {
        /*List<MessageItem> listItem=new ArrayList<>();
         listItem.add(new MessageItem("","","",""));
         listItem.add(new MessageItem("","","",""));
         listItem.add(new MessageItem("","","",""));
             adapter.addAll(listItem);*/
    }


}
