import DismantleBuilding from "./dismantleBase"
import useFloorStore from "@/store/modules/floorStore";
//定义楼拆解对象
let dismantleBuildingObj = null;
//记录初始化状态
let readyState = false;
//添加的自定义对象数组
let customObjectDataArr = [];
//添加的自定义对象ID数组
//let  customObjectIdArr = [];

export function getleftStatus() {
    //获取当前楼栋初始化是否完成，且当前运动过程是否完成
    let state = 0;
    if (dismantleBuildingObj == null) {
        state = false;
    }
    else {
        state = dismantleBuildingObj.isStopRun;
    }
    return state;
}

// /** 楼宇拆解初始化 */ export
export async function samples_floorInit(roomArr,params) {
    //判断该楼是否正在运动，如果在运动则不处理，
    if (readyState) {
        console.log('有升起的楼层未被还原');
        return;
    }
        await initFloors(roomArr, params);
    
}
//初始化楼层// 
async function initFloors(roomArr, params) {
    //待拆解的楼层模型信息数组
    dismantleBuildingObj = new DismantleBuilding({
        floorInfoArr: roomArr,         //必选 待拆解的楼层模型信息数组
        //defaultLocation: locationMap,                                 // 可选 建筑物位置坐标
        heightFromGround: params.ldgd,                                  //必选 建筑物整体离地高度        单位：米
        //单个楼层垂直方向共移动3米，且每次移动0.3米耗时30毫秒
        verticalMoveDistance: params.vzjl,                            //必选 单个楼层垂直移动总距离    单位：米
        verticalMoveOnceDistance: params.vomd,                        //可选 单个楼层每次垂直移动距离  单位：米 
        verticalMoveOnceCostTime: params.vomt,                        //可选 单个楼层每次垂直移动耗时  单位：毫秒
        //单个楼层水平方向共移动40米，且每次移动0.5米耗时20毫秒
        levelMoveDistance: params.lzjl,                               //可选 单个楼层水平移动总距离    单位：米
        levelMoveOnceDistance: params.lomd,                           //可选 单个楼层每次水平移动距离  单位：米 
        levelMoveOnceCostTime: params.lomt,                           //可选 单个楼层每次水平移动耗时  单位：毫秒
        levelMoveDirection:params.direction,                          //可选 单个楼层水平移动方向      取值：1东 2西 3南 4北
        camerafocus: params.camerpo,                                  //整栋楼时定位相机位置
        singlefocus:params.singlefocus                                //单个房间的定位距离
    });
    if (readyState) {
        //console.log('楼层信息已经初始化！');
    } else {
        //清空自定义对象
        // await window.__g.customObject.clear();
        //初始化待拆解楼层对象
        customObjectDataArr = await dismantleBuildingObj.addCustomObjects();
        //customObjectIdArr = dismantleBuildingObj.customObjectIds;
        readyState = true;

    }

}

//楼层升起
export async function samples_floorMoveUp() {
    //console.log("samples_floorMoveUp");
    //是否已经创建示例所需3dt   
    if (readyState) {
        //console.log("samples_floorMoveUp");
        //重新赋值
        //dismantleBuildingObj.cObjectDatas = customObjectDataArr;
        //dismantleBuildingObj.customObjectIds = customObjectIdArr;
        //dismantleBuildingObj.IsOnReady = readyState;
        //楼层下降 伸缩杆动画
        await dismantleBuildingObj.moveUp();
    } else {
        //console.log("%c 请先执行初始化楼层 MoveUp!", "color:green");
    }
}

//楼层下降
export async function samples_floorMoveDown() {
    if (readyState) {
        //重新赋值
        //dismantleBuildingObj.cObjectDatas = customObjectDataArr;
        //dismantleBuildingObj.customObjectIds = customObjectIdArr;
        //dismantleBuildingObj.IsOnReady = readyState;
        dismantleBuildingObj.moveDown();
        readyState = false;
        dismantleBuildingObj = null;
    }
    else {
        //console.log('请先执行初始化楼层 MoveDown！');
    }
}

//楼层水平抽出
export async function samples_floorMovePull(floor) {
    if (readyState) {
        let floorIndex = parseInt(floor) - 1;//待拆解的楼层模型信息数组索引
        let floorState = true;//楼层状态：true 抽出状态 false 正常状态
       await dismantleBuildingObj.levelShift({ val: floorIndex, status: floorState });
    } else {
        //console.log('请先执行初始化楼层 MovePull！');
    }
}

//楼层隐藏上面
export async function samples_floorMovePullHide(floor) {
    let g = window.__g
    if (readyState) {
        let arrs = useFloorStore().roomArr
        console.log(arrs,floor,'6666')
        let floorIndex = parseInt(floor) - 1;//待拆解的楼层模型信息数组索引
        let floorState = true;//楼层状态：true 抽出状态 false 正常状态
        arrs.forEach((item,index) => {
            if(floorIndex===index){
                g.customObject.show(item.ObjectIDs[0]) 
                if(item.Camera&&item.Camera?.length){
                g.camera.set(item.Camera, 1);
                }
            }else{
                if(index>floorIndex){
                    g.customObject.hide(item.ObjectIDs[0]) 
                }else{
                    g.customObject.show(item.ObjectIDs[0]) 
                }
                
            }
        });

    //    await dismantleBuildingObj.levelShift({ val: floorIndex, status: floorState });
    } else {
        //console.log('请先执行初始化楼层 MovePull！');
    }
}


