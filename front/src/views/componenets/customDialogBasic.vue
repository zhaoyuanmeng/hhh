<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-20 18:58:23
 * @LastEditTime: 2024-06-21 13:23:37
 * @LastEditors: Alex
-->
<template>
  <div class="app-container home">
    <el-dialog
      v-model="openBol"
      width="50%"
      @close="cancel"
      align-center
      :destory-on-close="false"
      :close-on-click-modal="false"
      class="my_Dialog"
    >
      <template #header>
        <div class="heard_name">
          <div class="d_name">{{ dialogData.title }}</div>
        </div>
      </template>
      <!-- 新增重点点位列表 -->
      <template v-if="dialogData.step == 1">
        <div class="dark_nav_wrap">
          <div class="nav-body">
            <div class="nav-body-desc">
              {{ archivesData.data ? archivesData.data.beizhu : "" }}
            </div>
            <div class="nav-body-edit">
              <el-button
                type="primary"
                size="small"
                @click="handlerBasicinfoFun"
                style="margin-right: 10px"
                >编辑</el-button
              >
              <el-button type="danger" size="small" @click="deleteBasicFun"
                >删除</el-button
              >
              <el-button
                type="primary"
                size="small"
                @click="sortAction"
                style="margin-left: 10px"
                >排序</el-button
              >
            </div>
          </div>
        </div>
        <ul class="keypointsList">
          <li
            class="keypointsList-item"
            :class="{ active: dialogData.selectKeypointId == listitem.id }"
            @click="dialogData.selectKeypointId = listitem.id"
            v-for="(listitem, index) of dialogData.keypointsList"
            :key="index"
          >
            <div class="keypointsList-item-contain">
              <span><span>{{index+1}}.</span>{{ listitem.archives_name }}</span><span class="keyNum">{{ listitem.count }}</span>
            </div>
          </li>
        </ul>
      </template>
      <!-- 新增重点点位信息填写 -->
      <template v-if="dialogData.step == 2">
        <div class="dark_nav_wrap">
          <el-select
            size="large"
            v-model="dialogData.selectKeypointId"
            placeholder="请选择"
            @change="onChangeKeyPointCategoryFun"
            style="width: 250px;"
          >
            <el-option
              v-for="item in dialogData.keypointsList"
              :key="item.id"
              :label="item.archives_name"
              :value="item.id"
            >
            </el-option>
          </el-select>
          <ul class="basicInfoList" v-if="dialogData.activekeypointsList?.length">
            <el-tag v-for="(listitem, index) of dialogData.activekeypointsList" :key="index" closable
             :class="dialogData.selectKeypointInfo.id == listitem.id?'success_active':'info_active'" 
             @close="delKeypointFun(listitem)" @click="onChangeSelectKeypointDetailFun(listitem)" style="cursor: pointer;margin-right:10px">
              {{
                listitem.data.mingcheng ||listitem.data.title|| "-"
              }}
          </el-tag>
          </ul>
        </div>
        <div class="customForm" style="height: 600px;overflow: auto;">
        <FormCreate v-model="fApi" :rule="rules" :option="option"/>
      </div>
      </template>

      <el-row
        type="flex"
        v-if="dialogData.step == 1"
        justify="center"
        style="margin-bottom: 42px; margin-top: 22px"
      >
        <el-button type="primary" @click="nextStepFun" class="btn-dialog"
          >下一步</el-button
        >
      </el-row>
    </el-dialog>

  <!-- 表格排序 -->
  <el-dialog
      v-model="centerDialogVisible"
      width="50%"
      @close="cancelTZ"
      align-center
      :destory-on-close="false"
      :close-on-click-modal="false"
      class="my_Dialog"
    >
      <template #header>
        <div class="heard_name">
          <div class="d_name">拖拽排序</div>
        </div>
      </template>
      <!-- <ul class="keypointsList">
          <li
            class="keypointsList-item"
            :class="{ active: dialogData.selectKeypointId == listitem.id }"
            @click="dialogData.selectKeypointId = listitem.id"
            v-for="(listitem, index) of dialogData.keypointsList"
            :key="index"
          >
            <div class="keypointsList-item-contain">
              <span><span>{{index+1}}.</span>{{ listitem.archives_name }}</span><span class="keyNum">{{ listitem.count }}</span>
            </div>
          </li>
        </ul> -->
        <div class="grid-container">
    <TransitionGroup name="list">
      <div 
      class="item_li"
        v-for="(item, index) in dropData"
        :key="item.id"
        draggable="true"
        @dragstart="dragStart(index)"
        @dragover.prevent="dragOver(index)"
        @dragend="dragEnd"
      >
        <span>{{ index+1 }}</span>{{ item.archives_name }}
    </div>
    </TransitionGroup>
  </div>
      <template #footer>
      <div class="dialog-footer">
        <el-button @click="cancelTZ">取消</el-button>
        <el-button type="primary" @click="sureTZ">
          确定
        </el-button>
      </div>
    </template>
    </el-dialog>
  </div>
