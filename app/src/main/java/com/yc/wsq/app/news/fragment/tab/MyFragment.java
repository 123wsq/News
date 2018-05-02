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
import com.yc.wsq.app.news.tools.ParamFormat;
import com.yc.wsq.app.news.tools.ParamValidate;
import com.yc.wsq.app.news.tools.SharedTools;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的页面
 */
public class MyFragment extends BaseFragment{

    public static final String TAG = MyFragment.class.getName();
    public static final String  INTERFACE_WITHP = TAG+_INTERFACE_WITHP;
    @BindView(R.id.ll_back) LinearLayout ll_back;
    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.iv_header) ImageView iv_header;
    @BindView(R.id.tv_userName) TextView tv_userName;


    @Override
    protected BasePresenter createPresenter() {
        return null;
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

    @OnClick({R.id.ll_user_login, R.id.ll_my_wallet, R.id.ll_integral, R.id.ll_member_upgrade, R.id.ll_about, R.id.ll_setting})
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
}
