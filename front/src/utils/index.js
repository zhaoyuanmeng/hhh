/*
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-15 19:41:57
 * @LastEditTime: 2024-06-22 14:41:43
 * @LastEditors: Alex
 */
/**
 * @description: 动态加载图片 （注意：将图片放到public目录下）
 * @param {*} imgUrl public目录下图片的地址：eg: /public/imgs/a.png, 则imgUrl为 ./imgs/a.png
 * @return {*} 返回图片的绝对路径
 */
export const loadPicture = (imgUrl) => {
  let pathnameArr = location.pathname.split("/");
  let realPathArr = [];
  pathnameArr.forEach((item) => {
    if (item && item.slice(-5) !== ".html") {
      realPathArr.push(item);
    }
  });
  let realPath = location.origin + "/";
  if (realPathArr.length > 0) {
    realPath = realPath + realPathArr.join("/") + "/";
  }

  return new URL(imgUrl, realPath).href;
};

export const retainAfter = (path, search)=> {
  const parts = path.split(search);
  return parts.length > 1 ? search + parts.slice(1).join('') : '';
}

/**
 * @description 将树形结构的多维数组转为一维数组
 * @param {array} data - 多维数组
 * @param {array} arr - 存储数据
 * @param {string} childKey - 需要转化的子数组key
 * @returns {array} 一维数组
 * @example

*/
 // 递归函数，用于将树形数据转换为一维数组
 export function flattenTreeData(treeData, result = []) {
  for (let i = 0; i < treeData.length; i++) {
    const node = treeData[i];
    result.push(node); // 将当前节点添加到结果数组中
    if (node.children && node.children.length > 0) {
      flattenTreeData(node.children, result); // 递归处理子节点
    }
  }
  return result;
}
/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 */
export function handleTree(data, id, parentId, children) {
  let config = {
    id: id || "id",
    parentId: parentId || "parentId",
    childrenList: children || "children",
  };

  var childrenListMap = {};
  var nodeIds = {};
  var tree = [];

  for (let d of data) {
    let parentId = d[config.parentId];
    if (childrenListMap[parentId] == null) {
      childrenListMap[parentId] = [];
    }
    nodeIds[d[config.id]] = d;
    childrenListMap[parentId].push(d);
  }

  for (let d of data) {
    let parentId = d[config.parentId];
    if (nodeIds[parentId] == null) {
      tree.push(d);
    }
  }

  for (let t of tree) {
    adaptToChildrenList(t);
  }

  function adaptToChildrenList(o) {
    if (childrenListMap[o[config.id]] !== null) {
      o[config.childrenList] = childrenListMap[o[config.id]];
    }
    if (o[config.childrenList]) {
      for (let c of o[config.childrenList]) {
        adaptToChildrenList(c);
      }
    }
  }
  return tree;
}

/**
 * 根据子id找到父节点id
 * @param {*} id 数据源
 * @param {*} tree 传入的树结构数据
 */
export function getParentTree(id, tree) {
  let arr = [] //要返回的数组
  for (let i = 0; i < tree.length; i++) {
    let item = tree[i]
    arr = []
    arr.push(item) //保存当前节点id
    if (id== item.id) {
      //判断当前id是否是默认id
      return arr //是则退出循环、返回数据
    } else {
      //否则进入下面判断，判断当前节点是否有子节点数据
      if (item.children && item.children.length > 0) {
        //合并子节点返回的数据
        arr = arr.concat(getParentTree(id, item.children ? item.children : []))
        if (arr.map(item2 => (item2 ? item2.id : '')).includes(id)) {
          //如果当前数据中已包含默认节点，则退出循环、返回数据
          return arr
        }
      }
    }
  }
}

