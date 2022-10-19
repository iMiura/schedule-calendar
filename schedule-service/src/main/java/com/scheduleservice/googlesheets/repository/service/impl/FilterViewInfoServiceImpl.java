package com.scheduleservice.googlesheets.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scheduleservice.googlesheets.repository.entity.FilterViewInfoEntity;
import com.scheduleservice.googlesheets.repository.mapper.FilterViewInfoMapper;
import com.scheduleservice.googlesheets.repository.service.IFilterViewInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * フィルタ情報 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-10-17
 */
@Service
public class FilterViewInfoServiceImpl extends ServiceImpl<FilterViewInfoMapper, FilterViewInfoEntity> implements
    IFilterViewInfoService {

    @Override
    public FilterViewInfoEntity getFilterViewInfo(Long teamId, Long userId, String calendarYm) {
        LambdaQueryWrapper<FilterViewInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FilterViewInfoEntity::getTeamId, teamId)
            .eq(FilterViewInfoEntity::getUserId, userId)
            .eq(FilterViewInfoEntity::getCalendarYm, calendarYm)
            .last("limit 1");
        return getOne(queryWrapper);
      }
}