</template>
<script setup>
import {
  reactive,
  ref,
  getCurrentInstance,
  nextTick,
  onMounted,
  onBeforeUnmount,
  onUnmounted,
} from "vue";
import {
  deletePointById,
  PostAddFormInfo,
  deleteFormInfo,
  getArchivesInfoList,
  GetSelectPointList,
  dropDatsToSort
} from "@/api/index";
import draggable from 'vuedraggable';
import { ElMessage, ElMessageBox } from "element-plus";
import $ from "jquery";
import FormCreateUtil from "@/utils/dynamicForm";
import emitter from "@/utils/emitter";
const { proxy } = getCurrentInstance();
const UPLOAD_BASE_URL = import.meta.env.VITE_APP_BASE_API;
const emit = defineEmits(["hideDialog", "editData"]);
const openBol = ref(true);
const centerDialogVisible = ref(false)
const archivesData = ref({}); //选择的档案数据basicInfo+选择的目标档案数据data
// 拖拽逻辑
let draggedIndex = null;

// 拖拽逻辑‌:ml-citation{ref="1,3" data="citationList"}
const dragStart = (index) => {
  draggedIndex = index;
};
const dropData = ref([])
const dragOver = (targetIndex) => {
  if (draggedIndex === targetIndex) return;
  
  const newList = [...dropData.value];
  const draggedItem = newList[draggedIndex];
  
  // 计算实际移动方向
  const actualIndex = targetIndex > draggedIndex ? 
    targetIndex - (targetIndex % 2) : 
    targetIndex + (targetIndex % 2);
  
  newList.splice(draggedIndex, 1);
  newList.splice(actualIndex, 0, draggedItem);
  
  dropData.value = newList;
  draggedIndex = actualIndex;
};
const dialogData = reactive({
  step: 1, //默认选择类目弹框
  selectBasicinfo: 0, //默认选中的基础信息列表
  selectKeypointId: "", //选中的点位类目ID
  activekeypointsList: [], //选中的目标档案 -> 点位类目 -> 点位列表
  selectKeypointInfo: {}, //默认选中的点位信息
  basicList: [], //目标档案基础信息列表
  keypointsList: [], //点位类目
});
// 上一步
const preStepFun = () => {
  dialogData.selectKeypointId = ''
  dialogData.selectKeypointInfo = {}
  fApi.value = {}
  rules.value = []
  nextTick(() => {
    getKeyPointCategoryList();
    dialogData.step = 1;
  });
};
let fApi = ref({});
let rules = ref([]);
let option = ref({
  form: {
    // 行内表单模式
    inline: false,
    // 表单域标签的位置，如果值为 left 或者 right 时，则需要设置 label-width
    labelPosition: "right",
    // 表单域标签的后缀
    labelSuffix: undefined,
    // 是否显示必填字段的标签旁边的红色星号
    hideRequiredAsterisk: false,
    // 表单域标签的宽度，例如 '50px'。作为 Form 直接子元素的 form-item 会继承该值。支持 auto。
    labelWidth: "145px",
    // 是否显示校验错误信息
    showMessage: true,
    // 是否以行内形式展示校验信息
    inlineMessage: false,
    // 是否在输入框中显示校验结果反馈图标
    statusIcon: false,
    // 是否在 rules 属性改变后立即触发一次验证
    validateOnRuleChange: true,
    // 是否禁用该表单内的所有组件。若设置为 true，则表单内组件上的 disabled 属性不再生效
    disabled: false,
    // 用于控制该表单内组件的尺寸 medium / small / mini
    size: undefined,
    // 是否显示 label
    title: true,
  },
  // 显示重置表单按扭
  resetBtn: {
    width: "100px",
    icon: "",
    size: "large",
    innerText: "上一步",
    show: true,
    click: preStepFun,
  },
  submitBtn: {
    width: "100px",
    icon: "",
    innerText: "新增",
  },
  // 表单提交按扭事件
  onSubmit: (formData) => {
    submitFormData(formData);
  },
});
const cancel = () => {
  dialogData.step = 1;
  dialogData.selectBasicinfo = 0;
  dialogData.selectKeypointId = "";
  dialogData.selectKeypointInfo = {};
  dialogData.activekeypointsList = [];
  emit("hideDialog");
};
const cancelTZ = () => {
  dropData.value = dialogData.keypointsList
  centerDialogVisible.value = false
}
const sureTZ = () => {
  console.log(dropData.value)
  let datas = []
  dropData.value.forEach((item,index)=>{
    datas.push({id:item.id,sort:index+1})
  })
  dropDatsToSort(datas).then(res=>{
    console.log(res)
    if (res.code === 0) {
        ElMessage.success("排序成功");
        getKeyPointCategoryList();
        centerDialogVisible.value = false
      }
    
  })
}
//添加或编辑提交
const submitFormData = (formData) => {
  console.log(formData);
  formData.weizhi = formData.weizhi ? Number.parseFloat(formData.weizhi) : 0;
  let data = formData,
    params = {};
  data.point = JSON.stringify({
    x: data.jingdu,
    y: data.weidu,
  });
  if (data["danweiyijian"]) {
    let splitArr = [],
      splitArr1 = [];
    data["danweiyijian"].split("|").forEach((item) => {
      splitArr.push({ danweiyijian: item });
    });
    data["ercihechaduibiqingkuang"].split("|").forEach((item) => {
      splitArr1.push({ ercihechaduibiqingkuang: item });
    });
    data["danweiyijian"] = splitArr;
    data["ercihechaduibiqingkuang"] = splitArr1;
  }
  let parseToObjArr = ["canting", "guibinxiuxishi", "huiyishi"];
  if (data["canting"]) {
    parseToObjArr.forEach((item) => {
      data[item] = {
        mingcheng: data[item + "mingcheng"],
        zuowei: data[item + "zuowei"],
      };
    });
  }
  params = {
    data,
    childTypeId: dialogData.selectKeypointId,
    jcxxId: archivesData.value.data.id,
    parentTypeId: archivesData.value.basicInfo.id,
    id:
      dialogData.selectKeypointInfo.id != ""
        ? dialogData.selectKeypointInfo.id
        : "",
  };
  PostAddFormInfo(params)
    .then((res) => {
      if (res.code === 0) {
        ElMessage.success("数据写入成功");
        preStepFun();
        // getKeyPointCategoryList();
      }
    })
    .catch((err) => {
      ElMessage.error("保存失败,请再次尝试保存重点点位信息~");
    });
};

