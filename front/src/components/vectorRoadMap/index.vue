<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-20 18:58:23
 * @LastEditTime: 2024-06-21 13:23:37
 * @LastEditors: Alex
-->
<template>
  <div class="vectorRoadBtn" @click="openVectonMap" :class="{ 'activeDiv': roadMap }"
    :style="{ right: styleObj.right, bottom: styleObj.bottom }">
    <div class="icon_name">道路</div>
  </div>
  <DraggableModal v-if="showModal" v-drag @closeMap="showModal = false">
    <div class="road_box">
      <div class="search_box">
        <div class="left_input">
          <el-input v-model="fileter" @clear="resetHighlight" placeholder="搜索道路名称" :prefix-icon="Search" clearable
            size="default" />
        </div>
        <div class="right_creat" @click="serachData">搜索</div>
      </div>
      <div class="tree-box">
        <el-scrollbar ref="scrollbarRef" class="tree-scrollbar" style="height: 320px;position: relative;">
          <el-tree ref="treeRef" accordion :data="treeData" show-checkbox node-key="id"
            :default-expanded-keys="defaultExpandedKeys" :expand-on-click-node="false" :highlight-current="true"
            @check="handleCheck">
            <template #default="{ node, data }">
              <span class="custom-tree-node">
                <el-tooltip :content="data.name" placement="right">
                  <span class="node-name" @click="clickNodeName(data)" :class="{ 'highlight-text': data.highlight }">{{
                    data.name
                  }}</span>
                </el-tooltip>
                <span v-if="data.children?.length">&nbsp;&nbsp;({{ data.id === 'all_0' ? allLength :
                  data.children.length
                }})</span>
              </span>
            </template>
          </el-tree>
        </el-scrollbar>
      </div>
    </div>
  </DraggableModal>
</template>

<script setup>
import {
  reactive,
  ref,
  computed,
  getCurrentInstance,
  watch,
  nextTick,
  onMounted,
  defineEmits,
  defineProps
} from "vue";
import roadShpData from "/public/geojson/hwc";
import roadNetData from "/public/geojson/road";
import { Search } from '@element-plus/icons-vue'
import useBasicStore from "@/store/modules/basicData";
import DraggableModal from '@/components/DraggableModal';
import { loadPicture } from "@/utils";
import { sessionStorage } from "@/utils/storage";
import emitter from "@/utils/emitter";
import { ElMessage, ElMessageBox } from "element-plus";
import { ElLoading } from 'element-plus';
const { proxy } = getCurrentInstance()
const BasicStore = useBasicStore()
const roadMap = computed(() => BasicStore.roadMapBol)
const styleObj = computed(() => props.styles)
const showModal = ref(false)
const showList = ref(false)
const treeRef = ref(null);
const scrollbarRef = ref(null)
const checkedNodes = ref([]);
const defaultExpandedKeys = ref([])
const matchedKeys = ref([])
// 道路类型
const roadType = ref([
  { name: '高速道路', type: '00', id: 'gsdl' },
  { name: '城市高速路', type: '01', id: 'csgsl' },
  { name: '国道', type: '02', id: 'gd' },
  { name: '省道', type: '03', id: 'sd' },
  { name: '县道', type: '04', id: 'xd' },
  { name: '乡镇村道', type: '06', id: 'xzcd' },
  { name: '其它道路', type: '08', id: 'qtdl' },
  { name: '非引导道路', type: '09', id: 'fyddl' },
  { name: '轮渡', type: '0a', id: 'ld' },
  { name: '步行道路', type: '0b', id: 'bxdl' },
  { name: '人渡', type: '0c', id: 'rd' },
  { name: '自行车专用道', type: '0d', id: 'zxczyd' }])
