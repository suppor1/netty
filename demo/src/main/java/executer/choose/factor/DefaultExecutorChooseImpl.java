package executer.choose.factor;

import io.netty.util.concurrent.EventExecutor;

/**
 * @author k.jiang
 * 2020/4/19 下午11:40
 * Description 默认的执行器
 */
public class DefaultExecutorChooseImpl implements EventExecutorChooserFacotry{
    @Override
    public EventExecutorChoode choose() {
        return new DefalutExecutorChoo();
    }

    private class DefalutExecutorChoo implements EventExecutorChoode {
        @Override
        public EventExecutor next() {
            return null;
        }
    }
}
