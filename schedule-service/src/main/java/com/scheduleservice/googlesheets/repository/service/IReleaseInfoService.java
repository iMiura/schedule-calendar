package com.scheduleservice.googlesheets.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scheduleservice.googlesheets.repository.entity.ReleaseInfoEntity;
import java.time.LocalDateTime;

/**
 * <p>
 * リリース情報 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-11-30
 */
public interface IReleaseInfoService extends IService<ReleaseInfoEntity> {

    /**
     * リリース情報登録
     *
     * @param releaseInfoEntity リリース情報Entity
     * @return boolean
     */
    boolean saveReleaseInfo(ReleaseInfoEntity releaseInfoEntity);

    /**
     * リリース情報更新
     *
     * @param releaseInfoEntity リリース情報Entity
     * @param finalChangeDate 最終更新日時
     * @return boolean
     */
    boolean updateReleaseInfo(ReleaseInfoEntity releaseInfoEntity, LocalDateTime finalChangeDate);
}
