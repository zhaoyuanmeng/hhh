<!--
 * @FileDescription: 标绘->拉面
 * @Author: yuanhaijun
 * @Date: 2023.04.03
 * @LastEditors: Alex
 * @LastEditTime: 2024-06-19 16:08:47
 -->
<template>
  <div>
    <template v-if="!editHelper.flag">
      <div v-if="isShowAddPoint" class="cloud-func draw-list">
        <div class="func-title">
          {{ groupId === "cordon" ? "三维警戒线" : "三维多边形" }}
          <!-- <span @click.stop="handleClose">
                            <i class="el-icon-close"></i>
                        </span> -->
          <el-icon class="" @click.stop="handleClose">
            <CircleClose />
          </el-icon>
        </div>
        <div class="func-warp">
          <div class="warp-list">
            <el-scrollbar>
              <div
                class="list"
                v-for="(item, index) in surfaceList"
                :key="index"
              >
                <el-checkbox
                  v-model="item.isChecked"
                  :checked="item.isChecked"
                  @change="checkedList(item, index)"
                >
                </el-checkbox>
                <div>
                  <div class="list-left">
                    <svg-icon :icon-class="icon" class-name="icon"></svg-icon>
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
                          :command="{ type: '定位', index: index }"
                          >定位</el-dropdown-item
                        >
                        <el-dropdown-item
                          :command="{ type: '编辑', item: item, index: index }"
                          >编辑</el-dropdown-item
                        >
                        <el-dropdown-item
                          :command="{ type: '删除', index: index }"
                          >删除</el-dropdown-item
                        >
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </div>
              </div>
            </el-scrollbar>
          </div>
          <div class="button-wrap">
            <div @click="drawParam()">
              <i class="el-icon-plus"></i>
              {{ groupId === "cordon" ? "新增警戒线" : "新增体" }}
            </div>
          </div>
        </div>
      </div>

      <div v-if="isShowPonitModel" class="cloud-func draw-form">
        <div class="func-title">
          {{ groupId === "cordon" ? "新增警戒线" : "新增体" }}
        </div>
        <el-scrollbar style="height: 100%">
          <div class="func-warp">
            <div class="warp-list">
              <p>名称</p>
              <el-input
                v-model="option.name"
                placeholder="请输入名称"
                maxlength="200"
              ></el-input>
            </div>
            <div class="warp-list">
              <p>样式</p>
              <el-select
                v-model="option.style"
                @change="handleChange"
                placeholder="请选择"
              >
                <el-option :value="0" label="波浪"></el-option>
                <el-option :value="1" label="单色"></el-option>
                <el-option :value="2" label="渐变"></el-option>
                <el-option :value="3" label="动态渐变"></el-option>
                <el-option :value="4" label="波浪渐变"></el-option>
                <el-option :value="5" label="宽波浪"></el-option>
                <el-option :value="6" label="旋转箭头"></el-option>
                <el-option :value="7" label="旋转线"></el-option>
                <el-option :value="8" label="旋转渐变"></el-option>
              </el-select>
            </div>
            <div class="warp-list">
              <p>高度（米）</p>
              <div class="input-box">
                <el-slider
                  v-model="option.height"
                  @change="handleChange"
                  :min="1"
                  :max="1000"
                  :step="10"
                ></el-slider>
                <el-input-number
                  v-model="option.height"
                  controls-position="right"
                  :min="1"
                  :max="1000"
                  :step="1"
                  @change="handleChange"
                ></el-input-number>
              </div>
            </div>
            <div class="warp-list">
              <p>亮度</p>
              <div class="input-box">
                <el-slider
                  v-model="option.intensity"
                  @change="handleChange"
                  :min="0"
                  :max="1000"
                  :step="10"
                ></el-slider>
                <el-input-number
                  v-model="option.intensity"
                  controls-position="right"
                  :min="0"
                  :max="1000"
                  :step="1"
                  @change="handleChange"
                ></el-input-number>
              </div>
            </div>
            <div class="warp-list">
              <p>X重复度</p>
              <div class="input-box">
                <el-slider
                  v-model="option.tillingX"
                  @change="handleChange"
                  :min="0"
                  :max="1"
                  :step="0.1"
                ></el-slider>
                <el-input-number
                  v-model="option.tillingX"
                  controls-position="right"
                  :min="0"
                  :max="1"
                  :step="0.1"
                  @change="handleChange"
                ></el-input-number>
              </div>
            </div>
            <div class="warp-list">
              <p>Y重复度</p>
              <div class="input-box">
                <el-slider
                  v-model="option.tillingY"
                  @change="handleChange"
                  :min="0"
                  :max="1"
                  :step="0.1"
                ></el-slider>
                <el-input-number
                  v-model="option.tillingY"
                  controls-position="right"
                  :min="0"
                  :max="1"
                  :step="0.1"
                  @change="handleChange"
                ></el-input-number>
              </div>
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
                    @change="handleChange"
                  ></el-input>
                </div>
              </div>
              <div class="item">
                <p>闪烁</p>
                <el-switch
                  v-model="isFlicker"
                  size="large"
                  active-text="开启"
                  inactive-text="关闭"
                  @change="handleChange"
                />
              </div>
            </div>
            <div class="warp-list">
              <p>体透明度</p>
              <div class="input-box">
                <el-slider
                  :min="0"
                  :max="1"
                  :step="0.1"
                  v-model="option.opacity"
                  @change="handleChange"
                  :show-input="false"
                  :show-input-controls="false"
                ></el-slider>
                <el-input-number
                  v-model="option.opacity"
                  controls-position="right"
                  :min="0"
                  :max="1"
                  :step="0.1"
                  @change="handleChange"
                ></el-input-number>
              </div>
            </div>
            <div class="warp-list user-attr-box">
              <p>选择防线</p>
              <el-radio-group v-model="option.info.fangxian">
              <el-radio value="一道防线">一道防线</el-radio>
              <el-radio value="二道防线">二道防线</el-radio>
              <el-radio value="三道防线">三道防线</el-radio>
            </el-radio-group>
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
    />
  </div>
