/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.curator.framework;

import com.google.common.collect.ImmutableList;
import org.apache.curator.RetryPolicy;
import org.apache.curator.connection.ConnectionHandlingPolicy;
import org.apache.curator.connection.StandardConnectionHandlingPolicy;
import org.apache.curator.ensemble.EnsembleProvider;
import org.apache.curator.ensemble.fixed.FixedEnsembleProvider;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.api.CompressionProvider;
import org.apache.curator.framework.api.CreateBuilder;
import org.apache.curator.framework.api.PathAndBytesable;
import org.apache.curator.framework.imps.CuratorFrameworkImpl;
import org.apache.curator.framework.imps.CuratorTempFrameworkImpl;
import org.apache.curator.framework.imps.DefaultACLProvider;
import org.apache.curator.framework.imps.GzipCompressionProvider;
import org.apache.curator.framework.schema.SchemaSet;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateErrorPolicy;
import org.apache.curator.framework.state.StandardConnectionStateErrorPolicy;
import org.apache.curator.utils.DefaultZookeeperFactory;
import org.apache.curator.utils.ZookeeperFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static org.apache.curator.utils.Compatibility.isZK34;

/**
 * Factory methods for creating framework-style clients
 */
public class CuratorFrameworkFactory {

    /**
     * 用于通过建造者模式创建zookeeper客户端
     */
    public static Builder builder();

    /**
     * 创建zookeeper客户端
     */
    public static CuratorFramework newClient(String connectString, RetryPolicy retryPolicy);

    /**
     * 创建zookeeper客户端
     */
    public static CuratorFramework newClient(String connectString, int sessionTimeoutMs, int connectionTimeoutMs, RetryPolicy retryPolicy);

    /**
     * 将本地地址作为可用作节点有效负载的字节返回
     */
    public static byte[] getLocalAddress();

    public static class Builder {

        /**
         * build CuratorFramework对象 -- zookeeper客户端
         */
        public CuratorFramework build();

        /**
         * 创建一个临时的CuratorFramework客户端,CuratorFramework,默认3分钟不活动客户端连接就被关闭
         */
        public CuratorTempFramework buildTemp();

        /**
         * 创建一个临时的CuratorFramework客户端,CuratorFramework,可以自己指定多长时间不活动客户端连接就被关闭
         */
        public CuratorTempFramework buildTemp(long inactiveThreshold, TimeUnit unit);

        /**
         * 添加zookeeper 访问权限
         */
        public Builder authorization(String scheme, byte[] auth);
        public Builder authorization(List<AuthInfo> authInfos);

        /**
         * 设置zookeeper服务器列表
         */
        public Builder connectString(String connectString);

        /**
         * zookeeper服务器地址通过EnsembleProvider(配置提供者)来提供,不能和connectString共同使用
         */
        public Builder ensembleProvider(EnsembleProvider ensembleProvider);

        /**
         * 为每次新建的节点设置一个默认值
         */
        public Builder defaultData(byte[] defaultData);

        /**
         * 设置命名空间,为了实现不同的Zookeeper业务之间的隔离，有的时候需要为每个业务分配一个独立的命名空间
         */
        public Builder namespace(String namespace)

        /**
         * 会话超时时间，单位毫秒，默认60000ms
         */
        public Builder sessionTimeoutMs(int sessionTimeoutMs);

        /**
         * 连接创建超时时间，单位毫秒，默认60000ms
         */
        public Builder connectionTimeoutMs(int connectionTimeoutMs);

        /**
         * @param maxCloseWaitMs time to wait during close to join background threads
         * @return this
         */
        public Builder maxCloseWaitMs(int maxCloseWaitMs);

        /**
         * 设置客户端重连策略
         */
        public Builder retryPolicy(RetryPolicy retryPolicy);

        /**
         * @param threadFactory thread factory used to create Executor Services
         * @return this
         */
        public Builder threadFactory(ThreadFactory threadFactory)
        {
            this.threadFactory = threadFactory;
            return this;
        }

        /**
         * @param compressionProvider the compression provider
         * @return this
         */
        public Builder compressionProvider(CompressionProvider compressionProvider)
        {
            this.compressionProvider = compressionProvider;
            return this;
        }

        /**
         * @param zookeeperFactory the zookeeper factory to use
         * @return this
         */
        public Builder zookeeperFactory(ZookeeperFactory zookeeperFactory)
        {
            this.zookeeperFactory = zookeeperFactory;
            return this;
        }

        /**
         * @param aclProvider a provider for ACLs
         * @return this
         */
        public Builder aclProvider(ACLProvider aclProvider)
        {
            this.aclProvider = aclProvider;
            return this;
        }

        /**
         * @param canBeReadOnly if true, allow ZooKeeper client to enter
         *                      read only mode in case of a network partition. See
         *                      {@link ZooKeeper#ZooKeeper(String, int, Watcher, long, byte[], boolean)}
         *                      for details
         * @return this
         */
        public Builder canBeReadOnly(boolean canBeReadOnly)
        {
            this.canBeReadOnly = canBeReadOnly;
            return this;
        }

        /**
         * By default, Curator uses {@link CreateBuilder#creatingParentContainersIfNeeded()}
         * if the ZK JAR supports {@link CreateMode#CONTAINER}. Call this method to turn off this behavior.
         *
         * @return this
         */
        public Builder dontUseContainerParents()
        {
            this.useContainerParentsIfAvailable = false;
            return this;
        }

