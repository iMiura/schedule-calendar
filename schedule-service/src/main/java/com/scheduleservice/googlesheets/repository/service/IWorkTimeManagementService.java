package com.scheduleservice.googlesheets.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scheduleservice.googlesheets.repository.entity.WorkTimeManagementEntity;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 作業時間管理 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
public interface IWorkTimeManagementService extends IService<WorkTimeManagementEntity> {

    /**
     * 作業時間管理情報取得
     *
     * @param teamId チームID
     * @param ym カレンダー年月度
     * @param taskId タスクID
     * @return WorkTimeManagementEntity
     */
    List<WorkTimeManagementEntity> getWorkTime(Long teamId, String ym, Long taskId);

    /**
     * 最新作業時間管理情報取得
     *
     * @param teamId チームID
     * @param ym カレンダー年月度
     * @param taskId タスクID
     * @return WorkTimeManagementEntity
     */
    WorkTimeManagementEntity findWorkTimeNew(Long teamId, String ym, Long taskId);

    /**
     * 作業時間管理 開始
     *
     * @param workTimeManagementEntity 作業時間管理Entity
     * @return boolean
     */
    boolean updateStart(WorkTimeManagementEntity workTimeManagementEntity);

    /**
     * 作業時間管理更新
     *
     * @param workTimeManagementEntity 作業時間管理Entity
     * @param finalChangeDate 最終更新日時
     * @return boolean
     */
    boolean updateTimeInfo(WorkTimeManagementEntity workTimeManagementEntity, LocalDateTime finalChangeDate);
}
