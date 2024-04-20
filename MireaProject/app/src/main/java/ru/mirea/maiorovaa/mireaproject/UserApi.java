package ru.mirea.maiorovaa.mireaproject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.mirea.maiorovaa.mireaproject.User;

public interface UserApi {
    @GET("users")
    Call<List<User>> getUsers();
}
