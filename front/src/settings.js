/*
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-06 17:53:07
 * @LastEditTime: 2024-06-06 17:58:10
 * @LastEditors: Alex
 */
export default {
  /**
   * 网页标题
   */
  title: import.meta.env.VITE_APP_TITLE,
  /**
   * @type {string | array} 'production' | ['production', 'development']
   * @description Need show err logs component.
   * The default is only used in the production env
   * If you want to also use it in dev, you can pass ['production', 'development']
   */
  errorLog: 'production',
  /**
   * @type {string} DES | AES  注意要大写 默认DES
   * @description 加密方式
   */
   cryptoType: 'DES',
   /**
    * @type {string} UP | DOWN | ALL | ''  注意要大写
    * @description direct: ALL #UP：上行，仅接口入参解密 DOWN：下行，仅接口返回加密 ALL：全部 ''：不做加密处理
    */
   direct: 'ALL',
   /**
    * @type {string}
    * @description 参数加密的key
    */
   cryptoKey: 'LFcRvbDRJshQzmlv',
   /**
    * @type {boolean} true | false
    * @description 越权处理
    */
   AuthTicket: true,
   /**
    * @type {string}
    * @description 越权处理的key
    */
   AuthTicketKey: 'LFcRvbDRJshQzmlv',
     /**
   * @description 是否使用国际化，默认为false
   *              如果不使用，则需要在路由中给需要在菜单中展示的路由设置meta: {title: 'xxx'}
   *              用来在菜单中显示文字
   */
  useI18n: false,
  devDebug: ''
}
