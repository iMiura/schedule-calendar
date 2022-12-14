package com.scheduleservice.googlesheets.release.service;

import com.scheduleservice.googlesheets.exception.ServiceException;
import com.scheduleservice.googlesheets.release.entity.ReleaseInfo;
import java.util.Map;

/**
 * @author :keisho
 */
public interface ReleaseService {

    /**
     * リリース情報検索画面初期化情報取得
     */
    Map initReleaseSearch() throws ServiceException;

    /**
     * 車種情報取得
     *
     * @param makerCd メーカーCD
     */
    Map getCarModelList(Long makerCd) throws ServiceException;

    /**
     * 車種系統情報取得
     *
     * @param makerCd メーカーCD
     * @param carModelCd 車種CD
     */
    Map getCarModelGroupList(Long makerCd, Long carModelCd) throws ServiceException;

    /**
     * リリース情報のスプレッドシートファイル取得
     *
     * @param releaseInfo 検索条件
     * @param checked 未チェック分を表示
     */
    Map getReleaseFile(ReleaseInfo releaseInfo, String checked) throws ServiceException;

    /**
     * リリース情報取得
     *
     * @param releaseInfoId リリースNo.
     */
    Map getReleaseInfo(String releaseInfoId) throws ServiceException;

    /**
     * スプレッドシート情報存在チェック
     */
    boolean checkReleaseFile();

    /**
     * リリース情報更新
     *
     * @param releaseInfo 検索条件
     * @param finalChangeDate 最終更新日時
     */
    boolean saveReleaseInfo(ReleaseInfo releaseInfo, String finalChangeDate) throws ServiceException;

    /**
     * リリース情報削除
     *
     * @param releaseInfoId リリースNo.
     * @param finalChangeDate 最終更新日時
     */
    boolean delReleaseInfo(String releaseInfoId, String finalChangeDate) throws ServiceException;
}
