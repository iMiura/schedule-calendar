package com.scheduleservice.googlesheets.repository.service.impl;

import com.scheduleservice.googlesheets.repository.entity.TeamInfoEntity;
import com.scheduleservice.googlesheets.repository.mapper.TeamInfoMapper;
import com.scheduleservice.googlesheets.repository.service.ITeamInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * チーム情報 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
@Service
public class TeamInfoServiceImpl extends ServiceImpl<TeamInfoMapper, TeamInfoEntity> implements ITeamInfoService {

}
