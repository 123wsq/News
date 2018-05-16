package com.yc.wsq.app.news.activity.my.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.wsq.library.tools.DialogTools;
import com.wsq.library.utils.AppManager;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.mvp.presenter.BasePresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity{



    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_cache_size) TextView tv_cache_size;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_setting;
    }

    @Override
    protected void initView() {

        tv_title.setText(getResources().getString(R.string.str_setting_text));
        try {
            tv_cache_size.setText(AppManager.getTotalCacheSize(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @OnClick({R.id.ll_back, R.id.ll_account_setting, R.id.ll_clear_cache})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_account_setting://账户设置  param =1
               startActivity(new Intent(this, AccountSettingActivity.class));
                break;
            case R.id.ll_clear_cache:
                onClearCache();
                break;
        }
    }


    /**
     * 清理缓存
     */
    public void onClearCache(){

        DialogTools.showDialog(this, "清理", "不了","提示", "清理缓存后将需要花费更多的时间加载数据，您确定要清理吗？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                AppManager.clearAllCache(SettingActivity.this);
                try {
                    tv_cache_size.setText(AppManager.getTotalCacheSize(getContext()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
    }
}
