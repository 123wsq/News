package com.yc.wsq.app.news.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.wsq.library.tools.ToastUtils;
import com.wsq.library.utils.SystemUtils;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.loader.OnAuthLoginListener;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UserLoginView;
import com.yc.wsq.app.news.mvp.view.UserView;
import com.yc.wsq.app.news.tools.ParamFormat;
import com.yc.wsq.app.news.tools.ShareTools;
import com.yc.wsq.app.news.tools.SharedTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

public class LoginActivity extends BaseActivity<UserLoginView, UserPresenter<UserLoginView>> implements UserLoginView{


    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_no_account_register) TextView tv_no_account_register;
    @BindView(R.id.et_username) EditText et_username;
    @BindView(R.id.et_password) EditText et_password;

    @Override
    protected UserPresenter<UserLoginView> createPresenter() {
        return new UserPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_activity_login;
    }

    @Override
    protected void initView() {

        tv_title.setText(getResources().getText(R.string.str_user_login_text));
        tv_no_account_register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @OnClick({R.id.ll_back, R.id.tv_login, R.id.tv_no_account_register, R.id.tv_forget_password, R.id.ll_wechat_login, R.id.ll_qq_login})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_login:
                onLogin();
                break;
            case R.id.tv_no_account_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                break;
            case R.id.ll_wechat_login:
              onRequestPermission();
                break;
            case R.id.ll_qq_login:

                break;
        }
    }

    private void onLogin(){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.getInstace().username, et_username.getText().toString().trim());
        param.put(ResponseKey.getInstace().password, et_password.getText().toString().trim());
        try {
            ipresenter.onUserLogin(param);
        } catch (Exception e) {
            ToastUtils.onToast(e.getMessage());
            e.printStackTrace();
        }
    }

    private void onWechatAuth(){
        ShareTools.onShare(5, "", "", new OnAuthLoginListener() {
            @Override
            public void onAuthState(int state, Platform platform) {

                try {
                    switch (state) {
                        case 0:

                            Map<String, Object> map = ParamFormat.onAllJsonToMap(platform.getDb().exportData());

                            SharedTools.getInstance(LoginActivity.this).onPutData(ResponseKey.getInstace().unionid,
                                    map.get(ResponseKey.getInstace().unionid)+"");
                            SharedTools.getInstance(LoginActivity.this).onPutData(ResponseKey.getInstace().openid,
                                    map.get(ResponseKey.getInstace().openid)+"");
                            SharedTools.getInstance(LoginActivity.this).onPutData(ResponseKey.getInstace().nickname,
                                    map.get(ResponseKey.getInstace().nickname)+"");
                            SharedTools.getInstance(LoginActivity.this).onPutData(ResponseKey.getInstace().icon,
                                    map.get(ResponseKey.getInstace().icon)+"");

                            onWechatLogin(true, 0);
                            break;
                        case 1:
                            ToastUtils.onToast("授权失败");
                            break;
                        case 2:
                            ToastUtils.onToast("取消授权");
                            break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void onWechatLogin(boolean isCheck, int bind){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.getInstace().unionid, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().unionid));
        param.put(ResponseKey.getInstace().openid, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().openid));
        param.put(ResponseKey.getInstace().nickname, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().nickname));
        param.put(ResponseKey.getInstace().headimgurl, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().icon));
        if (!isCheck){
            param.put(ResponseKey.getInstace().is_save, bind+"");
        }
        param.put(ResponseKey.getInstace().imei, SystemUtils.getIMEI());

        try {
            ipresenter.onCheckIMEI(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponseData(Map<String, Object> result) {

           onSaveData(result);
    }

    @Override
    public void onCheckIMEIResponseData(Map<String, Object> result) {

        Logger.d("第三方登录返回");
        int status = (int) result.get(ResponseKey.getInstace().rsp_status);
       if (status == 1){
           onSaveData(result);
       }else{
           String msg = (String) result.get(ResponseKey.getInstace().rsp_msg);
           onShowDialog("绑定", "重新申请", "提示", msg, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {

                   onWechatLogin(false, 1);
               }
           }, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   onWechatLogin(false, 2);
               }
           });
       }
    }

    private void onSaveData(Map<String, Object> result){

        Map<String, Object> data = (Map<String, Object>) result.get(ResponseKey.getInstace().result);
        SharedTools shared = SharedTools.getInstance(getContext());

        Iterator<Map.Entry<String, Object>> it =  data.entrySet().iterator();
        if (it != null) {
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                shared.onPutData(entry.getKey(), entry.getValue() + "");
            }
            finish();
        }
    }


    /**
     * 请求权限
     */
    private void onRequestPermission(){//READ_PHONE_STATE

        List<PermissionItem> permissions = new ArrayList<PermissionItem>();
        permissions.add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, "手机状态", R.drawable.permission_ic_phone));
        HiPermission.create(this).permissions(permissions).checkMutiPermission(new PermissionCallback() {
            @Override
            public void onClose() {
                Logger.d("用户关闭权限申请");
            }

            @Override
            public void onFinish() {
                Logger.d("所有权限申请完成");
               onWechatAuth();

            }

            @Override
            public void onDeny(String permission, int position) {

            }

            @Override
            public void onGuarantee(String permission, int position) {

            }
        });
    }
}
