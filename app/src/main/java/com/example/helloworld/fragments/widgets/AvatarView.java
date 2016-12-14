package com.example.helloworld.fragments.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
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
    float radius;
    Handler mainThreadHandler = new Handler();

    public void setBitmap(Bitmap bmp) {
        paint = new Paint();
        //设置笔刷
        paint.setShader(new BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        radius = Math.min(bmp.getWidth(), bmp.getHeight()) / 2;
        invalidate();
    }

    public void load(User user) {
        OkHttpClient client = Service.getShareClient();

        Request request = new Request.Builder()
                .url(Service.serverAddress + user.getAvatar())
                .method("GET",null).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

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
                    Log.d("AvatarView", ex.getLocalizedMessage().toString());
                }
            }
        });
    }

    //画圆形头像框
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (paint != null) {
            canvas.drawCircle(getWidth() / 2,getHeight() / 2,radius,paint);
        }
    }
}
