package com.scheduleservice.googlesheets.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scheduleservice.googlesheets.repository.entity.WorkInfoEntity;
import com.scheduleservice.googlesheets.repository.mapper.WorkInfoMapper;
import com.scheduleservice.googlesheets.repository.service.IWorkInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 業務情報 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
@Service
public class WorkInfoServiceImpl extends ServiceImpl<WorkInfoMapper, WorkInfoEntity> implements IWorkInfoService {

    @Override
    public WorkInfoEntity getWorkInfo(Long teamId, Long workId) {
        LambdaQueryWrapper<WorkInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WorkInfoEntity::getTeamId, teamId).eq(WorkInfoEntity::getWorkId, workId).last("limit 1");
        return getOne(queryWrapper);
    }
}
