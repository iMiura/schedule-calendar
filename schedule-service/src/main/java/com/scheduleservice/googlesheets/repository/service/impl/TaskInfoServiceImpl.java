package com.scheduleservice.googlesheets.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scheduleservice.googlesheets.repository.entity.TaskInfoEntity;
import com.scheduleservice.googlesheets.repository.mapper.TaskInfoMapper;
import com.scheduleservice.googlesheets.repository.service.ITaskInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * タスク情報 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
@Service
public class TaskInfoServiceImpl extends ServiceImpl<TaskInfoMapper, TaskInfoEntity> implements ITaskInfoService {

    @Override
    public TaskInfoEntity getTaskInfo(Long teamId, Long taskId) {
        LambdaQueryWrapper<TaskInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TaskInfoEntity::getTeamId, teamId).eq(TaskInfoEntity::getTaskId, taskId).last("limit 1");
        return getOne(queryWrapper);
      }
}
