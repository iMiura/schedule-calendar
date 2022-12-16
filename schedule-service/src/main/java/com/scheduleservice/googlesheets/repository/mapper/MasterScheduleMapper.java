package com.scheduleservice.googlesheets.repository.mapper;

import com.scheduleservice.googlesheets.repository.entity.MasterScheduleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.time.LocalDate;
import java.util.List;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * マスタスケジュール Mapper
 * </p>
 *
 * @author keisho
 * @since 2022-12-01
 */
public interface MasterScheduleMapper extends BaseMapper<MasterScheduleEntity> {

    /**
     * 対応時期リスト取得
     * @return List<String>
     */
    @Select({
        "select @lastDay := last_day(date_add(@lastDay,interval 1 month)) supportPeriod ",
        "  from (select @lastDay := date_add((select case when unix_timestamp(m.master_deadline) - unix_timestamp(CURRENT_DATE()) >= 0 THEN m.master_deadline ELSE date_add(CURRENT_DATE(), interval 1 month) END ",
        "                                       from master_schedule m ",
        "                                      where m.support_period = date_format(CURRENT_DATE(), '%Y年%m月')), interval -1 month) ",
        "         from master_schedule limit 12) tmp;"
    })
    List<LocalDate> selectScheduleList();

}
