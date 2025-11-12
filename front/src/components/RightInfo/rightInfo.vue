<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-06 18:03:15
 * @LastEditTime: 2024-06-23 01:51:49
 * @LastEditors: Alex
-->
<template>
  <div class="RightHome">
    <div class="left_title_shrink" v-if="!showPanle" @click="toggleLength">
      <div class="importantInfoText">详情信息</div>
    </div>
    <transition name="slide" v-if="showPanle">
      <div class="right_box">
        <div class="shrink" @click="toggleLength1">
          <div class="to_left"></div>
        </div>
        <div class="heard">
          <div class="text_heard">{{ rightInfoData.title }}</div>
          <div class="close_icon" @click="closeRight"></div>

        </div>
        <div class="content">
          <div class="top_box">
            <div class="top_title">详情介绍</div>
            <div class="top_info">
              <div class="text_show">
                {{ rightInfoData.info }}
              </div>
            </div>
          </div>
          <div class="bottom_box">
            <div class="top_title">重点点位（{{ rightInfoData.zddwNum }}）</div>
            <div class="bottom_info">
              <el-tree :data="rightInfoData.point" :props="defaultProps" accordion
                :current-node-key="useMarkerStore().markerId" node-key="id">
                <template #default="{ node, data }">
                  <div v-if="data.children && data.children.length > 0" class="custom-tree-node">
                    <span class="title_name">{{ node.label }}（{{ data.children ? data.children.length : 0 }}）</span>
                  </div>
                  <div v-else-if="node.level === 1" class="custom-tree-node">
                    <span class="title_name" style="padding-left:26px">{{ node.label }}（0）</span>
                  </div>
                  <div v-else class="customNode_box">
                    <el-collapse v-model="useMarkerStore().markerId" accordion @change="handleChange">
                      <el-collapse-item :name="data.id">
                        <template #title>
                          <div class="customCollapse">
                            <div class="name_div">{{
                              data.data.mingcheng || data.data.gaosumingcheng || data.data.pinpaixinghao || data.data.wangdianmingcheng || data.data.title
                            }}</div>
                            <div class="jl_div">
                              <!-- <el-icon class="icon_edit" @click.stop="editItem(data)"><Edit /></el-icon> -->
                              {{ data.data.weizhi }}公里处
                            </div>
                          </div>
                        </template>
                        <div class="detailsInfo">
                          <div v-for="(info, index) in infoList" :key="index" class="infoList_box">
                            <div class="left_label">{{ info.value }}：</div>
                            <div class="right_data">
                              <div class="" v-if="info.value === '图片附件'">
                                <el-image style="width: 100px; height: 100px"
                                  :src="getImagePath(infoObj.data[info.key][0])" :max-scale="70" :min-scale="0.2"
                                  :preview-teleported="true" :preview-src-list="getImagePath1(infoObj.data[info.key])"
                                  fit="cover" v-if="infoObj.data[info.key] && infoObj.data[info.key].length > 0" />
                                <span v-else>暂无图片</span>
                              </div>
                              <div class="" v-else-if="info.value === '视频附件'">
                               <span v-if="isNotEmpty(infoObj.data[info.key])" style="color: aqua;" @click="playVideo(infoObj.data[info.key])">播放视频</span>
                                <span v-else>暂无视频</span>
                              </div>
                              <div v-else>{{ infoObj.data[info.key] }}</div>
                            </div>
                          </div>
                          <!-- <img :src="'/imgs/'+imageName+'.png'" alt="" class="img_icon" v-if="data.data.point"/> -->
                        </div>
                      </el-collapse-item>
                    </el-collapse>
                  </div>
                </template>
              </el-tree>
            </div>
          </div>
        </div>
      </div>
    </transition>

    <!-- 编辑数据弹框 -->
    <EditBasic v-if="editPoint" @hideDialog="hideFun" :pointInfo="markerData" />

    <!-- 视频播放 -->
    <Mp4Play v-if="showMp4" :url="videoSrc"/>
  </div>
</template>

