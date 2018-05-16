package com.yc.wsq.app.news.activity.my.setting;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;
import com.wsq.library.tools.DialogTools;
import com.wsq.library.views.alertdialog.CustomDefaultDialog;
import com.wsq.library.views.view.CustomPopup;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.activity.MainActivity;
import com.yc.wsq.app.news.activity.my.wallet.ValidateAccountActivity;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.constant.Urls;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UpdateUserInfoView;
import com.yc.wsq.app.news.tools.SharedTools;
import com.yc.wsq.app.news.views.popup.CalendarPopup;
import com.yc.wsq.app.news.views.listener.OnCalendarResultCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * 账户设置页面
 */
public class AccountSettingActivity extends BaseActivity<UpdateUserInfoView, UserPresenter<UpdateUserInfoView>> implements UpdateUserInfoView{


    public static final int RESULT_IMAGE = 2022;

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_account) TextView tv_account;
    @BindView(R.id.tv_nickname) TextView tv_nickname;
    @BindView(R.id.tv_birthday) TextView tv_birthday;
    @BindView(R.id.ll_layout) LinearLayout ll_layout;
    @BindView(R.id.iv_header) CircleImageView iv_header;

    @BindView(R.id.tv_sex_secrecy) TextView tv_sex_secrecy;

    private CustomPopup popup;


    @Override
    protected UserPresenter<UpdateUserInfoView> createPresenter() {
        return new UserPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_account_setting;
    }

    @Override
    protected void initView() {

        tv_title.setText(getString(R.string.str_setting_account_text));
        SharedTools shared= SharedTools.getInstance(this);
        tv_account.setText(shared.onGetString(ResponseKey.getInstace().mobile));
        tv_nickname.setText(shared.onGetString(ResponseKey.getInstace().nickname));
        String sex = SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().sex);
        tv_birthday.setText(shared.onGetString(ResponseKey.getInstace().birthday));
        onShowHeader(Urls.HOST + SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().head_pic));

        if (sex.equals("0")){
            tv_sex_secrecy.setText("保密");
        }else if(sex.equals("1")){
            tv_sex_secrecy.setText("男");
        }else if(sex.equals("2")){
            tv_sex_secrecy.setText("女");
        }

    }

    @OnClick({R.id.ll_back, R.id.tv_exit_account, R.id.ll_header, R.id.ll_nickname, R.id.ll_birthday,
            R.id.ll_setting_withdraw_password, R.id.ll_setting_withdraw_account, R.id.ll_selector_sex,
            R.id.ll_setting_login_password, R.id.iv_header})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_header:

                onCameraPopup();
                break;
            case R.id.iv_header:
                List<LocalMedia> list = new ArrayList<>();
                LocalMedia media = new LocalMedia();
                media.setPath(Urls.HOST + SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().head_pic));
                list.add(media);
                PictureSelector.create(this).externalPicturePreview(0, list);
                break;
            case R.id.tv_exit_account:
                onExitApp();
                break;
            case R.id.ll_nickname:
                onUpdateNickName();
                break;
            case R.id.ll_birthday:
                onUpdateBirthday();
                break;
            case R.id.ll_selector_sex:
                onSelectorSexPopup();
                break;
            case R.id.ll_setting_withdraw_password: //设置提现密码 param =1
                startActivity(new Intent(this, ValidateAccountActivity.class));

                break;
            case R.id.ll_setting_withdraw_account:  //提现账户设置 param =2
                startActivity(new Intent(this, AccountCreditedActivity.class));
                break;
            case R.id.ll_setting_login_password:  //修改登录密码 param = 3
                startActivity(new Intent(this, UpdatePasswordActivity.class));
                break;
        }
    }

    private void onExitApp(){
        Map<String, String> param = new HashMap<>();

        try {
            ipresenter.onUserLogOut(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改昵称
     */
    private void onUpdateNickName(){
        DialogTools.showDialog(this, "提交", "取消", "修改昵称", "", false,
                new com.wsq.library.views.alertdialog.OnDialogClickListener() {
            @Override
            public void onClick(CustomDefaultDialog customDefaultDialog, String s, int i) {

                onUpdateUserInfo(ResponseKey.getInstace().nickname, s);
                tv_nickname.setText(s);
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
        popup = new CustomPopup(this, list, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popup.dismiss();
            }
        }, new CustomPopup.PopupItemListener() {
            @Override
            public void onClickItemListener(int i, String s) {

                onRequestPermission(i);

                popup.dismiss();
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
        HiPermission.create(this).permissions(permissions).checkMutiPermission(new PermissionCallback() {
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
                PictureSelector.create(this)
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
                PictureSelector.create(this)
                        .openCamera(PictureMimeType.ofImage())
                        .imageFormat(PictureMimeType.JPEG)
                        .compress(true)// 是否压缩 true or false
                        .minimumCompressSize(50)// 小于100kb的图片不压缩
                        .forResult(RESULT_IMAGE);
                break;
        }
    }


    private void onUpdateBirthday(){

        CalendarPopup calendarPopup =  new CalendarPopup(this, CalendarPopup.type_calendar, new OnCalendarResultCallBack() {
            @Override
            public void onCallBack(int year, int month, int day, int hour, int minute) {

                onUpdateUserInfo(ResponseKey.getInstace().birthday,year+"-"+month+"-"+day);
                tv_birthday.setText(year+"-"+month+"-"+day);
            }
        });
        calendarPopup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER, 0, 0);
    }


    private void onSelectorSexPopup(){
        List<String> list = new ArrayList<>();
        list.add("保密");
        list.add("男");
        list.add("女");
        popup = new CustomPopup(this, list, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popup.dismiss();
            }
        }, new CustomPopup.PopupItemListener() {
            @Override
            public void onClickItemListener(int i, String s) {

                tv_sex_secrecy.setText(s);
            onUpdateUserInfo(ResponseKey.getInstace().sex, i+"");
            popup.dismiss();
            }
        });
        popup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER, 0, 0);
    }

    /**
     * 修改用户头像
     * @param path
     */
    public void onChangeHeader(String path){

        onShowHeader(path);
        Map<String, String> param = new HashMap<>();
        try {
            param.put(ResponseKey.getInstace().user_id, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_id));
            ipresenter.onUploadUserHeader(path, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onShowHeader(String path){
        RequestOptions options = new RequestOptions();
        options.error(R.mipmap.image_header_default);
        options.circleCrop();
        Glide.with(this)
                .load(path)
                .apply(options)
                .into(iv_header);
    }


    private void onUpdateUserInfo(String key, String value){

        Map<String, String> param = new HashMap<>();
        param.put(key, value);
        try {
            param.put(ResponseKey.getInstace().user_id, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_id));
            ipresenter.onUpdateAccountInfo(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_IMAGE:
                    // 图片选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    if (selectList.size() != 0)
                   onChangeHeader(selectList.get(0).getCompressPath());
                    break;
            }
        }
    }

    @Override
    public void onUserLogOutResponseData(Map<String, Object> result) {
        SharedTools.getInstance(this).onClearUser();

        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onUpdateHeaderResponseData(Map<String, Object> result) {

        String msg = (String) result.get(ResponseKey.getInstace().rsp_msg);
        onShowDialog("提示",msg, null);
    }

    @Override
    public void onUpdateUserResponseData(Map<String, Object> result) {
        String msg = (String) result.get(ResponseKey.getInstace().rsp_msg);
        onShowDialog("提示",msg, null);

    }
}
