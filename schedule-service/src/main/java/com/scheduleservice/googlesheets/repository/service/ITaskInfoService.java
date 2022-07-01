package com.scheduleservice.googlesheets.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scheduleservice.googlesheets.repository.entity.TaskInfoEntity;

/**
 * <p>
 * タスク情報 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
public interface ITaskInfoService extends IService<TaskInfoEntity> {

    /**
     * タスク情報取得
     *
     * @param teamId チームID
     * @param taskId タスクID
     * @return TaskInfoEntity
     */
    TaskInfoEntity getTaskInfo(Long teamId, Long taskId);
}
