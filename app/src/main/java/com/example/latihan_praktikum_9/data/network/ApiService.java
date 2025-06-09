package com.example.latihan_praktikum_9.data.network;

import com.example.latihan_praktikum_9.data.entity.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("users")
    Call<List<Users>> getUsers();
}
