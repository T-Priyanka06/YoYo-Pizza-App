package com.example.yoyo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackActivity extends AppCompatActivity {
    EditText trackid;
    Button search;
    CardView card;
    String id="";
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        trackid = (EditText) findViewById(R.id.trackid);
        search = (Button) findViewById(R.id.search);
        card = (CardView) findViewById(R.id.card);
        result = (TextView) findViewById(R.id.result);
        card.setEnabled(false);
        card.setVisibility(View.INVISIBLE);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = trackid.getText().toString();
                if(!id.isEmpty()){
                    ApiInterface api = ApiClient.getApiClinet().create(ApiInterface.class);
                    Call<String> call = api.getResult(id);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.d("response",""+response.body());
                            card.setEnabled(true);
                            card.setVisibility(View.VISIBLE);
                            result.setText(Html.fromHtml(response.body()));
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("onFailure",call+"    "+t);
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(),"ID should not be empty",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}