package crab.threadpool;


/**
 * 线程池（暂未使用）
 * 基本功能：
 * 1.执行一个Job.
 * 2.得到正在等待的线程数.
 * 3.增加工作线程.
 * 4.减少工作线程.
 * @param <Job>
 */
public interface ThreadPool<Job extends Runnable> {

    //执行一个Job,这个Job需要实现Runnable
    void execute(Job job);
    //关闭线程池
    void shutdown();
    //增加工作线程
    void addWorkers(int num);
    //减少工作线程
    void removeWorkers(int num);
    //得到正在等待的线程数量
    int getJobSize();

}
