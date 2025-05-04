CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255),
    full_name VARCHAR(100),
    email_verified BOOLEAN DEFAULT false,
    created_at TIMESTAMP,
    role VARCHAR(20)
);

INSERT INTO users (email, password, full_name, email_verified, created_at, role)
VALUES ('test@user.com', '$2a$10$abcdefghijklmnopqrstuv', 'Test User', true, CURRENT_TIMESTAMP, 'USER')
ON CONFLICT (email) DO NOTHING;

CREATE TABLE IF NOT EXISTS packages (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100),
    description TEXT,
    country VARCHAR(50),
    total_credits INT,
    price DOUBLE PRECISION,
    validity_days INT
);

CREATE TABLE IF NOT EXISTS user_packages (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    pkg_id BIGINT REFERENCES packages(id),
    remaining_credits INT,
    purchase_date TIMESTAMP,
    expiry_date TIMESTAMP
);

INSERT INTO packages (title, description, country, total_credits, price, validity_days)
VALUES
('Yoga Package', '10 online yoga classes', 'India', 10, 50.00, 30),
('Pilates Package', '5 pilates sessions', 'USA', 5, 30.00, 15)
ON CONFLICT DO NOTHING;

CREATE TABLE IF NOT EXISTS bookings (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    class_name VARCHAR(100),
    class_date TIMESTAMP,
    booked_at TIMESTAMP
);