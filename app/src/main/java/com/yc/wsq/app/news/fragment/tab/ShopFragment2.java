package com.yc.wsq.app.news.fragment.tab;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.wsq.library.listener.OnRecyclerViewItemClickListener;
import com.wsq.library.tools.RecyclerViewDivider;
import com.wsq.library.utils.DensityUtil;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.adapter.GoodsAdapter;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.Constant;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.constant.Urls;
import com.yc.wsq.app.news.loader.GlideImageLoader;
import com.yc.wsq.app.news.mvp.presenter.ShopPresenter;
import com.yc.wsq.app.news.mvp.view.ShopView;
import com.yc.wsq.app.news.tools.SharedTools;
import com.yc.wsq.app.news.tools.WebViewTools;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商城页面
 */
public class ShopFragment2 extends BaseFragment<ShopView, ShopPresenter<ShopView>> implements ShopView{

    public static final String TAG = ShopFragment2.class.getName();
    public static final String INTERFACE_NRNP = TAG+_INTERFACE_NPNR;
    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.ll_back)LinearLayout ll_back;
    @BindView(R.id.wv_WebView)
    WebView wv_WebView;

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
        onInitWebView();
    }


    @Override
    public void onShopResponse(Map<String, Object> result) {


    }

    private void onInitWebView(){

        WebViewTools.init(wv_WebView);

        String articleUrl = "http://www.tp2.com/Mobile/Index/index.html";
//        String articleUrl = "http://api.yczmj.cn/";
        Logger.d(articleUrl);


        wv_WebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                CookieManager cm = CookieManager.getInstance();
                String cookie = cm.getCookie(url);
                Logger.d("cookie:"+cookie);
                String sp[] = cookie.split(";");
                for (int i = 0; i < sp.length; i++) {
                    if (sp[i].startsWith("uname")){
                        Logger.d(sp[i].split("=")[1]);
                    }else if(sp[i].startsWith("user_id")){

                    }
                }
//                if (!TextUtils.isEmpty(cookie)){
//                    SharedTools.getInstance(getActivity()).onPutData(Constant.getInstance().COOKIE, cookie);
//                }

//                synCookies(url);
                view.loadUrl(url);
                return true;
            }

        });
        synCookies(articleUrl);
        wv_WebView.loadUrl(articleUrl);
    }

    @OnClick({R.id.ll_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                mFunctionsManage.invokeFunction(INTERFACE_NRNP);
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
//        cookieManager.setCookie(url, session);
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


}
