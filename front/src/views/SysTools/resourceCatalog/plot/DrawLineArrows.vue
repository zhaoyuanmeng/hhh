<!--
 * @FileDescription: 标绘->画线
 * @Author: yuanhaijun
 * @Date: 2023.03.30
 * @LastEditors: Alex
 * @LastEditTime: 2024-06-19 16:04:45
 -->
<template>
  <div>
    <template v-if="!editHelper.flag">
      <div v-if="isShowAddPoint" class="cloud-func draw-list">
        <div class="func-title">
          画路线
          <el-icon class="" @click.stop="handleClose">
            <CircleClose />
          </el-icon>
        </div>
        <div class="func-warp">
          <!-- @tab-click="handleClick" -->
          <div class="warp-list" v-if="lineList && lineList.length > 0">
            <el-tabs v-model="activeName" class="demo-tabs" @tab-change="handleClick">
              <el-tab-pane label="常规路线" name="0">
                <el-scrollbar>
                  <!-- <div
      class="list"
      v-for="(item, index) in basicLineList"
      :key="item.id"
      :draggable="true"
      @dragstart="handleDragStart(index)"
      @dragover.prevent="handleDragOver(index)"
      @drop="handleDrop(index)"
    >
      <div>
        <div class="list-left">
          <svg-icon :icon-class="icon" class-name="icon"></svg-icon>
          <span>{{ item.name }}</span>
        </div>
      </div>
    </div> -->
                  <div
                    class="list"
                    v-for="(item, index) in basicLineList"
                    :key="item.id"
                    :draggable="true"
                    @dragstart="handleDragStart(index)"
                    @dragover.prevent="handleDragOver(index)"
                    @drop="handleDrop(index)"
                  >
                    <el-checkbox
                      v-model="item.isChecked"
                      :checked="item.isChecked"
                      @change="checkedList(item, index)"
                    >
                    </el-checkbox>
                    <div>
                      <div class="list-left">
                        <svg-icon
                          :icon-class="icon"
                          class-name="icon"
                        ></svg-icon>
                        <span>{{ item.name }}</span>
                      </div>
                      <el-dropdown
                        placement="left"
                        :disabled="disabled"
                        @command="handleCommand"
                      >
                        <span class="el-dropdown-link">
                          <svg-icon icon-class="cloud-more" class-name="icon">
                          </svg-icon>
                        </span>
                        <template v-slot:dropdown>
                          <el-dropdown-menu class="cloud-dropdown">
                            <el-dropdown-item
                              :command="{
                                type: '定位',
                                index: index,
                                status: '0',
                              }"
                              >定位</el-dropdown-item
                            >
                            <el-dropdown-item
                              :command="{
                                type: '编辑',
                                item: item,
                                index: index,
                                status: '0',
                              }"
                              >编辑</el-dropdown-item
                            >
                            <el-dropdown-item
                              :command="{
                                type: '删除',
                                index: index,
                                status: '0',
                              }"
                              >删除</el-dropdown-item
                            >
                          </el-dropdown-menu>
                        </template>
                      </el-dropdown>
                    </div>
                  </div>
                </el-scrollbar>
              </el-tab-pane>
              <el-tab-pane label="避险点路线" name="1">
                <el-scrollbar>
                  <!--  :draggable="true"
                    @dragstart="handleDragStart(index)"
                    @dragover.prevent="handleDragOver(index)"
                    @drop="handleDrop(index)" -->
                  <div
                    class="list"
                    v-for="(item, index) in pointLineList"
                    :key="item.id"
                  >
                    <el-checkbox
                      v-model="item.isChecked"
                      :checked="item.isChecked"
                      @change="checkedList(item, index)"
                    >
                    </el-checkbox>
                    <div>
                      <div class="list-left">
                        <svg-icon
                          :icon-class="icon"
                          class-name="icon"
                        ></svg-icon>
                        <span>{{ item.name }}</span>
                      </div>
                      <el-dropdown
                        placement="left"
                        :disabled="disabled"
                        @command="handleCommand"
                      >
                        <span class="el-dropdown-link">
                          <svg-icon icon-class="cloud-more" class-name="icon">
                          </svg-icon>
                        </span>
                        <template v-slot:dropdown>
                          <el-dropdown-menu class="cloud-dropdown">
                            <el-dropdown-item
                              :command="{
                                type: '定位',
                                index: index,
                                status: '1',
                              }"
                              >定位</el-dropdown-item
                            >
                            <el-dropdown-item
                              :command="{
                                type: '编辑',
                                item: item,
                                index: index,
                                status: '1',
                              }"
                              >编辑</el-dropdown-item
                            >
                            <el-dropdown-item
                              :command="{
                                type: '删除',
                                index: index,
                                status: '1',
                              }"
                              >删除</el-dropdown-item
                            >
                          </el-dropdown-menu>
                        </template>
                      </el-dropdown>
                    </div>
                  </div>
                </el-scrollbar>
              </el-tab-pane>
              <el-tab-pane label="医院路线" name="2">
                <el-scrollbar>
                  <!--  :draggable="true"
                    @dragstart="handleDragStart(index)"
                    @dragover.prevent="handleDragOver(index)"
                    @drop="handleDrop(index)" -->
                  <div
                    class="list"
                    v-for="(item, index) in hosplteList"
                    :key="item.id"
                  >
                    <el-checkbox
                      v-model="item.isChecked"
                      :checked="item.isChecked"
                      @change="checkedList(item, index)"
                    >
                    </el-checkbox>
                    <div>
                      <div class="list-left">
                        <svg-icon
                          :icon-class="icon"
                          class-name="icon"
                        ></svg-icon>
                        <span>{{ item.name }}</span>
                      </div>
                      <el-dropdown
                        placement="left"
                        :disabled="disabled"
                        @command="handleCommand"
                      >
                        <span class="el-dropdown-link">
                          <svg-icon icon-class="cloud-more" class-name="icon">
                          </svg-icon>
                        </span>
                        <template v-slot:dropdown>
                          <el-dropdown-menu class="cloud-dropdown">
                            <el-dropdown-item
                              :command="{
                                type: '定位',
                                index: index,
                                status: '2',
                              }"
                              >定位</el-dropdown-item
                            >
                            <el-dropdown-item
                              :command="{
                                type: '编辑',
                                item: item,
                                index: index,
                                status: '2',
                              }"
                              >编辑</el-dropdown-item
                            >
                            <el-dropdown-item
                              :command="{
                                type: '删除',
                                index: index,
                                status: '2',
                              }"
                              >删除</el-dropdown-item
                            >
                          </el-dropdown-menu>
                        </template>
                      </el-dropdown>
                    </div>
                  </div>
                </el-scrollbar>
              </el-tab-pane>
            </el-tabs>
          </div>
          <div class="button-wrap">
            <div @click="drawParam('lines')">
              <i class="el-icon-plus"></i> 新增路线
            </div>
          </div>
        </div>
      </div>

      <div v-if="isShowPonitModel" class="cloud-func draw-form">
        <div class="func-title">新增路线</div>
        <el-scrollbar style="height: 100%">
          <div class="func-warp">
            <div class="warp-list" v-if="option.customStyle === 0">
              <p>名称</p>
              <el-input
                v-model="option.name"
                placeholder="请输入名称"
                maxlength="200"
              ></el-input>
            </div>
            <div class="warp-list" v-else>
              <p>路线详情</p>
              <el-input
                v-model="option.name"
                :rows="5"
                type="textarea"
                placeholder="请输入避险点路线或者医院路线的详细规划..."
                maxlength="500"
              ></el-input>
            </div>
            <div class="warp-list" v-if="noEdit">
              <p>类型</p>
              <el-radio-group
                v-model="option.customStyle"
                @change="handleChange1"
              >
                <el-radio :value="0">常规路线</el-radio>
                <el-radio :value="4">避险点路线</el-radio>
                <el-radio :value="7">医院路线</el-radio>
              </el-radio-group>
            </div>
            <div class="warp-list color-picker-box">
              <div class="item">
                <p>颜色</p>
                <div class="color_picker_box">
                  <el-color-picker
                    size="small"
                    v-model="option.color"
                    @change="handleChange"
                    :min="1"
                    :max="10"
                    :step="1"
                  ></el-color-picker>
                  <el-input
                    v-model="option.color"
                    placeholder="输入，例如:#FF0000"
                    readonly
                    @change="handleChange"
                  ></el-input>
                </div>
              </div>
              <div class="item">
                <el-switch
                  v-model="isReversal"
                  size="large"
                  active-text="反向"
                  inactive-text="正向"
                  @change="handleChange"
                />
              </div>
            </div>
            <div class="warp-list">
              <p>宽度（米）</p>
              <div class="input-box">
                <el-slider
                  v-model="option.thickness"
                  @change="handleChange"
                  :min="1"
                  :max="20"
                  :step="1"
                ></el-slider>
                <el-input-number
                  v-model="option.thickness"
                  controls-position="right"
                  :min="1"
                  :max="20"
                  :step="1"
                  @change="handleChange"
                ></el-input-number>
              </div>
            </div>
            <div class="warp-list">
              <p>贴图重复度</p>
              <div class="input-box">
                <el-slider
                  v-model="option.tiling"
                  @change="handleChange"
                  :min="0"
                  :max="10000"
                  :step="1"
                ></el-slider>
                <el-input-number
                  v-model="option.tiling"
                  @change="handleChange"
                  :min="0"
                  :max="10000"
                  :step="1"
                  controls-position="right"
                ></el-input-number>
              </div>
            </div>
          </div>
        </el-scrollbar>
        <div class="button-wrap bg">
          <div @click="handleCancel">取消</div>
          <div @click="handleSure" class="bg">确定</div>
        </div>
      </div>
    </template>

    <!-- 操作按钮 -->
    <ActionButtonTmpl
      v-if="editHelper.flag"
      :editHelper="editHelper"
      ref="actionBtn"
      :showDel="true"
    />
  </div>
