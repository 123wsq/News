package com.yc.wsq.app.news.fragment.my;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wsq.library.tools.ToastUtils;
import com.wsq.library.views.view.CustomPopup;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UserView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提现页面
 */
public class WithdrawFragment extends BaseFragment<UserView, UserPresenter<UserView>> implements UserView{

    public static final String TAG  = WithdrawFragment.class.getName() ;

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ll_layout)
    LinearLayout ll_layout;
    @BindView(R.id.tv_withdraw_to) TextView tv_withdraw_to;

    private CustomPopup popup;

    @Override
    protected UserPresenter<UserView> createPresenter() {
        return new UserPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_withdraw;
    }

    @Override
    protected void initView() {
        tv_title.setText(getResources().getString(R.string.str_withdraw_text));
    }

    @OnClick({R.id.ll_back, R.id.tv_withdraw, R.id.ll_withdraw_to})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                mFunctionsManage.invokeFunction(INTERFACE_BACK);
                break;
            case R.id.ll_withdraw_to:
                onInitPopup();
                break;
            case R.id.tv_withdraw:

                break;
        }
    }

    private void onInitPopup(){
        List<String> list = new ArrayList<>();
        list.add("支付宝");
        list.add("微信");
        popup = new CustomPopup(getActivity(), list, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        }, new CustomPopup.PopupItemListener() {
            @Override
            public void onClickItemListener(int i, String s) {
                tv_withdraw_to.setText(s);
                popup.dismiss();
                ToastUtils.onToast(s);
            }
        });
        popup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER, 0, 0);
    }

    @Override
    public void onResponseData(Map<String, Object> result) {

    }
}
