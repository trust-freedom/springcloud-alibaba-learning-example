package com.freedom.springcloud.alibaba.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DegradeRuleRTTestController {
    private static final Logger logger = LoggerFactory.getLogger(DegradeRuleRTTestController.class);



    /**
     * 熔断降级测试 - 平均响应时间
     * @param delayInSecond
     * @return
     */
    @SentinelResource( value="degradeRuleRTTest", blockHandler="degradeRuleRTTestBlockHandler", fallback="degradeRuleRTTestFallback" )
    @RequestMapping("/degradeRuleRTTest/{delayInSecond}")
    public String degradeRuleRTTest(@PathVariable int delayInSecond) throws Exception {
        long start = System.currentTimeMillis();
        logger.info("=========degradeRuleRTTest call, delayInSecond=" + delayInSecond);

        try {
            Thread.sleep(delayInSecond * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long during = System.currentTimeMillis() - start;

        return "Hello, this is degradeRuleRTTest. Execution time " + during + "ms.";
    }

    // 限流处理器（BlockException）
    public String degradeRuleRTTestBlockHandler(int delayInSecond, BlockException ex) throws Exception {
        logger.error("degradeRuleRTTest 请求被限流", ex);

        return "degradeRuleRTTest 请求被限流";
    }

    // 熔断降级（DegradeException）
    public String degradeRuleRTTestFallback(int delayInSecond) throws Exception {
        logger.info("degradeRuleRTTest 请求被降级");

        return "degradeRuleRTTest 请求被降级";
    }


}
