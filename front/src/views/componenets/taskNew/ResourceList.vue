<template>
  <div class="resource-list">
    <div
      class="header-box"
      :class="{ bg_press: showList }"
      @click="handleCollapse"
    >
      <p class="header-title" :class="{ active: showList }">
        <span
          class="icon"
          :class="{ 'icon-normal': !showList, 'icon-press': showList }"
        ></span>
        <span>资源列表</span>
      </p>
    </div>
    <div class="tree-box" v-if="showList">
      <el-scrollbar>
        <div style="margin: 0 10px 0 6px">
          <el-tree
            ref="treeRef"
            :data="treeData"
            show-checkbox
            node-key="id"
            :default-expanded-keys="defaultExpandedKeys"
            :expand-on-click-node="false"
            highlight-current
            @check="handleCheck"
          >
            <template #default="{ node, data }">
              <span class="custom-tree-node">
                <img
                  class="icon"
                  v-if="data.type && data.dataLevelFlag === '1'"
                  :src="loadPicture(`./images/resource/${data.type==='警车'&&data.data.marker.userData==='船舶'?'cb':data.type}.png`)"
                  alt=""
                  @click="clickNodeName(data)"
                />
                <el-tooltip :content="data.name" placement="right">
                  <span class="node-name" @click="clickNodeName(data)"
                    >{{ data.name
                    }}{{ data.floorNum ? `-${data.floorNum}F` : "" }}</span
                  >
                </el-tooltip>
                <span v-if="data.children?.length"
                  >&nbsp;&nbsp;({{ data.num }})</span
                >
              </span>
            </template>
          </el-tree>
        </div>
      </el-scrollbar>
    </div>
  </div>
</template>

<script setup>
import { loadPicture } from "@/utils";
import { ArrowDown, ArrowUp } from "@element-plus/icons-vue";
import {
  computed,
  ref,
  onMounted,
  defineEmits,
  nextTick,
  defineProps,
  watch,
  onUnmounted,
  onBeforeUnmount,
} from "vue";
import { getReourceDataForScene } from "@/api/task/task";
// 驾驶舱store
import useWorkCockpitStore from "@/store/modules/workCockpit.js";
const WorkCockpitStore = useWorkCockpitStore();
const eventMapData = computed(() => WorkCockpitStore.eventMapData);
import {
  drawResourceDataHide,
  drawResourceDataShowHide,
  drawResourceDataAll,
  saveBasicData
} from "@/components/SmartMap/js/resource";
// store
import emitter from "@/utils/emitter";
import useScreenStore from "@/store/modules/screenStore";
import useFloorStore from "@/store/modules/floorStore";
const floorStore = useFloorStore();
const useScreen = useScreenStore();
const taskItem = computed(() => useScreen.screenInfo);
let showResouce = computed(() => WorkCockpitStore.showResouce);
let openFloors = computed(() => floorStore.openFloor);
const floornum = computed(() => floorStore.floornum); // 当前楼层点击楼层数
let explodebuildname = computed(() => floorStore.explodebuildname); //当前点击的楼栋名称
let selectIds = ref([]);
const emit = defineEmits(["close"]);
const showList = ref(true);
const markerTimer = ref(null);
const showFlag = ref(true);
const clickId = ref("");
const defaultExpandedKeys = ref([])
const handleCollapse = () => {
  clearTimerFun();
  showList.value = !showList.value;
};

const hideFloorsPoint = () => {
  let g = window.__g
  g.marker.deleteByGroupId('room')
  g.marker3d.deleteByGroupId('room')
  const checkedNodes = getCheckedNodes(treeData.value);
  let fileterNode = checkedNodes.filter(
    (item) => item.floorNum && item.buildName
  );
  drawResourceDataShowHide(fileterNode, false);
};

