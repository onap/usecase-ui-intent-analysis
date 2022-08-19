/*
 * Copyright (C) 2022 CMCC, Inc. and others. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onap.usecaseui.intentanalysis.bean.models;

import lombok.Data;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentFunctionType;
import org.onap.usecaseui.intentanalysis.bean.enums.SupportArea;
import org.onap.usecaseui.intentanalysis.bean.enums.SupportInterface;

import java.util.List;

@Data
public class IntentManagementFunctionRegInfo {
    private String id;
    private String description;
    private List<SupportArea> supportArea;  //ͨ��intentname   cll  ͨ��intentName
    private String supportModel; // expectation  expectationtarget  targetCondition  value
    private List<SupportInterface> supportInterfaces; //
    private String handleName;
    private IntentFunctionType intentFunctionType;//out  or  in

}
