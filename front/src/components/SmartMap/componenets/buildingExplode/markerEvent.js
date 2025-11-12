/*
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-13 10:13:10
 * @LastEditTime: 2024-06-22 10:26:47
 * @LastEditors: Alex
 */
// import { clearByName,handelByMoudleNamePlus } from "@/utils/clear";

import { samples_floorInit,samples_floorMoveUp,samples_floorMoveDown,samples_floorMovePull,samples_floorMovePullHide } from "./dismantle"
import {computed} from 'vue'
import useFloorStore from '@/store/modules/floorStore'
import mapConfig from '../../config'
import {floorModalInit} from '../../js/utils'
import { getFloorsDataApi } from "@/api/basic/index";
import useWorkCockpitStore from '@/store/modules/workCockpit.js'
let checked5= computed(()=>useWorkCockpitStore().bussinessCheckBox)
//会展中心数据
let layeridone = mapConfig.keybuildName;
let obleft = "D9381854A8E9F4D736C75C919F2A4CED+"
let ObjectIDsarr = ["3", "4", "5", "6",'2','7'];
let PropertyNamearr = ["1F", "2F", "3F", "4F","5F",'楼顶']
let camera1 = [[491996.029062, 4324425.92375, 147.900137, -60.806602, -92.410881, 0.000011],
[491996.029062, 4324425.92375, 157.900137, -60.806602, -92.410881, 0.000011],
[491996.029062, 4324425.92375, 167.900137, -60.806602, -92.410881, 0.000011],
[491996.029062, 4324425.92375, 177.900137, -60.806602, -92.410881, 0.000011],
[491996.029062, 4324425.92375, 187.900137, -60.806602, -92.410881, 0.000011]]
let floorArrone = [];
for (let i = 0; i < ObjectIDsarr.length; i++) {
    floorArrone.push(
        {
            "Id": layeridone,
            "ObjectIDs": [obleft + ObjectIDsarr[i]],
            "PropertyName": PropertyNamearr[i],
            "Camera":camera1[i]
        }
    )
}
// 会展中心附近楼层
//重点楼宇处理所需要的数据
//办公业态3号楼
let layerid3 = "7F26D3E54031BA0113CFABB1901F55BE";
let obleft3 = "70AA9560700006E71A1429570A98B010+";
let ObjectIDsarr3 = ["9057","9056","9058","9060","9062","9061","9063","9064","9065","9066","9067","9059","9068"];
let PropertyNamearr3 = ["1F","2F","3F","4F","5F","6F","7F","8F","9F","10","11","12","楼顶",];
let floorArr3 = [];
for (let i = 0; i < ObjectIDsarr3.length; i++) {
  floorArr3.push({
    Id: layerid3,
    ObjectIDs: [obleft3 + ObjectIDsarr3[i]],
    PropertyName: PropertyNamearr3[i],
  });
}
//办公业态4号楼
let layerid4 = "0DB832C446F9576249A0018874082C44";
let obleft4 = "FDF9FA68439AFEB4A2BABB1B90FDA4BA+";
let ObjectIDsarr4 = ["20663","20674","20665","20662","20673","20664","20672","20671","20670","20669","20668","20667","20666"];
let PropertyNamearr4 = ["1F","2F","3F","4F","5F","6F","7F","8F","9F","10F","11F","12F","楼顶"];
let floorArr4 = [];
for (let i = 0; i < ObjectIDsarr4.length; i++) {
  floorArr4.push({
    Id: layerid4,
    ObjectIDs: [obleft4 + ObjectIDsarr4[i]],
    PropertyName: PropertyNamearr4[i],
  });
}
//办公业态5号楼
let layerid5 = "9D5E811A422841881C8A459520F6704C";
let obleft5 = "47E9815321553671EB93A2D89B6A06C4+";
let ObjectIDsarr5 = ["21788","21789","21790","21795","23274","21792","21794","21793","21791","21797","21798","21799"];
let PropertyNamearr5 = ["1F","2F","3F","4F","5F","6F","7F","8F","9F","10F","11F","楼顶"];
let floorArr5 = [];
for (let i = 0; i < ObjectIDsarr5.length; i++) {
  floorArr5.push({
    Id: layerid5,
    ObjectIDs: [obleft5 + ObjectIDsarr5[i]],
    PropertyName: PropertyNamearr5[i],
  });
}
//办公业态6号楼
let layerid6 = "602582E94916AAF4D689CB8646CE51F5";
let obleft6 = "9494A19CAC22AFCCA8481C74F21DBB0F+";
let ObjectIDsarr6 = ["17050","17064","17054","17053","17052","17063","17062","17061","17060","17059","17058","17057","17056","17055","17051"];
let PropertyNamearr6 = ["1F","2F", "3F","4F","5F","6F","7F","8F","9F","10F","11F","12F","130F","14F","楼顶"];
let floorArr6 = [];
for (let i = 0; i < ObjectIDsarr6.length; i++) {
  floorArr6.push({
    Id: layerid6,
    ObjectIDs: [obleft6 + ObjectIDsarr6[i]],
    PropertyName: PropertyNamearr6[i],
  });
}

