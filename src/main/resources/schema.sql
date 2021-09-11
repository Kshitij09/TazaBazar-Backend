set search_path = public;
drop table if exists order_line;
drop table if exists purchase_order;
drop table if exists inventory;
drop table if exists product;
drop table if exists product_category;

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
    id serial,
    product_sku varchar(80),
    quantity_label text,
    price numeric(10,2),
    updated_at timestamptz,
    stock_available integer check (stock_available >= 0),
    primary key (id, product_sku),
    foreign key (product_sku) references product(sku)
    on delete cascade
);

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table purchase_order(
    id uuid primary key default uuid_generate_v4(),
    created_at timestamptz not null default now(),
    status varchar(20) not null check(status in ('Accepted','Pending','Dispatched','Delivered','Cancelled'))
);

create table order_line(
    order_id uuid not null,
    inventory_id integer not null,
    product_sku text not null,
    quantity integer not null check(quantity > 0),
    primary key (order_id, inventory_id, product_sku),
    foreign key (inventory_id, product_sku) references inventory(id, product_sku)
    on delete cascade,
    foreign key (order_id) references purchase_order(id)
    on delete cascade
);