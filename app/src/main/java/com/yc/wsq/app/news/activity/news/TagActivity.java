package com.yc.wsq.app.news.activity.news;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wsq.library.listener.OnRecyclerViewItemClickListener;
import com.wsq.library.tools.RecyclerViewDivider;
import com.wsq.library.tools.ToastUtils;
import com.wsq.library.utils.DensityUtil;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;
import com.yc.wsq.app.news.adapter.ApprenticeAdapter;
import com.yc.wsq.app.news.adapter.TagAdapter;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.presenter.NewsPresenter;
import com.yc.wsq.app.news.mvp.view.NewsView;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.tools.SharedTools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class TagActivity extends BaseActivity<NewsView, NewsPresenter<NewsView>> implements NewsView, OnItemMoveListener {

    @BindView(R.id.rv_RecyclerView_in) SwipeMenuRecyclerView rv_RecyclerView_in;
    @BindView(R.id.rv_RecyclerView_out) RecyclerView rv_RecyclerView_out;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_edit_tag) CheckBox tv_edit_tag;  //选中编辑 //未选中 显示保存

    public static final int RESULT_CODE = 2021;

    private List<Map<String, Object>> mDataIn, mDataOut;
    private TagAdapter mAdapterIn, mAdapterOut;

    @Override
    protected NewsPresenter<NewsView> createPresenter() {
        return new NewsPresenter<>();
    }

    @Override
    protected int getLayoutId() {

        return R.layout.layout_activity_tag;
    }

    @Override
    protected void initView() {

        tv_title.setText(getString(R.string.str_all_tag_text));
        onInitRecyclerView();
    }

    private void  onInitRecyclerView(){


        mDataIn = new ArrayList<>();
        mDataOut = new ArrayList<>();



        rv_RecyclerView_in.setLongPressDragEnabled(false);
        rv_RecyclerView_in.setLayoutManager(new GridLayoutManager(this, 4));
        rv_RecyclerView_in.setHasFixedSize(true);
        rv_RecyclerView_in.setOnItemMoveListener(this);
        mAdapterIn = new TagAdapter(this, mDataIn, mInListener);
        rv_RecyclerView_in.setAdapter(mAdapterIn);


        rv_RecyclerView_out.setLayoutManager(new GridLayoutManager(this, 4));
        rv_RecyclerView_out.setHasFixedSize(true);
        mAdapterOut = new TagAdapter(this, mDataOut, mOutListener);
        rv_RecyclerView_out.setAdapter(mAdapterOut);
    }

    @Override
    protected void onResume() {
        super.onResume();

        onGetTagData();
    }

    @OnClick({R.id.ll_back, R.id.tv_edit_tag})
     public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_edit_tag:
                if (tv_edit_tag.isChecked()){
                    tv_edit_tag.setText("完成");
                    mAdapterIn.onChangeState(2);
                    mAdapterOut.onChangeState(1);
                    rv_RecyclerView_in.setLongPressDragEnabled(true);
                }else{
                    tv_edit_tag.setText("编辑");
                    mAdapterIn.onChangeState(0);
                    mAdapterOut.onChangeState(0);
                    onUpdateSort();
                    rv_RecyclerView_in.setLongPressDragEnabled(false);
                }

                break;
        }
    }

    OnRecyclerViewItemClickListener mInListener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onRecyclerItemClickListener(View view, int i) {
            if(tv_edit_tag.isChecked()){
                int cat_type = (int) mDataIn.get(i).get(ResponseKey.getInstace().cat_type);
                if (cat_type ==0) {
                    mDataOut.add(mDataIn.get(i));
                    mDataIn.remove(i);
                    mAdapterIn.notifyDataSetChanged();
                    mAdapterOut.notifyDataSetChanged();
                }
            }else{
                Intent intent = new Intent();
                intent.putExtra(ResponseKey.getInstace().cat_id, mDataIn.get(i).get(ResponseKey.getInstace().cat_id)+"");
                intent.putExtra(ResponseKey.getInstace().position, i);
                setResult(RESULT_CODE, intent);
                finish();
            }
        }

        @Override
        public void onRecyclerItemLongClickListener(View view, int i) {

        }
    };
    OnRecyclerViewItemClickListener mOutListener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onRecyclerItemClickListener(View view, int i) {

            if(tv_edit_tag.isChecked()){
                mDataIn.add(mDataOut.get(i));
                mDataOut.remove(i);
                mAdapterIn.notifyDataSetChanged();
                mAdapterOut.notifyDataSetChanged();
            }
        }

        @Override
        public void onRecyclerItemLongClickListener(View view, int i) {

        }
    };


    private void onGetTagData(){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.getInstace().user_id, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_id));

        try {
            ipresenter.onGetAllTag(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onUpdateSort(){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.getInstace().user_id, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_id));
        String ids = "";
        for (int i=0; i< mDataIn.size(); i++){
            if (TextUtils.isEmpty(ids)) {
                ids = mDataIn.get(i).get(ResponseKey.getInstace().cat_id)+"";
            }else{
                ids += ","+mDataIn.get(i).get(ResponseKey.getInstace().cat_id);
            }
        }
        try {
            param.put(ResponseKey.getInstace().cat_ids, ids);
            ipresenter.onUpdateSortTag(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewsResponse(Map<String, Object> result) {


        List<Map<String, Object>> mIn = (List<Map<String, Object>>) result.get(ResponseKey.getInstace().in);

        List<Map<String, Object>> mOut = (List<Map<String, Object>>) result.get(ResponseKey.getInstace().out);
        mDataIn.addAll(mIn);
        mDataOut.addAll(mOut);

        mAdapterIn.notifyDataSetChanged();
        mAdapterOut.notifyDataSetChanged();
    }

    @Override
    public void onNewsTypeResponse(Map<String, Object> result) {

        String msg = (String) result.get(ResponseKey.getInstace().rsp_msg);
        ToastUtils.onToast(msg);
    }

    @Override
    public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
        int fromPosition = srcHolder.getAdapterPosition();
        int toPosition = targetHolder.getAdapterPosition();
        if (fromPosition < toPosition)
            for (int i = fromPosition; i < toPosition; i++)
                Collections.swap(mDataIn, i, i + 1);
        else
            for (int i = fromPosition; i > toPosition; i--)
                Collections.swap(mDataIn, i, i - 1);
        mAdapterIn.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {

    }
}
