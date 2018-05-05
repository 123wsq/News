package com.yc.wsq.app.news.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.ISBNParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;
import com.google.zxing.client.result.ProductParsedResult;
import com.google.zxing.client.result.TextParsedResult;
import com.google.zxing.client.result.URIParsedResult;
import com.mylhyl.zxing.scanner.OnScannerCompletionListener;
import com.mylhyl.zxing.scanner.ScannerOptions;
import com.mylhyl.zxing.scanner.ScannerView;
import com.mylhyl.zxing.scanner.common.Scanner;
import com.mylhyl.zxing.scanner.result.AddressBookResult;
import com.mylhyl.zxing.scanner.result.ISBNResult;
import com.mylhyl.zxing.scanner.result.ProductResult;
import com.mylhyl.zxing.scanner.result.URIResult;
import com.orhanobut.logger.Logger;
import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.mvp.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

public class QRcodeScanActivity extends BaseActivity{

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.scanner_view) ScannerView scanner_view;

    private ProgressDialog progressDialog;

    boolean showThumbnail = false;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_activity_scan;
    }

    @Override
    protected void initView() {

        tv_title.setText(getString(R.string.str_qrcode_scan_text));
        onRequestPermission();
        onScanListener();
    }

    @OnClick({R.id.ll_back})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
        }
    }
    private void onScanListener(){

        ScannerOptions.Builder builder = new ScannerOptions.Builder();
        builder.setFrameSize(256, 256)
                .setFrameCornerLength(22)
                .setFrameCornerWidth(2)
                .setFrameCornerColor(0xff06c1ae)
                .setFrameCornerInside(true)

//                .setLaserLineColor(0xff06c1ae)
//                .setLaserLineHeight(18)

//                .setLaserStyle(ScannerOptions.LaserStyle.RES_LINE,R.mipmap.wx_scan_line)

                .setLaserStyle(ScannerOptions.LaserStyle.RES_GRID, R.mipmap.zfb_grid_scan_line)//网格图
                .setFrameCornerColor(0xFF26CEFF)//支付宝颜色

                .setScanFullScreen(true)

//                .setFrameHide(false)
                .setFrameCornerHide(false)
//                .setLaserMoveFullScreen(false)

//                .setViewfinderCallback(new ScannerOptions.ViewfinderCallback() {
//                    @Override
//                    public void onDraw(View view, Canvas canvas, Rect frame) {
//                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.connect_logo);
//                        canvas.drawBitmap(bmp, frame.right / 2, frame.top - bmp.getHeight(), null);
//                    }
//                })

                .setScanMode(BarcodeFormat.QR_CODE)
                .setTipText("请将二维码至于扫描框中")
                .setTipTextSize(14)
                .setTipTextColor(getResources().getColor(R.color.colorAccent));


            scanner_view.setScannerOptions(builder.build());
                scanner_view.setOnScannerCompletionListener(new OnScannerCompletionListener() {
            /**
             * 扫描成功后将调用
             *
             * @param rawResult    扫描结果
             * @param parsedResult 结果类型
             * @param barcode      扫描后的图像
             */
            @Override
            public void onScannerCompletion(Result rawResult, ParsedResult parsedResult, Bitmap barcode) {


                Intent intent = new Intent();
                final Bundle bundle = new Bundle();
                ParsedResultType type = parsedResult.getType();
                switch (type) {
                    case ADDRESSBOOK:  //联系人
                        AddressBookParsedResult addressBook = (AddressBookParsedResult) parsedResult;
                        bundle.putSerializable(Scanner.Scan.RESULT, new AddressBookResult(addressBook));
                        break;
                    case PRODUCT:
                        ProductParsedResult product = (ProductParsedResult) parsedResult;
                        bundle.putSerializable(Scanner.Scan.RESULT, new ProductResult(product));
                        break;
                    case ISBN:
                        ISBNParsedResult isbn = (ISBNParsedResult) parsedResult;
                        bundle.putSerializable(Scanner.Scan.RESULT, new ISBNResult(isbn));
                        break;
                    case URI:
                        URIParsedResult uri = (URIParsedResult) parsedResult;
//                        bundle.putSerializable(Scanner.Scan.RESULT, new URIResult(uri));
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(uri.getURI());
                        intent.setData(content_url);
                        startActivity(intent);
                        break;
                    case TEXT:
                        ToastUtils.onToast(rawResult.getText());
                        TextParsedResult textParsedResult = (TextParsedResult) parsedResult;
                        bundle.putString(Scanner.Scan.RESULT, textParsedResult.getText());
                        break;
                    case GEO:
                        break;
                    case TEL:
                        break;
                    case SMS:
                        break;
                }
                vibrate();
                scanner_view.restartPreviewAfterDelay(500);
//                showProgressDialog();
//                if (showThumbnail) {
//                    onResultActivity(rawResult, type, bundle);
//                } else {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            onResultActivity(rawResult, type, bundle);
//                        }
//                    }, 3 * 1000);
//                }

            }
        });
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
    /**
     * 网络请求权限
     */
    private void onRequestPermission(){

        List<PermissionItem> permissions = new ArrayList<PermissionItem>();
        permissions.add(new PermissionItem(Manifest.permission.CAMERA, "手机权限", R.drawable.permission_ic_phone));
        HiPermission.create(this).permissions(permissions).checkMutiPermission(new PermissionCallback() {
            @Override
            public void onClose() {
                Logger.d("用户关闭权限申请");
                finish();
            }

            @Override
            public void onFinish() {
                Logger.d("所有权限申请完成");


            }

            @Override
            public void onDeny(String permission, int position) {

            }

            @Override
            public void onGuarantee(String permission, int position) {

            }
        });
    }

    void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("请稍候...");
        progressDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scanner_view.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanner_view.onPause();
    }
}
