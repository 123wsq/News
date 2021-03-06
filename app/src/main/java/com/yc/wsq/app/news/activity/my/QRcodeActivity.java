package com.yc.wsq.app.news.activity.my;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.mylhyl.zxing.scanner.encode.QREncode;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.constant.Urls;
import com.yc.wsq.app.news.mvp.presenter.BasePresenter;

import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.tools.SharedTools;

import butterknife.BindView;
import butterknife.OnClick;


public class QRcodeActivity extends BaseActivity{


    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_qcode)
    ImageView iv_qcode;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_qrcode;
    }

    @OnClick({R.id.ll_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    protected void initView() {

        tv_title.setText(getString(R.string.str_share_qrcode_text));

//        String contactUri= "https://www.pgyer.com/pMWs";
        String contactUri = Urls.HOST +Urls.SET_INVITE+"?first_leader="+ SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_id)+"&user_id=";
//        Bitmap bitmap = QREncode.encodeQR(new QREncode.Builder(getActivity())
//                .setParsedResultType(ParsedResultType.URI)
//                .setContents(contactUri).build());

        QREncode.Builder builder = new QREncode.Builder(this);
        builder.setContents(contactUri);
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.image_round_app_icon);
        builder.setLogoBitmap(logo);
        Bitmap bitmap = builder.build().encodeAsBitmap();

        iv_qcode.setImageBitmap(bitmap);
    }
}
