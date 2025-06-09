package com.example.latihan_praktikum_9.data.entity;

import android.location.Address;

public class Users {
    private int id;
    private String name;
    private String username;
    private String email;
    private Address address;

    public Users(int id, String name, String username, String email) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public Address getAddress() { return address; }
    public class Address {
        private Geo geo;
        public Geo getGeo() { return geo; }

        public class Geo {
            private String lat;
            private String lng;

            public String getLat() { return lat; }
            public String getLng() { return lng; }
        }
    }
}
