package com.freedom.springcloud.alibaba.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlowRuleQPSTestController {
    private static final Logger logger = LoggerFactory.getLogger(FlowRuleQPSTestController.class);


    /**
     * 基于QPS的流量控制规则测试
     * @return
     */
    @SentinelResource( value="flowRuleQPSTest", blockHandler="flowRuleQPSTestBlockHandler", fallback="flowRuleQPSTestFallback" )
    @RequestMapping("/flowRuleQPSTest")
    public String flowRuleQPSTest() throws Exception {
        logger.info("=========flowRuleQPSTest call");

        return "Hello, this is flowRuleQPSTest.";
    }

    // 限流处理器（BlockException）
    public String flowRuleQPSTestBlockHandler(BlockException ex) throws Exception {
        logger.error("flowRuleQPSTest 请求被限流", ex);

        return "flowRuleQPSTest 请求被限流";
    }

    // 熔断降级（DegradeException）
    public String flowRuleQPSTestFallback() throws Exception {
        logger.info("flowRuleQPSTest 请求被降级");

        return "flowRuleQPSTest 请求被降级";
    }


}
