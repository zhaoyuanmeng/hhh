<!-- 
 * @description: 空间查询
 * @fileName: query/spatial/Query.vue
 * @author: yuanhaijun
 * @date: 2023-01-10
!-->
<template>
    <div>
        <!-- <div v-show="!isShowDetail && !editHelper.flag" class="cloud-func">
            <div class="func-title">
                空间查询
                <el-icon class="" @click.stop="handleClose" style="cursor:pointer">
                    <CircleClose />
                </el-icon>
            </div>

            <el-form @submit.prevent :model="fModel" :rules="fRules" ref="fForm" size="mini" label-position="left"
                class="func-warp">
                <el-scrollbar>
                    <el-form-item v-if="!showR" prop="type" class="warp-type" @submit.prevent>
                        <div v-for="(item, index) in typeList" :key="index" @click="handleChangeTYP(item)"
                            :class="{ ck: item.value == fModel.type }" class="item">
                            <img :src="item.url" />
                            <span> {{ item.name }}</span>
                        </div>
                    </el-form-item>
                    <el-input v-model="selectMarkerValue" class="w-50 m-2" size="large" placeholder="输入关键字"
                        @change="selectMarker" />
                    <div class="sumTileLayerInfo">
                        <div class="tileLayer">图层数量：{{ tileLayerNameList.length }}</div>
                        <div class="object">构建数量：{{ objectNameList.length }}</div>
                    </div>
                    <el-form-item prop="list" class="warp-l">
                        <div v-for="(item, index) in tileLayerNameList" :key="index" class="item"
                            @click="handleClickMarker(item)">
                            <span>{{ item.text }}</span>
                        </div>
                        <div v-if="tileLayerNameList.length == 0" style="width: 100%;text-align: center;color: #b7bbc2;">
                            无数据！
                        </div>
                    </el-form-item>
                </el-scrollbar>
                <el-form-item class="btn">
                    <el-button @click="clearMarker()">清除</el-button>
                </el-form-item>
            </el-form>
        </div> -->

        <!-- 操作按钮 -->
        <!-- <ActionButtonTmpl v-if="editHelper.flag" :editHelper="editHelper" /> -->
        <!-- detail -->
        <!-- <Detail v-if="isShowDetail" :rowData="rowData" /> -->
    </div>
    <StatisticAnalysis />
</template>

