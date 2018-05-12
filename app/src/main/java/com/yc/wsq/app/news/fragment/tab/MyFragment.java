package com.yc.wsq.app.news.fragment.tab;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orhanobut.logger.Logger;
import com.wsq.library.tools.DialogTools;
import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.constant.Urls;
import com.yc.wsq.app.news.fragment.my.IntegralFragment;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UserMainView;
import com.yc.wsq.app.news.tools.ParamValidate;
import com.yc.wsq.app.news.tools.ShareTools;
import com.yc.wsq.app.news.tools.SharedTools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 我的页面
 */
public class MyFragment extends BaseFragment<UserMainView, UserPresenter<UserMainView>> implements UserMainView{

    public static final String TAG = MyFragment.class.getName();
    public static final String  INTERFACE_WITHP = TAG+_INTERFACE_WITHP;
    @BindView(R.id.iv_header) ImageView iv_header;
    @BindView(R.id.tv_userName) TextView tv_userName;
    @BindView(R.id.tv_vip_level) TextView tv_vip_level;
    @BindView(R.id.tv_total_money) TextView tv_total_money;
    @BindView(R.id.tv_total_integral) TextView tv_total_integral;
    @BindView(R.id.ll_already_login) LinearLayout ll_already_login;
    @BindView(R.id.ll_not_login) LinearLayout ll_not_login;
    @BindView(R.id.tv_disciple) TextView tv_disciple;



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



    }



    @OnClick({ R.id.ll_my_wallet, R.id.ll_integral, R.id.ll_member_upgrade,R.id.ll_invite,R.id.ll_generalize_code, R.id.ll_apprentice,
            R.id.ll_about, R.id.ll_setting, R.id.ll_collect,  R.id.ll_discount, R.id.ll_help_feedback, R.id.ll_wechat, R.id.ll_qq, R.id.ll_qcode, R.id.tv_login})
    public void onClick(View view){
        String uid = SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().user_id);
        String token = SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().token);
        try {
            if (view.getId() != R.id.ll_about  && view.getId() != R.id.tv_login) {
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
            case R.id.tv_login:  //会员登录 param = 4
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 4);
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
            case R.id.ll_invite: //输入邀请码  param =8

                String inviteCode = SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().first_leader);
                if (inviteCode.equals("0")) {
                    mFunctionsManage.invokeFunction(INTERFACE_WITHP, 8);
                }else {
                    onShowDialog("提示", "您已经填写了邀请码，不能再次修改了", null);
                }
                break;
            case R.id.ll_apprentice: //徒弟列表 param =9
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 9);
                break;
            case R.id.ll_wechat_moments:


                break;
            case R.id.ll_wechat:  //微信好友邀请
                ShareTools.onShare(1, "www.baidu.com");
//                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 9);
//                ShareTools.showShare(getActivity(), Wechat.NAME, "点一下，和我一起领10~15元现金！","","");
                break;
            case R.id.ll_qq:  //QQ好友邀请
//                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 10);
//                ShareTools.showShare(getActivity(), QQ.NAME, "点一下，和我一起领10~15元现金！","","");
                break;
            case R.id.ll_qcode: //展示二维码 param =10
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 10);
                break;
            case R.id.ll_discount:  // 优惠券 param = 12;
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 12);
                break;
            case R.id.ll_help_feedback: //帮助与反馈 param =13;
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 13);
                break;
            case R.id.ll_generalize_code:  //推广码
                String generalize_code = SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().user_id);
                onShowDialog("提示", "您的推广码是: " + generalize_code, null);
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
        tv_vip_level.setText(SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().level_name));
        tv_total_integral.setText(SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().pay_points));
        tv_total_money.setText(SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().user_money));
        tv_disciple.setText(SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().disciple));
        if(nickname.length() ==0){
            ll_already_login.setVisibility(View.GONE);
            ll_not_login.setVisibility(View.VISIBLE);
        }else{
            ll_already_login.setVisibility(View.VISIBLE);
            ll_not_login.setVisibility(View.GONE);
        }
    }

    /**
     * 获取用户信息
     */
    private void onGetUserInfo(){
        Map<String, String> param = new HashMap<>();

        try {
            param.put(ResponseKey.getInstace().uid, SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().user_id));
            param.put(ResponseKey.getInstace().token, SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().token));
            ipresenter.onGetUserInfo(param);
        } catch (Exception e) {
//            ToastUtils.onToast(e.getMessage());
            e.printStackTrace();
        }
    }



    @Override
    public void onUserInfoResponse(Map<String, Object> result) {
        SharedTools shared = SharedTools.getInstance(getContext());
        Map<String, Object> data = (Map<String, Object>) result.get(ResponseKey.getInstace().data);
        Iterator<Map.Entry<String, Object>> it =  data.entrySet().iterator();

        while (it.hasNext()){
            Map.Entry<String, Object> entry =  it.next();
            shared.onPutData(entry.getKey(), entry.getValue()+"");
        }
        onUserStatusChangeListener();
    }


    public void onUpdateData(){
        onGetUserInfo();
        onUserStatusChangeListener();
    }

}