</template>

<script>
import { mapState, mapActions } from "pinia";
import { ElMessage } from "element-plus";
import { deleteDrawData, saveScreenDraw } from "@/api/task/task";
import useScreenStore from "@/store/modules/screenStore";
import { queryTaskInfo } from "@/api/task/index";
import { useSysToolsCimStore } from "@/store/modules/sysToolsCim";
import { debounce } from "@/utils/funs.js";
import useTaskStore from "@/store/modules/taskStore";
import ActionButtonTmpl from "../common/ActionButton.tmpl";
import { CircleClose, CirclePlus } from "@element-plus/icons-vue";
import { sessionStorage } from "@/utils/storage";
import emitter from "@/utils/emitter"
import {arrToRepeat} from '@/utils/index'
const defaultOption = {
  id: "",
  name: "",
  utype: "polygon3d",
  coordinates: [], //坐标点数组

  style: 5, // 样式 单色1 ，取值范围：[0~8]
  height: 4, // 边框厚度 [0~100]
  intensity: 4, //  亮度，取值范围：[0~1]，默认值：0.3

  color: "#af3c3c", // 面颜色
  opacity: 0.8, // 面透明度
  info:{
    fangxian:''
  },// 防线
  tillingX: 0, //贴图重复度X
  tillingY: 0, //贴图重复度Y
  generateTop: false, //是否生成顶面
  generateSide: true,//是否生成侧面
  generateBottom: false, //是否生成底面
  cusAttr: [], // 自定义属性: [{key: "", value:"", index: 0}]
  isChecked: true,
};
export default {
  components: {
    ActionButtonTmpl,
    CircleClose,
    CirclePlus,
  },
  data() {
    return {
      groupId: sessionStorage.get("groupId") || "",
      surfaceList: [], // 体列表
      editHelper: {
        flag: false,
        type: "body",
      },
      option: {},
      editIndex: -1,
      icon: "cloud-huati",
      isShowAddPoint: true,
      isShowPonitModel: false,
      editFlag: false,
      disabled: false,
      isFlicker: false,
      isChangeFlicker: false,
    };
  },
  created() {
    this.initData("init");
  },
  computed: {
    ...mapState(useSysToolsCimStore, ["eventSealAPI"]),
    ...mapState(useScreenStore, ["screenInfo", "editScreen"]),
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
    // 获取数据库体
    async getPolygon3d() {
      let data = sessionStorage.get("QXZS_polygon3d");
      return data ? arrToRepeat(data) : [];
    },
    async initData(opr) {
      this.surfaceList = await this.getPolygon3d();
      console.log(this.surfaceList)
      this.option = JSON.parse(JSON.stringify(defaultOption));
      emitter.emit("refreshResource",this.surfaceList)
      // 初始效果
      if (opr === "init") {
        await window.origAPI.polygon3d.clear();
        this.surfaceList.forEach((item, i) => {
          this.checkedList(item, i);
        });
      }
    },
    // 画体
    drawParam() {
      this.isShowAddPoint = false;
      this.editHelper.flag = true;
    },
    async handleCommand(command) {
      let node = this.surfaceList[command.index];
      switch (command.type) {
        case "编辑":
          this.editIndex = command.index;
          if(command.item.info){
            command.item.info = command.item.info
          }else{
            command.item.info = {fangxian:''}
          }
          this.option = JSON.parse(JSON.stringify(command.item));
          this.option.color = this._colorBuild(this.option);
          this.editFlag = true;
          this.isShowAddPoint = false;
          this.$nextTick(() => {
            this.addModule();
          });
          break;
        case "删除":
          await window.origAPI.polygon3d.delete(node.id);
          this.deletePolygon3d(node, command.index);
          break;
        default:
          // window.origAPI.polygon3d.focus(node.id);
          window.origAPI.camera.set(
            node.userData[0],
            node.userData[1],
            node.userData[2],
            node.userData[3],
            node.userData[4],
            0.5
          );
      }
    },
    // 删除数据库体
    deletePolygon3d(data, index) {
      // let index = this.surfaceList.findIndex(i => i.name === obj.name);
      if (this.editScreen) {
        deleteDrawData(data.id).then((res) => {
          if (res.code === 0) {
            ElMessage.success("删除成功!");
            emitter.emit("refreshResource",data.id)
          }
        });
      }
      this.surfaceList.splice(index, 1);
      sessionStorage.set("QXZS_polygon3d", this.surfaceList);
    },
    _colorBuild(option) {
      let data = sessionStorage.get("QXZS_polygon3d");
      let datas = data ? data : [];
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

      // return {
      //     color: [r, g, b, option.opacity],
      // };
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
    // 获取编辑助手返回数据
    async getEditDate(coordinates) {
      this.editHelper.flag = false;
      this.isShowPonitModel = true;
      this.isShowAddPoint = false;

      this.option.coordinates = coordinates;
      let cameras = await window.__g.camera.get();
      this.option.userData = cameras.camera;
      if (!this.option.id) this.option.id = "body_" + new Date().getTime();
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
        let res = await window.origAPI.polygon3d.get(item.id);
        if (res.data && res.data.length > 0) {
          await window.origAPI.polygon3d.show(item.id);
          // window.origAPI.polygon3d.focus(item.id);
        } else {
          await window.origAPI.polygon3d.add(item);
          // window.origAPI.polygon3d.focus(item.id);
        }
      } else window.origAPI.polygon3d.hide(item.id);
    },
    // handleChange: debounce(async function (e) {
    //     window.origAPI.polygon3d.glow(this.option.id)
    //     if (this.isFlicker) {
    //         window.origAPI.polygon3d.highlight(this.option.id)
    //         // const res = await __g.polygon3d.glow(this.option.id, 1)
    //     } else {
    //         window.origAPI.polygon3d.stopHighlight(this.option.id)
    //     }
    //     //更改闪烁状态就不更新polygon
    //     if (this.isFlicker !== this.isChangeFlicker) {
    //         this.isChangeFlicker = this.isFlicker
    //         return
    //     }
    //     this.updateModule();
    // }),
    // async updateModule() {
    //     let option = {
    //         ...this.option,
    //         color: this._colorBuild(this.option),
    //     };

    //     await window.origAPI.polygon3d.update(option);
    //     window.origAPI.polygon3d.focus(option.id);
    //     this.isShowPonitModel = true;
    // },
    handleChange: debounce(async function (e) {
      if (this.isFlicker) {
        window.origAPI.polygon3d.highlight(this.option.id);
        // const res = await __g.polygon3d.glow(this.option.id, 1)
      } else {
        window.origAPI.polygon3d.stopHighlight(this.option.id);
      }
      //更改闪烁状态就不更新polygon
      if (this.isFlicker !== this.isChangeFlicker) {
        this.isChangeFlicker = this.isFlicker;
        return;
      }
      this.updateModule();
    }),
    async updateModule() {
      let option = {
        ...this.option,
        // color: this._colorBuild(this.option),
      };

      await window.origAPI.polygon3d.update(option);
      // window.origAPI.polygon3d.focus(option.id);
      this.isShowPonitModel = true;
    },
    async addModule() {
      let option = {
        ...this.option,
        // color: this._colorBuild(this.option),
      };
       console.log(option)
      await window.origAPI.polygon3d.delete([option.id]);
      await window.origAPI.polygon3d.add(option);
      // window.origAPI.polygon3d.focus(option.id);
      this.isShowPonitModel = true;
    },
    handleSure() {
      if (!this.option.name) return this.$message.warning("名称不能为空！");
      let targetIndex = this.surfaceList.findIndex(
        (item) => item.name == this.option.name
      );
      if (targetIndex >= 0 && targetIndex !== this.editIndex)
        return this.$message.warning("名称不能重复！");

      let option = JSON.parse(JSON.stringify(this.option));
      console.log(option,this.option)
      option.cusAttr = option.cusAttr.filter((item) => item.key !== ""); // 过滤无效属性

      if (this.editIndex < 0) this.surfaceList.push(option);
      else {
        // this.$set(this.surfaceList, this.editIndex, option);
        this.surfaceList[this.editIndex] = option;
        this.editIndex = -1;
      }

      this.option = JSON.parse(JSON.stringify(defaultOption));

      this.isShowPonitModel = false;
      this.isShowAddPoint = true;

      if (this.editFlag) this.editPolygon3d(option);
      else this.savePolygon3d(option);
    },
    // 保存体到后端数据库
    async savePolygon3d(obj) {
      let data = sessionStorage.get("QXZS_polygon3d");
      if (data) {
        data = data;
        data.push(obj);
      } else {
        data = [obj];
      }
      sessionStorage.set("QXZS_polygon3d", data);
      if (this.editScreen) {
        let query = {
          taskId: this.screenInfo.taskId,
          sceneId: this.screenInfo.id,
          planNode: "警力部署",
          type: "cordon",
          policeType: "1",
          data: obj,
        };
        saveScreenDraw(query).then((res) => {
          if (res.code === 0) {
            emitter.emit("refreshResource",query.data.id)
            if(this.screenInfo.taskId){
              queryTaskInfo({ id: this.screenInfo.taskId }).then((res) => {
              if (res.code === 0) {
                this.SET_TASKINFO(res.data);
              }
            });
            }
          }
        });
      }
      this.initData("init");
    },
  isFourLayerArray(arr) {
  // 检查是否是数组
  if (!Array.isArray(arr)) {
    return false;
  }

  // 初始化一个计数器，用于跟踪当前层数
  let depth = 1;

  // 递归函数，用于检查层数
  function checkDepth(array, currentDepth) {
    // 如果当前层数大于4，则返回false
    if (currentDepth > 3) {
      return false;
    }

    // 遍历数组中的每个元素
    for (const element of array) {
      // 如果元素是数组，则递归检查
      if (Array.isArray(element)) {
        // 如果在递归中返回false，则直接返回false
        if (!checkDepth(element, currentDepth + 1)) {
          return false;
        }
      } else if (currentDepth !== 3) {
        // 如果当前不是第四层，但遇到了非数组元素，则返回false
        return false;
      }
    }

    // 如果所有元素都检查完毕，并且当前是第四层，则返回true
    return currentDepth === 2;
  }

  // 开始递归检查
  return checkDepth(arr, depth);
},
    // 编辑体到后端数据库
    async editPolygon3d(obj) {
      if(obj.coordinates[0][0]&&obj.coordinates[0][0]?.length){
        obj.coordinates = obj.coordinates[0][0]
      }
      let index = this.surfaceList.findIndex((i) => i.name === obj.name);
      this.surfaceList.splice(index, 1, obj);
      sessionStorage.set("QXZS_polygon3d", this.surfaceList);
      if (this.editScreen) {
        let query = {
          id: obj.id,
          type: "cordon",
          data: obj,
        };
        saveScreenDraw(query).then((res) => {
          if (res.code === 0) {
            emitter.emit("refreshResource",query.data.id)
            ElMessage.success("编辑成功");
          }
        });
      }
      this.editFlag = false;
    },
    async handleCancel() {
      this.isShowPonitModel = false;
      this.isShowAddPoint = true;

      if (this.editIndex < 0)
        await window.origAPI.polygon3d.delete(this.option.id);
      else this.editIndex = -1;
      this.option = JSON.parse(JSON.stringify(defaultOption));
    },
    handleClose() {
      // if (this.$parent.hasOwnProperty('funcCMPT')) this.$parent.closeCMPT();
      this.$emit("close");
    },
  },
  beforeUnmount() {
    if (this.option.id) window.origAPI.polygon3d.delete(this.option.id);
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

.color_picker_box {
  display: flex;
}
</style>
