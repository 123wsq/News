package com.yc.wsq.app.news.fragment.my.setting;

import android.Manifest;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
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
import com.yc.wsq.app.news.views.popup.CalendarPopup;
import com.yc.wsq.app.news.views.listener.OnCalendarResultCallBack;

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
public class AccountSettingFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    public static final String TAG = AccountSettingFragment.class.getName();
    public static final String INTERFACE_BACK_ALL = TAG + "BACK_MYFRAGMENT";
    public static final String INTERFACE_EXIT_APP = TAG + "EXIT_APP";
    public static final int RESULT_IMAGE = 2022;

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_account) TextView tv_account;
    @BindView(R.id.tv_nickname) TextView tv_nickname;
    @BindView(R.id.ll_layout) LinearLayout ll_layout;
    @BindView(R.id.rg_sex) RadioGroup rg_sex;
    @BindView(R.id.rb_sex_boy) RadioButton rb_sex_boy;
    @BindView(R.id.rb_sex_lady) RadioButton rb_sex_lady;

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
        String sex = SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().sex);

        if (sex.equals("0")){
            rb_sex_boy.setChecked(true);
        }else {
            rb_sex_lady.setChecked(true);
        }

        rg_sex.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.ll_back, R.id.tv_exit_account, R.id.ll_header, R.id.ll_nickname, R.id.ll_birthday})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.ll_back:
                mFunctionsManage.invokeFunction(INTERFACE_BACK);
                break;
            case R.id.ll_header:

//                onCameraPopup();
                break;
            case R.id.tv_exit_account:
                SharedTools.getInstance(getActivity()).onClearUser();
                mFunctionsManage.invokeFunction(INTERFACE_BACK_ALL);
                break;
            case R.id.ll_nickname:
//                onUpdateNickName();
                break;
            case R.id.ll_birthday:
//                onUpdateBirthday();
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

                popup.dismiss();
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
     * 请求权限
     * @param position
     */
    private void onRequestPermission(final int position){//READ_PHONE_STATE

        List<PermissionItem> permissions = new ArrayList<PermissionItem>();
        permissions.add(new PermissionItem(Manifest.permission.CAMERA, "照相机", R.drawable.permission_ic_camera));
        permissions.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "SD卡", R.drawable.permission_ic_storage));
        HiPermission.create(getActivity()).permissions(permissions).checkMutiPermission(new PermissionCallback() {
            @Override
            public void onClose() {
                Logger.d("用户关闭权限申请");
            }

            @Override
            public void onFinish() {
                Logger.d("所有权限申请完成");

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


    private void onUpdateBirthday(){

        CalendarPopup calendarPopup =  new CalendarPopup(getActivity(), CalendarPopup.type_calendar, new OnCalendarResultCallBack() {
            @Override
            public void onCallBack(int year, int month, int day, int hour, int minute) {

            }
        });
        calendarPopup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER, 0, 0);
    }


    public void onChangeHeader(String path){

        Logger.d(path);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_sex_boy:

                break;
            case R.id.rb_sex_lady:

                break;
        }
    }


}
