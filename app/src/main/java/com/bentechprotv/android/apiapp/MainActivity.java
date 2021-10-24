package com.bentechprotv.android.apiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.bentechprotv.android.apiapp.model.GitUser;
import com.bentechprotv.android.apiapp.model.GitUserResponse;
import com.bentechprotv.android.apiapp.model.UsersListViewModel;
import com.bentechprotv.android.apiapp.service.GitRepoServiceAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class MainActivity extends AppCompatActivity {
    List<GitUser> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// solve :     android.os.NetworkOnMainThreadException
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        EditText _txtName = (EditText) findViewById(R.id.txtName);
        Button _btnGet = (Button) findViewById(R.id.btnGet);
        ListView _lsvUsers = (ListView) findViewById(R.id.lsvUsers  );

     //   ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,data);
        UsersListViewModel listViewModel = new UsersListViewModel(this,R.layout.users_list_view_layout,data);
     _lsvUsers.setAdapter(listViewModel);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

     _btnGet.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             String query = _txtName.getText().toString();
             GitRepoServiceAPI gitRepoServiceAPI = retrofit.create(GitRepoServiceAPI.class);
             Call<GitUserResponse> callGitUsers = gitRepoServiceAPI.searchUsers(query);
             callGitUsers.enqueue(new Callback<GitUserResponse>() {
                 @Override
                 public void onResponse(Call<GitUserResponse> call, Response<GitUserResponse> response) {
                     Log.i("info",call.request().url().toString());
                     if(!response.isSuccessful()){
                         Log.i("indo",String.valueOf(response.code()));
                         return;
                     }
                     GitUserResponse gitUserResponse = response.body();
                    // data.clear();
                     for(GitUser user:gitUserResponse.users){
                         data.add(user);
                     }
                     listViewModel.notifyDataSetChanged();
                 }

                 @Override
                 public void onFailure(Call<GitUserResponse> call, Throwable t) {
                    Log.e("error",t.getMessage());

                 }
             });
         }
     });

        _lsvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String login = data.get(position).login;
                Log.i("info", login);
                Intent intent = new Intent(getApplicationContext(),RepositoryActivity.class);
                intent.putExtra("user.login",login);
                startActivity(intent);
            }
        });

    }
}