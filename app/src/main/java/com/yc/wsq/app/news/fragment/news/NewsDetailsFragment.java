package com.yc.wsq.app.news.fragment.news;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.constant.Urls;
import com.yc.wsq.app.news.mvp.presenter.NewsPresenter;
import com.yc.wsq.app.news.mvp.view.NewsDetailsView;
import com.yc.wsq.app.news.mvp.view.NewsView;
import com.yc.wsq.app.news.tools.SharedTools;
import com.yc.wsq.app.news.tools.WebViewTools;
import com.yc.wsq.app.news.views.popup.SharePopup;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsDetailsFragment extends BaseFragment<NewsDetailsView, NewsPresenter<NewsDetailsView>> implements NewsDetailsView{

    public static final String TAG = NewsDetailsFragment.class.getName();
    public static final String INTERFACE_WITHP = TAG +_INTERFACE_WITHP;


    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.ll_layout) LinearLayout ll_layout;
    @BindView(R.id.wv_WebView) WebView wv_WebView;
    @BindView(R.id.iv_collect) ImageView iv_collect;


    private SharePopup sharePopup;

    private String article_id = "";
    private String article_title;
    private int collect_state = 2;  //2未收藏  1 收藏

    @Override
    protected NewsPresenter<NewsDetailsView> createPresenter() {
        return new NewsPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_news_info;
    }

    @Override
    protected void initView() {

        tv_title.setText("新闻详情");

        article_id = getArguments().getString(ResponseKey.getInstace().article_id);
        article_title = getArguments().getString(ResponseKey.getInstace().title);
        onInitWebView();
        onCollectState();
        ongetNewsCollectState();

        handler.postDelayed(runnable, 10*1000);
    }

    private void onInitWebView(){
        WebViewTools.init(wv_WebView);

        String articleUrl = Urls.HOST+Urls.GET_NEWS_DETAILS+"?id=" + article_id;
        wv_WebView.loadUrl(articleUrl);
    }



    @OnClick({R.id.ll_back, R.id.iv_share, R.id.iv_collect})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ll_back:
                mFunctionsManage.invokeFunction(INTERFACE_BACK);
                break;
            case R.id.iv_share:
                showSharePopup();
                break;
            case R.id.iv_collect:
                onCollect();
                break;
        }
    }

    /**
     * 获取新闻状态
     */
    private void ongetNewsCollectState(){

        Map<String, String> param = new HashMap<>();

        try {
            param.put(ResponseKey.getInstace().id, article_id);
            param.put(ResponseKey.getInstace().uid, SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().user_id));
            ipresenter.onGetCollectState(param);
        } catch (Exception e) {
            onReLogin(); //请先登录
//            ToastUtils.onToast(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 收藏
     */
    private void onCollect(){

        Map<String, String> param = new HashMap<>();

        try {
            param.put(ResponseKey.getInstace().id, article_id);
            param.put(ResponseKey.getInstace().uid, SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().user_id));
            param.put(ResponseKey.getInstace().token, SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().token));
            param.put(ResponseKey.getInstace().act, collect_state == 2 ? "1" :"0");
            ipresenter.onAddCollect(param);
        } catch (Exception e) {
            onReLogin(); //请先登录
//            ToastUtils.onToast(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 收藏状态
     */
    private void onCollectState(){

        if(collect_state == 2){
            iv_collect.setImageResource(R.mipmap.image_collect_icon);
        }else if(collect_state == 1){
            iv_collect.setImageResource(R.mipmap.image_collect_press_icon);
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Map<String, String> param = new HashMap<>();
            param.put(ResponseKey.getInstace().id, article_id);
            param.put(ResponseKey.getInstace().uid,
                    SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().user_id));
            param.put(ResponseKey.getInstace().token,
                    SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().token));
            try {
                ipresenter.onReadIntegral(param);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            Message msg = new Message();
            handler.sendMessage(msg);
        }
    };

    private void showSharePopup(){


        sharePopup = new SharePopup(getActivity(), new SharePopup.OnShareResultListener() {
            @Override
            public void onClickResult(String platform) {
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, platform, article_title, "这个是分享内容的描述");
            }
        });
        sharePopup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER, 0, 0);
    }


    @Override
    public void onReadNewsResponse(Map<String, Object> result) {
        String msg = (String) result.get(ResponseKey.getInstace().rsp_msg);
        ToastUtils.onToast(msg);
    }

    @Override
    public void onNewsStateResponse(Map<String, Object> result) {

        int status = (int) result.get(ResponseKey.getInstace().rsp_status);
        collect_state = status;
        onCollectState();
    }

    @Override
    public void onNewsCollectResponse(Map<String, Object> result) {
        String msg = (String) result.get(ResponseKey.getInstace().rsp_msg);
        int status = (int) result.get(ResponseKey.getInstace().rsp_status);
        ToastUtils.onToast(msg);

        ongetNewsCollectState();
    }
}
