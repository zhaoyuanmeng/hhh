<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-06 18:03:15
 * @LastEditTime: 2024-06-22 21:49:00
 * @LastEditors: Alex
-->
<template>
  <div class="indexHome">
    <div class="left_title_shrink" v-if="!showPanle" @click="toggleLength">
      <div class="importantInfoText">重点信息</div>
    </div>

    <transition name="slide">
      <div class="left_box" v-if="showPanle">
        <div :class="showPanle ? 'shrink' : ''" @click="toggleLength">
          <div :class="showPanle ? 'to_left' : ''"></div>
        </div>
        <div class="heard">
          重点信息
          <el-icon class="change_icon" title="展示切换" @click="changeSwitch"
            ><Switch
          /></el-icon>
        </div>
        <div class="content" v-if="switchVal">
          <div class="importantPoint">
            <div class="title_name">
              <div class="left_title">高速公路档案</div>
              <div
                class="right_ctrl"
                @click.stop="archiverTableList('高速公路档案')"
              >
                <div class="list_gs_gt_xc_zd" title="列表查询"></div>
              </div>
            </div>
            <div class="data_box">
              <div class="top_nav">
                <div
                  class="nav_item nav_item1"
                  @click="openInfoCard('0', 'gs')"
                >
                  <div class="icon_gsgl"></div>
                  <div class="gsgl_text">
                    {{ leftBox.gs.num }}<span class="gsgl_span">条</span>
                  </div>
                </div>
                <div class="nav_item">
                  <div class="top_text">
                    {{ leftBox.gs.length }}<span class="gsgl_span">公里</span>
                  </div>
                  <div class="center_icon"></div>
                  <div class="bottom_name">全长</div>
                </div>
                <div class="nav_item">
                  <div class="top_text">
                    {{ leftBox.gs.pointNum }}<span class="gsgl_span">个</span>
                  </div>
                  <div class="center_icon1"></div>
                  <div class="bottom_name">重点点位</div>
                </div>
              </div>
              <div class="data_details" v-if="showGL">
                <div class="data_list">
                  <div
                    class="data_list_item"
                    :class="{ active: item.id === styleCotrl }"
                    v-for="(item, index) in addClassName(leftBox.gsList)"
                    :key="index"
                    @click="addStyleAndAction('0', item)"
                  >
                    <div class="left_index">{{ index + 1 }}</div>
                    <div class="data_name">{{ item.data.title }}</div>
                    <div class="length_name">
                      <div class="l_data">
                        {{ item.data.TotalLengthOfMileage }}km
                      </div>
                      <div class="l_name">长度</div>
                    </div>
                    <div class="length_name">
                      <div class="l_data">{{ item.pointNum }}个</div>
                      <div class="l_name">重点点位</div>
                    </div>
                  </div>
                </div>
                <div class="collsp" @click="openInfoCard('0')">
                  <div class="div_text">详情</div>
                  <el-icon><ArrowUp /></el-icon>
                </div>
              </div>
            </div>
          </div>
          <div class="importantPoint">
            <div class="title_name">
              <div class="left_title">高速铁路档案</div>
              <div class="right_ctrl">
                <div
                  class="list_gs_gt_xc_zd"
                  title="列表查询"
                  @click.stop="archiverTableList('高速铁路档案')"
                ></div>
              </div>
            </div>
            <div class="data_box">
              <div class="top_nav">
                <div
                  class="nav_item nav_item1"
                  @click="openInfoCard('1', 'gt')"
                >
                  <div class="icon_gstl"></div>
                  <div class="gsgl_text">
                    {{ leftBox.gt.num }}<span class="gsgl_span">条</span>
                  </div>
                </div>
                <div class="nav_item">
                  <div class="top_text">
                    {{ leftBox.gt.length }}<span class="gsgl_span">公里</span>
                  </div>
                  <div class="center_icon"></div>
                  <div class="bottom_name">全长</div>
                </div>
                <div class="nav_item">
                  <div class="top_text">
                    {{ leftBox.gt.pointNum }}<span class="gsgl_span">个</span>
                  </div>
                  <div class="center_icon1"></div>
                  <div class="bottom_name">重点点位</div>
                </div>
              </div>
              <div class="data_details" v-if="showTL">
                <div class="data_list">
                  <div
                    class="data_list_item"
                    :class="{ active: item.id === styleCotrl }"
                    v-for="(item, index) in addClassName(leftBox.gtList)"
                    :key="index"
                    @click="addStyleAndAction('1', item)"
                  >
                    <div class="left_index">{{ index + 1 }}</div>
                    <div class="data_name">{{ item.data.title }}</div>
                    <div class="length_name">
                      <div class="l_data">
                        {{ item.data.TotalLengthOfMileage }}km
                      </div>
                      <div class="l_name">长度</div>
                    </div>
                    <div class="length_name">
                      <div class="l_data">{{ item.pointNum }}个</div>
                      <div class="l_name">重点点位</div>
                    </div>
                  </div>
                </div>
                <div class="collsp" @click="openInfoCard('1')">
                  <div class="div_text">详情</div>
                  <el-icon><ArrowUp /></el-icon>
                </div>
              </div>
            </div>
          </div>
          <div class="importantPoint">
            <div class="title_name">
              <div class="left_title">现场档案</div>
              <div class="right_ctrl">
                <div
                  class="list_gs_gt_xc_zd"
                  title="列表查询"
                  @click.stop="archiverTableList('现场档案')"
                ></div>
              </div>
            </div>
            <div class="data_box">
              <div class="top_nav">
                <div
                  class="nav_item nav_item1"
                  @click="openInfoCard('2', 'xc')"
                >
                  <div class="icon_xcda"></div>
                  <div class="gsgl_text">
                    {{ leftBox.xc.num }}<span class="gsgl_span">个</span>
                  </div>
                </div>
                <div class="nav_item">
                  <div class="top_text">
                    {{ leftBox.xc.area }}<span class="gsgl_span">㎡</span>
                  </div>
                  <div class="center_icon"></div>
                  <div class="bottom_name">总面积</div>
                </div>
                <div class="nav_item">
                  <div class="top_text">
                    {{ leftBox.xc.pointNum }}<span class="gsgl_span">个</span>
                  </div>
                  <div class="center_icon1"></div>
                  <div class="bottom_name">重点点位</div>
                </div>
              </div>
              <div class="data_details" v-if="showDA">
                <div class="data_list">
                  <div
                    class="data_list_item"
                    :class="{ active: item.id === styleCotrl }"
                    v-for="(item, index) in addClassName(leftBox.xcList)"
                    :key="index"
                    @click="addStyleAndAction('2', item)"
                  >
                    <div class="left_index">{{ index + 1 }}</div>
                    <div class="data_name">{{ item.data.title }}</div>
                    <div class="length_name">
                      <div class="l_data">{{ item.data.zhandimianji }}</div>
                      <div class="l_name">面积</div>
                    </div>
                    <div class="length_name">
                      <div class="l_data">{{ item.pointNum }}个</div>
                      <div class="l_name">重点点位</div>
                    </div>
                  </div>
                </div>
                <div class="collsp" @click="openInfoCard('2')">
                  <div class="div_text">详情</div>
                  <el-icon><ArrowUp /></el-icon>
                </div>
              </div>
            </div>
          </div>

          <div class="importantPoint">
            <div class="title_name">
              <div class="left_title">住地档案</div>
              <div class="right_ctrl">
                <div
                  class="list_gs_gt_xc_zd"
                  title="列表查询"
                  @click.stop="archiverTableList('住地档案')"
                ></div>
              </div>
            </div>
            <div class="data_box">
              <div class="top_nav">
                <div
                  class="nav_item nav_item1"
                  @click="openInfoCard('3', 'zd')"
                >
                  <div class="icon_xcda"></div>
                  <div class="gsgl_text">
                    {{ leftBox.zd.num }}<span class="gsgl_span">个</span>
                  </div>
                </div>
                <div class="nav_item">
                  <div class="top_text">
                    {{ leftBox.zd.area }}<span class="gsgl_span">㎡</span>
                  </div>
                  <div class="center_icon"></div>
                  <div class="bottom_name">总面积</div>
                </div>
                <div class="nav_item">
                  <div class="top_text">
                    {{ leftBox.zd.pointNum }}<span class="gsgl_span">个</span>
                  </div>
                  <div class="center_icon1"></div>
                  <div class="bottom_name">重点点位</div>
                </div>
              </div>
              <div class="data_details" v-if="showZD">
                <div class="data_list">
                  <div
                    class="data_list_item"
                    :class="{ active: item.id === styleCotrl }"
                    v-for="(item, index) in addClassName(leftBox.zdList)"
                    :key="index"
                    @click="addStyleAndAction('3', item)"
                  >
                    <div class="left_index">{{ index + 1 }}</div>
                    <div class="data_name">{{ item.data.title }}</div>
                    <div class="length_name">
                      <div class="l_data">{{ item.data.zhandimianji }}</div>
                      <div class="l_name">面积</div>
                    </div>
                    <div class="length_name">
                      <div class="l_data">{{ item.pointNum }}个</div>
                      <div class="l_name">重点点位</div>
                    </div>
                  </div>
                </div>
                <div class="collsp" @click="openInfoCard('3')">
                  <div class="div_text">详情</div>
                  <el-icon><ArrowUp /></el-icon>
                </div>
              </div>
            </div>
          </div>

          <!-- 城市道路档案 -->
          <div class="importantPoint">
            <div class="title_name">
              <div class="left_title">城市道路档案</div>
              <div class="right_ctrl">
                <div
                  class="list_gs_gt_xc_zd"
                  title="列表查询"
                  @click.stop="archiverTableList('城市道路档案')"
                ></div>
              </div>
            </div>
            <div class="data_box">
              <div class="top_nav">
                <div
                  class="nav_item nav_item1"
                  @click="openInfoCard('4', 'city')"
                >
                  <div class="icon_gsgl"></div>
                  <div class="gsgl_text">
                    {{ leftBox.city.num }}<span class="gsgl_span">条</span>
                  </div>
                </div>
                <div class="nav_item">
                  <div class="top_text">
                    {{ leftBox.city.length }}<span class="gsgl_span">公里</span>
                  </div>
                  <div class="center_icon"></div>
                  <div class="bottom_name">全长</div>
                </div>
                <div class="nav_item">
                  <div class="top_text">
                    {{ leftBox.city.pointNum }}<span class="gsgl_span">个</span>
                  </div>
                  <div class="center_icon1"></div>
                  <div class="bottom_name">重点点位</div>
                </div>
              </div>
              <div class="data_details" v-if="showCity">
                <div class="data_list">
                  <div
                    class="data_list_item"
                    :class="{ active: item.id === styleCotrl }"
                    v-for="(item, index) in addClassName(leftBox.cityList)"
                    :key="index"
                    @click="addStyleAndAction('4', item)"
                  >
                    <div class="left_index">{{ index + 1 }}</div>
                    <div class="data_name">{{ item.data.title }}</div>
                    <div class="length_name">
                      <div class="l_data">
                        {{ item.data.TotalLengthOfMileage }}km
                      </div>
                      <div class="l_name">长度</div>
                    </div>
                    <div class="length_name">
                      <div class="l_data">{{ item.pointNum }}个</div>
                      <div class="l_name">重点点位</div>
                    </div>
                  </div>
                </div>
                <div class="collsp" @click="openInfoCard('4')">
                  <div class="div_text">详情</div>
                  <el-icon><ArrowUp /></el-icon>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="content" v-if="!switchVal">
          <div class="importantPoint" v-for="(list, i) in newData" :key="i">
            <div class="title_name">
              <div class="left_title">{{ list.name }}</div>
              <div class="right_ctrl">
                <el-icon style="font-size:20px;cursor: pointer;" title="获取经纬度" @click="getLocation"><Location /></el-icon>
                <div
                  class="upload_gs_gt_xc_zd"
                  title="上传"
                  @click="uploadData('1')"
                  v-if="list.name !== '城市道路档案'"
                ></div>
                <div
                  class="add_gs_gt_xc_zd"
                  :style="{marginLeft:list.name === '城市道路档案'?'20px':'0'}"
                  title="添加"
                  @click="addData(list, {}, 'add')"
                ></div>
              </div>
            </div>
            <div
              class="data_box"
              style="padding: 0 20px 20px 20px; display: flex; flex-wrap: wrap"
            >
              <div
                v-for="(item, index) in list.list"
                :key="index"
                class="new_data_item"
                @click.stop="openCustomDialog(list, item)"
              >
                {{ item.title }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </transition>
    <!-- 右侧面板详情信息 -->
    <RightInfo ref="rightInfoRef" v-if="rightCard"></RightInfo>

    <!-- 新增数据弹框 -->
    <AddBasic v-if="addBasicBol" @hideDialog="getaddBasicBol" />
    <!-- 上传数据 -->
    <uploadBasic v-if="uploadBol" @hideDialog="uploadFun" />

    <!-- marker信息 -->
    <InfoDialog v-if="showMarkerInfo" :showPosition="false" />

    <!-- 数据弹框交互弹框 -->
    <CustomDialogBasic
      v-if="customDialog"
      @hideDialog="closeCustom"
      @editData="editDataFun"
    />

    <!-- 列表查询弹框 -->
    <ListTableQuery v-if="listDialog" @hideDialog="tableListFun"/>
  </div>
  <VectorRoadMap :styles="style"/>
</template>

<script setup>
import VectorRoadMap from "@/components/vectorRoadMap"
import RightInfo from "@/components/RightInfo";
import InfoDialog from "@/views/WorkCockpit/taskDetail/components/InfoDialog.vue";
import useWorkCockpitStore from "@/store/modules/workCockpit.js";
import {
  onBeforeUnmount,
  onMounted,
  onUnmounted,
  ref,
  computed,
  reactive,
  watch,
  nextTick,
} from "vue";
import { ElMessageBox,ElMessage } from "element-plus";
import emitter from "@/utils/emitter";
import useSettingStore from "@/store/modules/settingStore";
import useMarkerStore from "@/store/modules/markerStore";
import useBasicStore from "@/store/modules/basicData";
import gsglConfig from "../components/SmartMap/config/gsglJson";
import gstlConfig from "../components/SmartMap/config/gstlJson";
import xcdaConfig from "../components/SmartMap/config/xcdaJson";
import { addMarkerPoint } from "../components/SmartMap/js/addMarkers";
import AddBasic from "./componenets/addBasic.vue";
import uploadBasic from "./componenets/uploadBasic.vue";
import CustomDialogBasic from "./componenets/customDialogBasic.vue";
import ListTableQuery from "./componenets/archiverTable/index.vue";
const showMarkerInfo = computed(() => useWorkCockpitStore().showMarkerInfo);
import {
  getPointType,
  getDataStatistics,
  quertListForTypeId,
  getPointInfo,
} from "@/api/basic/index";
import { GetArchivesList } from "@/api/index";
import { loadPicture } from "@/utils";
const SettingStore = useSettingStore();
const BasicStore = useBasicStore();
const rightInfoRef = ref(null);
const addBasicBol = ref(false);
const uploadBol = ref(false);
const customDialog = ref(false);
const listDialog = ref(false);
const basicType = ref("");
const newData = ref([]);
let showPanle = computed(() => SettingStore.showPanle);
let showGL = computed(() => SettingStore.gsglInfo);
let showTL = computed(() => SettingStore.gstlInfo);
let showDA = computed(() => SettingStore.xcdaInfo);
let showZD = computed(() => SettingStore.zddaInfo);
let showCity = computed(() => SettingStore.cityInfo);
let rightCard = computed(() => SettingStore.rightCard);
let styleCotrl = computed(() => BasicStore.lineId);
const switchVal = ref(true);
const style = ref({
  bottom:'20px',
  right:'20%'
})
// 高速公路点位信息
const gsglvalue = ref(false);
// 高速铁路点位信息
const gstlvalue = ref(false);
// 现场档案点位信息
const xcdavalue = ref(false);
// 高速公路数据
// const gsglAllData = gsglConfig.gsglData.list?gsglConfig.gsglData.list
const gsglData = reactive(gsglConfig.gsglData);
// 高度铁路数据
const gstlData = reactive(gstlConfig.gstlData);
// 现场档案数据
const xcdaData = reactive(xcdaConfig.xcdaData);
// 对接接口数据
const leftBox = reactive({
  gs: {},
  gt: {},
  xc: {},
  zd: {},
  city: {},
  gsList: [],
  gtList: [],
  xcList: [],
  zdList: [],
  cityList: [],
});
// 获取经纬度
const getLocation = () => {
  ElMessage({
    duration:5000,
    message:'请点击地图获取经纬度',
    type: 'success',
  })
  BasicStore.set_locationBol(true)
}
// 新增数据
const addData = (row, targetRow, type) => {
  //type=0?高速公路=1？高速铁路=2？现场档案
  addBasicBol.value = true;
  nextTick(() => {
    emitter.emit("eventBasicinfoDialog", {
      data: row,
      targetRow: targetRow,
      show: true,
      type: type,
    });
  });
  console.log(row, targetRow, type);

  // addBasicBol.value = true;
  // basicType.value = type;
  // BasicStore.SET_TYPE(type);
};
// 编辑数据
const editDataFun = (e) => {
  console.log(e);
  addBasicBol.value = true;
  nextTick(() => {
    emitter.emit("eventBasicinfoDialog", e);
  });
};
const openCustomDialog = (basicInfo, listItem) => {
  BasicStore.SET_ACTIVE_BASICINFO_LIST(basicInfo.list);
  // this.$eventBus.$emit("eventKeypointDialog", {
  //   basicInfo,
  //   data: listItem,
  //   show: true,
  // });
  customDialog.value = true;
  nextTick(() => {
    emitter.emit("eventKeypointDialog", {
      basicInfo,
      data: listItem,
      show: true,
    });
  });
};
// 列表查询
const archiverTableList = (type) => {
  console.log(type);
  GetArchivesList({ parent_id: 0 }).then((res) => {
    if (res.code === 0) {
      let responseData = res.data;
      responseData.map((item) => {
        item.isExpand = false;
        item.lcqc = formatFloatNum(item.lcqc);
        if (item.name == "现场档案" || item.name == "住地档案") {
          item.isNotHaveKeypoint = true;
        } else {
          item.isNotHaveKeypoint = false;
        }
      });
      let listData = responseData;
      let archivers = listData.map((val) => {
        return { id: val.id, name: val.name };
      });
      let selectId
        for(const item of archivers){
          if(item.name===type){
            selectId = item.id
          }
        }
      listDialog.value = true;
      let data = { archiver: archivers, id: selectId };
      nextTick(() => {
        emitter.emit("eventTableinfoDialog", data);
      });
    }
  });

  // this.$router.push({path:"/archiverTable",query:{archiver:JSON.stringify(archiver),id:item.id}});
};
const tableListFun = () => {
  listDialog.value = false
}
// 打点后的数据
const getaddBasicBol = () => {
  addBasicBol.value = false;
  gitBasicListData();
};
// 上传数据
const uploadData = (type) => {
  uploadBol.value = true;
  BasicStore.SET_TYPE(type);
};
const showMarPoint = (t) => {
  if (t === 0) {
    if (gsglvalue.value) {
      if (gsglData.list && gsglData.list.length > 0) {
        gsglData.list.forEach((item) => {
          let obj = {
            title: item.title,
            info: item.info,
            zddwNum: item.zddwgs,
            point: gsglConfig[item.id],
          };
          addMarkerPoint(obj);
        });
      }
    } else {
      let g = window.__g;
      g.marker.deleteByGroupId("gsgl");
    }
  } else if (t === 1) {
    if (gstlvalue.value) {
      if (gstlData.list && gstlData.list.length > 0) {
        gstlData.list.forEach((item) => {
          let obj = {
            title: item.title,
            info: item.info,
            zddwNum: item.zddwgs,
            point: gstlConfig[item.id],
          };
          addMarkerPoint(obj);
        });
      }
    } else {
      let g = window.__g;
      g.marker.deleteByGroupId("gstl");
    }
  } else {
    if (xcdavalue.value) {
      if (xcdaData.list && xcdaData.list.length > 0) {
        xcdaData.list.forEach((item) => {
          let obj = {
            title: item.title,
            info: item.info,
            zddwNum: item.zddwgs,
            point: xcdaConfig[item.id],
          };
          addMarkerPoint(obj);
        });
      }
    } else {
      let g = window.__g;
      g.marker.deleteByGroupId("xcda");
    }
  }
};
// 添加class类方法
const addClassName = (list) => {
  list.map((item) => {
    if (!item.class) {
      item.class = "";
    }
  });
  return list;
};
// 加载成功后的回调
const loadAfterFun = () => {
  console.log("地图加载完后加载点位");
  // SettingStore.setShowPanle();
  // 加载地图点位
};
const getLeftBoxDta = () => {
  //高速
  getDataStatistics("gs").then((res) => {
    leftBox.gs = res.data;
  });
  //高铁
  getDataStatistics("gt").then((res) => {
    leftBox.gt = res.data;
  });
  // 现场
  getDataStatistics("xc").then((res) => {
    leftBox.xc = res.data;
  });
  //住地
  getDataStatistics("zd").then((res) => {
    leftBox.zd = res.data;
  });
  // 城市道路
  getDataStatistics("city").then((res) => {
    leftBox.city = res.data;
  });
};
onMounted(() => {
  // 获取左侧面板统计数据
  getLeftBoxDta();
  if (!showPanle.value) {
    SettingStore.setShowPanle(true);
  }
  if (rightCard.value) {
    SettingStore.setRightCard();
  }
  SettingStore.setLeftInfo("");
  BasicStore.setLineId("");
});
const toggleLength = () => {
  SettingStore.setShowPanle();
};
// 点击打开详情
const openInfoCard = (type, e) => {
  if (e) {
    quertListForTypeId(e).then((res) => {
      if (e === "gs") {
        leftBox.gsList = res.data;
      }
      if (e === "gt") {
        leftBox.gtList = res.data;
      }
      if (e === "xc") {
        leftBox.xcList = res.data;
      }
      if (e === "zd") {
        leftBox.zdList = res.data;
      }
      if (e === "city") {
        leftBox.cityList = res.data;
      }
    });
  }
  SettingStore.setLeftInfo(type);
  // 清空选择的样式
};
// 点击详情添加样式并且定位到该点
const addStyleAndAction = async (type, item) => {
  let e =
    type === "0"
      ? "gs"
      : type === "1"
      ? "gt"
      : type === "2"
      ? "xc"
      : type === "3"
      ? "zd"
      : "city";
  let params = { id: item.id };
  let g = window.__g;
  await g.reset(4);
  getPointInfo(e, params).then((res) => {
    if (res.code === 0) {
      // console.log(JSON.parse(res.data.data.point))
      if (type === "2" || type === "3") {
        // 画住地、现场点位
      }
      useMarkerStore().setMarkerId("");
      BasicStore.setLineId(item.id);
      SettingStore.setRightCard(true);
      let g = window.__g;
      g.infoTree.show(item.id);
      let pointsList = res.data.pointList.map((item, index) => {
        let obj = {
          num: item.num,
          id: `${e}_${index}`,
          name: item.name,
          children: item.data,
        };
        return obj;
      });
      let obj = {
        title: res.data.data.title,
        info: "暂无介绍",
        type: e,
        zddwNum: res.data.pointNum,
        point: pointsList,
        xczdPoint:
          type === "2" || type === "3"||type === "4"
            ? {
                jd: Number(res.data.data.jingdu),
                wd: Number(res.data.data.weidu),
              }
            : null,
      };
      addMarkerPoint(obj, res.data.data.title);
      // if(res.data.data.jingdu&&res.data.data.weidu){
        BasicStore.setRightPanel(true);
        BasicStore.setRightPop(obj);
      // }
    }
  });

  //高速公路点击
  // if (type === "0") {
  //   gsglData.list.forEach((i) => {
  //     if (i.id === item.id) {
  //       i.class = "active";
  //     } else {
  //       i.class = "";
  //     }
  //   });
  //   gstlData.list.forEach((i) => {
  //     i.class = "";
  //   });
  //   xcdaData.list.forEach((i) => {
  //     i.class = "";
  //   });
  // } else if (type === "1") {
  //   // 高速铁路点击
  //   gstlData.list.forEach((i) => {
  //     if (i.id === item.id) {
  //       i.class = "active";
  //     } else {
  //       i.class = "";
  //     }
  //   });
  //   gsglData.list.forEach((i) => {
  //     i.class = "";
  //   });
  //   xcdaData.list.forEach((i) => {
  //     i.class = "";
  //   });
  // } else if (type === "2") {
  //   // 现场档案点击
  //   xcdaData.list.forEach((i) => {
  //     if (i.id === item.id) {
  //       i.class = "active";
  //     } else {
  //       i.class = "";
  //     }
  //   });
  //   gstlData.list.forEach((i) => {
  //     i.class = "";
  //   });
  //   gsglData.list.forEach((i) => {
  //     i.class = "";
  //   });
  // }
};

const gitBasicListData = () => {
  GetArchivesList({ parent_id: 0 }).then((res) => {
    if (res.code === 0) {
      let responseData = res.data;
      responseData.map((item) => {
        item.isExpand = false;
        item.lcqc = formatFloatNum(item.lcqc);
        if (item.name == "现场档案" || item.name == "住地档案") {
          item.isNotHaveKeypoint = true;
        } else {
          item.isNotHaveKeypoint = false;
        }
      });
      newData.value = responseData;
    }
  });
};
// 新防范
const changeSwitch = () => {
  switchVal.value = !switchVal.value;
  SettingStore.setLeftInfo('8');
  // 获取左侧面板统计数据
  getLeftBoxDta();
  gitBasicListData();
};
const uploadFun = (e) => {
  uploadBol.value = false;
  if (e) {
    // console.log("更新数据");
  gitBasicListData();
  }
};
const formatFloatNum = (data) => {
  let num = data.toString();
  let floatArr = num.split(".");
  if (floatArr.length > 1) {
    let len = floatArr[1].length;
    if (len > 2) {
      return Number(num).toFixed(2);
    } else {
      return num;
    }
  } else {
    return num;
  }
};
const closeCustom = () => {
  customDialog.value = false;
  GetArchivesList({ parent_id: 0 }).then((res) => {
    if (res.code === 0) {
      let responseData = res.data;
      responseData.map((item) => {
        item.isExpand = false;
        item.lcqc = formatFloatNum(item.lcqc);
        if (item.name == "现场档案" || item.name == "住地档案") {
          item.isNotHaveKeypoint = true;
        } else {
          item.isNotHaveKeypoint = false;
        }
      });
      newData.value = responseData;
    }
  });
};
</script>

<style lang="scss" scoped>
.indexHome {
  .left_title_shrink {
    cursor: pointer;
    position: absolute;
    width: 30px;
    height: 120px;
    top: 50%;
    left: 10px;
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
  .shrink {
    position: absolute;
    // left: 430px;
    right: -20px;
    top: 50%;
    z-index: 10;
    width: 10px;
    height: 20px;
    border: 1px solid cyan;
    border-left: none;
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
      border-right: 6px solid cyan;
      animation: flow 0.8s infinite alternate;
    }
  }
  .shrink_hide {
    position: absolute;
    left: 40px;
    top: 50%;
    z-index: 10;
    width: 10px;
    height: 20px;
    border: 1px solid cyan;
    border-right: none;
    display: flex;
    align-items: center;
    justify-content: flex-start;
    cursor: pointer;
    transform: translate(0, -50%);
    .to_left_hide {
      width: 0;
      height: 0;
      border-top: 7px solid transparent;
      border-bottom: 7px solid transparent;
      border-left: 6px solid cyan;
      animation: flowHide 0.8s infinite alternate;
    }
  }

  @keyframes flow {
    from {
      margin-right: 1px;
    }
    to {
      margin-right: 16px;
    }
  }
  @keyframes flowHide {
    from {
      margin-left: 1px;
    }
    to {
      margin-left: 16px;
    }
  }
  /* 使用 @keyframes 定义过渡效果 */
  @keyframes slide {
    0% {
      width: 0px;
    } /* 打开时宽度从0开始 */
    100% {
      width: 400px;
    } /* 打开时宽度变为400 */
  }
  @keyframes slideReverse {
    0% {
      width: 400px;
    } /* 关闭时宽度从400开始 */
    100% {
      width: 0px;
    } /* 关闭时宽度变为0 */
  }

  .slide-enter-active,
  .slide-leave-active {
    animation: slide 0.1s forwards; /* 应用定义的动画 */
  }
  .slide-leave-active {
    animation-direction: reverse; /* 设置动画反向播放 */
  }
  .left_box {
    transform: scale(0.8);
    position: absolute;
    z-index: 3;
    // left: 20px;
    left: -2%;
    bottom: -20px;
    top: 0px;
    width: 400px;
    background: linear-gradient(180deg, #0a1d64 0%, rgba(21, 30, 73, 0.7) 100%);
    display: flex;
    flex-direction: column;
    transition: width 0.1s; /* 添加过渡效果 */
    .heard {
      height: 48px;
      background: url("../assets/panel/panel_bg.png") no-repeat;
      background-size: 100% 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-family: YouSheBiaoTiHei;
      font-weight: 400;
      font-size: 24px;
      color: #ffffff;
      text-shadow: 0px 0px 12px rgba(0, 106, 255, 0.8);
      font-style: normal;
      position: relative;
      .change_icon {
        position: absolute;
        top: 50%;
        right: 20px;
        transform: translateY(-50%);
        cursor: pointer;
      }
    }
    .content {
      flex: 1;
      padding: 10px;
      overflow: auto;
      .importantPoint {
        width: 100%;
        background: rgba(0, 0, 0, 0.3);
        border-radius: 12px;
        margin-bottom: 10px;
        .title_name {
          display: flex;
          justify-content: space-between;
          align-items: center;
          .left_title {
            margin: 12px 0 17px 20px;
            font-family: YouSheBiaoTiHei;
            font-size: 18px;
            color: #ffffff;
            line-height: 23px;
            height: 23px;
            font-style: normal;
          }
          .right_ctrl {
            display: flex;
            align-items: center;
            margin-right: 10px;
            .text_left {
              height: 20px;
              font-family: PingFangSC;
              font-weight: 400;
              font-size: 14px;
              color: #ffffff;
              line-height: 20px;
              text-align: left;
              font-style: normal;
            }
            .custSwith {
              --el-switch-on-color: #327dff;
              margin: 0 20px 0 10px;
            }
            .add_gs_gt_xc_zd {
              width: 20px;
              height: 20px;
              background: url("../assets/basic/add.png") no-repeat;
              background-size: 100% 100%;
              cursor: pointer;
            }
            .upload_gs_gt_xc_zd {
              width: 20px;
              height: 20px;
              background: url("../assets/basic/upload.png") no-repeat;
              background-size: 100% 100%;
              cursor: pointer;
              margin: 0 20px;
            }
            .list_gs_gt_xc_zd {
              width: 20px;
              height: 20px;
              background: url("../assets/basic/listSearch.png") no-repeat;
              background-size: 100% 100%;
              cursor: pointer;
            }
          }
        }
        .data_box {
          .top_nav {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-top: 8px;
            padding-bottom: 0 20px 0 20px;
            .nav_item {
              flex: 1;
              position: relative;
              height: 108px;
              display: flex;
              align-items: center;
              justify-content: center;
              flex-direction: column;
              .icon_gsgl {
                background: url("../assets/panel/gsgl_icon.png") no-repeat;
                background-size: 100% 100%;
                width: 34px;
                height: 30px;
              }
              .icon_gstl {
                background: url("../assets/panel/gstl_icon.png") no-repeat;
                background-size: 100% 100%;
                width: 58px;
                height: 38px;
              }
              .icon_xcda {
                background: url("../assets/panel/xcda_icon.png") no-repeat;
                background-size: 100% 100%;
                width: 38px;
                height: 34px;
              }
              .gsgl_text {
                font-family: DINCond;
                font-weight: 900;
                font-size: 46px;
                color: #ffffff;
                text-align: center;
                font-style: normal;
                position: absolute;
                top: -10px;
              }
              .gsgl_span {
                font-family: PingFangSC;
                font-weight: 500;
                font-size: 14px;
                color: #ffffff;
                text-align: center;
                font-style: normal;
                margin-left: 4px;
              }
              .top_text {
                font-family: DINCond;
                font-weight: 900;
                font-size: 23px;
                color: #ffffff;
                text-align: center;
                font-style: normal;
                position: absolute;
                top: -10px;
              }
              .center_icon {
                background: url("../assets/panel/panel_data.png") no-repeat;
                background-size: 100% 100%;
                width: 67px;
                height: 55px;
              }
              .center_icon1 {
                background: url("../assets/panel/panel_point.png") no-repeat;
                background-size: 100% 100%;
                width: 67px;
                height: 55px;
              }
              .bottom_name {
                font-family: PingFangSC;
                font-weight: 400;
                font-size: 14px;
                color: #ffffff;
                font-style: normal;
              }
            }
            .nav_item1 {
              cursor: pointer;
            }
          }
          .data_details {
            .data_list {
              padding: 20px;
              max-height: 200px;
              overflow: auto;
              border: 1px solid rgba(255, 255, 255, 0.1);
              border-left: 0px;
              border-right: 0px;
              padding-bottom: 10px;
              .data_list_item {
                display: flex;
                align-items: center;
                height: 56px;
                background: rgba(82, 106, 135, 0.5);
                border-radius: 8px;
                font-family: PingFangSC, PingFang SC;
                font-weight: 400;
                font-size: 12px;
                color: #ffffff;
                margin-bottom: 10px;
                padding: 0 18px;
                cursor: pointer;
                .length_name {
                  text-align: center;
                  width: 28%;
                }
                .data_name {
                  flex: 1;
                  text-align: center;
                }
              }
              .data_list_item:hover {
                background: rgba(0, 107, 236, 0.7);
              }
              .active {
                background: rgba(0, 107, 236, 0.7);
              }
            }
            .collsp {
              display: flex;
              align-items: center;
              justify-content: center;
              cursor: pointer;
              font-family: PingFangSC;
              font-weight: 400;
              font-size: 12px;
              color: #ffffff;
              height: 30px;
              .div_text {
                padding: 0 5px;
              }
            }
          }
          .new_data_item {
            padding: 10px 10px;
            border: 1px solid #0f5ee2;
            border-radius: 5px;
            margin: 0 5px;
            margin-bottom: 10px;
            color: #fff;
            cursor: pointer;
          }
        }
      }
    }
  }
}
</style>
