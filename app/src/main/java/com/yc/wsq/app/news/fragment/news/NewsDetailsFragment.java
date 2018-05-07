package com.yc.wsq.app.news.fragment.news;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.constant.Urls;
import com.yc.wsq.app.news.mvp.presenter.NewsPresenter;
import com.yc.wsq.app.news.mvp.view.NewsView;
import com.yc.wsq.app.news.tools.SharedTools;
import com.yc.wsq.app.news.tools.WebViewTools;
import com.yc.wsq.app.news.views.popup.SharePopup;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsDetailsFragment extends BaseFragment<NewsView, NewsPresenter<NewsView>> implements NewsView{

    public static final String TAG = NewsDetailsFragment.class.getName();

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.ll_layout) LinearLayout ll_layout;
    @BindView(R.id.wv_WebView) WebView wv_WebView;


    private SharePopup sharePopup;

    private String article_id = "";
    private String article_title;

    @Override
    protected NewsPresenter<NewsView> createPresenter() {
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


        handler.postDelayed(runnable, 10*1000);
    }

    private void onInitWebView(){
        WebViewTools.init(wv_WebView);

        String articleUrl = Urls.HOST+Urls.GET_NEWS_DETAILS+"?id=" + article_id;
        wv_WebView.loadUrl(articleUrl);
    }

    @Override
    public void onNewsResponse(Map<String, Object> result) {

        String msg = (String) result.get(ResponseKey.getInstace().rsp_msg);
        int status = (int) result.get(ResponseKey.getInstace().rsp_status);
        if (status !=-1) {
            ToastUtils.onToast(msg);
        }
    }

    @Override
    public void onNewsTypeResponse(Map<String, Object> result) {

    }

    @OnClick({R.id.ll_back, R.id.iv_share})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ll_back:
                mFunctionsManage.invokeFunction(INTERFACE_BACK);
                break;
            case R.id.iv_share:
                showSharePopup();
                break;
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


        sharePopup = new SharePopup(getActivity(), article_title);
        sharePopup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER, 0, 0);
//        showShare();
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("这个是分享标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(getActivity());
    }
}
