package com.scheduleservice.googlesheets.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scheduleservice.googlesheets.repository.entity.GoogleSheetsInfoMstEntity;
import com.scheduleservice.googlesheets.repository.mapper.GoogleSheetsInfoMstMapper;
import com.scheduleservice.googlesheets.repository.service.IGoogleSheetsInfoMstService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * スプレッドシート情報管理マスタ 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-11-30
 */
@Service
public class GoogleSheetsInfoMstServiceImpl extends ServiceImpl<GoogleSheetsInfoMstMapper, GoogleSheetsInfoMstEntity> implements IGoogleSheetsInfoMstService {

    @Override
    public GoogleSheetsInfoMstEntity getGoogleSheetsInfo(String googleSheetsDiv, String googleSheetsApiDiv) {
        LambdaQueryWrapper<GoogleSheetsInfoMstEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoogleSheetsInfoMstEntity::getGoogleSheetsDiv, googleSheetsDiv)
            .eq(GoogleSheetsInfoMstEntity::getGoogleSheetsApiDiv, googleSheetsApiDiv);

        return getOne(queryWrapper);
    }
}
