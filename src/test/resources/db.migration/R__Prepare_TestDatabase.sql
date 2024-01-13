DROP TABLE IF EXISTS "users";
DROP TABLE IF EXISTS "tokens";
DROP TABLE IF EXISTS "locations";
DROP TABLE IF EXISTS "vehicles";
DROP TABLE IF EXISTS "vehicle_rentals";
DROP TABLE IF EXISTS "invoices";
DROP TABLE IF EXISTS "transactions";

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       d_type VARCHAR(20) NOT NULL,
                       role VARCHAR(20) NOT NULL,
                       status VARCHAR(20) NOT NULL,
                       address VARCHAR(255),
                       phone_number VARCHAR(15),
                       sales_rep_id INTEGER,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE tokens (
                        id SERIAL PRIMARY KEY,
                        token VARCHAR(255) UNIQUE,
                        token_type VARCHAR(20) DEFAULT 'BEARER' NOT NULL,
                        revoked BOOLEAN,
                        expired BOOLEAN,
                        user_id INTEGER,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE locations (
                           id SERIAL PRIMARY KEY,
                           city VARCHAR(255),
                           address VARCHAR(255),
                           postcode VARCHAR(20),
                           capacity INTEGER,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE vehicles (
                          id SERIAL PRIMARY KEY,
                          model VARCHAR(255),
                          manufacturer VARCHAR(255),
                          year INTEGER,
                          fuel_type VARCHAR(255),
                          license_plate VARCHAR(255),
                          status VARCHAR(255),
                          vehicle_type VARCHAR(255),
                          location_id INTEGER,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          FOREIGN KEY (location_id) REFERENCES locations (id) ON DELETE SET NULL
);

CREATE TABLE vehicle_rentals (
                                 id SERIAL PRIMARY KEY,
                                 rental_status VARCHAR(255),
                                 rental_date DATE,
                                 return_date DATE,
                                 user_id INTEGER,
                                 vehicle_id INTEGER,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL,
                                 FOREIGN KEY (vehicle_id) REFERENCES vehicles (id) ON DELETE SET NULL
);

CREATE TABLE invoices (
                          id SERIAL PRIMARY KEY,
                          vehicle_rental_id INTEGER,
                          amount NUMERIC(19, 4),
                          return_date TIMESTAMP,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          FOREIGN KEY (vehicle_rental_id) REFERENCES vehicle_rentals (id) ON DELETE CASCADE
);

CREATE TABLE transactions (
                              id SERIAL PRIMARY KEY,
                              amount NUMERIC(19, 4),
                              payment_status VARCHAR(255),
                              vehicle_rental_id INTEGER,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                              FOREIGN KEY (vehicle_rental_id) REFERENCES vehicle_rentals (id) ON DELETE CASCADE
);

