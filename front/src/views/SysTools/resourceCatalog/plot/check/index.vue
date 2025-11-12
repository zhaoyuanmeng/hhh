<!--
 * @FileDescription: 标绘->三维标注（标签）
 * @Author: yuanhaijun
 * @Date: 2022.11.28
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2022.11.28
 -->
 <template>
  <div>
    <div v-show="!isShowAdd" class="cloud-func tag">
      <div class="func-title">
        防爆安检
        <el-icon class="" @click.stop="handleClose" style="cursor: pointer">
          <CircleClose />
        </el-icon>
      </div>
      <div class="func-warp">
        <el-scrollbar>
          <draggable
            :list="labelList"
            item-key="src"
            @start="drag = true"
            @end="onDragEnd"
          >
            <template #item="{ element }">
              <div class="material-list">
                <ul
                  @click.stop="clickPoint(element)"
                  :style="{
                    border: element.select ? '2px solid #274eef' : 'none',
                  }"
                >
                  <img :src="element.src" alt="" srcset="" />
                  <div class="name" style="padding-top: 12px">
                    {{ element.name }}
                  </div>
                </ul>
              </div>
            </template>
          </draggable>
        </el-scrollbar>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, getCurrentInstance, computed, watch, defineEmits } from "vue";
import draggable from "vuedraggable";
import useBasicStore from "@/store/modules/basicData";
import useScreenStore from "@/store/modules/screenStore";
import useEmergencyStore from "@/store/modules/emergencyPlan";// 预案store
import useTaskStore from "@/store/modules/taskStore";
import { loadPicture } from "@/utils";
import { CircleClose } from "@element-plus/icons-vue";
import { sessionStorage } from "@/utils/storage";
import { queryTaskInfo } from "@/api/task/index";
import { saveScreenDraw } from "@/api/task/task";
import { ElMessage, ElMessageBox } from "element-plus";
import emitter from "@/utils/emitter"
const { proxy } = getCurrentInstance();
const emit = defineEmits(["close"]);
let eventMap = computed(() => useBasicStore().eventSealAPI);
let screenInfo = computed(() => useScreenStore().screenInfo);
const editScreen = computed(() => useScreenStore().editScreen);
const y_aEdit = computed(()=>useEmergencyStore().editYA) // 预案是否调用接口
const y_a_Info = computed(()=>useEmergencyStore().YAInfo) // 预案信息
const isShowAdd = ref(false);
const drag = ref(false);
const items = ref({});
const labelList = ref([
  {
    src: loadPicture("./images/cloud/police/ajy.png"),
    name: "安检员",
  },
  {
    src: loadPicture("./images/cloud/police/ajz.png"),
    name: "安检站",
  }
]);
watch(
  () => eventMap.value,
  (nV, oV) => {
    if (nV) {
      if (nV.eventtype === "LeftMouseButtonClick") {
        addPoint(nV.MouseClickPoint, items.value);
      }
    }
  },
  { deep: true, immediate: false }
);
const clickPoint = (el) => {
  labelList.value.forEach((item) => {
    if (el.name === item.name) {
      item.select = true;
      useBasicStore().setAddPoint(true);
      items.value = item;
      proxy.$modal.msg("请点击地图选择点位");
    } else {
      item.select = false;
    }
  });
};
const extractImagePath =(url)=> { const parts = url.split('/images/'); return parts.length > 1 ? '/images/' + parts[1] : null; }
// 绘制场景标绘数据
const keepImagesAndAfter = (url) => {
  return extractImagePath(url)
};
const onDragEnd = async (event) => {
};
const addPoint = (arr, obj) => {
  console.log(arr, obj);
  let g = window.__g;
  var tagObject = {
    id: `aj${new Date().getTime()}`,
    userData: obj.name,
    groupId: "fbaj",
    coordinate: arr, //坐标位置
    anchors: [-17, 34], //锚点，设置Marker的整体偏移，取值规则和imageSize设置的宽高有关，图片的左上角会对准标注点的坐标位置。示例设置规则：x=-imageSize.width/2，y=imageSize.height
    imageSize: [34, 34], //图片的尺寸
    range: [0, 18000], //可视范围
    imagePath: HostConfig.ImagePath + keepImagesAndAfter(obj.src),
    fixedSize: true, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
    text: obj.name, //显示的文字
    useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
    textRange: [1, 10000], //文本可视范围[近裁距离, 远裁距离]
    textOffset: [-34, -30], // 文本偏移
    textBackgroundColor: [0,0,0,0.75], //文本背景颜色
    fontSize: 8, //字体大小
    fontOutlineSize: 1, //字体轮廓线大小
    fontColor: Color.White, //字体颜色
    showLine: false, //标注点下方是否显示垂直牵引线
    lineSize: [1, 5], //垂直牵引线宽度和高度[width, height]
    lineColor: Color.SpringGreen, //垂直牵引线颜色
    lineOffset: [15, 0], //垂直牵引线偏移
    autoHeight: false, // 自动判断下方是否有物体
    displayMode: 2, //智能显示模式  开发过程中请根据业务需求判断使用四种显示模式
    priority: 0, //避让优先级
    occlusionCull: true, //是否参与遮挡剔除
  };
  g.marker.add(tagObject);
  let allObj = {
    id: tagObject.id,
    info: {},
    marker: tagObject,
  };
  if (editScreen.value) {
    let query = {
      taskId: screenInfo.value.taskId,
      sceneId: screenInfo.value.id,
      type: "fbaj",
      data: allObj,
    };
    saveScreenDraw(query).then((res) => {
      if (res.code === 0) {
         // 更新资源
         emitter.emit("refreshResource",query.data.id)
        if(screenInfo.value.taskId){
          queryTaskInfo({ id: screenInfo.value.taskId}).then((res) => {
          if (res.code === 0) {
            useTaskStore().SET_TASKINFO(res.data);
          }
        });
        }
      }
    });
  }
  if(y_aEdit.value){
      let query = {
        sceneId: y_a_Info.value.id,
        type: "fbaj",
        data: allObj,
      };
      saveScreenDraw(query).then((res) => {
        if (res.code === 0) {
           // 更新资源
           emitter.emit("refreshResource",query.data.id)
            ElMessage.success('新增成功')
        }
      });
    }
  let data1 = sessionStorage.get("AJData");
  if (data1 && data1.length > 0) {
    let newData = data1.filter((item) => item.id !== allObj.id);
    newData.push(allObj);
    sessionStorage.set("AJData", newData);
  } else {
    sessionStorage.set("AJData", [allObj]);
  }
  clearAddPoint();
};
const clearAddPoint = () => {
  items.value = {};
  useBasicStore().setAddPoint(false);
  labelList.value.forEach((item) => {
    item.select = false;
  });
};
const handleClose = async () => {
  clearAddPoint();
  emit("close");
};
</script>

