package com.example.latihan_praktikum_9.presentation.viewModel;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.latihan_praktikum_9.data.database.DatabaseContract;
import com.example.latihan_praktikum_9.data.database.DatabaseHelper;
import com.example.latihan_praktikum_9.data.entity.Users;
import com.example.latihan_praktikum_9.data.network.ApiService;
import com.example.latihan_praktikum_9.data.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersViewModel extends AndroidViewModel {
    private final DatabaseHelper dbHelper;
    private final MutableLiveData<List<Users>> countryList = new MutableLiveData<>();

    public UsersViewModel(@NonNull Application application) {
        super(application);
        dbHelper = new DatabaseHelper(application);
        fetchCountriesFromApi();
    }

    public MutableLiveData<List<Users>> getCountryList() {
        return countryList;
    }

    public void fetchCountriesFromApi() {
        ApiService apiService = RetrofitClient.getClient();
        Call<List<Users>> call = apiService.getUsers();

        call.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    saveCountriesToDatabase(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Log.e("API_ERROR", "Failed to fetch data: " + t.getMessage());
            }
        });
    }

    public void saveCountriesToDatabase(List<Users> countries) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + DatabaseContract.CountryEntry.TABLE_NAME);

        for (Users country : countries) {
            db.execSQL("INSERT INTO " + DatabaseContract.CountryEntry.TABLE_NAME +
                            " (" + DatabaseContract.CountryEntry.COLUMN_ID + ", " +
                            DatabaseContract.CountryEntry.COLUMN_NAME + ", " +
                            DatabaseContract.CountryEntry.COLUMN_USERNAME + ", " +
                            DatabaseContract.CountryEntry.COLUMN_EMAIL + ") VALUES (?, ?, ?, ?)",
                    new Object[]{country.getId(), country.getName(), country.getUsername(), country.getEmail()});
        }

        loadCountriesFromDatabase();
    }

    public void loadCountriesFromDatabase() {
        List<Users> countries = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseContract.CountryEntry.TABLE_NAME +
                " ORDER BY id DESC", null);

        while (cursor.moveToNext()) {
            countries.add(new Users(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            ));
        }
        cursor.close();
        countryList.postValue(countries);
    }

    public void saveData(String name, String capital, String currency) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CountryEntry.COLUMN_NAME, name);
        values.put(DatabaseContract.CountryEntry.COLUMN_USERNAME, capital);
        values.put(DatabaseContract.CountryEntry.COLUMN_EMAIL, currency);

        db.insert(DatabaseContract.CountryEntry.TABLE_NAME, null, values);
        loadCountriesFromDatabase();
    }

    public void updateData(int id, String name, String capital, String currency) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CountryEntry.COLUMN_NAME, name);
        values.put(DatabaseContract.CountryEntry.COLUMN_USERNAME, capital);
        values.put(DatabaseContract.CountryEntry.COLUMN_EMAIL, currency);

        db.update(DatabaseContract.CountryEntry.TABLE_NAME, values,
                DatabaseContract.CountryEntry.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
        loadCountriesFromDatabase();
    }

    public void deleteData(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseContract.CountryEntry.TABLE_NAME,
                DatabaseContract.CountryEntry.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
        loadCountriesFromDatabase();
    }
    public List<Users> searchUsers(String keyword) {
        return dbHelper.searchUsers(keyword);
    }
}
