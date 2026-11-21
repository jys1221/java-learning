// ==========================================================================
// 文件作用：
//   让 demo5 启动时自动从 Nacos 读取【限流规则】和【熔断规则】，并装进 Sentinel。
//   另外还注册了 @SentinelResource 注解所需的"切面"——没有它，注解等于白写。
// ==========================================================================
package com.example.demo5;

// Sentinel 的"数据源"通用接口：能从某个地方（这里是 Nacos）读出配置数据
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
// Sentinel 官方提供的 Nacos 数据源实现：专门负责"连 Nacos、拉配置"
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
// 流控规则对象 + 流控规则管理器（限流用）
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
// 熔断规则对象 + 熔断规则管理器（熔断降级用）
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
// @SentinelResource 注解的"切面"：负责在方法调用前后统计耗时/异常，注解才能生效
import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
// JSON 解析工具（阿里 fastjson）：把 Nacos 里的 JSON 字符串转成 Java 对象列表
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
// @PostConstruct：标记这个方法在"对象创建完成之后"自动执行一次
import jakarta.annotation.PostConstruct;
// @Value：从配置文件里读取某个键的值
import org.springframework.beans.factory.annotation.Value;
// @Bean：把方法返回的对象注册进 Spring 容器
import org.springframework.context.annotation.Bean;
// @Configuration：标记这是"配置类"，Spring 启动时会加载它
import org.springframework.context.annotation.Configuration;

// 集合类型：这里用 List 装一堆规则
import java.util.List;

// @Configuration：告诉 Spring "这是配置类，启动时请加载我"
@Configuration
public class SentinelRuleConfig {

    // 从 application.properties 里读取 Nacos 地址（我们配的是 localhost:8848）
    @Value("${spring.cloud.nacos.server-addr}")
    private String nacosServerAddr;

    // Nacos 配置的分组名
    private static final String GROUP_ID = "DEFAULT_GROUP";

    // 限流规则在 Nacos 里的 Data ID
    private static final String FLOW_DATA_ID = "sentinel-demo5-flow-rules.json";

    // 熔断规则在 Nacos 里的 Data ID
    private static final String DEGRADE_DATA_ID = "sentinel-demo5-degrade-rules.json";

    // @PostConstruct：Spring 创建好本对象后自动执行一次，
    //   相当于"服务刚启动、还没接客，先把两类规则都装好"
    @PostConstruct
    public void loadRulesFromNacos() {
        loadFlowRules();        // 装限流规则
        loadDegradeRules();     // 装熔断规则
    }

    // 从 Nacos 加载"限流规则"
    private void loadFlowRules() {
        ReadableDataSource<String, List<FlowRule>> dataSource = new NacosDataSource<>(
                nacosServerAddr,        // Nacos 地址
                GROUP_ID,               // 分组
                FLOW_DATA_ID,           // Data ID
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {})
                // ↑ 把拉到的 JSON 字符串转成 List<FlowRule>
        );
        // 注册给流控管理器：启动即生效，之后 Nacos 一改这里自动更新
        FlowRuleManager.register2Property(dataSource.getProperty());
    }

    // 从 Nacos 加载"熔断规则"
    private void loadDegradeRules() {
        ReadableDataSource<String, List<DegradeRule>> dataSource = new NacosDataSource<>(
                nacosServerAddr,
                GROUP_ID,
                DEGRADE_DATA_ID,        // 熔断规则的 Data ID
                source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {})
                // ↑ 这次转成 List<DegradeRule>（熔断规则类型）
        );
        // 注册给熔断管理器
        DegradeRuleManager.register2Property(dataSource.getProperty());
    }

    // @Bean：把下面的对象交给 Spring 管理。
    // 这个切面是 @SentinelResource 注解的"发动机"：
    //   不注册它，注解写了也不会统计耗时/异常，熔断永远不会触发（这是最常见的坑）
    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }
}
