package com.scheduleservice.googlesheets.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scheduleservice.googlesheets.repository.entity.ReleaseInfoEntity;
import com.scheduleservice.googlesheets.repository.mapper.ReleaseInfoMapper;
import com.scheduleservice.googlesheets.repository.service.IReleaseInfoService;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

/**
 * <p>
 * リリース情報 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-11-30
 */
@Service
public class ReleaseInfoServiceImpl extends ServiceImpl<ReleaseInfoMapper, ReleaseInfoEntity> implements IReleaseInfoService {

    @Override
    public boolean saveReleaseInfo(ReleaseInfoEntity releaseInfoEntity) {
        return save(releaseInfoEntity);
    }

    @Override
    public boolean updateReleaseInfo(ReleaseInfoEntity releaseInfoEntity, LocalDateTime finalChangeDate) {
        LambdaUpdateWrapper<ReleaseInfoEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(ReleaseInfoEntity::getReleaseCategory, releaseInfoEntity.getReleaseCategory());
        updateWrapper.set(ReleaseInfoEntity::getNextConfirmDate, releaseInfoEntity.getNextConfirmDate());
        updateWrapper.set(ReleaseInfoEntity::getReleaseUrl, releaseInfoEntity.getReleaseUrl());
        updateWrapper.set(ReleaseInfoEntity::getTempCarName, releaseInfoEntity.getTempCarName());
        updateWrapper.set(ReleaseInfoEntity::getTempCarGroupName, releaseInfoEntity.getTempCarGroupName());
        updateWrapper.set(ReleaseInfoEntity::getSalesCategoryL, releaseInfoEntity.getSalesCategoryL());
        updateWrapper.set(ReleaseInfoEntity::getSalesCategoryR, releaseInfoEntity.getSalesCategoryR());
        updateWrapper.set(ReleaseInfoEntity::getSalesCategoryNote, releaseInfoEntity.getSalesCategoryNote());
        updateWrapper.set(ReleaseInfoEntity::getSupportPeriod, releaseInfoEntity.getSupportPeriod());
        updateWrapper.set(ReleaseInfoEntity::getPicId, releaseInfoEntity.getPicId());
        updateWrapper.set(ReleaseInfoEntity::getNote, releaseInfoEntity.getNote());
        updateWrapper.eq(ReleaseInfoEntity::getReleaseInfoId, releaseInfoEntity.getReleaseInfoId())
            .eq(ReleaseInfoEntity::getUpdateDate, finalChangeDate).last("limit 1");
        return update(releaseInfoEntity, updateWrapper);
    }
}