<script setup>
import { onBeforeUnmount, onMounted, onUnmounted, ref, computed, watch, nextTick } from "vue";
import EditBasic from '@/views/componenets/editBasic.vue'
import useSettingStore from "@/store/modules/settingStore";
import useMarkerStore from '@/store/modules/markerStore'
import Mp4Play from '@/components/mp4Play'
import { moveCamera } from '../SmartMap/js/utils'
import gsglConfig from '../SmartMap/config/gsglJson'
import useBasicStore from "@/store/modules/basicData";
import useWorkCockpitStore from '@/store/modules/workCockpit.js'
import { flattenTreeData, getParentTree, getImageForName } from '@/utils'
// 编辑操作数据
const editPoint = ref(false)
const markerData = ref({})
const imageName = ref('出入口')
// 隐藏
const hideFun = () => {
  editPoint.value = false
}
const videoSrc = ref('')
const BasicStore = useBasicStore()
let rightInfoData = computed(() => BasicStore.rightPop)
const SettingStore = useSettingStore();
let showPanle = computed(() => BasicStore.rightPanel)
const defaultProps = ref({
  children: "children",
  label: "name",
});
let showPoint = computed(() => useMarkerStore().markerId);
let showMp4 = computed(()=>useWorkCockpitStore().showMp4)
watch(() => showPoint, val => {
  if (val) {
    if (val.value) {
      SettingStore.setRightCard(true);
      setTimeout(() => {
        handleChange(val.value)
        // expandAndScrollToNode(val.value);
      }, 500)
    }

  }
}, { immediate: true, deep: true });
const infoList = ref([])
const infoObj = ref({})
const expandAndScrollToNode = (nodeId) => {
  const treeRef = tree.value; // 获取 el-tree 组件的引用
  const node = treeRef.getNode(nodeId); // 根据 ID 获取节点
  if (node) {
    // 展开 node 节点
    // 在下一个 tick 中滚动到该节点，确保 DOM 已经更新
    nextTick(() => {
      const targetElement = treeRef.$el.querySelector(`.el-tree-node[data-node-key="${nodeId}"]`);
      if (targetElement) {
        targetElement.scrollIntoView({ behavior: 'smooth' });
      }
    });
  }
}
// 获取图片信息
const getImagePath = (url) => {
  if (url.includes('/imgPath')) {
    return url
  } else {
    return '/imgPath' + url
  }
}
const getImagePath1 = (urlList) => {
  let urls = []
  if(urlList?.length){
    for(const item of urlList){
      if (item.includes('/imgPath')) {
        urls.push(item)
  } else {
    urls.push('/imgPath' + item)
  }
    }
  }
  return urls
}
const playVideo = (url) => {
  let src
  if(Array.isArray(url)){
    if (url[0].includes('/imgPath')) {
    src = url[0]
  } else {
    src = '/imgPath'+url[0]
  }
  }else{
    if (url.includes('/imgPath')) {
    src = url
  } else {
    src = '/imgPath'+url
  }
  }
  videoSrc.value = src
  useWorkCockpitStore().set_showMp4(true)
}
const isNotEmpty = (value) => {
  if (typeof value === 'string') {
    return value.trim() !== ''
  }
  if (Array.isArray(value)) {
    return value.length > 0
  }
  return false
}
const handleChange = (val) => {
  let g = window.__g;
  //  拿到id请求详情数据 可以拿到经纬度就不需要匹配 否则先去匹配
  useMarkerStore().setMarkerId(val)
  let pointData = flattenTreeData(rightInfoData.value.point)
  let fatherId = getParentTree(val, rightInfoData.value.point)
  imageName.value = getImageForName(fatherId[0].name)
  useMarkerStore().setParentId(fatherId[0].id)
  pointData.forEach(item => {
    if (item.id === val) {
      let arrs = Object.entries(item.dataFiledName).map(([key, value]) => ({
        key, value
      }))
      infoList.value = arrs
      infoObj.value = item
      if (rightInfoData.value.title === '会展中心') {
        g.camera.set(491994.287813, 4324266.534219, 121.076094, -25.685902, -90.563744, 0.5)
      } else
        if (rightInfoData.value.title === '市民服务中心') {
          g.camera.set(492075.363281, 4323555.916563, 171.621699, -29.448523, -96.42614, 0.5)
        } else {
          g.marker.focus(val, 150, 0.1);
          g.markerLayer.focusByMarkerId('basicDataLayer',val,150,1)
        }
    }
  });

}
const closeRight = () => {
  let g = window.__g
  g.marker.deleteByGroupId('gs')
  g.marker.deleteByGroupId('gt')
  g.marker.deleteByGroupId('xc')
  g.marker.deleteByGroupId('zd')
  g.marker.deleteByGroupId('city')
  g.marker.deleteByGroupId('basic')
  g.markerLayer.delete('basicDataLayer')
  //初始化、不传参等同于1;1：清除所有接口添加的对象 2：重置用户设置 4：复位相机到初始位置
  g.reset(4);
  SettingStore.setRightCard();
  BasicStore.setLineId('')
  SettingStore.setLeftInfo('')
  g.infoTree.hide(['6987FC234EC3F28BD8AFC2984E7669E4', '0779A2DA4565A9E1E62816BE9DE982C4'])// 隐藏高铁、高速
}
// 收缩右侧面板
const toggleLength = () => {
  BasicStore.setRightPanel(true)
}
const toggleLength1 = () => {
  BasicStore.setRightPanel(false)
}