<script>
import { get } from "@/utils/fetch";
import { deepTree } from "@/utils/util";
import { tableToExcel } from "@/utils/funs";
import { mapState } from "pinia";
import { useSysToolsCimStore } from '@/stores/sysToolsCim'
import { useAirCityStore } from '@/stores/aircity'
import { CircleClose, Search } from '@element-plus/icons-vue'
import Detail from "./Detail.vue"
import ActionButtonTmpl from "../../common/ActionButton.tmpl"
import { getCommunityData } from '@/api/dataApi'
import StatisticAnalysis from '../statisticAnalysis1/StatisticAnalysis.vue'
import * as turf from '@turf/turf'
export default {
    name: "SpatialQuery",
    data() {
        return {
            editHelper: {
                flag: false,
                type: "shape"
            },
            typeList: [
                {
                    name: "多边形",
                    url: require("@/assets/images/cloud/model-2.png"),
                    value: "polygon",
                },
                // {
                //     name: "线段",
                //     url: require("@/assets/images/cloud/model-1.png"),
                //     value: "line",
                // },
                // {
                //     name: "圆",
                //     url: require("@/assets/images/cloud/model-4.png"),
                //     value: "circular",
                // },
            ],
            fModel: {
                type: "",
                coords: [],
                buffer: 0,
                tableName: "", // 基础数据
            },
            fRules: {
                type: [
                    { required: true, message: "请选择查询方式", trigger: "change" },
                ],
            },
            showR: false, // 显示查询结果
            rList: [], // 查询结果
            rowData: [],
            isShowDetail: false,
            coordinates: [],
            markerArr: [],
            tileLayerNameList: [],
            tileLayerList: [],
            objectNameList: [],
            selectMarkerValue: '',
            tileLayerInfoData: [],
            polygon3dArr: []
        };
    },
    components: {
        // Detail,
        // ActionButtonTmpl,
        // CircleClose,
        StatisticAnalysis
    },
    computed: {
        // ...mapGetters(["eventSealAPI", "treeData"]),
        ...mapState(useSysToolsCimStore, ["eventSealAPI", "treeData"]),
        ...mapState(useAirCityStore, ["tileLayerInfo", "layerTreeIdKeyObject"]),
        // 基础数据
        basicData() {
            let dirTree = this.treeData.basedataDirTree || [];
            // TODO 直接显示数据，不显示目录（后期改为组件选数据）
            let dList = deepTree(dirTree, [], "childrens");
            return dList.map(i => { if (i.tableName) return i; }).filter(i => i);
        },
    },
    watch: {
        eventSealAPI: {
            indeterminate: true,
            deep: true,
            handler(val) {
                if (val.eventtype === "LeftMouseButtonClick" && val.Id) {
                    if (["tag"].includes(val.Type)) {
                        let id = val.Id.replace(
                            `${(val.Type || "").toLocaleLowerCase()}_`,
                            ""
                        )
                        this.rowData = this.rList.find((item) => item.grid_id === id);
                        this.isShowDetail = true;
                    }
                } else if (val.eventtype === "Measurement" && val.Type === "Coordinate") {
                    this.$nextTick(async () => {
                        await window.sealAPI._tools.stopMeasurement();
                        // this.$set(this.fModel, "coords", [val.Coordinate]);
                        this.fModel.coords = [val.Coordinate]
                        this.handleRP("add");
                    });
                }
            }
        },
    },
    mounted() {
        this.tileLayerInfo.forEach((item) => {
            const [minX, minY, minZ, maxX, maxY, maxZ] = item.bbox
            const leftTop = [minX, maxY]
            const leftButtom = [minX, minY]
            const rightButtom = [maxX, minY]
            const rightTop = [maxX, maxY]
            this.tileLayerInfoData.push({ id: item.id, bboxCoordinate: [leftTop, leftButtom, rightButtom, rightTop] })
        })
        this.addPloygon3d()
    },
    methods: {
        async handleChangeBasic(editHelperCoordinates) {
            editHelperCoordinates.push(editHelperCoordinates[0])
            this.tileLayerInfoData.forEach((item) => {
                item.bboxCoordinate.push(item.bboxCoordinate[0])
                const editHelperPoly = turf.polygon([editHelperCoordinates]);

                const polyItem = turf.polygon([item.bboxCoordinate]);

                const intersection = turf.intersect(editHelperPoly, polyItem);
                if (intersection) {
                    this.tileLayerNameList.push({ text: this.layerTreeIdKeyObject[item.id], id: item.id })
                    this.tileLayerList.push({ text: this.layerTreeIdKeyObject[item.id], id: item.id })
                }
            })
            const res = await __g.tileLayer.getObjectIDs(this.tileLayerNameList.map(item => item.id))
            res.data.forEach((item) => {
                if (item.objectIds) {
                    this.objectNameList.push(...item.objectIds)
                }
            })
        },
        handleClickMarker(item) {
            __g.infoTree.focus(item.id);
        },
        selectMarker(value) {
            this.tileLayerNameList = this.tileLayerList.filter(item => {
                if (item.text.toLowerCase().includes(value.toLowerCase())) {
                    return true
                }
            })
        },
        async clearMarker() {
            // const markerIdArr = this.markerArr.map(item => item.id)
            // await __g.marker.delete(markerIdArr)
            this.tileLayerNameList = []
            this.tileLayerList = []
            this.markerArr = []
            this.fModel.type = ''
            await this.handlePlane('clear')
        },
        addPloygon3d() {
            const rainbowColors = ['#FF0000', '#FF7F00', '#FFFF00', '#00FF00', '#0000FF', '#4B0082', '#8A2BE2'];
            getCommunityData().then(async (res) => {
                this.polygon3dArr = []
                __g.polygon3d.clear()
                res.features.forEach((element, index) => {
                    let o1 = {
                        id: 'getCommunityData' + index,
                        coordinates: element.geometry.rings,
                        coordinateType: 0,//坐标系类型，取值范围：0为Projection类型，1为WGS84类型，2为火星坐标系(GCJ02)，3为百度坐标系(BD09)，默认值：0 
                        color: rainbowColors[index % rainbowColors.length - 1],        //颜色值
                        height: 100,                //3D多边形的高度
                        intensity: 1.0,             //亮度
                        style: 10, //3DPolygon的样式 请参照API开发文档选取枚举
                        tillingX: 0, //可选，仅当3DPolygon的样式支持贴图显示，贴图横向平铺  
                        tillingY: 0,  //可选，仅当3DPolygon的样式支持贴图显示，贴图纵向平铺
                        generateTop: true, //是否生成顶面
                        generateSide: true,//是否生成侧面
                        generateBottom: true//是否生成底面
                    }
                    this.polygon3dArr.push(o1)
                })
                await __g.polygon3d.add(this.polygon3dArr)
                __g.polygon3d.focus(this.polygon3dArr[0].id)
            })
        },
        // PRM：点
        defaultOptionD() {
            return {
                imagePath: require("@/assets/images/cloud/grid/point.png"), // 图片路径
                imageSize: [20, 20], // 图片宽高[width,height]
                range: [-9007199254740991, 9007199254740991], // 标签的可见范围 [Min,Max]
                showLine: true, // 标签下方是否显示垂直牵引线
                textSize: 12, // 文字大小
                textRange: [100, 6000], // 文字的可见范围
                textColor: Color.White, // 文字颜色
                textBackgroundColor: [0, 0, 0, 0.5], // 文本背景颜色
                autoHeight: true, // 自动判断下方是否有物体
            };
        },
        // PRM：线
        defaultOptionX() {
            return {
                range: [-9007199254740991, 9007199254740991], // 可视范围
                color: [0.396094, 0.748619, 0.921822, 0.4], // 折线颜色
                style: PolylineStyle.Normal, // 折线样式
                thickness: 4, // 折线宽度
                intensity: 1, // 亮度[0~1000]
                flowRate: 0, // 流速[0~1.0]
                tiling: 0, // 材质贴图平铺比例
                shape: 0, // 折线类型 0：直线， 1：曲线
                depthTest: false, // 是否做深度检测
            };
        },
        // PRM：面
        defaultOptionM() {
            return {
                // id: "",
                color: [0.396094, 0.748619, 0.921822, 0.1], // 面颜色值
                // coordinates: [], // 多边形坐标数组
                coordinateType: 0, // 坐标系类型
                range: [-9007199254740991, 9007199254740991], // 可视范围
                frameColor: [0.396094, 0.748619, 0.921822, 0.4], // 边框颜色
                frameThickness: 2, // 边框厚度[0~100]
                depthTest: false, // 深度检测
                intensity: 1, // 亮度[0~1000]
                style: PolygonStyle.SingleColor, // 多边形样式
            };
        },
        // 字符串截取（转数组）
        stringToArray(str) {
            let indexS = str.lastIndexOf("(");
            let indexE = str.indexOf(")");
            str = str.substr(indexS + 1, indexE - indexS - 1);
            let arr = str.split(",").map((item) => item.split(" "));
            let data = [];
            arr.forEach((item) => {
                let _c = item.map((i0) => Number(i0));
                data.push([_c[0], _c[1], _c[2] || 0.1]);
            });
            return data;
        },
        // 类型：点、线、面
        async handleChangeTYP(data) {
            // if (data.value === "circular") return;

            let that = this;
            await that.clearEffect();
            // 赋值、校验
            // that.$set(that.fModel, "type", data.value);
            this.fModel.type = data.value
            setTimeout(async () => {
                that.$refs.fForm.validateField("type");
                // 拾取坐标
                if (["line", "polygon"].includes(that.fModel.type)) {
                    // that.$set(that.editHelper, 'type', that.fModel.type === "polygon" ? "shape" : "lines");
                    // that.$set(that.editHelper, 'flag', true);
                    that.editHelper.type = that.fModel.type === "polygon" ? "shape" : "lines"
                    that.editHelper.flag = true
                } else {
                    await window.sealAPI._tools.startMeasurement();
                    let options = {
                        lineSize: 3.0,
                        pointSize: 8.0,
                        textColor: Color.Yellow,
                        pointColor: [0, 0, 1, 0.3],
                        lineColor: Color.Red,
                        areaColor: [0, 1, 0, 0.3],
                        showCoordinateText: false,
                    };
                    await window.sealAPI._tools.setMeasurement(MeasurementMode.Coordinate, options);
                }
            }, 200);
        },
        // 获取编辑助手返回数据
        getEditDate(coordinates) {
            // this.$set(this.editHelper, 'flag', false);

            // this.$set(this.fModel, "coords", coordinates);
            this.editHelper.flag = false
            this.fModel.coords = coordinates
            // this.handleChangeBasic(this.fModel.coords)
            this.handleChangeBasic(coordinates)
            this.$nextTick(() => {
                switch (this.fModel.type) {
                    case "line":
                        this.handleLine("add");
                        break;
                    case "polygon":
                        this.handlePlane("add");
                        break;
                }
            });
        },
        // 关闭编辑助手
        closeEditHelper() {
            // this.$set(this.editHelper, 'flag', false);
            this.editHelper.flag = false
        },
        // 线面：清除效果
        async clearEffect() {
            // this.$set(this.fModel, "coords", []);
            this.fModel.coords = []

            if (this.rList.length > 0) {
                await this.handlePoint("clear");
            }

            switch (this.fModel.type) {
                case "circular":
                    await window.sealAPI._tools.stopMeasurement();
                    await this.handleRP("clear");
                    break;
                case "line":
                    await this.handleLine("clear");
                    break;
                case "polygon":
                    await this.handlePlane("clear");
                    break;
            }
        },
        // 辐射圈：添加/移除
        async handleRP(opr) {
            switch (opr) {
                case "add":
                    {
                        if (this.fModel.coords.length === 0) {
                            return this.$message.warning("未获取到坐标，请在场景中拾取！");
                        }
                        let prm = {
                            id: `radiationPoint_0`,
                            coordinate: this.fModel.coords.flat(2),
                            radius: Number(this.fModel.buffer), // 辐射圈的半径，取值范围：[0~500000]，单位：米
                            rippleNumber: 0, // 波纹数量，取值范围：[0~5]，单位：个
                            color: "#00FFFF", // 颜色
                            intensity: 0.06, // 亮度，取值范围：[0~1.0]
                        }
                        let obj = new RadiationPointData(
                            prm.id,
                            prm.coordinate,
                            prm.radius,
                            prm.rippleNumber,
                            prm.color,
                            prm.intensity
                        )
                        let res = await window.origAPI.radiationPoint.get(`radiationPoint_0`);
                        if (!res.data) await window.origAPI.radiationPoint.add(obj);
                        else await window.origAPI.radiationPoint.update(obj);
                        break;
                    }
                case "clear":
                    await window.origAPI.radiationPoint.clear();
                    break;
            }
        },
        // 点：添加/移除 标签
        async handlePoint(opr, data) {
            switch (opr) {
                case "add":
                    {
                        let _type = Object.prototype.toString.call(data);
                        if (_type === "[object Array]") {
                            let arr = [];
                            data.forEach((item) => {
                                if (!item.th_geom) return;
                                arr.push({
                                    id: `tag_${item.grid_id}`,
                                    coordinate: this.stringToArray(item.th_geom).flat(2),
                                    text: item.feature_name || item.name,
                                    ...this.defaultOptionD(),
                                });
                            });
                            window.origAPI.tag.add(arr, () => {
                                window.origAPI.tag.focusAll();
                            });
                        } else if (_type === "[object Object]") {
                            let res = await window.origAPI.tag.get(`tag_${data.grid_id}`);
                            if (!res.data) {
                                if (!data.th_geom) return this.$message.warning("未找到坐标值！");
                                await window.origAPI.tag.add({
                                    id: `tag_${data.grid_id}`,
                                    coordinate: this.stringToArray(data.th_geom).flat(2),
                                    text: data.feature_name || data.name,
                                    ...this.defaultOptionD(),
                                });
                            }
                            setTimeout(() => {
                                window.origAPI.tag.focus(`tag_${data.grid_id}`);
                            }, 200);
                        }
                        break;
                    }
                case "clear":
                    await window.origAPI.tag.clear();
                    break;
            }
        },
        // 线：添加/移除
        async handleLine(opr) {
            switch (opr) {
                case "add":
                    await window.origAPI.polyline.add({
                        id: `polyline_0`,
                        coordinates: this.fModel.coords,
                        ...this.defaultOptionX(),
                    });
                    break;
                case "clear":
                    await window.origAPI.polyline.clear();
                    break;
            }
        },
        // 面：添加/移除
        async handlePlane(opr) {
            switch (opr) {
                case "add":
                    await window.origAPI.polygon.add({
                        id: `polygon_0`,
                        coordinates: this.fModel.coords,
                        ...this.defaultOptionM(),
                    });

                    break;
                case "clear":
                    await window.origAPI.polygon.clear();
                    break;
            }
        },
        // 点线面：获取坐标
        getPoint() {
            let coord = [];
            (this.fModel.coords || []).forEach((item) => {
                let _type = Object.prototype.toString.call(item);
                if (_type === "[object Array]") coord.push(`${item[0]} ${item[1]}`);
            });
            if (["line", "polygon"].includes(this.fModel.type)) {
                if (coord.length > 1) coord.push(coord[0]);
                else return this.$message.warning("请绘制查询范围！");
            } else if (coord.length === 0) this.$message.warning("请绘制查询范围！");
            coord = coord.join(",");

            switch (this.fModel.type) {
                case "circular":
                    return `POINT(${coord})`;
                case "line":
                    return `LINESTRING(${coord})`;
                case "polygon":
                    return `POLYGON((${coord}))`;
            }
        },
        // BTN：查询
        handleInquire() {
            this.$refs.fForm.validate(async (valid) => {
                if (valid) {
                    let tableName = this.fModel.tableName;
                    if (!tableName)
                        return this.$message.warning("请选择数据！");
                    if (this.fModel.coords.length === 0)
                        return this.$message.warning("未获取到坐标，请在场景中拾取！");

                    let params = {
                        wktStr: this.getPoint(),
                        radius: this.fModel.buffer,
                        tableName: tableName,
                    };
                    let res = await get(
                        `${cim_main}fusion/gridAnalysisWithBuffer`,
                        params
                    );
                    if (!res || !res.message) return;

                    this.rList = res.message;
                    this.showR = true;
                    this.$nextTick(() => {
                        if (this.rList.length > 0) this.handlePoint("add", this.rList);
                    });
                }
            });
        },
        handleClose() {
            // if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
            this.$emit('close')
        },
    },
    async beforeUnmount() {
        await this.clearEffect()
        await this.clearMarker()
        __g.polygon3d.clear()
    },
};
</script>

