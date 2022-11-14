package cn.coolcollege.fast.aspect;

import net.coolcollege.platform.util.DateTimeUtil;
import net.coolcollege.platform.util.constants.MonitorLogStatusEnum;
import net.coolcollege.platform.util.model.BaseResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Author bai bin
 * @Date 2021/5/25 13:45
 */
@Aspect
@Component
public class MonitorLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(MonitorLogAspect.class);
    private static final String DEFAULT_ERR_MSG = "-";

    @Value("${service.name}")
    private String serviceName;

    @Pointcut("@annotation(net.coolcollege.platform.util.annonation.MonitorLog)")
    public void annotationPointCut() {
    }

    @Around("annotationPointCut()")
    public Object aroundPointCut(ProceedingJoinPoint joinPoint) {
        long st = System.currentTimeMillis();
        String status = MonitorLogStatusEnum.SUCCESS.getValue();
        BaseResult baseResult = null;
        try {
            baseResult = (BaseResult) joinPoint.proceed();
        } catch (Throwable t) {
            status = MonitorLogStatusEnum.EXCEPTION.getValue();
            logger.error("doService err", t);
        }
        long et = System.currentTimeMillis();
        long cost = et - st;

        String errMsg = DEFAULT_ERR_MSG;
        if (null != baseResult) {
            Boolean success = baseResult.getSuccess();
            if (!success) {
                status = MonitorLogStatusEnum.FAILED.getValue();
                errMsg = baseResult.getMessage();
            }
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Object target = joinPoint.getTarget();

        String formatStartTime = DateTimeUtil.convertTimestamp2String(st, DateTimeUtil.TIME_FORMAT_WITH_MILLI_SECOND);
        logger.info("{},{},{},{},{},{},{}", formatStartTime, serviceName, target.getClass().getName(), method.getName(),
                status, cost, errMsg);
        return baseResult;
    }
}
