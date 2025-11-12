<!--
 * @FileDescription: 三维标注->新增标签
 * @Author: yuanhaijun
 * @Date: 2022.11.28
 * @LastEditors: Alex
 * @LastEditTime: 2024-06-20 16:08:05
 -->
 <template>
  <div>
    <div class="cloud-func add">
      <div class="func-title">
        {{ajybol?'安检员详情':'安检站详情'}}
        <el-icon class="" @click.stop="handleClose" style="cursor: pointer">
          <CircleClose />
        </el-icon>
      </div>
      <div class="func-warp">
        <div class="basicInfo">
          <div class="infoName">基本信息</div>
          <div class="input_name" style="display: flex;align-items: center;justify-content: space-between;">
            <div>{{ajybol?'安检员名称':'安检站名称'}}</div>
          </div>
          <div class="input_box">
            <el-input v-model="pointData.mc" placeholder="请输入..." clearable />
          </div>
          <div v-if="ajybol">
          <div class="input_name">安检员位置</div>
          <div class="input_box">
            <el-input v-model="pointData.wz" placeholder="请输入..." clearable />
          </div>
          <div class="input_name">部署数量</div>
          <div class="input_box">
            <el-input v-model="pointData.num" placeholder="请输入..." clearable />
          </div>
          <div class="input_name">联系方式</div>
          <div class="input_box">
            <el-input v-model="pointData.phone" placeholder="请输入..." clearable />
          </div>
        </div>
        <div v-else>
          <div class="input_name">备注</div>
          <div class="input_box">
            <el-input v-model="pointData.bz" type="textarea" :rows="5" placeholder="请输入..." clearable />
          </div>
        </div>
      
        </div>
      </div>

      <div class="warp-footer">
        <div @click="deleteMarker(markerData)">删除</div>
        <div @click="handleClose">取消</div>
        <div class="primary" @click="handleSubmit">确定</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ElMessage } from "element-plus";
import { deleteDrawData, saveScreenDraw, getById } from "@/api/task/task";
import useWorkCockpitStore from '@/store/modules/workCockpit'
import useScreenStore from "@/store/modules/screenStore";
import useFloorStore from "@/store/modules/floorStore";
import useEmergencyStore from "@/store/modules/emergencyPlan";
import { useSysToolsCimStore } from "@/store/modules/sysToolsCim";
import { CircleClose } from "@element-plus/icons-vue";
import { sessionStorage } from "@/utils/storage";
import emitter from "@/utils/emitter"
const WorkCockpitStore = useWorkCockpitStore()
const taskId = computed(()=>WorkCockpitStore.basicTaskId)
const sceneId = computed(()=>WorkCockpitStore.basicSceneId)
const y_aEdit = computed(()=>useEmergencyStore().editYA) // 预案是否调用接口
const editScreen = computed(() => useScreenStore().editScreen)
const openFloor = computed(() => useFloorStore().openFloor) // 楼层是否炸开
const floornum = computed(() => useFloorStore().floornum) // 当前楼层点击楼层数
//当前点击的楼栋名称
let explodebuildname = computed(() => useFloorStore().explodebuildname);
import { computed, nextTick, reactive, ref, watch } from "vue";
const ToolsCimStore = useSysToolsCimStore();
const emit = defineEmits(["closeRightBox"]);
let markerEvent = computed(() => ToolsCimStore.markerEvent);
watch(
  () => markerEvent,
  (nV, oV) => {
    if (nV) {
      nextTick(() => {
        getMarker(nV.value);
      });
    }
  },
  { deep: true, immediate: true }
);
const ajybol = ref(true)
const pointData = reactive({
  mc: "",
  bz: "",
  num:'',
  phone:'',
  wz:'',
});
const markerData = ref({});
// 取消
const handleClose = () => {
  ToolsCimStore.SET_MARKEREVENT({});
  emit("closeRightBox", false);
};
// 获取地图打点的数据
const getMarker = async (item) => {
  if (
    item.GroupID === "fbaj" &&
    item.eventtype === "LeftMouseButtonClick" &&
    (item.Type === "marker"||item.Type === 'MarkerLayer')
  ) {
    showData(item);
  }
};
// 回显数据
const showData = async (item) => {
  if(item.UserData==='安检员'){
    ajybol.value = true
  }else{
    ajybol.value = false
  }
    getById({ id: item.Id}).then(res => {
      markerData.value = res.data.data.marker
      pointData.mc = res.data.data.marker.text
      if (res.data.data.info && Object.keys(res.data.data.info).length !== 0) {
        pointData.mc = res.data.data.info.mc || res.data.data.marker.text
        pointData.bz = res.data.data.info.bz || null;;
        pointData.num = res.data.data.info.num || null;;
        pointData.phone = res.data.data.info.phone || null;;
        pointData.wz = res.data.data.info.wz|| null;;
      }      
    })
};
// 删除地位信息
const deleteMarker = async (data) => {
  let g = window.__g;
  await g.marker.delete(data.id);
  await g.markerLayer.delete(data.id)
  if (editScreen.value||y_aEdit.value) {
    deleteDrawData(data.id).then(res => {
      if (res.code === 0) {
        ElMessage.success('删除成功!')
        emitter.emit("refreshResource",data.id)
      }
    })
  }
  emit("closeRightBox", false);
  // 从缓存中找警车数据
  let carData = sessionStorage.get("AJData") || [];
  if (carData.length > 0) {
    let newData = carData.filter((item) => data.id !== item.id);
    sessionStorage.set("AJData", newData);
  }
};
// 确定
const handleSubmit = async () => {
  if (pointData.mc) {
    let g = window.__g;
    markerData.value.text = pointData.mc;
    g.marker.update(markerData.value);
    let obj = {
            id: markerData.value.id,
            groupId: markerData.value.groupId,
            userData:markerData.value.userData,
            coordinateType: 0, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
            range: [0, 1, 10000, 20000],
            viewHeightRange: [0, 10000],
            autoHeight: false,
            radius: 0,
            fixedSize: false,
            anchors: [-15, 34], //锚点，设置Marker的整体偏移，取值规则和imageSize设置的宽高有关，图片的左上角会对准标注点的坐标位置。示例设置规则：x=-imageSize.width/2，y=imageSize.height
            useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
            textRange: [1, 1000], //文本可视范围[近裁距离, 远裁距离]
            textOffset: [0, 0], // 文本偏移
            textBackgroundColor: [0, 0, 0, 0.75], //文本背景颜色
            fontSize: 8, //字体大小
            fontOutlineSize: 1, //字体轮廓线大小
            fontColor: Color.White, //字体颜色
            displayMode: 0, //智能显示模式  开发过程中请根据业务需求判断使用四种显示模式
            clusterByImage: false, // 聚合时是否根据图片路径分类，即当多个marker的imagePath路径参数相同时按路径对marker分类聚合
            priority: 0, //避让优先级
            occlusionCull: true, //是否参与遮挡剔除
            markers: [
              {
                id: markerData.value.id,
                groupId: markerData.value.groupId,
                coordinate: markerData.value.coordinate, //坐标位置
                text: pointData.mc, //显示的文字
                imagePath: markerData.value.imagePath,
                imageSize: [30,34],
                hoverImagePath: markerData.value.imagePath, // 鼠标悬停时显示的图片路径
                hoverImageSize: [30,34], //鼠标悬停时显示的图片尺寸
              },
            ],
          };
      g.markerLayer.update(obj)    
  }
  pointData.num = pointData.num?Number(pointData.num):null
  let allObj = {
    id: markerData.value.id,
    info: pointData,
    marker: markerData.value,
  };
  if (editScreen.value||y_aEdit.value) {
    let query = {
      id: markerData.value.id,
      type: 'fbaj',
      data: allObj,
    }
    saveScreenDraw(query).then(res => {
      if (res.code === 0) {
        ElMessage.success('编辑成功')
        emitter.emit("refreshResource",query.data.id)
      }
    })
  }
  let data1 = sessionStorage.get("AJData");
  if (data1 && data1.length > 0) {
    let newData = data1.filter((item) => item.id !== allObj.id);
    newData.push(allObj);
    sessionStorage.set("AJData", newData);
  } else {
    sessionStorage.set("AJData", [allObj]);
  }

  nextTick(() => {
    handleClose();
  });
};
</script>

