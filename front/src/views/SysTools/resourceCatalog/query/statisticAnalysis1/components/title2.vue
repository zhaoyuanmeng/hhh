<template>
    <div class="title2">
        <div class="title">
            <div class="titleName">
                {{ prop.name }}
            </div>
        </div>
    </div>
</template>
<script lang="ts" setup>
const prop = defineProps({
    name: {
        type: String,
        default: '标题一'
    }
})
</script>

<style lang="scss" scoped>
.title {
    width: 379.4285583496094px;
    height: 22.095237731933594px;
    background: url(~@/assets/images/statisticAnalysis/标题一.png) no-repeat;

    .titleName {
        font-family: JiangChengXieHei;
        font-size: 15px;
        font-weight: 400;
        line-height: 15px;
        letter-spacing: 0px;
        text-align: left;
        margin-left: 20px;
    }
}
</style>