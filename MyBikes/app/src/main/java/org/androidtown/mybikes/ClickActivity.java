package org.androidtown.mybikes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


public class ClickActivity extends AppCompatActivity {
    private TextView detail_tv;
    private TextView direction_tv;

    private static final String TAG_WEB_URL = "WEB_URL";  //URL
    private static final String TAG_TEL_NUMBER = "TEL_NUMBER"; //전화번호
    private static final String TAG_CONTENT_NM = "CONTENT_NM";  //콘텐츠명
    private static final String TAG_SUBCATEGORY_NM = "SUBCATEGORY_NM";  //서울자전거 '따릉이' 또는 자전거대여소
    private static final String TAG_ADDR_GU = "ADDR_GU";  //구 주소
    private static final String TAG_NEW_ADDR = "NEW_ADDR";  // 새주소
    private static final String TAG_DETAIL_CONTENT2 = "DETAIL_CONTENT2";  // 거치대수
    private static final String TAG_DETAIL_CONTENT3 = "DETAIL_CONTENT3";  //"연중무휴\n[이용요금]\n1일권 1천원(하루 동안 대여횟수 무제한. 단 1시간 초과시 추가요금 발생) / 정기권 1주일 3천원, 1개월 5천원, 6개월 1만5천원, 1년 3만원\n[이용대상]\n만 15세 이상"
    private static final String TAG_DETAIL_CONTENT4 = "DETAIL_CONTENT4";  //"보행자전거과"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click);
        Intent intent = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(intent.getStringExtra(TAG_CONTENT_NM));

        TextView bike_number_tv = (TextView)findViewById(R.id.bike_number_tv);
        TextView newAddress_tv = (TextView)findViewById(R.id.newAddress_tv);
        TextView oldAddress_tv = (TextView)findViewById(R.id.oldAddress_tv);
        TextView URL_tv = (TextView)findViewById(R.id.URL_tv);
        TextView Tel_tv = (TextView)findViewById(R.id.Tel_tv);
        TextView time_tv = (TextView) findViewById(R.id.time_tv);
        TextView charge_tv = (TextView)findViewById(R.id.charge_tv);
        TextView team_tv = (TextView)findViewById(R.id.team_tv);
        detail_tv = (TextView)findViewById(R.id.detail_tv);
        direction_tv = (TextView)findViewById(R.id.direction_tv);

        detail_tv.setText("<'이용요금' 자세히 보기>");
        direction_tv.setText("<'이용방법' 자세히 보기>");

        if(intent != null){
            if((intent.getStringExtra(TAG_SUBCATEGORY_NM)).equals("자전거대여소")){
                bike_number_tv.setText("관련자료없음");
                String time = intent.getStringExtra(TAG_DETAIL_CONTENT2);
                time_tv.setText(time);
                String charge = intent.getStringExtra(TAG_DETAIL_CONTENT3);
                charge_tv.setText(charge);
                String team = intent.getStringExtra(TAG_DETAIL_CONTENT4);
                team_tv.setText("관리부서\n"+team); // 관리부서
            }else{
                String bike_count = intent.getStringExtra(TAG_DETAIL_CONTENT2);
                bike_number_tv.setText(bike_count);
                time_tv.setText("24시간, 연중무휴");
                charge_tv.setText("[ 일일권 ]\n* 1시간 : 1,000원\n(주의! 1시간 초과시 추가요금 발생)\n\n[ 정기권 ]\n* 1주일 : 3,000원\n* 1개월 : 5,000원\n* 6개월 : 15,000원\n* 1년 : 30,000원\n\n[ 이용대상 ]\n만 15세 이상");
                team_tv.setText("관리부서\n보행자전거과"); // 관리부서
            }

            detail_tv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_DOWN){
                        if(detail_tv.getClass()==v.getClass()){
                            detail_tv.setBackgroundColor(Color.parseColor("#ff33b5e5"));
                        }
                    }
                    if(event.getAction()==MotionEvent.ACTION_MOVE){
                        if(detail_tv.getClass()==v.getClass()){
                            detail_tv.setBackgroundColor(Color.parseColor("#ffffff"));
                        }
                    }
                    if(event.getAction()==MotionEvent.ACTION_UP){
                        if(detail_tv.getClass()==v.getClass()){
                            detail_tv.setBackgroundColor(Color.parseColor("#ffffff"));
                            Intent intent = new Intent(getApplicationContext(), ChargeActivity.class);
                            startActivity(intent);
                        }
                    }
                    return true;
                }
            });

            direction_tv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_DOWN){
                        if(direction_tv.getClass()==v.getClass()){
                            direction_tv.setBackgroundColor(Color.parseColor("#ff33b5e5"));
                        }
                    }
                    if(event.getAction()==MotionEvent.ACTION_MOVE){
                        if(direction_tv.getClass()==v.getClass()){
                            direction_tv.setBackgroundColor(Color.parseColor("#ffffff"));
                        }
                    }
                    if(event.getAction()==MotionEvent.ACTION_UP){
                        if(direction_tv.getClass()==v.getClass()){
                            direction_tv.setBackgroundColor(Color.parseColor("#ffffff"));
                            Intent intent = new Intent(getApplicationContext(), DirectionTapActivity.class);
                            startActivity(intent);
                        }
                    }
                    return true;
                }
            });

            String newAddr = intent.getStringExtra(TAG_NEW_ADDR);
            newAddress_tv.setText(newAddr.substring(6));

            String addrGu = intent.getStringExtra(TAG_ADDR_GU);
            oldAddress_tv.setText(addrGu.substring(6));

            String web_URL = intent.getStringExtra(TAG_WEB_URL);
            URL_tv.setText(web_URL);

            String tel_Number = intent.getStringExtra(TAG_TEL_NUMBER);
            Tel_tv.setText(tel_Number);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
