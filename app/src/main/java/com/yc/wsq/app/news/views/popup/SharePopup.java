package com.yc.wsq.app.news.views.popup;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wsq.library.listener.OnRecyclerViewItemClickListener;
import com.wsq.library.utils.DensityUtil;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.tools.ShareTools;
import com.yc.wsq.app.news.tools.SharedTools;

import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class SharePopup extends PopupWindow implements View.OnClickListener {


    private RecyclerView rv_RecyclerView;
    private TextView tv_cancel;

    private Integer[] drawableId = {R.mipmap.image_wechat_icon, R.mipmap.image_friend_circle_icon, R.mipmap.image_qq_icon, R.mipmap.image_qzone_icon};
    private String[] title = {"微信好友", "朋友圈", "QQ好友","QQ空间"};
    private ShareAdapter mAdapter;
    private OnShareResultListener mListener;


    private Activity mContext;
    private View popupView;


    public SharePopup(Activity context,  OnShareResultListener listener){
        this.mContext = context;
        this.mListener = listener;
        onInitView();

        initPopup();
    }

    public void initPopup(){

        int w = mContext.getResources().getDisplayMetrics().widthPixels;
        int h = mContext.getResources().getDisplayMetrics().heightPixels;
        // 设置按钮监听
        // 设置SelectPicPopupWindow的View
        this.setContentView(popupView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth((int)(w));
        //
        // 设置SelectPicPopupWindow弹出窗体的高
        int height = 60+10 +60;
//
        if (drawableId.length  % 4 == 0) {
            height += (drawableId.length / 4) * 90;
        } else {
            height += ((drawableId.length / 4)+1) * 90;
        }

        this.setHeight(DensityUtil.dp2px(mContext, height));
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.style_pop);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

//        this.setOnDismissListener(new PoponDismissListener());

        popupView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

//					dismiss();

                return false;
            }
        });
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {


        super.showAtLocation(parent, gravity, x, y);
        backgroundAlpha(0.5f);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        backgroundAlpha(1f);
    }


    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        mContext.getWindow().setAttributes(lp);
    }
    public void onInitView(){

        popupView = LayoutInflater.from(mContext).inflate(R.layout.layout_popup_share, null, false);

        rv_RecyclerView = popupView.findViewById(R.id.rv_RecyclerView);
        tv_cancel = popupView.findViewById(R.id.tv_cancel);

        tv_cancel.setOnClickListener(this);

        rv_RecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mAdapter = new ShareAdapter();

        rv_RecyclerView.setAdapter(mAdapter);

    }


    @Override
    public void onClick(View v) {

        dismiss();
    }

    class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder>{

        @Override
        public ShareAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_share, parent, false);
            ViewHolder holder = new ViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(ShareAdapter.ViewHolder holder, int position) {

            holder.iv_share_image.setImageResource(drawableId[position]);
            holder.tv_share_name.setText(title[position]);
        }

        @Override
        public int getItemCount() {
            return drawableId.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private ImageView iv_share_image;
            private TextView tv_share_name;
            public ViewHolder(View itemView) {
                super(itemView);

                iv_share_image = itemView.findViewById(R.id.iv_share_image);
                tv_share_name = itemView.findViewById(R.id.tv_share_name);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

                listener.onRecyclerItemClickListener(itemView, getPosition());
            }
        }
    }

    OnRecyclerViewItemClickListener listener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onRecyclerItemClickListener(View view, int position) {

            mListener.onClickResult(position+1);
            dismiss();
        }

        @Override
        public void onRecyclerItemLongClickListener(View view, int i) {

        }
    };

    public interface OnShareResultListener{
        void onClickResult(int platform);
    }


}
