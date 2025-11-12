class DismantleBuilding {
    //构造器参数的默认值
    constructor({

        floorInfoArr,                    //必选 待拆解的楼层模型信息数组
        defaultLocation = new Map(),     //可选 建筑物位置坐标           单位：[X,Y,Z] 注意坐标系
        heightFromGround = 10,           //可选 建筑物整体离地高度        单位：米
        verticalMoveDistance = 5,        //可选 单个楼层垂直移动总距离    单位：米
        verticalMoveOnceDistance = 0.25, //可选 单个楼层每次垂直移动距离  单位：米 
        verticalMoveOnceCostTime = 30,   //可选 单个楼层每次垂直移动耗时  单位：毫秒 

        levelMoveDistance = 4,           //可选 单个楼层水平移动总距离    单位：米
        levelMoveOnceDistance = 1,       //可选 单个楼层每次水平移动距离  单位：米 
        levelMoveOnceCostTime = 20,      //可选 单个楼层每次水平移动耗时  单位：毫秒

        levelMoveDirection = 4,          // 暂未开放 可选 单个楼层水平移动方向      取值：1东 2西 3南 4北
        camerafocus = { diatance: 100, camera: [-15, -120, 0] }, //可选，楼层升起后相机的定位到建筑的视角和距离
        singlefocus=60                                     //可选，单个房间的定位距离

    }) {
        this.floorInfoArr = floorInfoArr;                                   // 待拆解的楼层模型信息数组：包含图层id和模型id
        //this.defaultLocation = defaultLocation;                           // 建筑物位置坐标  注意坐标系类型
        this.heightFromGround = heightFromGround;                           // 建筑物整体离地高度  单位：米
        //垂直移动参数
        this.verticalMoveDistance = verticalMoveDistance;                   // 单个楼层垂直移动总距离    单位：米
        this.verticalMoveOnceDistance = verticalMoveOnceDistance;           // 单个楼层每次垂直移动距离  单位：米 
        this.verticalMoveOnceCostTime = verticalMoveOnceCostTime;           // 单个楼层每次垂直移动耗时  单位：毫秒 
        //水平移动参数
        this.levelMoveDistance = levelMoveDistance;                         // 单个楼层水平移动总距离    单位：米
        this.levelMoveOnceDistance = levelMoveOnceDistance;                 // 单个楼层每次水平移动距离  单位：米
        this.levelMoveOnceCostTime = levelMoveOnceCostTime;                 // 单个楼层每次水平移动耗时  单位：毫秒

        this.levelMoveDirection = levelMoveDirection;                       // 单个楼层水平移动方向：1东2西3南4北
        this.camerafocus = camerafocus;                                     //升起时相机定位视角和距离
        this.singlefocus =singlefocus;                                       //单个房间的定位距离

        this.cObjectDatas = null;                                           // 记录打开后状态的自定义对象数据
        this.customObjectIds = null;                                        // 存储customobjectid 用于相机定位
        this.isClickVal = -1;                                               // 需要抽出的楼层层数
        this.IsOnReady = false;                                             // 是否添加自定义对象并获取坐标完毕  
        this.isClose = true;                                                // 是否处于关闭状态
        this.isStopRun = true;                                              // 是否处于不在运动状态
        this.isClickLayerClose = false;                                     // 当前楼层是否已关闭
        this.eachlayerNumber = 1;                                           // 每层模型actor数量

    }

    // 添加自定义对象
    async addCustomObjects() {

        // 自定义对象id数组
        let customObjectIds = [];
        // 自定义对象数据数组
        let customObjectDatas = [];
        // 添加自定义对象
        this.floorInfoArr.forEach(item => {

            if (item.ObjectIDs.length > this.eachlayerNumber) this.eachlayerNumber = item.ObjectIDs.length;
            for (let value of item.ObjectIDs) {
                //let locationObj = this.defaultLocation.get(value);
                let o = {
                    id: value,
                    tileLayerId: item.Id,
                    objectId: value,
                    smoothMotion: 0, //1: 平滑插值，0: 跳跃
                    //location: locationObj,
                    // rotation: [0, 0, 0],
                    //scale: [1, 1, 1]
                };
                customObjectDatas.push(o);
                customObjectIds.push(value);
            }
        });
        //console.log('addByTileLayer初始化待拆解楼层对象');
        await window.__g.customObject.addByTileLayer(customObjectDatas);

        let data = { "id": this.floorInfoArr[0].Id, "objectIds": customObjectIds };
        await window.__g.tileLayer.hideActors(data);

        //console.log('hideActors初始化待拆解楼层对象');
        this.customObjectIds = customObjectIds;

        // 获取添加的自定义对象的信息
        let customObjectData = await window.__g.customObject.get(customObjectIds);
        this.cObjectDatas = customObjectData.data;
        this.IsOnReady = true;
        return customObjectData.data;
    }

    upImplementation() {
        // 打开位移状态
        this.isStopRun = false;
        let index = 0;
        this.timer = setInterval(async () => {
            let i = index * this.verticalMoveOnceDistance;
            if (i >= this.verticalMoveDistance) {
                i = this.verticalMoveDistance;
            }
            window.__g.customObject.updateBegin();
            const setLocation = (j, length) => {
                window.__g.customObject.setLocation(this.cObjectDatas[j].id, [
                    this.cObjectDatas[j].location[0],
                    this.cObjectDatas[j].location[1],
                    this.cObjectDatas[j].location[2] + this.heightFromGround + i * j
                ]);
                if (++j < length) {
                    setLocation(j, length);
                }
            };

            //设置楼层位移动画效果
            if (this.cObjectDatas instanceof Array) {
                setLocation(0, this.cObjectDatas.length);
            } else {
                //console.log("%c 数据未初始化成功", "color:red");
            }
            window.__g.customObject.updateEnd();
            index++;
            if (i >= this.verticalMoveDistance) {
                this.isClose = false;
                // 关闭位移状态
                this.isStopRun = true;
                clearInterval(this.timer);
                //切换到到合适的视角
                window.__g.customObject.focus(this.customObjectIds, this.camerafocus.diatance,
                    2,
                    this.camerafocus.camera)
                    // window.__g.customObject.focus(this.customObjectIds, 0,
                    //     2)
            }
        }, this.verticalMoveOnceCostTime);
    }
    // 楼层升起
    async moveUp() {
        if (this.IsOnReady) {
            if (this.isStopRun) {
                if (this.isClose) {
                    //("moveup");
                    this.upImplementation();
                } else {
                    //console.log("%c 已经是开启状态", "color:red");
                }
            } else {
                //console.log("%c 正处于位移状态，请稍后", "color:red");
            }
        } else {
            //console.log("%c 尚未获取到模型信息，请稍后", "color:red");
        }
    }
    downImplementation() {
        // 打开位移状态
        this.isStopRun = false;
        let index = 0;
        this.timer1 = setInterval(async () => {
            let i = this.verticalMoveDistance - index * this.verticalMoveOnceDistance;
            if (i <= 0) {
                i = 0;
            }
            window.__g.customObject.updateBegin();
            const setLocation = (j, length) => {
                window.__g.customObject.setLocation(this.cObjectDatas[j].id, [
                    this.cObjectDatas[j].location[0],
                    this.cObjectDatas[j].location[1],
                    this.cObjectDatas[j].location[2] +
                    i * (parseInt(j / this.eachlayerNumber)),
                ]);
                if (++j < length) {
                    setLocation(j, length);
                }
            };
            if (this.cObjectDatas instanceof Array) {
                setLocation(0, this.cObjectDatas.length);
            } else {
                // console.log("%c 数据未初始化成功", "color:red");
            }
            window.__g.customObject.updateEnd();
            index++;
            if (i <= 0) {
                // 楼层抽出的数据
                this.isClickVal = -1;
                this.isClose = true;
                clearInterval(this.timer1);
                this.reset();
                // 关闭位移状态
                this.isStopRun = true;
            }
        }, this.verticalMoveOnceCostTime);
    }
    // 楼层下降
    async moveDown() {
        if (this.IsOnReady) {
            if (this.isStopRun) {
                if (!this.isClose) {
                    this.downImplementation();

                } else {
                    // console.log("%c 已经是关闭状态", "color:red");
                }
            } else {
                //console.log("%c 正处于位移状态，请稍后", "color:red");
            }
        } else {
            //console.log("%c 尚未获取到模型信息，请稍后", "color:red");
        }
    }
    //拉出
    pullout(val) {
        // 打开位移状态
        this.isStopRun = false;
        let i = this.levelMoveOnceDistance;
        //定位
        let focusObjects = [];
        for (let k = 0; k < this.eachlayerNumber; k++) {
            focusObjects[k] = this.cObjectDatas[val * this.eachlayerNumber + k].id;
        }
        this.timer2 = setInterval(async () => {
            if (i > this.levelMoveDistance) {
                i = this.levelMoveDistance;
            }

            // window.__g.customObject.updateBegin();
            let clickidIndex = val * this.eachlayerNumber;
            switch (this.levelMoveDirection) {
                case 1:
                    for (let j = 0; j < this.eachlayerNumber; j++) {
                        let location = [
                            this.cObjectDatas[clickidIndex + j].location[0] + i,
                            this.cObjectDatas[clickidIndex + j].location[1],
                            this.cObjectDatas[clickidIndex + j].location[2] + this.heightFromGround + this.verticalMoveDistance * val
                        ];
                        window.__g.customObject.setLocation(this.cObjectDatas[clickidIndex + j].id, location);
                    }
                    break;
                case 2:
                    for (let j = 0; j < this.eachlayerNumber; j++) {
                        let location = [
                            this.cObjectDatas[clickidIndex + j].location[0] - i,
                            this.cObjectDatas[clickidIndex + j].location[1],
                            this.cObjectDatas[clickidIndex + j].location[2] + this.heightFromGround + this.verticalMoveDistance * val
                        ];
                        window.__g.customObject.setLocation(this.cObjectDatas[clickidIndex + j].id, location);
                    }
                    break;
                case 3:
                    for (let j = 0; j < this.eachlayerNumber; j++) {
                        let location = [
                            this.cObjectDatas[clickidIndex + j].location[0],
                            this.cObjectDatas[clickidIndex + j].location[1] - i,
                            this.cObjectDatas[clickidIndex + j].location[2] + this.heightFromGround + this.verticalMoveDistance * val
                        ];
                        window.__g.customObject.setLocation(this.cObjectDatas[clickidIndex + j].id, location);
                    }
                    break;
                case 4:
                    for (let j = 0; j < this.eachlayerNumber; j++) {
                        let location = [
                            this.cObjectDatas[clickidIndex + j].location[0],
                            this.cObjectDatas[clickidIndex + j].location[1] + i,
                            this.cObjectDatas[clickidIndex + j].location[2] + this.heightFromGround + this.verticalMoveDistance * val
                        ];
                        window.__g.customObject.setLocation(this.cObjectDatas[clickidIndex + j].id, location);
                    }
                    break;
            }


            if (i >= this.levelMoveDistance) {
                // 关闭位移状态
                this.isStopRun = true;
                this.isClickLayerClose = false;
                clearInterval(this.timer2);
                //移动到合适的视角
                // window.__g.customObject.focus(focusObjects, 40,
                //     2,
                //     [-40, -100, 0]);
                window.__g.customObject.focus(focusObjects, this.singlefocus,
                    2);
                return;
            }
            // window.__g.customObject.updateEnd();

            i += this.levelMoveOnceDistance;
        }, this.levelMoveOnceCostTime);
    }
    //水平推入
    pullin(val, isequal) {
        let ii = this.levelMoveDistance;
        this.timer4 = setInterval(async () => {
            if (ii <= 0) {
                ii = 0;
            }
            // window.__g.customObject.updateBegin();
            let clickidIndex = val * this.eachlayerNumber;
            switch (this.levelMoveDirection) {
                case 1:
                    for (let j = 0; j < this.eachlayerNumber; j++) {
                        let location = [
                            this.cObjectDatas[clickidIndex + j].location[0] + ii,
                            this.cObjectDatas[clickidIndex + j].location[1],
                            this.cObjectDatas[clickidIndex + j].location[2] + this.heightFromGround + this.verticalMoveDistance * val
                        ];
                        window.__g.customObject.setLocation(this.cObjectDatas[clickidIndex + j].id, location);
                    }
                    break;
                case 2:
                    for (let j = 0; j < this.eachlayerNumber; j++) {
                        let location = [
                            this.cObjectDatas[clickidIndex + j].location[0] - ii,
                            this.cObjectDatas[clickidIndex + j].location[1],
                            this.cObjectDatas[clickidIndex + j].location[2] + this.heightFromGround + this.verticalMoveDistance * val
                        ];
                        window.__g.customObject.setLocation(this.cObjectDatas[clickidIndex + j].id, location);
                    }
                    break;
                case 3:
                    for (let j = 0; j < this.eachlayerNumber; j++) {
                        let location = [
                            this.cObjectDatas[clickidIndex + j].location[0],
                            this.cObjectDatas[clickidIndex + j].location[1] - ii,
                            this.cObjectDatas[clickidIndex + j].location[2] + this.heightFromGround + this.verticalMoveDistance * val
                        ];
                        window.__g.customObject.setLocation(this.cObjectDatas[clickidIndex + j].id, location);
                    }
                    break;
                case 4:
                    for (let j = 0; j < this.eachlayerNumber; j++) {
                        let location = [
                            this.cObjectDatas[clickidIndex + j].location[0],
                            this.cObjectDatas[clickidIndex + j].location[1] + ii,
                            this.cObjectDatas[clickidIndex + j].location[2] + this.heightFromGround + this.verticalMoveDistance * val
                        ];
                        window.__g.customObject.setLocation(this.cObjectDatas[clickidIndex + j].id, location);
                    }
                    break;

            }

            if (ii <= 0) {
                // 关闭位移状态
                this.isStopRun = true;
                if (isequal) {
                    this.isClickLayerClose = true;
                    this.isClickVal = null;
                }
                clearInterval(this.timer4);
                return;
            }
            // window.__g.customObject.updateEnd();
            ii -= this.levelMoveOnceDistance;
        }, this.levelMoveOnceCostTime);
    }

    // 水平拉出楼层或水平推入楼层
    async levelShift({ val, status }) {
        if (!this.IsOnReady || !this.isStopRun || this.isClose) {
            return;
        }
        let oldClickVal = this.isClickVal;
        this.isClickVal = val;
        //this.isClickData = this.cObjectDatas[val];
        if (status && oldClickVal !== val) {
            this.pullout(val)
            // 抽出楼层时关闭其他楼层
            if (
                !this.isClickLayerClose && (oldClickVal > -1)
            ) {
                // console.log(oldClickVal, "抽出楼层时关闭其他楼层");
                this.pullin(oldClickVal, false)
            }
        } else if (oldClickVal === val) {
            this.isStopRun = false;
            this.pullin(val, true)
        }

    }
    async reset() {
        if (this.floorInfoArr.length > 0) {
            //console.log("楼宇降落显示出原来的楼" + this.floorInfoArr[0].Id);
            await window.__g.tileLayer.showAllActors(this.floorInfoArr[0].Id);
        }
        //console.log("清除customObject");
        //await window.__g.customObject.clear();
        let customids = [];
        this.floorInfoArr.forEach(el => {
            customids.push(el["ObjectIDs"][0]);
        });

        await window.__g.customObject.delete(customids);
        //再显示一次 防止有处理不过来的情况发生
        if (this.floorInfoArr.length > 0) {
            await window.__g.tileLayer.showAllActors(this.floorInfoArr[0].Id);
        }
    }
}
export default DismantleBuilding