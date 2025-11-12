<template>
    <div class="statisticAnalysis" v-if="sysToolsCimStore.isShowStatisticAnalysis">
        <div class="statisticAnalysisBox">
            <div class="content">
                <div class="close" @click="handleClose"></div>
                <title2 :name="'区域概况'" class="quyu" />
                <div class="content1">
                    雄安商务服务中心项目建设地点在容东片区西部、市民服务中心北侧，规划占地约24公顷，总建筑面积约82万平方米，项目总投资约86亿元，建安费暂估71.8亿元。项目建设内容包括酒店、商务办公用房专家公寓、服务型公寓、商业幼儿园、服务配套、会议展览雄安国际科技成果展示交易中心、地下停车场等
                </div>
                <title2 :name="'区域概况'" class="quyu2" />
                <div class="content2">
                    <div class="item">
                        <div class="topValue">{{ polygon3dId.includes('悦容社区') ? 2 : 0 }}<span>版</span></div>
                        <img src="@/assets/images/statisticAnalysis/类型装饰.png" alt="">
                        <div class="typeName">{{ '版本数量' }}</div>
                    </div>
                    <div class="item">
                        <div class="topValue">BIM</div>
                        <img src="@/assets/images/statisticAnalysis/类型装饰.png" alt="">
                        <div class="typeName">{{ '模型类型' }}</div>
                    </div>
                    <div class="item">
                        <div class="topValue">{{ polygon3dId.includes('悦容社区') ? 12 : 0 }} <span>栋</span></div>
                        <img src="@/assets/images/statisticAnalysis/类型装饰.png" alt="">
                        <div class="typeName">{{ '楼栋数量' }}</div>
                    </div>
                    <div class="item">
                        <div class="topValue">{{ polygon3dId.includes('悦容社区') ? 2568 : 0 }} <span>家</span></div>
                        <img src="@/assets/images/statisticAnalysis/类型装饰.png" alt="">
                        <div class="typeName">{{ '企业数量' }}</div>
                    </div>
                </div>
                <title2 :name="'图层统计'" class="quyu3" />
                <div class="content3">
                    <div class="leftTypeName">图层统计</div>
                    <div class="rightValue">
                        <div class="value">{{ tileLayerList.length }}</div>
                        <div class="unit">个</div>
                    </div>
                </div>
                <title2 :name="'BIM构件统计'" class="quyu4" />
                <div class="content4">
                    <div class="leftTypeName">构件数量</div>
                    <div class="rightValue">
                        <div class="value">{{ objectSum }}</div>
                        <div class="unit">个</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script lang="ts" setup>
import { computed, onBeforeUnmount, onMounted, ref } from 'vue';
import title2 from './components/title2.vue'
import { getCommunityData } from '@/api/dataApi'
import { useSysToolsCimStore } from '@/stores/sysToolsCim'
const emit = defineEmits(['close'])
const sysToolsCimStore = useSysToolsCimStore()
const objectSum = computed(() => sysToolsCimStore.objectSum)
const tileLayerList = computed(() => sysToolsCimStore.tileLayerList)
const polygon3dId = computed(() => sysToolsCimStore.polygon3dId)
const Statistic = ref([
    {
        typeName: '模型',
        value: 123,
        unit: '个'
    },
    {
        typeName: '构件',
        value: 123,
        unit: '个'
    },
    {
        typeName: '模型',
        value: 123,
        unit: '个'
    },
    {
        typeName: '构件',
        value: 123,
        unit: '个'
    },
])
onMounted(() => {
    sysToolsCimStore.SET_ISSHOWSTATISTICANALYSIS(false)
    addPloygon3d()
})
const polygon3dArr: any = ref([])
const addPloygon3d = () => {
    const rainbowColors = ['#FF0000', '#FF7F00', '#FFFF00', '#00FF00', '#0000FF', '#4B0082', '#8A2BE2'];
    getCommunityData().then(async (res) => {
        polygon3dArr.value = []
        __g.polygon3d.clear()
        (res as any).features.forEach((element:any, index:number) => {
            let o1 = {
                id: 'getCommunityData' + index + element.attributes.BlockName,
                coordinates: element.geometry.rings,
                coordinateType: 0,//坐标系类型，取值范围：0为Projection类型，1为WGS84类型，2为火星坐标系(GCJ02)，3为百度坐标系(BD09)，默认值：0 
                userData: JSON.stringify(element.geometry.rings),
                color: [101 / 255, 203 / 255, 216 / 255, 0.3],        //颜色值
                height: 75,                //3D多边形的高度
                intensity: 1.0,             //亮度
                style: 1, //3DPolygon的样式 请参照API开发文档选取枚举
                tillingX: 0, //可选，仅当3DPolygon的样式支持贴图显示，贴图横向平铺  
                tillingY: 0,  //可选，仅当3DPolygon的样式支持贴图显示，贴图纵向平铺
                generateTop: true, //是否生成顶面
                generateSide: true,//是否生成侧面
                generateBottom: true//是否生成底面
            }
            polygon3dArr.value.push(o1)
        })
        await __g.polygon3d.add(polygon3dArr.value)
    })
}
const handleClose = () => {
    // emit('close')
    sysToolsCimStore.SET_ISSHOWSTATISTICANALYSIS(false)
}
onBeforeUnmount(() => {
    __g.polygon3d.delete(polygon3dArr.value.map((item:any) => item.id))
    sysToolsCimStore.SET_ISSHOWSTATISTICANALYSIS(false)
})
</script>
<style lang="scss" scoped>
* {
    padding: 0;
    margin: 0;
}


