package com.yunnex.merge.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.SwipeRefreshListView.interfaces.MODE;
import com.SwipeRefreshListView.listener.OnPullDownListener;
import com.SwipeRefreshListView.listener.OnPullUpListener;
import com.SwipeRefreshListView.view.SwipeRefreshAutoListView;
import com.bigkoo.pickerview.TimePickerView;
import com.yunnex.merge.BaseActivity;
import com.yunnex.merge.R;
import com.yunnex.merge.adapter.SwipeAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * author ChenCHaoXue
 * Created by supercard on 2016/6/23 17:38
 * 新刷新框架
 */
public class SwipeRefreshActivity extends BaseActivity {

    private SwipeRefreshAutoListView mSwipeRefreshLayout;
    SwipeAdapter adapter;
    private ListView mListView;
    TextView tv_time;
    TimePickerView pvTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        mSwipeRefreshLayout = (SwipeRefreshAutoListView) findViewById(R.id.list_refresh);
        if(mSwipeRefreshLayout!=null){
            mSwipeRefreshLayout.setMode(MODE.BOTH);
            mListView = mSwipeRefreshLayout.getListView();
        }
        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
//        Calendar calendar = Calendar.getInstance();
//        pvTime.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        tv_time=(TextView)findViewById(R.id.tv_time);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                tv_time.setText(getTime(date));
            }
        });

        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });
        adapter = new SwipeAdapter(this, getList());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(SwipeRefreshActivity.this,TradeDetailActivity.class));
                Toast.makeText(SwipeRefreshActivity.this, "--OnItem--" + (position - 1), Toast.LENGTH_SHORT).show();
            }
        });
        mListView.setAdapter(adapter);

        mSwipeRefreshLayout.setOnPullDownListener(new OnPullDownListener() {
            @Override
            public void onRefresh() {
                 /*
                  *  进行下拉刷新的时候 建议把FooterView隐藏掉
                  */
                mSwipeRefreshLayout.setMode(MODE.ONLY_DOWN);
                mListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (getList().size() < 8) {
                            mSwipeRefreshLayout.setMode(MODE.ONLY_DOWN);
                        } else {
                            mSwipeRefreshLayout.setMode(MODE.BOTH);
                        }
                        adapter.setList(getList());
                        mSwipeRefreshLayout.pullDownComplete();
                    }
                }, 1000);
            }
        });
        mSwipeRefreshLayout.setOnPullUpListener(new OnPullUpListener() {
            @Override
            public void onLoad() {
                mListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addList(getAddList());
                        mSwipeRefreshLayout.pullUpSuccess();
                    }
                }, 1000);
            }
        });
        mSwipeRefreshLayout.autoRefreshing();
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    ArrayList<String> list=new ArrayList<>();

    public ArrayList<String> getList() {

        if(list!=null)list.clear();
        for (int i = 0; i < 9; i++) {
            list.add("start " + i);
        }
        return list;
    }

    public ArrayList<String> getAddList() {
    ArrayList<String> addList=new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            addList.add("add " + i);
        }
        return addList;
    }

}
