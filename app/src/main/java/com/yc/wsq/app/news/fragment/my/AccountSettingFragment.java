package com.yc.wsq.app.news.fragment.my;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.orhanobut.logger.Logger;
import com.wsq.library.tools.DialogTools;
import com.wsq.library.views.alertdialog.CustomDefaultDialog;
import com.wsq.library.views.alertdialog.OnDialogClickListener;
import com.wsq.library.views.view.CustomPopup;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.presenter.BasePresenter;
import com.yc.wsq.app.news.tools.SharedTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * 账户设置页面
 */
public class AccountSettingFragment extends BaseFragment{

    public static final String TAG = AccountSettingFragment.class.getName();
    public static final String INTERFACE_BACK_ALL = TAG + "BACK_MYFRAGMENT";
    public static final int RESULT_IMAGE = 2022;

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_account) TextView tv_account;
    @BindView(R.id.tv_nickname) TextView tv_nickname;
    @BindView(R.id.ll_layout) LinearLayout ll_layout;

    private CustomPopup popup;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_account_setting;
    }

    @Override
    protected void initView() {

        tv_title.setText(getString(R.string.str_setting_account_text));
        SharedTools shared= SharedTools.getInstance(getActivity());
        tv_account.setText(shared.onGetString(ResponseKey.getInstace().mobile));
        tv_nickname.setText(shared.onGetString(ResponseKey.getInstace().nickname));
    }

    @OnClick({R.id.ll_back, R.id.tv_exit_account, R.id.ll_header, R.id.ll_nickname})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.ll_back:
                mFunctionsManage.invokeFunction(INTERFACE_BACK);
                break;
            case R.id.ll_header:

                onCameraPopup();
                break;
            case R.id.tv_exit_account:
                SharedTools.getInstance(getActivity()).onPutData(ResponseKey.getInstace().nickname, "");
                SharedTools.getInstance(getActivity()).onPutData(ResponseKey.getInstace().mobile, "");
                SharedTools.getInstance(getActivity()).onPutData(ResponseKey.getInstace().token, "");
                SharedTools.getInstance(getActivity()).onPutData(ResponseKey.getInstace().head_pic, "");
                mFunctionsManage.invokeFunction(INTERFACE_BACK_ALL);
                break;
            case R.id.ll_nickname:
                onUpdateNickName();
                break;
        }
    }

    /**
     * 修改昵称
     */
    private void onUpdateNickName(){
        DialogTools.showDialog(getActivity(), "提交", "取消", "修改昵称", "", false, new OnDialogClickListener() {
            @Override
            public void onClick(CustomDefaultDialog customDefaultDialog, String s, int i) {

                customDefaultDialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
    }

    /***
     * 照片相册选择Popup
     */
    private void onCameraPopup(){
        List<String> list = new ArrayList<>();
        list.add("相册");
        list.add("拍照");
        popup = new CustomPopup(getActivity(), list, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }, new CustomPopup.PopupItemListener() {
            @Override
            public void onClickItemListener(int i, String s) {

                onRequestPermission(i);

            }
        });
        popup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER, 0, 0);
    }

    /**
     * 网络请求
     * @param position
     */
    private void onRequestPermission(final int position){//READ_PHONE_STATE

        List<PermissionItem> permissions = new ArrayList<PermissionItem>();
        permissions.add(new PermissionItem(Manifest.permission.CALL_PHONE, "手机权限", R.drawable.permission_ic_phone));
        HiPermission.create(getActivity()).permissions(permissions).checkMutiPermission(new PermissionCallback() {
            @Override
            public void onClose() {
                Logger.d("用户关闭权限申请");
            }

            @Override
            public void onFinish() {
                Logger.d("所有权限申请完成");
//                Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+result));
//                startActivity(intent);
                onOpenCamera(position);
                popup.dismiss();

            }

            @Override
            public void onDeny(String permission, int position) {

            }

            @Override
            public void onGuarantee(String permission, int position) {

            }
        });
    }

    /**
     * 打开图片选择
     * @param position
     */
    private void onOpenCamera(int position){
        switch (position){
            case 0:
                PictureSelector.create(getActivity())
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)

                        .isZoomAnim(true)
                        .imageSpanCount(3)
                        .isCamera(false)
                        .previewImage(true)
                        .compress(true)// 是否压缩 true or false
                        .minimumCompressSize(50)// 小于100kb的图片不压缩
                        .forResult(RESULT_IMAGE);
                break;
            case 1:
                PictureSelector.create(getActivity())
                        .openCamera(PictureMimeType.ofImage())
                        .imageFormat(PictureMimeType.JPEG)
                        .compress(true)// 是否压缩 true or false
                        .minimumCompressSize(50)// 小于100kb的图片不压缩
                        .forResult(RESULT_IMAGE);
                break;
        }
    }

    public void onChangeHeader(String path){

        Logger.d(path);
    }
}