<style lang="scss" scoped>
@import "@/styles/func3.scss";

.list {
  display: flex;
  align-items: center;
  margin-bottom: 16px;

  .list-left {
    margin-left: 16px;
  }
}

.cloud-func {
  height: 200px !important;
}

.tag .func-warp {
  width: 370px;
  padding: 10px 12px;

  :deep(.el-scrollbar__wrap) {
    max-height: 720px;
    margin-bottom: 0 !important;
    overflow-x: hidden;

    .label-warp-list {
      margin-bottom: 12px;
      height: 164px;
      position: relative;
      background: #4b5059;
      box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
        0px 0px 0px 0px rgba(0, 0, 0, 0.5);
      border-radius: 16px;
      border: 2px solid #4b5059;
      display: flex;
      flex-direction: column;
      justify-content: space-between;

      &.active {
        border-color: #02b2ff;
      }

      .warp-list-top {
        width: 100%;
        height: 100%;
        padding: 18px;
        padding-bottom: 0;
        box-sizing: border-box;
        text-align: center;
        overflow: hidden;

        li {
          font-size: 18px;
          font-weight: 400;
          color: #ffffff;
          text-align: left;
        }

        img {
          width: auto;
          height: auto;
        }
      }

      .el-dropdown {
        position: absolute;
        top: 18px;
        right: 18px;
      }
    }
  }

  .warp-footer {
    display: flex;
    justify-content: center;
    margin-top: 10px;

    div {
      width: 100%;
      line-height: 38px;
      cursor: pointer;
      border-radius: 5px;
      border: 1px solid #029eff;
      text-align: center;
    }
  }

  :deep(.el-scrollbar__view) {
    height: 500px;
  }

  .material-list {
    cursor: pointer;
    width: 100px;
    height: 100px;
    margin-bottom: 5px;
    margin-right: 8px;
    background: #363840;
    border-radius: 9px;
    border: 1px solid #63646a;
    display: inline-block;
    text-align: center;
    padding: 7px;
    box-sizing: border-box;
    overflow: hidden;

    ul {
      width: 100%;
      height: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;

      img {
        width: 48px;
        height: 48px;
      }

      .name {
        // padding: 10px 0;
      }
    }
  }
}
</style>
