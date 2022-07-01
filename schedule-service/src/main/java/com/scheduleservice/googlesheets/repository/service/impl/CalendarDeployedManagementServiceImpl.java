package com.scheduleservice.googlesheets.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.scheduleservice.googlesheets.repository.entity.CalendarDeployedManagementEntity;
import com.scheduleservice.googlesheets.repository.mapper.CalendarDeployedManagementMapper;
import com.scheduleservice.googlesheets.repository.service.ICalendarDeployedManagementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

/**
 * <p>
 * カレンダー展開管理 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
@Service
public class CalendarDeployedManagementServiceImpl extends ServiceImpl<CalendarDeployedManagementMapper, CalendarDeployedManagementEntity> implements ICalendarDeployedManagementService {

    @Override
    public boolean updateCalendarDeployed(CalendarDeployedManagementEntity calendarDeployedManagementEntity, LocalDateTime finalChangeDate) {
        LambdaUpdateWrapper<CalendarDeployedManagementEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(CalendarDeployedManagementEntity::getTeamId, calendarDeployedManagementEntity.getTeamId()).eq(CalendarDeployedManagementEntity::getFinalChangerDate, finalChangeDate).last("limit 1");
        return update(calendarDeployedManagementEntity, updateWrapper);
    }
}
