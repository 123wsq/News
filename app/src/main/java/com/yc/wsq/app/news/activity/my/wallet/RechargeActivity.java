package com.yc.wsq.app.news.activity.my.wallet;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pay.alipay.AlipayTools;
import com.example.pay.alipay.PayResult;
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
 * 充值页面
 */
public class RechargeActivity extends BaseActivity<RechargeView, UserPresenter<RechargeView>> implements RechargeView{



    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.ll_layout) LinearLayout ll_layout;
    @BindView(R.id.et_recharge_money) EditText et_recharge_money;

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
        return R.layout.layout_fragment_recharge;
    }

    @Override
    protected void initView() {

        tv_title.setText(getResources().getString(R.string.str_recharge_wallet_text));

        onEditChange();
    }

    @OnClick({R.id.ll_back, R.id.tv_next, R.id.ll_money_1, R.id.ll_money_10, R.id.ll_money_20, R.id.ll_money_30,
            R.id.ll_money_50, R.id.ll_money_100, R.id.ll_money_200, R.id.ll_money_500})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_money_1:
                et_recharge_money.setText("1");
                break;
            case R.id.ll_money_10:
                et_recharge_money.setText("10");
                break;
            case R.id.ll_money_20:
                et_recharge_money.setText("20");
                break;
            case R.id.ll_money_30:
                et_recharge_money.setText("30");
                break;
            case R.id.ll_money_50:
                et_recharge_money.setText("50");
                break;
            case R.id.ll_money_100:
                et_recharge_money.setText("100");
                break;
            case R.id.ll_money_200:
                et_recharge_money.setText("200");
                break;
            case R.id.ll_money_500:
                et_recharge_money.setText("500");
                break;
            case R.id.tv_next:
                try {
                    String amount = et_recharge_money.getText().toString().trim();
                    double am = Double.parseDouble(amount);
                    if (am == 0){
                        ToastUtils.onToast("请输入提现金额");
                        return;
                    }
                    onInitPopup();

                }catch (Exception e){
                    ToastUtils.onToast("请输入提现金额");
                    e.printStackTrace();
                }

                break;
        }
        et_recharge_money.setSelection(et_recharge_money.getText().length());
    }


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
        price =et_recharge_money.getText().toString().trim();
        try {
            param.put(ResponseKey.getInstace().uid, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_id));
            param.put(ResponseKey.getInstace().token, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().token));
            param.put(ResponseKey.getInstace().type, "3");
            param.put(ResponseKey.getInstace().money_type, money_type);
            param.put(ResponseKey.getInstace().price, price);
            ipresenter.onGetTradeDetails(param);
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
                case 2:
                    finish();
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
            ToastUtils.onToast("充值成功");

            String total_amount = SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_money);
            double amount1 = 0, amount2 = 0;;
            try {
                amount1 = Double.parseDouble(total_amount);
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                amount2 = Double.parseDouble(price);
            }catch (Exception e){
                e.printStackTrace();
            }
            SharedTools.getInstance(this).onPutData(ResponseKey.getInstace().user_money, (amount1+amount2)+"");


            Message message = new Message();
            message.arg1 =2;
            handler.sendMessageDelayed(message, 500);
        } else {
            ToastUtils.onToast(result.get("memo"));
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
            param.put(ResponseKey.getInstace().type, "3");
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

    @Override
    public void onGetRechargeResponse(Map<String, Object> result) {
//        int status = (int) result.get(ResponseKey.getInstace().rsp_status);
//        if (status == -1){
//            ToastUtils.onToast(result.get(ResponseKey.getInstace().rsp_msg).toString());
//            return;
//        }
//        Map<String, Object> data = (Map<String, Object>) result.get(ResponseKey.getInstace().data);
//        String app_Id = data.get(ResponseKey.getInstace().partner).toString();
//        String private_key = data.get(ResponseKey.getInstace().private_key).toString();
//        order_sn = data.get(ResponseKey.getInstace().order_sn).toString();
//        String price = data.get(ResponseKey.getInstace().price).toString();
//        title = data.get(ResponseKey.getInstace().title).toString();
//
//        //调用支付宝
//        AlipayTools.getInstance().onStartAlipay(getActivity(), app_Id,
//                private_key, order_sn,
//                price, title, "测试数据", handler);

        AlipayTools.getInstance().onStartAlipay(this, result.get(ResponseKey.getInstace().data).toString(), handler);
    }

    @Override
    public void onNotificationResponse(Map<String, Object> result) {

        finish();
    }

    private void onEditChange(){
        et_recharge_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String data = s.toString();
                if (data.startsWith(".")){
                    et_recharge_money.setText("0.");
                    et_recharge_money.setSelection(et_recharge_money.getText().length());
                }

                if (data.indexOf(".")>1){
                    data.substring(0, data.length()-1);
                    et_recharge_money.setSelection(et_recharge_money.getText().length());
                }
            }
        });
    }
}
