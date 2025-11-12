<template>
  <div class="content-box">
    <div class="content-header">勤务工作指导思想</div>
    <!-- <el-scrollbar> -->
      <div class="content">
        <div style="width:100%;height:200px;cursor: pointer;" @click="centerDialogVisible = true">
          <el-carousel  height="auto" arrow="hover">
    <el-carousel-item v-for="(item,index) in showImgList" :key="index" class="banna_box" style="height:200px">
      <el-image style="width: 100%; height: 100%" :src="item" fit="contain" />
    </el-carousel-item>
  </el-carousel>
        </div>
        <div class="text_box_content">
          <div class="para">
          以新时代中国特色社会主义思想为指导，坚决贯彻“以人民为中心”发展思想和“外松内紧”重要指示，严格执行中央八项规定及实施细则和中办23号文件精神，坚持“党委政府统·领导、公安机关集中指挥、特勤部门牵头组织、相关部门协同配合”工作机制，突出“属地主责、分工负责、精千高效？原则，以防疫情、防暴恐、防爆炸、防冲撞、防投掷、防非正常访和极端访、防个人极端行为、防公共安全事故、防反宣标语条幅传单、防负面奥情发酵炒作等为重点，强化“万无一失、一失万无”的标准和“细致、精致、极致”的作风，加强统筹协调、组织指挥、督导检查，强化联勤联动，严密安全措施、防疫措施，确保警卫对象绝对安全，确保警卫形式自然缓和，确保警卫对象和人民群众“双满意”，切实达到安全效果、政治效果和社会效果的高度统一。
        </div>
        </div>
       
      </div>
    <!-- </el-scrollbar> -->
  </div>
  <div class="change2d" @click="changMap">
    <div class="icon_name">{{mapType==='3D'?'2D':'3D'}}</div>
    <!-- <div class="text">场景切换</div> -->
  </div>
  <VectorRoadMap :styles="style"/>
  <div class="changeJG" @click="openJingGai" :class="{'activeDiv':jgBol}">
    <div class="icon_name">井盖</div>
  </div>
  <div class="changeGL" @click="openGL" :class="{'activeDiv':glBol}">
    <div class="icon_name">管廊</div>
  </div>
  <el-dialog v-model="centerDialogVisible" 
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      append-to-body
      class="my_Dialog_basic" width="1000" center>
      <template #header>
      <div class="heard_name">
        <div class="d_name">{{ titleName }}</div>
      </div>
    </template>
    <div class="content_dialog">
      <el-carousel  height="auto" arrow="always" @change="changeCard">
    <el-carousel-item v-for="(item,index) in imgList" :key="index" class="banna_box" style="height:600px">
      <el-image style="width: 100%; height: 100%" :src="item.icon" fit="fill" />
    </el-carousel-item>
  </el-carousel>
    </div>
    
  </el-dialog>
</template>

