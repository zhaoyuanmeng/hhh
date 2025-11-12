<template>
    <div class="scene-dialog">
        <div class="header">
            <p class="title">请选择场景时间：</p>
            <el-icon :size="20" class="close" @click="closeDialog"><Close /></el-icon>
        </div>
        <div class="list">
            <div class="list-item" v-for="item in sceneList" :key="item.sceneId" @click="toSceneDetail(item)">
                <p class="date">{{ item.sceneDate }}</p>
                <p>{{ item.sceneTimeRange }}</p>
            </div>
        </div>
  
    </div>
  
  </template>
  
  <script setup>
  import { Close } from '@element-plus/icons-vue'
  import { ref, onMounted ,computed} from 'vue'
  import { closeFloors } from "@/components/SmartMap/js/utils";
  // store
  import useWorkCockpitStore from '@/store/modules/workCockpit.js'
  import useSettingStore from "@/store/modules/settingStore";
  const WorkCockpitStore = useWorkCockpitStore()
  const sceneList = computed(() => WorkCockpitStore.sceneList)
  const eventMapData = computed(() => WorkCockpitStore.eventMapData)

  const toSceneDetail=(item)=>{
    // closeFloors()
    WorkCockpitStore.setEventMapData({
        ...eventMapData.value,
        GroupID:item.sceneId,
        UserData:item.sceneName
    })
    WorkCockpitStore.setCurPage('sceenDetail')
    useSettingStore().setShowTool(true);
    // WorkCockpitStore.set_showResouce(false)
    // WorkCockpitStore.set_checkBox('init',false)
    WorkCockpitStore.setShowSceneDialog(false)
  }

  const closeDialog=()=>{
    WorkCockpitStore.setShowSceneDialog(false)
  }
  
  onMounted(() => {
 
  })
  </script>
  
  <style lang="scss" scoped>
  .scene-dialog {
    width: 301px;
    height: 200px;
    background: linear-gradient(180deg, rgba(10, 29, 100, 0.7) 0%, rgba(21, 30, 73, 0.6984) 100%);
    padding: 16px;
    box-sizing: border-box;
    position: absolute;
    right: 380px;
    bottom: 150px;
    z-index: 51;

    .header{
        display: flex;
        align-items: center;
        justify-content: space-between;

        .title{
            font-size: 18px;
            font-weight: bold;
        }

        .close{
            cursor: pointer;
        }
    }

    .list-item{
        width: 269px;
        height: 34px;
        background: rgba(0, 0, 0, 0.1);
        box-sizing: border-box;
        border: 1px solid #00CEFF;
        display: flex;
        align-items: center;
        gap: 4px;
        padding-left: 8px;
        margin-top: 16px;
        font-size: 14px;
        cursor: pointer;

        &:hover{
            background: rgba(0, 206, 255, 0.15);
        }

        .date {
            color: #00CEFF;
            font-size: 14px;
            font-weight: bold;
        }
    }
  
 
  }
  </style>