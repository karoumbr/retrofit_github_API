package com.bentechprotv.android.apiapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bentechprotv.android.apiapp.model.GitRepo;
import com.bentechprotv.android.apiapp.model.GitUser;
import com.bentechprotv.android.apiapp.model.GitUserResponse;
import com.bentechprotv.android.apiapp.service.GitRepoServiceAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepositoryActivity extends AppCompatActivity {
    List<String>  data = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repository_layout);
        setTitle("Repositories");
        Intent intent = getIntent();
        String Login = intent.getStringExtra("user.login");
        TextView txtLogin = (TextView) findViewById(R.id.txtLoginDetail);
        ListView listViewRepositories =(ListView) findViewById(R.id.lsvRepositories);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);
        listViewRepositories.setAdapter(arrayAdapter);
        txtLogin.setText(Login);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GitRepoServiceAPI gitRepoServiceAPI = retrofit.create(GitRepoServiceAPI.class);
        Call<List<GitRepo>> reposCall = gitRepoServiceAPI.userRepositories(Login);
        reposCall.enqueue(new Callback<List<GitRepo>>() {
            @Override
            public void onResponse(Call<List<GitRepo>> call, Response<List<GitRepo>> response) {
                Log.i("info",call.request().url().toString());
                if(!response.isSuccessful()){
                    Log.i("indo",String.valueOf(response.code()));
                    return;
                }
                List<GitRepo> gitRepos = response.body();
                data.clear();
                for(GitRepo gitRepo :gitRepos){

                    String content = "";
                    content+=gitRepo.id+ "\n";
                    content+=gitRepo.name+ "\n";
                    content+=gitRepo.language+ "\n";
                    content+=gitRepo.size+ "\n";
                    data.add(content);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<GitRepo>> call, Throwable t) {

            }
        });
    }
}
