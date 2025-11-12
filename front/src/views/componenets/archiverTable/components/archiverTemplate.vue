<template>
  <!-- 你的模板内容 -->
  <render-table />
  <el-dialog
    v-model="centerDialogVisible"
    title="预览图片"
    width="1200"
    align-center
  >
  <el-image style="width: 1160px; height: 700px" :src="url" fit="cover" :preview-src-list="srcList"/>
    </el-dialog>
    <!-- 视频播放 -->
    <Mp4Play v-if="showMp4" :url="videoSrc"/>
</template>
<script setup>
import { ref, onMounted, h, watch,computed   } from 'vue';
import { archiverData } from './archiverData';
import Mp4Play from '@/components/mp4Play'
import { ElTable, ElTableColumn, ElMessage, ElImage } from 'element-plus';
import useWorkCockpitStore from '@/store/modules/workCockpit.js'
let showMp4 = computed(()=>useWorkCockpitStore().showMp4)
const props = defineProps({
  templateData: {
    type: Object,
    default: () => ({})
  }
});
const centerDialogVisible = ref(false)
const url = ref('')
const srcList = ref([])
const videoSrc = ref('')
const archiver = ref(archiverData);
const handlePreview = (src,arr) => {
 let list = arr.map(item => {
if (item.includes('/imgPath')) {
return item;
} else {
return '/imgPath'+item
}
 })
 console.log(list)
 srcList.value = list
  url.value = src
  centerDialogVisible.value = true
}
const play = (src) => {
  console.log(src)
  if (typeof src === 'string') {
    videoSrc.value = src.includes('/imgPath')?src:'/imgPath'+src
  }
  if (Array.isArray(src)) {
    if(src?.length){
      videoSrc.value = src[0].includes('/imgPath')?src[0]:'/imgPath'+src[0]
    }
  }
  useWorkCockpitStore().set_showMp4(true)
}
const recursionArchiver = (columns, arr) => {
  for (let i = 0; i < columns.length; i++) {
    let column = columns[i];
    if (column.hasOwnProperty("prop")) {
      if (column.prop === 'photo') {
        // arr.push(h(ElTableColumn, {
        //   prop: column.prop,
        //   label: column.label,
        //   // 关键修复：使用函数返回插槽内容
        //   vSlots: {
        //     default: (scope) => {
        //       const imgList = scope.row[column.prop][0] || [];
        //       if (imgList?.length > 0&& imgList) {
        //         const fullPath = `/imgPath${imgList}`;
        //         return h(ElImage, {
        //           src: fullPath,
        //           previewSrcList: [fullPath],
        //           style: { width: '50px', cursor: 'pointer' },
        //           previewZIndex: 2800 // 修复层级问题
        //         });
        //       }else{
        //         return h('span', { style: { color: '#fff' } }, '暂无图片');
        //       }
        //     }
        //   }
        // })
        // )
        arr.push(h(ElTableColumn, {
          prop: column.prop,
          label: column.label,
        }, {
          default: (scope) =>{
            // 获取图片路径数组（兼容空值）
            const imgList = scope.row[column.prop][0] || [];
            const imgArr = scope.row[column.prop]
               // 判断是否存在有效图片
               if (imgList.length > 0 && imgList) {
                const fullPath = imgList.includes('/imgPath')?imgList:`/imgPath${imgList}`;
                return h(ElImage, {
                  src: fullPath,
                  style: { width: '50px', cursor: 'pointer' },
                  previewZIndex: 10000, // 关键参数
                  appendToBody: true, // 关键！突破父容器限制
                  // previewSrcList: imgArr,
                  onClick: () =>  handlePreview(fullPath,imgArr)
                });
              } else {
                // 无图片时显示占位符
                return h('div', { style: { color: '#fff' } }, '暂无图片');
              }
          } 
        }));
      } else {
        if (column.prop === 'video'){
          arr.push(h(ElTableColumn, {
          prop: column.prop,
          label: column.label,
        }, {
          default: (scope) =>{
            // 获取图片路径数组（兼容空值）
            const imgList = scope.row[column.prop] || [];
               // 判断是否存在有效图片
               if (imgList.length > 0 && imgList) {
                return h('div', { style: { color: '#3AA8FC',cursor:'pointer' },onClick: () =>  play(imgList) }, '播放视频');
              } else {
                // 无图片时显示占位符
                return h('div', { style: { color: '#fff' } }, '暂无视频');
              }
          } 
        }));
        }else{
          arr.push(h(ElTableColumn,
          {
            prop: column.prop,
            label: column.label,
            // 可以根据需要添加更多属性，例如 width
          }));
        }
      }
    } else {
      let children = [];
      recursionArchiver(column.children, children);
      arr.push( h(ElTableColumn, {
          label: column.label
        }, () => children));
    }
  }
};

const renderTable = () => {
  let { tableData = [], archiverName = "", archiverItemName = "" } = props.templateData;
  if (archiverName === "" || archiverItemName === "") {
    return;
  }
  let columns = archiver.value[archiverName][archiverItemName];

  if (!columns) {
    // ElMessage.warning(`未找到表头-${archiverName}-${archiverItemName}`)
    console.log("未找到表头", archiverName, archiverItemName);
    return;
  }
  let columnsArr = [];
  let copyColumns = JSON.parse(JSON.stringify(columns));
  console.log(copyColumns, '80980980')
  recursionArchiver(copyColumns, columnsArr);
  console.log(tableData, columnsArr)
  return h(
    ElTable, { data: tableData, style: { width: '100%' } }, columnsArr
  );
};

onMounted(() => {
  // 生命周期钩子，组件挂载后执行的代码
  // render()
});
watch(
  () => props.templateData,
  (val) => {
    if (val && val.tableData?.length) {
      render()
    }
  },
  { deep: true }
);
// 在 setup 中定义渲染函数
const render = () => {
  return renderTable();
};

// // 使用 defineExpose 暴露渲染函数，如果需要在外部访问
// defineExpose({
//   render
// });
</script>