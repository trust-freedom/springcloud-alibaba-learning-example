package com.freedom.springcloud.alibaba.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlowRuleThreadTestController {
    private static final Logger logger = LoggerFactory.getLogger(FlowRuleThreadTestController.class);


    /**
     *
     * @param delayInSecond
     * @return
     */
    @RequestMapping("/httpSentinelResurce/{delayInSecond}")
    public String httpSentinelResurce(@PathVariable int delayInSecond){
        long start = System.currentTimeMillis();
        logger.info("=========httpSentinelResurce call, delayInSecond=" + delayInSecond);

        try {
            Thread.sleep(delayInSecond * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long during = System.currentTimeMillis() - start;

        return "Hello, this is httpSentinelResurce. Execution time " + during + "ms.";
    }


    /**
     * 基于并发线程数的流量控制规则测试
     * @param delayInSecond
     * @return
     */
    @SentinelResource( value="flowRuleThreadTest", blockHandler="flowRuleThreadTestBlockHandler", fallback="flowRuleThreadTestFallback" )
    @RequestMapping("/flowRuleThreadTest/{delayInSecond}")
    public String flowRuleThreadTest(@PathVariable int delayInSecond) throws Exception {
        long start = System.currentTimeMillis();
        logger.info("=========flowRuleThreadTest call, delayInSecond=" + delayInSecond);

        try {
            Thread.sleep(delayInSecond * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long during = System.currentTimeMillis() - start;

        return "Hello, this is flowRuleThreadTest. Execution time " + during + "ms.";
    }

    // 限流处理器（BlockException）
    public String flowRuleThreadTestBlockHandler(int delayInSecond, BlockException ex) throws Exception {
        logger.error("flowRuleThreadTest 请求被限流", ex);

        return "flowRuleThreadTest 请求被限流";
    }

    // 熔断降级（DegradeException）
    public String flowRuleThreadTestFallback(int delayInSecond) throws Exception {
        logger.info("flowRuleThreadTest 请求被降级");

        return "flowRuleThreadTest 请求被降级";
    }


}
