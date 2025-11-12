<!--
 * @FileDescription: 辅助分析->可视域分析
 * @Author: yuanhaijun
 * @Date: 2022.10.25
 * @LastEditors: Alex
 * @LastEditTime: 2024-06-19 19:45:53
 -->
<template>
  <div v-if="isShowPanel" class="cloud-func viewshed">
    <div class="func-title">
      可视域
      <el-icon class="" @click.stop="handleClose">
        <CircleClose />
      </el-icon>
    </div>
    <div class="func-warp">
      <el-scrollbar>
        <div v-if="false">
          <div class="warp-border">
            <p>视域方向</p>
            <ul>
              <el-slider
                v-model="fxVal"
                :min="0"
                :max="359"
                @change="handleSliderChange"
                :show-tooltip="false"
              />
            </ul>
          </div>
        </div>
        <div v-for="(item, index) in setting" :key="index">
          <!-- slider -->
          <div v-if="item.type === 'slider'" class="warp-border">
            <p>{{ item.name }}</p>
            <ul>
              <el-slider
                :min="item.min"
                :max="item.max"
                :step="item.step"
                v-model="item.value"
                @change="handleChangeVAL"
                :show-input="false"
                :show-input-controls="false"
              ></el-slider>
              <el-input-number
                v-model="item.value"
                @change="handleChangeVAL"
                controls-position="right"
                :min="item.min"
                :max="item.max"
                :step="item.step"
              ></el-input-number>
            </ul>
          </div>
          <!-- color -->
          <div v-else-if="item.type === 'color'">
            <div class="warp-color">
              <span>{{ item.name }}</span>
              <div class="color-setting">
                <ul>
                  <svg-icon
                    icon-class="cloud-color"
                    class-name="icon"
                  ></svg-icon>
                  <el-color-picker
                    v-model="item.value"
                    @change="handleChangeVAL"
                    size="small"
                  ></el-color-picker>
                </ul>
                <el-input
                  v-model="item.value"
                  placeholder="输入，例如:#FF0000"
                  @change="handleChangeVAL"
                ></el-input>
              </div>
            </div>
            <div class="warp-border">
              <p>透明度</p>
              <ul>
                <el-slider
                  :min="0"
                  :max="1"
                  :step="0.1"
                  v-model="item.opacity"
                  @change="handleChangeVAL"
                  :show-input="false"
                  :show-input-controls="false"
                ></el-slider>
                <el-input-number
                  v-model="item.opacity"
                  @change="handleChangeVAL"
                  controls-position="right"
                  :min="0"
                  :max="1"
                  :step="0.1"
                ></el-input-number>
              </ul>
            </div>
          </div>
        </div>
      </el-scrollbar>

      <div class="warp-footer">
        <div @click="handleReset">重置</div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "pinia";
