package com.tuacy.tableshard.dao;

import com.tuacy.tableshard.entity.model.AccHour;
import com.tuacy.tableshard.utils.DbDataTimeUtils;
import com.tuacy.tableshard.utils.LocalDateTimeUtil;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class TableSharedTest {

    private IAccHourDao accHourDao;

    @Autowired
    public void setAccHourDao(IAccHourDao accHourDao) {
        this.accHourDao = accHourDao;
    }

    /**
     * 测试 acchour表插入单条记录
     */
    @Test
    public void testAccHourInsertItem() {

        AccHour item = new AccHour();
        item.setRecTime(DbDataTimeUtils.dateTime2Long(LocalDateTime.now()));
        item.setPtId(1001L);
        item.setValue(new Random(LocalDateTimeUtil.getMilliByTime(LocalDateTime.now())).nextDouble());

        int insertItem = accHourDao.insertItem(item);
        System.out.println("称该插入条数:" + insertItem);
    }

    /**
     * 测试 acchour表多条记录的插入,我们会估计设计成多条记录不再同一个表中
     */
    @Test
    public void testAccHourInsertList() {
        LocalDateTime startDateTime = LocalDateTime.of(2019, 8, 31, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2019, 8, 31, 5, 0);
        List<AccHour> dbItemList = Lists.newArrayList();
        while (startDateTime.isBefore(endDateTime)) {
            AccHour dbItem = new AccHour();
            dbItem.setRecTime(DbDataTimeUtils.dateTime2Long(startDateTime));
            dbItem.setPtId(1001L);
            dbItem.setValue(new Random(LocalDateTimeUtil.getMilliByTime(LocalDateTime.now())).nextDouble());
            dbItemList.add(dbItem);
            // 10分钟一条记录
            startDateTime = startDateTime.plusMinutes(10);
        }
        int insertItem = accHourDao.insertList(dbItemList);
        System.out.println("称该插入条数:" + insertItem);

    }

    @Test
    public void testSelect() {
        LocalDateTime startDateTime = LocalDateTime.of(2019, 8, 31, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2019, 8, 31, 5, 0);

        List<AccHour> retList = accHourDao.selectList(DbDataTimeUtils.dateTime2Long(startDateTime), DbDataTimeUtils.dateTime2Long(endDateTime));
        if (retList != null) {
            System.out.println(retList.size());
        }

    }
}
