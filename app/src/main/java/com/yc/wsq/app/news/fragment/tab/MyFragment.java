package com.yc.wsq.app.news.fragment.tab;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.activity.LoginActivity;
import com.yc.wsq.app.news.activity.RegisterActivity;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.constant.Urls;
import com.yc.wsq.app.news.mvp.presenter.BasePresenter;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UserMainView;
import com.yc.wsq.app.news.mvp.view.UserView;
import com.yc.wsq.app.news.tools.ParamFormat;
import com.yc.wsq.app.news.tools.ParamValidate;
import com.yc.wsq.app.news.tools.SharedTools;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的页面
 */
public class MyFragment extends BaseFragment<UserMainView, UserPresenter<UserMainView>> implements UserMainView{

    public static final String TAG = MyFragment.class.getName();
    public static final String  INTERFACE_WITHP = TAG+_INTERFACE_WITHP;
    @BindView(R.id.ll_back) LinearLayout ll_back;
    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.iv_header) ImageView iv_header;
    @BindView(R.id.tv_userName) TextView tv_userName;


    @Override
    protected UserPresenter<UserMainView> createPresenter() {
        return new UserPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_my;
    }

    @Override
    protected void initView() {

        ll_back.setVisibility(View.GONE);

        tv_title.setText(getResources().getString(R.string.str_my_text));

        onUserStatusChangeListener();


    }

    @OnClick({R.id.ll_user_login, R.id.ll_my_wallet, R.id.ll_integral, R.id.ll_member_upgrade,
            R.id.ll_about, R.id.ll_setting, R.id.ll_collect, R.id.ll_history, R.id.ll_comment,
            R.id.ll_message, R.id.ll_focus, R.id.ll_discount, R.id.ll_help_feedback})
    public void onClick(View view){
        String uid = SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().user_id);
        String token = SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().token);
        try {
            if (view.getId() != R.id.ll_about) {
                ParamValidate.getInstance().onValidateIsNull(uid, token);
            }
        } catch (Exception e) {
            //用户信息为空，需要重新登录
            mFunctionsManage.invokeFunction(INTERFACE_WITHP, 4);
            e.printStackTrace();
            return;
        }
        switch (view.getId()){

            case R.id.ll_my_wallet: //param =1;  我的钱包
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 1);
                break;
            case R.id.ll_integral:  //我的积分  param =2
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 2);
                break;
            case R.id.ll_member_upgrade: //会员升级 param =3
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 3);
                break;
            case R.id.ll_user_login:  //会员登录 param = 4
                if (TextUtils.isEmpty(SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().nickname))) {
                    mFunctionsManage.invokeFunction(INTERFACE_WITHP, 4);
                }
                break;
            case R.id.ll_about: //关于  param = 5;
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 5);
                break;
            case R.id.ll_setting: //设置 param =6;
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 6);
                break;
            case R.id.ll_collect: //收藏 param =7;
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 7);
                break;
            case R.id.ll_history: //历史记录 param =8;
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 8);
                break;
            case R.id.ll_comment: //评论  param =9
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 9);
                break;
            case R.id.ll_message: // 消息  param = 10
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 10);
                break;
            case R.id.ll_focus: //我的关注 param =11;
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 11);
                break;
            case R.id.ll_discount:  // 优惠券 param = 12;
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 12);
                break;
            case R.id.ll_help_feedback: //帮助与反馈 param =13;
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 13);
                break;
            default:
                break;
        }
    }


    /**
     * 用户状态变化监听
     */
    public void onUserStatusChangeListener(){
        String head_pic= SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().head_pic);

        if (TextUtils.isEmpty(head_pic)){
            iv_header.setImageResource(R.mipmap.image_head_default);
        }else {
            RequestOptions options = new RequestOptions();
            options.error(R.mipmap.image_header_default);
            options.circleCrop();
            Glide.with(getActivity())
                    .load(Urls.HOST + SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().head_pic))
                    .apply(options)
                    .into(iv_header);
        }
        String nickname = SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().nickname);
        tv_userName.setText(TextUtils.isEmpty(nickname)? "点击登录" :nickname);
    }

    /**
     * 获取用户信息
     */
    private void onGetUserInfo(){
        Map<String, String> param = new HashMap<>();

        try {
            ipresenter.onGetUserInfo(param);
        } catch (Exception e) {
            ToastUtils.onToast(e.getMessage());
            e.printStackTrace();
        }
    }

    private void onHideLogin(){
        Map<String, String> param = new HashMap<>();

        try {
            String username = SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().username);
            String password = SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().password);
            param.put(ResponseKey.getInstace().username, username);
            param.put(ResponseKey.getInstace().password, password);
            ipresenter.onUserLogin(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUserInfoResponse(Map<String, Object> result) {

        int status = (int) result.get(ResponseKey.getInstace().rsp_status);
        if (status == 1){
//            onGetUserInfo();
        }
    }

    @Override
    public void onHideLoginResponse(Map<String, Object> result) {

    }
}
