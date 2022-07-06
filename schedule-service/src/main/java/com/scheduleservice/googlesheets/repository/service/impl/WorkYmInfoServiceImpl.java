package com.scheduleservice.googlesheets.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.scheduleservice.googlesheets.repository.entity.WorkYmInfoEntity;
import com.scheduleservice.googlesheets.repository.mapper.WorkYmInfoMapper;
import com.scheduleservice.googlesheets.repository.service.IWorkYmInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 年月度情報 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-07-04
 */
@Service
public class WorkYmInfoServiceImpl extends ServiceImpl<WorkYmInfoMapper, WorkYmInfoEntity> implements IWorkYmInfoService {

    @Override
    public WorkYmInfoEntity findWorkYmInfo(String calendarYmd) {
        LambdaQueryWrapper<WorkYmInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.le(WorkYmInfoEntity::getCalendarYmStart, calendarYmd)
            .ge(WorkYmInfoEntity::getCalendarYmEnd, calendarYmd)
            .last("limit 1");
        return getOne(queryWrapper);
    }
}
