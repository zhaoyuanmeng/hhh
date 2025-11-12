<!--
 * @FileDescription: 标绘->画线
 * @Author: yuanhaijun
 * @Date: 2023.03.30
 * @LastEditors: Alex
 * @LastEditTime: 2024-06-19 18:05:03
 -->
 <template>
    <div>
      <template v-if="!editHelper.flag">
        <div v-if="isShowAddPoint" class="cloud-func draw-list">
          <div class="func-title">
            画线
            <el-icon class="" @click.stop="handleClose" style="cursor: pointer">
              <CircleClose />
            </el-icon>
          </div>
          <div class="func-warp">
            <div class="warp-list">
              <el-scrollbar v-if="false">
                <div class="list" v-for="(item, index) in lineList" :key="index">
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
              <div @click="drawParam('lines')">
                <i class="el-icon-plus"></i> 直线
              </div>
              <div @click="drawParam('curve')">
                <i class="el-icon-plus"></i> 曲线
              </div>
            </div>
          </div>
        </div>
  
        <div v-if="isShowPonitModel" class="cloud-func draw-form">
          <div class="func-title">
            新增{{ editHelper.type == "lines" ? "直线" : "曲线" }}
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
              <div class="warp-list color-picker-box">
                <p>颜色</p>
                <div>
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
              <div class="warp-list">
                <p>透明度</p>
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
              <div class="warp-list">
                <p>样式</p>
                <el-select
                  v-model="option.style"
                  @change="handleChange"
                  placeholder="请选择"
                  :popper-append-to-body="false"
                >
                  <el-option :value="0" label="宽箭头"></el-option>
                  <el-option :value="1" label="细箭头"></el-option>
                  <el-option :value="2" label="光流"></el-option>
                  <el-option :value="3" label="光流1"></el-option>
                  <el-option :value="4" label="单色"></el-option>
                </el-select>
              </div>
              <div class="warp-list">
                <p>宽度（米）</p>
                <div class="input-box">
                  <el-slider
                    v-model="option.thickness"
                    @change="handleChange"
                    :min="1"
                    :max="100"
                    :step="1"
                  ></el-slider>
                  <el-input-number
                    v-model="option.thickness"
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
              <div class="warp-list">
                <p>流速</p>
                <div class="input-box">
                  <el-slider
                    v-model="option.flowRate"
                    @change="handleChange"
                    :min="0"
                    :max="1"
                    :step="0.1"
                  ></el-slider>
                  <el-input-number
                    v-model="option.flowRate"
                    @change="handleChange"
                    :min="0"
                    :max="1"
                    :step="0.1"
                    controls-position="right"
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
                    :max="50"
                    :step="1"
                  ></el-slider>
                  <el-input-number
                    v-model="option.tiling"
                    @change="handleChange"
                    :min="0"
                    :max="50"
                    :step="1"
                    controls-position="right"
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
                    <i
                      class="icon-btn-del el-icon-remove-outline"
                      @click="delUserAttr(item, index)"
                    ></i>
                  </li>
                </ul>
                <i
                  class="icon-btn-add el-icon-circle-plus-outline"
                  @click="addUserAttr"
                ></i>
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
  import { debounce } from "@/utils/funs.js";
  import ActionButtonTmpl from "../common/ActionButton.tmpl";
  import { useSysToolsCimStore } from "@/store/modules/sysToolsCim";
  import useScreenStore from "@/store/modules/screenStore";
  import { CircleClose } from "@element-plus/icons-vue";
  const defaultOption = {
    id: "",
    name: "",
    utype: "polyline",
    coordinates: [], //坐标点数组
    color: "#FF0000", //颜色
    opacity: 1, // 透明度
    style: 4, // 样式 箭头/光流/贴地/实线/虚线等 单色 4，取值范围：[0~7]
    thickness: 10, // 线宽，单位：米，[0~100]
    brightness: 0.3, //  亮度，取值范围：[0~1]，默认值：0.3
    flowRate: 0.3, // 流速 取值范围：[0~1.0]，默认值：0.5
    tiling: 5, // 贴图重复度 [0~50]
    depthTest: true, // 深度检测
    shape: 0, // 0：直线， 1：曲线
  
    cusAttr: [], // 自定义属性: [{key: "", value:"", index: 0}]
    isChecked: true,
  };
  export default {
    components: {
      ActionButtonTmpl,
      CircleClose,
    },
    data() {
      return {
        lineList: [], // 线列表
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
        ...mapState(useScreenStore,['set_sceneLine']),
      // 获取数据库线
      async getPointLine() {
        let data = sessionStorage.getItem("QXZS_pointLine");
        return data ? JSON.parse(data) : [];
      },
      async initData(opr) {
        this.lineList = await this.getPointLine();
        this.option = JSON.parse(JSON.stringify(defaultOption));
  
        // 初始效果
        if (opr === "init") {
          await window.origAPI.polyline.clear();
          this.lineList.forEach((item, i) => {
            this.checkedList(item, i);
          });
        }
      },
      // 画线（直线、曲线）
      drawParam(lineType) {
        this.editHelper.type = lineType;
        this.isShowAddPoint = false;
        this.editHelper.flag = true;
      },
      // 列表-操作
      async handleCommand(command) {
        let node = this.lineList[command.index];
        switch (command.type) {
          case "编辑":
            this.editIndex = command.index;
            this.option = JSON.parse(JSON.stringify(command.item));
            this.option.color = this._colorBuild(this.option);
            this.editHelper.type = this.option.shape == 0 ? "lines" : "curve";
            this.editFlag = true;
            this.isShowAddPoint = false;
            this.$nextTick(() => {
              this.addModule();
            });
            break;
          case "删除":
            await window.origAPI.polyline.delete(node.id);
            this.deletePointLine(node, command.index);
            break;
          default:
            console.log(node.userData);
            // window.origAPI.polyline.focus(node.id);
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
      // 删除数据库线
      deletePointLine(data, index) {
        // let index = this.lineList.findIndex(i => i.name === obj.name);
        this.lineList.splice(index, 1);
        sessionStorage.setItem("QXZS_pointLine", JSON.stringify(this.lineList));
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
          element[2] += 0.2;
        });
        this.option.coordinates = coordinates;
        let cameras = await window.__g.camera.get();
        this.option.userData = cameras.camera;
        this.option.shape = this.editHelper.type == "lines" ? 0 : 1;
  
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
      async updateModule() {
        let option = {
          ...this.option,
          // color: this._colorBuild(this.option),
        };
        console.log(option);
        await window.origAPI.polyline.update(option);
        // window.origAPI.polyline.focus(option.id);
        this.isShowPonitModel = true;
      },
      async addModule() {
        let option = {
          ...this.option,
          // color: this._colorBuild(this.option),
        };
        await window.origAPI.polyline.delete([option.id]);
        await window.origAPI.polyline.add(option);
        // window.origAPI.polyline.focus(option.id);
        this.isShowPonitModel = true;
      },
      handleSure() {
        if (!this.option.name) return this.$message.warning("名称不能为空！");
        let targetIndex = this.lineList.findIndex(
          (item) => item.name == this.option.name
        );
        if (targetIndex >= 0 && targetIndex !== this.editIndex)
          return this.$message.warning("名称不能重复！");
  
        let option = JSON.parse(JSON.stringify(this.option));
        option.cusAttr = option.cusAttr.filter((item) => item.key !== ""); // 过滤无效属性
  
        if (this.editIndex < 0) this.lineList.push(option);
        else {
          // this.$set(this.lineList, this.editIndex, option);
          this.lineList[this.editIndex] = option;
          this.editIndex = -1;
        }
  
        this.option = JSON.parse(JSON.stringify(defaultOption));
  
        this.isShowPonitModel = false;
        this.isShowAddPoint = true;
  
        if (this.editFlag) this.editPointLine(option);
        else this.savePointLine(option);
      },
      // 保存线到后端数据库
      async savePointLine(obj) {
        console.log(obj);
        this.set_sceneLine(obj)
        this.handleClose(obj)
        // let data = sessionStorage.getItem("QXZS_pointLine");
        // if (data) {
        //   data = JSON.parse(data);
        //   data.push(obj);
        // } else {
        //   data = [obj];
        // }
        // sessionStorage.setItem("QXZS_pointLine", JSON.stringify(data));
  
        // this.initData("init");
      },
      // 编辑线到后端数据库
      async editPointLine(obj) {
        let index = this.lineList.findIndex((i) => i.name === obj.name);
        this.lineList.splice(index, 1, obj);
        sessionStorage.setItem("QXZS_pointLine", JSON.stringify(this.lineList));
  
        this.editFlag = false;
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
      },
      handleClose(obj) {
        // if (this.$parent.hasOwnProperty('funcCMPT')) this.$parent.closeCMPT();
        this.$emit("close",obj);
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
  
  // /deep/ .el-popper[role=tooltip] {
  //     background: rgba(63, 216, 244, 0.7) !important;
  // }
  </style>
  