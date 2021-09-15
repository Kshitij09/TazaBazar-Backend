set search_path = public;
drop table if exists order_line;
drop table if exists purchase_order;
drop table if exists cart_item;
drop table if exists inventory;
drop table if exists product;
drop table if exists product_category;
drop table if exists authorized;
drop table if exists user_role;
drop table if exists user_auth;
drop table if exists user_detail;

create table product_category(
    label varchar(50) primary key,
    sku_prefix varchar(10) not null check(sku_prefix <> ''),
    name text not null check(name <> '')
);

create table product(
    sku varchar(80) primary key,
    name varchar(120) not null check(name <> ''),
    category varchar(50),
    foreign key (category) references product_category(label)
);

create table inventory(
    id serial primary key,
    product_sku varchar(80),
    quantity_label text,
    price numeric(10,2),
    updated_at timestamptz,
    stock_available integer check (stock_available >= 0),
    unique (product_sku, quantity_label),
    foreign key (product_sku) references product(sku)
    on delete cascade
);

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table user_detail(
    username text primary key check(username <> ''),
    phone varchar(20) not null unique check(phone <> ''),
    full_name text
);

create table user_role(name varchar(50) primary key);

create table user_auth(
    username text primary key,
    password text not null check(password <> ''),
    refresh_token text,
    email_verified boolean default false,
    phone_verified boolean default false,
    foreign key (username) references user_detail(username)
    on delete cascade
);

create table authorized(
    username text not null,
    role varchar(50) not null,
    primary key (username, role),
    foreign key (username) references user_auth(username)
    on delete cascade,
    foreign key (role) references user_role(name)
    on delete cascade
);

create table purchase_order(
    id uuid primary key default uuid_generate_v4(),
    username text references user_detail(username)
    on delete cascade,
    created_at timestamptz not null default now(),
    status varchar(20) not null check(status in ('Accepted','Pending','Dispatched','Delivered','Cancelled'))
);

create table order_line(
    order_id uuid not null,
    inventory_id integer not null,
    quantity integer not null check(quantity > 0),
    primary key(order_id, inventory_id),
    foreign key (inventory_id) references inventory(id)
    on delete cascade,
    foreign key (order_id) references purchase_order(id)
    on delete cascade
);

create table cart_item(
    username text not null,
    inventory_id integer not null,
    quantity integer not null check(quantity > 0),
    primary key (username, inventory_id),
    foreign key (username) references user_detail(username)
    on delete cascade,
    foreign key (inventory_id) references inventory(id)
    on delete cascade
);