//国酒会议中心
let layeridTwo = '194EED5640F6856D066253A95D209453'
let oblefTwo = "1B85B71FB13F39E28988C09645283376+"
let ObjectIDsarrTwo = ["33579", "33577","33617", "33625", "33647"];
let PropertyNamearrTwo = ["地下","1F", "2F", "3F",'楼顶']
let floorArrTwo = [];
for (let i = 0; i < ObjectIDsarrTwo.length; i++) {
  floorArrTwo.push(
        {
            "Id": layeridTwo,
            "ObjectIDs": [oblefTwo + ObjectIDsarrTwo[i]],
            "PropertyName": PropertyNamearrTwo[i],
        }
    )
}
//国际酒店数据
let layeridThree = '42EF76104B6C33777614D4B1BD338446';
let oblefThree = "88CA7B6F8951FA4EA354A47E5FC93479+"
let ObjectIDsarrThree = ["8116", '27229',"34051", "39793", "8873",'35354'];
let PropertyNamearrThree = ["1F", "2F", "3F", "4F","5F",'楼顶']
let floorArrThree = [];
for (let i = 0; i < ObjectIDsarrThree.length; i++) {
  floorArrThree.push(
        {
            "Id": layeridThree,
            "ObjectIDs": [oblefThree + ObjectIDsarrThree[i]],
            "PropertyName": PropertyNamearrThree[i],
        }
    )
}
//公务接待数据
let layeridFour = 'B143C7C34954960615DE2CB746220B85';
let oblefFour = "11BE05E1D3A2617CABC1F92450034BD0+"
let ObjectIDsarrFour = ["1791","7932", "7973", "7954"];
let PropertyNamearrFour = ["地下","1F", "2F",'楼顶']
let floorArrFour = [];
for (let i = 0; i < ObjectIDsarrFour.length; i++) {
  floorArrFour.push(
        {
            "Id": layeridFour,
            "ObjectIDs": [oblefFour + ObjectIDsarrFour[i]],
            "PropertyName": PropertyNamearrFour[i],
        }
    )
}
//雄安站
let layeridFive = 'E9E887FC4B5A5578DCD79EBBF4E69583';
let oblefFive = "983FDB03E27D07FC2A256F937BA8DB74+"
let ObjectIDsarrFive = ["9","2","10","16", "15" ,"6"];
let PropertyNamearrFive = ["站台和一层楼梯","夹层","站台","站台和二层候车厅楼梯","二层候车厅", "顶层"]
// let ObjectIDsarrFive = ["516870","524946","408900" ];
// let PropertyNamearrFive = ["站台和一层楼梯","站台","二层候车厅"]
let floorArrFive = [];
for (let i = 0; i < ObjectIDsarrFive.length; i++) {
  floorArrFive.push(
        {
            "Id": layeridFive,
            "ObjectIDs": [oblefFive + ObjectIDsarrFive[i]],
            "PropertyName": PropertyNamearrFive[i],
        }
    )
}
//计算中心
let layeridSix = '0AFBE44F4AFC4302C7A3E6BD717D768F';
let oblefSix = "70AA9560700006E71A1429570A98B010+"
let ObjectIDsarrSix = ["5326","5324","4818","5341","5325","5331"];
let PropertyNamearrSix = ["地面","地下",'1F',"2F","3F","楼顶"]
let camera =[[],[491701.84375, 4324343.11375, 117.742305, -47.066597, 179.619812, 0],[491701.84375, 4324343.11375, 117.742305, -47.066597, 179.619812, 0],[491701.84375, 4324343.11375, 117.742305, -47.066597, 179.619812, 0],[491701.84375, 4324343.11375, 117.742305, -47.066597, 179.619812, 0],[]]
let floorArrSix = [];
for (let i = 0; i < ObjectIDsarrSix.length; i++) {
  floorArrSix.push(
        {
            "Id": layeridSix,
            "ObjectIDs": [oblefSix + ObjectIDsarrSix[i]],
            "PropertyName": PropertyNamearrSix[i],
            "Camera":camera[i]
        }
    )
}
//中国星网
let layeridSeven = '7602D058499FD7D2BF2E399578D95535';
let oblefSeven = "E799DBD5E27F8486B26CAF13D9951F3A+"
let ObjectIDsarrSeven = ["480","35","4","8","6","379","378","484"];
let PropertyNamearrSeven = ["地下室","地面","bcde",'1F',"32层","4F","5F","楼顶"]
let floorArrSeven = [];
for (let i = 0; i < ObjectIDsarrSeven.length; i++) {
  floorArrSeven.push(
        {
            "Id": layeridSeven,
            "ObjectIDs": [oblefSeven + ObjectIDsarrSeven[i]],
            "PropertyName": PropertyNamearrSeven[i],
        }
    )
}
//中国中化大厦
let layerid8 = 'DFD4BC43432648316A1ED7AFA9E29A1D';
let oblef8 = "D13E7AB99BADD609F4A694A600C20E69+"
let ObjectIDsarr8 = ["1360","3090"];
let PropertyNamearr8 = ["二十一层","楼顶"]
let floorArr8 = [];
for (let i = 0; i < ObjectIDsarr8.length; i++) {
  floorArr8.push(
        {
            "Id": layerid8,
            "ObjectIDs": [oblef8 + ObjectIDsarr8[i]],
            "PropertyName": PropertyNamearr8[i],
        }
    )
}
//中国电信
let layerid9 = '5F081B404D874D74AC9C86B16BBC9F1A';
let oblef9 = "6F69AD9540DBFECC290E05D8E7847BD4+"
let ObjectIDsarr9 = ["1290", '472',"463", "1536", "1514","451",'1060'];
let PropertyNamearr9 = ["1F", "2F", "3F", "4F","5F",'6F','楼顶']
let floorArr9 = [];
for (let i = 0; i < ObjectIDsarr9.length; i++) {
  floorArr9.push(
        {
            "Id": layerid9,
            "ObjectIDs": [oblef9 + ObjectIDsarr9[i]],
            "PropertyName": PropertyNamearr9[i],
        }
    )
}
//规划展示中心
let layerid10 = '45F4FC254F9FCE38415DE381831BA0DE';
let oblef10 = "27B3346DB2EF20DE09ACDF3544D67664+"
let ObjectIDsarr10 = ["549342",'549185'];
let PropertyNamearr10 = ["1F",'顶层']
let floorArr10 = [];
for (let i = 0; i < ObjectIDsarr10.length; i++) {
  floorArr10.push(
        {
            "Id": layerid10,
            "ObjectIDs": [oblef10 + ObjectIDsarr10[i]],
            "PropertyName": PropertyNamearr10[i],
        }
    )
}
//雄安会展中心酒店
let layerid11 = '648398474737F24D50F03E8BFE496F15';
let oblef11 = "E1A376E8CC0615DF19A6CE26E0967FFA+"
let ObjectIDsarr11 = ["5",'6','7','34','3','4','9'];
let PropertyNamearr11 = ["1F",'2F','3F','夹层','16F','17F','楼顶']
let floorArr11 = [];
for (let i = 0; i < ObjectIDsarr11.length; i++) {
  floorArr11.push(
        {
            "Id": layerid11,
            "ObjectIDs": [oblef11 + ObjectIDsarr11[i]],
            "PropertyName": PropertyNamearr11[i],
        }
    )
}
window.keybuildings = {
  "会展中心":{
     roomarr: floorArrone,
     moveparams:{
      ldgd:25,//整体离地高度，单位米
        vzjl:5, //垂直升高距离，单位米
        vomd:0.5,//垂直单次移动距离，单位米
        vomt:30,//垂直单次移动时间，单位毫秒
        lzjl:80,//水平移动距离，单位米
        lomd:5,//水平单次移动距离，单位米
        lomt:80,//垂直单次移动时间，单位毫秒
        direction:3,//水平移动方向1234 东西南北
        camerpo:{diatance:200,camera:[ -70.99995,-90, 0]},//整栋楼时定位相机位置
        singlefocus:80//单个房间的定位距离
     }
  },
  "国酒会议中心":{
    roomarr: floorArrTwo,
    moveparams:{
      ldgd:20,
      vzjl:5,
      vomd:0.5,
      vomt:50,
      lzjl:150,
      lomd:5,
      lomt:31.818181818181817,
      direction:3,
      camerpo:{diatance:180,camera:[-48.987354,-94.735474,0]},
      singlefocus:80
    }
 },
 "国际酒店":{
  roomarr: floorArrThree,
  moveparams:{
    ldgd:10,
    vzjl:5,
    vomd:0.5,
    vomt:50,
    lzjl:200,
    lomd:5,
    lomt:31.818181818181817,
    direction:1,
    camerpo:{diatance:150,camera:[-27.371483,-93.10907,0]},
    singlefocus:80
  }
},
"公务接待楼":{
  roomarr: floorArrFour,
  moveparams:{
    ldgd:15,
    vzjl:5,
    vomd:0.5,
    vomt:50,
    lzjl:90,
    lomd:5,
    lomt:50,
    direction:3,
    camerpo:{diatance:100,camera:[-63.77062,-92.168335,0]},
    singlefocus:60}
},
//会展中心附近楼宇
"办公业态3#": {
  roomarr: floorArr3,
  moveparams: {
    ldgd: 25, //整体离地高度，单位米
    vzjl: 0, //垂直升高距离，单位米
    vomd: 0.4, //垂直单次移动距离，单位米
    vomt: 30, //垂直单次移动时间，单位毫秒
    lzjl: 120, //水平移动距离，单位米
    lomd: 12, //水平单次移动距离，单位米
    lomt: 120, //垂直单次移动时间，单位毫秒
    direction: 2, //水平移动方向1234 东西南北
    camerpo: { diatance: 110, camera: [-24, -50, 0] }, //整栋楼时定位相机位置
    singlefocus: 70, //单个房间的定位距离
  },
},
"办公业态4#": {
  roomarr: floorArr4,
  moveparams: {
    ldgd: 25, //整体离地高度，单位米
    vzjl: 0, //垂直升高距离，单位米
    vomd: 0.4, //垂直单次移动距离，单位米
    vomt: 30, //垂直单次移动时间，单位毫秒
    lzjl: 110, //水平移动距离，单位米
    lomd: 11, //水平单次移动距离，单位米
    lomt: 100, //水平单次移动时间，单位毫秒
    direction: 2, //水平移动方向1234 东西南北
    camerpo: { diatance: 90, camera: [-19, -70, 0] }, //整栋楼时定位相机位置
    singlefocus: 60, //单个房间的定位距离
  },
},
"办公业态5#": {
  roomarr: floorArr5,
  moveparams: {
    ldgd: 25, //整体离地高度，单位米
    vzjl: 0, //垂直升高距离，单位米
    vomd: 0.5, //垂直单次移动距离，单位米
    vomt: 30, //垂直单次移动时间，单位毫秒
    lzjl: 110, //水平移动距离，单位米
    lomd: 11, //水平单次移动距离，单位米
    lomt: 100, //垂直单次移动时间，单位毫秒
    direction: 3, //水平移动方向1234 东西南北
    camerpo: { diatance: 140, camera: [-22, -77, 0] }, //整栋楼时定位相机位置
    singlefocus: 80, //单个房间的定位距离
  },
},
"综合商业6#": {
  roomarr: floorArr6,
  moveparams: {
    ldgd: 25, //整体离地高度，单位米
    vzjl: 0, //垂直升高距离，单位米
    vomd: 0.4, //垂直单次移动距离，单位米
    vomt: 30, //垂直单次移动时间，单位毫秒
    lzjl: 110, //水平移动距离，单位米
    lomd: 11, //水平单次移动距离，单位米
    lomt: 100, //垂直单次移动时间，单位毫秒
    direction: 3, //水平移动方向1234 东西南北
    camerpo: { diatance: 100, camera: [-16, -105, 0] }, //整栋楼时定位相机位置
    singlefocus: 60, //单个房间的定位距离
  },
},
"雄安站": {
  roomarr: floorArrFive,
  moveparams: {
    ldgd: 0, //整体离地高度，单位米
    vzjl:0, //垂直升高距离，单位米
    vomd:0.5,//垂直单次移动距离，单位米
    vomt:30,//垂直单次移动时间，单位毫秒
    lzjl:80,//水平移动距离，单位米
    lomd:5,//水平单次移动距离，单位米
    lomt:80,//垂直单次移动时间，单位毫秒
    direction:1,//水平移动方向1234 东西南北
    camerpo:{diatance:600,camera:[-53.413868, 31.98572, 0]},//整栋楼时定位相机位置
    singlefocus:50//单个房间的定位距离
  },
  // 512936.844219, 4324949.344688, 538.696445, -53.413868, 31.98572, -0.000001
},
"计算中心": {
  roomarr: floorArrSix,
  moveparams:{
    ldgd:25,
    vzjl:0,
    vomd:0.5,
    vomt:50,
    lzjl:90,
    lomd:5,
    lomt:50,
    direction:3,
    camerpo:{diatance:150,camera:[-47.066597, 179.619812,0]},
    singlefocus:60}
},
"中国星网": {
  roomarr: floorArrSeven,
  moveparams:{
    ldgd:18,//整体离地高度，单位米
      vzjl:0, //垂直升高距离，单位米
      vomd:0.5,//垂直单次移动距离，单位米
      vomt:30,//垂直单次移动时间，单位毫秒
      lzjl:80,//水平移动距离，单位米
      lomd:5,//水平单次移动距离，单位米
      lomt:80,//垂直单次移动时间，单位毫秒
      direction:3,//水平移动方向1234 东西南北
      camerpo:{diatance:200,camera:[  -38.571331, -88.735344, 0]},//整栋楼时定位相机位置
      singlefocus:80//单个房间的定位距离
   }
},
"中国中化大厦": {
  roomarr: floorArr8,
  moveparams:{
    ldgd:0,//整体离地高度，单位米
      vzjl:0, //垂直升高距离，单位米
      vomd:0.5,//垂直单次移动距离，单位米
      vomt:30,//垂直单次移动时间，单位毫秒
      lzjl:80,//水平移动距离，单位米
      lomd:5,//水平单次移动距离，单位米
      lomt:80,//垂直单次移动时间，单位毫秒
      direction:3,//水平移动方向1234 东西南北
      camerpo:{diatance:100,camera:[ -30,-90, 0]},//整栋楼时定位相机位置
      singlefocus:80//单个房间的定位距离
   }
},
"中国电信": {
  roomarr: floorArr9,
  moveparams:{
    ldgd:15,//整体离地高度，单位米
      vzjl:0, //垂直升高距离，单位米
      vomd:0.5,//垂直单次移动距离，单位米
      vomt:30,//垂直单次移动时间，单位毫秒
      lzjl:80,//水平移动距离，单位米
      lomd:5,//水平单次移动距离，单位米
      lomt:80,//垂直单次移动时间，单位毫秒
      direction:3,//水平移动方向1234 东西南北
      camerpo:{diatance:80,camera:[ -45.580566, -104.984116, 0]},//整栋楼时定位相机位置
      singlefocus:80//单个房间的定位距离
   }
},
"规划展示中心": {
  roomarr: floorArr10,
  moveparams:{
    ldgd:0,//整体离地高度，单位米
      vzjl:0, //垂直升高距离，单位米
      vomd:0.5,//垂直单次移动距离，单位米
      vomt:30,//垂直单次移动时间，单位毫秒
      lzjl:80,//水平移动距离，单位米
      lomd:5,//水平单次移动距离，单位米
      lomt:80,//垂直单次移动时间，单位毫秒
      direction:3,//水平移动方向1234 东西南北
      camerpo:{diatance:100,camera:[ -45.580566, -104.984116, 0]},//整栋楼时定位相机位置
      singlefocus:80//单个房间的定位距离
   }
},
"雄安会展中心酒店": {
  roomarr: floorArr11,
  moveparams:{
    ldgd:0,//整体离地高度，单位米
      vzjl:0, //垂直升高距离，单位米
      vomd:0.5,//垂直单次移动距离，单位米
      vomt:30,//垂直单次移动时间，单位毫秒
      lzjl:80,//水平移动距离，单位米
      lomd:5,//水平单次移动距离，单位米
      lomt:80,//垂直单次移动时间，单位毫秒
      direction:3,//水平移动方向1234 东西南北
      camerpo:{diatance:80,camera:[  -34.613827, 2.372722, 0]},//整栋楼时定位相机位置
      singlefocus:80//单个房间的定位距离
   }
},
};

