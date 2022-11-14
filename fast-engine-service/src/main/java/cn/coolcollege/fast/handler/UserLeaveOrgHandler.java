package cn.coolcollege.fast.handler;

import cn.coolcollege.fast.event.BaseEvent;
import cn.coolcollege.fast.event.org.UserLeaveOrgEvent;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * @author pk
 */
@Service
public class UserLeaveOrgHandler extends AbstractEventHandler {

    @Subscribe
    public void onEvent(UserLeaveOrgEvent event) {
        if (event == null) {
            return;
        }
        logger.info("handle userLeaveOrgEvent, event={}", event);
        super.handleEvent(event);
    }

    @Override
    boolean checkEvent(BaseEvent baseEvent) {
        UserLeaveOrgEvent event = (UserLeaveOrgEvent) baseEvent;
        if (CollectionUtils.isEmpty(event.getUsers())) {
            return false;
        }
        return true;
    }

    @Override
    void doHandle(BaseEvent baseEvent) {
        //由于用户离职对部门修改事件不会产品任何影响,暂时不做处理
    }
}
