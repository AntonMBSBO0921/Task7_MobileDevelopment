package ru.mirea.maiorovaa.mireaproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkRecourcesFragment extends Fragment {

    private TextView textViewUsers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network_recources, container, false);

        textViewUsers = view.findViewById(R.id.textViewUsers);

        UserApi userApi = UserApiClient.getClient();
        Call<List<User>> call = userApi.getUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> users = response.body();
                    StringBuilder usersText = new StringBuilder();
                    for (User user : users) {
                        usersText.append("ID: ").append(user.getId()).append("\n")
                                .append("Name: ").append(user.getName()).append("\n")
                                .append("Username: ").append(user.getUsername()).append("\n")
                                .append("Email: ").append(user.getEmail()).append("\n\n");
                    }
                    textViewUsers.setText(usersText.toString());
                } else {
                    textViewUsers.setText("Failed to load users");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                textViewUsers.setText("Failed to load users: " + t.getMessage());
            }
        });

        return view;
    }
}
