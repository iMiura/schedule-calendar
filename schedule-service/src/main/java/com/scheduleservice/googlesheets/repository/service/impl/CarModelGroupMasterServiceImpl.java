package com.scheduleservice.googlesheets.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.scheduleservice.googlesheets.repository.entity.CarModelGroupMasterEntity;
import com.scheduleservice.googlesheets.repository.mapper.CarModelGroupMasterMapper;
import com.scheduleservice.googlesheets.repository.service.ICarModelGroupMasterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 車種系統マスタ 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-12-01
 */
@Service
public class CarModelGroupMasterServiceImpl extends ServiceImpl<CarModelGroupMasterMapper, CarModelGroupMasterEntity> implements ICarModelGroupMasterService {

    @Override
    public List<CarModelGroupMasterEntity> getCarModelGroupList(Long makerCd, Long carModelCd) {
        LambdaQueryWrapper<CarModelGroupMasterEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CarModelGroupMasterEntity::getMakerCd, makerCd)
            .eq(CarModelGroupMasterEntity::getCarModelCd, carModelCd);
        queryWrapper.orderByAsc(CarModelGroupMasterEntity::getCarModelGroupCd);

        return list(queryWrapper);
    }
}
