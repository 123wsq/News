package com.yc.wsq.app.news.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.constant.Constant;
import com.yc.wsq.app.news.mvp.presenter.BasePresenter;
import com.yc.wsq.app.news.tools.SharedTools;

public class StartActivity extends BaseActivity{

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_activity_start;
    }

    @Override
    protected void initView() {

        handler.post(thread);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            boolean isFirst = SharedTools.getInstance(StartActivity.this).onGetBoolean(Constant.getInstance().ISFIRST);
            if (isFirst){
                startActivity(new Intent(StartActivity.this, MainActivity.class));
            }else {
                startActivity(new Intent(StartActivity.this, WellcomeActivity.class));
            }

            overridePendingTransition(R.anim.anim_activity_default_in, R.anim.anim_activity_default_out);
            finish();
        }
    };

    Runnable thread = new Runnable() {
        @Override
        public void run() {
            handler.sendEmptyMessageDelayed(0, 1*1000);
        }
    };
}
