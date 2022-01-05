package cn.beau.component;

import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池处理类
 *
 * @author: liushilin
 * @date: 2021/4/23 2:35 下午
 */
@Component
public class TheadPoolComponent {
    private final ExecutorService theadPool = new ThreadPoolExecutor(
        5,
        20,
        10,
        TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(3000)
    );

    /**
     * 数据魔方任务处理
     *
     * @param runnable
     */
    public void addTask(Runnable runnable) {
        theadPool.submit(runnable);
    }

    /**
     * 发送线程池关闭指令、等待所有任务执行完再关闭
     */
    public void shutdown() {
        theadPool.shutdown();
    }

    /**
     * 返回 线程池 是否已暂停
     *
     * @return
     */
    public Boolean isTerminated() {
        return theadPool.isTerminated();
    }
}
