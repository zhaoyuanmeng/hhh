<template>
    <div class="cloud-func">
        <div class="func-title">
            退线分析
            <el-icon class="" @click.stop="handleClose" style="cursor:pointer">
                <CircleClose />
            </el-icon>
        </div>
        <div class="func-warp">
            <div class="warp-list">
                <div class="coordianteDataList">
                    <div class="item">
                        <div class="dataName">起位坐标</div>
                        <div class="data">
                            <div class="dataItem">
                                <div>x</div> <span>{{ regressionAnalysis.StartCoordinate[0] }}</span>
                            </div>
                            <div class="dataItem">
                                <div> y</div> <span>{{ regressionAnalysis.StartCoordinate[1] }}</span>
                            </div>
                        </div>
                    </div>
                    <div class="item">
                        <div class="dataName">终位坐标</div>
                        <div class="data">
                            <div class="dataItem">
                                <div>x</div> <span>{{ regressionAnalysis.VerticalCoordinate[0] }}</span>
                            </div>
                            <div class="dataItem">
                                <div> y</div> <span>{{ regressionAnalysis.VerticalCoordinate[1] }}</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="dataList">
                    <div class="item">
                        <div class="dataName">起位高度</div>
                        <div class="data">{{ regressionAnalysis.StartCoordinate[2] }}</div>
                    </div>
                    <div class="item">
                        <div class="dataName">终位高度</div>
                        <div class="data">{{ regressionAnalysis.VerticalCoordinate[2] }}</div>
                    </div>
                    <div class="item">
                        <div class="dataName">垂直高度</div>
                        <div class="data">{{ regressionAnalysis.VerticalHeight }}</div>
                    </div>
                    <div class="item">
                        <div class="dataName">两点距离</div>
                        <div class="data">{{ Math.sqrt(regressionAnalysis.VerticalHeight * regressionAnalysis.VerticalHeight
                            + regressionAnalysis.HorizontalLength * regressionAnalysis.HorizontalLength) }}</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { computed, defineEmits, onBeforeUnmount, onMounted } from 'vue'
import { useSysToolsCimStore } from '@/stores/sysToolsCim'
import { CircleClose } from '@element-plus/icons-vue'
const emits = defineEmits(['close'])
const SysToolsCimStore = useSysToolsCimStore()
const regressionAnalysis: any = computed(() => SysToolsCimStore.regressionAnalysis)
onMounted(() => {
    SysToolsCimStore.SET_REGRESSIONANALYSIS(
        {
            "eventtype": "null",
            "Type": "null",
            "HorizontalLength": "0",
            "VerticalHeight": "0",
            "StartCoordinate": [0, 0, 0],
            "VerticalCoordinate": [0, 0, 0],
            "EndCoordinate": [0, 0, 0]
        }
    )

    //测量模式配置选项参数
    let options = {
        'pointSize': 8.0,
        'textSize': 10.0,
        'textColor': Color.Yellow,
        'pointColor': [0, 0, 1, 0.3],
        'lineColor': Color.Blue,
        'areaColor': [0, 1, 0, 0.3],
        'showCoordinateText': false
    };
    //设置测量模式，详情参考文档内MeasurementMode枚举 支持以下6类： 1坐标测量 2直线测量 3水平测量 4垂直测量 5多边形测量 6地表面积
    __g.tools.setMeasurement(4, options);
    //开始测量 注意：5.3支持右键结束交互
    __g.tools.startMeasurement();
})
const handleClose = () => {
    emits('close')
    __g.tools.stopMeasurement();
}

onBeforeUnmount(() => {
    __g.tools.stopMeasurement();
})
</script>

<style lang="scss">
@import "~@/assets/css/func3.scss";

.func-warp {

    .warp-list {
        width: 100%;

        .coordianteDataList {
            display: flex;
            align-items: center;
            flex-direction: column;
            margin-bottom: 15px;


            .item {
                width: 100%;
                height: 60px;
                display: flex;
                align-items: center;
                flex-direction: column;
                justify-content: space-around;

                .dataName {
                    color: #2CE1FF;
                }

                .data {
                    width: 100%;
                    display: flex;
                    justify-content: space-between;
                    color: #2CE1FF;

                    .dataItem {
                        width: 45%;
                        overflow: hidden;
                        display: flex;
                        align-items: center;
                        flex-direction: column;

                    }

                    span {
                        width: 100%;
                        text-align: left;
                        color: bisque;
                    }
                }
            }
        }

        .dataList {
            width: 100%;
            display: flex;
            flex-direction: column;

            .item {
                width: 100%;
                height: 30px;
                display: flex;
                justify-content: space-around;

                .dataName {
                    width: 90px;
                    color: #2CE1FF;
                }

                .data {
                    flex: 1;
                    color: bisque;
                }
            }
        }
    }
}
</style>