set search_path = public;
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