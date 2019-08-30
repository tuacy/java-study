package com.tuacy.tableshard;

import com.tuacy.tableshard.dao.IAccHourDao;
import com.tuacy.tableshard.entity.model.AccHour;
import com.tuacy.tableshard.mapper.AccHourMapper;
import com.tuacy.tableshard.tableextend.multidatasource.DataSourceAnnotation;
import com.tuacy.tableshard.tableextend.multidatasource.EDataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AccHourDao implements IAccHourDao {

    private AccHourMapper accHourMapper;

    @Autowired
    public void setAccHourMapper(AccHourMapper accHourMapper) {
        this.accHourMapper = accHourMapper;
    }

    /**
     * DataSourceAnnotation 用于指定数据源,放到统计数据库里面
     */
    @Override
    @DataSourceAnnotation(sourceType = EDataSourceType.STATIS)
    @Transactional(rollbackFor = Exception.class)
    public int insertItem(AccHour item) {
        return accHourMapper.insertItem(item);
    }

    @Override
    @DataSourceAnnotation(sourceType = EDataSourceType.STATIS)
    @Transactional(rollbackFor = Exception.class)
    public AccHour selectItem(Long time, Long pkId) {
        return null;
    }
}
