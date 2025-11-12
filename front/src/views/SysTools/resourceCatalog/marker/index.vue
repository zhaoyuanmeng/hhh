<template>
    <div>
    </div>
</template>

<script lang="ts" setup>
import { computed, nextTick, onBeforeUnmount, onMounted, ref, watch,defineProps } from "vue";
import { ElMessage } from "element-plus"
import { getPoiByTypeData } from '@/api/dataApi'
import { addSbMarker } from '@/utils/equipmentMarker'
const props = defineProps({
    cmptData: {
        type: Object,
        default: () => {
            return {
                type: "", // 功能：横剖切分析、纵剖切分析、体剖切分析
            };
        },
    },
})
const emit = defineEmits(["close"])

onMounted(async () => {
    const res = await getPoiByTypeData({
        type: props.cmptData.type,
        code: null,
        tunnelId: null,
        cabinId: null
    })
    addSbMarker(props.cmptData.type, null, null, null,)

})
onBeforeUnmount(async () => {
    __g.marker.clear()
    __g.tag.clear()
})

const close = async () => {
    emit("close")
}

</script>

<style lang="scss" scoped></style>