package com.example.latihan_praktikum_9.presentation.ui;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.latihan_praktikum_9.R;
import com.example.latihan_praktikum_9.data.entity.Users;
import com.example.latihan_praktikum_9.data.network.RetrofitClient;
import com.example.latihan_praktikum_9.data.network.ApiService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritFragment extends Fragment implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private GoogleMap mMap;
    private TextView tvNearestUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorit, container, false);
        tvNearestUser = view.findViewById(R.id.tv_nearest_user);

        createNotificationChannel();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        getLastLocation();

        return view;
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                fetchUsersAndFindNearest();
            }
        });
    }

    private void fetchUsersAndFindNearest() {
        ApiService apiService = RetrofitClient.getClient();
        apiService.getUsers().enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Users> usersList = response.body();
                    double minDistance = Double.MAX_VALUE;
                    Users nearestUser = null;

                    for (Users user : usersList) {
                        try {
                            double userLat = Double.parseDouble(user.getAddress().getGeo().getLat());
                            double userLng = Double.parseDouble(user.getAddress().getGeo().getLng());
                            float[] result = new float[1];
                            Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), userLat, userLng, result);

                            if (result[0] < minDistance) {
                                minDistance = result[0];
                                nearestUser = user;
                            }

                            if (mMap != null) {
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(userLat, userLng))
                                        .title(user.getName()));
                            }
                        } catch (Exception ignored) {
                        }
                    }

                    if (nearestUser != null) {
                        tvNearestUser.setText("Terdekat: " + nearestUser.getName());
                        showNotification(nearestUser.getName()); // Tampilkan notifikasi

                        if (mMap != null) {
                            double userLat = Double.parseDouble(nearestUser.getAddress().getGeo().getLat());
                            double userLng = Double.parseDouble(nearestUser.getAddress().getGeo().getLng());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userLat, userLng), 10));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                tvNearestUser.setText("Gagal memuat data.");
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "nearest_user_channel";
            CharSequence name = "Nearest User Notifications";
            String description = "Channel for nearest user found";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(String userName) {
        String channelId = "nearest_user_channel";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Pengguna Terdekat")
                .setContentText("Pengguna terdekat adalah: " + userName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.POST_NOTIFICATIONS}, 2);
            return;
        }

        notificationManager.notify(1001, builder.build());
    }
}
