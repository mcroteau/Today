create table users (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	username character varying(55) NOT NULL,
	password character varying(155) NOT NULL,
	uuid character varying(155),
	date_created bigint default 0,
	stripe_user_id text,
	location_id bigint
);

create table roles (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name character varying(155) NOT NULL UNIQUE
);

create table user_permissions(
	user_id bigint REFERENCES users(id),
	permission character varying(55)
);

create table user_roles(
	role_id bigint NOT NULL REFERENCES roles(id),
	user_id bigint NOT NULL REFERENCES users(id)
);

create table statuses (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name character varying(131) NOT NULL,
	constraint status_name unique(name)
);

create table prospects (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name character varying(254) NOT NULL,
	email text,
	phone character varying(143),
	status_id bigint NOT NULL REFERENCES statuses(id),
	constraint prospect_name unique(name)
);

create table activities (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name character varying(254) NOT NULL,
	constraint activity_name unique(name)
);

create table effort (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	prospect_id bigint NOT NULL REFERENCES prospects(id),
	finished boolean
);

create table effort_activities (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	activity_id bigint NOT NULL REFERENCES activities(id)
);
