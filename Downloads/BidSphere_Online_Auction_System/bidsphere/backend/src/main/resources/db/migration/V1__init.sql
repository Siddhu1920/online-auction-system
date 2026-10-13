CREATE TABLE users (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 name VARCHAR(120) NOT NULL,
 email VARCHAR(190) NOT NULL UNIQUE,
 password_hash VARCHAR(100) NOT NULL,
 role VARCHAR(20) NOT NULL,
 active BOOLEAN NOT NULL DEFAULT TRUE,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE auctions (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 title VARCHAR(180) NOT NULL,
 description TEXT NOT NULL,
 category VARCHAR(80) NOT NULL,
 condition_label VARCHAR(40) NOT NULL,
 starting_price DECIMAL(12,2) NOT NULL,
 current_price DECIMAL(12,2) NOT NULL,
 min_increment DECIMAL(12,2) NOT NULL,
 start_time TIMESTAMP NOT NULL,
 end_time TIMESTAMP NOT NULL,
 status VARCHAR(20) NOT NULL,
 seller_id BIGINT NOT NULL,
 winner_id BIGINT NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT fk_auction_seller FOREIGN KEY (seller_id) REFERENCES users(id),
 CONSTRAINT fk_auction_winner FOREIGN KEY (winner_id) REFERENCES users(id),
 INDEX idx_auction_status_end (status, end_time),
 INDEX idx_auction_category (category)
);
CREATE TABLE bids (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 auction_id BIGINT NOT NULL,
 bidder_id BIGINT NOT NULL,
 amount DECIMAL(12,2) NOT NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT fk_bid_auction FOREIGN KEY (auction_id) REFERENCES auctions(id),
 CONSTRAINT fk_bid_user FOREIGN KEY (bidder_id) REFERENCES users(id),
 INDEX idx_bid_auction_amount (auction_id, amount DESC)
);
