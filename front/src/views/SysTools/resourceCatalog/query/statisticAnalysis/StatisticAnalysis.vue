<template>
    <div class="cloud-func">
        <div class="func-title">
            统计分析
        </div>
        <div class="func-warp">
            <div class="Analysis">
                <div class="item" v-for="item in Statistic" :key="item.typeName">
                    <div class="type">{{ item.typeName }}</div>
                    <img :src="require(`@/assets/images/XAXCCimTools/查询定位/${item.typeName}.png`)" alt="">
                    <div class="value">{{ item.value }} <span>{{ item.unit }}</span></div>
                </div>
            </div>
        </div>

    </div>
</template>
<script lang="ts" setup>
import { ref } from 'vue';

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
</script>
<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";


.func-warp {
    .Analysis {
        width: 100%;

        display: flex;
        flex-wrap: wrap;

        .item {
            display: flex;
            flex-direction: column;
            justify-content: space-around;
            align-items: center;
            width: 50%;
            height: 150px;


            img {
                width: 40px;
            }

            .value {
                font-size: 18px;
                font-weight: 700;
                color: #2ce1ff;
                font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;

                span {
                    font-size: 14px;
                    color: rgba(240, 248, 255, 0.803);
                }
            }
        }
    }
}
</style>