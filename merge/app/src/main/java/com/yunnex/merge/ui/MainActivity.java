package com.yunnex.merge.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.yunnex.merge.BaseActivity;
import com.yunnex.merge.R;
import com.yunnex.merge.common.BarcodeEncoder;
import com.yunnex.merge.http.HttpBaseRequest;
import com.yunnex.merge.http.data.HttpResult;
import com.yunnex.merge.http.data.TestEntity;
import com.yunnex.merge.http.data.Weather;
import com.yunnex.merge.subscribers.ProgressSubscriber;
import com.yunnex.merge.subscribers.SubscriberOnNextListener;
import com.yunnex.merge.view.RoundAngleGJImageView;

import rx.Subscriber;

public class MainActivity extends BaseActivity {
    TextView tv_left,tv_right;
    ViewFlipper viewFlipper;
    View lly_two;
    ImageView img;
    RoundAngleGJImageView img_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewFlipper=(ViewFlipper)findViewById(R.id.flipper);
        img=(ImageView)findViewById(R.id.img);
        img_logo=(RoundAngleGJImageView)findViewById(R.id.img_logo);
        img_logo.setBackgroundResource(R.mipmap.test);
        lly_two=findViewById(R.id.lly_two);
        tv_left=(TextView)findViewById(R.id.tv_left);
        tv_right=(TextView)findViewById(R.id.tv_right);
        showQrCode("0000");
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.showNext();
            }
        });
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpBaseRequest.getInstance().getWeatherData(new Subscriber<HttpResult<Weather>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("","------>onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpResult<Weather> weather) {
                        Log.d("","------>"+weather);
                    }
                });

                startActivity(new Intent(MainActivity.this,SwipeRefreshActivity.class));
            }
        });

    }

    public void showQrCode(String code){
        BarcodeEncoder encoder;
        try {
            encoder = new BarcodeEncoder(code, 276, 276, BarcodeFormat.QR_CODE);
            img.setImageBitmap(encoder.encodeAsBitmap());
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
