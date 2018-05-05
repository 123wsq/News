package com.yc.wsq.app.news.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.wsq.library.struct.FunctionNoParamNoResult;
import com.wsq.library.struct.FunctionWithParamOnly;
import com.wsq.library.struct.FunctionsManage;
import com.wsq.library.tools.DialogTools;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.fragment.MainFragment;
import com.yc.wsq.app.news.fragment.benefit.BenefitDetailsFragment;
import com.yc.wsq.app.news.fragment.my.AboutFragment;
import com.yc.wsq.app.news.fragment.my.setting.AccountSettingFragment;
import com.yc.wsq.app.news.fragment.my.CollectFragment;
import com.yc.wsq.app.news.fragment.my.CommentFragment;
import com.yc.wsq.app.news.fragment.my.DiscountFragment;
import com.yc.wsq.app.news.fragment.my.FocusFragment;
import com.yc.wsq.app.news.fragment.my.feedback.HelpFragment;
import com.yc.wsq.app.news.fragment.my.HistoryFragment;
import com.yc.wsq.app.news.fragment.my.IntegralFragment;
import com.yc.wsq.app.news.fragment.my.MemberUpgradeFragment;
import com.yc.wsq.app.news.fragment.my.MessageFragment;
import com.yc.wsq.app.news.fragment.my.wallet.RechargeFragment;
import com.yc.wsq.app.news.fragment.my.setting.SettingFragment;
import com.yc.wsq.app.news.fragment.my.wallet.TradeRecordFragment;
import com.yc.wsq.app.news.fragment.my.wallet.WalletFragment;
import com.yc.wsq.app.news.fragment.my.wallet.WithdrawFragment;
import com.yc.wsq.app.news.fragment.news.NewsDetailsFragment;
import com.yc.wsq.app.news.fragment.news.SearchFragment;
import com.yc.wsq.app.news.fragment.news.SearchResultFragment;
import com.yc.wsq.app.news.fragment.tab.BenefitFragment;
import com.yc.wsq.app.news.fragment.tab.HomeFragment;
import com.yc.wsq.app.news.fragment.tab.MyFragment;
import com.yc.wsq.app.news.fragment.tab.ShopFragment;
import com.yc.wsq.app.news.mvp.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private FragmentManager fragmentManager;
    private List<Fragment> mListFragment;
    private Fragment curFragment;
    public static final int LOGIN_REQUEST_CODE =2001;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mListFragment = new ArrayList<>();
        fragmentManager = getSupportFragmentManager();
        onEnter(new MainFragment(), MainFragment.TAG, false);

    }


    /**
     *
     * @param fragment
     * @param tag
     * @param isBack  是否支持返回
     */
    private void onEnter( Fragment fragment, String tag, boolean isBack){
        onEnter(fragment, tag, null, isBack);
    }

    /**
     *
     * @param fragment
     * @param tag
     * @param param 传递的参数
     * @param isBack  是否支持返回
     */
    private void onEnter(Fragment fragment, String tag, Bundle param, boolean isBack){
        FragmentTransaction fTransaction = fragmentManager.beginTransaction();

        if (curFragment != null) fTransaction.hide(curFragment);

        if (!fragment.isAdded()) {
            if (param != null) fragment.setArguments(param);

            mListFragment.add(fragment);
            fTransaction.add(R.id.layout_content, fragment, tag);
            if (isBack)fTransaction.addToBackStack(tag);
            fTransaction.show(fragment).commit();
        } else {
            fTransaction.show(fragment).commit();
        }
        curFragment = fragment;
    }

    public void setFunctionsForFragment(final String tag) {

        FragmentManager fm = getSupportFragmentManager();
        final BaseFragment fragment = (BaseFragment) fm.findFragmentByTag(tag);

        FunctionsManage functionsManage = FunctionsManage.getInstance();

        functionsManage.addFunction(new FunctionWithParamOnly<Integer>(HomeFragment.INTERFACE_WITHP) {
            @Override
            public void function(Integer position) {
                switch (position){
                    case -1:
                        onEnter(new SearchFragment(), SearchFragment.TAG, true);
                        break;
                    default:
                            onEnter(new NewsDetailsFragment(), NewsDetailsFragment.TAG, true);
                        break;
                }

            }
        });

        functionsManage.addFunction(new FunctionWithParamOnly<String>(HomeFragment.INTERFACE_WITHPS) {
            @Override
            public void function(String data) {

                Bundle bundle = new Bundle();
                bundle.putString(ResponseKey.getInstace().article_id, data);
                onEnter(new NewsDetailsFragment(), NewsDetailsFragment.TAG, bundle, true);
            }
        });
        functionsManage.addFunction(new FunctionWithParamOnly<Integer>(MyFragment.INTERFACE_WITHP) {
            @Override
            public void function(Integer data) {

                switch (data){
                    case 1:
                        onEnter(new WalletFragment(), WalletFragment.TAG, true);
                        break;
                    case 2:
                        onEnter(new IntegralFragment(), IntegralFragment.TAG, true);
                        break;
                    case 3:
                        onEnter(new MemberUpgradeFragment(), MemberUpgradeFragment.TAG, true);
                        break;
                    case 4:
                        startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), LOGIN_REQUEST_CODE);
                        break;
                    case 5:
                        onEnter(new AboutFragment(), AboutFragment.TAG, true);
                        break;
                    case 6:
                        onEnter(new SettingFragment(), SettingFragment.TAG, true);
                        break;
                    case 7:
                        onEnter(new CollectFragment(), CollectFragment.TAG, true);
                        break;
                    case 8:
                        onEnter(new HistoryFragment(), HistoryFragment.TAG, true);
                        break;
                    case 9:
                        onEnter(new CommentFragment(), CommentFragment.TAG, true);
                        break;
                    case 10:
                        onEnter(new MessageFragment(), MessageFragment.TAG, true);
                        break;
                    case 11:
                        onEnter(new FocusFragment(), FocusFragment.TAG, true);
                        break;
                    case 12:
                        onEnter(new DiscountFragment(), DiscountFragment.TAG, true);
                        break;
                    case 13:
                        onEnter(new HelpFragment(), HelpFragment.TAG, true);
                        break;
                }
            }
        });
        functionsManage.addFunction(new FunctionWithParamOnly<String>(SearchFragment.INTERFACE_WITHP) {
            @Override
            public void function(String data) {
                onKeyBack();
                Bundle bundle = new Bundle();
                bundle.putString(ResponseKey.getInstace().keywords, data);
                onEnter(new SearchResultFragment(), SearchResultFragment.TAG, bundle, true);

            }
        });
        functionsManage.addFunction(new FunctionWithParamOnly<Integer>(WalletFragment.INTERFACE_WITHP) {
            @Override
            public void function(Integer data) {
                    switch (data){
                        case 1:
                            onEnter(new RechargeFragment(), RechargeFragment.TAG, true);
                            break;
                        case 2:
                            onEnter(new WithdrawFragment(), WithdrawFragment.TAG, true);
                            break;
                        case 3:
                            onEnter(new TradeRecordFragment(), TradeRecordFragment.TAG, true);
                            break;
                    }
            }
        });
        functionsManage.addFunction(new FunctionWithParamOnly<Integer>(SettingFragment.INTEFACE_WITHP) {
            @Override
            public void function(Integer data) {
                switch (data){
                    case 1:
                        onEnter(new AccountSettingFragment(), AccountSettingFragment.TAG, true);
                        break;
                }
            }
        });
        functionsManage.addFunction(new FunctionNoParamNoResult(AccountSettingFragment.INTERFACE_BACK_ALL) {
            @Override
            public void function() {

                for (int i = 0; i < mListFragment.size(); i++) {

                    if (mListFragment.get(i) instanceof MainFragment){
                        MainFragment mf = (MainFragment) mListFragment.get(i);
                        //用户状态发生变化
                        mf.onUserStatusChange();
                    }
                }

                onBackAllFragment();
            }
        });

        functionsManage.addFunction(new FunctionWithParamOnly<String>(BenefitFragment.INTERFACE_WITHP) {
            @Override
            public void function(String data) {

                Bundle bundle = new Bundle();
                bundle.putString(ResponseKey.getInstace().id, data);
                onEnter(new BenefitDetailsFragment(), BenefitDetailsFragment.TAG, bundle, true);
            }
        });

        functionsManage.addFunction(new FunctionNoParamNoResult(ShopFragment.INTERFACE_NRNP) {
            @Override
            public void function() {
                for (int i = 0; i < mListFragment.size(); i++) {

                    if (mListFragment.get(i) instanceof MainFragment){
                        MainFragment mf = (MainFragment) mListFragment.get(i);
                        //用户状态发生变化
                        mf.onSetShowPosition();
                    }
                }
            }
        });

        /**
         * 返回按钮的事件监听
         */
        functionsManage.addFunction(new FunctionNoParamNoResult(BaseFragment.INTERFACE_BACK) {
            @Override
            public void function() {
                onKeyBack();
            }
        });
        /**
         * 退出应用
         */
        functionsManage.addFunction(new FunctionNoParamNoResult(AccountSettingFragment.INTERFACE_EXIT_APP) {
            @Override
            public void function() {
                finish();
            }
        });
        fragment.setFunctionsManager(functionsManage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case LOGIN_REQUEST_CODE:
                for (int i = 0; i < mListFragment.size(); i++) {

                    if (mListFragment.get(i) instanceof MainFragment){
                        MainFragment mf = (MainFragment) mListFragment.get(i);
                        //用户状态发生变化
                        mf.onUserStatusChange();
                    }
                }

                break;
        }
        switch (resultCode){
            case AccountSettingFragment.RESULT_IMAGE:
                for (int i = 0; i < mListFragment.size(); i++) {

                    if (mListFragment.get(i) instanceof  AccountSettingFragment){
                        AccountSettingFragment mf = (AccountSettingFragment) mListFragment.get(i);
                        //用户状态发生变化
                        List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                        mf.onChangeHeader(selectList.get(0).getCompressPath());
                    }
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                onKeyBack();
                break;

        }
        return true;
    }

    private void onKeyBack(){

        fragmentManager.popBackStack();
        if (mListFragment.size() > 1) {
            mListFragment.remove(mListFragment.size() - 1);
            curFragment = mListFragment.get(mListFragment.size()-1);
        }else{
            onExitAppDialog();
        }
    }
    private void onBackAllFragment(){

        for (int i = mListFragment.size()-1; i >0; i--) {
            onKeyBack();
        }
    }

    private void onExitAppDialog(){
        DialogTools.showDialog(this, "离开", "不了","提示", "您确定要离我们而去吗？",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

              dialog.dismiss();
                finish();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
    }


}
