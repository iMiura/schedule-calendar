package com.scheduleservice.googlesheets.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.scheduleservice.googlesheets.repository.entity.CarModelMasterEntity;
import com.scheduleservice.googlesheets.repository.mapper.CarModelMasterMapper;
import com.scheduleservice.googlesheets.repository.service.ICarModelMasterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 車種マスタ 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-12-01
 */
@Service
public class CarModelMasterServiceImpl extends ServiceImpl<CarModelMasterMapper, CarModelMasterEntity> implements ICarModelMasterService {

    @Override
    public List<CarModelMasterEntity> getCarModelList(Long makerCd) {
        LambdaQueryWrapper<CarModelMasterEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CarModelMasterEntity::getMakerCd, makerCd);
        queryWrapper.orderByAsc(CarModelMasterEntity::getCarModelCd);

        return list(queryWrapper);
    }
}
