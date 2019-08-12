package com.tuacy.study.distributelock.distributedlock.zoo;

import com.tuacy.study.distributelock.config.ZkClient;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

/**
 * @name: ZookeeperTransitionTest
 * @author: tuacy.
 * @date: 2019/8/12.
 * @version: 1.0
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ZookeeperTransitionTest {

    private ZkClient zkClient;

    @Autowired
    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    @Test
    public void transaction() throws Exception {
        CuratorOp createOp = zkClient.getClient().transactionOp().create().forPath("/a/path", "some data".getBytes());
        CuratorOp setDataOp = zkClient.getClient().transactionOp().setData().forPath("/another/path", "other data".getBytes());
        CuratorOp deleteOp = zkClient.getClient().transactionOp().delete().forPath("/yet/another/path");

        Collection<CuratorTransactionResult> results = zkClient.getClient().transaction().forOperations(createOp, setDataOp, deleteOp);
        for (CuratorTransactionResult result : results) {
            System.out.println(result.getForPath() + " - " + result.getType());
        }
    }


}
