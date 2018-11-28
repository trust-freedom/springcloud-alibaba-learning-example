package com.freedom.springcloud.alibaba;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SentinelClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SentinelClientApplication.class, args);

		/**
		 * 初始化流量控制规则
		 *   - 并发线程数量
		 *   - QPS
		 */
		initFlowRules();

		/**
		 * 初始化熔断降级规则
		 *   - 异常数量
		 *   - 平均响应时间
		 *   - 异常比例
		 * 注意： 多次使用DegradeRuleManager.loadRules(rules)，后面会覆盖前面的
		 */
		initDegradeRules();

	}


	/**
	 * 初始化流量控制规则
	 * FlowRule没有恢复时间窗口的概念
	 */
	private static void initFlowRules(){
		List<FlowRule> rules = new ArrayList<FlowRule>();

		/**
		 * 并发线程数量
		 */
		FlowRule rule1 = new FlowRule();
		rule1.setResource("flowRuleThreadTest");
		rule1.setCount(2); //流控阈值设置为2
		rule1.setGrade(RuleConstant.FLOW_GRADE_THREAD); //并发线程数量控制
		rule1.setLimitApp("default");
		rules.add(rule1);

		/**
		 * 并发线程数量
		 */
		FlowRule rule2 = new FlowRule();
		rule2.setResource("flowRuleQPSTest");
		rule2.setCount(2); //QPS阈值2
		rule2.setGrade(RuleConstant.FLOW_GRADE_QPS); //QPS流控
		rule2.setLimitApp("default");
		rules.add(rule2);

		FlowRuleManager.loadRules(rules);
	}


	/**
	 * 初始化熔断降级规则
	 */
	private static void initDegradeRules(){
		List<DegradeRule> rules = new ArrayList<>();

		/**
		 * 初始化熔断降级规则 - 异常数量
		 *
		 * 60s内发生的业务异常大于阈值即熔断降级
		 * 1.3.0-GA版本仍需要使用Tracer.trace(e)记录非BlockException的业务异常
		 */
		DegradeRule rule1 = new DegradeRule();
		rule1.setResource("degradeRuleExceptionCountTest");
		rule1.setCount(2);
		rule1.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT); // Degrade by biz exception count in the last 60 seconds.
		rule1.setTimeWindow(10);
		rules.add(rule1);


		/**
		 * 初始化熔断降级规则 - 平均响应时间
		 */
		DegradeRule rule2 = new DegradeRule();
		rule2.setResource("degradeRuleRTTest");
		rule2.setCount(500);  //平均响应时间阈值，单位ms
		rule2.setGrade(RuleConstant.DEGRADE_GRADE_RT);
		rule2.setTimeWindow(10);  //熔断降级持续的时间窗口，单位秒
		rules.add(rule2);


		/**
		 * 初始化熔断降级规则 - 异常比例
		 */
		DegradeRule rule3 = new DegradeRule();
		rule3.setResource("degradeRuleExceptionRatioTest");
		rule3.setCount(0.1);
		rule3.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO);
		rule3.setTimeWindow(10);  //熔断降级持续的时间窗口，单位秒
		rules.add(rule3);

		DegradeRuleManager.loadRules(rules);
	}


}
