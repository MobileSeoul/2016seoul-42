package org.androidtown.mybikes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link GPSBikeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GPSBikeFragment extends Fragment {
    private SimpleAdapter adapter;
    private boolean lastitemVisibleFlag = false;        //화면에 리스트의 마지막 아이템이 보여지는지 체크
    private ListView list;
    private Activity activity;
    private ProgressDialog pDialog;
    private ArrayList<HashMap<String, String>> dataList;
    private ArrayList<HashMap<String, String>> viewList;
    private double longitude;  //X
    private double latitude; //Y

    private static final String TAG_SUM = "SUM";
    private static final String TAG_DISTANCE = "DISTANCE";
    private static final String TAG_COORDINATE_Y = "COORDINATE_Y";
    private static final String TAG_COORDINATE_X = "COORDINATE_X";
    private static final String TAG_WEB_URL = "WEB_URL";  //URL
    private static final String TAG_USE_YN = "USE_YN";  // 사용여부
    private static final String TAG_TEL_NUMBER = "TEL_NUMBER"; //전화번호
    private static final String TAG_CONTENT_NM = "CONTENT_NM";  //콘텐츠명
    private static final String TAG_SUBCATEGORY_NM = "SUBCATEGORY_NM";  //자전거대여소
    private static final String TAG_ADDR_GU = "ADDR_GU";  //구 주소
    private static final String TAG_ADDR_DONG = "ADDR_DONG";
    private static final String TAG_ADDR_NUM2 = "ADDR_NUM2";
    private static final String TAG_ADDR_NUM1 = "ADDR_NUM1";
    private static final String TAG_NEW_ADDR = "NEW_ADDR";  // 새주소
    private static final String TAG_DETAIL_CONTENT2 = "DETAIL_CONTENT2";  // 운영시간
    private static final String TAG_DETAIL_CONTENT3 = "DETAIL_CONTENT3";  // 이용비용
    private static final String TAG_DETAIL_CONTENT4 = "DETAIL_CONTENT4";  // 관리부서
    private static final String TAG_DETAIL_CONTENT5 = "DETAIL_CONTENT5";  // 기타 안내사항

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GPSBikeFragment() {
        // Required empty public constructor
        this.longitude = GPSTabActivity.longitude;
        this.latitude = GPSTabActivity.latitude;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        dataList = new ArrayList<>();
        viewList = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GPSBikeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GPSBikeFragment newInstance(String param1, String param2) {
        GPSBikeFragment fragment = new GPSBikeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        new GetGPS().execute();
    }

    // 주어진 도(degree) 값을 라디언(radian) 값으로 변환
    private double deg2rad(double deg){
        return (double)(deg * Math.PI / (double)180d);
    }

    // 주어진 라디언(radian) 값을 도(degree) 값으로 변환
    private double rad2deg(double rad){
        return (double)(rad * (double)180d / Math.PI);
    }
    public void getGPS(double longitude, double latitude) {
        try {
            double x, y;
            AssetManager am = getResources().getAssets();
            AssetManager.AssetInputStream ais = (AssetManager.AssetInputStream) am.open("bike.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(ais));

            String line = null;
            while ((line = br.readLine()) != null) {
                boolean check = line.contains("WEB_URL");
                if (check == true) {
                    line = line.substring(0, line.length());
                    line = "[" + line + "]";
                    JSONArray jarray = new JSONArray(line);
                    JSONObject object = jarray.getJSONObject(0);
                    if ((object.getString(TAG_USE_YN)).equals("Y") && !(object.getString(TAG_SUBCATEGORY_NM)).equals("서울자전거 '따릉이'")) {
                        if ((object.getString(TAG_ADDR_GU)).equals("null") && (object.getString(TAG_NEW_ADDR)).equals("null"))
                            continue;

                        else {
                            HashMap<String, String> contact = new HashMap<String, String>();
                            String addrGu = null;
                            x = object.getDouble(TAG_COORDINATE_X);
                            y = object.getDouble(TAG_COORDINATE_Y);

                            double theta, dist, t_dist;
                            theta = longitude - x;
                            dist = Math.sin(deg2rad(latitude)) * Math.sin(deg2rad(y)) + Math.cos(deg2rad(latitude))
                                    * Math.cos(deg2rad(y)) * Math.cos(deg2rad(theta));
                            dist = Math.acos(dist);
                            dist = rad2deg(dist);

                            dist = dist * 60 * 1.1515;
                            dist = dist * 1.609344;    // 단위 mile 에서 km 변환
                            dist = dist * 1000.0;      // 단위  km 에서 m 로 변환

                            String distance = null;
                            if(dist >= 1000.0){
                                t_dist = Math.round((dist/1000)*10)/10.0;
                                distance = Double.toString(t_dist)+"km";
                            }
                            else{
                                t_dist = Math.round(dist);
                                distance = Double.toString(t_dist)+"m";
                            }

                            String sum = Double.toString(dist);

                            //구주소
                            if ((object.getString(TAG_ADDR_GU)).equals("null")) {
                                addrGu = "구주소 : 관련자료없음";
                            } else if (!(object.getString(TAG_ADDR_GU)).equals("null") && (object.getString(TAG_ADDR_DONG)).equals("null")) {
                                addrGu = "구주소 : " + object.getString(TAG_ADDR_GU);
                            } else if (!(object.getString(TAG_ADDR_GU)).equals("null") && !(object.getString(TAG_ADDR_DONG)).equals("null")
                                    && (object.getString(TAG_ADDR_NUM1)).equals("null")) {
                                addrGu = "구주소 : " + object.getString(TAG_ADDR_GU) + " " + object.getString(TAG_ADDR_DONG);
                            } else if (!(object.getString(TAG_ADDR_GU)).equals("null") && !(object.getString(TAG_ADDR_DONG)).equals("null")
                                    && !(object.getString(TAG_ADDR_NUM1)).equals("null") && (object.getString(TAG_ADDR_NUM2)).equals("null")) {
                                addrGu = "구주소 : " + object.getString(TAG_ADDR_GU) + " " + object.getString(TAG_ADDR_DONG) + " " +
                                        object.getString(TAG_ADDR_NUM1);
                            } else {
                                addrGu = "구주소 : " + object.getString(TAG_ADDR_GU) + " " + object.getString(TAG_ADDR_DONG) + " " +
                                        object.getString(TAG_ADDR_NUM1) + "-" + object.getString(TAG_ADDR_NUM2);
                            }

                            //새주소
                            String newAddr = null;
                            if ((object.getString(TAG_NEW_ADDR)).equals("null")) {
                                newAddr = "새주소 : 관련자료없음";
                            } else {
                                newAddr = "새주소 : " + object.getString(TAG_NEW_ADDR);
                            }

                            String subcategory_NM = object.getString(TAG_SUBCATEGORY_NM);

                            String content_NM = object.getString(TAG_CONTENT_NM);

                            String web_URL = null;
                            if((object.getString(TAG_WEB_URL)).equals("null")){
                                web_URL = "관련자료없음";
                            }else{
                                web_URL = object.getString(TAG_WEB_URL);
                            }

                            String tel_Number = null;
                            if((object.getString(TAG_TEL_NUMBER)).equals("null")){
                                tel_Number = "관련자료없음";
                            }else{
                                tel_Number = object.getString(TAG_TEL_NUMBER);
                            }

                            String bike_count = object.getString(TAG_DETAIL_CONTENT2);

                            String time = null;
                            if((object.getString(TAG_DETAIL_CONTENT5)).equals("null")){
                                time = object.getString(TAG_DETAIL_CONTENT3);
                            }else{
                                time = object.getString(TAG_DETAIL_CONTENT3)+"\n\n[ 기타 안내사항 ]\n"+object.getString(TAG_DETAIL_CONTENT5);
                            }

                            String team = object.getString(TAG_DETAIL_CONTENT4);

                            contact.put(TAG_ADDR_GU, addrGu);
                            contact.put(TAG_NEW_ADDR, newAddr);
                            contact.put(TAG_CONTENT_NM, content_NM);
                            contact.put(TAG_SUBCATEGORY_NM, subcategory_NM);
                            contact.put(TAG_WEB_URL, web_URL);
                            contact.put(TAG_TEL_NUMBER, tel_Number);
                            contact.put(TAG_DETAIL_CONTENT2, bike_count);
                            contact.put(TAG_DETAIL_CONTENT3, time);
                            contact.put(TAG_DETAIL_CONTENT4, team);
                            contact.put(TAG_SUM, sum);
                            contact.put(TAG_DISTANCE, distance);
                            dataList.add(contact);
                        }
                    }

                }
            }

            bubbleSort(dataList.size());

        } catch (Exception ex) {
            Log.d("bikeError", Log.getStackTraceString(ex));
        }
    }

    public void bubbleSort(int array_size) {
        HashMap<String, String> hash = new HashMap<>();
        for(int i = 0; i < array_size; i++){
            for(int j = 0; j < array_size - 1; j++){
                if(Double.parseDouble(dataList.get(j).get(TAG_SUM)) > Double.parseDouble(dataList.get(j+1).get(TAG_SUM))){
                    hash = dataList.get(j);
                    dataList.set(j, dataList.get(j+1));
                    dataList.set(j+1, hash);
                }
            }
        }
        for(int i = 0; i < 7; i++){
            viewList.add(dataList.get(i));
        }
    }


    private class GetGPS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage("검색중입니다.");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            getGPS(longitude, latitude);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            adapter = new SimpleAdapter(
                    activity, viewList,
                    R.layout.list_gps_item, new String[] { TAG_NEW_ADDR, TAG_ADDR_GU, TAG_CONTENT_NM, TAG_DISTANCE},
                    new int[] { R.id.new_addr, R.id.addr_gu, R.id.content_nm, R.id.distace});
            list.setAdapter(adapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gpsbike, container, false);
        list = (ListView) rootView.findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String new_addr = ((TextView) view.findViewById(R.id.new_addr))
                        .getText().toString();
                String addr_gu = ((TextView) view.findViewById(R.id.addr_gu))
                        .getText().toString();

                HashMap<String, String> hash = new HashMap<String, String>();

                for(int i = 0; i < dataList.size(); i++){
                    if((new_addr.equals(dataList.get(i).get(TAG_NEW_ADDR))) && (addr_gu.equals(dataList.get(i).get(TAG_ADDR_GU)))){
                        hash = dataList.get(i);
                        break;
                    }
                }
                Intent in = new Intent(getActivity(), ClickActivity.class);
                in.putExtra(TAG_ADDR_GU, hash.get(TAG_ADDR_GU));
                in.putExtra(TAG_NEW_ADDR, hash.get(TAG_NEW_ADDR));
                in.putExtra(TAG_CONTENT_NM, hash.get(TAG_CONTENT_NM));
                in.putExtra(TAG_SUBCATEGORY_NM, hash.get(TAG_SUBCATEGORY_NM));
                in.putExtra(TAG_WEB_URL, hash.get(TAG_WEB_URL));
                in.putExtra(TAG_TEL_NUMBER, hash.get(TAG_TEL_NUMBER));
                in.putExtra(TAG_DETAIL_CONTENT2, hash.get(TAG_DETAIL_CONTENT2));
                in.putExtra(TAG_DETAIL_CONTENT3, hash.get(TAG_DETAIL_CONTENT3));
                in.putExtra(TAG_DETAIL_CONTENT4, hash.get(TAG_DETAIL_CONTENT4));
                startActivity(in);
            }
        });

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    //TODO 화면이 바닦에 닿을때 처리
                    int count;
                    count = adapter.getCount();
                    if(dataList.size() <= count+7){
                        for(int i = count; i < dataList.size(); i++){
                            viewList.add(dataList.get(i));
                        }
                    }else{
                        for(int i = count; i < count + 7; i++){
                            viewList.add(dataList.get(i));
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastitemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
        });

        return rootView;
    }
}
