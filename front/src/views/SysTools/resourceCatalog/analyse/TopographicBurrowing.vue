<!--
 * @FileDescription: 辅助分析->挖洞
 * @Author: yuanhaijun
 * @Date: 2022.11.28
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2022.11.28
 -->
<template>
    <div class="cloud-query" v-if="!editHelper.flag">
        <div class="query-title">
            地形挖洞
            <el-icon class="" @click.stop="handleClose">
                <CircleClose />
            </el-icon>
        </div>
        <div class="operate-btns">
            <div class="item">
                <p>地形源</p>
                <el-select v-model="sourceTerrainObject.sourceTerrain" @change="handleChangeSourceTerrain"
                    placeholder="请选择挖洞地形" class="select">
                    <el-option :value="terrain.name" :label="terrain.name"
                        v-for="terrain in sourceTerrainObject.sourceTerrainList" :key="terrain"
                        placement="bottom"></el-option>
                </el-select>
            </div>
            <div class="item">
                <div @click="startDraw()">
                    <img src="@/assets/images/cloud/query/icon-attr.png" alt="属性" />
                    <span>绘制挖洞区</span>
                </div>
            </div>
        </div>
    </div>
    <!-- 操作按钮 -->
    <ActionButtonTmpl v-if="editHelper.flag" :editHelper="editHelper" />
</template>

<script>
import { ElMessage } from 'element-plus';
import { onBeforeUnmount, onMounted, defineEmits, ref } from 'vue'
import ActionButtonTmpl from '../common/ActionButton.tmpl.vue'
import { CircleClose } from '@element-plus/icons-vue'
import { useAirCityStore } from "@/stores/aircity";
import { mapState } from 'pinia'

