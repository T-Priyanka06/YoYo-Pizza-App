package com.example.yoyo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText userInput;
    RecyclerView recyclerView;
    CustomAdapter customAdapter;
    List<Responses> responseMessageList;
    int type=0,last=0;
    int prev=0;
    String[] req = {
            "no",
            "ok",
            "confirm",
            "cancel",
            "yes"
    };
    String[] items = {
            "neapolitan pizza",
            "pepperoni pizza",
            "cheese pizza",
            "bbq chicken pizza",
            "meat pizza",
            "double cheese margherita",
            "peppy paneer",
            "deluxe veggie",
            "cheese n corn",
            "indi tandoori paneer"
    };
    int[] price = {
            100,
            150,
            250,
            200,
            240,
            300,
            190,
            300,
            280,
            400
    };
    String[] mes = {
            "Okay.",
            "Would you like to have anything else? (Yes/No)",
            "There is no such services",
    };
    String final_list="",final_contact="",resultresp="",here="";
    int rep=0;
    String initial_message="Welcome to YoYo Pizza. You can order varities of pizza you want.\nHere the menu\n Neapolitan Pizza\n Pepperoni Pizza\n Cheese Pizza\n BBQ Chicken Pizza\n Meat Pizza\n Double Cheese Margherita\n Peppy Paneer\n Deluxe Veggie\n Cheese N corn\n Indi Tandoori Paneer";
    String menu = "Here the menu\n Neapolitan Pizza\n Pepperoni Pizza\n Cheese Pizza\n BBQ Chicken Pizza\n Meat Pizza\n Double Cheese Margherita\n Peppy Paneer\n Deluxe Veggie\n Cheese N corn\n Indi Tandoori Paneer";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Yo Yo Pizza");
        userInput = findViewById(R.id.userInput);
        recyclerView = findViewById(R.id.conversation);
        responseMessageList = new ArrayList<>();
        Responses responseMessage2 = new Responses(initial_message, false);
        responseMessageList.add(responseMessage2);
        customAdapter = new CustomAdapter(responseMessageList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(customAdapter);


        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    String sending_message = userInput.getText().toString().toLowerCase();
                    userInput.setText("");
                    String re_m = recieveMessage(sending_message);
                    Log.d("details",sending_message+" ** "+re_m);
                    Responses responseMessage = new Responses(sending_message, true);
                    responseMessageList.add(responseMessage);
                    if(rep==0){
                        Responses responseMessage2 = new Responses(re_m, false);
                        responseMessageList.add(responseMessage2);
                        customAdapter.notifyDataSetChanged();
                    }
//                    Responses responseMessage2 = new Responses(re_m, false);
//                    responseMessageList.add(responseMessage2);
                    customAdapter.notifyDataSetChanged();
                    if (!isLastVisible())
                        recyclerView.smoothScrollToPosition(customAdapter.getItemCount());
                }
                return false;
            }
        });
    }

    private String recieveMessage(String message) {
        int j=0,l=0;
        String rm="";
        if(last!=1){
            if(type == 0){
                for (String i:items){
                    Pattern pattern1 = Pattern.compile(".*" +i+ ".*");
                    Matcher matcher1 = pattern1.matcher(message);
                    j+=1;
                    if(matcher1.find()){
                        j-=1;
                        type=1;
                        prev=j;
                        rm = "Could you please enter the quantity";
                        break;
                    }
                }
                for(String k : req){
                    Pattern pattern1 = Pattern.compile(".*" +k+ ".*");
                    Matcher matcher1 = pattern1.matcher(message);
                    l+=1;
                    if(matcher1.find()){
                        l-=1;
                        if(message.contains("no")) {
                            rm = mes[0] + " Your final cart is \n" + final_list + "\nConfirm to confirm the order\nCancel to cancel the order";
                        }else if (message.contains("yes")){
                            rm = menu;
                        }else if (message.contains("confirm")){
                            rm = "Enter Your name, address and phone number";
                            last=1;
                        }
                    }
                }
            }else{
                Log.d("before",price[j]+"   "+Integer.parseInt(message));
                int htot = price[prev]*Integer.parseInt(message);
                type=0;
                final_list+=items[prev]+" - "+message+" - "+htot+"\n";
                rm=mes[1];
                Log.d("cart",final_list);
            }
        }
        else{
            rep=1;
            final_contact=message;
            placeOrder();
            last=0;
//            Responses responseMessage2 = new Responses(placeOrder(), false);
//            responseMessageList.add(responseMessage2);
        }
        Log.d("rm",j+" "+items.length+"     "+l+"   "+req.length);
        if(j==items.length && l==req.length){
            rm = "Sorry, the requested service is not available";
        }
        return rm;
    }

    private void placeOrder() {
        ApiInterface api = ApiClient.getApiClinet().create(ApiInterface.class);
        Call<String> call = api.getUserRegi(final_list,final_contact);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("l-onSuccess", response.body());
                        String jsonresponse = response.body();
                        try {
                            JSONObject jsonObject = new JSONObject(jsonresponse);
                            Log.i("logres and jsonobject", "" + jsonresponse + "****" + jsonObject);
                            here = jsonObject.getString("result");
                            Responses responseMessage2 = new Responses(jsonObject.getString("result"), false);
                            responseMessageList.add(responseMessage2);
                            customAdapter.notifyDataSetChanged();
                            rep=0;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.i("l-onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("onFailure",call+" "+t);
            }
        });
    }

    boolean isLastVisible() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
        int numItems = recyclerView.getAdapter().getItemCount();
        return (pos >= numItems);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menufile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.track:
                Intent i = new Intent(MainActivity.this,TrackActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Click exit to close the app. Your chat will be cleared");

        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //perform any action
                finish();
                System.exit(0);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //perform any action
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder.create();
        alert11.show();
    }
}