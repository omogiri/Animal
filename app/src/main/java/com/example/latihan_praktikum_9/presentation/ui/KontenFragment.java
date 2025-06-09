package com.example.latihan_praktikum_9.presentation.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.latihan_praktikum_9.R;
import com.example.latihan_praktikum_9.data.entity.Users;
import com.example.latihan_praktikum_9.presentation.adapter.UsersAdapter;
import com.example.latihan_praktikum_9.presentation.viewModel.UsersViewModel;

import java.util.ArrayList;
import java.util.List;

public class KontenFragment extends Fragment {
    private UsersViewModel countryViewModel;
    private EditText etName, etCapital, etCurrency;
    private Button btnSave, btnUpdate, btnDelete;
    private RecyclerView recyclerView;
    private UsersAdapter adapter;
    private List<Users> usersList = new ArrayList<>();
    private int selectedCountryId = -1;
    private EditText etSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_konten, container, false); // Gantilah layout sesuai
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        countryViewModel = new ViewModelProvider(requireActivity()).get(UsersViewModel.class);

        etName = view.findViewById(R.id.et_name);
        etCapital = view.findViewById(R.id.et_capital);
        etCurrency = view.findViewById(R.id.et_currency);
        btnSave = view.findViewById(R.id.btn_save);
        btnUpdate = view.findViewById(R.id.btn_update);
        btnDelete = view.findViewById(R.id.btn_delete);
        recyclerView = view.findViewById(R.id.recycler_view);
        etSearch = view.findViewById(R.id.et_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        adapter = new UsersAdapter(usersList, country -> {
            etName.setText(country.getName());
            etCapital.setText(country.getUsername());
            etCurrency.setText(country.getEmail());
            selectedCountryId = country.getId();
        });
        recyclerView.setAdapter(adapter);

        countryViewModel.getCountryList().observe(getViewLifecycleOwner(), list -> {
            usersList.clear();
            usersList.addAll(list);
            adapter.notifyDataSetChanged();
        });

        countryViewModel.fetchCountriesFromApi();

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String capital = etCapital.getText().toString();
            String currency = etCurrency.getText().toString();

            if (name.isEmpty() || capital.isEmpty() || currency.isEmpty()) {
                Toast.makeText(getContext(), "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            countryViewModel.saveData(name, capital, currency);
            clearInputFields();
        });

        btnUpdate.setOnClickListener(v -> {
            if (selectedCountryId == -1) {
                Toast.makeText(getContext(), "Pilih data terlebih dahulu!", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = etName.getText().toString();
            String capital = etCapital.getText().toString();
            String currency = etCurrency.getText().toString();

            if (name.isEmpty() || capital.isEmpty() || currency.isEmpty()) {
                Toast.makeText(getContext(), "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            countryViewModel.updateData(selectedCountryId, name, capital, currency);
            clearInputFields();
        });

        btnDelete.setOnClickListener(v -> {
            if (selectedCountryId == -1) {
                Toast.makeText(getContext(), "Pilih data terlebih dahulu!", Toast.LENGTH_SHORT).show();
                return;
            }

            countryViewModel.deleteData(selectedCountryId);
            clearInputFields();
        });

        etSearch.addTextChangedListener(new TextWatcher() {        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    private void searchUsers(String keyword) {
        new Thread(() -> {
            List<Users> results = countryViewModel.searchUsers(keyword);
            requireActivity().runOnUiThread(() -> {
                usersList.clear();
                usersList.addAll(results);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void clearInputFields() {
        etName.setText("");
        etCapital.setText("");
        etCurrency.setText("");
        selectedCountryId = -1;
    }
}
