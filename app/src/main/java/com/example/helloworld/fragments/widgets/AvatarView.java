package com.example.helloworld.fragments.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.example.helloworld.api.Service;
import com.example.helloworld.entity.User;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/14.
 */

public class AvatarView extends View {

    public AvatarView(Context context) {
        super(context);
    }

    public AvatarView(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
    }

    public AvatarView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context,attributeSet,defStyleAttr);
    }

    Paint paint;
    float srcWidth,srcHeight;
    Handler mainThreadHandler = new Handler();

    public void setBitmap(Bitmap bmp) {
        if (bmp == null)  {
            paint = new Paint();
            paint.setColor(Color.GRAY);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1);
            //5长度直线，10长度空白，15长度支线，20长度空白
            paint.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, 0));
            //抗锯齿
            paint.setAntiAlias(true);
        }else {
            paint = new Paint();
            paint.setShader(new BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
            paint.setAntiAlias(true);

            srcHeight = bmp.getHeight();
            srcWidth = bmp.getWidth();
        }

//        paint = new Paint();
//        //设置笔刷
//        paint.setShader(new BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
//        radius = Math.min(bmp.getWidth(), bmp.getHeight()) / 2;
        invalidate();
    }

    public void load(User user) {
        //getAvatar得到的是字符串，服务器保存的地址
        load(Service.serverAddress + user.getAvatar());
    }

    public void load(String url) {
        OkHttpClient client = Service.getShareClient();

        Request request = new Request.Builder()
//                .url(Service.serverAddress + user.getAvatar())
                .url(url)
                .method("GET",null).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setBitmap(null);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    byte[] bytes = response.body().bytes();
                    final Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    mainThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            setBitmap(bmp);
                        }
                    });
                } catch (Exception ex) {
                    mainThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            setBitmap(null);
                        }
                    });
                }
            }
        });
    }

    //画圆形头像框,调用AvatarView是自动加载draw函数
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (paint != null) {
//            canvas.drawCircle(getWidth() / 2,getHeight() / 2,radius,paint);
            canvas.save();

            float dstWidth = getWidth();
            float dstHeight = getHeight();

            //计算缩放比例
            float scaleX = srcWidth / dstWidth;
            float scaleY = srcHeight / dstHeight;

            //设置缩放
            canvas.scale(1/scaleX,1/scaleY);

            //画圆，第1，2个参数设置圆心，第三个为半径，第四个为采用的画笔
            canvas.drawCircle(srcWidth/2,srcHeight/2,Math.min(srcWidth,srcHeight),paint);

            canvas.restore();
        }
    }
}
