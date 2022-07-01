package com.scheduleservice.googlesheets.repository.service;

import com.scheduleservice.googlesheets.repository.entity.CalendarDeployedManagementEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.time.LocalDateTime;

/**
 * <p>
 * カレンダー展開管理 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
public interface ICalendarDeployedManagementService extends IService<CalendarDeployedManagementEntity> {

    /**
     * カレンダー展開管理更新
     *
     * @param calendarDeployedManagement カレンダー展開管理Entity
     * @param finalChangeDate 最終更新日時
     * @return boolean
     */
    boolean updateCalendarDeployed(CalendarDeployedManagementEntity calendarDeployedManagementEntity, LocalDateTime finalChangeDate);
}
