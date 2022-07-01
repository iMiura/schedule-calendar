package com.scheduleservice.googlesheets.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scheduleservice.googlesheets.repository.entity.WorkTimeManagementEntity;
import com.scheduleservice.googlesheets.repository.mapper.WorkTimeManagementMapper;
import com.scheduleservice.googlesheets.repository.service.IWorkTimeManagementService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 作業時間管理 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
@Service
public class WorkTimeManagementServiceImpl extends ServiceImpl<WorkTimeManagementMapper, WorkTimeManagementEntity> implements IWorkTimeManagementService {

    @Override
    public List<WorkTimeManagementEntity> getWorkTime(Long teamId, String ym, Long taskId) {
        LambdaQueryWrapper<WorkTimeManagementEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WorkTimeManagementEntity::getTeamId, teamId)
            .eq(WorkTimeManagementEntity::getCalendarYm, ym)
            .eq(WorkTimeManagementEntity::getTaskId, taskId);
        queryWrapper.orderByAsc(WorkTimeManagementEntity::getTimeRecordStart);

        return list(queryWrapper);
    }

    @Override
    public WorkTimeManagementEntity findWorkTimeNew(Long teamId, String ym, Long taskId) {
        LambdaQueryWrapper<WorkTimeManagementEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WorkTimeManagementEntity::getTeamId, teamId)
            .eq(WorkTimeManagementEntity::getCalendarYm, ym)
            .eq(WorkTimeManagementEntity::getTaskId, taskId)
            .orderByDesc(WorkTimeManagementEntity::getTimeRecordStart)
            .last("limit 1");
        return getOne(queryWrapper);
    }

    @Override
    public boolean updateStart(WorkTimeManagementEntity workTimeManagementEntity) {
        return save(workTimeManagementEntity);
    }

    @Override
    public boolean updateTimeInfo(WorkTimeManagementEntity workTimeManagementEntity, LocalDateTime finalChangeDate) {
        LambdaUpdateWrapper<WorkTimeManagementEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WorkTimeManagementEntity::getTimeRecordId, workTimeManagementEntity.getTimeRecordId())
            .eq(WorkTimeManagementEntity::getFinalChangerDate, finalChangeDate).last("limit 1");
        return update(workTimeManagementEntity, updateWrapper);
    }
}
