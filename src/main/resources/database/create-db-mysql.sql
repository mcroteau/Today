create table products(
	id bigint PRIMARY KEY AUTO_INCREMENT,
    nickname varchar (255),
	stripe_id text
);

create table prices(
	id bigint PRIMARY KEY AUTO_INCREMENT,
	stripe_id text,
    amount decimal default 0.0,
    nickname varchar (255),
	product_id bigint NOT NULL REFERENCES products(id)
);

create table users (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	username varchar(55) NOT NULL,
	password varchar(155) NOT NULL,
	uuid varchar(155),
	date_created bigint default 0,
	stripe_user_id text,
	location_id bigint
);

create table roles (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name varchar(155) NOT NULL UNIQUE
);

create table user_permissions(
	user_id bigint REFERENCES users(id),
	permission varchar(55)
);

create table user_roles(
	role_id bigint NOT NULL REFERENCES roles(id),
	user_id bigint NOT NULL REFERENCES users(id)
);

create table towns(
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name varchar(255) NOT NULL,
    town_uri varchar(255),
    count bigint default 0
);

create table prospects (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name varchar(255) NOT NULL,
	description text,
	needs text,
	count bigint default 1,
	location_uri varchar (255),
	town_id bigint NOT NULL REFERENCES towns(id),
	constraint unique_location_uri unique(location_uri)
);

create table donations(
	id bigint PRIMARY KEY AUTO_INCREMENT,
	amount decimal,
	processed boolean,
	charge_id text,
	subscription_id text,
	cancelled boolean default false,
	user_id bigint NOT NULL REFERENCES users(id),
	location_id bigint REFERENCES prospects(id)
);

create table daily_counts (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	user_id bigint NOT NULL REFERENCES users(id),
	location_id bigint NOT NULL REFERENCES prospects(id),
	count bigint NOT NULL,
	date_entered bigint NOT NULL,
	constraint unique_location_count unique(location_id, date_entered)
);

