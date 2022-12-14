package com.scheduleservice.googlesheets.repository.service;

import com.scheduleservice.googlesheets.repository.entity.MasterScheduleEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * マスタスケジュール 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-12-01
 */
public interface IMasterScheduleService extends IService<MasterScheduleEntity> {

    /**
     * 対応時期リスト取得
     * @return List<LocalDate>
     */
    List<LocalDate> selectScheduleList();
}
