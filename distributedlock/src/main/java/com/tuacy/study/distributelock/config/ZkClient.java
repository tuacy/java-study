package com.tuacy.study.distributelock.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @name: ZkClient
 * @author: tuacy.
 * @date: 2019/8/7.
 * @version: 1.0
 * @Description:
 */
public class ZkClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * zookeeper客户端实例
     */
    private CuratorFramework client;
    /**
     * 服务器列表，格式host1:port1,host2:port2,...
     */
    private String zookeeperServer;
    /**
     * 会话超时时间，单位毫秒，默认60000ms
     */
    private int sessionTimeoutMs;
    /**
     * 连接创建超时时间，单位毫秒，默认60000ms
     */
    private int connectionTimeoutMs;
    /**
     * 重试之间等待的初始时间
     */
    private int baseSleepTimeMs;
    /**
     * 当连接异常时的重试次数
     */
    private int maxRetries;
    /**
     * 为了实现不同的Zookeeper业务之间的隔离，有的时候需要为每个业务分配一个独立的命名空间
     */
    private String namespace;

    public void setZookeeperServer(String zookeeperServer) {
        this.zookeeperServer = zookeeperServer;
    }

    public String getZookeeperServer() {
        return zookeeperServer;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setBaseSleepTimeMs(int baseSleepTimeMs) {
        this.baseSleepTimeMs = baseSleepTimeMs;
    }

    public int getBaseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * spring 自动调用,不需要我们主动调用
     */
    public void init() {
        // 创建客户端
        // 重连规则
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        client = CuratorFrameworkFactory.builder()
                .connectString(zookeeperServer)
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(sessionTimeoutMs)
                .connectionTimeoutMs(connectionTimeoutMs)
                .namespace(namespace)
                .build();
        // 启动客户端,连接服务器
        client.start();
    }

    /**
     * spring 自动调用,不需要我们主动调用
     */
    public void stop() {
        // 关闭客户端
        client.close();
    }

    /**
     * 获取 zookeeper 客户端对象
     *
     * @return CuratorFramework
     */
    public CuratorFramework getClient() {
        return client;
    }

    /**
     * 同步-获取某个节点的所有子节点路径
     *
     * @param path 目录
     * @return children
     * @throws Exception errors
     */
    public List<String> getChildrenSync(String path) throws Exception {
        return client.getChildren()
                .forPath(path);
    }

    /**
     * 异步-获取某个节点的所有子节点路径
     *
     * @param path     目录
     * @param callback 回调
     * @throws Exception errors
     */
    public void getChildrenAsync(String path, BackgroundCallback callback) throws Exception {
        client.getChildren()
                .inBackground(callback)
                .forPath(path);
    }

    /**
     * 异步-获取某个节点的所有子节点路径
     *
     * @param path     目录
     * @param callback 回调
     * @param executor 回调在哪里执行
     * @throws Exception errors
     */
    public void getChildrenAsync(String path, BackgroundCallback callback, Executor executor) throws Exception {
        client.getChildren()
                .inBackground(callback, executor)
                .forPath(path);

    }

    /**
     * 同步 创建持久化节点
     *
     * @param path 节点路径
     * @throws Exception errors
     */
    public void createPersistentNodeSync(String path) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.PERSISTENT)
                .forPath(path);
    }

    /**
     * 异步 创建持久化节点
     *
     * @param path     节点路径
     * @param callback 回调
     * @throws Exception errors
     */
    public void createPersistentNodeAsync(String path, BackgroundCallback callback) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.PERSISTENT)
                .inBackground(callback)
                .forPath(path);
    }

    /**
     * 异步 创建持久化节点
     *
     * @param path     节点路径
     * @param callback 回调
     * @param executor 回调在哪里执行
     * @throws Exception errors
     */
    public void createPersistentNodeAsync(String path, BackgroundCallback callback, Executor executor) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.PERSISTENT)
                .inBackground(callback, executor)
                .forPath(path);
    }

    /**
     * 同步-创建持久化节点
     *
     * @param path 节点路径
     * @param data 节点对应的值
     * @throws Exception errors
     */
    public void createPersistentNodeSync(String path, byte[] data) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.PERSISTENT)
                .forPath(path, data);

    }

    /**
     * 异步-创建持久化节点
     *
     * @param path     节点路径
     * @param data     节点对应的值
     * @param callback 回调
     * @throws Exception errors
     */
    public void createPersistentNodeAsync(String path, byte[] data, BackgroundCallback callback) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.PERSISTENT)
                .inBackground(callback)
                .forPath(path, data);
    }

    /**
     * 异步-创建持久化节点
     *
     * @param path     节点路径
     * @param data     节点对应的值
     * @param callback 回调
     * @param executor 回调在哪里执行
     * @throws Exception errors
     */
    public void createPersistentNodeAsync(String path, byte[] data, BackgroundCallback callback, Executor executor) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.PERSISTENT)
                .inBackground(callback, executor)
                .forPath(path, data);
    }

    /**
     * 同步-创建持久化并且带序列号的节点
     *
     * @param path 路径
     * @throws Exception errors
     */
    public void createPersistentSequentialNodeSync(String path) throws Exception {

        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .forPath(path);
    }

    /**
     * 异步-创建持久化并且带序列号的节点
     *
     * @param path     路径
     * @param callback 回调
     * @throws Exception errors
     */
    public void createPersistentSequentialNodeAsync(String path, BackgroundCallback callback) throws Exception {

        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .inBackground(callback)
                .forPath(path);
    }

    /**
     * 异步-创建持久化并且带序列号的节点
     *
     * @param path     路径
     * @param callback 回调
     * @param executor 回调在哪里执行
     * @throws Exception errors
     */
    public void createPersistentSequentialNodeAsync(String path, BackgroundCallback callback, Executors executor) throws Exception {

        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .inBackground(callback, executor)
                .forPath(path);
    }

    /**
     * 同步-创建临时并且带序列号的节点
     *
     * @param path 路径
     * @param data 节点对应数据
     * @throws Exception errors
     */
    public void createPersistentSequentialNodeSync(String path, byte[] data) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .forPath(path, data);
    }

    /**
     * 异步-创建临时并且带序列号的节点
     *
     * @param path     路径
     * @param data     节点对应数据
     * @param callback 回调
     * @throws Exception errors
     */
    public void createPersistentSequentialNodeAsync(String path, byte[] data, BackgroundCallback callback) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .inBackground(callback)
                .forPath(path, data);
    }

    /**
     * 异步-创建临时并且带序列号的节点
     *
     * @param path     路径
     * @param data     节点对应数据
     * @param callback 回调
     * @param executor 回调在哪里执行
     * @throws Exception errors
     */
    public void createPersistentSequentialNodeAsync(String path, byte[] data, BackgroundCallback callback, Executors executor) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .inBackground(callback, executor)
                .forPath(path, data);
    }

    /**
     * 同步-创建临时节点
     *
     * @param path 路径
     * @throws Exception errors
     */
    public void createEphemeralNodeSync(String path) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path);
    }

    /**
     * 异步-创建临时节点
     *
     * @param path     路径
     * @param callback 回调
     * @throws Exception errors
     */
    public void createEphemeralNodeAsync(String path, BackgroundCallback callback) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.EPHEMERAL)
                .inBackground(callback)
                .forPath(path);
    }

    /**
     * 异步-创建临时节点
     *
     * @param path     路径
     * @param callback 回调
     * @param executor 回调在哪里执行
     * @throws Exception errors
     */
    public void createEphemeralNodeAsync(String path, BackgroundCallback callback, Executors executor) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.EPHEMERAL)
                .inBackground(callback, executor)
                .forPath(path);
    }

    /**
     * 同步-创建临时节点
     *
     * @param path 路径
     * @param data 节点对应数据
     * @throws Exception errors
     */
    public void createEphemeralNodeSync(String path, byte[] data) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path, data);
    }

    /**
     * 异步-创建临时节点
     *
     * @param path     路径
     * @param data     节点对应数据
     * @param callback 回调
     * @throws Exception errors
     */
    public void createEphemeralNodeAsync(String path, byte[] data, BackgroundCallback callback) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.EPHEMERAL)
                .inBackground(callback)
                .forPath(path, data);
    }

    /**
     * 异步-创建临时节点
     *
     * @param path     路径
     * @param data     节点对应数据
     * @param callback 回调
     * @param executor 回调在哪里执行
     * @throws Exception errors
     */
    public void createEphemeralNodeAsync(String path, byte[] data, BackgroundCallback callback, Executors executor) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.EPHEMERAL)
                .inBackground(callback, executor)
                .forPath(path, data);
    }

    /**
     * 同步-创建临时并且带序列号的节点
     *
     * @param path 路径
     * @throws Exception errors
     */
    public void createEphemeralSequentialNodeSync(String path) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath(path);
    }

    /**
     * 异步-创建临时并且带序列号的节点
     *
     * @param path     路径
     * @param callback 回调
     * @throws Exception errors
     */
    public void createEphemeralSequentialNodeAsync(String path, BackgroundCallback callback) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .inBackground(callback)
                .forPath(path);
    }

    /**
     * 异步-创建临时并且带序列号的节点
     *
     * @param path     路径
     * @param callback 回调
     * @param executor 回调在哪里执行
     * @throws Exception errors
     */
    public void createEphemeralSequentialNodeAsync(String path, BackgroundCallback callback, Executors executor) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .inBackground(callback, executor)
                .forPath(path);
    }

    /**
     * 同步-创建临时并且带序列号的节点
     *
     * @param path 路径
     * @param data 对应的值
     * @throws Exception errors
     */
    public void createEphemeralSequentialNodeSync(String path, byte[] data) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath(path, data);
    }

    /**
     * 异步-创建临时并且带序列号的节点
     *
     * @param path     路径
     * @param data     对应的值
     * @param callback 回调
     * @throws Exception errors
     */
    public void createEphemeralSequentialNodeAsync(String path, byte[] data, BackgroundCallback callback) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .inBackground(callback)
                .forPath(path, data);
    }

    /**
     * 异步-创建临时并且带序列号的节点
     *
     * @param path     路径
     * @param data     对应的值
     * @param callback 回调
     * @param executor 回调在哪里执行
     * @throws Exception errors
     */
    public void createEphemeralSequentialNodeAsync(String path, byte[] data, BackgroundCallback callback, Executors executor) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .inBackground(callback, executor)
                .forPath(path, data);
    }

    /**
     * 同步-创建节点
     *
     * @param crateMode 节点创建模式  PERSISTENT,PERSISTENT_SEQUENTIAL,EPHEMERAL,EPHEMERAL_SEQUENTIAL
     * @param path      路径
     * @throws Exception errors
     */
    public void createNodeSync(CreateMode crateMode, String path) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(crateMode) // 指定创建模式
                .forPath(path);
    }

    /**
     * 异步-创建节点
     *
     * @param crateMode 节点创建模式  PERSISTENT,PERSISTENT_SEQUENTIAL,EPHEMERAL,EPHEMERAL_SEQUENTIAL
     * @param path      路径
     * @param callback  回调
     * @throws Exception errors
     */
    public void createNodeAsync(CreateMode crateMode, String path, BackgroundCallback callback) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(crateMode) // 指定创建模式
                .inBackground(callback)
                .forPath(path);
    }

    /**
     * 异步-创建节点
     *
     * @param crateMode 节点创建模式  PERSISTENT,PERSISTENT_SEQUENTIAL,EPHEMERAL,EPHEMERAL_SEQUENTIAL
     * @param path      路径
     * @param callback  回调
     * @param executor  回调在哪里执行
     * @throws Exception errors
     */
    public void createNodeAsync(CreateMode crateMode, String path, BackgroundCallback callback, Executors executor) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(crateMode) // 指定创建模式
                .inBackground(callback, executor)
                .forPath(path);
    }

    /**
     * 同步-创建节点
     *
     * @param crateMode 节点创建模式  PERSISTENT,PERSISTENT_SEQUENTIAL,EPHEMERAL,EPHEMERAL_SEQUENTIAL
     * @param path      路径
     * @param data      对应的值
     * @throws Exception errors
     */
    public void createNodeSync(CreateMode crateMode, String path, byte[] data) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(crateMode)
                .forPath(path, data);
    }

    /**
     * 异步-创建节点
     *
     * @param crateMode 节点创建模式  PERSISTENT,PERSISTENT_SEQUENTIAL,EPHEMERAL,EPHEMERAL_SEQUENTIAL
     * @param path      路径
     * @param data      对应的值
     * @param callback  回调
     * @throws Exception errors
     */
    public void createNodeAsync(CreateMode crateMode, String path, byte[] data, BackgroundCallback callback) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(crateMode)
                .inBackground(callback)
                .forPath(path, data);
    }

    /**
     * 异步-创建节点
     *
     * @param crateMode 节点创建模式  PERSISTENT,PERSISTENT_SEQUENTIAL,EPHEMERAL,EPHEMERAL_SEQUENTIAL
     * @param path      路径
     * @param data      对应的值
     * @param callback  回调
     * @param executor  回调在哪里执行
     * @throws Exception errors
     */
    public void createNodeAsync(CreateMode crateMode, String path, byte[] data, BackgroundCallback callback, Executors executor) throws Exception {
        client.create()
                .creatingParentContainersIfNeeded() // 自动递归创建父节点
                .withMode(crateMode)
                .inBackground(callback, executor)
                .forPath(path, data);
    }

    /**
     * 同步-删除一个叶子节点(注意哦,只能删除叶子节点否则报错的)
     *
     * @param path 需要删除的节点对应的路径
     * @throws Exception errors
     */
    public void deleteNodeSync(String path) throws Exception {
        client.delete()
                .forPath(path);
    }

    /**
     * 异步-删除一个叶子节点(注意哦,只能删除叶子节点否则报错的)
     *
     * @param path     需要删除的节点对应的路径
     * @param callback 回调
     * @throws Exception errors
     */
    public void deleteNodeAsync(String path, BackgroundCallback callback) throws Exception {
        client.delete()
                .inBackground(callback)
                .forPath(path);
    }

    /**
     * 异步-删除一个叶子节点(注意哦,只能删除叶子节点否则报错的)
     *
     * @param path     需要删除的节点对应的路径
     * @param callback 回调
     * @param executor 回调在哪里执行
     * @throws Exception errors
     */
    public void deleteNodeAsync(String path, BackgroundCallback callback, Executors executor) throws Exception {
        client.delete()
                .inBackground(callback, executor)
                .forPath(path);
    }

    /**
     * 同步-删除一个节点，并且递归删除其所有的子节点
     *
     * @param path 需要删除的节点对应的路基
     * @throws Exception errors
     */
    public void deleteNodeRecursivelySync(String path) throws Exception {
        client.delete()
                .deletingChildrenIfNeeded()
                .forPath(path);

    }

    /**
     * 异步-删除一个节点，并且递归删除其所有的子节点
     *
     * @param path     需要删除的节点对应的路基
     * @param callback 回调
     * @throws Exception errors
     */
    public void deleteNodeRecursivelyAsync(String path, BackgroundCallback callback) throws Exception {
        client.delete()
                .deletingChildrenIfNeeded()
                .inBackground(callback)
                .forPath(path);

    }

    /**
     * 异步-删除一个节点，并且递归删除其所有的子节点
     *
     * @param path     需要删除的节点对应的路基
     * @param callback 回调
     * @param executor 回调在哪里执行
     * @throws Exception errors
     */
    public void deleteNodeRecursivelyAsync(String path, BackgroundCallback callback, Executors executor) throws Exception {
        client.delete()
                .deletingChildrenIfNeeded()
                .inBackground(callback, executor)
                .forPath(path);

    }

    /**
     * 同步-删除一个节点，强制指定版本进行删除
     *
     * @param path    需要删除的节点对应的路基
     * @param version 节点版本
     * @throws Exception errors
     */
    public void deleteNodeWithVersionSync(String path, int version) throws Exception {
        client.delete()
                .withVersion(version)
                .forPath(path);
    }

    /**
     * 异步-删除一个节点，强制指定版本进行删除
     *
     * @param path     需要删除的节点对应的路基
     * @param version  节点版本
     * @param callback 回调
     * @throws Exception errors
     */
    public void deleteNodeWithVersionAsync(String path, int version, BackgroundCallback callback) throws Exception {
        client.delete()
                .withVersion(version)
                .inBackground(callback)
                .forPath(path);
    }

    /**
     * 异步-删除一个节点，强制指定版本进行删除
     *
     * @param path     需要删除的节点对应的路基
     * @param version  节点版本
     * @param callback 回调
     * @param executor 回调在哪里执行
     * @throws Exception errors
     */
    public void deleteNodeWithVersionAsync(String path, int version, BackgroundCallback callback, Executors executor) throws Exception {
        client.delete()
                .withVersion(version)
                .inBackground(callback, executor)
                .forPath(path);
    }

    /**
     * 同步-删除一个节点，强制保证删除
     * guaranteed()接口是一个保障措施，只要客户端会话有效，那么Curator会在后台持续进行删除操作，直到删除节点成功。
     *
     * @param path 需要删除的节点对应的路基
     * @throws Exception errors
     */
    public void deleteNodeGuaranteedSync(String path) throws Exception {
        client.delete()
                .guaranteed()
                .forPath(path);
    }

    /**
     * 异步-删除一个节点，强制保证删除
     * guaranteed()接口是一个保障措施，只要客户端会话有效，那么Curator会在后台持续进行删除操作，直到删除节点成功。
     *
     * @param path     需要删除的节点对应的路基
     * @param callback 回调
     * @throws Exception errors
     */
    public void deleteNodeGuaranteedAsync(String path, BackgroundCallback callback) throws Exception {
        client.delete()
                .guaranteed()
                .inBackground(callback)
                .forPath(path);
    }

    /**
     * 异步-删除一个节点，强制保证删除
     * guaranteed()接口是一个保障措施，只要客户端会话有效，那么Curator会在后台持续进行删除操作，直到删除节点成功。
     *
     * @param path     需要删除的节点对应的路基
     * @param callback 回调
     * @param executor 回调在哪里执行
     * @throws Exception errors
     */
    public void deleteNodeGuaranteedAsync(String path, BackgroundCallback callback, Executors executor) throws Exception {
        client.delete()
                .guaranteed()
                .inBackground(callback, executor)
                .forPath(path);
    }

    /**
     * 同步-读取一个节点的数据内容
     *
     * @param path 节点路基
     * @return 节点内容
     * @throws Exception errors
     */
    public byte[] getNodeDataSync(String path) throws Exception {
        return client.getData()
                .forPath(path);
    }

    /**
     * 异步-读取一个节点的数据内容
     *
     * @param path     节点路基
     * @param callback 回调
     * @return 节点内容
     * @throws Exception errors
     */
    public byte[] getNodeDataAsync(String path, BackgroundCallback callback) throws Exception {
        return client.getData()
                .inBackground(callback)
                .forPath(path);
    }

    /**
     * 异步-读取一个节点的数据内容
     *
     * @param path     节点路基
     * @param callback 回调
     * @param executor 回调在哪里执行
     * @return 节点内容
     * @throws Exception errors
     */
    public byte[] getNodeDataAsync(String path, BackgroundCallback callback, Executors executor) throws Exception {
        return client.getData()
                .inBackground(callback, executor)
                .forPath(path);
    }

    /**
     * 同步-获取到该节点的stat
     *
     * @param path 节点路径
     * @return 节点对应的stat
     * @throws Exception errors
     */
    public Stat getNodeStatSync(String path) throws Exception {
        Stat stat = new Stat();
        client.getData()
                .storingStatIn(stat)
                .forPath(path);
        return stat;
    }

    /**
     * 异步-获取到该节点的stat
     *
     * @param path     节点路径
     * @param callback 回调
     * @throws Exception errors
     */
    public void getNodeStatAsync(String path, BackgroundCallback callback) throws Exception {
        client.getData()
                .inBackground(callback)
                .forPath(path);
    }

    /**
     * 异步-获取到该节点的stat
     *
     * @param path     节点路径
     * @param callback 回调
     * @param executor 回调在哪里执行
     * @throws Exception errors
     */
    public void getNodeStatAsync(String path, BackgroundCallback callback, Executors executor) throws Exception {
        client.getData()
                .inBackground(callback, executor)
                .forPath(path);
    }

    /**
     * 同步-更新一个节点的数据内容
     *
     * @param path 节点路径
     * @param data 节点对应数据
     * @throws Exception errors
     */
    public void updateNodeDataSync(String path, byte[] data) throws Exception {
        client.setData()
                .forPath(path, data);
    }

    /**
     * 异步-更新一个节点的数据内容
     *
     * @param path     节点路径
     * @param data     节点对应数据
     * @param callback 回调
     * @throws Exception errors
     */
    public void updateNodeDataAsync(String path, byte[] data, BackgroundCallback callback) throws Exception {
        client.setData()
                .inBackground(callback)
                .forPath(path, data);
    }

    /**
     * 异步-更新一个节点的数据内容
     *
     * @param path     节点路径
     * @param data     节点对应数据
     * @param callback 回调
     * @param executor 回调在哪里执行
     * @throws Exception errors
     */
    public void updateNodeDataAsync(String path, byte[] data, BackgroundCallback callback, Executors executor) throws Exception {
        client.setData()
                .inBackground(callback, executor)
                .forPath(path, data);
    }

    /**
     * 同步-更新一个节点的数据内容，强制指定版本进行更新
     *
     * @param path    节点路径
     * @param data    节点对应数据
     * @param version 节点版本
     * @throws Exception errors
     */
    public void updateNodeDataSync(String path, byte[] data, int version) throws Exception {
        client.setData()
                .withVersion(version)
                .forPath(path, data);

    }

    /**
     * 异步-更新一个节点的数据内容，强制指定版本进行更新
     *
     * @param path     节点路径
     * @param data     节点对应数据
     * @param version  节点版本
     * @param callback 回调
     * @throws Exception errors
     */
    public void updateNodeDataAsync(String path, byte[] data, int version, BackgroundCallback callback) throws Exception {
        client.setData()
                .withVersion(version)
                .inBackground(callback)
                .forPath(path, data);

    }

    /**
     * 异步-更新一个节点的数据内容，强制指定版本进行更新
     *
     * @param path     节点路径
     * @param data     节点对应数据
     * @param version  节点版本
     * @param callback 回调
     * @param executor 回调在哪里执行
     * @throws Exception errors
     */
    public void updateNodeDataAsync(String path, byte[] data, int version, BackgroundCallback callback, Executors executor) throws Exception {
        client.setData()
                .withVersion(version)
                .inBackground(callback, executor)
                .forPath(path, data);

    }

    /**
     * 同步-检查节点是否存在
     *
     * @param path 节点路径
     * @return 节点是否存在
     * @throws Exception errors
     */
    public boolean isNodeExistSync(String path) throws Exception {
        Stat state = client.checkExists()
                .forPath(path);
        return state != null;
    }

    /**
     * 同步-在一个事务里面同时执行多个操作
     * CuratorOp createOp = zkClient.transactionOp().create().forPath("/a/path", "some data".getBytes());
     *
     * @param operations 操作列表
     * @return Collection<CuratorTransactionResult>
     * @throws Exception errors
     */
    public Collection<CuratorTransactionResult> transactionSync(CuratorOp... operations) throws Exception {
        return client.transaction()
                .forOperations(operations);
    }

    /**
     * 异步-在一个事务里面同时执行多个操作
     * CuratorOp createOp = zkClient.transactionOp().create().forPath("/a/path", "some data".getBytes());
     *
     * @param operations 操作列表
     * @param callback   回调
     * @return Collection<CuratorTransactionResult>
     * @throws Exception errors
     */
    public Collection<CuratorTransactionResult> transactionAsync(BackgroundCallback callback, CuratorOp... operations) throws Exception {
        return client.transaction()
                .inBackground(callback)
                .forOperations(operations);
    }

    /**
     * 异步-在一个事务里面同时执行多个操作
     * CuratorOp createOp = zkClient.transactionOp().create().forPath("/a/path", "some data".getBytes());
     *
     * @param operations 操作列表
     * @param callback   回调
     * @param executor   回调在哪里执行
     * @return Collection<CuratorTransactionResult>
     * @throws Exception errors
     */
    public Collection<CuratorTransactionResult> transactionAsync(BackgroundCallback callback, Executors executor, CuratorOp... operations) throws Exception {
        return client.transaction()
                .inBackground(callback, executor)
                .forOperations(operations);
    }

    /**
     * 同步-在一个事务里面同时执行多个操作
     * CuratorOp createOp = zkClient.transactionOp().create().forPath("/a/path", "some data".getBytes());
     *
     * @param operations 操作列表
     * @return Collection<CuratorTransactionResult>
     * @throws Exception errors
     */
    public Collection<CuratorTransactionResult> transactionSync(List<CuratorOp> operations) throws Exception {
        return client.transaction()
                .forOperations(operations);
    }

    /**
     * 异步-在一个事务里面同时执行多个操作
     * CuratorOp createOp = zkClient.transactionOp().create().forPath("/a/path", "some data".getBytes());
     *
     * @param operations 操作列表
     * @param callback   回调
     * @throws Exception errors
     */
    public void transactionAsync(BackgroundCallback callback, List<CuratorOp> operations) throws Exception {
        client.transaction()
                .inBackground(callback)
                .forOperations(operations);
    }

    /**
     * 异步-在一个事务里面同时执行多个操作
     * CuratorOp createOp = zkClient.transactionOp().create().forPath("/a/path", "some data".getBytes());
     *
     * @param operations 操作列表
     * @param callback   回调
     * @param executor   回调在哪里执行
     * @throws Exception errors
     */
    public void transactionAsync(BackgroundCallback callback, Executors executor, List<CuratorOp> operations) throws Exception {
        client.transaction()
                .inBackground(callback, executor)
                .forOperations(operations);
    }

    // 缓存

    // Path Cache用来监控一个ZNode的子节点. 当一个子节点增加， 更新，删除时， Path Cache会改变它的状态，
    // 会包含最新的子节点， 子节点的数据和状态，而状态的更变将通过PathChildrenCacheListener通知。

//    public void test() {
//        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "path", true);
//        try {
//            pathChildrenCache.getListenable().addListener();
//            pathChildrenCache.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


}