// 编辑操作
const editItem = (data) => {
  editPoint.value = true
  nextTick(() => {
    markerData.value = { ...data }
  })
}
</script>

<style lang="scss" scoped>
.RightHome {
  .shrink {
    position: absolute;
    // left: 430px;
    left: -20px;
    top: 50%;
    z-index: 10;
    width: 10px;
    height: 20px;
    border: 1px solid cyan;
    border-right: none;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    cursor: pointer;
    transform: translate(0, -50%);

    .to_left {
      width: 0;
      height: 0;
      border-top: 7px solid transparent;
      border-bottom: 7px solid transparent;
      border-left: 6px solid cyan;
      animation: flow 0.8s infinite alternate;
    }
  }

  .left_title_shrink {
    cursor: pointer;
    position: absolute;
    width: 30px;
    height: 120px;
    top: 50%;
    right: 10px;
    transform: translate(0, -50%);
    z-index: 5;
    background: rgba(4, 35, 84, 1);
    border-radius: 8px;
    color: #ffffff;
    text-align: center;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  @keyframes flow {
    from {
      margin-right: -1px;
    }

    to {
      margin-right: -16px;
    }
  }

  /* 使用 @keyframes 定义过渡效果 */
  @keyframes slide {
    0% {
      width: 0px;
    }

    /* 打开时宽度从0开始 */
    100% {
      width: 400px;
    }

    /* 打开时宽度变为400 */
  }

  @keyframes slideReverse {
    0% {
      width: 400px;
    }

    /* 关闭时宽度从400开始 */
    100% {
      width: 0px;
    }

    /* 关闭时宽度变为0 */
  }

  .slide-enter-active,
  .slide-leave-active {
    animation: slide 0.1s forwards;
    /* 应用定义的动画 */
  }

  .slide-leave-active {
    animation-direction: reverse;
    /* 设置动画反向播放 */
  }

  .right_box {
    position: absolute;
    transform: scale(0.8);
    z-index: 8;
    // right: 20px;
    right: -2%;
    bottom: -20px;
    top: 0px;
    width: 400px;
    background: linear-gradient(180deg, #0a1d64 0%, rgba(21, 30, 73, 0.7) 100%);
    display: flex;
    flex-direction: column;
    transition: width 0.1s;

    /* 添加过渡效果 */
    .heard {
      position: relative;
      background: url("../../assets/panel/panel_bg.png") no-repeat;
      background-size: 100% 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-family: PingFangSC;
      font-weight: 400;
      font-size: 14px;
      color: #ffffff;
      text-shadow: 0px 0px 12px rgba(0, 106, 255, 0.8);
      font-style: normal;

      .close_icon {
        position: absolute;
        background: url("../../assets/panel/close.png") no-repeat;
        background-size: 100% 100%;
        width: 26px;
        height: 26px;
        top: 50%;
        right: 8px;
        transform: translate(0, -50%);
        cursor: pointer;
      }

      .text_heard {
        height: 48px;
        line-height: 48px;
        font-family: YouSheBiaoTiHei;
        font-size: 24px;
      }

      //     .shrink {
      //   position: absolute;
      //   left: 10px;
      //   top: 50%;
      //   z-index: 10;
      //   width: 15px;
      //   height: 20px;
      //   border: 1px solid cyan;
      //   border-right: none;
      //   display: flex;
      //   align-items: center;
      //   justify-content: flex-end;
      //   cursor: pointer;
      //   transform: translate(0, -50%);
      //   .to_left {
      //     width: 0;
      //     height: 0;
      //     border-top: 10px solid transparent;
      //     border-bottom: 10px solid transparent;
      //     border-left: 8px solid cyan;
      //     animation: flow 0.8s infinite alternate;
      //   }
      // }
    }

    .content {
      flex: 1;
      height: calc(100% - 40px);
      padding: 15px 20px;
      display: flex;
      flex-direction: column;

      .top_title {
        height: 28px;
        width: 100%;
        display: flex;
        align-items: center;
        background: url("../../assets/panel/right_panel.png") no-repeat;
        background-size: 100% 100%;
        font-family: YouSheBiaoTiHei;
        font-size: 17px;
        color: #ffffff;
        padding-left: 24px;
        font-style: normal;
        vertical-align: top;
      }

      .top_box {
        .top_info {
          height: 107px;
          font-family: PingFangSC;
          font-weight: 400;
          font-size: 14px;
          color: #ffffff;
          text-align: left;
          font-style: normal;

          .text_show {
            line-height: 30px;
          }
        }
      }

      .bottom_box {
        flex: 1;
        display: flex;
        flex-direction: column;

        .bottom_info {
          flex: 1;
          padding: 10px 0;
          overflow: auto;
          position: relative;

          :deep(.el-tree) {
            background: none;
            position: absolute;
            top: 10px;
            left: 0;
            right: 0;
            bottom: 10px;
          }

          :deep(.el-tree-node__content) {
            height: auto;
            background: none;
            padding-bottom: 8px;
          }

          :deep(.custom-tree-node .title_name) {
            font-family: PingFangSC;
            font-weight: 500;
            font-size: 14px;
            color: #ffffff;
            font-style: normal;
          }

          :deep(.el-tree-node__expand-icon) {
            color: #fff;
            font-size: 14px;
          }

          :deep(.el-tree-node__content .is-leaf) {
            display: none;
          }

          .customNode_box {
            background: rgba(82, 106, 135, 0.5);
            border-radius: 8px;
            width: 100%;

            :deep(.el-collapse) {
              border: none;
            }

            :deep(.el-collapse-item__header) {
              background: none;
              border: none;
              height: 42px;
              font-family: PingFangSC;
              font-weight: 400;
              font-size: 14px;
              color: #ffffff;
              font-style: normal;

              .customCollapse {
                overflow: hidden !important;
                white-space: nowrap !important;
                text-overflow: ellipsis !important;
              }
            }

            :deep(.el-collapse-item__wrap) {
              background: none;
              border: none;
            }

            :deep(.el-collapse-item__content) {
              padding-bottom: 8px;
            }

            :deep(.el-button:focus) {
              outline: none;
            }

            :deep(.el-collapse) {
              --el-collapse-border-color: transparent !important;
            }

            :deep(.el-collapse .el-collapse-item__header:focus,
              .el-collapse .el-collapse-item__header:hover) {
              outline: none;
              box-shadow: none;
            }

            .customCollapse {
              width: 100%;
              display: flex;
              align-items: center;
              height: 42px;

              div {
                // flex: 1;
                height: 42px;
              }

              .name_div {
                text-align: left;
                padding-left: 8px;
                width: 60%;
              }

              .jl_div {
                flex: 1;
                padding-right: 8px;
                display: flex;
                align-items: center;
                justify-content: end;

                .icon_edit {
                  font-size: 20px;
                  margin-right: 8px;
                }
              }
            }

            .detailsInfo {
              position: relative;
              font-family: PingFangSC;
              font-weight: 400;
              font-size: 12px;
              color: #ffffff;
              line-height: 24px;
              text-align: left;
              font-style: normal;
              white-space: normal;
              padding-left: 16px;

              .img_icon {
                width: 50px;
                height: 60px;
                position: absolute;
                right: 16px;
                bottom: 16px;
              }
            }

            .infoList_box {
              display: flex;
              opacity: 0.6;
              font-family: PingFangSC;
              font-weight: 400;
              font-size: 12px;
              color: #FFFFFF;
              text-align: left;
              font-style: normal;

              .left_label {}

              .right_data {}
            }
          }
        }
      }
    }
  }
}</style>
