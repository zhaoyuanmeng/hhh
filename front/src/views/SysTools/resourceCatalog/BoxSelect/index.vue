<!--
 * @FileDescription: 辅助分析->挖洞
 * @Author: yuanhaijun
 * @Date: 2022.11.28
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2022.11.28
 -->
<template>
    <div>
        <template v-if="!editHelper.flag">
            <div class="cloud-func draw-list">
                <div class="func-title">
                    列表
                    <el-icon class="" @click.stop="handleClose" style="cursor: pointer">
                        <CircleClose />
                    </el-icon>
                </div>
                <div class="func-warp" style="max-height: 300px;overflow:auto;">
                    <div class="warp-list">
                        <el-scrollbar>
                            <div class="list" v-for="(item, index) in resList" :key="index">
                                <div>
                                    <div class="list-left">
                                       <!-- <img :src="loadPicture(`./imgs/${item.type}.png`)"/> -->
                                        <span style="margin-left: 8px;">{{ item.name }}</span>
                                    </div>
                                </div>
                            </div>
                        </el-scrollbar>
                    </div>
                    <!-- <div class="button-wrap">
                        <div @click="drawParam('lines')">
                            <i class="el-icon-plus"></i> 直线
                        </div>
                        <div @click="drawParam('curve')">
                            <i class="el-icon-plus"></i> 曲线
                        </div>
                    </div> -->
                </div>
            </div>
        </template>
        <!-- 操作按钮 -->
        <ActionButtonTmpl v-if="editHelper.flag" :editHelper="editHelper" />
    </div>
</template>

<script>
import { loadPicture } from "@/utils";
import { ElMessage } from 'element-plus';
import ActionButtonTmpl from '../common/ActionButton.tmpl.vue'
import { queryIntersectsData } from '@/api/basic/index'
import { ref, reactive, defineEmits, onMounted, onBeforeUnmount, onUnmounted } from 'vue'
export default {
    components: {
        ActionButtonTmpl
    },
    data() {
        return {
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
            resList:[]
        }
    },
    mounted() {
        this.startDraw();
    },
    beforeDestroy() {
        this.cancelDraw();
        this.handleClose();
    },
    methods: {
        async getEditDate(coordinates) {
            this.editHelper.flag = false
            this.fModel.coords = coordinates
            console.log('this.fModel.coords::', this.fModel.coords);

            const res = await fdapi.coord.pcs2gcs(this.fModel.coords)
            queryIntersectsData(JSON.stringify(res.coordinates.map(item => [item[0], item[1]]))).then((res) => {
                this.resList = res.data
            })
        },
        async startDraw() {
            await window.sealAPI._editHelper.drawParam(3);
            this.editHelper.flag = true
        },
        closeEditHelper() {
            this.editHelper.flag = false
        },
        cancelDraw() {
            window.sealAPI._editHelper.cancelDraw();
            window.sealAPI._tools.stopPolygonClip_biz();
        },
        handleClose() {
            this.$emit('close')
        },
    },
}

// const emit = defineEmits(['close'])
// const editHelper = ref({
//     flag: false,
//     type: "shape"
// })
// const resList = ref([])
// const fModel = ref({
//     type: "",
//     coords: [],
//     buffer: 0,
//     tableName: "", // 基础数据
// })

// onMounted(async () => {
//     startDraw();
// })

// // 开始绘制
// const startDraw = async function () {
//     await window.sealAPI._editHelper.drawParam(3);
//     editHelper.value.flag = true
// }

// // 获取编辑助手返回数据
// const getEditDate = async (coordinates) => {
//     editHelper.value.flag = false
//     fModel.value.coords = coordinates
//     console.log('this.fModel.coords::', fModel.value.coords);

//     const res = await fdapi.coord.pcs2gcs(fModel.value.coords)
//     queryIntersectsData(JSON.stringify(res.coordinates.map(item => [item[0], item[1]]))).then((res) => {
//         resList.value = res.data
//     })
// }
// // 关闭编辑助手
// const closeEditHelper = function () {
//     editHelper.value.flag = false
// }

// // 绘制结束
// const cancelDraw = async function () {
//     window.sealAPI._editHelper.cancelDraw();
//     window.sealAPI._tools.stopPolygonClip_biz();
// }
// const handleClose = async function () {
//     emit('close')
// }
// onBeforeUnmount(async () => {
//     await cancelDraw();
//     await handleClose();
// })
// onUnmounted(async () => {
//     await cancelDraw();
//     await handleClose();
// })

</script>

<style lang="scss" scoped>
@import "@/styles/func3.scss";
@import "../css/draw.scss";

.cloud-func {
    position: absolute;
    top: 150px;
    right: 0;
    transform: translate(0, -50%) scale(0.8);
}
.list-left{
    display: flex;
    align-items: center;
}
</style>