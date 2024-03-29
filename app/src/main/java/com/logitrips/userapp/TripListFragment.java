package com.logitrips.userapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.logitrips.userapp.adapter.MyTripListRecyclerViewAdapter;
import com.logitrips.userapp.model.Car;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TripListFragment extends Fragment {


     private  List<Car> lists;

    private String json;
    private int status;
    public TripListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TripListFragment newInstance(String json,int status) {


        TripListFragment fragment = new TripListFragment();
        Bundle args = new Bundle();
        args.putString("json",json);
        args.putInt("status",status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            json=getArguments().getString("json");
            status=getArguments().getInt("status");
        }
    }
    private void setList(JSONArray array) throws JSONException {



        lists=new ArrayList<Car>();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        for (int i =0;i<array.length();i++){
            Car car = new Car();
            JSONObject item = array.getJSONObject(i);
            car.setCar_id(item.getInt("car_id"));
            car.setCar_model(item.getString("car_model"));
            car.setYear(item.getInt("car_year"));
            car.setCar_class(item.getString("car_class"));
            car.setCar_rating(item.getDouble("car_rating"));
            JSONArray car_image_ar = item.getJSONArray("car_pic_urls");
            String[] car_image = new String[car_image_ar.length()];
            for (int j = 0; j < car_image_ar.length(); j++) {
                car_image[j] = car_image_ar.getString(j);
            }
            car.setCar_pic_urls(car_image);
            car.setLocation(item.getInt("location"));
            if (!item.isNull("hourly_price"))
                car.setHourly_price(item.getDouble("hourly_price"));
            car.setDaily_price(item.getDouble("daily_price"));
            car.setDay2_price(item.getDouble("day2_price"));
            car.setDriver_id(item.getInt("driver_id"));
            car.setDriver_name(item.getString("driver_name"));
            car.setDriver_pic_url(item.getString("driver_pic_url"));
            car.setDriver_knowledge(item.getString("driver_knowledge"));
            car.setDriver_smoking(item.getInt("driver_smoking"));



            JSONArray driver_lang_ar = item.getJSONArray("driver_languages");
            String[] driver_lang = new String[driver_lang_ar.length()];
            for (int k = 0; k < driver_lang_ar.length(); k++) {
                driver_lang[k] = driver_lang_ar.getString(k);
            }
            car.setDriver_lang(driver_lang);
            car.setDriver_year(item.getInt("driver_year"));
            car.setStart_date(item.getString("start_date"));
            car.setEnd_date(item.getString("end_date"));
            car.setTrip_status(item.getInt("trip_status"));
            if(!item.isNull("total_fee"))
                car.setTotal_fee(item.getDouble("total_fee"));
            car.setPayment_status(item.getInt("payment_status"));
            Date date = new Date();

            try {
                if(status==0){
                    if(date.before(dateFormatter.parse(car.getEnd_date()))) {
                        lists.add(car);
                    }
                }
                else{
                    if(!date.before(dateFormatter.parse(car.getEnd_date()))) {
                        lists.add(car);
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_triplist_list, container, false);


            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.mytrip_list);


            try {
                setList(new JSONArray(json));

                recyclerView.setAdapter(new MyTripListRecyclerViewAdapter(lists, getActivity()));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        return view;
    }



    @Override
    public void onDetach() {
        super.onDetach();

    }



}
