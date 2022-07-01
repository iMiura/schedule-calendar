/**
 * サーバレスポンスcodeEnum
 */
export enum ResponseEnum {

  /**
   * 成功code
   */
  SUCCESS = 200,
  /**
   * 失敗code
   */
  FAILED = 500,
  /**
   * token失効code
   */
  TOKEN_EXPIRE = -9999,
  /**
   * tokenリフレッシュcode
   */
  TOKEN_REFRESH = 3,

  /**
   * 401（権限なし）
   */
  RESP_UNAUTHORIZED = 401,

  /**
   * 406（Not Acceptable）
   */
  RESP_UNRESOLVED = 406,

  /**
   * 500（サーバ異常）
   */
  RESP_ERROR = 500,

  /**
   * 0（サーバ反応なし）
   */
  RESP_NO_RESPONSE = 0,
  /**
   * 失敗code
   */
  VERIFY_EXPIRE_ERROR = -3,
}