<script setup>
import VectorRoadMap from "@/components/vectorRoadMap"
import imageSource1 from '@/assets/basic/img@1x.png';
import imageSource2 from '@/assets/basic/img@2x.png';
import imageSource3 from '@/assets/basic/img@3x.png';
import imageSource4 from '@/assets/basic/img@4x.png';
import imageSource5 from '@/assets/basic/img@5x.png';
import titleImg from '@/assets/basic/image@1x.png';
import title1 from '@/assets/basic/图1@1x.png';
import title2 from '@/assets/basic/图2@1x.png';
import title3 from '@/assets/basic/图3@1x.png';
import { ref,computed } from 'vue'
import { handleTree,loadPicture } from "@/utils";
import useMapStore from '@/store/modules/mapStore.js'
import { drawGsGtData } from "@/components/SmartMap/js/utils";
import { init } from "@/components/SmartMap/js/map";
import {getJGdata} from '@/api/basic/index'
let mapType = computed(()=>useMapStore().mapType)
let centerDialogVisible = ref(false)
let titleName = ref('背景介绍')
let jgBol = ref(false)
let glBol = ref(false)
const style = ref({
  bottom:'240px',
  right:'340px'
})
const jgSrc = loadPicture("./images/basic/jtjg.png")
let showImgList = ref([title1,title2,title3])
let imgList = ref([{name:'背景介绍',icon:imageSource1},{name:'背景介绍',icon:imageSource2},{name:'背景介绍',icon:imageSource3},{name:'背景介绍',icon:imageSource4},{name:'背景介绍',icon:imageSource5}])
const changMap = () => {
  if(mapType.value==='3D'){
    useMapStore().SETMAPTYPE('2D')
    console.log('切换成2d场景')
    changMapModal('2D')
  }else{
    useMapStore().SETMAPTYPE('3D')
    console.log('切换成3d场景')
    changMapModal('3D')
  }
}
const openJingGai = () => {
  let g = window.__g
  if(!jgBol.value){
    jgBol.value = true
    // 绘制井盖数据
    getJGdata({}).then(res=>{
      console.log(res)
      addJGmarker(res.data)
    })
  }else{
    jgBol.value = false
    // 清除井盖数据
    g.marker.deleteByGroupId('JG')
  }

}
const openGL = () => {
  if(!glBol.value){
    // 打开管廊
    glBol.value = true
    showHideLayerFun(true)
  }else{
    // 隐藏管廊
    glBol.value = false
    showHideLayerFun(false)
  }
  
}
const showHideLayerFun= async(bol)=> {
  // bol true开启地下模式 false 关闭地下
  let g = window.__g
  let res =  await g.infoTree.get()
  let treeLayer = handleTree(res.infotree, 'index', 'parentIndex')[0].children
  let initData = treeLayer.filter((item) => item.visiblity)
  let dxData = treeLayer.filter((item) => item.name === '地下空间')
  let dsData = initData
  let hideData = dxData
  if (bol) {
    for (const item of dsData) {
       g.infoTree.hide(item.iD)
    }
    for (const item1 of hideData) {
       g.infoTree.show(item1.iD)
    }
    await g.marker.hideAll(); // 隐藏所有的marker
    if(jgBol.value){
      await g.marker.showByGroupId('JG')
    }
    await g.customObject.clear();
    await g.tag.clear() // 清楚所以Tag
    await g.polyline.clear() // 清除线
    await g.polygon.clear();// 清除面
    await g.polygon3d.clear();// 清除面
    await g.marker3d.clear()// 清除
     g.camera.set(493389.413523, 4323697.050156, 3365.91625, -74.007416, -87.579132, 0.4)
  }
  if (!bol) {
    for (const item of dsData) {
      await g.infoTree.show(item.iD)
    }
    for (const item1 of hideData) {
      await g.infoTree.hide(item1.iD)
    }
    await g.reset(2 | 4);
    await g.marker.showAll(); // 显示所有的marker
    if(!jgBol.value){
      await g.marker.hideByGroupId('JG')
    }
    await g.infoTree.hide(window.shapeFile) // 隐藏shepfile点位
    await g.infoTree.hide(window.shepfileCommunity) //隐藏所有的shepfile
  }
}
const changeCard = (e,i) => {
  titleName.value = imgList.value[e].name
}
const changMapModal = async(type) => {
  let g = window.__g
  let res = await g.infoTree.get();
  let treeLayer = handleTree(res.infotree, 'index', 'parentIndex')
  console.log(treeLayer)
    if(type==='2D'){
      g.infoTree.hide('ProjectTree_Root')
      g.infoTree.hide('6D6C753E4356603BB3B3B98D25C90F13') // 商服
      g.infoTree.hide('8C6F67F84F71F11130C1BEACD0BAF612')
      g.infoTree.hide('A3B755BF43736C15455300A0FB44761F')
      g.infoTree.hide("8B6725594EA91CCA8431338C12C4399F");
      drawGsGtData(0)
      g.polygon3d.clear()
      g.polygon.clear();// 清除面
    }else{
       g.reset( 2 |4);
       g.infoTree.show('6D6C753E4356603BB3B3B98D25C90F13') // 商服
       g.infoTree.show('8C6F67F84F71F11130C1BEACD0BAF612')
       g.infoTree.show('A3B755BF43736C15455300A0FB44761F')
       g.infoTree.show("8B6725594EA91CCA8431338C12C4399F");
       drawGsGtData(1)
       init()
    }
}
const extractImagePath =(url)=> { const parts = url.split('/images/'); return parts.length > 1 ? '/images/' + parts[1] : null; }
// 绘制场景标绘数据
const keepImagesAndAfter = (url) => {
  return extractImagePath(url)
};
// 添加井盖
const addJGmarker = async(arr) => {
  let g = window.__g;
  let counter = 0;
  let markers = []
  for(const item of arr){
    var tagObject = {
    id: `jg${new Date().getTime()}${++counter}`,
    userData: '',
    groupId: "JG",
    coordinate: [Number(item.lng),Number(item.lat),1], //坐标位置
    coordinateType: 1,//默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
    anchors: [-15, 34], //锚点，设置Marker的整体偏移，取值规则和imageSize设置的宽高有关，图片的左上角会对准标注点的坐标位置。示例设置规则：x=-imageSize.width/2，y=imageSize.height
    imageSize: [30, 34], //图片的尺寸
    range: [0, 180000], //可视范围
    imagePath: HostConfig.ImagePath + keepImagesAndAfter(jgSrc),
    fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
    text: item.addr, //显示的文字
    useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
    textRange: [1, 20], //文本可视范围[近裁距离, 远裁距离]
    textOffset: [-34, -30], // 文本偏移
    textBackgroundColor: [0,0,0,0.75], //文本背景颜色
    fontSize: 12, //字体大小
    fontOutlineSize: 1, //字体轮廓线大小
    fontColor: Color.White, //字体颜色
    showLine: false, //标注点下方是否显示垂直牵引线
    lineSize: [1, 5], //垂直牵引线宽度和高度[width, height]
    lineColor: Color.SpringGreen, //垂直牵引线颜色
    lineOffset: [15, 0], //垂直牵引线偏移
    autoHeight: false, // 自动判断下方是否有物体
    displayMode: 1, //智能显示模式  开发过程中请根据业务需求判断使用四种显示模式
    priority: 0, //避让优先级
    occlusionCull: true, //是否参与遮挡剔除
  };
  markers.push(tagObject)
  }
  if(markers?.length){
    g.marker.add(markers);
  }
}
</script>

