package com.scheduleservice.googlesheets.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scheduleservice.googlesheets.repository.entity.RowInfoEntity;
import com.scheduleservice.googlesheets.repository.mapper.RowInfoMapper;
import com.scheduleservice.googlesheets.repository.service.IRowInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * M_行数管理 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-12-01
 */
@Service
public class RowInfoServiceImpl extends ServiceImpl<RowInfoMapper, RowInfoEntity> implements IRowInfoService {

    @Override
    public boolean updateRowInfo(RowInfoEntity rowInfoEntity) {
        LambdaUpdateWrapper<RowInfoEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RowInfoEntity::getGoogleSheetsDiv, rowInfoEntity.getGoogleSheetsDiv()).last("limit 1");
        return update(rowInfoEntity, updateWrapper);
    }
}