export default {
    name: 'TopographicBurrowing',
    components: {
        CircleClose,
        ActionButtonTmpl
    },
    data() {
        return {
            sourceTerrainObject: {
                sourceTerrain: '',
                sourceTerrainList: [
                    {
                        name: 'dixing'
                    },
                    {
                        name: 'Topographic image'
                    },
                ]
            },
            editHelper: {
                flag: false,
                type: "shape"
            },
            fModel: {
                type: "",
                coords: [],
                buffer: 0,
                tableName: "", // 基础数据
            },
            holeInfo: {}
        }
    },
    computed: {
        ...mapState(useAirCityStore, ['layerTreeObject'])
    },
    methods: {
        // 获取编辑助手返回数据
        getEditDate(coordinates) {
            this.editHelper.flag = false
            this.fModel.coords = coordinates
            this.getTileLyearByCoordinate(coordinates)
        },
        // 关闭编辑助手
        closeEditHelper() {
            this.editHelper.flag = false
        },
        // 开始绘制
        async startDraw() {
            this.editHelper.flag = true
        },
        async handleClose() {
            this.$emit('close')
        },
        async handleChangeSourceTerrain() {
            // this.$emit('close')
        },
        async getTileLyearByCoordinate(coordinatesBase) {
            let startEndPointArr = [];
            for (let i = 0; i < coordinatesBase.length; i++) {
                let pointBase = coordinatesBase[i];
                let startPoint = [coordinatesBase[i][0], coordinatesBase[i][1], 1000];
                let endPoint = [coordinatesBase[i][0], coordinatesBase[i][1], -1000];
                let obj = { "start": startPoint, "end": endPoint };
                startEndPointArr.push(obj);
            }
            //利用射线求交求采集点高度
            let result = await __g.tools.linesIntersect(startEndPointArr, true, false);

            let coordinates = [];
            let pointArr = result.intersects;
            for (let i = 0; i < pointArr.length; i++) {
                let pointZ = pointArr[i].LineIntersectPoint;
                coordinates.push(pointZ);
            }


            for (let i = 0; i < coordinates.length; i++) {
                coordinates[i][2] = -50 + coordinates[i][2];
            }

            let holeId = "hole";
            await __g.tileLayer.deleteHole(this.holeInfo.id, this.holeInfo.tileLayerId);
            console.log(this.sourceTerrainObject.sourceTerrain,);
            //多个坐标构成内环 三维数组
            const id = holeId + this.sourceTerrainObject.sourceTerrain
            const tileLayerId = this.layerTreeObject[this.sourceTerrainObject.sourceTerrain]
            // let data = [{ 'id': holeId + this.sourceTerrainObject.sourceTerrain, 'tileLayerId': this.layerTreeObject[this.sourceTerrainObject.sourceTerrain], 'coordinates': coordinates, 'isReverseCut': false }];

            let data = [{ 'id': id, 'tileLayerId': tileLayerId, 'coordinates': coordinates, 'isReverseCut': false }];
            await __g.tileLayer.addHole(data);
            this.holeInfo = { id: data[0].id, tileLayerId: data[0].tileLayerId }
            console.log(this.holeInfo, ';lcfcfcfcfcfcfcfcfcfcfcfcf');
            //添加自定义材质的polygon3d
            await __g.polygon3d.clear();
            let side = {
                id: 'polygon3d_side',
                coordinates: coordinates,
                color: [1, 1, 1, 1],        //颜色值
                height: 50,                //3D多边形的高度
                intensity: 1.0,             //亮度
                tillingX: 0, //可选，仅当3DPolygon的样式支持贴图显示，贴图横向平铺  
                tillingY: 0,  //可选，仅当3DPolygon的样式支持贴图显示，贴图纵向平铺
                material: "/JC_CustomAssets/M_侧_1",
                scalarParameters: [{ "name": "U缩放", "value": 9 }],
                //vectorParameters: [{ "name": "color", "value": [1, 0, 0] }],
                generateTop: false,
                generateSide: true,
                generateBottom: false
            }
            await __g.polygon3d.add(side);

            let bottom = {
                id: 'polygon3d_bottom',
                coordinates: coordinates,
                color: [1, 1, 1, 1],        //颜色值
                height: 1,                //3D多边形的高度
                intensity: 1.0,             //亮度
                tillingX: 0, //可选，仅当3DPolygon的样式支持贴图显示，贴图横向平铺  
                tillingY: 0,  //可选，仅当3DPolygon的样式支持贴图显示，贴图纵向平铺
                material: "/JC_CustomAssets/M_底",
                //material: "/JC_CustomAssets/MaterialLibrary/Exhibition/建筑场地/建筑场地_11",
                scalarParameters: [{ "name": "UV", "value": 2 }],
                vectorParameters: [{ "name": "color", "value": [1, 0, 0] }],
                generateTop: true,
                generateSide: true,
                generateBottom: true
            }
            await __g.polygon3d.add(bottom);
            __g.polygon3d.focus('polygon3d_bottom', 300, 1);
        }
    },
    async beforeUnmount() {
        await this.handleClose();
    },
}
</script>

<style lang="scss" scoped>
.box-item {
    width: 100%;
    text-align: center;
    margin-top: 20px;
}



