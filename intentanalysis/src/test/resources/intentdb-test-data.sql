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

 Date: 26/07/2022 10:12:23
*/

-- ----------------------------
MERGE INTO intent (intent_id, intent_name)KEY(intent_id)values ('1234','test-intent');


MERGE INTO expectation (expectation_id, expectation_name, target_moi, intent_id)KEY(expectation_id)values ('2234','test-expectation',null, '1234');
