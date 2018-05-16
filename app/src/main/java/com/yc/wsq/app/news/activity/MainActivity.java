package com.yc.wsq.app.news.activity;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wsq.library.tools.DialogTools;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.fragment.tab.BenefitFragment;
import com.yc.wsq.app.news.fragment.tab.HomeFragment;
import com.yc.wsq.app.news.fragment.tab.MyFragment;
import com.yc.wsq.app.news.fragment.tab.ShopFragment;
import com.yc.wsq.app.news.mvp.presenter.BasePresenter;
import com.yc.wsq.app.news.tools.ViewSize;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.rg_buttom_menu) RadioGroup rg_buttom_menu;
    @BindView(R.id.rb_home) RadioButton rb_home;
    @BindView(R.id.rb_shop) RadioButton rb_shop;
    @BindView(R.id.rb_benefit) RadioButton rb_benefit;
    @BindView(R.id.rb_my) RadioButton rb_my;

    private FragmentManager fragmentManager;



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
        ViewSize.onSetRadioButtonSize(this, rb_home, R.drawable.selector_home);
        ViewSize.onSetRadioButtonSize(this, rb_shop, R.drawable.selector_shopping);
        ViewSize.onSetRadioButtonSize(this, rb_benefit, R.drawable.selector_benefit);
        ViewSize.onSetRadioButtonSize(this, rb_my, R.drawable.selector_my);
        fragmentManager = getSupportFragmentManager();
        onEnter(new HomeFragment());
        rb_home.setChecked(true);
        rg_buttom_menu.setOnCheckedChangeListener(this);
    }




    /**
     *
     * @param fragment
     */
    private void onEnter(Fragment fragment){

        fragmentManager.beginTransaction().replace(R.id.layout_content,fragment).commit();
    }

    public void onExitAppDialog(){
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            onExitAppDialog();
        }

        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId){
            case R.id.rb_home:
                onEnter(new HomeFragment());
                break;
            case R.id.rb_shop:
                onEnter(new ShopFragment());
                break;
            case R.id.rb_benefit:
                onEnter(new BenefitFragment());
                break;
            case R.id.rb_my:
                onEnter(new MyFragment());
                break;
        }
    }
}
