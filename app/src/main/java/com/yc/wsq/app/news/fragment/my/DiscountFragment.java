package com.yc.wsq.app.news.fragment.my;

import android.view.View;
import android.widget.TextView;

import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UserView;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 优惠券
 */
public class DiscountFragment extends BaseFragment<UserView, UserPresenter<UserView>> implements UserView{

    public static final String TAG = DiscountFragment.class.getName();
    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    protected UserPresenter<UserView> createPresenter() {
        return new UserPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_discount;
    }

    @Override
    protected void initView() {

        tv_title.setText(getResources().getString(R.string.str_discount_text));
    }

    @OnClick({R.id.ll_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                mFunctionsManage.invokeFunction(INTERFACE_BACK);
                break;
        }
    }

    @Override
    public void onResponseData(Map<String, Object> result) {

    }
}