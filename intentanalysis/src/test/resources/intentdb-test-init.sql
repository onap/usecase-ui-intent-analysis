/*
 Navicat Premium Data Transfer

 Source Server         : 1
 Source Server Type    : PostgreSQL
 Source Server Version : 100011
 Source Host           : localhost:5432
 Source Catalog        : exampledb
 Source Schema         : mec

 Target Server Type    : PostgreSQL
 Target Server Version : 100011
 File Encoding         : 65001

 Date: 30/12/2019 14:40:23
*/
//CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS intent;
DROP TABLE IF EXISTS expectation;
DROP TABLE IF EXISTS expectation_object;
DROP TABLE IF EXISTS expectation_target;
DROP TABLE IF EXISTS context;
DROP TABLE IF EXISTS fulfillment_info;
DROP TABLE IF EXISTS condition;

create table if not exists intent
(
    intent_id   varchar(255) primary key,
    intent_name varchar(255),
    intent_generateType VARCHAR (225)
);

create table if not exists expectation
(
    expectation_id   varchar(255) primary key,
    expectation_name varchar(255),
    expectation_type varchar(255),
    intent_id        varchar(255)
);

create table if not exists expectation_object
(
    object_id       varchar(255) DEFAULT random_uuid(),
    primary key (object_id),
    object_type     varchar(255),
    expectation_id  varchar(255)
);

create table if not exists expectation_target
(
    target_id      varchar(255) primary key,
    target_name    varchar(255),
    expectation_id varchar(255)
);

create table if not exists context
(
    context_id   varchar(255) primary key,
    context_name varchar(255),
    parent_id    varchar(255)
);

create table if not exists fulfillment_info
(
    fulfillment_info_id     varchar(255) primary key,
    fulfillment_info_status varchar(255),
    not_fulfilled_state    varchar(255),
    not_fulfilled_reason   varchar(255),
    achieve_value varchar(255)
);

create table if not exists condition
(
    condition_id    varchar(255) primary key,
    condition_name  varchar(255),
    operator_type   varchar(255),
    condition_value varchar(255),
    parent_id       varchar(255)
);

create table if not exists intent_management_function_reg_info(
    imfr_info_id varchar(255) primary key,
    imfr_info_description varchar(255),
    support_area varchar(255),
    support_model varchar(255),
    support_interfaces varchar(255),
    handle_name varchar(255),
    intent_function_type varchar(255)
    );

create table if not exists intent_event_record(
    id varchar(255) DEFAULT random_uuid (),
    intent_id varchar(255),
    intent_name varchar(255),
    intent_status varchar (225),
    operate_type varchar (225),
    parent_id varchar(255)
    );

create table if not exists intent_report(
    intent_report_id varchar(255) primary key,
    intent_reference varchar(255),
    report_time varchar(255)
    );

create table if not exists intent_report_fulfillment_info(
    parent_id varchar(255),
    fulfillment_info_id varchar(255),
    fulfillment_info_status varchar(255),
    not_fulfilled_state varchar(255),
    not_fulfilled_reason varchar(255),
    achieve_value varchar(255)
    );

create table if not exists object_instance(
    parent_id varchar(255),
    object_instance varchar(255)
    );

create table if not exists intent_instance(
    intent_instance_id varchar(255) primary key,
    intent_id varchar(255)
    );