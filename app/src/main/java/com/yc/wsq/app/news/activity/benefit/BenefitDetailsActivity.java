package com.yc.wsq.app.news.activity.benefit;

import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.constant.Urls;
import com.yc.wsq.app.news.mvp.presenter.BenefitPresenter;
import com.yc.wsq.app.news.mvp.view.BenefitView;
import com.yc.wsq.app.news.tools.WebViewTools;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class BenefitDetailsActivity extends BaseActivity<BenefitView, BenefitPresenter<BenefitView>> implements BenefitView{

    @BindView(R.id.tv_title) TextView  tv_title;
    @BindView(R.id.wv_WebView) WebView wv_WebView;
    @Override
    protected BenefitPresenter<BenefitView> createPresenter() {
        return new BenefitPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_benefit_details;
    }

    @Override
    protected void initView() {

        tv_title.setText(getResources().getText(R.string.str_benefit_details_text));
        onInitWebView();
    }

    private void onInitWebView(){
        WebViewTools.init(wv_WebView);


        String articleUrl = Urls.HOST+Urls.BENEFIT_DETAILS+"?id=" + getIntent().getStringExtra(ResponseKey.getInstace().id);
        wv_WebView.loadUrl(articleUrl);
    }

    @OnClick({R.id.ll_back})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    public void onBenefitResponse(Map<String, Object> result) {

    }



}
