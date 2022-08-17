create table if not exists intent(
    intent_id varchar(255) primary key,
    intent_name varchar(255)
);

create table if not exists expectation(
    expectation_id varchar(255) primary key,
    expectation_name varchar(255),
    expectation_type varchar(255),
    intent_id varchar(255)
);

create table if not exists expectation_object(
    expectation_id varchar(255) primary key,
    object_type varchar(255),
    object_instance varchar(255)
);

create table if not exists expectation_target(
    target_id varchar(255) primary key,
    target_name varchar(255),
    target_condition varchar(255),
    expectation_id varchar(255)
);

create table if not exists context(
    context_id varchar(255) primary key,
    context_name varchar(255),
    context_type varchar(255),
    context_condition varchar(255)
);

create table if not exists context_mapping(
    context_id varchar(255) primary key,
    parent_type varchar(255),
    parent_id varchar(255)
);

create table if not exists fulfilment_info(
    fulfilment_info_id varchar(255) primary key,
    fulfilment_info_status varchar(255),
    not_fulfilled_state varchar(255),
    not_fulfilled_reason varchar(255)
);

create table if not exists state(
    state_id varchar(255) primary key,
    state_name varchar(255),
    is_satisfied boolean,
    condition varchar(255),
    expectation_id varchar(255)
);