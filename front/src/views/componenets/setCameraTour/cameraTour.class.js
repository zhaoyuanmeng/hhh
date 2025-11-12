class CameraTour {
  constructor() {}

  /**
   * 创建一个或多个CameraTour对象
   * @param {CameraTourData | array} data CameraTourData类的对象或者数组
   * @returns 可选的回调函数
   */
  add = function (data) {
    return new Promise((resolve, reject) => {
      window.__g.cameraTour.add(data, () => {
        resolve();
      });
    });
  };
  /**
   * 删除一个或多个CameraTour对象
   * @param {string | array} ids 要删除的CameraTour对象的ID或者ID数组（可以删除一个或者多个）
   * @returns 可选的回调函数
   */
  delete = function (ids) {
    return new Promise((resolve, reject) => {
      window.__g.cameraTour.delete(ids, () => {
        resolve();
      });
    });
  };
  /**
   * 开始播放导览动画
   * @param {string} id 相机导览的ID
   * @returns 可选的回调函数
   */
  play = function (id) {
    return new Promise((resolve, reject) => {
      window.__g.cameraTour.play(id, () => {
        resolve();
      });
    });
  };
  /**
   * 停止播放导览动画
   * @returns 可选的回调函数
   */
  stop = function () {
    return new Promise((resolve, reject) => {
      window.__g.cameraTour.stop(() => {
        resolve();
      });
    });
  };
  /**
   * 暂停播放导览动画
   * @returns 可选的回调函数
   */
  pause = function () {
    return new Promise((resolve, reject) => {
      window.__g.cameraTour.pause(() => {
        resolve();
      });
    });
  };
  /**
   * 暂停播放导览动画
   * @returns 可选的回调函数
   */
  resume = function () {
    return new Promise((resolve, reject) => {
      window.__g.cameraTour.resume(() => {
        resolve();
      });
    });
  };
}

export default CameraTour;