<style lang="scss" scoped>
@keyframes scroll-up {
    0% {
      transform: translateY(0);
    }
   100% {
    transform: translateY(-100%);
   } 
}
.content-box {
  width: 400px;
  height: 353px;
  background: linear-gradient(180deg, #0A1D64 0%, rgba(21, 30, 73, 0.6984) 100%);
  position: absolute;
  top: 72px;
  left: 10px;
  z-index: 20;
  transform: scaleX(0.8);
  transform-origin: left center;

  .content-header {
    width: 100%;
    height: 48px;
    line-height: 48px;
    text-align: center;
    font-family: YouSheBiaoTiHei;
    font-size: 24px;
    background: url("@/assets/workcockpit/header_bg.png") no-repeat;
    transform: scaleY(0.8);
  }

  .content {
    padding: 12px 20px 20px;
    line-height: 24px;
    transform: scaleY(0.8);
    transform-origin: left top;

    .title {
      font-family: YouSheBiaoTiHei;
      font-size: 18px;
    }
    .text_box_content{
      position: relative;
      height: 160px;
      overflow: hidden;
      width: 100%;
    }
    .para{  
      text-indent: 2em; /* 首行缩进2个字符宽度 */
      text-align: justify; /* 两端对齐 */
      position: absolute;
      top: 10px;
      width: 100%;
      animation: scroll-up 20s linear infinite;
      height: 250px;
    }
  }
}
.banna_box{
  height:100%;
  width:100%;
}
:deep(.el-scrollbar) {
  height: 80%;
}


.change2d {
  position: absolute;
  bottom: 15px;
  right: 340px;
  z-index: 20;
  width: 42px;
  height: 42px;
  opacity: 1;
  /* 自动布局 */
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  // padding: 5px;
  cursor: pointer;
  background: linear-gradient(180deg, rgba(10, 29, 100, 0.7) 0%, rgba(21, 30, 73, 0.6984) 100%);

  .icon_name {
    width: 42px;
    height: 42px;
    border-radius: 4px;
    opacity: 1;
    background: rgba(14, 31, 96, 0.5);
    box-sizing: border-box;
    border: 1px solid #5B759B;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #5B759B;
  }

  .text {
    opacity: 0.65;
    font-family: Source Han Sans;
    font-size: 12px;
    color: #fff;
    margin-top: 8px;
  }
}
.changeJG{
  position: absolute;
  bottom: 180px;
  right: 340px;
  z-index: 20;
  width: 42px;
  height: 42px;
  opacity: 1;
  /* 自动布局 */
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  // padding: 5px;
  cursor: pointer;
  background: linear-gradient(180deg, rgba(10, 29, 100, 0.7) 0%, rgba(21, 30, 73, 0.6984) 100%);

  .icon_name {
    width: 42px;
    height: 42px;
    border-radius: 4px;
    opacity: 1;
    background: rgba(14, 31, 96, 0.5);
    box-sizing: border-box;
    border: 1px solid #5B759B;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #5B759B;
    font-size: 14px;
  }

  .text {
    opacity: 0.65;
    font-family: Source Han Sans;
    font-size: 12px;
    color: #fff;
    margin-top: 8px;
  }
}
.changeGL{
  position: absolute;
  bottom: 125px;
  right: 340px;
  z-index: 20;
  width: 42px;
  height: 42px;
  opacity: 1;
  /* 自动布局 */
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  // padding: 5px;
  cursor: pointer;
  background: linear-gradient(180deg, rgba(10, 29, 100, 0.7) 0%, rgba(21, 30, 73, 0.6984) 100%);

  .icon_name {
    width: 42px;
    height: 42px;
    border-radius: 4px;
    opacity: 1;
    background: rgba(14, 31, 96, 0.5);
    box-sizing: border-box;
    border: 1px solid #5B759B;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #5B759B;
    font-size: 14px;
  }

  .text {
    opacity: 0.65;
    font-family: Source Han Sans;
    font-size: 12px;
    color: #fff;
    margin-top: 8px;
  }
}
.activeDiv{
  background:  rgba(38, 68, 173, 0.3);
  .icon_name{
    color: #00CEFF;
  }
}

  .icon_name:hover{
    background: rgba(38, 68, 173, 0.5);
    box-sizing: border-box;
    border: 0.61px solid rgba(38, 68, 173, 0.3);
  }
  .content_dialog{
    height: 600px;
  }
</style>
<style lang="scss">
  .my_Dialog_basic{
    background: url("@/assets/panel/弹窗bg.png") no-repeat;
  background-size: 100% 100%;
  padding: 20px 10px 20px 10px;
  .heard_name {
    background: url("@/assets/panel/right_panel.png") no-repeat;
    background-size: contain;
    height: 28px;
    .d_name {
      font-family: YouSheBiaoTiHei;
      font-weight: 400;
      font-size: 17px;
      color: #ffffff;
      height: 22px;
      line-height: 20px;
      padding-left: 20px;
      text-align: left;
    }
  }
  .el-dialog__headerbtn:active {
    background-color: transparent !important;
    outline: none !important;
    box-shadow: none !important;
  }
  .el-dialog__header{
    padding-bottom: 0px;
  }
  .el-dialog__headerbtn{
    position: absolute;
  top: 10px;
  right: 20px;
  .el-dialog__close {
    color: #fff;
    font-size: 20px;
  }
  }
  .el-dialog__headerbtn :focus, .el-dialog__headerbtn:hover{
    outline: none;
    box-shadow: none;
  }
  .el-button:focus{
    outline: none;
  }
  .el-carousel__indicators--horizontal{
    position: absolute;
    bottom: -10px;
  }
}
</style>