const fileter = ref('')
const allLength = ref(0)
const treeData = ref([])
const props = defineProps({
  styles: {
    type: Object,
    defalut: () => {
      return { bottom: '100px', right: '100px' };
    },
  },
});
watch(
  () => roadMap.value,
  (val) => {
    if (val) {
      dosomething()
    } else {
      cancelRoadMap()
    }
  },
  { deep: false, immediate: false }
);
const loadingData = () => {
  const loading = ElLoading.service({
    lock: true,
    text: '数据量过多，加载中...',
    background: 'rgba(0, 0, 0, 0.7)',
  })
  setTimeout(() => {
    loading.close()
  }, 5000)
}
const dosomething = async () => {
  loadingData()
  // console.log('加载矢量图层', roadNetData.features)
  showModal.value = true
  if (roadNetData.features?.length) {
    const initData = mergeRoadFeatures(roadNetData.features)
    let list = initData.map(item => {
      return { name: item.attributes.PathName, id: item.attributes.ID, funcClass: item.attributes.FuncClass, type: (item.attributes.Kind).substring(0, 2), coordinates: item.geometry.paths }
    })
    let data = list.filter(item => item.name)
    // console.log(data)
    allLength.value = data.length
    const roadTree = buildRoadTree(roadType.value, data);
    treeData.value = [roadTree]
    // console.log(treeData.value)
    defaultExpandedKeys.value = treeData.value.map(node => node.id)
    checkedNodes.value = getCheckedNodesData(treeData.value);
    showList.value = true
    await nextTick()
    if (treeRef.value) {
      const checkIds = checkedNodes.value.map((item) => item.id);
      treeRef.value.setCheckedKeys(checkIds, true)
      drawRoadNetLineAndPoint(checkedNodes.value, true)
    }
  }
}
const openVectonMap = () => {
  if (roadMap.value) {
    BasicStore.set_roadMap(false)
  } else {
    BasicStore.set_roadMap(true)
  }
}
const cancelRoadMap = () => {
  showModal.value = false
  let g = window.__g
  let allDatas = getCheckedNodesData(treeData.value);
  drawRoadNetLineAndPoint(allDatas, false)
}
// 复选框切换
const handleCheck = (data, node) => {
  let list = [];
  list = data.children?.length ? getCheckedNodesData(data.children) : [data];
  const checked = node.checkedKeys.includes(data.id);
  if (checked) {
    // 显示
    drawRoadNetLineAndPoint(list, true)
  } else {
    // 隐藏
    drawRoadNetLineAndPoint(list, false)
  }
};
// 点击名称可以定位
const clickNodeName = (data) => {
  let g = window.__g
  g.marker.focus(data.id, 100, 1);
}
// 获取资源列表下的所有节点数据
const getCheckedNodesData = (arr) => {
  const nodes = [];
  arr.forEach((item) => {
    if (item.coordinates && item.funcClass) {
      nodes.push(item);
    }
    if (item.children?.length) {
      nodes.push(...getCheckedNodesData(item.children));
    }
  });
  return nodes;
};
const resetTreeHighlight = (nodes) => {
  nodes.forEach(node => {
    node.highlight = false;
    if (node.children) {
      resetTreeHighlight(node.children);
    }
  });
}
const resetHighlight = () => {
  resetTreeHighlight(treeData.value);
}
//  搜索
const serachData = async () => {
  if (fileter.value.trim()) {
    // 清除之前的高亮状态
    resetTreeHighlight(treeData.value);
    // 获取所有节点找到匹配项
    const matchedNodes = treeRef.value && treeRef.value.store._getAllNodes()
      .filter(node => node.data.name.includes(fileter.value));

    if (matchedNodes.length > 0) {
      // 高亮匹配节点并定位到第一个
      matchedNodes[0].data.highlight = true
      const firstMatch = matchedNodes[0];
      treeRef.value && treeRef.value.setCurrentKey(firstMatch.data.id);
      firstMatch.expand(); // 自动展开父级
      if (firstMatch.parent) {
        firstMatch.parent.expanded = true
      }
      const node = treeRef.value.getNode(firstMatch)
      const isChecked = node?.checked || false
      let g = window.__g
      if (isChecked) {
        g.marker.focus(firstMatch.data.id, 100, 1);
      } else {
        treeRef.value.setCheckedKeys([firstMatch.data.id])
        drawRoadNetLineAndPoint([firstMatch.data], true).then(() => {
          g.marker.focus(firstMatch.data.id, 100, 1);
        })
      }
      // 关键滚动定位代码
      // 2. 双重等待确保DOM更新
      await nextTick()
      await new Promise(resolve => setTimeout(resolve, 50))
      const nodeElement = document.querySelector(`[data-key="${firstMatch.data.id}"]`);
      if (nodeElement) {
        scrollbarRef.value.setScrollTop(nodeElement.offsetTop - 100)
      }
    } else {
      ElMessage.error('暂无此道路！')
    }
  }
}
// 构建分类树
const buildRoadTree = (categories, roadData) => {
  // 创建根节点
  const tree = {
    name: '全部',
    type: 'all',
    id: 'all_0',
    children: []
  };

  // 构建分类节点映射
  const categoryMap = new Map();
  categories.forEach(category => {
    const node = {
      ...category,
      children: []
    };
    categoryMap.set(category.type, node);
    tree.children.push(node);
  });

  // 将道路数据分配到对应分类
  roadData.forEach(road => {
    const categoryNode = categoryMap.get(road.type);
    if (categoryNode) {
      categoryNode.children.push(road);
    }
  });

  return tree;
}

