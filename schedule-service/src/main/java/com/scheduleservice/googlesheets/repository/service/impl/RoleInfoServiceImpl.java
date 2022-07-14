package com.scheduleservice.googlesheets.repository.service.impl;

import com.scheduleservice.googlesheets.repository.entity.RoleInfoEntity;
import com.scheduleservice.googlesheets.repository.mapper.RoleInfoMapper;
import com.scheduleservice.googlesheets.repository.service.IRoleInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 役割情報 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-07-12
 */
@Service
public class RoleInfoServiceImpl extends ServiceImpl<RoleInfoMapper, RoleInfoEntity> implements IRoleInfoService {

}
