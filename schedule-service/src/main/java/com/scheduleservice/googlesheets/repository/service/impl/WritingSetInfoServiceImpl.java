package com.scheduleservice.googlesheets.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scheduleservice.googlesheets.repository.entity.WritingSetInfoEntity;
import com.scheduleservice.googlesheets.repository.mapper.WritingSetInfoMapper;
import com.scheduleservice.googlesheets.repository.service.IWritingSetInfoService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 書込定義情報 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-12-08
 */
@Service
public class WritingSetInfoServiceImpl extends ServiceImpl<WritingSetInfoMapper, WritingSetInfoEntity> implements IWritingSetInfoService {

    @Override
    public List<WritingSetInfoEntity> getWritingSetInfo(String googleSheetsDiv, String processDiv) {
        LambdaQueryWrapper<WritingSetInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WritingSetInfoEntity::getGoogleSheetsDiv, googleSheetsDiv)
            .eq(WritingSetInfoEntity::getProcessDiv, processDiv)
            .orderByAsc(WritingSetInfoEntity::getSeqNum);

        return list(queryWrapper);
    }
}
