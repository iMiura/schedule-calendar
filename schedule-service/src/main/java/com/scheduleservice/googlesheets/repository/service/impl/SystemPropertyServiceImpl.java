package com.scheduleservice.googlesheets.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scheduleservice.googlesheets.repository.entity.SystemPropertyEntity;
import com.scheduleservice.googlesheets.repository.mapper.SystemPropertyMapper;
import com.scheduleservice.googlesheets.repository.service.ISystemPropertyService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * システムプロパティ 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
@Service
public class SystemPropertyServiceImpl extends ServiceImpl<SystemPropertyMapper, SystemPropertyEntity> implements ISystemPropertyService {

    @Override
    public String getProperty(Long teamId, String propertyKey) {
        LambdaQueryWrapper<SystemPropertyEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemPropertyEntity::getTeamId, teamId).eq(SystemPropertyEntity::getPropertyKey, propertyKey).last("limit 1");
        return getOne(queryWrapper).getPropertyValue();
    }
}
