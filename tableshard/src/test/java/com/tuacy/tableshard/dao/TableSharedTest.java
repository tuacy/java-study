package com.tuacy.tableshard.dao;

import com.tuacy.tableshard.entity.model.AccHour;
import com.tuacy.tableshard.utils.DbDataTimeUtils;
import com.tuacy.tableshard.utils.LocalDateTimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class TableSharedTest {

    private IAccHourDao accHourDao;
    private IAccDayDao accDayDao;
    private IAccMonthDao accMonthDao;

    @Autowired
    public void setAccHourDao(IAccHourDao accHourDao) {
        this.accHourDao = accHourDao;
    }

    @Autowired
    public void setAccDayDao(IAccDayDao accDayDao) {
        this.accDayDao = accDayDao;
    }

    @Autowired
    public void setAccMonthDao(IAccMonthDao accMonthDao) {
        this.accMonthDao = accMonthDao;
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
}
