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
        <div v-if="isShowPonitModel" class="cloud-func draw-form">
          <div class="func-title">新增路线</div>
          <el-scrollbar style="height: 100%">
            <div class="func-warp">
              <!-- <div class="warp-list">
                <p>名称</p>
                <el-input
                  v-model="option.name"
                  placeholder="请输入名称"
                  maxlength="200"
                ></el-input>
              </div> -->
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
        class="customBtn"
      />
    </div>
  </template>
  
  <script>
  import { mapState } from "pinia";
  import { useSysToolsCimStore } from "@/store/modules/sysToolsCim";
  import { debounce } from "@/utils/funs.js";
  import ActionButtonTmpl from "../common/ActionButton.tmpl";
  import { CircleClose } from "@element-plus/icons-vue";
  const defaultOption = {
    id: "",
    name: "",
    utype: "polyline",
    groupId:'policeLine',
    coordinates: [], //坐标点数组
    color: "#0000ff", //颜色
    opacity: 1, // 透明度
    style: 0, // 样式 箭头/光流/贴地/实线/虚线等 单色 4，取值范围：[0~7]
    thickness: 10, // 线宽，单位：米，[0~100]
    brightness: 0.3, //  亮度，取值范围：[0~1]，默认值：0.3
    flowRate: 0.3, // 流速 取值范围：[0~1.0]，默认值：0.5
    tiling: 4, // 贴图重复度 [0~50]
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
        isReversal: false,
      };
    },
    created() {
        this.initData("init");
        this.drawParam('lines')
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
          // await window.origAPI.polyline.clear();
        }
      },
      handleChange: debounce(function (e) {
      this.updateModule();
    }),
    async updateModule() {
      let coord = JSON.parse(JSON.stringify(this.option.coordinates));
      coord = this.isReversal ? coord.reverse() : coord;
      let option = {
        ...this.option,
        coordinates: coord,
      };

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
      // 画线（直线、曲线）
      drawParam(lineType) {
        this.editHelper.type = lineType;
        this.isShowAddPoint = false;
        this.editHelper.flag = true;
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
      this.$emit("close");
    },
      handleSure() {
        // if (!this.option.name) return this.$message.warning("名称不能为空！");
        // let targetIndex = this.lineList.findIndex(
        //   (item) => item.name == this.option.name
        // );
        // if (targetIndex >= 0 && targetIndex !== this.editIndex)
        //   return this.$message.warning("名称不能重复！");
  
        let option = JSON.parse(JSON.stringify(this.option));
        option.cusAttr = option.cusAttr.filter((item) => item.key !== ""); // 过滤无效属性
  
        if (this.editIndex < 0) this.lineList.push(option);
        else {
          this.lineList[this.editIndex] = option;
          this.editIndex = -1;
        }
  
        this.option = JSON.parse(JSON.stringify(defaultOption));
  
        this.isShowPonitModel = false;
        this.isShowAddPoint = true;
        this.editHelper.flag = false;
        this.$emit("close");
        if (this.editFlag) this.editPointLine(option);
        else this.savePointLine(option);
      },
      // 保存线到后端数据库
      async savePointLine(obj) {
        // let data = sessionStorage.getItem("QXZS_pointLine");
        // if (data) {
        //   data = JSON.parse(data);
        //   data.push(obj);
        // } else {
        //   data = [obj];
        // }
        // sessionStorage.setItem("QXZS_pointLine", JSON.stringify(data));
        // console.log(obj)
        this.$emit("getLineInfo",obj);
      },
      async handleCancel() {
        let that = this;
        this.isShowPonitModel = false;
        this.isShowAddPoint = true;
        this.editHelper.flag = false;
        if (this.editIndex < 0) {
          await window.origAPI.polyline.delete(this.option.id);
          that.option = JSON.parse(JSON.stringify(defaultOption));
        } else {
          this.editIndex = -1;
          that.option = JSON.parse(JSON.stringify(defaultOption));
        }
        this.$emit("close");
      },
      handleClose() {
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
  .customBtn{
    position: fixed;
    top: 12%;
    left: 50%;
    transform: translate(0,-50%);
    height: auto;
    display: flex;
    flex-direction: row;
    width: 200px;
    div:nth-child(1){
        margin-right: 20px;
    }
  }
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
  
  .draw-form {
    height: 500px;
    position: fixed;
  top: 15%;
//   background: linear-gradient( 180deg, #0A1D64 0%, rgba(21,30,73,0.7) 100%);
    .el-scrollbar {
      height: 350px !important;
    }
  
    .color_picker_box {
      display: flex;
    }
  }
  </style>
  