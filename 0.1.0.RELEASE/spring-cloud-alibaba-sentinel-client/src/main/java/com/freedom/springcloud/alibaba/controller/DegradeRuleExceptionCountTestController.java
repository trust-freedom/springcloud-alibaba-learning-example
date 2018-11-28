package com.freedom.springcloud.alibaba.controller;

import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class DegradeRuleExceptionCountTestController {
    private static final Logger logger = LoggerFactory.getLogger(DegradeRuleExceptionCountTestController.class);

    //private static AtomicInteger exceptionCount = new AtomicInteger(0);  //记录异常数


    /**
     * 熔断降级测试 - 异常数量
     * @return
     * @throws Exception
     */
    @SentinelResource( value="degradeRuleExceptionCountTest",
                       blockHandler="degradeRuleExceptionCountTestBlockHandler",
                       fallback="degradeRuleExceptionCountTestFallback" )
    @RequestMapping("/degradeRuleExceptionCountTest")
    public String degradeRuleExceptionCountTest() throws Exception {
        logger.info("=========degradeRuleExceptionCountTest call");

        try {
            //模拟异常
            System.out.println(1/0);
        }
        catch (Exception e){
            // 如果不是BlockException，使用Tracer.trace(e)记录异常
            if (!BlockException.isBlockException(e)) {
                Tracer.trace(e);  //记录异常

                //exceptionCount.getAndIncrement();  //异常数加1
            }

            throw e; //上抛异常
        }

        return "Hello, this is degradeRuleExceptionCountTest.";
    }


    // 限流处理器（BlockException）
    public String degradeRuleExceptionCountTestBlockHandler(BlockException ex) throws Exception {
        logger.error("degradeRuleExceptionCountTest 请求被限流", ex);

        return "degradeRuleExceptionCountTest 请求被限流";
    }

    // 熔断降级（DegradeException）
    public String degradeRuleExceptionCountTestFallback() throws Exception {
        logger.info("degradeRuleExceptionCountTest 请求被降级");

        // 熔断降级后重置异常数
        //exceptionCount.set(0);

        return "degradeRuleExceptionCountTest 请求被降级";
    }

}