.cloud-query {
    position: fixed;
    top: 50%;
    right: 33px;
    transform: translateY(-50%);
    z-index: 5;
    width: 300px;
    // background: rgba(46, 52, 65, 0.8);
    // box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
    //     0px 0px 0px 0px rgba(0, 0, 0, 0.5), 0px 2px 5px 0px rgba(38, 38, 44, 0.5);
    // border-radius: 15px;
    background: rgba(0, 13, 31, 0.4);
    box-shadow: 0px 2px 8px 0px rgba(0, 31, 44, 0.3);
    border: 1px solid rgba(44, 225, 255, 0.4);
    border-top: 4px solid #2CE1FF;
    pointer-events: auto;
    color: #fff;

    // height: 80%;
    // padding-bottom: 20px;
    .query-title {
        height: 56px;
        line-height: 56px;
        font-size: 22px;
        color: #ffffff;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0 25px;
        box-sizing: border-box;
        background: rgba(46, 52, 65, 0.8);
        // border-top-left-radius: 15px;
        // border-top-right-radius: 15px;

        span {
            cursor: pointer;
            display: inline-block;
            width: 22px;
            height: 22px;
            background: rgba(0, 0, 0, 0.5);
            box-shadow: 0px 0px 5px 0px rgba(0, 0, 0, 0.5);
            border: 1px solid #ffffff;
            border-radius: 50%;
            display: flex;
            justify-content: center;
            align-items: center;

            i {
                font-size: 8px;
            }
        }
    }

    .query-warp {
        padding: 10px;
        padding-bottom: 20px;
        box-sizing: border-box;
        font-size: 16px;
        font-weight: 400;
        color: #ffffff;

        .alert-info {
            text-align: center;
            padding: 40px 0 80px 0;
        }

        h3 {
            font-size: 20px;
            font-weight: 400;
            color: #ffffff;
            margin: 0;
            padding-bottom: 10px;
        }

        ul {
            height: 460px;

            /deep/ .el-scrollbar__wrap {
                overflow-x: hidden;
            }

            li {
                display: flex;
                align-items: stretch;
                border-bottom: solid 1px #666;

                h5,
                p {
                    box-sizing: border-box;
                    display: inline-flex;
                    align-items: center;
                    padding: 8px 5px;
                    background: #3f4246;
                    color: #848691;
                    font-size: 14px;
                    font-weight: 400;
                    word-break: break-all;
                    width: 120px;
                    margin: 0;
                }

                p {
                    background: #494d52;
                    color: #c0c2cc;
                    flex-grow: 1;
                }
            }
        }
    }

    .query-warp-2 {
        ul {
            /deep/ li {
                align-items: center !important;

                p.id-con {
                    box-sizing: border-box;
                    padding: 2px 5px !important;
                    width: 130px;
                    font-size: 10px !important;
                    overflow: hidden;
                    display: -webkit-box;
                    -webkit-box-orient: vertical;
                    -webkit-line-clamp: 2;
                }

                i {
                    cursor: pointer;
                    color: #ccc;
                    padding: 0 2px;
                }

                .active2 {
                    color: #0396ff;
                }
            }
        }
    }

    /deep/.el-input {
        --el-input-bg-color: transparent;
        // background-color: transparent;
    }

    /deep/ .el-select__popper .el-popper[role=tooltip] {
        background-color: transparent !important;
    }
}

.operate-btns {
    padding: 10px 16px;
    display: flex;
    align-items: center;
    justify-content: space-around;

    .item {
        display: flex;
        flex-direction: column;
        align-items: center;

        p {
            margin-top: 10px;
        }
    }


    div {
        cursor: pointer;
        display: inline-flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-between;
        color: #fff;
        font-size: 12px;
        width: 76px;
        height: 76px;
        padding: 6px;
        // background: #4d515a;
        // box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
        //     0px 0px 0px 0px rgba(0, 0, 0, 0.5);
        // border-radius: 8px;

        img {
            height: 46px;
        }
    }

    .item {
        padding: 0;
    }

    .active {
        background: #535d84;
        border-style: hidden;
        position: relative;
    }

    .active:after {
        content: "";
        position: absolute;
        top: -2px;
        bottom: -2px;
        left: -2px;
        right: -2px;
        background: linear-gradient(to right, #0396ff, #21f7e8);
        border-radius: 8px;
        z-index: -1;
    }

    .select {
        width: 130px;
    }
}

.construc-info {
    color: #fff;
    padding-bottom: 20px;

    h3 {
        color: #fff;
        margin: 5px 16px;
        display: flex;
        align-items: center;

        i {
            color: rgb(218, 92, 72);
            margin-left: 10px;
            font-size: 16px;
            cursor: pointer;
        }
    }

    p {
        padding: 5px 16px;
        color: #ccc;

        span {
            font-size: 12px;
            color: #fff;
        }
    }
}

.el-popper.is-customized {
    /* Set padding to ensure the height is 32px */
    padding: 6px 12px;
    background: linear-gradient(90deg, rgb(159, 229, 151), rgb(204, 229, 129));
    color: #fff;
}

.el-popper.is-customized .el-popper__arrow::before {
    background: linear-gradient(45deg, #b2e68d, #bce689);
    right: 0;
    color: #fff;
}

/deep/ .el-input__inner {
    background: rgba(46, 52, 65, 0.8);
    box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
        0px 0px 0px 0px rgba(0, 0, 0, 0.5), 0px 2px 5px 0px rgba(38, 38, 44, 0.5);
    color: #fff;

}
</style>