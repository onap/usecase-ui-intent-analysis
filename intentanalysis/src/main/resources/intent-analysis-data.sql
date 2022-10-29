-- ----------------------------
-- Records of intent_management_function_reg_info
-- ----------------------------

insert into intent_management_function_reg_info(imfr_info_id, imfr_info_description, support_area, support_model, support_interfaces, handle_name, intent_function_type) select 'CLLBusinessId','CLLBusiness','CLLBUSINESS',null,'CREATE,DELETE,UPDATE,SEARCH','CLLBusinessIntentManagementFunction','INTERNALFUNCTION'  where not exists(select * from intent_management_function_reg_info where imfr_info_id='CLLBusinessId' )
insert into intent_management_function_reg_info(imfr_info_id, imfr_info_description, support_area, support_model, support_interfaces, handle_name, intent_function_type) select 'CLLDeliveryId','CLLDelivery','CLLBUSINESS,DELIVERY',null,'CREATE,DELETE,UPDATE,SEARCH','CLLDeliveryIntentManagementFunction','INTERNALFUNCTION'  where not exists(select * from intent_management_function_reg_info where imfr_info_id='CLLDeliveryId' )
insert into intent_management_function_reg_info(imfr_info_id, imfr_info_description, support_area, support_model, support_interfaces, handle_name, intent_function_type) select 'CLLAssuranceId','CLLAssurance','CLLBUSINESS,ASSURANCE',null,'CREATE,DELETE,UPDATE,SEARCH','CLLAssuranceIntentManagementFunction','INTERNALFUNCTION'  where not exists(select * from intent_management_function_reg_info where imfr_info_id='CLLAssuranceId' )

