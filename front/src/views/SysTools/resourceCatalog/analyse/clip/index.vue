<!--
 * @FileDescription: 辅助分析->剖切分析
 * @Author: yuanhaijun
 * @Date: 2022.11.28
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2022.11.28
 -->
<template>
    <div>
        <!-- 设置 -->
        <clip-tmpl v-if="SysToolsCimStore.isShowClip && props.cmptData.funcMOD != '横剖切' && props.cmptData.funcMOD != '纵剖切'"
            @close="close" />
    </div>
</template>

<script lang="ts" setup>
import { computed, nextTick, onBeforeUnmount, onMounted, ref, watch,defineProps } from "vue";
import { useSysToolsCimStore } from '@/stores/sysToolsCim'
import { useAirCityStore } from '@/stores/aircity'
import ClipTmpl from "./common/Clip.tmpl.vue"
import { ElMessage } from "element-plus"
const SysToolsCimStore = useSysToolsCimStore()
const AirCityStore = useAirCityStore()
const props = defineProps({
    cmptData: {
        type: Object,
        default: () => {
            return {
                funcMOD: "", // 功能：横剖切分析、纵剖切分析、体剖切分析
            };
        },
    },
})
const emit = defineEmits(["close"])

const isShowClip = ref(false)
const eventSealAPI = computed(() => SysToolsCimStore.eventSealAPI)
console.log(eventSealAPI.value);

const paramVAL = computed(() => {
    switch (props.cmptData.funcMOD) {
        case "横剖切":
            return 0;
        case "纵剖切":
            return 1;
        case "体剖切":
            return 2;
        default:
            return null
    }
})

const isFirstLeftMouseButtonClick = ref(true)

watch(
    () => eventSealAPI.value,
    (val) => {
        if (isFirstLeftMouseButtonClick.value && val && (val as any).eventtype === "LeftMouseButtonClick") {
            window.sealAPI._clip.clipCallback(val);
        }
        isFirstLeftMouseButtonClick.value = false
    },
    {
        deep: true,
        immediate: false,
    }
)
onMounted(async () => {
    ElMessage({
        message: '请单击选择进行剖切的位置',
        type: 'success',
    })
    nextTick(async () => {

        console.log(paramVAL, 'paramVALparamVAL');
        const ids = []
        for (const key in AirCityStore.layerTreeIdKeyObject) {
            if (Object.prototype.hasOwnProperty.call(AirCityStore.layerTreeIdKeyObject, key)) {
                const element = AirCityStore.layerTreeIdKeyObject[key];
                ids.push(key)
            }
        }
        await __g.tileLayer.enableClip(ids);
        await startClip()
    })
})

// 开始剖切
const startClip = async function () {
    await window.sealAPI._clip.start(paramVAL.value);
}
// 剖切结束
const cancelClip = async function () {
    await window.sealAPI._clip.clear();
}
onBeforeUnmount(async () => {
    // close()
    cancelClip()
})

const close = async () => {
    SysToolsCimStore.SET_ISSHOWCLIP(false)
    emit("close")
}

</script>

<style lang="scss" scoped></style>