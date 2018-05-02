package com.yc.wsq.app.news.fragment.news;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
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

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class NewsDetailsFragment extends BaseFragment<NewsView, NewsPresenter<NewsView>> implements NewsView{

    public static final String TAG = NewsDetailsFragment.class.getName();

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.wv_WebView)
    WebView wv_WebView;

    private String article_id = "";

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
        onInitWebView();


        handler.postDelayed(runnable, 5*1000);
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

    @OnClick({R.id.ll_back})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ll_back:
                mFunctionsManage.invokeFunction(INTERFACE_BACK);
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
}
