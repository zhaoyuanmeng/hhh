import request from "@/utils/request";

// 基础列表接口
export function GetArchivesList(params) {
  return request({
    url: "/specialServiceArchives/getArchivesList",
    method: "get",
    params,
  });
}

//根据档案ID查询基本信息表单项
export function GetBasicinfoById(params) {
  return request({
    url: `/specialServiceArchives/getBasicInfoById/${params}`,
    method: "get",
  });
}

// 添加基础信息表单
export function PostAddFormItemInfo(params) {
  return request({
    url: "/specialServiceArchivesForm/addFormItemInfo",
    method: "post",
    data:params,
  });
}

  /**
   * 根据档案id查询点位类目列表（带统计数据）
   * @params jcxxId - 目标档案基础信息ID; parentId - 所属档案ID
   */
export function getArchivesInfoList(params) {
  return request({
    url: `/specialServiceArchivesForm/selectPointInfo/${params.parentId}/${params.jcxxId}`,
    method: "get",
  });
}

  /**
   * 查询目标档案基础信息列表
   * @params parentTypeId - 档案ID
   * */
export function PostBasicInfoList(params) {
  return request({
    url: '/specialServiceArchives/basicInfoList',
    method: "post",
    data:params,
  });
}

 /**
     * 删除档案信息
     */
export function deletePointById(id) {
  return request({
    url:  `/specialServiceArchivesForm/deletePointById${id}`,
    method: "delete",
  });
}

  /**
   * 查询重点点位表单列表、数据、重点点位列表
   * @params
   */
export function GetSelectPointList(params) {
  return request({
    url: '/specialServiceArchivesForm/selectPointList',
    method: "get",
    params,
  });
}

  /**
   * 添加重点点位表单
   * @params form表单
   */
export function PostAddFormInfo(params) {
  return request({
    url: "/specialServiceArchivesForm/addFormInfo",
    method: "post",
    data:params,
  });
}

  /**
   * 添加重点点位表单
   * @params form表单
   */
  export function deleteFormInfo(id) {
    return request({
      url: `/specialServiceArchivesForm/deleteFormInfo/${id}`,
      method: "delete",
    });
  }
    /**
   * 根据档案id获取档案下面的信息
   */
    export function getInfoByArchivesId(params) {
      return request({
        url: `/specialServiceArchives/getInfoByArchivesId`,
        method: "get",
        params
      });
    }
      /**
    * 根据档案内容id查询table 列表，分页
    */
      export function getPointInfoByArchivesId(params) {
        return request({
          url: `/specialServiceArchives/getPointInfoByArchivesId`,
          method: "get",
          params
        });
      }

        /**
    * 重新拖拽排序
    */
        export function dropDatsToSort(data) {
          return request({
            url: `/specialServiceArchives/saveFormItemSort`,
            method: "post",
            data
          });
        }  

              /**
    * 删除任务关联文件
    */
      export function deleteTaskFile(params) {
        return request({
          url: `/file/deleteFile`,
          method: "get",
          params
        });
      }