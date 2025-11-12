<!--
 * @FileDescription: 标绘->画面
 * @Author: yuanhaijun
 * @Date: 2023.04.03
 * @LastEditors: Alex
 * @LastEditTime: 2024-06-19 15:48:25
 -->
<template>
  <div>
    <template v-if="!editHelper.flag">
      <div v-if="isShowAddPoint" class="cloud-func draw-list">
        <div class="func-title">
          画面
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
            <div @click="drawParam()"><i class="el-icon-plus"></i> 新增面</div>
          </div>
        </div>
      </div>

      <div v-if="isShowPonitModel" class="cloud-func draw-form">
        <div class="func-title">新增面</div>
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
                <el-option :value="0" label="单色"></el-option>
                <el-option :value="1" label="圆点"></el-option>
                <el-option :value="2" label="体积"></el-option>
                <el-option :value="3" label="渐变"></el-option>
                <el-option :value="4" label="动态渐变"></el-option>
                <el-option :value="5" label="波纹"></el-option>
                <el-option :value="6" label="宽波纹"></el-option>
                <el-option :value="7" label="旋转箭头"></el-option>
                <el-option :value="8" label="旋转线"></el-option>
                <el-option :value="9" label="旋转渐变"></el-option>
              </el-select>
            </div>
            <div class="warp-list">
              <p>边框厚度（米）</p>
              <div class="input-box">
                <el-slider
                  v-model="option.frameThickness"
                  @change="handleChange"
                  :min="1"
                  :max="100"
                  :step="1"
                ></el-slider>
                <el-input-number
                  v-model="option.frameThickness"
                  controls-position="right"
                  :min="1"
                  :max="100"
                  :step="1"
                  @change="handleChange"
                ></el-input-number>
              </div>
            </div>
            <div class="warp-list">
              <p>亮度</p>
              <div class="input-box">
                <el-slider
                  v-model="option.brightness"
                  @change="handleChange"
                  :min="0"
                  :max="1"
                  :step="0.1"
                ></el-slider>
                <el-input-number
                  v-model="option.brightness"
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
              <p>面透明度</p>
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

            <div class="warp-list color-picker-box">
              <p>边框颜色</p>
              <div>
                <el-color-picker
                  size="small"
                  v-model="option.frameColor"
                  @change="handleChange"
                  :min="1"
                  :max="10"
                  :step="1"
                ></el-color-picker>
                <el-input
                  v-model="option.frameColor"
                  placeholder="输入，例如:#FF0000"
                  @change="handleChange"
                ></el-input>
              </div>
            </div>
            <div class="warp-list">
              <p>边框透明度</p>
              <div class="input-box">
                <el-slider
                  :min="0"
                  :max="1"
                  :step="0.1"
                  v-model="option.frameOpacity"
                  @change="handleChange"
                  :show-input="false"
                  :show-input-controls="false"
                ></el-slider>
                <el-input-number
                  v-model="option.frameOpacity"
                  controls-position="right"
                  :min="0"
                  :max="1"
                  :step="0.1"
                  @change="handleChange"
                ></el-input-number>
              </div>
            </div>

            <div class="warp-list user-attr-box">
              <p>自定义属性</p>
              <ul>
                <li v-for="(item, index) in option.cusAttr" :key="index">
                  <el-input
                    v-model="item.key"
                    placeholder="key"
                    @change="attrChange(item, index)"
                  />
                  <el-input v-model="item.value" placeholder="value" />
                  <el-icon @click="delUserAttr(item, index)">
                    <CircleClose />
                  </el-icon>
                  <!-- <i class="icon-btn-del el-icon-remove-outline" @click="delUserAttr(item, index)"></i> -->
                </li>
              </ul>
              <el-icon @click="addUserAttr">
                <CirclePlus />
              </el-icon>
              <!-- <i class="icon-btn-add el-icon-circle-plus-outline" @click="addUserAttr"></i> -->
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
import { mapState } from "pinia";
import { useSysToolsCimStore } from "@/store/modules/sysToolsCim";
import { debounce } from "@/utils/funs.js";
import ActionButtonTmpl from "../common/ActionButton.tmpl";
import { CircleClose, CirclePlus } from "@element-plus/icons-vue";
const defaultOption = {
  id: "",
  name: "",
  utype: "polygon",
  coordinates: [], //坐标点数组

  style: 0, // 样式 单色 0 ，取值范围：[0~9]
  frameThickness: 2, // 边框厚度 [0~100]
  brightness: 0.8, //  亮度，取值范围：[0~1]，默认值：0.3

  color: "#af3c3c", // 面颜色
  opacity: 0.8, // 面透明度
  frameColor: "#0000FF", // 边框颜色
  frameOpacity: 0.8, // 边框透明度

  depthTest: true, // 深度检测

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
      surfaceList: [], // 面列表
      editHelper: {
        flag: false,
        type: "shape",
      },
      option: {},
      editIndex: -1,
      icon: "cloud-huamian",
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
    // 获取数据库面
    async getPolygon() {
      let data = sessionStorage.getItem("QXZS_polygon");
      return data ? JSON.parse(data) : [];
    },
    async initData(opr) {
      this.surfaceList = await this.getPolygon();
      this.option = JSON.parse(JSON.stringify(defaultOption));

      // 初始效果
      if (opr === "init") {
        await window.origAPI.polygon.clear();
        this.surfaceList.forEach((item, i) => {
          this.checkedList(item, i);
        });
      }
    },
    // 画面
    drawParam() {
      this.isShowAddPoint = false;
      this.editHelper.flag = true;
    },
    async handleCommand(command) {
      let node = this.surfaceList[command.index];
      switch (command.type) {
        case "编辑":
          this.editIndex = command.index;
          this.option = JSON.parse(JSON.stringify(command.item));
          this.option.color = this._colorBuild(this.option);
          this.option.frameColor = this._colorBuild1(this.option);
          this.editFlag = true;
          this.isShowAddPoint = false;
          this.$nextTick(() => {
            this.addModule();
          });
          break;
        case "删除":
          await window.origAPI.polygon.delete(node.id);
          this.deletePolygon(node, command.index);
          break;
        default:
          // window.origAPI.polygon.focus(node.id);
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
    // 删除数据库画
    deletePolygon(data, index) {
      // let index = this.surfaceList.findIndex(i => i.name === obj.name);
      this.surfaceList.splice(index, 1);
      sessionStorage.setItem("QXZS_polygon", JSON.stringify(this.surfaceList));
    },
    _colorBuild1(option) {
      let data = sessionStorage.getItem("QXZS_polygon");
      let datas = data ? JSON.parse(data) : [];
      if (datas.length > 0) {
        var color = "";
        datas.forEach((item) => {
          if (option.id === item.id) {
            color = item.frameColor;
          }
        });
        return color;
      } else {
        return option.color;
      }
    },
    _colorBuild(option) {
      let data = sessionStorage.getItem("QXZS_polygon");
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

      // let fr = parseInt(option.frameColor.slice(1, 3), 16) / 255;
      // let fg = parseInt(option.frameColor.slice(3, 5), 16) / 255;
      // let fb = parseInt(option.frameColor.slice(5, 7), 16) / 255;

      // return {
      //   color: [r, g, b, option.opacity],
      //   frameColor: [fr, fg, fb, option.frameOpacity],
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
      coordinates.forEach((element) => {
        element[2] += 0.1;
      });
      this.option.coordinates = coordinates;
      let cameras = await window.__g.camera.get();
      this.option.userData = cameras.camera;
      if (!this.option.id) this.option.id = "drline_" + new Date().getTime();
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
        let res = await window.origAPI.polygon.get(item.id);
        if (res.data && res.data.length > 0) {
          await window.origAPI.polygon.show(item.id);
          // window.origAPI.polygon.focus(item.id);
        } else {
          await window.origAPI.polygon.add(item);
          // window.origAPI.polygon.focus(item.id);
        }
      } else window.origAPI.polygon.hide(item.id);
    },
    handleChange: debounce(function (e) {
      if (this.isFlicker) {
        window.origAPI.polygon.highlight(this.option.id);
      } else {
        window.origAPI.polygon.stopHighlight(this.option.id);
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

      await window.origAPI.polygon.update(option);
      // window.origAPI.polygon.focus(option.id);
      this.isShowPonitModel = true;
    },
    async addModule() {
      let option = {
        ...this.option,
        // color: this._colorBuild(this.option),
      };

      await window.origAPI.polygon.delete([option.id]);
      await window.origAPI.polygon.add(option);
      // window.origAPI.polygon.focus(option.id);
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

      if (this.editFlag) this.editPolygon(option);
      else this.savePolygon(option);
    },
    // 保存面到后端数据库
    async savePolygon(obj) {
      let data = sessionStorage.getItem("QXZS_polygon");
      if (data) {
        data = JSON.parse(data);
        data.push(obj);
      } else {
        data = [obj];
      }
      sessionStorage.setItem("QXZS_polygon", JSON.stringify(data));

      this.initData("init");
    },
    // 编辑面到后端数据库
    async editPolygon(obj) {
      let index = this.surfaceList.findIndex((i) => i.name === obj.name);
      this.surfaceList.splice(index, 1, obj);
      sessionStorage.setItem("QXZS_polygon", JSON.stringify(this.surfaceList));

      this.editFlag = false;
    },
    async handleCancel() {
      this.isShowPonitModel = false;
      this.isShowAddPoint = true;

      if (this.editIndex < 0)
        await window.origAPI.polygon.delete(this.option.id);
      else this.editIndex = -1;
      this.option = JSON.parse(JSON.stringify(defaultOption));
    },
    handleClose() {
      // if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
      this.$emit("close");
    },
  },
  beforeUnmount() {
    if (this.option.id) window.origAPI.polygon.delete(this.option.id);
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
