package com.scheduleservice.googlesheets.repository.service;

import com.scheduleservice.googlesheets.repository.entity.CarModelMasterEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 車種マスタ 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-12-01
 */
public interface ICarModelMasterService extends IService<CarModelMasterEntity> {

    /**
     * 車種情報取得
     *
     * @param makerCd チームCD
     * @return CarModelMasterEntity
     */
    List<CarModelMasterEntity> getCarModelList(Long makerCd);
}
