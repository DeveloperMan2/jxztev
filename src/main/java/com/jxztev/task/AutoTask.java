/**
 *
 */
package com.jxztev.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author winnerlbm
 * @date 2019年7月15日
 * @desc
 */
@Component
@PropertySource(value = {"classpath:task.properties"}, encoding = "utf-8")
public class AutoTask {
    private Logger logger = LogManager.getLogger(this.getClass());

    /*@Autowired
    @Qualifier("findSlideRainService")
    private IFindSlideRainService findSlideRainService;*/

    @Scheduled(cron = "${task.gap0}")
    private void executeTask0() {
        logger.info("每隔5秒执行一次计算...");
    }

    @Scheduled(cron = "${task.gap1}")
    private void executeTask1() {
        logger.info("每隔10秒执行一次计算...");
    }

    @Scheduled(cron = "${task.gap2}")
    private void executeTask2() {
        logger.info("每隔15秒执行一次计算...");
    }

    @Scheduled(cron = "${task.gap3}")
    private void executeTask3() {
        logger.info("每隔20秒执行一次计算...");
    }
}