/**
 * 合并同名道路坐标并统一为LineString格式
 * @param {Array} features GeoJSON特征数组
 * @returns {Array} 合并后的特征数组
 */
function mergeRoadFeatures(features) {
  const roadMap = new Map();

  features.forEach(feature => {
    const name = feature.attributes.PathName;
    const coords = extractCoordinates(feature.geometry);

    if (!roadMap.has(name)) {
      roadMap.set(name, {
        type: 'Feature',
        attributes: { ...feature.attributes },
        geometry: {
          type: 'LineString',
          paths: [coords]
        }
      });
    } else {
      const existing = roadMap.get(name);
      existing.geometry.paths = existing.geometry.paths.concat([coords]);
    }
  });

  return Array.from(roadMap.values());
}

/**
 * 从几何对象提取坐标数组
 * @param {Object} geometry GeoJSON几何对象
 * @returns {Array} 扁平化坐标数组
 */
function extractCoordinates(geometry) {
  // switch (geometry.type) {
  //   case 'LineString':
  //     return geometry.coordinates;
  //   case 'MultiLineString':
  //     return geometry.coordinates.flat();
  //   default:
  //     throw new Error(`Unsupported geometry type: ${geometry.type}`);
  // }
  return geometry.paths.flat();
}
const addZValue = (coordinates, z = 5.8) => coordinates.map(([x, y]) => [x, y, z]);

// 绘制矢量路网线和点名称
const drawRoadNetLineAndPoint = async (node, bol) => {
  let fileterNode = node;
  // console.log(node)
  let g = window.__g
  // 显示
  if (bol) {
    if (fileterNode?.length) {
      let markers = [];
      let lines = [];
      fileterNode.forEach((item, index) => {
        if (item.coordinates?.length) {
          let coors = findCentralElements(item.coordinates)
          let o = {
            id: `${item.id}`,
            groupId: 'roadNet',
            userData: item.name,
            coordinate: item.coordinates[0][1], //坐标位置
            coordinateType: 0, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
            anchors: [-15, 34],
            range: [0, 50000], //可视范围
            imageSize: [30, 34], //图片的尺寸,//图片的尺寸
            imagePath: '',
            fixedSize: true, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
            text: item.name, //显示的文字
            useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
            textRange: [0, 100000], //文本可视范围[近裁距离, 远裁距离]
            textOffset: [0, 0], // 文本偏移
            textBackgroundColor: [0, 0, 0, 0.75], //文本背景颜色
            fontSize: 10, //字体大小
            fontOutlineSize: 1, //字体轮廓线大小
            fontColor: Color.White, //字体颜色
            fontOutlineColor: Color.Black, //字体轮廓线颜色
            autoHeight: true, // 自动判断下方是否有物体
            displayMode: 2, //0：相机移动不显示，参与避让聚合;1：相机移动时显示，参与避让聚合; 2：相机移动时显示，不参与避让聚合;3：相机移动时不显示，不参与避让聚合
            priority: 0, //避让优先级
            occlusionCull: true, //是否参与遮挡剔除
            clusterByImage: true
          };
          markers.push(o)
          item.coordinates.forEach((coor, i) => {
            let line = {
              id: item.id + '_' + i,//折线唯一标识id
              groupId: 'roadNet',
              userData: item.name,
              coordinates: coor,//构成折线的坐标点数组
              coordinateType: 0,//坐标系类型，取值范围：0为Projection类型，1为WGS84类型，2为火星坐标系(GCJ02)，3为百度坐标系(BD09)，默认值：0 
              range: [0, 100000],//可视范围：[近裁距离, 远裁距离]，取值范围: [任意负值, 任意正值]
              color: [1, 0, 0, 1],//折线颜色
              thickness: 10,//折线宽度
              shape: 0, //折线类型 0：直线， 1：曲线
              depthTest: true,//是否做深度检测 开启后会被地形高度遮挡
              style: 4,//折线样式 参考样式枚举：PolylineStyle
            };
            lines.push(line)
          })
        }
      })
      if (lines?.length) {
        let datas = lines.map(item => {
          return { ...item, coordinates: addZValue(item.coordinates) }
        })
        g.polyline.add(datas)
      }
      if (markers?.length) {
        g.marker.add(markers)
      }
    }
  } else {
    // 隐藏
    if (fileterNode?.length) {
      let linesIds = []
      let delIds = []
      fileterNode.forEach(item => {
        if (item.coordinates?.length) {
          delIds.push(item.id)
          item.coordinates.forEach((coor, i) => {
            linesIds.push(item.id + '_' + i)
          })
        }
      })
      if (linesIds?.length) {
        g.polyline.delete(linesIds);
      }
      if (delIds?.length) {
        g.marker.delete(delIds)
      }
    }
  }
}
function findCentralElements(array) {
  const length = array.length;
  if (length === 0) return null;

  const midIndex = Math.floor((length - 1) / 2);
  return length % 2 === 1 ?
    [array[midIndex]] :          // 奇数长度取单个中间元素
    array.slice(midIndex, midIndex + 2); // 偶数长度取中间两个元素
}
</script>

