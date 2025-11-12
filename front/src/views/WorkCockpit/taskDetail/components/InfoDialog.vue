<template>
  <div class="content-box">
    <div class="content-header">
      <span v-if="resourcVal">{{ infoData['mingcheng'] || infoData['title'] }}</span>
      <span v-else-if="basicVal">要素信息</span>
      <span v-else-if="fbajVal">防爆安检信息</span>
      <span v-else>警力信息</span>
      <el-icon class="close" @click="handleClose">
        <Close />
      </el-icon>
    </div>
    <div class="content" v-if="resourcVal">
      <div class="changePoint" @click="changePointData">点位纠偏</div>
      <div v-if="position.position&&props.showPosition">
        任务位置：{{ `在${position.refLineName}约${position.position}公里处` }}
        </div>
      <p v-for="item in infoList" :key="item.key">
        <div class="" v-if="item.value==='图片附件'" style="display: flex;">
          <div class="left" style="width:100px">{{ item.value}}</div>
          <div class="right" style="width:100px;height:100px;">
            <el-image
            style="width: 100%; height: 100%"
            :src="getImagePath(infoData[item.key][0])"
            :max-scale="70"
            :min-scale="0.2"
            :preview-teleported="true"
            :preview-src-list="getImagePath1(infoData[item.key])"
            fit="cover"
           v-if="infoData[item.key]&&infoData[item.key]?.length"/>
           <span v-else>暂无图片</span>
          </div>
        </div>
        <div class="" v-else>
          {{ item.value}}：{{ infoData[item.key]?infoData[item.key]:'无' }}
        </div>
      </p>
    </div>
    <div class="content" v-else-if="policeVal">
      <p>
          警力名称：{{ policeObj.data.info.jlmc?policeObj.data.info.jlmc:policeObj.name }}
      </p>
      <p>
          警力位置：{{ policeObj.data.info.weizhi?policeObj.data.info.weizhi:'无' }}
      </p>
      <p>
          部署数量：{{ policeObj.data.info.num?policeObj.data.info.num:'无' }}
      </p>
      <p>
          分组名称：{{ policeObj.data.info.group?policeObj.data.info.group:'无' }}
      </p>
      <p>
          部署防线：{{ policeObj.data.info.fangxian?policeObj.data.info.fangxian:'无' }}
      </p>
      <p>
          警力编号：{{ policeObj.data.info.jlbh?policeObj.data.info.jlbh:'无'}}
      </p>
      <p>
          联系方式：{{ policeObj.data.info.lxfs||'无' }}
      </p>
      <p>
          所属单位：
      </p>
      <p>
          负责人：
      </p>
      <p>
          联系电话：
      </p>
    </div>
    <div class="content" v-else-if="fbajVal">
      <p>
          名称：{{ fbajObj.data.info.mc?fbajObj.data.info.mc:fbajObj.name }}
      </p>
      <div v-if="fbajObj.data.marker.userData==='安检员'">
        <p>
        位置：{{ fbajObj.data.info.wz?fbajObj.data.info.wz:'' }}
      </p>
      <p>
        数量：{{ fbajObj.data.info.num?fbajObj.data.info.num:'' }}
      </p>
      <p>
        联系方式：{{ fbajObj.data.info.phone?fbajObj.data.info.phone:''}}
      </p>
      </div>
  
      <p v-else>
        备注：{{ fbajObj.data.info.bz?fbajObj.data.info.bz:'无' }}
      </p>
    </div>
    <div class="content" v-else>
      <p>
          名称：{{ basicObj.data.info.mc?basicObj.data.info.mc:basicObj.name }}
      </p>
      <p>
        位置：{{ `在${basicObj.refLineName}约${basicObj.position}公里处` }}
      </p>
      <p>
          备注：{{ basicObj.data.info.bz?basicObj.data.info.bz:'无' }}
      </p>
    </div>

  </div>

</template>

