package com.scheduleservice.googlesheets.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.scheduleservice.googlesheets.repository.entity.UserInfoEntity;
import com.scheduleservice.googlesheets.repository.mapper.UserInfoMapper;
import com.scheduleservice.googlesheets.repository.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * <p>
 * ユーザ情報 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfoEntity> implements IUserInfoService {

    @Override
    public UserInfoEntity getByGmail(String gmail) {
        LambdaQueryWrapper<UserInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfoEntity::getGmailAddress, gmail).eq(UserInfoEntity::getDelFlg, 0).last("limit 1");
        return getOne(queryWrapper);
    }

    @Override
    public List<UserInfoEntity> getUserList() {
        LambdaQueryWrapper<UserInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfoEntity::getDelFlg, 0).ne(UserInfoEntity::getRoleId, 6);
        return list(queryWrapper);
    }
}