// 引入图片的问题
/** vite的特殊性, 需要处理图片 */
export function require_(imgPath){
  try {
    const handlePath = imgPath.replace('@', '..')
    return new URL(handlePath, import.meta.url).href
  } catch (error) {
    console.warn(error)
  }
}
function distanceBetweenPoints(point1, point2) {
  let dx = point2[0] - point1[0];
  let dy = point2[1] - point1[1];
  let dz = point2[2] - point1[2];
  return Math.sqrt(dx*dx + dy*dy + dz*dz);
}
// 计算各点位之间的整个的长度
/** vite的特殊性, 需要处理图片 */
export function calculateTotalLength(points){
  let totalLength = 0;
  for (let i = 0; i < points.length - 1; i++) {
      totalLength += distanceBetweenPoints(points[i], points[i + 1]);
  }
  return totalLength;
}

/** 根据名称拿到图片 */
export function getImageForName(name){
  console.log(name)
  let str = '重点部位';
  if(name.includes('出入口')){
    str = '出入口';
  }
  if(name.includes('服务区')){
    str = '服务区';
  }
  if(name.includes('桥梁')){
    str = '桥梁';
  }
  if(name.includes('涵洞')){
    str = '涵洞';
  }
  if(name.includes('隧道')){
    str = '隧道';
  }
  if(name.includes('制高点')){
    str = '制高点';
  }
  if(name.includes('两侧危爆物')){
    str = '两侧危险物品厂库';
  }
  if(name.includes('复杂路段')){
    str = '复杂路段';
  }
  if(name.includes('复杂村镇')){
    str = '复杂村落';
  }
  if(name.includes('高速交界结合部')){
    str = '高速结合部';
  }
  if(name.includes('下穿道路')){
    str = '下穿道路';
  }
  if(name.includes('疏散梯')){
    str = '疏散梯';
  }
  if(name.includes('基站')){
    str = '高铁基站';
  }
  if(name.includes('四电所')){
    str = '高铁四电所';
  }
  if(name.includes('低路基')){
    str = '平交路口';
  }
  if(name.includes('车站')){
    str = '高铁车站登记';
  }
  if(name.includes('高铁交接结合')){
    str = '高铁结合部协议';
  }
  if(name.includes('监控')){
    str = '沿线监控';
  }

  if(name.includes('医院')){
    str = '医院';
  }
  if(name.includes('复杂村庄')){
    str = '复杂村庄';
  }
  if(name.includes('工地')){
    str = '工地';
  }
  if(name.includes('复杂地段')){
    str = '复杂地段';
  }
  if(name.includes('其他情况')){
    str = '其他情况';
  }
  if(name.includes('丁字路口')){
    str = '丁字路口';
  }
  if(name.includes('红绿灯')){
    str = '红绿灯';
  }
  if(name.includes('公交站')){
    str = '公交站';
  }
  if(name.includes('危爆场所')){
    str = '危爆场所';
  }
  if(name.includes('主线桥')){
    str = '主线桥';
  }
  if(name.includes('跨线桥')){
    str = '跨线桥';
  }
  if(name.includes('限高')){
    str = '限高';
  }
  if(name.includes('学校')){
    str = '学校';
  }
  if(name.includes('十字路口')){
    str = '十字路口';
  }
  if(name.includes('井盖')){
    str = '井盖';
  }
  return str;
}


// 判断不能为空校验
export function dialogRules(type) {
  return [Object.freeze({
    required: true,
    validator: (rule, value, callback) => {
      const Text = new Error('请填写此字段');
      if(typeof(value) === 'string'){
        value = value.replace(/\s+/g, "");
        if(rule.notZero && value == 0){
          callback(new Error('该字段不能为0'));
        }
        if(value===""){
          callback(Text);
        }else{
          callback();
        }
      }else if(typeof(value)==="number"){
        if(rule.notZero && value == 0){
          callback(new Error('该字段不能为0'));
        }else{
          callback();
        }
      }else if(value instanceof Array){
        if (value.length < 1 || value.filter(item=>item!=='').length < 1) {
          callback(Text);
        } else {
          callback();
        }
      }else{
        if (!value) {
          callback(Text);
        } else {
          callback();
        }
      }
    },
    trigger: type,
  })]
}

// 对象数组根据id去重
export function arrToRepeat(arr){
  const uniqueArr = arr.filter((item, index, self) =>
  index === self.findIndex(t => t.id === item.id)
);
return uniqueArr
}