import { useSysToolsCimStore } from "@/store/modules/sysToolsCim";
import { CircleClose } from "@element-plus/icons-vue";
export default {
  name: "ViewableAnalysis",
  components: {
    CircleClose,
  },
  data() {
    return {
      endPointList: [],
      startPointList: [],
      fxVal: 0, // 分析方向值
      isShowPanel: false,
      value: 5,
      opacity: 0.5,
      curOa: {
        fov_h: 60,
        fov_v: 30,
        height: 0,
        visibleColor: [0, 1, 0, 1],
        invisibleColor: [1, 0, 0, 1],
        interactiveLock: false,
      },
      setting: [
        {
          type: "slider",
          name: "水平视角（°）",
          key: "fov_h",
          value: 60,
          min: 1,
          max: 150,
          step: 1,
        },
        {
          type: "slider",
          name: "垂直视角（°）",
          value: 30,
          key: "fov_v",
          min: 1,
          max: 150,
          step: 1,
        },
        {
          type: "slider",
          name: "视角高度（米）",
          value: 0,
          key: "height",
          min: 0,
          max: 500,
          step: 1,
        },
        {
          type: "color",
          name: "可视颜色",
          value: "#00FF00",
          opacity: 1.0,
          key: "visibleColor",
        },
        {
          type: "color",
          name: "不可视颜色",
          value: "#FF0000",
          opacity: 1.0,
          key: "invisibleColor",
        },
      ],
    };
  },
  computed: {
    ...mapState(useSysToolsCimStore, ["eventSealAPI"]),
  },
  watch: {
    eventSealAPI: {
      indeterminate: true,
      deep: true,
      handler(val) {
        if (val.eventtype === "ViewShed") {
        //   console.log(val, "调试");
        //   const radius = this.calculateDistance(val.position[0], val.position[1], val.position[2], val.Position[0], val.Position[1], val.Position[2]);
        //     console.log(radius)
        //     let length= Math.floor(radius / 10)
        //     this.startPointList = val.position;
        //     this.endPointList = this.getCircular(this.startPointList,length)
          // 设置视角高度
          this.setting[2].value = val.position[2];
          this._originSetting = JSON.parse(JSON.stringify(this.setting));
          this.isShowPanel = true;
        }
      },
    },
  },
  created() {
    this.startAnalysis();
  },
  methods: {
    calculateDistance(xA, yA, zA, xB, yB, zB) {
  const deltaX = xB - xA;
  const deltaY = yB - yA;
  const deltaZ = zB - zA;
  return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
},
getCircular(center, radius, pointNum = 360){
const num = pointNum
 //待计算圆上的点 注意：半径越大需要计算的点数量越多 本示例使用360个点
const pointArr = []
for (let i = 0; i < num; i++) {
 //角度转弧度
 const radians = (i * (360 / num) * Math.PI) / 180
//计算圆上的点X坐标
const x1 = center[0] + radius * Math.cos(radians)
//计算圆上的点Y坐标
const y1 = center[1] + radius * Math.sin(radians)
//赋值圆上所有点数组
 pointArr.push([x1, y1, center[2]])
}
console.log('pointArr::: ', pointArr);
 return pointArr
},
    handleSliderChange(val) {
      console.log(val, this.endPointList[val], "89080");
      window.sealAPI._viewshed.cancel();
      let g = window.__g;
      let options = {
        startPoint: this.startPointList, //起点坐标
        endPoint: this.endPointList[val], //终点坐标
        fov_h: this.setting[0].value, //横向视角，取值范围：[1°~150°]，默认值：60
        fov_v: this.setting[1].value, //纵向视角，取值范围：[1°~150°]，默认值：30
        height: this.startPointList[2], //视点高度（距离场景交互所拾取点的高度），默认值：0
        visibleColor: this.setting[3].value, //可见区域的颜色，默认值：红色 Color.Red
        invisibleColor: this.setting[4].value, //不可见区域的颜色，默认值：绿色 Color.Green
        interactiveLock: true, //是否开启交互锁定
      };
      g.tools.startViewshedAnalysis(options);
    },
    getCirclePoints(x1, y1, x2, y2, numPoints = 360) {
      // 计算圆心
      const centerX = (x1 + x2) / 2;
      const centerY = (y1 + y2) / 2;

      // 计算半径
      const radius = Math.sqrt(
        Math.pow(centerX - x1, 2) + Math.pow(centerY - y1, 2)
      );

      // 存储圆周上的点
      const points = [];

      // 计算圆周上的点
      for (let i = 0; i < numPoints; i++) {
        const angle = (2 * Math.PI * i) / numPoints; // 角度
        const x = centerX + radius * Math.cos(angle); // x坐标
        const y = centerY + radius * Math.sin(angle); // y坐标
        points.push({ x, y });
      }

      return points;
    },
    // 开始分析
    async startAnalysis() {
      await window.sealAPI._viewshed.cancel();
      await window.sealAPI._viewshed.start();
    },
    handleChangeName(value) {
      var curOa = window.sealAPI._viewshed.getSetting().curOa;
      curOa["name"] = value;
    },
    handleChangeVAL() {
      var curOa = window.sealAPI._viewshed.getSetting().curOa;
      if (curOa) {
        for (var i = 0; i < this.setting.length; i++) {
          if (this.setting[i].type === "color") {
            var r = parseInt(this.setting[i].value.slice(1, 3), 16) / 255;
            var g = parseInt(this.setting[i].value.slice(3, 5), 16) / 255;
            var b = parseInt(this.setting[i].value.slice(5, 7), 16) / 255;
            curOa[this.setting[i].key] = [r, g, b, this.setting[i].opacity];
          } else {
            curOa[this.setting[i].key] = this.setting[i].value;
          }
        }
        window.sealAPI._viewshed.update();
      }
    },
    handleReset() {
      this.setting = JSON.parse(JSON.stringify(this._originSetting));
      this.handleChangeVAL();
    },
    // 分析结束
    async cancelAnalyse() {
      await window.sealAPI._viewshed.cancel();
      let g = window.__g;
      g.tools.stopViewshedAnalysis();
    },
    async handleClose() {
      // if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
      this.$emit("close");
    },
  },
  async beforeUnmount() {
    await this.cancelAnalyse();
    await this.handleClose();
  },
  async unmounted() {
    await this.cancelAnalyse();
    await this.handleClose();
  },
};
</script>

<style lang="scss" scoped>
@import "@/styles/func3.scss";

.viewshed .func-warp {
  :deep(.el-scrollbar__wrap) {
    max-height: 720px;
    margin-bottom: 0 !important;
    overflow-x: hidden;

    .el-scrollbar__view {
      > div {
        margin-bottom: 20px;

        p {
          font-size: 16px;
          color: #ffffff;
          margin-bottom: 8px;
        }

        ul {
          display: flex;
          justify-content: space-between;
          align-items: center;

          .el-slider {
            width: calc(100% - 125px);
            margin-left: 10px;
          }

          .el-input-number {
            width: 95px;
          }
        }
      }

      .warp-border {
        border-bottom: 1px solid rgba(255, 255, 255, 0.1);
        padding: 10px 0;
      }

      .warp-color {
        padding: 10px 0;
        box-sizing: border-box;
        font-size: 16px;
        color: #ffffff;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .color-setting {
          display: flex;
          align-items: center;

          ul {
            background: #494d52;
            border: 1px solid #646b6f;
            overflow: hidden;
            border-radius: 4px;

            .icon {
              width: 20px;
              height: 22px;
            }
          }

          .el-input {
            width: 95px;
            margin-left: 10px;
          }
        }
      }
    }
  }

  .warp-footer {
    display: flex;
    justify-content: center;

    div {
      width: 100%;
      line-height: 38px;
      cursor: pointer;
      border-radius: 5px;
      border: 1px solid #029eff;
      text-align: center;
    }
  }
}
</style>