const showFloorsPoint = () => {
  let checkdNodes = treeRef.value && treeRef.value.getCheckedNodes();
  let fileterNode = checkdNodes.filter(
    (item) => (item.floorNum&&item.buildName)||item.type==='企业数据'||item.type==='商业数据'||item.type==='社区数据'
  );
  if(fileterNode?.length){
  //   for(const item of fileterNode){
  //       if (
  //       Number(item.floorNum) === Number(floornum.value) &&
  //       item.buildName === explodebuildname.value
  //     ) {
  //       selectIds.push(item);
  //     }
  //   }
  //   console.log(selectIds)
  //   let selectNodes = checkdNodes.filter(
  //   (item) => !item.floorNum && !item.buildName
  // );
  // let lastArr = selectIds.concat(selectNodes)
  // treeRef.value && treeRef.value.setCheckedNodes(lastArr, true);
    drawResourceDataShowHide(fileterNode, true);
  }
};

const props = defineProps({
  // 是否为在驾驶舱页面展示
  isCocpit: {
    type: Boolean,
    default: false,
  },
});
const treeData = ref([]);
const treeRef = ref(null);
// 点击要素名称
const clickNodeName = async (data) => {
  console.log(data,'11111')
  let  g = window.__g
  if (data.type === "场景路线" || data.type === "应急路线") {
    g.polyline.focus(data.data.id, 50, 0.5);
  }
  if (data.type === "警戒线") {
    g.polygon3d.focus(data.data.id, 50, 0.5);
  }
  if (data.data.jingdu && data.data.weidu) {
    g.markerLayer.focusByMarkerId('gsgtcity',data.id, 20, 0.5);
  }
  if(data.data.marker){
    if(data.groupId==='police'||data.groupId==='car'||data.groupId==='uav'){
      g.customObject.focus(data.id, 2, 0.5)
      // g.marker.focus(data.id, 2, 0.5)
      g.marker3d.focus(data.id, 2, 0.5)
    }else{
      g.markerLayer.focusByMarkerId(data.id,data.id, 10, 0.5);
    }
    // g.marker.focus(data.id, 20, 0.5);
  }
  // if (data.data.marker || (data.data.jingdu && data.data.weidu)) {
  //   g.marker.focus(data.id, 50, 0.5);
  // }
  // console.log(data)
  // if (data.data.jingdu && data.data.weidu) {
  //   g.markerLayer.focusByMarkerId('gsgtcity',data.id, 20, 0.5);
  // }
  // if(data.data.marker){
  //   // g.marker.focus(data.id, 20, 0.5);
  //   g.markerLayer.focusByMarkerId(data.id,data.id, 20, 0.5);
  // }
  // clearTimerFun().then(async () => {
  //   let g = window.__g;
  //   let info = await g.marker.get(data.id);
  //   if (info.result !== 0) {
  //     return;
  //   }
  //   await g.marker.focus(data.id, 20, 0.1);
  //   clickId.value = data.id;
  //   // markerTimer.value = setInterval(() => {
  //   //   if (showFlag.value) {
  //   //     showMrkers(data.id);
  //   //   } else {
  //   //     hideMarkers(data.id);
  //   //   }
  //   // }, 1000);
  // });
};
const showMrkers = async (id) => {
  let g = window.__g;
  await g.marker.show(id);
  showFlag.value = false;
};
const hideMarkers = async (id) => {
  let g = window.__g;
  await g.marker.hide(id);
  showFlag.value = true;
};
const clearTimerFun = async () => {
  let g = window.__g;
  if (clickId.value) {
    await g.marker.show(clickId.value);
    clickId.value = "";
  }
  if (markerTimer.value) {
    clearInterval(markerTimer.value);
    markerTimer.value = null;
    showFlag.value = true;
  }
};
// 复选框切换
const handleCheck = (data, node) => {
  let checkdNodes = treeRef.value && treeRef.value.getCheckedNodes();
  const checkedLeafNodes = checkdNodes.filter(node => (!node.children || node.children.length === 0)&&node.dataLevelFlag==='1');
  saveBasicData(checkedLeafNodes) // 保存勾选的节点
  let list = [];
  list = data.children?.length ? getCheckedNodes(data.children) : [data];
  console.log("点击复选框", list);
  const checked = node.checkedKeys.includes(data.id);
  checked
    ? drawResourceDataShowHide(list, true)
    : drawResourceDataShowHide(list, false);
};

