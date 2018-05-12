package com.yc.wsq.app.news.fragment.news;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.presenter.BasePresenter;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.tools.ShareTools;

import butterknife.BindView;
import butterknife.OnClick;

public class ShareFragment extends BaseFragment{

    public static final String TAG = ShareFragment.class.getName();

    @BindView(R.id.et_share_content) EditText et_share_content;
    @BindView(R.id.tv_share_name) TextView tv_share_name;
    @BindView(R.id.tv_share_desc) TextView tv_share_desc;
    @BindView(R.id.tv_title) TextView tv_title;

    private String platform = "";
    private String title = "";
    private String description = "";

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_share;
    }

    @Override
    protected void initView() {
        tv_title.setText(getString(R.string.str_share_text));

        platform = getArguments().getString(ResponseKey.getInstace().platform);
        title = getArguments().getString(ResponseKey.getInstace().title);
        description = getArguments().getString(ResponseKey.getInstace().description);

        tv_share_name.setText(title);
        tv_share_desc.setText(description);
    }

    @OnClick({R.id.ll_back, R.id.tv_share})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                mFunctionsManage.invokeFunction(INTERFACE_BACK);
                break;
            case R.id.tv_share:
//                ShareTools.showShare(getActivity(), platform, title, "", et_share_content.getText().toString());

                break;
        }
    }
}
