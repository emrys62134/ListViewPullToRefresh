package com.pei.listviewpulltorefresh;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.pei.listviewpulltorefresh.knowledge.KnowledgeAdapter;
import com.pei.listviewpulltorefresh.knowledge.KnowledgeBean;
import com.pei.listviewpulltorefresh.volleysingleton.NetListener;
import com.pei.listviewpulltorefresh.volleysingleton.VolleySingleton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnRefreshListener, OnLoadMoreListener {

    private SwipeToLoadLayout swipeToLoadLayout;
    private ListView lv;

    private String url = "http://food.boohee.com/fb/v1/feeds/category_feed?page=1&category=3&per=10";
    private String headerUrl = "http://food.boohee.com/fb/v1/feeds/category_feed?page=";
    private String footerUrl = "&category=3&per=10";
    private KnowledgeAdapter adapter;
    private String newUrl;
    private KnowledgeBean data;
    private int i = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipe_to_load_layout);
        swipeToLoadLayout.setOnRefreshListener(this); // 刷新监听
        swipeToLoadLayout.setOnLoadMoreListener(this); // 加载监听
        lv = (ListView) findViewById(R.id.swipe_target);

        adapter = new KnowledgeAdapter(this);
        lv.setAdapter(adapter);

        VolleySingleton.MyRequest(url, KnowledgeBean.class, new NetListener<KnowledgeBean>() {
            @Override
            public void successListener(final KnowledgeBean response) {
                adapter.setBean(response);
                data = response; // 解析后的数据就是这个类的内容

            }

            @Override
            public void errorListener(VolleyError error) {

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                if (data != null) {
                    intent.putExtra("data", data.getFeeds().get(i).getLink());
                }
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRefresh() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(false);
                getRefreshData();
            }
        }, 2000);
    }

    private void getRefreshData() {
        VolleySingleton.MyRequest(url, KnowledgeBean.class, new NetListener<KnowledgeBean>() {
            @Override
            public void successListener(KnowledgeBean response) {
                adapter.addMore(response);
            }

            @Override
            public void errorListener(VolleyError error) {

            }
        });
    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
                newUrl = headerUrl + i + footerUrl;
                getLoadData();
                i++;
            }
        }, 2000);
    }

    private void getLoadData() {
        VolleySingleton.MyRequest(newUrl, KnowledgeBean.class, new NetListener<KnowledgeBean>() {
            @Override
            public void successListener(final KnowledgeBean response) {
                adapter.addMore(response);

                data.getFeeds().addAll(response.getFeeds()); // 将网址拼接获取的内容添加到原有内容之中


            }

            @Override
            public void errorListener(VolleyError error) {

            }
        });
    }
}