export async function keyBuilding(markertext,auto=null){
	//关闭楼层所有可升起的楼层标签
    let roomarr=window.keybuildings[markertext].roomarr;
    let params=window.keybuildings[markertext].moveparams;
	samples_floorInit(roomarr,params).then(() => {
		//楼层升起
	  // setTimeout(samples_floorMoveUp, 800)
    useFloorStore().setFloornumberShow(true)
    useFloorStore().setExplodebuildName(markertext)
    useFloorStore().set_roomArr(roomarr)
    useFloorStore().setfloorsTotalNum(roomarr.length - 1)
    if(auto>0){
      setTimeout(()=>{
        samples_floorMoveUp().then(()=>{
          setTimeout(()=>{
            // samples_floorMovePull(auto)
            samples_floorMovePullHide(auto)
          },1800)
        })
      },800)

    }else{
      let Number = markertext==='会展中心'?2:markertext==='国酒会议中心'?3:markertext==='国际酒店'?
      4:markertext==='雄安站'?6:markertext==='计算中心'?7:markertext==='中国星网'?8:markertext==='中国中化大厦'?9:
      markertext==='中国电信'?10:markertext==='规划展示中心'?11:markertext==='雄安会展中心酒店'?12:5
      floorModalInit(Number)
      setTimeout(samples_floorMoveUp, 800) // 楼层抬起
      // window.__g.infoTree.hide('033778894069F4FF69887998DB5A2926')
      // window.__g.infoTree.show('6D6C753E4356603BB3B3B98D25C90F13')
    }
	});

}

