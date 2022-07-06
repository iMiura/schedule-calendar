package com.scheduleservice.googlesheets.repository.service;

import com.scheduleservice.googlesheets.repository.entity.WorkYmInfoEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 年月度情報 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-07-04
 */
public interface IWorkYmInfoService extends IService<WorkYmInfoEntity> {

    /**
     * 年月度情報情報取得
     *
     * @param calendarYmd システム日付の年月日
     * @return WorkTimeManagementEntity
     */
    WorkYmInfoEntity findWorkYmInfo(String calendarYmd);
}
