package sun.ch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import sun.ch.safe.R;

/**
 * 创建可以在第三方应用中的浮窗
 */
public class ShowWindowManager {

    private static WindowManager manager;
    private static View view;
    private static SharedPreferences sharedPreferences;

    /**
     * 弹出浮窗
     * @param context
     * @param msg
     */
    public static void showWindow(Context context,String msg){

        sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //配置参数信息
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

        //设置重心为左上角,默认为中心
        params.gravity = Gravity.LEFT + Gravity.TOP;
        //设置浮窗位置
        int lastLeft = sharedPreferences.getInt("lastLeft", 0);
        int lastTop = sharedPreferences.getInt("lastTop", 0);
        params.x = lastLeft;
        params.y = lastTop;

        //引用view控件对象
        view = View.inflate(context, R.layout.activity_showwindow,null);
        TextView tvView = (TextView) view.findViewById(R.id.window_text);
        tvView.setText(msg);

        //自定义背景风格
        int[] styles = new int[]{R.mipmap.call_locate_white,R.mipmap.call_locate_orange,
                R.mipmap.call_locate_blue,R.mipmap.call_locate_gray,R.mipmap.call_locate_green};
        int window_style = sharedPreferences.getInt("window_style", 0);
        view.setBackgroundResource(styles[window_style]);

        //把view添加到window屏幕
        manager.addView(view,params);
        //获取屏幕宽高
        final int screenWidth = manager.getDefaultDisplay().getWidth();
        final int screenHeight = manager.getDefaultDisplay().getHeight();
        //拖拽事件
        view.setOnTouchListener(new View.OnTouchListener() {
            private int startY;
            private int startX;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN://手指按下触发
                        //获取初始坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE://手指移动触发
                        //获取移动时的坐标
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();
                        //计算位移
                        int x = endX - startX;
                        int y = endY - startY;
                        //重新设置控件位置
                        params.x += x;
                        params.y += y;
                        //限制边界
                        if(params.x<0){
                            params.x = 0;
                        }
                        if(params.x>screenWidth-view.getWidth()){
                            params.x = screenWidth-view.getWidth();
                        }
                        if(params.y<0){
                            params.y = 0;
                        }
                        if(params.y>screenHeight-view.getHeight()){
                            params.y = screenHeight-view.getHeight();
                        }
                        //更新view
                        manager.updateViewLayout(view,params);
                        //重新获取初始坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP://手指抬起触发
                        //存储坐标位置
                        sharedPreferences.edit().putInt("lastLeft", params.x).commit();
                        sharedPreferences.edit().putInt("lastTop", params.y).commit();
                        System.out.println("x:"+params.x+";y:"+params.y);
                        break;
                }
                return true;
            }
        });
    }
    public static void closeWindow(){
        if(manager!=null&&view!=null){
            manager.removeView(view);
            view = null;
        }
    }
}
