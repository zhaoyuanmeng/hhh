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
      width="30%"
      @close="cancel"
      align-center
      :destory-on-close="false"
      :close-on-click-modal="false"
      class="my_Dialog"
    >
      <template #header>
        <div class="heard_name">
          <div class="d_name">{{ DialogData.title }}</div>
        </div>
      </template>
      <div class="customForm">
        <FormCreate v-model="fApi" :rule="rules" :option="option" />
      </div>
    </el-dialog>
  </div>
</template>
<script setup>
import {
  reactive,
  ref,
  getCurrentInstance,
  onMounted,
  onBeforeUnmount,
  onUnmounted,
  nextTick,
} from "vue";
import {
  GetBasicinfoById,
  PostBasicInfoList,
  PostAddFormItemInfo,
} from "@/api/index";
import FormCreateUtil from "@/utils/dynamicForm";
import emitter from "@/utils/emitter";
import { ElMessage } from "element-plus";
const { proxy } = getCurrentInstance();
const emit = defineEmits(["hideDialog"]);
const openBol = ref(true);
let fApi = ref({});
let rules = ref([]);
let option = ref({
  // 显示重置表单按扭
  resetBtn: false,
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
  // 表单提交按扭事件
  onSubmit: (formData) => {
    // alert(JSON.stringify(formData));
    submitFormData(formData);
    console.log("获取表单中的数据：", formData);
    // emit('submitForm', formData)
    //按钮进入提交状态
    // this.fApi.btn.loading();

    //重置按钮禁用
    //   this.fApi.resetBtn.disabled();

    //按钮进入可点击状态
    //   this.fApi.btn.finish();
  },
});
const DialogData = reactive({
  archiveId: "", // 档案Id
  title: "", // 档案名称
  targetArchive: {}, // 档案信息
  archiveType: "add", // 档案类型
  childTypeId: "", // 子集基本信息id
});

const cancel = () => {
  emit("hideDialog");
};

// 校验规则文件（如 src/utils/validateRules.js）
 const longitudeRule = (value) => {
  const reg = /^(-|\+)?(((\d|[1-9]\d|1[0-7]\d|0{1,3})\.\d{0,15})|(\d|[1-9]\d|1[0-7]\d|0{1,3})|180\.0{0,15}|180)$/;
  return reg.test(value)
};

 const latitudeRule = (value) => {
  const reg = /^(-|\+)?([0-8]?\d{1}\.\d{0,15}|90\.0{0,15}|[0-8]?\d{1}|90)$/;
  return reg.test(value)
};

// 提交
const submitFormData = (e) => {
  console.log(e)
  let data = e;
  let params = {};
  if (DialogData.archiveType == "add") {
    params = {
      data,
      childTypeId: DialogData.childTypeId,
      parentTypeId: DialogData.archiveId,
    };
  } else if (DialogData.archiveType == "edit") {
    params = {
      data,
      childTypeId: DialogData.childTypeId,
      id: DialogData.targetArchive.id,
    };
  }
  if(e.jingdu||e.weidu){
    let str = longitudeRule(Number(e.jingdu))
    let str1 = latitudeRule(Number(e.weidu))
    console.log(str,str1)
    if(str&&str1){
    }else{
      return ElMessage.warning('经纬度有误，请重新填写')
    }
  }
  PostAddFormItemInfo(params).then((res) => {
    if (res.code === 0) {
      ElMessage.success("数据写入成功");
      // TODO 刷新列表
      cancel();
    }
  });
};
// 新增
const getBasicinfoById = (archiveId) => {
  // 获取接口
  GetBasicinfoById(archiveId).then((res) => {
    console.log(res);
    if (res.code === 0) {
      let data = res.data;
      if (data?.length) {
        DialogData.childTypeId = data[0].archives_id;
        data.forEach((item) => {
          if (item.field_name === "名称") {
            item.field_id = "title";
          }
        });
      }
      rules.value = new FormCreateUtil(fApi.value, false).formatRules(
        res.data || []
      );
    }
  });
};
// 编辑回显
const getBasicInfoDetail = () => {
  if (DialogData.archiveType == "edit") {
    PostBasicInfoList({ parentTypeId: DialogData.archiveId }).then((res) => {
      if (res.code === 0) {
        let detailData = res.data.find(
          (item) => item.id == DialogData.targetArchive.id
        );
          // 处理图片和视频前缀
          let prefix = '/imgPath';
            let arrs = []
            let arrs1 = []
            if(detailData.data.photo&&detailData.data.photo?.length){
              detailData.data.photo.forEach(item=>{
                if(item.includes('/imgPath')){
                  arrs.push(`${item}`)
                }else{
                  arrs.push(`${prefix}${item}`)
                }
              })
            }
            if(detailData.data.video&&Array.isArray(detailData.data.video)){
              if(detailData.data.video?.length){
                detailData.data.video.forEach(item=>{
                if(item.includes('/imgPath')){
                  arrs1.push(`${item}`)
                }else{
                  arrs1.push(`${prefix}${item}`)
                }
              })
              }
            }
            if(Array.isArray(detailData.data.video)){
              if(detailData.data.video?.length){
                detailData.data.video.forEach(item=>{
                if(item.includes('/imgPath')){
                  arrs1.push(`${item}`)
                }else{
                  arrs1.push(`${prefix}${item}`)
                }
              })
              }
            }else{
              if(detailData.data.video){
                arrs1 =[detailData.data.video]
              }
             
            }
            if(arrs?.length){
              detailData.data.photo = arrs
            }
            if(arrs1?.length){
              detailData.data.video = [arrs1[0]]
            }
        nextTick(() => {
          // 图片回显
          fApi.value = { ...fApi.value, ...detailData.data };
          console.log(fApi.value)
        });
      }
    });
  }
};
onMounted(() => {
  emitter.on("eventBasicinfoDialog", (res) => {
    console.log("刷新资源页面666", res);
    DialogData.archiveId = res.data.id;
    DialogData.targetArchive = res.targetRow;
    DialogData.archiveType = res.type;
    getBasicinfoById(DialogData.archiveId);
    if (res.type === "add") {
      DialogData.title = "新增" + res.data.name;
    } else {
      DialogData.title = "编辑" + res.data.name;
      getBasicInfoDetail();
    }
  });
  onBeforeUnmount(() => {
    emitter.off("eventBasicinfoDialog");
  });
  onUnmounted(() => {
    emitter.off("eventBasicinfoDialog");
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
</style>
