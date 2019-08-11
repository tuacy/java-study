package com.tuacy.study.distributelock.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
     * 获取某个节点的所有子节点路径
     *
     * @param path 目录
     * @return children
     */
    public List<String> getChildren(String path) {
        try {
            return client.getChildren().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建持久化节点
     *
     * @param path 路径
     * @return 是否创建成功
     */
    public boolean createPersistentNode(String path) {
        try {
            client.create()
                    .creatingParentContainersIfNeeded() // 自动递归创建父节点
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建持久化节点
     *
     * @param path 路径
     * @param data 对应的值
     * @return 是否创建成功
     */
    public boolean createPersistentNode(String path, byte[] data) {
        try {
            client.create()
                    .creatingParentContainersIfNeeded() // 自动递归创建父节点
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path, data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建持久化并且带序列号的节点
     *
     * @param path 路径
     * @return 是否创建成功
     */
    public boolean createPersistentSequentialNode(String path) {
        try {
            client.create()
                    .creatingParentContainersIfNeeded() // 自动递归创建父节点
                    .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                    .forPath(path);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建临时并且带序列号的节点
     *
     * @param path 路径
     * @param data 节点对应数据
     * @return 是否创建成功
     */
    public boolean createPersistentSequentialNode(String path, byte[] data) {
        try {
            client.create()
                    .creatingParentContainersIfNeeded() // 自动递归创建父节点
                    .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                    .forPath(path, data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建临时节点
     *
     * @param path 路径
     * @return 是否创建成功
     */
    public boolean createEphemeralNode(String path) {
        try {
            client.create()
                    .creatingParentContainersIfNeeded() // 自动递归创建父节点
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建临时节点
     *
     * @param path 路径
     * @param data 节点对应数据
     * @return 是否创建成功
     */
    public boolean createEphemeralNode(String path, byte[] data) {
        try {
            client.create()
                    .creatingParentContainersIfNeeded() // 自动递归创建父节点
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path, data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建临时并且带序列号的节点
     *
     * @param path 路径
     * @return 是否创建成功
     */
    public boolean createEphemeralSequentialNode(String path) {
        try {
            client.create()
                    .creatingParentContainersIfNeeded() // 自动递归创建父节点
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(path);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建临时并且带序列号的节点
     *
     * @param path 路径
     * @param data 对应的值
     * @return 是否创建成功
     */
    public boolean createEphemeralSequentialNode(String path, byte[] data) {
        try {
            client.create()
                    .creatingParentContainersIfNeeded() // 自动递归创建父节点
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(path, data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建临时节点
     *
     * @param crateMode 节点创建模式  PERSISTENT,PERSISTENT_SEQUENTIAL,EPHEMERAL,EPHEMERAL_SEQUENTIAL
     * @param path      路径
     * @return 是否创建成功
     */
    public boolean createNode(CreateMode crateMode, String path) {
        try {
            client.create()
                    .creatingParentContainersIfNeeded() // 自动递归创建父节点
                    .withMode(crateMode) // 指定创建模式
                    .forPath(path);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建临时节点
     *
     * @param crateMode 节点创建模式  PERSISTENT,PERSISTENT_SEQUENTIAL,EPHEMERAL,EPHEMERAL_SEQUENTIAL
     * @param path      路径
     * @param data      对应的值
     * @return 是否创建成功
     */
    public boolean createNode(CreateMode crateMode, String path, byte[] data) {
        try {
            client.create()
                    .creatingParentContainersIfNeeded() // 自动递归创建父节点
                    .withMode(crateMode)
                    .forPath(path, data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除一个叶子节点(注意哦,只能删除叶子节点否则报错的)
     *
     * @param path 需要删除的节点对应的路径
     * @return 是否删除成功
     */
    public boolean deleteNode(String path) {
        try {
            client.delete().forPath(path);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除一个节点，并且递归删除其所有的子节点
     *
     * @param path 需要删除的节点对应的路基
     * @return 是否删除成功
     */
    public boolean deleteNodeRecursively(String path) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(path);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除一个节点，强制指定版本进行删除
     *
     * @param path    需要删除的节点对应的路基
     * @param version 节点版本
     * @return 是否删除成功
     */
    public boolean deleteNodeWithVersion(String path, int version) {
        try {
            client.delete().withVersion(version).forPath(path);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除一个节点，强制保证删除
     * guaranteed()接口是一个保障措施，只要客户端会话有效，那么Curator会在后台持续进行删除操作，直到删除节点成功。
     *
     * @param path 需要删除的节点对应的路基
     * @return 是否删除成功
     */
    public boolean deleteNodeGuaranteed(String path) {
        try {
            client.delete().guaranteed().forPath(path);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 读取一个节点的数据内容
     *
     * @param path 节点路基
     * @return 节点内容
     */
    public byte[] getNodeData(String path) {
        try {
            return client.getData().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取到该节点的stat
     *
     * @param path 节点路径
     * @return 节点对应的stat
     */
    public Stat getNodeStat(String path) {
        try {
            Stat stat = new Stat();
            client.getData().storingStatIn(stat).forPath(path);
            return stat;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取到该节点对应的版本
     *
     * @param path 节点路径
     * @return 节点对应的版本
     */
    public Integer getNodeVersion(String path) {
        try {
            Stat stat = new Stat();
            client.getData().storingStatIn(stat).forPath(path);
            return stat.getVersion();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 更新一个节点的数据内容
     *
     * @param path 节点路径
     * @param data 节点对应数据
     * @return 是否更新成功
     */
    public boolean updateNodeData(String path, byte[] data) {
        try {
            client.setData().forPath(path, data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新一个节点的数据内容，强制指定版本进行更新
     *
     * @param path    节点路径
     * @param data    节点对应数据
     * @param version 节点版本
     * @return 是否更新成功
     */
    public boolean updateNodeData(String path, byte[] data, int version) {
        try {
            client.setData().withVersion(version).forPath(path, data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 检查节点是否存在
     *
     * @param path 节点路径
     * @return 节点是否存在
     */
    public boolean isNodeExist(String path) {
        try {
            Stat state = client.checkExists().forPath(path);
            return state != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // 事务


    // 异步接口

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
