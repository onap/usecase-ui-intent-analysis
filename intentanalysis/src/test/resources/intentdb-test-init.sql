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
DROP TABLE IF EXISTS fulfilment_info;
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
    object_instance varchar(255),
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

create table if not exists fulfilment_info
(
    fulfilment_info_id     varchar(255) primary key,
    fulfilment_info_status varchar(255),
    not_fulfilled_state    varchar(255),
    not_fulfilled_reason   varchar(255)
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

create table if not exists intent_Event_Record(
    id varchar(255) DEFAULT random_uuid(),
    intentId varchar(255),
    intentName varchar(255),
    intentStatus varchar (225),
    operateType varchar (225)
    );