// 获取经纬度不为空的所有节点
const getCheckedNodes = (arr) => {
  const nodes = [];
  arr.forEach((item) => {
    if (item.dataLevelFlag === "1") {
      nodes.push(item);
    }
    if (item.children?.length) {
      nodes.push(...getCheckedNodes(item.children));
    }
  });
  return nodes;
};

// 获取资源树
const getTreeData = () => {
  const sceneId = props.isCocpit
    ? eventMapData.value.GroupID
    : taskItem.value.id;
  getReourceDataForScene({ sceneId }).then((res) => {
    WorkCockpitStore.set_basicIds(sceneId,1)
    treeData.value = res.data || [];
    if(res.data?.length){
      defaultExpandedKeys.value = res.data.map(node => node.id)
    }
    const checkedNodes = getCheckedNodes(treeData.value);
    let fileterNode = checkedNodes.filter((item) => !item.floorNum);
      drawResourceDataHide(fileterNode);
      nextTick(() => {
        if (WorkCockpitStore.threePageType === "roadLine") {
          let taskLine = fileterNode.filter((item) => item.type === "场景路线");
          if (taskLine?.length) {
            showList.value = true;
            WorkCockpitStore.set_taskLines(taskLine)
            nextTick(() => {
              drawResourceDataShowHide(taskLine, true);
              treeRef.value && treeRef.value.setCheckedNodes(taskLine, true);
            });
          } else {
            showList.value = false;
          }
        } else {
          let taskLine = fileterNode.filter((item) => item.type === "警戒线");
          if (taskLine?.length) {
            showList.value = true;
            nextTick(() => {
              drawResourceDataShowHide(taskLine, true);
              treeRef.value && treeRef.value.setCheckedNodes(taskLine, true);
            });
          } else {
            showList.value = false;
          }
        }
      });
  });
};
const setCheckedKeysAndExpand = (keys) => {
      // 展开勾选的节点
      keys.forEach((key) => {
        const node = treeRef.value.getNode(key);
        if (node) {
          // 展开节点
          node.expanded = true;
          // 递归展开所有父节点
          let parent = treeRef.value.getParent(node.data);
          while (parent) {
            parent.expanded = true;
            parent = treeRef.value.getParent(parent.data);
          }
        }
      });
    };
