create table if not exists intent(
    intent_id varchar(255) primary key,
    intent_name varchar(255)
);

create table if not exists expectation(
    expectation_id varchar(255) primary key,
    expectation_name varchar(255),
    target_moi varchar(255),
    intent_id varchar(255)
);

create table if not exists state(
    state_id varchar(255) primary key,
    state_name varchar(255),
    is_satisfied boolean,
    condition varchar(255),
    expectation_id varchar(255)
);