<script setup>
import {ElMessage} from 'element-plus'
import { Close } from '@element-plus/icons-vue'
import { ref, onMounted ,computed,defineProps,watch} from 'vue'
// 接口
import { getPointInfo } from '@/api/workCockpit/index.js'
import {PostAddFormInfo} from "@/api/index";
import {getById } from "@/api/task/task";
// store
import {arrToRepeat} from '@/utils/index'
import { sessionStorage } from "@/utils/storage";
import useWorkCockpitStore from '@/store/modules/workCockpit.js'
const WorkCockpitStore = useWorkCockpitStore()
const eventMapData=computed(()=>WorkCockpitStore.eventMapData)
const pointData = computed(()=>WorkCockpitStore.newLocation)
const taskId = computed(()=>WorkCockpitStore.basicTaskId)
const sceneId = computed(()=>WorkCockpitStore.basicSceneId)
const props = defineProps({
  showPosition: {
    type: Boolean,
    default: true,
  },
});
const handleClose = () => {
  WorkCockpitStore.setShowMarkerInfo(false)
  WorkCockpitStore.set_changeLocation(false)
}
const resourcVal = ref(true)
const basicVal = ref(false)
const policeVal = ref(false)
const fbajVal = ref(false)
const basicInitData = ref({})
const infoList=ref([])
const infoData=ref({})
const position = ref({
  position:'',
  refLineName:''
})
const basicObj = ref({
})
const policeObj = ref({})
const fbajObj = ref({})
// 获取点位信息
const getInfo=()=>{
  // sceneId:sceneId.value,taskId:taskId.value
  let params
  if(props.showPosition){
     params = {id:eventMapData.value.Id,sceneId:sceneId.value,taskId:taskId.value}
  }else{
     params = {id:eventMapData.value.Id}
  }
  getPointInfo(params).then(res=>{
    if(!res.data) {
      getById(params).then(res=>{
        if(!res.data){
          ElMessage({ message: '暂无信息', type: 'warning' })
        }else{
          if(res.data.type==='basic'){
            basicVal.value = true
            resourcVal.value = false
            policeVal.value = false
            fbajVal.value = false
            basicObj.value = res.data
            if(res.data.position){
              basicObj.value.position =Number(res.data.position).toFixed(2)
            }
          }
          if(res.data.type==='police'){
            policeVal.value = true
            resourcVal.value = false
            basicVal.value = false
            fbajVal.value = false
            policeObj.value = res.data
          }
          if(res.data.type==='fbaj'){
            policeVal.value = false
            resourcVal.value = false
            basicVal.value = false
            fbajVal.value = true
            fbajObj.value = res.data
          }
        }
      })
      
    }else{
      basicVal.value = false
      resourcVal.value = true
      policeVal.value = false
      fbajVal.value = false
    infoData.value=res.data?.data
    position.value.position = Number(res.data.position).toFixed(2) || ''
    position.value.refLineName = res.data.refLineName || ''
    let arrs = Object.entries(res.data.dataFiledName).map(([key, value]) => ({ key, value}))
    infoList.value=arrs
    basicInitData.value = res.data
    }
  })
}
// 获取图片信息
const getImagePath = (url) => {
  if (url.includes('/imgPath')) {
    return url
  } else {
    return '/imgPath' + url
  }
}
const getImagePath1 = (urlList) => {
  let urls = []
  if(urlList?.length){
    for(const item of urlList){
      if (item.includes('/imgPath')) {
        urls.push(item)
  } else {
    urls.push('/imgPath' + item)
  }
    }
  }
  return urls
}
const changePointData = () => {
  ElMessage({
    duration:5000,
    message:'请点击地图选择要重新打点的位置',
    type: 'info',
  })
  WorkCockpitStore.set_changeLocation(true)
}
const updatePoint = (obj) => {
  PostAddFormInfo(obj).then(async(res)=>{
    if(res.code===0){
      if(eventMapData.value.GroupID==='resource'){
        let g = window.__g
        // 场景及其任务内纠偏
        let oldMarker = sessionStorage.get('gsgtcityData')
        if(oldMarker?.length){
        let newData = oldMarker.map(item=>{
          if(item.id===obj.id){
            return {...item,coordinate:[Number(obj.data.jingdu),Number(obj.data.weidu),Number(obj.data.gaodu)]}
          }else{
            return {...item}
          }
        })
        await g.markerLayer.delete('gsgtcity');
          let o = {
          id: `gsgtcity`,
          groupId: 'resource',
          coordinateType: 1,//默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
          range: [0,0.1,10000,200000],
          viewHeightRange :[0,10000],
          autoHeight: false,
          radius: 0.1,
          fixedSize: false,
          anchors: [-15, 34],//锚点，设置Marker的整体偏移，取值规则和imageSize设置的宽高有关，图片的左上角会对准标注点的坐标位置。示例设置规则：x=-imageSize.width/2，y=imageSize.height
          useTextAnimation: false,//关闭文字展开动画效果 打开会影响效率
          textRange: [1, 1000], //文本可视范围[近裁距离, 远裁距离]
          textOffset: [0, 0], // 文本偏移
          textBackgroundColor: [0, 0, 0, 0.75], //文本背景颜色
          fontSize: 8, //字体大小
          fontOutlineSize: 1, //字体轮廓线大小
          fontColor: Color.White, //字体颜色
          displayMode: 0,//智能显示模式  开发过程中请根据业务需求判断使用四种显示模式 
          clusterByImage: false,// 聚合时是否根据图片路径分类，即当多个marker的imagePath路径参数相同时按路径对marker分类聚合
          priority: 0,//避让优先级
          occlusionCull: true,//是否参与遮挡剔除
          markers:arrToRepeat(newData),
      }
      sessionStorage.set("gsgtcityData", arrToRepeat(newData));
       g.markerLayer.add(o)
      ElMessage.success('纠偏成功')
        }
      }else{
        let oldMarker = sessionStorage.get('layerMarkers')
      if(oldMarker?.length){
        let g = window.__g
        oldMarker.map(item=>{
          if(item.id===obj.id){
            item.coordinate = [Number(obj.data.jingdu),Number(obj.data.weidu),Number(obj.data.gaodu)]
          }
        })
        let o = {
        id: `basicDataLayer`,
        groupId: 'basicMarkerLayer',
        coordinateType: 1,//默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
        range: [0,1,100000,200000],
        viewHeightRange :[0,100000],
        autoHeight:true,
        radius: 0,
        fixedSize: false,
        anchors: [-15, 34],//锚点，设置Marker的整体偏移，取值规则和imageSize设置的宽高有关，图片的左上角会对准标注点的坐标位置。示例设置规则：x=-imageSize.width/2，y=imageSize.height
        useTextAnimation: false,//关闭文字展开动画效果 打开会影响效率
        textRange: [1, 1000], //文本可视范围[近裁距离, 远裁距离]
        textOffset: [0, 0], // 文本偏移
        textBackgroundColor: [0, 0, 0, 0.75], //文本背景颜色
        fontSize: 8, //字体大小
        fontOutlineSize: 1, //字体轮廓线大小
        fontColor: Color.White, //字体颜色
        displayMode: 0,//智能显示模式  开发过程中请根据业务需求判断使用四种显示模式 
        clusterByImage: false,// 聚合时是否根据图片路径分类，即当多个marker的imagePath路径参数相同时按路径对marker分类聚合
        priority: 0,//避让优先级
        occlusionCull: true,//是否参与遮挡剔除
        markers:oldMarker,
        }
        g.markerLayer.update(o)
        ElMessage.success('纠偏成功')
      }
      }
      WorkCockpitStore.set_changeLocation(false)
    }
  })
}
watch(
  () => pointData.value,
  (nV, oV) => {
    if (nV) {
  let obj = {
    childTypeId:basicInitData.value.childTypeId,
    id:basicInitData.value.id,
    jcxxId:basicInitData.value.jcxxId,
    parentTypeId:basicInitData.value.parentTypeId,
    data:basicInitData.value.data
  }
  obj.data.jingdu = nV.jingdu
  obj.data.weidu = nV.weidu
  obj.data.gaodu = nV.height
  updatePoint(obj)
    }
  },
  { deep: true, immediate: false }
);
onMounted(() => {
  getInfo()
})
</script>

<style lang="scss" scoped>
.content-box {
  width: 392px;
  height: 520px;
  background: linear-gradient(180deg, #0A1D64 0%, rgba(21, 30, 73, 0.6984) 100%);
  position: absolute;
  right: 22%;
  top: 35px;
  z-index: 51;
  transform: scale(0.8);
  transform-origin: right center;
 

  .content-header {
    width: 100%;
    height: 48px;
    line-height: 48px;
    text-align: center;
    font-family: YouSheBiaoTiHei;
    font-size: 24px;
    background: url("@/assets/workcockpit/header_bg.png") no-repeat;
    position: relative;

    .close {
      position: absolute;
      right: 12px;
      top: 8px;
      cursor: pointer;
    }
 
  }

  .content {
    height: calc(520px - 48px);
    padding: 25px 45px;
    box-sizing: border-box;
    font-size: 14px;
    line-height: 32px;
    overflow: auto;
    position: relative;
    .changePoint{
      height: 30px;
      border-radius: 4px;
      width: 70px;
      text-align: center;
      line-height: 30px;
      background: rgba(35, 101, 255, 1);
      cursor: pointer;
      font-size: 14px;
      cursor: pointer;
      // color: aqua;
    }
  }
}
</style>