export async function keyBuilding3(id){
  useFloorStore().set_noFloorsId(id)
  let params = {buildingId:id}
  getFloorsDataApi(params).then(res=>{
    useFloorStore().set_nofloorNumber(true)
    useFloorStore().set_noFloorNum(Number(res.data))
  })
   
}

export async function drawBussinessUser(arr){
  let g = window.__g
  let markerarr = []
  //`${item.name}\n安全隐患：${item.dangerNum}\n警力部署：${item.policeData?.onduty}\n应急力量：${item.policeData?.emergency}`
  for (let item of arr) {
    // let o = {
    //   id: item.roomCode,
    //   groupId: 'room',
    //   userData:item.status===128?'no':'ok',
    //   coordinate: [Number(item.x),Number(item.y),1], //坐标位置
    //   coordinateType: 0,//默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
    //   anchors: [-15, 34],
    //   range: [0, 1000], //可视范围
    //   imageSize: [30, 34], //图片的尺寸,//图片的尺寸
    //   imagePath: '',
    //   fixedSize: true, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
    //   text: `${item.roomTitle}\n${item.tenant?item.tenant:'待租'}`, //显示的文字
    //   useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
    //   textRange: [1, 1000], //文本可视范围[近裁距离, 远裁距离]
    //   textOffset: [0, 0], // 文本偏移
    //   textBackgroundColor: item.status===128?[0.43,0.5,0.56,0.85]:[0.23,0.70,0.44,0.85], //文本背景颜色
    //   fontSize: 10, //字体大小
    //   fontOutlineSize: 1, //字体轮廓线大小
    //   fontColor: Color.White, //字体颜色
    //   fontOutlineColor: Color.Black, //字体轮廓线颜色
    //   autoHeight: true, // 自动判断下方是否有物体
    //   displayMode: 2, //0：相机移动不显示，参与避让聚合;1：相机移动时显示，参与避让聚合; 2：相机移动时显示，不参与避让聚合;3：相机移动时不显示，不参与避让聚合
    //   priority: 0, //避让优先级
    //   occlusionCull: true, //是否参与遮挡剔除
    //   clusterByImage: true
    // };
    let marker3dObj = {
      id: item.roomCode,
      userData: item.status === 128 ? "no" : "ok",
      groupId: "room",
      coordinate: [Number(item.x), Number(item.y), Number(item.z)+8], //坐标位置
      coordinateType: 0, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      text: `${item.roomTitle}-${item.tenant ? item.tenant : "待租"}`, //显示的文字
      textSize: 30,//3D标注显示文字大小
      textColor: [0, 1, 0, 1],//3D标注显示文字颜色
      textOutlineSize: 1,//3D标注显示文字轮廓大小
      textOutlineColor: Color.Black,// 3D标注显示文字轮廓颜色
      textFixed: true,// 3D标注显示文字是否固定文本朝向
      fixedSize: true,// 默认尺寸 非近大远小
        textVisible: false,//3D标注显示文字是否显示文本
        textLocation: [0, 0, 0],// 文字位置
        textRotation: [0, 90, 0],// 文字旋转
        textScale: [1, 1, 1],// 文字缩放
        pointName: '/JC_CustomAssets/EffectLibrary/Exhibition/Sign/Sign_BQ_A_1_4',//3D标注展示的特效名称
        pointVisible: true,//3D标注是否显示
        pointScale: 2,//3D标注整体缩放比例
        range: [0, 2000], //3D标注的可视距离范围：[min,max]，单位：米
        autoHeight: false, //自动判断下方是否有物体，设置正确高度，默认值：false
        collision: true //默认开启碰撞
      // textSize: 20,//3D标注显示文字大小
      // textFixed: false,// 3D标注显示文字是否固定文本朝向
      // fixedSize: false,// 默认尺寸 非近大远小
      // textVisible: false,//3D标注显示文字是否显示文本
      // textLocation: [0, 0, 0],// 文字位置
      // textRotation: [0, 90, 0],// 文字旋转
      // textScale: [1, 1, 1],// 文字缩放
      // pointName: '/JC_CustomAssets/EffectLibrary/Exhibition/Sign/Sign_BQ_A_1_1',//3D标注展示的特效名称
      // pointVisible: true,//3D标注是否显示
      // pointScale: 1,//3D标注整体缩放比例
      // range: [1, 1000], //3D标注的可视距离范围：[min,max]，单位：米
      // autoHeight: true, //自动判断下方是否有物体，设置正确高度，默认值：false
      // collision: true //默认开启碰撞
    }
    // markerarr.push(o)
    markerarr.push(marker3dObj)
  }
  if (markerarr.length > 0) {
    // await g.marker.add(markerarr)
    await g.marker3d.add(markerarr)
    let arrs = []
    for(const item of markerarr){
      let text = {
        id: item.id,
        functionName: '文字',
        parameters: [
      { "paramType":5, "paramValue": item.text },
      { "paramType":6, "paramValue": [0,0,0,1] },
       { "paramType":2, "paramValue": 45 },
        ]
    }
  // let bg= {
  //       id: item.id,
  //       functionName: '背景',
  //       parameters: [
  //           { "paramType":6, "paramValue":item.userData === 'no'
  //                 ? [1,0.85,0.39,1]
  //                 : [1,0.85,0.39,1] }
  //       ]
  //   }
    arrs.push(text)
    // arrs.push(bg)
    }
    g.marker3d.callBPFunction(arrs)
    // if(checked5.value){
    //   g.marker.showByGroupId('room')
    // }else{
    //   g.marker.hideByGroupId('room')
    // }
  }
}

