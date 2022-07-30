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
DROP TABLE IF  EXISTS intent;
DROP TABLE IF  EXISTS  expectation;
DROP TABLE  IF  EXISTS state;

CREATE TABLE if NOT EXISTS intent(
    intent_id varchar(255),
    intent_name varchar(255),
    CONSTRAINT intent_test_task_pkey PRIMARY KEY (intent_id)
);

create table if not exists expectation(
    expectation_id varchar(255),
    expectation_name varchar(255),
    target_moi varchar(255),
    intent_id varchar(255),
    CONSTRAINT expectation_test_task_pkey PRIMARY KEY (expectation_id)
);

create table if not exists state(
    state_id varchar(255),
    state_name varchar(255),
    is_satisfied boolean,
    condition varchar(255),
    expectation_id varchar(255),
    CONSTRAINT state_test_task_pkey PRIMARY KEY (state_id)
);
