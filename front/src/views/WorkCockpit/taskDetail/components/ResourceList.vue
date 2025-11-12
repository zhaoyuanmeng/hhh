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
                  <span class="node-name" @click="clickNodeName(data)">{{
                    data.name
                  }}</span>
                </el-tooltip>
                <span v-if="data.children?.length">({{ data.num }})</span>
              </span>
            </template>
          </el-tree>
        </div>
      </el-scrollbar>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, onMounted, nextTick, watch,onUnmounted,onBeforeUnmount } from "vue";
import emitter from "@/utils/emitter";
import {
  getTaskResources,
  getMultiSceneResources,
} from "@/api/workCockpit/index.js";
import { loadPicture } from "@/utils";
import {
  drawResourceDataHide,
  drawResourceDataShowHide,
  drawResourceDataAll,
  saveBasicData
} from "@/components/SmartMap/js/resource";

// store
import useWorkCockpitStore from "@/store/modules/workCockpit.js";
const WorkCockpitStore = useWorkCockpitStore();
import useFloorStore from "@/store/modules/floorStore";
const floorStore = useFloorStore();
let selectTab = computed(() => WorkCockpitStore.tabsOne);
const taskItem = computed(() => WorkCockpitStore.taskItem);
const sceneIds = computed(() => WorkCockpitStore.sceneIds);
const tabsName = computed(() => WorkCockpitStore.tabsName);
let twoPageSource = computed(() => WorkCockpitStore.twoPageSource);
let isfloornumberShow = computed(() => floorStore.floornumberShow);
let openFloors = computed(() => floorStore.openFloor);
const floornum = computed(() => floorStore.floornum); // 当前楼层点击楼层数
let explodebuildname = computed(() => floorStore.explodebuildname); //当前点击的楼栋名称
const props = defineProps({
  curBtn: {
    type: String,
    default: "",
  },
});

const showList = ref(false);
const handleCollapse = () => {
  showList.value = !showList.value;
};
const defaultExpandedKeys = ref([])
const treeData = ref([]);
const treeRef = ref(null);
const checkedNodes = ref([]);
// 点击要素名称
const clickNodeName = (data) => {
  console.log(data)
  let g = window.__g;
  if (data.data.jingdu && data.data.weidu) {
    g.markerLayer.focusByMarkerId('gsgtcity',data.id, 20, 0.5);
  }
  if(data.data.marker){
    if(data.groupId==='police'||data.groupId==='car'||data.groupId==='uav'){
      g.customObject.focus(data.id, 2, 0.5)
    }else{
      g.markerLayer.focusByMarkerId(data.id,data.id, 10, 0.5);
    }
    // g.marker.focus(data.id, 20, 0.5);
  }
  if (data.type === "场景路线" || data.type === "应急路线") {
    g.polyline.focus(data.data.id, 50, 0.5);
  }
  if (data.type === "警戒线") {
    g.polygon3d.focus(data.data.id, 50, 0.5);
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

// 获取资源列表下的所有节点数据
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


// 获取任务详情资源树
const getTaskTreeData = async () => {
  let g = window.__g;
  getTaskResources({ taskId: taskItem.value.id }).then((res) => {
    WorkCockpitStore.set_basicIds(taskItem.value.id,0)
    treeData.value = res.data || [];
    if(res.data?.length){
      defaultExpandedKeys.value = res.data.map(node => node.id)
    }
    checkedNodes.value = getCheckedNodes(res.data);
    if (props.curBtn !== "rcap") {
      showList.value = true;
      let fileterNode = checkedNodes.value.filter((item) => !item.floorNum);
      drawResourceDataAll(fileterNode);
      nextTick(() => {
        const checkIds = fileterNode.map((item) => item.id);
        treeRef.value && treeRef.value.setCheckedKeys(checkIds);
      });
    } else {
      showList.value = false;
      let taskLine = checkedNodes.value.filter(item=>item.type==='场景路线')
      drawResourceDataHide(checkedNodes.value);
      nextTick(() => {
        treeRef.value && treeRef.value.setCheckedNodes(taskLine, true);
      });
    }
  });
};

// 根据多场景ID查询资源树（日程安排和任务部署点击某个场景或住地时调用）
const getTaskTreeDataBySceneIds = async () => {
  let g = window.__g;
  await g.marker.deleteByGroupId("resource");
  getMultiSceneResources({ sceneIds: sceneIds.value.join(",") }).then((res) => {
    WorkCockpitStore.set_basicIds(sceneIds.value.join(","),1)
    treeData.value = res.data || [];
    if(res.data?.length){
      defaultExpandedKeys.value = res.data.map(node => node.id)
    }
    checkedNodes.value = getCheckedNodes(res.data);
    if (tabsName.value === "rcap") {
      showList.value = false;
      let fileterNode = checkedNodes.value.filter((item) => !item.floorNum);
      drawResourceDataHide(fileterNode);
      nextTick(() => {
        const checkIds = fileterNode.map((item) => item.id);
        treeRef.value && treeRef.value.setCheckedKeys([]);
      });
    } else {
      showList.value = true;
      let fileterNode = checkedNodes.value.filter((item) => !item.floorNum);
      drawResourceDataAll(fileterNode);
      nextTick(() => {
        const checkIds = fileterNode.map((item) => item.id);
        treeRef.value && treeRef.value.setCheckedKeys(checkIds);
      });
    }
  });
};

watch(
  () => props.curBtn,
  async () => {
    await getTaskTreeData();
  }
);

watch(
  () => twoPageSource.value,
  async (newVal, oldVal) => {
    if (newVal) {
      await getTaskTreeData();
    }
  }
);

watch(
  () => sceneIds.value,
  async () => {
    if (sceneIds.value?.length) {
      getTaskTreeDataBySceneIds();
    }
  }
);
watch(
  () => floornum.value,
  (nV, oV) => {
    if (nV) {
      let checkdNodes = treeRef.value && treeRef.value.getCheckedNodes();
      let datas = checkdNodes.filter(item=>(item.floorNum&&item.buildName)||item.type==='企业数据'||item.type==='商业数据'||item.type==='社区数据')
      let list = []
      if(datas?.length){
        for(let item of datas){
          if((Number(item.floorNum)===Number(floornum.value)&&item.buildName===explodebuildname.value)||item.type==='企业数据'||item.type==='商业数据'||item.type==='社区数据'){
            list.push(item)
          }
        }
      }
      drawResourceDataShowHide(list, true)
    }
  },
  { deep: true, immediate: false }
);

onMounted(() => {
  if(selectTab.value==='rcap'){
    getTaskTreeData();
  }
  emitter.on("refreshResource", (data) => {
  if(data==='hide'||data==='reset'){
    let g = window.__g
    g.marker.deleteByGroupId('room')
    g.marker3d.deleteByGroupId('room')
  }
});
});
onBeforeUnmount(() => {
  emitter.off("refreshResource");
});
onUnmounted(()=>{
  emitter.off("refreshResource");
})
</script>

<style lang="scss" scoped>
.header-box {
  position: absolute;
  top: 72px;
  left: 20px;
  width: 120px;
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
      max-width: 95px;
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
