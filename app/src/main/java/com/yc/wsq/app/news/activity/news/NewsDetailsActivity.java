package com.yc.wsq.app.news.activity.news;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;
import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.bean.NewsBean;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.constant.Urls;
import com.yc.wsq.app.news.loader.OnAuthLoginListener;
import com.yc.wsq.app.news.mvp.presenter.NewsPresenter;
import com.yc.wsq.app.news.mvp.view.NewsDetailsView;
import com.yc.wsq.app.news.tools.ShareTools;
import com.yc.wsq.app.news.tools.SharedTools;
import com.yc.wsq.app.news.tools.WebViewTools;
import com.yc.wsq.app.news.views.TextEditTextView;
import com.yc.wsq.app.news.views.popup.SharePopup;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;

public class NewsDetailsActivity extends BaseActivity<NewsDetailsView, NewsPresenter<NewsDetailsView>> implements NewsDetailsView, View.OnFocusChangeListener {



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


        article_id = getIntent().getStringExtra(ResponseKey.getInstace().article_id);
        article_title = getIntent().getStringExtra(ResponseKey.getInstace().title);
        onInitWebView();
        onCollectState();
        ongetNewsCollectState();
        et_input_comment.setOnFocusChangeListener(this);
        handler.postDelayed(runnable, 10*1000);
    }

    @SuppressLint("JavascriptInterface")
    private void onInitWebView(){


        ContentValues values =  new ContentValues();
        values.put(ResponseKey.getInstace().isRead, "1");
        DataSupport.updateAll(NewsBean.class, values, ResponseKey.getInstace().article_id+" = ?", article_id);
        WebViewTools.init(wv_WebView);

        articleUrl = Urls.HOST+Urls.GET_NEWS_DETAILS+"?id=" + article_id;
        wv_WebView.loadUrl(articleUrl);
        wv_WebView.addJavascriptInterface(new JavascriptInterface(), "imagelistener");
        wv_WebView.setWebViewClient(new MyWebViewClient());
    }

    @OnClick({R.id.ll_back, R.id.iv_share, R.id.iv_collect, R.id.iv_comment, R.id.tv_publish})
    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.iv_share:
                showSharePopup();
                break;
            case R.id.iv_comment: //进入到评论页面
                intent = new Intent(this, ArticleCommentActivity.class);
                intent.putExtra(ResponseKey.getInstace().article_id, article_id);
                startActivity(intent);
                break;
            case R.id.iv_collect:
                String user_id = SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_id);
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
            param.put(ResponseKey.getInstace().uid, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_id));
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
            param.put(ResponseKey.getInstace().uid, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_id));
            param.put(ResponseKey.getInstace().token, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().token));
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
            param.put(ResponseKey.getInstace().user_id, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_id));
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
                    SharedTools.getInstance(NewsDetailsActivity.this).onGetString(ResponseKey.getInstace().user_id));
            param.put(ResponseKey.getInstace().token,
                    SharedTools.getInstance(NewsDetailsActivity.this).onGetString(ResponseKey.getInstace().token));
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


        sharePopup = new SharePopup(this, new SharePopup.OnShareResultListener() {
            @Override
            public void onClickResult(int platform) {
                ShareTools.onShare(platform, article_title, articleUrl, new OnAuthLoginListener() {
                    @Override
                    public void onAuthState(int state, Platform platform) {

                        switch (state){
                            case 0:
                                ToastUtils.onToast("分享成功");
                                break;
                            case 1:
                                ToastUtils.onToast("分享失败");
                                break;
                            case 2:
                                ToastUtils.onToast("取消分享");
                                break;
                        }
                    }
                });
            }
        });
        sharePopup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER, 0, 0);
    }


    @Override
    public void onReadNewsResponse(Map<String, Object> result) {
        String msg = (String) result.get(ResponseKey.getInstace().rsp_msg);
//        ToastUtils.onToast(msg);
        ImageView view = new ImageView(this);
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

        ll_comment_state.setVisibility(hasFocus ? View.GONE : View.VISIBLE);
        tv_publish.setVisibility(hasFocus ? View.VISIBLE : View.GONE);

    }

    class JavascriptInterface{

        public JavascriptInterface() {
        }

        @android.webkit.JavascriptInterface
        public void openImage(String all, String img) {
            Logger.d("图片路径："+all+"\n"+img);

            if (TextUtils.isEmpty(all)){
                ToastUtils.onToast("读取图片失败");
                return;
            }

            int position = -1;
            String[] images = all.split(",");
            List<LocalMedia> list = new ArrayList<>();
            for (int i = 0 ; i < images.length; i++){
                LocalMedia media = new LocalMedia();
                media.setPath(images[i]);
                list.add(media);
                if (images[i].equals(img)){
                    position = i;
                }
            }


            if (position == -1){
                position = 0 ;
            }

            PictureSelector.create(NewsDetailsActivity.this).externalPicturePreview(position, list);
        }
    }


    class MyWebViewClient extends WebViewClient{

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);
            addImageClickListener(view);
        }

        private void addImageClickListener(WebView webView) {
            webView.loadUrl("javascript:(function(){" +
                    "var objs = document.getElementsByTagName(\"img\"); " +
                    "var path =''; "+
                    "for(var i=0;i<objs.length;i++)  " +
                    "{"+
                    "path += objs[i].src +\",\" ;"+
                    "var obj = objs[i];"+
                    "    objs[i].onclick=function()  " +
                    "    {  " +
                    "        window.imagelistener.openImage(path, this.src);  " +//通过js代码找到标签为img的代码块，设置点击的监听方法与本地的openImage方法进行连接
                    "    }  " +
                    "}" +
//                    "var objpdfs = document.getElementsByTagName(\"a\"); " +
//                    "for(var i=0;i<objpdfs.length;i++)  " +
//                    "{"+
//                    "var objpdf = objpdfs[i];"+
//                    "    objpdfs[i].onclick=function()  " +
//                    "    {  " +
//                    "        window.imagelistener.openPdf(this.href, this.innerText);  " +//通过js代码找到标签为img的代码块，设置点击的监听方法与本地的openImage方法进行连接
//                    "    }  " +
//                    "}" +
                    "})()");
        }

    }
}
