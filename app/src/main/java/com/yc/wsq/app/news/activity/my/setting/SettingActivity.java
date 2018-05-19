package com.yc.wsq.app.news.activity.my.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.wsq.library.tools.DialogTools;
import com.wsq.library.utils.AppManager;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.constant.Urls;
import com.yc.wsq.app.news.mvp.presenter.BasePresenter;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UserView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity<UserView, UserPresenter<UserView>> implements UserView{



    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_cache_size) TextView tv_cache_size;

    @Override
    protected UserPresenter<UserView> createPresenter() {
        return new UserPresenter<>();
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
    @OnClick({R.id.ll_back, R.id.ll_account_setting, R.id.ll_clear_cache, R.id.ll_check_update})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_account_setting://账户设置  param =1
               startActivity(new Intent(this, AccountSettingActivity.class));
                break;
            case R.id.ll_check_update:
                onCheckUpdate();
                break;
            case R.id.ll_clear_cache:
                onClearCache();
                break;
        }
    }

    private void onCheckUpdate(){
        Map<String, String> param = new HashMap<>();

        try {
            ipresenter.onGetAppVersion(param);
        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public void onResponseData(Map<String, Object> result) {


        Map<String, Object> data = (Map<String, Object>) result.get(ResponseKey.getInstace().data);
        String curVersion = AppManager.getAppVersion(this);
        String serverVerion = (String) data.get(ResponseKey.getInstace().app_version);
        if (curVersion.equals(serverVerion)){
            onShowDialog("更新提示","您当前就是最新版本！", null);
        }else{
            onShowDialog("更新", "下次吧", "更新提示", "您有最新的版本，请及时更新！", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Uri uri = Uri.parse(Urls.APP_UPDATE);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
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
}
