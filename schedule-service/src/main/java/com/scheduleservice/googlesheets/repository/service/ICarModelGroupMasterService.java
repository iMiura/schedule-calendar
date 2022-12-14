package com.scheduleservice.googlesheets.repository.service;

import com.scheduleservice.googlesheets.repository.entity.CarModelGroupMasterEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 車種系統マスタ 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-12-01
 */
public interface ICarModelGroupMasterService extends IService<CarModelGroupMasterEntity> {

    /**
     * 車種系統情報取得
     *
     * @param makerCd メーカーCD
     * @param carModelCd 車種CD
     * @return CarGroupMstEntity
     */
    List<CarModelGroupMasterEntity> getCarModelGroupList(Long makerCd, Long carModelCd);
}
