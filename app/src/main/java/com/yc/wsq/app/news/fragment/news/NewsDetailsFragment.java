package com.yc.wsq.app.news.fragment.news;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
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
import com.yc.wsq.app.news.tools.ShareTools;
import com.yc.wsq.app.news.tools.SharedTools;
import com.yc.wsq.app.news.tools.WebViewTools;
import com.yc.wsq.app.news.views.TextEditTextView;
import com.yc.wsq.app.news.views.popup.SharePopup;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class NewsDetailsFragment extends BaseFragment<NewsDetailsView, NewsPresenter<NewsDetailsView>> implements NewsDetailsView, View.OnFocusChangeListener {

    public static final String TAG = NewsDetailsFragment.class.getName();
    public static final String INTERFACE_WITHP = TAG +_INTERFACE_WITHP;


    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.ll_layout) LinearLayout ll_layout;
    @BindView(R.id.wv_WebView) WebView wv_WebView;
    @BindView(R.id.iv_collect) ImageView iv_collect;
    @BindView(R.id.et_input_comment) TextEditTextView et_input_comment;
    @BindView(R.id.ll_comment_state) LinearLayout ll_comment_state;
    @BindView(R.id.tv_publish) TextView tv_publish;


    private SharePopup sharePopup;

    private String article_id = "";
    private String article_title;
    private int collect_state = 2;  //2未收藏  1 收藏
    private String articleUrl;
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
        et_input_comment.setOnFocusChangeListener(this);
        handler.postDelayed(runnable, 10*1000);
    }

    private void onInitWebView(){
        WebViewTools.init(wv_WebView);

        articleUrl = Urls.HOST+Urls.GET_NEWS_DETAILS+"?id=" + article_id;
        wv_WebView.loadUrl(articleUrl);
    }

    @OnClick({R.id.ll_back, R.id.iv_share, R.id.iv_collect, R.id.iv_comment, R.id.tv_publish})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ll_back:
                mFunctionsManage.invokeFunction(INTERFACE_BACK);
                break;
            case R.id.iv_share:
                showSharePopup();
                break;
            case R.id.iv_comment: //进入到评论页面
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, "1", article_id);
                break;
            case R.id.iv_collect:
                String user_id = SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().user_id);
                if (TextUtils.isEmpty(user_id)){
                    ToastUtils.onToast("请先登录");
                    return;
                }
                onCollect();
                break;
            case R.id.tv_publish:
                onSendArticleComment();
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
//            onReLogin(); //请先登录
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
     * 写评论
     */
    private void onSendArticleComment(){
        Map<String, String> param = new HashMap<>();

        try {
            param.put(ResponseKey.getInstace().user_id, SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().user_id));
            param.put(ResponseKey.getInstace().content, et_input_comment.getText().toString().trim());
            param.put(ResponseKey.getInstace().article_id, article_id);
            param.put(ResponseKey.getInstace().is_anonymous, "0");
            ipresenter.onReadArticleComment(param);
        } catch (Exception e) {

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
            public void onClickResult(int platform) {
                ShareTools.onShare(platform, articleUrl);
            }
        });
        sharePopup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER, 0, 0);
    }


    @Override
    public void onReadNewsResponse(Map<String, Object> result) {
        String msg = (String) result.get(ResponseKey.getInstace().rsp_msg);
//        ToastUtils.onToast(msg);
        ImageView view = new ImageView(getActivity());
//        view.setLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setImageResource(R.mipmap.image_read_toast);
        ToastUtils.onToast(view);
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

    @Override
    public void onArticleCommentResponse(Map<String, Object> result) {

        et_input_comment.setText("");
        ll_comment_state.setVisibility( View.VISIBLE);
        tv_publish.setVisibility(View.GONE);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

//        ToastUtils.onToast(hasFocus ? "获得焦点" :"失去焦点");
        ll_comment_state.setVisibility(hasFocus ? View.GONE : View.VISIBLE);
        tv_publish.setVisibility(hasFocus ? View.VISIBLE : View.GONE);

    }
}
