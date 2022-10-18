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
-- Records of intent
-- ----------------------------
MERGE INTO intent (intent_id, intent_name) KEY (intent_id)
values ('intentId1', 'CLL Business intent');
MERGE INTO intent (intent_id, intent_name) KEY (intent_id)
values ('intentId2', 'CLL Business intent');
MERGE INTO intent (intent_id, intent_name) KEY (intent_id)
values ('intent without affiliate', 'CLL Business intent');

-- ----------------------------
-- Records of expectation
-- ----------------------------
MERGE INTO expectation (expectation_id, expectation_name, expectation_type, intent_id) KEY (expectation_id)
values ('expectationId1', 'CLL Service Expectation', 'DELIVERY', 'intentId1');
MERGE INTO expectation (expectation_id, expectation_name, expectation_type, intent_id) KEY (expectation_id)
values ('expectationId2', 'CLL Assurance Expectation', 'ASSURANCE', 'intentId1');
MERGE INTO expectation (expectation_id, expectation_name, expectation_type, intent_id) KEY (expectation_id)
values ('expectationId3', 'CLL Service Expectation', 'DELIVERY', 'intentId2');
MERGE INTO expectation (expectation_id, expectation_name, expectation_type, intent_id) KEY (expectation_id)
values ('expectationId4', 'CLL Service Expectation', 'DELIVERY', 'intentId2');
MERGE INTO expectation (expectation_id, expectation_name, expectation_type, intent_id) KEY (expectation_id)
values ('expectation without affiliate', 'CLL Assurance Expectation', 'DELIVERY', 'intentId2');
MERGE INTO expectation (expectation_id, expectation_name, expectation_type, intent_id) KEY (expectation_id)
values ('expectation without affiliate 2', 'CLL Assurance Expectation', 'ASSURANCE', 'intentId2');

-- ----------------------------
-- Records of expectation_object
-- ----------------------------
MERGE INTO expectation_object (object_id, object_type, object_instance, expectation_id) KEY (object_id)
values ('b1bc45a6-f396-4b65-857d-6d2312bfb352', 'SLICING', '', 'expectationId1');
MERGE INTO expectation_object (object_id, object_type, object_instance, expectation_id) KEY (object_id)
values ('931a8690-fa61-4c2b-a387-9e0fa6152136', 'CCVPN', '', 'expectationId2');
MERGE INTO expectation_object (object_id, object_type, object_instance, expectation_id) KEY (object_id)
values ('3f36bf22-3416-4150-8005-cdc406a43310', 'CCVPN', '', 'expectationId3');

-- ----------------------------
-- Records of expectation_target
-- ----------------------------
MERGE INTO expectation_target (target_id, target_name, expectation_id) KEY (target_id)
values ('target1-1', 'source', 'expectationId1');
MERGE INTO expectation_target (target_id, target_name, expectation_id) KEY (target_id)
values ('target1-2', 'destination', 'expectationId1');
MERGE INTO expectation_target (target_id, target_name, expectation_id) KEY (target_id)
values ('target1-3', 'bandwidth', 'expectationId1');
MERGE INTO expectation_target (target_id, target_name, expectation_id) KEY (target_id)
values ('target2-1', 'bandwidthAssurance', 'expectationId2');
MERGE INTO expectation_target (target_id, target_name, expectation_id) KEY (target_id)
values ('target2-2', 'bandwidthAssurance', 'expectationId2');
MERGE INTO expectation_target (target_id, target_name, expectation_id) KEY (target_id)
values ('target2-3', 'bandwidthAssurance', 'expectationId2');
MERGE INTO expectation_target (target_id, target_name, expectation_id) KEY (target_id)
values ('target3-1', 'source', 'expectationId3');
MERGE INTO expectation_target (target_id, target_name, expectation_id) KEY (target_id)
values ('target4-1', 'source', 'expectationId4');

-- ----------------------------
-- Records of condition
-- ----------------------------
MERGE INTO condition (condition_id, condition_name, operator_type, condition_value, parent_id) KEY (condition_id)
values ('condition1-1', 'condition of the cll service source', 'EQUALTO', 'Company A', 'target1-1');
MERGE INTO condition (condition_id, condition_name, operator_type, condition_value, parent_id) KEY (condition_id)
values ('condition1-2', 'condition of the cll service destination', 'EQUALTO', 'Cloud 1', 'target1-2');
MERGE INTO condition (condition_id, condition_name, operator_type, condition_value, parent_id) KEY (condition_id)
values ('condition1-3', 'condition of the cll service bandwidth', 'EQUALTO', '1000', 'target1-3');
MERGE INTO condition (condition_id, condition_name, operator_type, condition_value, parent_id) KEY (condition_id)
values ('condition2-1', 'condition of the cll service bandwidth', 'EQUALTO', 'true', 'target2-1');
MERGE INTO condition (condition_id, condition_name, operator_type, condition_value, parent_id) KEY (condition_id)
values ('condition3-1', 'condition of the cll service source', 'EQUALTO', 'Company A', 'target3-1');

-- ----------------------------
-- Records of context
-- ----------------------------
MERGE INTO context (context_id, context_name, parent_id) KEY (context_id)
values ('d64f3a5f-b091-40a6-bca0-1bc6b1ce8f43', 'intentContextName', 'intentId1');
MERGE INTO context (context_id, context_name, parent_id) KEY (context_id)
values ('72f6c546-f234-4be5-a2fe-5740139e20cb', 'intentContextName', 'intentId2');

-- ----------------------------
-- Records of fulfilment_info
-- ----------------------------
MERGE INTO fulfilment_info (fulfilment_info_id, fulfilment_info_status, not_fulfilled_state, not_fulfilled_reason) KEY (fulfilment_info_id)
values ('intentId1', 'NOT_FULFILLED', 'COMPLIANT', 'NotFulfilledReason');
MERGE INTO fulfilment_info (fulfilment_info_id, fulfilment_info_status, not_fulfilled_state, not_fulfilled_reason) KEY (fulfilment_info_id)
values ('intentId2', 'NOT_FULFILLED', 'COMPLIANT', 'NotFulfilledReason');


-- ----------------------------
-- Records of intent_management_function_reg_info
-- ----------------------------
INSERT INTO intent_management_function_reg_info(imfr_info_id,imfr_info_description,support_area,support_model,support_interfaces,handle_name,intent_function_type)
VALUES ('CLLBusinessId','CLLBusiness','CLLBUSINESS',null,'CREATE,DELETE,UPDATE,SEARCH','CLLBusinessIntentManagementFunction','INTERNALFUNCTION');

INSERT INTO intent_management_function_reg_info(imfr_info_id,imfr_info_description,support_area,support_model,support_interfaces,handle_name,intent_function_type)
VALUES ('CLLDeliveryId','CLLDelivery','CLLBUSINESS,DELIVERY',null,'CREATE,DELETE,UPDATE,SEARCH','CLLDeliveryIntentManagementFunction','INTERNALFUNCTION');

INSERT INTO intent_management_function_reg_info(imfr_info_id,imfr_info_description,support_area,support_model,support_interfaces,handle_name,intent_function_type)
VALUES ('CLLAssuranceId','CLLAssurance','CLLBUSINESS,ASSURANCE',null,'CREATE,DELETE,UPDATE,SEARCH','CLLAssuranceIntentManagementFunction','INTERNALFUNCTION');