<style lang="scss" scoped>
@import "@/styles/func3.scss";

:deep(.add) {
  width: 400px;
  position: absolute;
  top: -20px;
  bottom: -20px;
  background: linear-gradient(180deg, #0a1d64 0%, rgba(21, 30, 73, 0.7) 100%);

  .func-warp {
    height: calc(100% - 150px);
    display: flex;
    flex-direction: column;
    overflow: auto;
    padding: 4px 20px;

    .input_box {
      display: flex;
      align-items: center;

      .el-input {
        border: 1px solid #5b6799;
        border-radius: 2px;

        .el-input__wrapper {
          background: rgba(0, 12, 78, 0.5);
          box-shadow: none;
        }

        .el-input__inner {
          height: 36px;
          line-height: 36px;
          font-size: 14px;
          color: #ffffff;
          opacity: 0.8;
        }
      }
      .el-textarea__inner{
        background: rgba(0, 12, 78, 0.5);
          box-shadow: none;
          border: 1px solid #5b6799;
        border-radius: 2px;
        color:#fff;
      }
      .el-input__count{
        background: rgba(0, 12, 78, 0.5);
          box-shadow: none;
      }
    }

    .infoName {
      height: 40px;
      line-height: 40px;
      font-weight: 400;
      font-size: 17px;
      color: #ffffff;
    }

    .input_name {
      height: 30px;
      line-height: 30px;
      font-size: 14px;
      color: #fff;
      padding-top: 10px;
    }

    .fuzhufenx {
      .el-select__wrapper {
        background: rgba(0, 12, 78, 0.5);
        box-shadow: none;
        border: 1px solid #5b6799;

        .el-select__selected-item {
          color: #ffffff;
          opacity: 0.8;
        }

        .is-transparent {
          color: #a8abb2;
        }
      }

      .el-input-number__decrease,
      .el-input-number__increase {
        background: rgba(0, 0, 0, 0);
        border-radius: 2px 2px 2px 2px;
        border: 1px solid #ffffff;
        opacity: 0.8;
        width: 30px;

        .el-icon {
          color: #fff;
        }
      }
    }

    .setting {
      .setting_box {
        display: flex;
        align-items: center;
        height: 30px;

        .proportion {
          display: flex;
          justify-content: space-between;

          .el-slider {
            width: calc(60% - 14px);
          }

          .el-input-number {
            width: 40%;
          }
        }
      }
    }

    .warp-border {
      border-bottom: 1px solid rgba(255, 255, 255, 0.1);
      padding: 10px 0;
      display: flex;
      align-items: center;

      p {
        width: 120px;
      }

      ul {
        display: flex;
      }
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

  .warp-footer {
    display: flex;
    justify-content: center;
    padding: 24px;
    border-top: 1px solid #ffffff33;

    div {
      width: 80px;
      line-height: 38px;
      cursor: pointer;
      border-radius: 5px;
      border: 1px solid #029eff;
      text-align: center;
      margin-left: 20px;

      &.primary {
        background: #029eff;
        margin-left: 20px;
      }
    }
  }
}
</style>