// 下一步
const nextStepFun = () => {
  if (!dialogData.selectKeypointId) {
    ElMessage.warning("请至少选择一个重点点位类目~");
  } else {
    dialogData.step = 2;
    fApi.value = {}
    nextTick(()=>{
      getSelectPointList();
    })
    
  }
};
const handlerBasicinfoFun = () => {
  let data = {
    data: archivesData.value.basicInfo,
    targetRow: archivesData.value.data,
    show: true,
    type: "edit",
  };
  emit("editData", data);
};
const sortAction = () => {
  centerDialogVisible.value = true
}
const deleteBasicFun = () => {
  let { id, title } = archivesData.value.data;
  ElMessageBox.confirm(`确定要删除${title}吗？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      deletePointById(id).then((res) => {
        if (res.code === 0) {
          ElMessage.success("删除成功");
          cancel();
        }
      });
    })
    .catch(() => {
      console.log("已取消");
    });
};
//加载重点点位数据以及表单
const getSelectPointList = () => {
  GetSelectPointList({
    id: dialogData.selectKeypointId,
    jcxxId: archivesData.value.data.id,
  })
    .then((res) => {
      if (res.code === 0) {
        let point = res.data.point;
        // 动态表单的结构数据
       let datas = new FormCreateUtil(fApi.value, false).formatRules(
          res.data.info || []
        );
        const lastData = datas.map(item => ({
            ...item,
            value: item.type==='InputNumber'?Number(item.value):item.value
          }));
        rules.value =lastData 
        //查出来有点位列表进行数据筛选 - 选出档案-对应目标档案 - 对应点位类目下的点位列表
        if (point?.length) {
          dialogData.activekeypointsList = point.filter(
            (i) =>
              i.parentTypeId === archivesData.value.basicInfo.id &&
              (i.jcxxId != null ? archivesData.value.data.id : true)
          );
          if (
            !dialogData.activekeypointsList[0].data.mingcheng &&
            (dialogData.activekeypointsList[0].data.xingming ||
              dialogData.activekeypointsList[0].data.title)
          ) {
            dialogData.activekeypointsList[0].data.mingcheng ==
            dialogData.activekeypointsList[0].data.xingming
              ? dialogData.activekeypointsList[0].data.xingming
              : dialogData.activekeypointsList[0].data.title;
          }
          //选择目标点位塞入数据，反之，显示表单模板
          if (
            Object.keys(dialogData.selectKeypointInfo).length &&
            dialogData.selectKeypointInfo.id != ""
          ) {
            let detailData = null;
            option.value.submitBtn.innerText = "保存";
            detailData = dialogData.activekeypointsList.filter(
              (i) => i.id == dialogData.selectKeypointInfo.id
            );
            let detailDataObj = detailData.length ? detailData[0].data : {};
            // 处理图片和视频前缀
            let prefix = '/imgPath';
            let arrs = []
            let arrs1 = []
            if(detailDataObj.photo&&detailDataObj.photo?.length){
              detailDataObj.photo.forEach(item=>{
                if(item.includes('/imgPath')){
                  arrs.push(`${item}`)
                }else{
                  arrs.push(`${prefix}${item}`)
                }
              })
            }
            if(arrs?.length){
              detailDataObj.photo = arrs
            }
            if(Array.isArray(detailDataObj.video)){
              if(detailDataObj.video?.length){
                detailDataObj.video.forEach(item=>{
                if(item.includes('/imgPath')){
                  arrs1.push(`${item}`)
                }else{
                  arrs1.push(`${prefix}${item}`)
                }
              })
              }
            }else{
              if(detailDataObj.video){
                arrs1 =[detailDataObj.video]
              }
             
            }
            if(arrs1?.length){
              detailDataObj.video = [arrs1[0]]
            }
            //处理基本情况的名称
            let detailParseArr = ["canting", "guibinxiuxishi", "huiyishi"];
            detailParseArr.forEach((item) => {
              if (Object.keys(detailDataObj).includes(item)) {
                detailDataObj[item + "mingcheng"] =
                  detailDataObj[item].mingcheng;
                detailDataObj[item + "zuowei"] = detailDataObj[item].zuowei;
              }
            });
            //处理单位意见/二次核查对比情况
            if (detailDataObj["danweiyijian"]) {
              detailDataObj["danweiyijian"] = detailDataObj["danweiyijian"]
                .map((item) => {
                  return item.danweiyijian;
                })
                .join("|");
              detailDataObj["ercihechaduibiqingkuang"] = detailDataObj[
                "ercihechaduibiqingkuang"
              ]
                .map((item) => {
                  return item.ercihechaduibiqingkuang;
                })
                .join("|");
            }
            nextTick(() => {
              let datas = { ...fApi.value, ...detailDataObj }
              console.log(datas)
              fApi.value = datas
            });
          } else {
            option.value.submitBtn.innerText = "新增";
          }
        } else {
          dialogData.activekeypointsList = [];
          option.value.submitBtn.innerText = "新增";
        }
        /**视频预览处理 */
        setTimeout(() => {
            // 使用 jQuery 替换 img 标签为 video 标签
            let videoHtml = $('._fc-upload ._fc-exceed').find('.el-upload-list__item-thumbnail')
          console.log(fApi.value)
          let videoUrl = fApi.value.video&&fApi.value.video.length>0?fApi.value.video[0]:''
          let vUrl = videoUrl.includes('/imgPath')?videoUrl:'/imgPath'+videoUrl
        $(videoHtml).each((index, img) => {
          const video = $('<video controls></video>')
            .attr('src', vUrl)
            .attr('alt', img.alt)
            .css({
              width: '100%',
              height: '100%'
            });
          $(img).replaceWith(video);
        });
          // let files = $('._fc-upload[type="video"]').children(".fc-files");
          // files.each(function (index, file) {
          //   let img_thumbnail = $(file).children("img");
          //   let url = img_thumbnail.attr("src");
          //   // 利用canvas + video 处理 缩略图显示
          //   const videoElement = document.createElement("VIDEO");
          //   videoElement.setAttribute("crossorigin", "Anonymous");
          //   videoElement.preload = true;
          //   videoElement.autoplay = true;
          //   videoElement.muted = true;
          //   videoElement.currentTime = 3;
          //   const callBack = () => {
          //     const { videoWidth, videoHeight } = videoElement; // 获取video的宽高
          //     const canvas = document.createElement("canvas");
          //     canvas.width = videoWidth;
          //     canvas.height = videoHeight;
          //     const ctx = canvas.getContext("2d");
          //     ctx.drawImage(videoElement, 0, 0, videoWidth, videoHeight);
          //     const dataBase64 = canvas.toDataURL("image/png"); // 完成base64图片的创建
          //     img_thumbnail.attr("src", dataBase64);
          //   };
          //   videoElement.onloadeddata = setTimeout(() => {
          //     callBack();
          //   }, 1000);
          //   // let relativePath = new URL(url).pathname;
          //   let relativePath = url;
          //   let baseUrl = UPLOAD_BASE_URL;
          //   url = baseUrl + relativePath;
          //   videoElement.src = url;
          //   // 处理放大图得显示
          //   $(file)
          //     .find(".el-icon-view")
          //     .on("click", function (e) {
          //       setTimeout(() => {
          //         let img = $(".el-dialog__wrapper")
          //           .find(".el-dialog__body")
          //           .children('[alt="example"]');
          //         img.attr("src", img_thumbnail.attr("src"));
          //       }, 0);
          //     });
          // });
        }, 100);
      }
    })
};
//加载重点点位分类类目
const getKeyPointCategoryList = () => {
  let basicInfoId = archivesData.value.data["id"];
  let params = {
    jcxxId: basicInfoId,
    parentId: archivesData.value.basicInfo.id,
  };
  getArchivesInfoList(params).then((res) => {
    if (res.code === 0) {
      let archieveList = res.data;
      dialogData.keypointsList = archieveList.filter(
        (i) => (i.archives_name != "高速基本情况"&&i.archives_name != "高铁基本情况"&&i.archives_name != "现场基本情况"&&i.archives_name != "住地基本情况")
      );
      dropData.value = archieveList.filter(
        (i) => (i.archives_name != "高速基本情况"&&i.archives_name != "高铁基本情况"&&i.archives_name != "现场基本情况"&&i.archives_name != "住地基本情况")
      );
    }
  });
};
//切换重点点位类目选项
const onChangeKeyPointCategoryFun = (id) => {
  fApi.value = {}
  dialogData.selectKeypointId = id;
  dialogData.selectKeypointInfo = {};
  getSelectPointList();
};
//切换点位列表
const onChangeSelectKeypointDetailFun = (option) => {
  if (dialogData.selectKeypointInfo.id === option.id) {
    dialogData.selectKeypointInfo = {};
  } else {
    dialogData.selectKeypointInfo.id = option.id;
  }
  getSelectPointList();
};
//删除点位某一项
const delKeypointFun = (option) => {
  ElMessageBox.confirm(`此操作将永久删除该点位, 是否继续?`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      deleteFormInfo(option.id).then((res) => {
        if (res.code === 0) {
          ElMessage.success("删除重点点位成功~");
          if (dialogData.selectKeypointInfo.id === option.id) {
            dialogData.selectKeypointInfo = {};
          }
          getSelectPointList();
        }
      });
    })
    .catch(() => {
      console.log("已取消");
    });
};
onMounted(() => {
  emitter.on("eventKeypointDialog", (res) => {
    console.log("刷新资源页面666", res);
    archivesData.value = res;
    if (archivesData.value.data) {
      dialogData.title = archivesData.value.data.title;
    } else {
      dialogData.title = "";
    }
    getKeyPointCategoryList();
  });
  onBeforeUnmount(() => {
    emitter.off("eventKeypointDialog");
  });
  onUnmounted(() => {
    emitter.off("eventKeypointDialog");
  });
});
</script>

<style scoped lang="scss">
:deep(._Dialog) {
  background: none !important;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  margin-top: 0;
  margin-left: 200px;
  .el-dialog__header {
    display: none !important;
  }
  .el-dialog__footer {
    padding-top: 0px !important;
  }
}
// :deep(.dialog-footer) {
//   display: flex;
//   justify-content: center;
//   padding-right: 20px;
//   .sub_btn {
//     width: 80px;
//     height: 32px;
//     line-height: 32px;
//     text-align: center;
//     background: rgba(31, 76, 152, 0.87);
//     border-radius: 4px 4px 4px 4px;
//     cursor: pointer;
//   }
//   .sure_btn {
//     margin-left: 16px;
//     background: #274eef;
//   }
// }
:deep(.my_Dialog) {
  background: url("../../assets/panel/弹窗bg.png") no-repeat;
  background-size: 100% 100%;
  padding: 20px 32px 37px 32px;
  .heard_name {
    background: url("../../assets/panel/right_panel.png") no-repeat;
    background-size: contain;
    height: 28px;
    // display: flex;
    // align-items: center;
    .d_name {
      font-family: YouSheBiaoTiHei;
      font-weight: 400;
      font-size: 17px;
      color: #ffffff;
      height: 22px;
      line-height: 20px;
      padding-left: 20px;
    }
  }
  .el-dialog__headerbtn:active {
    background-color: transparent !important;
    outline: none !important;
    box-shadow: none !important;
  }
  .map_box {
    display: flex;
    align-items: center;
    .mapMarker {
      width: 80px;
      height: 26px;
      background: #274eef;
      border-radius: 2px;
      line-height: 26px;
      text-align: center;
      font-family: PingFang SC;
      font-weight: 400;
      font-size: 14px;
      color: #ffffff;
      cursor: pointer;
    }
    .customBtn {
      background: #274eef;
      opacity: 0.5;
      height: 26px;
      border: 0px;
      width: 80px;
      margin-left: 10px;
    }
    .point {
      font-family: PingFang SC;
      font-weight: 400;
      font-size: 14px;
      color: #ffffff;
      margin-left: 22px;
      opacity: 0.8;
    }
  }
  .el-input-number {
    width: 180px;
    .el-input {
      height: 30px !important;
      border-radius: 0px !important;
      border: none !important;
    }
    .el-input__wrapper {
      // background: none !important;
      box-shadow: #dcdfe6 !important;
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
}
:deep(.el-dialog__headerbtn) {
  position: absolute;
  top: 10px;
  right: 20px;
  .el-dialog__close {
    color: #fff;
    font-size: 20px;
  }
}
:deep(.el-dialog__headerbtn :focus, .el-dialog__headerbtn:hover) {
  outline: none;
  box-shadow: none;
}
/* 解决按钮黑边框bug */
:deep(.el-button:focus) {
  outline: none;
}
:deep(.el-dialog__body) {
  padding-right: 30px;
}
:deep(.dialog-footer) {
  display: flex;
  justify-content: end;
  padding-right: 20px;
  .sub_btn {
    width: 88px;
    height: 36px;
    line-height: 36px;
    text-align: center;
    background: rgba(31, 76, 152, 0.87);
    border-radius: 4px 4px 4px 4px;
    cursor: pointer;
  }
  .sure_btn {
    margin-left: 16px;
    background: #274eef;
  }
}
:deep(.customForm) {
  width: 80%;
  margin: 20px auto;
  .el-form-item__label {
    font-weight: 400;
    font-size: 14px;
    color: #ffffff;
    opacity: 0.8;
  }
  .el-form-item__content {
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
    .el-input-number {
      .el-input__wrapper {
        box-shadow: #dcdfe6 !important;
      }
      .el-input__inner {
        border: 1px solid #eee;
        height: 28px;
      }
    }
  }
}
:deep(.el-textarea__inner) {
  background: rgba(0, 12, 78, 0.5);
  box-shadow: none;
  border: 1px solid #5b6799;
}
:deep(.el-select__wrapper) {
  background: rgba(0, 12, 78, 0.5);
  box-shadow: none;
  border: 1px solid #5b6799;
  .el-select__selected-item {
    color: #ffffff;
    opacity: 0.8;
  }
  .is-transparent {
    color: #a8abb2;
    // opacity: 0.8;
  }
}
:deep(.el-date-editor) {
  background: rgba(0, 12, 78, 0.5);
  box-shadow: none;
  border: 1px solid #5b6799;
  .el-range-input,
  .el-range-separator {
    font-size: 14px;
    color: #ffffff;
    opacity: 0.8;
  }
}
:deep(.el-textarea__inner) {
  color: #fff;
  opacity: 0.8;
}
:deep(.el-input-number) {
  .el-input__wrapper {
    box-shadow: #dcdfe6 !important;
    background: rgba(0, 12, 78, 0.5) !important;
  }
}
:deep(._fc-upload) {
  .el-upload {
    width: 58px;
    height: 58px;
    .el-icon {
      width: 20px;
      height: 20px;
    }
  }
}
:deep(.el-upload-list--picture-card .el-upload-list__item){
  width: 58px;
  height: 58px;
}
:deep(.fc-submit-btn) {
  width: 100px;
  height: 36px;
  margin-left: 100px;
}
.dark_nav_wrap {
  // background-color: #094a82;
  padding: 10px 25px 10px 10px;
  .nav-body {
    display: flex;
    justify-content: space-between;
    align-items: center;
    color: #fff;
    .nav-body-desc {
      padding-right: 20px;
      padding-left: 8px;
    }
  }
}

.basicInfoList {
  display: -webkit-box;
  overflow-y: hidden;
  overflow-x: auto;
  padding-bottom: 10px;
  margin: 16px 0;
  display:flex;
  &-item {
    height: 42px;
    border-radius: 4px;
    background-color: #ffffff;
    font-size: 14px;
    border: 1px solid #edf1f6;
    margin-right: 16px;
    cursor: pointer;
    min-width: 120px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    span {
      flex: 1;
      height: inherit;
      line-height: 42px;
      &:first-child {
        padding-right: 1rem;
      }
    }
    i {
      height: inherit;
      line-height: 42px;
    }

    &.active {
      color: #196bbd;
      border-color: #196bbd;
    }
  }
  :deep(.info_active){
    .el-tag__content{
      color:#333;
    }
  }
  :deep(.success_active){
    .el-tag__content{
      color: #196bbd;
    }
  }
}

.keypointsList {
  padding: 14px 14px 0;
  display: flex;
  flex-wrap: wrap;

  &-item {
    width: 50%;
    box-sizing: border-box;
    cursor: pointer;
    margin-bottom: 8px;
    &:nth-child(2n) {
      margin-right: 0;
    }
    .keypointsList-item-contain {
      color: #fff;
      height: 36px;
      display: flex;
      align-items: center;
      padding: 0 10px;
      margin-right: 10px;
    }
    &.active .keypointsList-item-contain {
      color: #0266c2;
      font-weight: 700;
      border-color: #196bbd;
    }

    &-contain {
      display: flex;
      justify-content: space-between;
      border-radius: 4px;
      font-size: 14px;
      border: 1px solid #edf1f6;

      .keyNum {
        color: #a3b4cc;
        font-size: 14px;
      }
    }
  }
}

/* 双列网格布局 */
.grid-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
}

.item_li {
  height: 40px;
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: move;
  transition: all 0.3s;
  font-size: 14px;
}

/* 拖拽动画 */
.list-move {
  transition: transform 0.3s ease;
}
</style>
