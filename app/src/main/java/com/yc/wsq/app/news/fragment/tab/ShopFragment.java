package com.yc.wsq.app.news.fragment.tab;


import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.Constant;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.constant.Urls;
import com.yc.wsq.app.news.mvp.presenter.ShopPresenter;
import com.yc.wsq.app.news.mvp.view.ShopView;
import com.yc.wsq.app.news.tools.SharedTools;
import com.yc.wsq.app.news.tools.WebViewTools;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商城页面
 */
public class ShopFragment extends BaseFragment<ShopView, ShopPresenter<ShopView>> implements ShopView{


    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.ll_back)LinearLayout ll_back;
    @BindView(R.id.wv_WebView)
    WebView wv_WebView;

    private String user_id = "";
    private String articleUrl;
    @Override
    protected ShopPresenter<ShopView> createPresenter() {
        return new ShopPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_shop2;
    }

    @Override
    protected void initView() {

        tv_title.setText(getResources().getString(R.string.str_shop_text));
        ll_back.setVisibility(View.GONE);
        onInitWebView();
    }


    @Override
    public void onShopResponse(Map<String, Object> result) {


    }

    @Override
    public void onResume() {
        super.onResume();
        user_id = SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().user_id);
    }

    private void onInitWebView(){

        WebViewTools.init(wv_WebView);

        articleUrl = Urls.SHOPPING +"?"+ ResponseKey.getInstace().user_id+"="+user_id;
        Logger.d(articleUrl);


        wv_WebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }

        });
        wv_WebView.addJavascriptInterface(new AndroidObject(), "nativeMethod");
//        synCookies(articleUrl);
        wv_WebView.loadUrl(articleUrl);
    }

    @OnClick({R.id.ll_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:

                break;
        }
    }

    /**
     * 同步一下cookie
     */
    public void synCookies(String url) {

        CookieSyncManager.createInstance(getActivity()).startSync();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
//        cookieManager.removeSessionCookie();//移除

        String session = SharedTools.getInstance(getActivity()).onGetString(Constant.getInstance().SESSION);
//        String cookies = "cn=0; is_mobile=0; is_distribut=1;  user_id="
//                +SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().user_id)
//                +"; uname="+SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().mobile);
//        cookieManager.setCookie(url, cookies);
        cookieManager.setCookie(url, session);
//        if (TextUtils.isEmpty(SharedTools.getInstance(getActivity()).onGetString(Constant.getInstance().COOKIE))) {

            cookieManager.setCookie(url, "cn=0");
            cookieManager.setCookie(url, "is_mobile=1");
            cookieManager.setCookie(url, "is_distribut=1");
            cookieManager.setCookie(url, "user_id=" + SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().user_id));
            cookieManager.setCookie(url, "uname=" + SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().mobile));
//        }else {
//            cookieManager.setCookie(url, SharedTools.getInstance(getActivity()).onGetString(Constant.getInstance().COOKIE));
//            Logger.d("cookie:"+SharedTools.getInstance(getActivity()).onGetString(Constant.getInstance().COOKIE));
//        }
        CookieSyncManager.getInstance().sync();
    }


    class AndroidObject extends Object{

        @JavascriptInterface
        public void onLogin(){
            onReLogin();
        }

    }

}
