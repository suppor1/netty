package executer.choose.factor;

import io.netty.util.concurrent.EventExecutor;

/**
 * @author k.jiang
 * 2020/4/19 下午11:37
 * Description 工程方法模式实现选择器
 */
public interface EventExecutorChooserFacotry {

    EventExecutorChoode choose();

    interface EventExecutorChoode {
        EventExecutor next();
    }
}