<style scoped lang="scss">
.vectorRoadBtn {
  position: absolute;
  bottom: 180px;
  right: 340px;
  z-index: 20;
  width: 42px;
  height: 42px;
  opacity: 1;
  /* 自动布局 */
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  background: linear-gradient(180deg, rgba(10, 29, 100, 0.7) 0%, rgba(21, 30, 73, 0.6984) 100%);

  .icon_name {
    width: 42px;
    height: 42px;
    border-radius: 4px;
    opacity: 1;
    background: rgba(14, 31, 96, 0.5);
    box-sizing: border-box;
    border: 1px solid #5B759B;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #5B759B;
    font-size: 14px;
  }

  .text {
    opacity: 0.65;
    font-family: Source Han Sans;
    font-size: 12px;
    color: #fff;
    margin-top: 8px;
  }
}

.activeDiv {
  background: rgba(38, 68, 173, 0.3);

  .icon_name {
    color: #00CEFF;
  }
}

.road_box {
  width: 100%;
  height: 100%;

  .search_box {
    display: flex;
    align-items: center;

    .left_input {
      width: 80%;

      :deep(.el-input__wrapper) {
        background: none;

        .el-input__inner {
          color: #fff;
        }
      }
    }

    .right_creat {
      background: #2E5AFF;
      color: #fff;
      width: 37px;
      height: 30px;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      margin-left: 8px;
    }
  }
}

.tree-box {
  width: 100%;
  padding: 10px 0;
  height: calc(100% - 90px);
  box-sizing: border-box;
  // background: linear-gradient(
  //   180deg,
  //   #0a1d64 0%,
  //   rgba(21, 30, 73, 0.6984) 100%
  // );
  font-size: 14px;
  margin-top: 8px;

  .custom-tree-node {
    display: flex;
    align-items: center;

    .node-name {
      display: inline-block;
      max-width: 100px;
      overflow: hidden;
      text-overflow: ellipsis;
      text-wrap: nowrap;
      font-size: 12px;
    }

    .highlight-text {
      color: aqua;
    }
  }
}

:deep(.el-tree) {
  background: transparent;
  color: #fff;
  --el-tree-node-hover-bg-color: rgba(21, 30, 73, 0.6984);
  --el-tree-node-content-height: 32px;
}

:deep(.el-checkbox) {
  --el-checkbox-bg-color: transparent;
  --el-checkbox-checked-bg-color: transparent;
  --el-checkbox-checked-input-border-color: #fff;
}

:deep(.el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content) {
  background-color: rgba(21, 30, 73, 0.6);
}

:deep(.el-tree-node > .el-tree-node__children) {
  margin-left: -6px;
}
</style>