// 打开对应楼层的遮罩
export async function openFloorsModal(name,type){
  let g = window.__g
  let modals = '7492A0AC4D9D3FFD499FB68F6873305D' //总共大的遮罩
  // 公务接待楼 C479C4654A1747853B9715ADAA397778
  let obj = {
    '会展中心':'1FD31C4845D757E4722DE6B27ABF32A7',
    '国酒会议中心':'5E5880BC450E65C86E65BDB971CD4917',
    '国际酒店':'A2D30C5B4A05A0562C278AB175944D54',
    '办公业态3#':'23B568244951E9ADF78BEC88CCBDAD75',
    '办公业态4#':'EEE7793B46395045C39A3CBE00FCF604',
    '办公业态5#':'E77367454CF4BBEE9BA86B98D6EF306E',
    '负一楼':'865C36814356497827A5AEB4AC7AF7C7',
    '综合商业6#':'4AC8DEF2444F3075DBC08FA6A5FE3DE0',
    '公务接待楼':'C479C4654A1747853B9715ADAA397778'
  }
  if(type){
    // if(obj[name] !== undefined){
    //   g.infoTree.show(obj[name])
    // }
    if(obj.hasOwnProperty(name)){
      g.infoTree.show(obj[name])
    }
  }else{
    g.infoTree.hide(modals)
  }
}