<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";

.func-warp {

    /deep/.el-form-item__error {
        padding-top: 3px;
    }

    /deep/.el-scrollbar {
        min-height: 326px;
        max-height: 64vh;
        overflow-y: auto;
        margin: 18px 0;

        .el-scrollbar__wrap {
            width: 100%;
        }

        .el-form-item:last-child {
            margin-bottom: 0;
        }
    }

    /deep/.warp-type {
        margin-bottom: 24px;

        .el-form-item__content {
            margin-left: 0 !important;
            display: flex;
            justify-content: center;
            width: 100%;

            &>div.item {
                width: 68px;
                height: 68px;
                background: #5c6067;
                box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
                    0px 0px 0px 0px rgba(0, 0, 0, 0.5);
                border-radius: 7px;
                backdrop-filter: blur(20px);
                border: 1px solid transparent;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                cursor: pointer;

                img {
                    width: 24px;
                    height: 24px;
                }

                span {
                    font-size: 12px;
                    color: #eeeeee;
                    line-height: normal;
                    margin-top: 6px;
                }

                &.ck {
                    background-color: #535d84;
                    border-color: #0078d4;
                }
            }

            &>div.item:nth-child(2) {
                margin: auto;
            }
        }
    }

    /deep/.warp-buf {
        .el-input__inner {
            border-right: 0;
            border-top-right-radius: 0;
            border-bottom-right-radius: 0;
        }

        .el-input-group__append {
            border-color: #646b6f;
            background: #494d52;
        }
    }

    /deep/.warp-l {
        border-top: 1px solid #97979766;
        padding-top: 24px;
        display: flex;
        flex-direction: column;
        align-items: center;

        // .listBox {
        //     width: 100%;
        //     max-height: 300px;
        //     display: flex;
        //     flex-direction: column;
        //     align-items: center;
        //     overflow: scroll;
        // }

        .el-form-item__content {
            margin-left: 0 !important;
            max-height: 300px;
            overflow: scroll;

            &>div.item {
                // display: flex;
                // align-items: center;
                height: 30px;
                line-height: 30px;
                overflow-x: hidden;
                cursor: pointer;
                text-align: center;

                &:hover {
                    background-color: rgba($color: #fff, $alpha: 0.5);
                }

                img {
                    width: 16px;
                }

                span {
                    text-overflow: ellipsis;
                    white-space: nowrap;
                    overflow: hidden;
                    font-size: 14px;
                    color: #eeeeee;
                    margin-left: 10px;
                }
            }
        }
    }

}

.sumTileLayerInfo {
    width: 100%;
    margin-top: 10px;
    justify-content: space-around;
    display: flex;
}
</style>