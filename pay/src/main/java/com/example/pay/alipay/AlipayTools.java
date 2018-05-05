package com.example.pay.alipay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.example.pay.inter.OnPayListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlipayTools {

    private static AlipayTools alipayTools;

    private AlipayTools(){

    }
    public static AlipayTools getInstance(){

        if (alipayTools == null){
            synchronized (AlipayTools.class){
                if (alipayTools == null){
                    alipayTools = new AlipayTools();
                }
            }
        }
        return alipayTools;
    }

    public void onStartAlipay(final Activity context, String appID, String privateKey, String tradeNo, String tradeAmount, String goodsTitle, String tradeBody, final Handler handler){

        final Map<String, String> params = onBuildOrderParamMap(appID, tradeNo, tradeAmount, goodsTitle, tradeBody);
        String orderParam = buildOrderParam(params);
        String sign = getSign(params, privateKey, true);
        final String orderInfo = orderParam + "&" + sign;
        Log.i("支付信息", orderInfo);
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {

                PayTask alipay = new PayTask(context);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                result.put("timestamp", params.get("timestamp"));
                Log.i("msp", result.toString());



                Message msg = new Message();
                msg.arg1 =1;
                msg.obj = result;
                handler.sendMessage(msg);


            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    public void onStartAlipay(final Activity context, String body, final Handler handler){

//        final Map<String, String> params = onBuildOrderParamMap(appID, tradeNo, tradeAmount, goodsTitle, tradeBody);
//        String orderParam = buildOrderParam(params);
//        String sign = getSign(params, privateKey, true);
        final String orderInfo = body;
        final Map<String, String> params = new HashMap<>();
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {

                PayTask alipay = new PayTask(context);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                result.put("timestamp", params.get("timestamp"));
                Log.i("msp", result.toString());



                Message msg = new Message();
                msg.arg1 =1;
                msg.obj = result;
                handler.sendMessage(msg);


            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    /**
     *
     * @param appId
     * @param tradeNo
     * @param tradeAmount
     * @param goodsTitle
     * @param tradeBody
     * @return
     */
    private Map<String, String> onBuildOrderParamMap(String appId, String tradeNo, String tradeAmount, String goodsTitle, String tradeBody){

        Map<String, String> keyValues = new HashMap<String, String>();

//        JSONObject keyValues = new JSONObject();
            //支付宝分配给开发者的应用ID
            keyValues.put("app_id", appId);
//            keyValues.put("notify_url","http://api.yczmj.cn/user/chongzhiback");
            //业务请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
            keyValues.put("biz_content", onBuildTrade(tradeNo, tradeAmount, goodsTitle, tradeBody));
            //请求使用的编码格式，如utf-8,gbk,gb2312等
            keyValues.put("charset", "utf-8");
            //接口名称
            keyValues.put("method", "alipay.trade.app.pay");
            //商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
            keyValues.put("sign_type", "RSA2");

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = format.format(new Date());
            keyValues.put("timestamp", timestamp);
            keyValues.put("version", "1.0");

        return keyValues;
    }

    /**
     *
     * @param tradeNo
     * @param tradeAmount
     * @param goodsTitle
     * @param tradeBody
     * @return
     */
    private String onBuildTrade(String tradeNo, String tradeAmount, String goodsTitle, String tradeBody){
        JSONObject json = new JSONObject();
        try {
            //该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。
            // 该参数数值不接受小数点， 如 1.5h，可转换为 90m。注：若为空，则默认为15d。
            json.put("timeout_express","30m");
            //销售产品码，商家和支付宝签约的产品码，为固定值
            json.put("product_code","QUICK_MSECURITY_PAY");
            //订单总金额，单位为元，精确到小数点后两位，取值范围
            json.put("total_amount", tradeAmount);
            //商品的标题/交易标题/订单标题/订单关键字等。
            json.put("subject", goodsTitle);
            //对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
            json.put("body", tradeBody);
            //商户网站唯一订单号
            json.put("out_trade_no", tradeNo);
        }catch (Exception e){
            e.printStackTrace();
        }

        return json.toString();
    }

    /**
     *
     * @param map
     * @return
     */
    private String buildOrderParam(Map<String, String> map) {
        List<String> keys = new ArrayList<String>(map.keySet());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));

        return sb.toString();
    }

    /**
     * 对支付参数信息进行签名
     *
     * @param map
     *    待签名授权信息
     *
     * @return
     */
    private String getSign(Map<String, String> map, String rsaKey, boolean rsa2) {
        List<String> keys = new ArrayList<String>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value, false));
            authInfo.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));

        String oriSign = SignUtils.sign(authInfo.toString(), rsaKey, rsa2);
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }
}