</template>

<script>
import { mapState, mapActions } from "pinia";
import { useSysToolsCimStore } from "@/store/modules/sysToolsCim";
import { ElMessage } from "element-plus";
import { queryTaskInfo } from "@/api/task/index";
import { getSceneDataForIdAndToDtaw } from "@/api/workCockpit/index.js";
import {
  deleteDrawData,
  saveScreenDraw,
  getMarkersForFloorAllData,
  getDrawDataForScreen,
  setLinesSort
} from "@/api/task/task";
import emitter from "@/utils/emitter";
import { getEmcyInfo } from "@/api/task/emergency";
import { drawLinesName } from "@/views/WorkCockpit/utils";
import useScreenStore from "@/store/modules/screenStore";
import useWorkCockpitStore from "@/store/modules/workCockpit.js";
import useTaskStore from "@/store/modules/taskStore";
import useEmergencyStore from "@/store/modules/emergencyPlan"; // 预案store
import useFloorStore from "@/store/modules/floorStore";
import { debounce } from "@/utils/funs.js";
import ActionButtonTmpl from "../common/ActionButton.tmpl";
import { CircleClose } from "@element-plus/icons-vue";
const defaultOption = {
  id: "",
  name: "",
  utype: "polyline",
  coordinates: [], //坐标点数组
  color: "#0000ff", //颜色
  opacity: 1, // 透明度
  style: 0, // 样式 箭头/光流/贴地/实线/虚线等 单色 4，取值范围：[0~7]
  thickness: 10, // 线宽，单位：米，[0~100]
  brightness: 0.3, //  亮度，取值范围：[0~1]，默认值：0.3
  flowRate: 0.3, // 流速 取值范围：[0~1.0]，默认值：0.5
  tiling: 0, // 贴图重复度 [0~10000]
  depthTest: true, // 深度检测
  shape: 0, // 0：直线， 1：曲线
  info: {}, // 楼层信息
  cusAttr: [], // 自定义属性: [{key: "", value:"", index: 0}]
  isChecked: true,
  customStyle: 0, // 0是常规路线 4 避险点路线 7 应急医院路线
};
export default {
  components: {
    ActionButtonTmpl,
    CircleClose,
  },
  data() {
    return {
      dragStartIndex: null, // 存储起始位置
      noEdit: true,
      activeName: "0", // 常规路线
      lineList: [], // 线列表
      basicLineList: [], // 常规路线
      pointLineList: [], // 避险点路线
      hosplteList: [], // 医院路线
      editHelper: {
        flag: false,
        type: "lines",
      },
      option: {},
      editIndex: -1,
      icon: "cloud-huaxian",
      isShowAddPoint: true,
      isShowPonitModel: false,

      editFlag: false,
      disabled: false,
      isReversal: false,
    };
  },
  created() {
    this.initData("init");
  },
  computed: {
    ...mapState(useWorkCockpitStore, ["policeCheckBox"]),
    ...mapState(useSysToolsCimStore, ["eventSealAPI"]),
    ...mapState(useScreenStore, ["screenInfo", "editScreen", "colorWidth"]),
    ...mapState(useEmergencyStore, ["YAInfo", "editYA"]),
    ...mapState(useFloorStore, [
      "openFloor",
      "floornum",
      "explodebuildname",
      "floorMarkers",
    ]), // 楼层是否抽出 抽出楼层数、楼层名称
  },
  watch: {
    eventSealAPI: {
      indeterminate: false,
      deep: true,
      handler(val) {
        if (val.eventtype === "EditHelperFinished") {
          this.$refs.actionBtn.setParam(this.editHelper.type);
        }
      },
    },
  },
  methods: {
    ...mapActions(useTaskStore, ["SET_TASKINFO"]),
    ...mapActions(useFloorStore, ["set_floorMarkers"]),
    ...mapActions(useScreenStore, ["set_colorWidth"]),
    handleDragStart(index) {
      this.dragStartIndex = index; // 记录拖拽起始索引 ‌:ml-citation{ref="4,5" data="citationList"}
    },
    handleDragOver(targetIndex) {
      if (this.dragStartIndex === null || targetIndex === this.dragStartIndex)
        return;
      // 实时交换位置
      let newArr = this.activeName==='0'?this.basicLineList:this.activeName==='1'?this.pointLineList:this.hosplteList
      const temp = [...newArr];
      const draggedItem = temp[this.dragStartIndex];
      temp.splice(this.dragStartIndex, 1);
      temp.splice(targetIndex, 0, draggedItem);
      if(this.activeName==='0'){
        this.basicLineList = temp;
      }
      if(this.activeName==='1'){
        this.pointLineList = temp;
      }
      if(this.activeName==='2'){
        this.hosplteList = temp;
      }
      this.dragStartIndex = targetIndex; // 更新当前拖拽位置 ‌:ml-citation{ref="4,5" data="citationList"}
    },
    async handleDrop(targetIndex) {
      this.dragStartIndex = null; // 重置状态
      // console.log("最终顺序:",); // 可在此处提交数据更新 ‌:ml-citation{ref="4,5" data="citationList"}
      // let datas = await this.getPointLine(); // 所有线
      // console.log(datas);
      let newArr = this.activeName==='0'?this.basicLineList:this.activeName==='1'?this.pointLineList:this.hosplteList
      this.applySortedOrder(this.activeName,newArr)
    },
    async handleClick(tab){
      this.dragStartIndex = null
      this.lineList = await this.getPointLine(); // 所有线
      this.lineList = this.lineList.map((item) => {
        return { ...item, depthTest: true };
      });
      if (this.lineList?.length) {
        this.basicLineList = this.lineList.filter(
          (item) => item.customStyle === 0
        );
        this.pointLineList = this.lineList.filter(
          (item) => item.customStyle === 4
        );
        this.hosplteList = this.lineList.filter(
          (item) => item.customStyle === 7
        );
      } else {
        this.basicLineList = [];
        this.pointLineList = [];
        this.hosplteList = [];
      }
    },
    // 核心处理逻辑
    async applySortedOrder(active,sortedType0Arr) {
      let datas = await this.getPointLine(); // 所有线
      let localArr = []
      let cursor = 0; // 排序数组指针
      if(active==='0'){
        localArr = datas.map((item) => {
        if (item.customStyle === 0) {
          // 保持原数组结构，仅替换type:0的元素
          return sortedType0Arr[cursor++] || item;
        }
        return item; // 非type:0元素保持原位
      });
      }else if(active==='1'){
        localArr = datas.map((item) => {
        if (item.customStyle === 4) {
          // 保持原数组结构，仅替换type:0的元素
          return sortedType0Arr[cursor++] || item;
        }
        return item; // 非type:0元素保持原位
      });
      }else{
        localArr = datas.map((item) => {
        if (item.customStyle === 7) {
          // 保持原数组结构，仅替换type:0的元素
          return sortedType0Arr[cursor++] || item;
        }
        return item; // 非type:0元素保持原位
      });
      }
      
      sessionStorage.setItem("QXZS_pointLine", JSON.stringify(localArr));
      console.log(localArr,'重新的数据');
      let ids = localArr.map(item=>item.id)
      console.log(ids)
      if(ids?.length){
        let dataList = []
        ids.forEach((item,index)=>{
          dataList.push({id:item,sort:index})
        })
        setLinesSort(dataList).then((res)=>{
        console.log(res)
        emitter.emit("refreshResource", ids[0]);
      })
      }
      
    },
    // 获取数据库线
    async getPointLine() {
      let data = sessionStorage.getItem("QXZS_pointLine");
      return data ? JSON.parse(data) : [];
    },
    async initData(opr) {
      this.lineList = await this.getPointLine(); // 所有线
      this.lineList = this.lineList.map((item) => {
        return { ...item, depthTest: true };
      });
      this.option = JSON.parse(JSON.stringify(defaultOption));
      if (this.lineList?.length) {
        if (
          this.openFloor &&
          this.floornum &&
          this.explodebuildname &&
          this.floorMarkers.length > 0
        ) {
          console.log("进这里");
        }
        this.basicLineList = this.lineList.filter(
          (item) => item.customStyle === 0
        );
        this.pointLineList = this.lineList.filter(
          (item) => item.customStyle === 4
        );
        this.hosplteList = this.lineList.filter(
          (item) => item.customStyle === 7
        );
      } else {
        this.basicLineList = [];
        this.pointLineList = [];
        this.hosplteList = [];
      }
      // 初始效果
      if (opr === "init") {
        emitter.emit("refreshResource", this.lineList);
        let ids = this.lineList.map(item=>item.id)
        // await window.origAPI.polyline.clear();
        console.log(ids)
        await window.origAPI.polyline.delete(ids)
        let data = sessionStorage.getItem("policeData");
        let yds_line = data ? JSON.parse(data) : [];
        if (yds_line.length > 0) {
          yds_line.forEach(async (item) => {
            if (item.info && Object.keys(item.info).length !== 0) {
              if (
                item.info.lineData &&
                Object.keys(item.info.lineData).length !== 0
              ) {
                let g = window.__g;
                // let marker = await g.marker.get(item.id)
                let customObj = await g.customObject.get(item.id);
                // 判断游动哨对象存不存在
                if (customObj.result === 0) {
                  item.info.lineData.depthTest = true;
                  window.__g.polyline.add(item.info.lineData);
                }
              }
            }
          });
        }
        if (this.floorMarkers && this.floorMarkers.length > 0) {
          this.floorMarkers.forEach(async (item) => {
            if (item.data.info && Object.keys(item.data.info).length !== 0) {
              if (
                item.data.info.lineData &&
                Object.keys(item.data.info.lineData).length !== 0
              ) {
                // 判断游动哨对象存不存在
                // let marker = await g.marker.get(item.id)
                let customObj = await g.customObject.get(item.data.id);
                // 判断游动哨对象存不存在
                if (customObj.result === 0) {
                  item.data.info.lineData.depthTest = true;
                  window.__g.polyline.add(item.data.info.lineData);
                }
              }
            }
          });
        }
        this.lineList.forEach((item, i) => {
          this.checkedList(item, i);
        });
        this.basicLineList.forEach((item, i) => {
          this.checkedList(item, i);
        });
        this.pointLineList.forEach((item, i) => {
          this.checkedList(item, i);
        });
        this.hosplteList.forEach((item, i) => {
          this.checkedList(item, i);
        });
      }
    },
    // 画线（直线、曲线）
    drawParam(lineType) {
      this.editHelper.type = lineType;
      this.isShowAddPoint = false;
      this.editHelper.flag = true;
      if (this.activeName === "0") {
        this.option.customStyle = 0;
      } else if (this.activeName === "1") {
        this.option.customStyle = 4;
      } else {
        this.option.customStyle = 7;
      }
    },
    // 列表-操作
    async handleCommand(command) {
      console.log(command);
      let node;
      if (command.status === "0") {
        node = this.basicLineList[command.index];
      }
      if (command.status === "1") {
        node = this.pointLineList[command.index];
      }
      if (command.status === "2") {
        node = this.hosplteList[command.index];
      }
      //  let node = this.lineList[command.index]
      switch (command.type) {
        case "编辑":
          this.editIndex = command.index;
          this.option = JSON.parse(JSON.stringify(command.item));
          this.option.color = this._colorBuild(this.option);
          this.editHelper.type = this.option.shape == 0 ? "lines" : "curve";
          this.editFlag = true;
          this.isShowAddPoint = false;
          this.noEdit = false;
          this.$nextTick(() => {
            this.addModule();
          });
          break;
        case "删除":
          console.log(node);
          await window.origAPI.polyline.delete(node.id);
          await window.__g.marker3d.delete(`${node.id}${node.name}`);
          this.deletePointLine(node, command.index, command.status);
          break;
        default:
          window.origAPI.camera.set(
            node.userData[0],
            node.userData[1],
            node.userData[2],
            node.userData[3],
            node.userData[4],
            0.5
          );
        // window.origAPI.polyline.focus(node.id);
      }
    },
    // 删除数据库线
    deletePointLine(data, index, type) {
      // let index = this.lineList.findIndex(i => i.name === obj.name);
      if (this.editScreen || this.editYA) {
        deleteDrawData(data.id).then((res) => {
          if (res.code === 0) {
            ElMessage.success("删除成功!");
            emitter.emit("refreshResource", data.id);
            if (this.editScreen && this.screenInfo.id) {
              // getSceneDataForIdAndToDtaw({ sceneIds: this.screenInfo.id }).then(
              //   (res) => {
              //     if (res.data.drawLineData?.length) {
              //       // 绘制路线名称
              //       window.__g.marker.deleteByGroupId("linesName", () => {
              //         drawLinesName(res.data.drawLineData);
              //         sessionStorage.setItem(
              //           "lineName",
              //           JSON.stringify(res.data.drawLineData)
              //         );
              //       });
              //     }
              //   }
              // );
            }
            if (this.editYA && this.YAInfo.id) {
              // getEmcyInfo({ id: this.YAInfo.id }).then((res) => {
              //   if (res.data.drawDataList?.length) {
              //     // 绘制路线名称
              //     window.__g.marker.deleteByGroupId("linesName", () => {
              //       drawLinesName(res.data.drawDataList);
              //       // sessionStorage.setItem("lineName", JSON.stringify(res.data.drawLineData));
              //     });
              //   }
              // });
            }
          }
        });
      }
      if (type === "0") {
        this.basicLineList.splice(index, 1);
      }
      if (type === "1") {
        this.pointLineList.splice(index, 1);
      }
      if (type === "2") {
        this.hosplteList.splice(index, 1);
      }
      // this.lineList.splice(index, 1);
      let lastData = this.basicLineList.concat(
        this.pointLineList,
        this.hosplteList
      );
      sessionStorage.setItem("QXZS_pointLine", JSON.stringify(lastData));
    },
    _colorBuild(option) {
      let data = sessionStorage.getItem("QXZS_pointLine");
      let datas = data ? JSON.parse(data) : [];
      if (datas.length > 0) {
        var color = "";
        datas.forEach((item) => {
          if (option.id === item.id) {
            color = item.color;
          }
        });
        return color;
      } else {
        return option.color;
      }
      // let r = parseInt(option.color.slice(1, 3), 16) / 255;
      // let g = parseInt(option.color.slice(3, 5), 16) / 255;
      // let b = parseInt(option.color.slice(5, 7), 16) / 255;
      // return [r, g, b, option.opacity];
    },
    // 增加自定义属性
    addUserAttr() {
      this.option.cusAttr.push({
        key: "",
        value: "",
        index: this.option.cusAttr.length,
      });
    },
    // 屏蔽重复属性名
    attrChange(attr, index) {
      let target = this.option.cusAttr.find(
        (item, i) => attr.key != "" && index != i && item.key == attr.key
      );
      if (target) {
        this.$message.warning("不能存在重复的属性！");
        this.option.cusAttr.splice(index, 1, {
          key: "",
          value: "",
          index,
        });
      }
    },
    // 删除自定义属性
    delUserAttr(item, index) {
      this.option.cusAttr.splice(index, 1);
    },
    // 三维空间距离计算（包含Z轴）
 calculate3DLength(points) {
  let total = 0;
  for (let i = 0; i < points.length - 1; i++) {
    const [x1, y1, z1] = points[i];
    const [x2, y2, z2] = points[i + 1];
    total += Math.hypot(x2 - x1, y2 - y1, z2 - z1);
  }
  return total;
},
    // 获取编辑助手返回数据
    async getEditDate(coordinates) {
      this.editHelper.flag = false;
      this.isShowPonitModel = true;
      this.isShowAddPoint = false;
      let g = window.__g
      let height = await g.camera.get()
      console.log(height)
      coordinates.forEach((element) => {
        element[2] += 1;
      });
      this.option.coordinates = coordinates;
      let length = Math.floor(this.calculate3DLength(coordinates))
      this.option.tiling = Math.round(Number(length)/20)
      let cameras = await window.__g.camera.get();
      this.option.userData = cameras.camera;
      this.option.shape = this.editHelper.type == "lines" ? 0 : 1;

      if (!this.option.id) this.option.id = "drline_" + new Date().getTime();
      if (this.colorWidth.color && this.colorWidth.width) {
        this.option.color = this.colorWidth.color;
        this.option.thickness = this.colorWidth.width;
      }
      this.$nextTick(() => {
        this.addModule();
      });
    },
    // 关闭编辑助手
    closeEditHelper() {
      this.editHelper.flag = false;

      this.isShowPonitModel = false;
      this.isShowAddPoint = true;
    },
    // 复选框事件
    async checkedList(item, index) {
      if (item.isChecked) {
        let res = await window.origAPI.polyline.get(item.id);
        if (res.data && res.data.length > 0) {
          await window.origAPI.polyline.show(item.id);
          // window.origAPI.polyline.focus(item.id);
        } else {
          await window.origAPI.polyline.add(item);
          // window.origAPI.polyline.focus(item.id);
        }
      } else window.origAPI.polyline.hide(item.id);
    },
    handleChange: debounce(function (e) {
      this.updateModule();
    }),
    handleChange1: debounce(function (e) {
      this.option.name = "";
      this.updateModule();
    }),
    async updateModule() {
      let coord = JSON.parse(JSON.stringify(this.option.coordinates));
      coord = this.isReversal ? coord.reverse() : coord;
      let option = {
        ...this.option,
        coordinates: coord,
        depthTest: true,
      };

      await window.origAPI.polyline.update(option);
      // window.origAPI.polyline.focus(option.id);
      // this.isShowPonitModel = true;
    },
    async addModule() {
      let option = {
        ...this.option,
        // color: this._colorBuild(this.option),
      };
      console.log(this.colorWidth);
      await window.origAPI.polyline.delete([option.id]);
      option.depthTest = true;
      // if(this.colorWidth.color&&this.colorWidth.width){
      //   option.color = this.colorWidth.color
      //   option.thickness = this.colorWidth.width
      // }
      await window.origAPI.polyline.add(option);
      // window.origAPI.polyline.focus(option.id);
      this.isShowPonitModel = true;
    },
    handleSure() {
      if (!this.option.name) return this.$message.warning("名称不能为空！");
      // let targetIndex = this.lineList.findIndex(
      //   (item) => item.name == this.option.name
      // );
      let targetIndex;
      if (this.activeName === "0") {
        targetIndex = this.basicLineList.findIndex(
          (item) => item.name == this.option.name
        );
      }
      if (this.activeName === "1") {
        targetIndex = this.pointLineList.findIndex(
          (item) => item.name == this.option.name
        );
      }
      if (this.activeName === "2") {
        targetIndex = this.hosplteList.findIndex(
          (item) => item.name == this.option.name
        );
      }
      if (targetIndex >= 0 && targetIndex !== this.editIndex)
        return this.$message.warning("名称不能重复！");

      let option = JSON.parse(JSON.stringify(this.option));
      option.cusAttr = option.cusAttr.filter((item) => item.key !== ""); // 过滤无效属性

      if (this.editIndex < 0) {
        // this.lineList.push(option);
        if (this.activeName === "0") {
          this.basicLineList.push(option);
        }
        if (this.activeName === "1") {
          this.pointLineList.push(option);
        }
        if (this.activeName === "2") {
          this.hosplteList.push(option);
        }
      } else {
        // this.lineList[this.editIndex] = option;
        if (this.activeName === "0") {
          this.basicLineList[this.editIndex] = option;
        }
        if (this.activeName === "1") {
          this.pointLineList[this.editIndex] = option;
        }
        if (this.activeName === "2") {
          this.hosplteList[this.editIndex] = option;
        }
        this.editIndex = -1;
      }

      this.option = JSON.parse(JSON.stringify(defaultOption));

      this.isShowPonitModel = false;
      this.isShowAddPoint = true;
      this.noEdit = true;
      if (this.editFlag) this.editPointLine(option);
      else this.savePointLine(option);
    },
    // 保存线到后端数据库
    async savePointLine(obj) {
      console.log(obj, "路线数据");
      let cw = { color: obj.color, width: obj.thickness };
      this.set_colorWidth(cw);
      let data = sessionStorage.getItem("QXZS_pointLine");
      if (data) {
        data = JSON.parse(data);
        data.push(obj);
      } else {
        data = [obj];
      }
      sessionStorage.setItem("QXZS_pointLine", JSON.stringify(data));
      if (this.openFloor && this.floornum) {
        obj.info.buildName = this.explodebuildname;
        obj.info.floorNum = this.floornum;
      }
      if (this.editScreen) {
        let query = {
          taskId: this.screenInfo.taskId,
          sceneId: this.screenInfo.id,
          planNode: "3",
          type: "lines",
          policeType: "3",
          data: obj,
        };
        saveScreenDraw(query).then((res) => {
          if (res.code === 0) {
            emitter.emit("refreshResource", query.data.id);
            if (this.screenInfo.taskId) {
              queryTaskInfo({ id: this.screenInfo.taskId }).then((res) => {
                if (res.code === 0) {
                  this.SET_TASKINFO(res.data);
                }
              });
            }
            if (this.openFloor && this.floornum) {
              getMarkersForFloorAllData({
                buildName: this.explodebuildname,
              }).then((res) => {
                if (res.data?.length) {
                  this.set_floorMarkers(res.data);
                } else {
                  this.set_floorMarkers([]);
                }
              });
            }
            // 修改路线名称
            // getSceneDataForIdAndToDtaw({ sceneIds: this.screenInfo.id }).then(
            //   (res) => {
            //     if (res.data.drawLineData?.length) {
            //       // 绘制路线名称
            //       window.__g.marker.deleteByGroupId("linesName", () => {
            //         drawLinesName(res.data.drawLineData);
            //         sessionStorage.setItem(
            //           "lineName",
            //           JSON.stringify(res.data.drawLineData)
            //         );
            //       });
            //     }
            //   }
            // );
          }
        });
      }
      if (this.editYA) {
        let query = {
          sceneId: this.YAInfo.id,
          type: "lines",
          data: obj,
        };
        saveScreenDraw(query).then((res) => {
          if (res.code === 0) {
            emitter.emit("refreshResource", query.data.id);
            ElMessage.success("新增成功");
            // getEmcyInfo({ id: this.YAInfo.id }).then((res) => {
            //     if (res.data.drawDataList?.length) {
            //       // 绘制路线名称
            //       window.__g.marker.deleteByGroupId("linesName", () => {
            //         drawLinesName(res.data.drawDataList);
            //         // sessionStorage.setItem("lineName", JSON.stringify(res.data.drawLineData));
            //       });
            //     }
            //   });
            // getSceneDataForIdAndToDtaw({sceneIds:this.YAInfo.id}).then(res=>{
            //   if (res.data.drawLineData?.length) {
            //         // 绘制路线名称
            //          window.__g.marker.deleteByGroupId('linesName',()=>{
            //           drawLinesName(res.data.drawLineData)
            //           sessionStorage.setItem("lineName", JSON.stringify(res.data.drawLineData));
            //         })
            //   }
            // })
            // queryTaskInfo({ id: this.screenInfo.taskId }).then((res) => {
            //   if (res.code === 0) {
            //     this.SET_TASKINFO(res.data);
            //   }
            // });
          }
        });
      }
      this.initData("init");
    },
    // 编辑线到后端数据库
    async editPointLine(obj) {
      let cw = { color: obj.color, width: obj.thickness };
      this.set_colorWidth(cw);
      if (this.activeName === "0") {
        let index = this.basicLineList.findIndex((i) => i.name === obj.name);
        this.basicLineList.splice(index, 1, obj);
      }
      if (this.activeName === "1") {
        let index = this.pointLineList.findIndex((i) => i.name === obj.name);
        this.pointLineList.splice(index, 1, obj);
      }
      if (this.activeName === "2") {
        let index = this.hosplteList.findIndex((i) => i.name === obj.name);
        this.hosplteList.splice(index, 1, obj);
      }
      // let index = this.lineList.findIndex((i) => i.name === obj.name);
      // this.lineList.splice(index, 1, obj);
      let lastData = this.basicLineList.concat(
        this.pointLineList,
        this.hosplteList
      );
      sessionStorage.setItem("QXZS_pointLine", JSON.stringify(lastData));

      this.editFlag = false;
      if (this.openFloor && this.floornum) {
        obj.info.buildName = this.explodebuildname;
        obj.info.floorNum = this.floornum;
      }
      if (this.editScreen) {
        let query = {
          id: obj.id,
          type: "lines",
          data: obj,
        };
        saveScreenDraw(query).then((res) => {
          if (res.code === 0) {
            emitter.emit("refreshResource", query.data.id);
            ElMessage.success("编辑成功");
            // getSceneDataForIdAndToDtaw({ sceneIds: this.screenInfo.id }).then(
            //   (res) => {
            //     if (res.data.drawLineData?.length) {
            //       // 绘制路线名称
            //       window.__g.marker.deleteByGroupId("linesName", () => {
            //         drawLinesName(res.data.drawLineData);
            //         sessionStorage.setItem(
            //           "lineName",
            //           JSON.stringify(res.data.drawLineData)
            //         );
            //       });
            //     }
            //   }
            // );
          }
        });
      }
      if (this.editYA) {
        let query = {
          id: obj.id,
          type: "lines",
          data: obj,
        };
        saveScreenDraw(query).then((res) => {
          if (res.code === 0) {
            ElMessage.success("编辑成功");
            emitter.emit("refreshResource", query.data.id);
            // getEmcyInfo({ id: this.YAInfo.id }).then((res) => {
            //     if (res.data.drawDataList?.length) {
            //       // 绘制路线名称
            //       window.__g.marker.deleteByGroupId("linesName", () => {
            //         drawLinesName(res.data.drawDataList);
            //         // sessionStorage.setItem("lineName", JSON.stringify(res.data.drawLineData));
            //       });
            //     }
            //   });
          }
        });
      }
    },
    async handleCancel() {
      let that = this;
      this.isShowPonitModel = false;
      this.isShowAddPoint = true;

      if (this.editIndex < 0) {
        await window.origAPI.polyline.delete(this.option.id);
        that.option = JSON.parse(JSON.stringify(defaultOption));
      } else {
        this.editIndex = -1;
        that.option = JSON.parse(JSON.stringify(defaultOption));
      }
      this.noEdit = true;
    },
    handleClose() {
      this.set_colorWidth({});
      // if (this.$parent.hasOwnProperty('funcCMPT')) this.$parent.closeCMPT();
      this.$emit("close");
    },
  },
  async beforeUnmount() {
    if (this.option.id) window.origAPI.polyline.delete(this.option.id);
    await this.handleClose();
  },
};
</script>

<style lang="scss" scoped>
@import "@/styles/func3.scss";
@import "../css/draw.scss";

.color-picker-box {
  display: flex;
  justify-content: space-around;
  align-items: center;

  .item {
    display: flex;
    flex-direction: column;

    :deep(.el-switch__label) {
      color: #fff;
    }

    :deep(.is-active) {
      color: #2ce1ff;
    }
  }
}
:deep(.el-radio__input.is-checked + .el-radio__label) {
  color: #2ce1ff;
}

:deep(.el-textarea__inner) {
  background: rgba(0, 12, 78, 0.5) !important;
  box-shadow: none !important;
  border: 1px solid #5b6799 !important;
  color: #fff !important;
}
:deep(.el-tabs__item) {
  color: #fff;
}
:deep(.is-active) {
  color: #409eff;
}
.draw-form {
  height: 500px;

  .el-scrollbar {
    height: 350px !important;
  }

  .color_picker_box {
    display: flex;
  }
}
.list {
  transition: all 0.3s;
  cursor: move;
}
.list.dragging {
  opacity: 0.5;
  background: #f5f7fa;
}
</style>
