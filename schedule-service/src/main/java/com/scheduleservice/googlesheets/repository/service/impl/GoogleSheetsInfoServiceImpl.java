package com.scheduleservice.googlesheets.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scheduleservice.googlesheets.repository.entity.GoogleSheetsInfoEntity;
import com.scheduleservice.googlesheets.repository.mapper.GoogleSheetsInfoMapper;
import com.scheduleservice.googlesheets.repository.service.IGoogleSheetsInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * スプレッドシート情報 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
@Service
public class GoogleSheetsInfoServiceImpl extends ServiceImpl<GoogleSheetsInfoMapper, GoogleSheetsInfoEntity> implements IGoogleSheetsInfoService {

    @Override
    public GoogleSheetsInfoEntity getGoogleSheetsInfo(Long teamId, String calendarYm) {
        LambdaQueryWrapper<GoogleSheetsInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoogleSheetsInfoEntity::getTeamId, teamId).eq(GoogleSheetsInfoEntity::getCalendarYm, calendarYm).last("limit 1");
        return getOne(queryWrapper);
    }
}
