package com.scheduleservice.googlesheets.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scheduleservice.googlesheets.repository.entity.UserInfoEntity;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * ユーザ情報 Mapper
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
public interface UserInfoMapper extends BaseMapper<UserInfoEntity> {

    /**
     * ユーザー情報取得
     */
    @Select({
        " select * from user_info ",
        "  where gmailAddress = #{gmailAddress,jdbcType=VARCHAR} ",
    })
    List<UserInfoEntity> selectByGmail(@Param("gmailAddress") String gmail);

}