onMounted(() => {
    getTreeData();
  if (markerTimer.value) {
    clearInterval(markerTimer.value);
    markerTimer.value = null;
    showFlag.value = true;
  }
  emitter.on("refreshResource", (data) => {
  showList.value = true;
  const sceneId = props.isCocpit
    ? eventMapData.value.GroupID
    : taskItem.value.id;
  if (data === "hide") {
    hideFloorsPoint();
  } else if (data === "show") {
    showFloorsPoint();
  } else if(data==='reset'){
    let g = window.__g
    g.marker.deleteByGroupId('room')
    g.marker3d.deleteByGroupId('room')
    const checkedNodes = getCheckedNodes(treeData.value);
  let fileterNode = checkedNodes.filter(
    (item) => item.floorNum && item.buildName
  );
  drawResourceDataShowHide(fileterNode, false);
  let checkdNodes = treeRef.value && treeRef.value.getCheckedNodes();
  if(checkdNodes){
    let selectNodes = checkdNodes.filter(
    (item) => item.type==='现场室内'||item.type==='住地室内'
  );
  let ids = selectNodes.map(item=>item.id)
  treeRef.value && treeRef.value.setChecked(ids[0], false, true);
  }
  }else if(Array.isArray(data)){
    console.log('勾选路线以及警戒线')
    // 获取当前已勾选的节点
    let ids = data.map(item=>item.id)
    const currentCheckedKeys = treeRef.value && treeRef.value.getCheckedKeys();
    if(currentCheckedKeys){
        // 将额外的节点 ID 添加到当前已勾选的节点数组中
        const newCheckedKeys = [...currentCheckedKeys, ...ids];
      // 设置新的勾选状态
      treeRef.value && treeRef.value.setCheckedKeys(newCheckedKeys,true);
      // if(newCheckedKeys?.length){
      //   setCheckedKeysAndExpand(newCheckedKeys)
      // }
    }
    
  }else{
    getReourceDataForScene({ sceneId }).then((res) => {
      WorkCockpitStore.set_basicIds(sceneId,1)
      let checkedKeys = treeRef.value && treeRef.value.getCheckedKeys();
      treeData.value = res.data || [];
      if(res.data?.length){
      defaultExpandedKeys.value = res.data.map(node => node.id)
    }
      nextTick(() => {
        if(data.name==='yjbj'){
          const currentCheckedKeys = treeRef.value && treeRef.value.getCheckedKeys();
          let checkIds = checkedKeys.concat(data.idArr);
           // 将额外的节点 ID 添加到当前已勾选的节点数组中
        const newCheckedKeys = [...currentCheckedKeys, ...data.idArr];
            treeRef.value && treeRef.value.setCheckedKeys(newCheckedKeys, true);
        }else{
          let checkIds = checkedKeys.concat([data]);
          treeRef.value && treeRef.value.setCheckedKeys(checkIds, true);
        }
       
        // if(checkIds?.length){
        //   // 展开勾选节点
        //   setCheckedKeysAndExpand(checkIds)
        // }
      });
    });
  }
});
});
onUnmounted(() => {
  if (markerTimer.value) {
    clearInterval(markerTimer.value);
    markerTimer.value = null;
    showFlag.value = true;
  }
  emitter.off("refreshResource");
});
onBeforeUnmount(() => {
  if (markerTimer.value) {
    clearInterval(markerTimer.value);
    markerTimer.value = null;
    showFlag.value = true;
  }
  emitter.off("refreshResource");
});
</script>

<style lang="scss" scoped>
.header-box {
  position: absolute;
  top: 72px;
  left: 20px;
  width: 128px;
  height: 42px;
  background: url("@/assets/workcockpit/res_bg_normal.png") no-repeat center;
  background-size: contain;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 51;
  cursor: pointer;

  .header-title {
    font-size: 14px;

    .icon {
      display: inline-block;
      width: 12px;
      height: 12px;
      margin-right: 10px;
    }

    .icon-normal {
      background: url("@/assets/workcockpit/res_normal_icon.png") no-repeat;
    }

    .icon-press {
      background: url("@/assets/workcockpit/res_press_icon.png") no-repeat;
    }
  }

  .active {
    color: #00ceff;
  }
}

.bg_press {
  background: url("@/assets/workcockpit/res_bg_press.png") no-repeat;
}

.tree-box {
  position: absolute;
  top: 120px;
  left: 20px;
  bottom: 180px;
  width: 240px;
  padding: 20px 0;
  box-sizing: border-box;
  background: linear-gradient(
    180deg,
    #0a1d64 0%,
    rgba(21, 30, 73, 0.6984) 100%
  );
  font-size: 14px;
  z-index: 52;
  margin-top: 8px;
  .custom-tree-node {
    display: flex;
    align-items: center;

    img {
      width: 20px;
      height: 22px;
      margin-right: 4px;
      object-fit: contain;
    }

    .node-name {
      display: inline-block;
      max-width: 100px;
      overflow: hidden;
      text-overflow: ellipsis;
      text-wrap: nowrap;
      font-size: 12px;
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

:deep(
    .el-tree--highlight-current
      .el-tree-node.is-current
      > .el-tree-node__content
  ) {
  background-color: rgba(21, 30, 73, 0.6);
}

:deep(.el-tree-node > .el-tree-node__children) {
  margin-left: -6px;
}
</style>
