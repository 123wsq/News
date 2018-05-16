package com.yc.wsq.app.news.activity.my;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pay.alipay.AlipayTools;
import com.example.pay.alipay.PayResult;
import com.wsq.library.tools.DialogTools;
import com.wsq.library.tools.ToastUtils;
import com.wsq.library.views.view.CustomPopup;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.RechargeView;
import com.yc.wsq.app.news.tools.SharedTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 会员升级
 */
public class MemberUpgradeActivity extends BaseActivity<RechargeView, UserPresenter<RechargeView>> implements RechargeView{

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ll_layout)
    LinearLayout ll_layout;
    @BindView(R.id.iv_member_upgrade)
    ImageView iv_member_upgrade;

    @BindView(R.id.ll_layout_general) LinearLayout ll_layout_general;
    @BindView(R.id.ll_layout_vip) LinearLayout ll_layout_vip;

    private CustomPopup popup;
    private String money_type;
    private String  price;
    private String order_sn;
    private String title;

    @Override
    protected UserPresenter<RechargeView> createPresenter() {
        return new UserPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_member_upgrade;
    }

    @Override
    protected void initView() {

        tv_title.setText(getResources().getString(R.string.str_member_upgrade_text));

       onUpdatePage();
    }

    @OnClick({R.id.ll_back, R.id.iv_member_upgrade})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.iv_member_upgrade:

                String is_vip = SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().is_vip);
                if(is_vip.equals("1")){
                    onIsVip();
                }else{

                   onSelctorInviteCode();
                }

                break;
        }
    }

    private void onIsVip(){
        DialogTools.showDialog(this, "确定", "提示", "您已经是VIP会员了！", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
    }

    /**
     *
     */
    private void onSelctorInviteCode(){

        String inviteCode = SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().first_leader);
        if (inviteCode.equals("0")){
            DialogTools.showDialog(this, "填写", "下次吧", "提示",
                    "您还没有填写邀请码，是否填写？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                   startActivity(new Intent(MemberUpgradeActivity.this, InviteCodeActivity.class));
                    dialog.dismiss();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onInitPopup();
                    dialog.dismiss();
                }
            });
        }else {
            onInitPopup();
        }
    }

    /**
     * 选择支付类型
     */
    private void onInitPopup(){
        List<String> list = new ArrayList<>();
        list.add("支付宝");
//        list.add("微信");
        popup = new CustomPopup(this, list, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        }, new CustomPopup.PopupItemListener() {
            @Override
            public void onClickItemListener(int i, String s) {
                popup.dismiss();

                money_type = i+"";
                onRecharge();
//
            }
        });
        popup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER, 0, 0);
    }

    /**
     * 获取充值信息
     */
    private void onRecharge(){
        Map<String, String> param = new HashMap<>();

        try {
            param.put(ResponseKey.getInstace().uid, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_id));
            param.put(ResponseKey.getInstace().token, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().token));
            param.put(ResponseKey.getInstace().type, "4");
            param.put(ResponseKey.getInstace().money_type, money_type);
            param.put(ResponseKey.getInstace().price, "");
            param.put(ResponseKey.getInstace().first_leader, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().first_leader));
            ipresenter.onGetTradeDetails(param);
        } catch (Exception e) {
            ToastUtils.onToast(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 通知服务器充值成功
     * @param timestamp
     */
    private void onNotificationServer(String timestamp){
        Map<String, String> param = new HashMap<>();

        try {
            param.put(ResponseKey.getInstace().uid, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_id));
            param.put(ResponseKey.getInstace().token, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().token));
            param.put(ResponseKey.getInstace().type, "4");
            param.put(ResponseKey.getInstace().money_type, money_type);
            param.put(ResponseKey.getInstace().price, price);
            param.put(ResponseKey.getInstace().order_sn, order_sn);
            param.put(ResponseKey.getInstace().times, timestamp);
            ipresenter.onNotificationServer(param);
        } catch (Exception e) {
            ToastUtils.onToast(e.getMessage());
            e.printStackTrace();
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.arg1){
                case 1:
                    Map<String, String > result = (Map<String, String>) msg.obj;
                    onRechargeResult(result);
                    break;

            }
        }
    };

    /**
     * 充值数据返回
     * @param result
     */
    private void onRechargeResult(Map<String, String >result){
        PayResult payResult = new PayResult( result);
        /**
         对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
         */
        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
        String resultStatus = payResult.getResultStatus();
        // 判断resultStatus 为9000则代表支付成功
        if (TextUtils.equals(resultStatus, "9000")) {
            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
            ToastUtils.onToast("充值成功");
            SharedTools.getInstance(this).onPutData(ResponseKey.getInstace().is_vip, "1");
            onUpdatePage();
        } else {
            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
            ToastUtils.onToast(result.get("memo"));
        }
    }

    private void onUpdatePage(){

        String is_vip = SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().is_vip);
        if (is_vip.equals("1")){

            iv_member_upgrade.setVisibility(View.GONE);
            ll_layout_general.setVisibility(View.GONE);
            ll_layout_vip.setVisibility(View.VISIBLE);
        }else{
            iv_member_upgrade.setVisibility(View.VISIBLE);

            ll_layout_general.setVisibility(View.VISIBLE);
            ll_layout_vip.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetRechargeResponse(Map<String, Object> result) {



        AlipayTools.getInstance().onStartAlipay(this, result.get(ResponseKey.getInstace().data).toString(), handler);
    }

    @Override
    public void onNotificationResponse(Map<String, Object> result) {

        iv_member_upgrade.setClickable(false);
    }


}
