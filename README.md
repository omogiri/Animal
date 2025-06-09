# Animal
# 📱 Aplikasi Manajemen Pengguna & Lokasi

Aplikasi mobile sederhana yang menyediakan fitur manajemen pengguna serta pelacakan lokasi menggunakan Google Maps. Aplikasi ini cocok digunakan untuk sistem informasi internal, pelacakan pengguna, atau dashboard pengelolaan akun.

---

## 🚀 Fitur Utama

### 🔐 1. Autentikasi Pengguna

Halaman login menyediakan dua metode untuk masuk ke aplikasi:
- **Login dengan Email & Password**
- **Login menggunakan akun Google**
  
Selain itu, pengguna baru dapat mendaftar melalui tautan:  
👉 _"Belum punya akun? Register di sini"_

---

### 📍 2. Lokasi Terdekat

Fitur geolokasi yang menampilkan peta interaktif:
- Menampilkan lokasi pengguna di Google Maps
- Marker lokasi pengguna terdekat
- Dropdown _“Terdekat”_ menampilkan nama pengguna terdekat
- Sangat cocok untuk sistem pelacakan atau pemetaan posisi pengguna

---

### 👤 3. Manajemen Pengguna (CRUD)

Halaman ini memungkinkan admin untuk mengelola data pengguna:
- **Input Field**:
  - ID
  - Nama
  - Nama Panggilan
  - Email
- **Aksi CRUD**:
  - `Simpan`: Menambahkan pengguna baru
  - `Update`: Memperbarui data pengguna
  - `Hapus`: Menghapus pengguna
- **Daftar Pengguna**:
  Menampilkan informasi yang sudah disimpan dalam bentuk kartu
- **Search Bar**:
  Mencari pengguna berdasarkan nama atau ID

---

## 🧱 Teknologi yang Digunakan

- Google Maps API
- Firebase Authentication (opsional jika login dengan Google)
- State Management (Provider/Bloc/SetState)
- Dark Mode Friendly UI

---