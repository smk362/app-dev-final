package com.appdevcourse.homeworkorganizer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.AlarmClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BrainBreak extends Fragment {
    private String id;
    private Button setTimer;
    private Button newCat;
    private Button share;
    private ImageView cat;
    private CatPhoto photo;
    private String photoUrl;
    private int i;

    public BrainBreak() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_brain_break, container, false);

        id = "dbcd95b2-759b-4817-8534-13d4d7412f48";
        cat = rootView.findViewById(R.id.imageView);
        newCat = rootView.findViewById(R.id.newCat);
        share = rootView.findViewById(R.id.share);
        i = 0;

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.thecatapi.com/v1/images/search?limit=100&page=100&order=DESC")
                .newBuilder();
        urlBuilder.addQueryParameter("api-key", id);

        //Picasso.get().load("https://cdn2.thecatapi.com/images/6ph.jpg").into(cat);

        final String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(!response.isSuccessful()) {
                    throw(new IOException("Unexpected call " + response));
                } else {
                    Moshi moshi = new Moshi.Builder().build();
                    ParameterizedType listType = Types.newParameterizedType(List.class, CatPhoto.class);
                    //Log.d("test12", response.body().string());
                    JsonAdapter<List<CatPhoto>> jsonAdapter = moshi.adapter(listType);
                    final List<CatPhoto> results = jsonAdapter.fromJson(response.body().string());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            photoUrl = results.get(i).getUrl();
                            Picasso.get().load(results.get(i).getUrl()).into(cat);

                            newCat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    i++;
                                    Picasso.get().load(results.get(i).getUrl()).into(cat);
                                }
                            });

                            share.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_SEND);
                                    Uri uri = Uri.parse(results.get(i).getUrl());
                                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                                    intent.setType("image/*");
                                    intent.putExtra(Intent.EXTRA_TEXT, "A cat to improve your day: \n" + results.get(i).getUrl());
                                    intent.setType("text/plain");
                                    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });

        setTimer = rootView.findViewById(R.id.setTimer);

        setTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                        .putExtra(AlarmClock.EXTRA_MESSAGE, "Back to Work!")
                        .putExtra(AlarmClock.EXTRA_LENGTH, 300)
                        .putExtra(AlarmClock.EXTRA_SKIP_UI, true);

                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });


        return rootView;
    }
}
