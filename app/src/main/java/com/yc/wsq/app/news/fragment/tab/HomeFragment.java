package com.yc.wsq.app.news.fragment.tab;

import android.Manifest;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wsq.library.listener.OnRecyclerViewItemClickListener;
import com.wsq.library.tools.RecyclerViewDivider;
import com.wsq.library.tools.ToastUtils;
import com.wsq.library.utils.DensityUtil;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.activity.QRcodeScanActivity;
import com.yc.wsq.app.news.adapter.NewsAdapter;
import com.yc.wsq.app.news.adapter.TitleAdapter;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.presenter.NewsPresenter;
import com.yc.wsq.app.news.mvp.view.NewsView;
import com.yc.wsq.app.news.tools.SharedTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * 主页面
 */
public class HomeFragment extends BaseFragment<NewsView, NewsPresenter<NewsView>> implements NewsView{

    public static final String TAG =  HomeFragment.class.getName();
    public static final String INTERFACE_WITHP = TAG+ _INTERFACE_WITHP;
    public static final String INTERFACE_WITHPS = TAG+ _INTERFACE_WITHP+"_STRING";

    @BindView(R.id.rv_RecyclerView_title)
    RecyclerView rv_RecyclerView_title;
    @BindView(R.id.rv_RecyclerView_content) RecyclerView rv_RecyclerView_content;
    @BindView(R.id.refreshLayout) SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_not_data) TextView tv_not_data;
    @BindView(R.id.tv_location) TextView tv_location;

    private TitleAdapter mTitleAdapter;
    private List<Map<String, Object>> mTitleData;
    private NewsAdapter mNewsAdapter;
    private List<Map<String, Object>> mNewsData;
    private int refreshState = 0;  //0 正常  1 刷新  2 加载
    private String cat_id="";
    private String curCity;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;


    @Override
    protected NewsPresenter<NewsView> createPresenter() {
        return new NewsPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_home;
    }

    @Override
    protected void initView() {
        mTitleData = new ArrayList<>();
        mNewsData = new ArrayList<>();

        tv_location.setText(SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().city_name));

        onInitRecyclerView();
        onInitRefreshLayout();
        onUpdateData();
    }

    private void onStartRequest(){
        Map<String, String> param = new HashMap<>();
        if (mNewsData.size() !=0) {
            if (refreshState == 1) {  //刷新

                param.put(ResponseKey.getInstace().max_id, mNewsData.get(0).get(ResponseKey.getInstace().article_id) + "");
            } else if (refreshState == 2) { //更多
                param.put(ResponseKey.getInstace().min_id, mNewsData.get(mNewsData.size() - 1).get(ResponseKey.getInstace().article_id) + "");
            }
        }
            param.put(ResponseKey.getInstace().cat_id, cat_id);
        try {
            ipresenter.onGetNewsList(param);

        } catch (Exception e) {
            ToastUtils.onToast(e.getMessage());
            onResetRefreshState();
            e.printStackTrace();
        }
    }

    private void onGetNewsType(){
        Map<String, String> param = new HashMap<>();
        try {
            ipresenter.onGetNewsType(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewsResponse(Map<String, Object> result) {

        List<Map<String, Object>> newsData = (List<Map<String, Object>>) result.get(ResponseKey.getInstace().data);
        if (refreshState ==1) {
            mNewsData.addAll(0, newsData);
        }else {
            mNewsData.addAll(newsData);
        }
        mNewsAdapter.notifyDataSetChanged();
        tv_not_data.setVisibility(mNewsData.size()==0 ? View.VISIBLE : View.GONE);

        onResetRefreshState();
    }

    @Override
    public void onNewsTypeResponse(Map<String, Object> result) {

        if (mTitleData.size() != 0){
            mTitleData.clear();
        }
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.getInstace().result);


        mTitleData.addAll(list);
        mTitleAdapter.notifyDataSetChanged();
    }

    /**
     * 重置刷新状态
     */
    private void onResetRefreshState(){
        if (refreshState ==1){
            refreshLayout.finishRefresh();
        }else if(refreshState ==2){
            refreshLayout.finishLoadmore();
        }
        refreshState =0;
    }


    /**
     * RecyclenView 的初始化
     */
    private void  onInitRecyclerView(){


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_RecyclerView_title.setLayoutManager(layoutManager);
        rv_RecyclerView_title.setHasFixedSize(true);

        mTitleAdapter = new TitleAdapter(getActivity(), mTitleData, onTitleItemListener);
        rv_RecyclerView_title.setAdapter(mTitleAdapter);


        rv_RecyclerView_content.addItemDecoration(new RecyclerViewDivider(
        getActivity(), LinearLayoutManager.HORIZONTAL, DensityUtil.dp2px(getActivity(), 1),
        ContextCompat.getColor(getActivity(), R.color.default_backgroud_color)));
        rv_RecyclerView_content.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_RecyclerView_content.setHasFixedSize(true);

        mNewsAdapter = new NewsAdapter(getActivity(), mNewsData, onNewsItemListener, 0);
        rv_RecyclerView_content.setAdapter(mNewsAdapter);

    }
    private void onInitRefreshLayout(){

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                refreshState = 1;
                onStartRequest();
//                ToastUtils.onToast("开始刷新数据");
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                refreshState =2;
                onStartRequest();
//                ToastUtils.onToast("开始加载更多");
            }
        });
    }

    OnRecyclerViewItemClickListener onTitleItemListener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onRecyclerItemClickListener(View view, int i) {

            cat_id = mTitleData.get(i).get(ResponseKey.getInstace().cat_id)+"";

            refreshState = 3;
            mNewsData.clear();
            onStartRequest();
        }

        @Override
        public void onRecyclerItemLongClickListener(View view, int i) {

        }
    };

    OnRecyclerViewItemClickListener onNewsItemListener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onRecyclerItemClickListener(View view, int position) {

            mFunctionsManage.invokeFunction(INTERFACE_WITHPS, mNewsData.get(position).get(ResponseKey.getInstace().article_id)+"",
                    mNewsData.get(position).get(ResponseKey.getInstace().title)+"");
        }

        @Override
        public void onRecyclerItemLongClickListener(View view, int i) {

        }
    };

    @OnClick({R.id.ll_search, R.id.iv_qcode_scan})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_search: //搜索 param= -1
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, -1);
                break;
            case R.id.iv_qcode_scan:

                startActivity(new Intent(getActivity(), QRcodeScanActivity.class));
                break;
        }
    }


    private void onInitLocation(){


        //初始化定位
        mLocationClient = new AMapLocationClient(getActivity());

        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        option.setOnceLocation(true);
        //设置是否返回地址信息（默认返回地址信息）
        option.setNeedAddress(true);
        //关闭缓存机制
        option.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(option);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        mLocationClient.startLocation();
    }

    //声明定位回调监听器
    AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {

            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
                    curCity = aMapLocation.getCity();

                    tv_location.setText(curCity);

                    SharedTools.getInstance(getActivity()).onPutData(ResponseKey.getInstace().city_name, curCity);
                    SharedTools.getInstance(getActivity()).onPutData(ResponseKey.getInstace().city_code, aMapLocation.getCityCode());

//                    Map<String, Object> location = new HashMap<>();
//                    location.put(ResponseKey.getInstace().cat_name, curCity);
//                    location.put(ResponseKey.getInstace().cat_id, 999);
//                    if(mTitleData.size()>4){
//                        mTitleData.add( 4, location);
//                    }else{
//                        mTitleData.add(location);
//                    }
//                    mTitleAdapter.notifyDataSetChanged();
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                }
            }
        }
    };

    /**
     * 请求权限
     */
    private void onRequestPermission(){//READ_PHONE_STATE

        List<PermissionItem> permissions = new ArrayList<PermissionItem>();
        permissions.add(new PermissionItem(Manifest.permission.ACCESS_COARSE_LOCATION, "定位", R.drawable.permission_ic_location));
        HiPermission.create(getActivity()).permissions(permissions).checkMutiPermission(new PermissionCallback() {
            @Override
            public void onClose() {
                Logger.d("用户关闭权限申请");
            }

            @Override
            public void onFinish() {
                Logger.d("所有权限申请完成");

                onInitLocation();
            }

            @Override
            public void onDeny(String permission, int position) {

            }

            @Override
            public void onGuarantee(String permission, int position) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLocationClient != null){
            mLocationClient.stopLocation();
        }
    }

    public void onUpdateData(){

        mTitleData.clear();
        mNewsData.clear();
        onStartRequest();
        onGetNewsType();
        onRequestPermission();
    }
}
