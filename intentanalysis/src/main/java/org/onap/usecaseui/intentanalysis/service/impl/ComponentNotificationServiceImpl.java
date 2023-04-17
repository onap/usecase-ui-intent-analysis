package org.onap.usecaseui.intentanalysis.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.onap.usecaseui.intentanalysis.adapters.dmaap.NotificationEventModel;
import org.onap.usecaseui.intentanalysis.service.ComponentNotificationService;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class ComponentNotificationServiceImpl  implements ComponentNotificationService {
    /**
     * transform NoticationEventModel to fulfillment
     * @param eventModel
     */
    @Override
    public void callBack(NotificationEventModel eventModel) {
        log.info("callBack begin");
    }
}
