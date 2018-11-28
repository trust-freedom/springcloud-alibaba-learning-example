package com.freedom.springcloud.alibaba.controller;

import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlowRuleTestController {
    private static final Logger logger = LoggerFactory.getLogger(FlowRuleTestController.class);


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
     * 流量控制规则测试方法
     * @param delayInSecond
     * @return
     */
    @SentinelResource( value="flowRuleTest", blockHandler="flowRuleTestBlockHandler", fallback="flowRuleTestFallback" )
    @RequestMapping("/flowRuleTest/{delayInSecond}")
    public String flowRuleTest(@PathVariable int delayInSecond) throws Exception {
        long start = System.currentTimeMillis();
        logger.info("=========flowRuleTest call, delayInSecond=" + delayInSecond);

        try {
            Thread.sleep(delayInSecond * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long during = System.currentTimeMillis() - start;

        return "Hello, this is flowRuleTest. Execution time " + during + "ms.";
    }

    // 限流处理器（BlockException）
    public String flowRuleTestBlockHandler(int delayInSecond, BlockException ex) throws Exception {
        logger.error("flowRuleTest 请求被限流", ex);

        return "flowRuleTest 请求被限流";
    }

    // 熔断降级（DegradeException）
    public String flowRuleTestFallback(int delayInSecond) throws Exception {
        logger.info("flowRuleTest 请求被降级");

        return "flowRuleTest 请求被降级";
    }


}
