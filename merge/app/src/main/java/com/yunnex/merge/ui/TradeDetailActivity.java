package com.yunnex.merge.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.yunnex.merge.BaseActivity;
import com.yunnex.merge.R;

/**
 * author ChenCHaoXue
 * Created by supercard on 2016/6/27 14:22
 * 交易详细页
 */
public class TradeDetailActivity extends BaseActivity {
    TextView tv_price,tv_title,tv_detail,
            tv_detail_number,tv_coupon_name,
            tv_order,tv_source,tv_time,
            tv_user_name,tv_card,tv_phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_detail);
        init();

    }

    public void init(){
        tv_price=(TextView)findViewById(R.id.tv_price);
        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_detail=(TextView)findViewById(R.id.tv_detail);
        tv_detail_number=(TextView)findViewById(R.id.tv_detail_number);
        tv_coupon_name=(TextView)findViewById(R.id.tv_coupon_name);
        tv_order=(TextView)findViewById(R.id.tv_order);
        tv_source=(TextView)findViewById(R.id.tv_source);
        tv_time=(TextView)findViewById(R.id.tv_time);
        tv_user_name=(TextView)findViewById(R.id.tv_user_name);
        tv_card=(TextView)findViewById(R.id.tv_card);
        tv_phone=(TextView)findViewById(R.id.tv_phone);

    }


}
