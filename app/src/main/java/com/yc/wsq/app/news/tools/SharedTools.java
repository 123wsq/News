package com.yc.wsq.app.news.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.yc.wsq.app.news.constant.Constant;
import com.yc.wsq.app.news.constant.ResponseKey;

/**
 * 数据缓存
 */
public class SharedTools {

    private  static SharedTools sharedTools;
    private Context mContext;
    private SharedPreferences shared;
    private SharedTools (Context context){
        this.mContext = context;
        shared = mContext.getSharedPreferences(Constant.getInstance().SHARED_NAME, Context.MODE_PRIVATE);
    }

    public static SharedTools getInstance(Context context){

        if (sharedTools == null){
            synchronized (SharedTools.class){
                if (sharedTools == null){
                    sharedTools = new SharedTools(context);
                }
            }
        }
        return sharedTools;
    }

    /**
     * 数据保存
     * @param key
     * @param data
     */
    public void onPutData(String key, Object data){

        SharedPreferences.Editor editor = shared.edit();
        if (data instanceof String){
            editor.putString(key, (String) data);
        }else if (data instanceof Boolean){
            editor.putBoolean(key, (Boolean) data);
        }else if (data instanceof Float){
            editor.putFloat(key, (Float) data);
        }else if (data instanceof Integer){
            editor.putInt(key, (Integer) data);
        }else if (data instanceof Long){
            editor.putLong(key, (Long) data);
        }else {
            try {
                throw new Exception("未知数据类型");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        editor.commit();
    }

    public String onGetString(String key){
        return shared.getString(key, "");
    }
    public boolean onGetBoolean(String key){
        return shared.getBoolean(key, false);
    }
    public float onGetFloat(String key){
        return shared.getFloat(key, -1);
    }
    public int onGetInt(String key){
        return shared.getInt(key, -1);
    }
    public long onGetLong(String key){
        return shared.getLong(key, -1);
    }


    public void onClearUser(){
        onPutData(ResponseKey.getInstace().nickname, "");
        onPutData(ResponseKey.getInstace().mobile, "");
        onPutData(ResponseKey.getInstace().token, "");
        onPutData(ResponseKey.getInstace().head_pic, "");
        onPutData(ResponseKey.getInstace().username, "");
        onPutData(ResponseKey.getInstace().password, "");
        onPutData(ResponseKey.getInstace().user_id,"");
        onPutData(ResponseKey.getInstace().paypwd,"");
        onPutData(ResponseKey.getInstace().user_money,"");
        onPutData(ResponseKey.getInstace().mobile,"");
        onPutData(ResponseKey.getInstace().total_amount,"");
        onPutData(ResponseKey.getInstace().is_vip,"");
        onPutData(ResponseKey.getInstace().level_name,"");
    }
}
