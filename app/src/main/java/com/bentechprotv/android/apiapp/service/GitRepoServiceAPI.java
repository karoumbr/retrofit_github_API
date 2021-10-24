package com.bentechprotv.android.apiapp.service;

import com.bentechprotv.android.apiapp.model.GitRepo;
import com.bentechprotv.android.apiapp.model.GitUserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitRepoServiceAPI {
    @GET("search/users")
    public Call<GitUserResponse> searchUsers(@Query("q") String query);
    @GET("/users/{u}/repos")
    public Call<List<GitRepo>> userRepositories(@Path("u") String login);
}
