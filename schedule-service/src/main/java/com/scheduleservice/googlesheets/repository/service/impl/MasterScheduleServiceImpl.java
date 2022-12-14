package com.scheduleservice.googlesheets.repository.service.impl;

import com.scheduleservice.googlesheets.repository.entity.MasterScheduleEntity;
import com.scheduleservice.googlesheets.repository.mapper.MasterScheduleMapper;
import com.scheduleservice.googlesheets.repository.service.IMasterScheduleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * マスタスケジュール 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-12-01
 */
@Service
public class MasterScheduleServiceImpl extends ServiceImpl<MasterScheduleMapper, MasterScheduleEntity> implements IMasterScheduleService {

    @Autowired
    private MasterScheduleMapper masterScheduleMapper;

    /**
     * 対応時期リスト取得
     * @return List<String>
     */
    @Override
    public List<LocalDate> selectScheduleList() {
        return masterScheduleMapper.selectScheduleList();
    }

}
