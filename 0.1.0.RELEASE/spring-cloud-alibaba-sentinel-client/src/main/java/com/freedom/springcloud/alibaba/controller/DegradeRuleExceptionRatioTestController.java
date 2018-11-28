package com.freedom.springcloud.alibaba.controller;

import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class DegradeRuleExceptionRatioTestController {
    private static final Logger logger = LoggerFactory.getLogger(DegradeRuleExceptionRatioTestController.class);


    /**
     * 熔断降级测试 - 异常比例
     * @return
     * @throws Exception
     */
    @SentinelResource( value="degradeRuleExceptionRatioTest",
                       blockHandler="degradeRuleExceptionRatioTestBlockHandler",
                       fallback="degradeRuleExceptionRatioTestFallback" )
    @RequestMapping("/degradeRuleExceptionRatioTest")
    public String degradeRuleExceptionRatioTest() throws Exception {
        logger.info("=========degradeRuleExceptionRatioTest call");

        try {
            //模拟异常
            System.out.println(1/0);
        }
        catch (Exception e){
            // 如果不是BlockException，使用Tracer.trace(e)记录异常
            if (!BlockException.isBlockException(e)) {
                Tracer.trace(e);  //记录异常
            }

            throw e; //上抛异常
        }

        return "Hello, this is degradeRuleExceptionRatioTest.";
    }


    // 限流处理器（BlockException）
    public String degradeRuleExceptionRatioTestBlockHandler(BlockException ex) throws Exception {
        logger.error("degradeRuleExceptionRatioTest 请求被限流", ex);

        return "degradeRuleExceptionRatioTest 请求被限流";
    }

    // 熔断降级（DegradeException）
    public String degradeRuleExceptionRatioTestFallback() throws Exception {
        logger.info("degradeRuleExceptionRatioTest 请求被降级");

        return "degradeRuleExceptionRatioTest 请求被降级";
    }

}
