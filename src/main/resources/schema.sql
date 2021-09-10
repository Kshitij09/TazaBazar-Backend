set search_path = public;
drop table if exists product;
drop table if exists product_category;
drop table if exists inventory;

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