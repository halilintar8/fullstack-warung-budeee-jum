-- Step 1: Create the Database
CREATE DATABASE db_warung_makan;
\c db_warung_makan;

-- Step 2: Create Tables

-- Users Table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

-- Tokens Table (Linked to Users)
CREATE TABLE tokens (
    id BIGSERIAL PRIMARY KEY,
    token TEXT NOT NULL,
    token_type VARCHAR(255) NOT NULL,
    expired BOOLEAN DEFAULT FALSE,
    revoked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE
);

-- Products Table
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    kode VARCHAR(255) UNIQUE NOT NULL,
    nama VARCHAR(255) NOT NULL,
    harga INTEGER NOT NULL,
    is_ready BOOLEAN DEFAULT TRUE,
    gambar TEXT,
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP
);

-- Pesanans Table (Orders)
CREATE TABLE pesanans (
    id BIGSERIAL PRIMARY KEY,
    nama VARCHAR(255) NOT NULL,
    no_meja VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP
);

-- Keranjangs Table (Carts - Links Orders and Products)
CREATE TABLE keranjangs (
    id BIGSERIAL PRIMARY KEY,
    jumlah_pemesanan INTEGER NOT NULL,
    keterangan TEXT,
    pesanan_id BIGINT REFERENCES pesanans(id) ON DELETE CASCADE,
    product_id BIGINT REFERENCES products(id) ON DELETE CASCADE,
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP
);

-- Step 3: Verify Tables
\dt


