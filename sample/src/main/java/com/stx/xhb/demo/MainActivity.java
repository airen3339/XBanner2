package com.stx.xhb.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.stx.xhb.demo.entity.TuchongEntity;
import com.stx.xhb.xbanner.XBanner;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private XBanner mBanner;
    List<TuchongEntity.FeedListBean.EntryBean> tempData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(this);
        initView();
        initBanner();
        initData();
    }

    private void initView() {
        mBanner = (XBanner) findViewById(R.id.banner);
        mBanner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ScreenUtil.getScreenWidth(this) / 2));
        ListView listView = (ListView) findViewById(R.id.lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.pages));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, ListViewActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, GuideActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, CustomViewsActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, ClipChildrenModeActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, RecyclerViewActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(MainActivity.this, UserInFragmentActivity.class));
                        break;
                    case 6:
                        startActivity(new Intent(MainActivity.this, VideoViewActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * ?????????XBanner
     */
    private void initBanner() {
        //??????????????????????????????
        mBanner.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner banner, Object model, View view, int position) {
                ToastUtils.showShort("????????????" + (position + 1) + "??????");
            }
        });
        //??????????????????
        mBanner.loadImage(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                //????????????????????????????????????????????????demo?????????glide?????????????????????????????????????????????????????????
                TuchongEntity.FeedListBean.EntryBean listBean = ((TuchongEntity.FeedListBean.EntryBean) model);
                String url = "https://photo.tuchong.com/" + listBean.getImages().get(0).getUser_id() + "/f/" + listBean.getImages().get(0).getImg_id() + ".jpg";
                Glide.with(MainActivity.this).load(url).placeholder(R.drawable.default_image).error(R.drawable.default_image).into((ImageView) view);
            }
        });
        //??????????????????????????????????????????????????????
        mBanner.setBannerPlaceholderImg(R.mipmap.xbanner_logo, ImageView.ScaleType.CENTER_CROP);
    }

    /**
     * ???????????????
     */
    private void initData() {
        //????????????????????????
        String url = "https://api.tuchong.com/2/wall-paper/app";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        Toast.makeText(MainActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        TuchongEntity advertiseEntity = new Gson().fromJson(response, TuchongEntity.class);
                        List<TuchongEntity.FeedListBean> others = advertiseEntity.getFeedList();
                        final List<TuchongEntity.FeedListBean.EntryBean> data = new ArrayList<>();
                        for (int i = 0; i < others.size(); i++) {
                            TuchongEntity.FeedListBean feedListBean = others.get(i);
                            if ("post".equals(feedListBean.getType())) {
                                data.add(feedListBean.getEntry());
                            }
                        }
                        //???????????????????????????????????????????????????????????????
                        mBanner.setAutoPlayAble(true);
                        mBanner.setBannerData(data);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        mBanner.setBannerCurrentItem(2);
    }
}