        /**
         * Set the error policy to use. The default is {@link StandardConnectionStateErrorPolicy}
         *
         * @since 3.0.0
         * @param connectionStateErrorPolicy new error policy
         * @return this
         */
        public Builder connectionStateErrorPolicy(ConnectionStateErrorPolicy connectionStateErrorPolicy)
        {
            this.connectionStateErrorPolicy = connectionStateErrorPolicy;
            return this;
        }

        /**
         * If mode is true, create a ZooKeeper 3.4.x compatible client. IMPORTANT: If the client
         * library used is ZooKeeper 3.4.x <code>zk34CompatibilityMode</code> is enabled by default.
         *
         * @since 3.5.0
         * @param mode true/false
         * @return this
         */
        public Builder zk34CompatibilityMode(boolean mode)
        {
            this.zk34CompatibilityMode = mode;
            return this;
        }

        /**
         * <p>
         *     Change the connection handling policy. The default policy is {@link StandardConnectionHandlingPolicy}.
         * </p>
         * <p>
         *     <strong>IMPORTANT: </strong> StandardConnectionHandlingPolicy has different behavior than the connection
         *     policy handling prior to version 3.0.0.
         * </p>
         * <p>
         *     Major differences from the older behavior are:
         * </p>
         * <ul>
         *     <li>
         *         Session/connection timeouts are no longer managed by the low-level client. They are managed
         *         by the CuratorFramework instance. There should be no noticeable differences.
         *     </li>
         *     <li>
         *         Prior to 3.0.0, each iteration of the retry policy would allow the connection timeout to elapse
         *         if the connection hadn't yet succeeded. This meant that the true connection timeout was the configured
         *         value times the maximum retries in the retry policy. This longstanding issue has been address.
         *         Now, the connection timeout can elapse only once for a single API call.
         *     </li>
         *     <li>
         *         <strong>MOST IMPORTANTLY!</strong> Prior to 3.0.0, {@link ConnectionState#LOST} did not imply
         *         a lost session (much to the confusion of users). Now,
         *         Curator will set the LOST state only when it believes that the ZooKeeper session
         *         has expired. ZooKeeper connections have a session. When the session expires, clients must take appropriate
         *         action. In Curator, this is complicated by the fact that Curator internally manages the ZooKeeper
         *         connection. Now, Curator will set the LOST state when any of the following occurs:
         *         a) ZooKeeper returns a {@link Watcher.Event.KeeperState#Expired} or {@link KeeperException.Code#SESSIONEXPIRED};
         *         b) Curator closes the internally managed ZooKeeper instance; c) The session timeout
         *         elapses during a network partition.
         *     </li>
         * </ul>
         *
         * @param connectionHandlingPolicy the policy
         * @return this
         * @since 3.0.0
         */
        public Builder connectionHandlingPolicy(ConnectionHandlingPolicy connectionHandlingPolicy)
        {
            this.connectionHandlingPolicy = connectionHandlingPolicy;
            return this;
        }

        /**
         * Add an enforced schema set
         *
         * @param schemaSet the schema set
         * @return this
         * @since 3.2.0
         */
        public Builder schemaSet(SchemaSet schemaSet)
        {
            this.schemaSet = schemaSet;
            return this;
        }

        public ACLProvider getAclProvider()
        {
            return aclProvider;
        }

        public ZookeeperFactory getZookeeperFactory()
        {
            return zookeeperFactory;
        }

        public CompressionProvider getCompressionProvider()
        {
            return compressionProvider;
        }

        public ThreadFactory getThreadFactory()
        {
            return threadFactory;
        }

        public EnsembleProvider getEnsembleProvider()
        {
            return ensembleProvider;
        }

        public int getSessionTimeoutMs()
        {
            return sessionTimeoutMs;
        }

        public int getConnectionTimeoutMs()
        {
            return connectionTimeoutMs;
        }

        public int getMaxCloseWaitMs()
        {
            return maxCloseWaitMs;
        }

        public RetryPolicy getRetryPolicy()
        {
            return retryPolicy;
        }

        public String getNamespace()
        {
            return namespace;
        }

        public boolean useContainerParentsIfAvailable()
        {
            return useContainerParentsIfAvailable;
        }

        public ConnectionStateErrorPolicy getConnectionStateErrorPolicy()
        {
            return connectionStateErrorPolicy;
        }

        public ConnectionHandlingPolicy getConnectionHandlingPolicy()
        {
            return connectionHandlingPolicy;
        }

        public SchemaSet getSchemaSet()
        {
            return schemaSet;
        }

        public boolean isZk34CompatibilityMode()
        {
            return zk34CompatibilityMode;
        }

        @Deprecated
        public String getAuthScheme()
        {
            int qty = (authInfos != null) ? authInfos.size() : 0;
            switch ( qty )
            {
                case 0:
                {
                    return null;
                }

                case 1:
                {
                    return authInfos.get(0).scheme;
                }

                default:
                {
                    throw new IllegalStateException("More than 1 auth has been added");
                }
            }
        }

        @Deprecated
        public byte[] getAuthValue()
        {
            int qty = (authInfos != null) ? authInfos.size() : 0;
            switch ( qty )
            {
                case 0:
                {
                    return null;
                }

                case 1:
                {
                    byte[] bytes = authInfos.get(0).getAuth();
                    return (bytes != null) ? Arrays.copyOf(bytes, bytes.length) : null;
                }

                default:
                {
                    throw new IllegalStateException("More than 1 auth has been added");
                }
            }
        }

        public List<AuthInfo> getAuthInfos()
        {
            return authInfos;
        }

        public byte[] getDefaultData()
        {
            return defaultData;
        }

        public boolean canBeReadOnly()
        {
            return canBeReadOnly;
        }

        private Builder()
        {
        }
    }

    private CuratorFrameworkFactory()
    {
    }
}
