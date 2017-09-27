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
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import static android.util.Log.d;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class SeoulFragment extends Fragment {

    private static ListView list;
    private static Activity activity;
    private ProgressDialog pDialog;
    private static ArrayList<HashMap<String, String>> dataList;
    private static ArrayList<HashMap<String, String>> searchList;

    private static final String TAG = "TAG_BIKE";  // TAG
    private static final String TAG_NUMBER = "NUMBER";  // 대여소 번호
    private static final String TAG_WEB_URL = "WEB_URL";  //URL
    private static final String TAG_USE_YN = "USE_YN";  // 사용여부
    private static final String TAG_TEL_NUMBER = "TEL_NUMBER"; //전화번호
    private static final String TAG_CONTENT_NM = "CONTENT_NM";  //콘텐츠명
    private static final String TAG_SUBCATEGORY_NM = "SUBCATEGORY_NM";  //"서울자전거 '따릉이'"
    private static final String TAG_ADDR_GU = "ADDR_GU";  //구 주소
    private static final String TAG_ADDR_DONG = "ADDR_DONG";
    private static final String TAG_ADDR_NUM2 = "ADDR_NUM2";
    private static final String TAG_ADDR_NUM1 = "ADDR_NUM1";
    private static final String TAG_NEW_ADDR = "NEW_ADDR";  // 새주소

    private static final String TAG_DETAIL_CONTENT1 = "DETAIL_CONTENT1";  //대여소번호
    private static final String TAG_DETAIL_CONTENT2 = "DETAIL_CONTENT2";  // 갯수
    private static final String TAG_DETAIL_CONTENT3 = "DETAIL_CONTENT3";  //"[운영시간]\n24시간, 연중무휴\n[이용요금]\n1일권 1천원(하루 동안 대여횟수 무제한. 단 1시간 초과시 추가요금 발생) / 정기권 1주일 3천원, 1개월 5천원, 6개월 1만5천원, 1년 3만원\n[이용대상] 만 15세 이상"

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        dataList = new ArrayList<>();
        searchList = new ArrayList<>();
    }
    public SeoulFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeoulFragment newInstance(String param1, String param2) {
        SeoulFragment fragment = new SeoulFragment();
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

        Bundle arg = getArguments();

        if(arg == null){
            new GetContacts().execute(0);
        }else if((arg.get("search").toString()).equals("")){
            new GetContacts().execute(1);
        }else{
            String text = arg.get("search").toString();
            new GetSearch().execute(text);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_seoul, container, false);
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
                in.putExtra(TAG_SUBCATEGORY_NM, hash.get(TAG_SUBCATEGORY_NM));
                in.putExtra(TAG_CONTENT_NM, hash.get(TAG_CONTENT_NM));
                in.putExtra(TAG_DETAIL_CONTENT2, hash.get(TAG_DETAIL_CONTENT2));  // 거치대수
                in.putExtra(TAG_NEW_ADDR, hash.get(TAG_NEW_ADDR));
                in.putExtra(TAG_ADDR_GU, hash.get(TAG_ADDR_GU));
                in.putExtra(TAG_WEB_URL, hash.get(TAG_WEB_URL));
                in.putExtra(TAG_TEL_NUMBER, hash.get(TAG_TEL_NUMBER));
                in.putExtra(TAG_DETAIL_CONTENT3, hash.get(TAG_DETAIL_CONTENT3));  //[운영시간]

                startActivity(in);

            }
        });
        return rootView;
    }

    public void loadJSONFromAsset() {
        try {
            AssetManager am = getResources().getAssets();
            AssetManager.AssetInputStream ais = (AssetManager.AssetInputStream)am.open("bike.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(ais));

            String line = null;
            while((line = br.readLine()) != null) {
                boolean check = line.contains("WEB_URL");
                if(check == true){
                    line = line.substring(0,line.length());
                    line = "["+line+"]";
                    JSONArray jarray = new JSONArray(line);
                    JSONObject object = jarray.getJSONObject(0);
                    if((object.getString(TAG_USE_YN)).equals("Y") && (object.getString(TAG_SUBCATEGORY_NM)).equals("서울자전거 '따릉이'")){
                        if((object.getString(TAG_ADDR_GU)).equals("null") && (object.getString(TAG_NEW_ADDR)).equals("null"))
                            continue;

                        else{
                            HashMap<String, String> contact = new HashMap<String, String>();
                            String addrGu = null;

                            //구주소
                            if((object.getString(TAG_ADDR_GU)).equals("null")) {
                                addrGu = "구주소 : 관련자료없음";
                            }else if(!(object.getString(TAG_ADDR_GU)).equals("null") && (object.getString(TAG_ADDR_DONG)).equals("null")){
                                addrGu = "구주소 : " + object.getString(TAG_ADDR_GU);
                            }else if(!(object.getString(TAG_ADDR_GU)).equals("null") && !(object.getString(TAG_ADDR_DONG)).equals("null")
                                    && (object.getString(TAG_ADDR_NUM1)).equals("null")){
                                addrGu = "구주소 : " + object.getString(TAG_ADDR_GU) + " " + object.getString(TAG_ADDR_DONG);
                            }else if(!(object.getString(TAG_ADDR_GU)).equals("null") && !(object.getString(TAG_ADDR_DONG)).equals("null")
                                    && !(object.getString(TAG_ADDR_NUM1)).equals("null") && (object.getString(TAG_ADDR_NUM2)).equals("null")){
                                addrGu = "구주소 : " + object.getString(TAG_ADDR_GU) + " " + object.getString(TAG_ADDR_DONG) + " " +
                                        object.getString(TAG_ADDR_NUM1);
                            }else{
                                addrGu = "구주소 : " + object.getString(TAG_ADDR_GU) + " " + object.getString(TAG_ADDR_DONG) + " " +
                                        object.getString(TAG_ADDR_NUM1) + "-" + object.getString(TAG_ADDR_NUM2);
                            }

                            //새주소
                            String newAddr = null;
                            if((object.getString(TAG_NEW_ADDR)).equals("null")){
                                newAddr = "새주소 : 관련자료없음";
                            }else{
                                newAddr = "새주소 : " + object.getString(TAG_NEW_ADDR);
                            }

                            //따릉이
                            String subcategory_NM = object.getString(TAG_SUBCATEGORY_NM);

                            String content_NM = object.getString(TAG_CONTENT_NM).substring(0,3) + ". " + object.getString(TAG_CONTENT_NM).substring(4);

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

                            String number = object.getString(TAG_DETAIL_CONTENT1);  // 대여소 번호 정렬을 위해

                            String bike_count = object.getString(TAG_DETAIL_CONTENT2);

                            String time = object.getString(TAG_DETAIL_CONTENT3);

                            contact.put(TAG_NUMBER, number);
                            contact.put(TAG_ADDR_GU, addrGu);
                            contact.put(TAG_NEW_ADDR, newAddr);
                            contact.put(TAG_CONTENT_NM, content_NM);
                            contact.put(TAG_SUBCATEGORY_NM, subcategory_NM);
                            contact.put(TAG_WEB_URL, web_URL);
                            contact.put(TAG_TEL_NUMBER, tel_Number);
                            contact.put(TAG_DETAIL_CONTENT2, bike_count);
                            contact.put(TAG_DETAIL_CONTENT3, time);

                            dataList.add(contact);
                        }
                    }
                }
            }
            bubbleSort(dataList.size());

        } catch (Exception ex) {
            d(TAG, Log.getStackTraceString(ex));
        }
    }

    public void bubbleSort(int array_size) {
        HashMap<String, String> hash = new HashMap<>();
        for(int i = 0; i < array_size; i++){
            for(int j = 0; j < array_size - 1; j++){
                if(Integer.parseInt(dataList.get(j).get(TAG_NUMBER)) > Integer.parseInt(dataList.get(j+1).get(TAG_NUMBER))){
                    hash = dataList.get(j);
                    dataList.set(j, dataList.get(j+1));
                    dataList.set(j+1, hash);
                }
            }
        }
    }

    private class GetContacts extends AsyncTask<Integer, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage("로딩중입니다.");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Integer... arg0) {
            int size = arg0[0];
            if(size == 0){
                dataList.clear();
                searchList.clear();
                loadJSONFromAsset();
            }
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
            ListAdapter adapter = new SimpleAdapter(
                    activity, dataList,
                    R.layout.list_item, new String[] { TAG_NEW_ADDR, TAG_ADDR_GU, TAG_CONTENT_NM},
                    new int[] { R.id.new_addr, R.id.addr_gu, R.id.content_nm});
            list.setAdapter(adapter);
        }
    }

    private class GetSearch extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage("로딩중입니다.");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... arg0) {
            String text = arg0[0];
            searchList.clear();
            for(int i = 0; i < dataList.size(); i++){
                if((dataList.get(i).get(TAG_NEW_ADDR)).contains(text) || (dataList.get(i).get(TAG_ADDR_GU)).contains(text)
                        || (dataList.get(i).get(TAG_CONTENT_NM)).contains(text)){
                    searchList.add(dataList.get(i));
                }
            }
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
            ListAdapter adapter = new SimpleAdapter(
                    activity, searchList,
                    R.layout.list_item, new String[] { TAG_NEW_ADDR, TAG_ADDR_GU, TAG_CONTENT_NM},
                    new int[] { R.id.new_addr, R.id.addr_gu, R.id.content_nm});
            list.setAdapter(adapter);
        }
    }
}