.statisticAnalysis {
    position: relative;
    top: 0;
    left: 1285px;
    z-index: 10;
    width: 486px;
    height: 573px;
    background: url(~@/assets/images/statisticAnalysis/大背景.png) no-repeat;
    background-size: 100% 100%;
    display: flex;
    align-items: center;
    flex-direction: column;
    justify-content: center;


    .close {
        position: absolute;
        width: 12.929951667785645px;
        height: 12.929951667785645px;
        top: 10.66680908203125px;
        left: 385.5238037109375px;
        background: url(~@/assets/images/statisticAnalysis/关闭.png) no-repeat;
        z-index: 11;
    }

    .statisticAnalysisBox {
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        flex-direction: column;
        justify-content: center;
    }

    .content {
        position: relative;
        width: 416px;
        height: 503px;

        .quyu {
            position: absolute;
            top: 67.80966186523438px;
            left: 18.28570556640625px;
        }

        .quyu2 {
            position: absolute;
            top: 204.19061279296875px;
            left: 18.28570556640625px;

        }

        .quyu3 {
            position: absolute;
            top: 317.34686279296875px;
            left: 18.28570556640625px;
        }

        .quyu4 {
            position: absolute;
            top: 406.0953674316406px;
            left: 18.28570556640625px;
        }

        .content1 {
            position: absolute;
            width: 377.9051208496094px;
            height: 82px;
            top: 105.14299774169922px;
            left: 19.80950927734375px;
            opacity: 0.699999988079071px;

            font-family: PingFang HK;
            font-size: 12px;
            font-weight: 400;
            line-height: 18px;
            letter-spacing: 0.02em;
            text-align: left;
            color: #B3C4E6;
        }

        .content2 {
            position: absolute;
            width: 100%;
            height: 60px;
            top: 242.2069854736328px;
            display: flex;
            justify-content: space-around;

            .item {
                display: flex;
                flex-direction: column;
                align-items: center;

                .topValue {
                    font-family: DIN Black;
                    font-size: 18px;
                    font-weight: 900;
                    line-height: 23px;
                    letter-spacing: 0.04em;
                    text-align: left;

                    font-family: DIN Black;
                    font-size: 20px;
                    font-weight: 900;
                    line-height: 24px;
                    letter-spacing: 0.04em;
                    text-align: left;
                    background: linear-gradient(180deg, rgba(143, 185, 247, 0) 60.05%, #8FB9F7 85%),
                        linear-gradient(0deg, #EEF3FF, #EEF3FF);
                    -webkit-background-clip: text;
                    /* 用于支持部分浏览器 */
                    -webkit-text-fill-color: transparent;
                    /* 用于支持部分浏览器 */

                }

                img {
                    width: 86px;
                    height: 14px;
                }

                .typeName {
                    font-family: PingFang HK;
                    font-size: 12px;
                    font-weight: 500;
                    line-height: 17px;
                    letter-spacing: 0.04em;
                    text-align: center;
                    color: #93C8FF;

                }
            }
        }

        .content3 {
            position: absolute;
            padding: 0 22px 0 22px;
            width: 377.9047546386719px;
            height: 37.33333206176758px;
            top: 353.5239562988281px;
            left: 19.80950927734375px;
            background: url(~@/assets/images/statisticAnalysis/统计背景.png);
            display: flex;
            align-items: center;
            justify-content: space-between;


            .leftTypeName {
                width: 51px;
                height: 17px;
                font-family: PingFang HK;
                font-size: 12px;
                font-weight: 500;
                line-height: 17px;
                letter-spacing: 0.04em;
                text-align: center;
            }

            .rightValue {
                // width: 36px;
                height: 23.999999475523246px;
                display: flex;
                align-items: center;
                font-family: DIN Black;
                font-size: 18px;
                font-weight: 900;
                line-height: 23px;
                letter-spacing: 0.04em;
                background: linear-gradient(180deg, rgba(143, 185, 247, 0) 60.05%, #8FB9F7 85%),
                    linear-gradient(0deg, #EEF3FF, #EEF3FF);
                -webkit-background-clip: text;
                /* 用于支持部分浏览器 */
                -webkit-text-fill-color: transparent;
                /* 用于支持部分浏览器 */


                .value {}

                .unit {
                    margin-left: 4px;
                }
            }
        }

        .content4 {
            position: absolute;
            padding: 0 22px 0 22px;
            width: 377.9047546386719px;
            height: 37.33333206176758px;
            top: 441.9049072265625px;
            left: 19.80950927734375px;
            background: url(~@/assets/images/statisticAnalysis/统计背景.png);
            display: flex;
            align-items: center;
            justify-content: space-between;

            .leftTypeName {
                width: 51px;
                height: 17px;
                font-family: PingFang HK;
                font-size: 12px;
                font-weight: 500;
                line-height: 17px;
                letter-spacing: 0.04em;
                text-align: center;
            }

            .rightValue {
                // width: 36px;
                height: 23.999999475523246px;
                display: flex;
                align-items: center;
                font-family: DIN Black;
                font-size: 18px;
                font-weight: 900;
                line-height: 23px;
                letter-spacing: 0.04em;
                background: linear-gradient(180deg, rgba(143, 185, 247, 0) 60.05%, #8FB9F7 85%),
                    linear-gradient(0deg, #EEF3FF, #EEF3FF);
                -webkit-background-clip: text;
                /* 用于支持部分浏览器 */
                -webkit-text-fill-color: transparent;
                /* 用于支持部分浏览器 */


                .value {}

                .unit {
                    margin-left: 4px;
                }
            }
        }
    }
}
</style>