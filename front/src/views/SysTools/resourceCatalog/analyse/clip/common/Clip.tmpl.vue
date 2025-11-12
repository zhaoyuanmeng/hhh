<!--
 * @FileDescription: 辅助分析->剖切（设置）
 * @Author: yuanhaijun
 * @Date: 2022.11.28
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2022.11.28
 -->
<template>
    <div class="cloud-func clip">
        <div class="func-title">
            剖切
            <el-icon class="" @click.stop="closeClip">
                <CircleClose />
            </el-icon>
            <!-- <span @click.stop="closeClip">
                        <i class="el-icon-close"></i>
                      </span> -->
        </div>
        <div class="func-warp">
            <el-scrollbar>
                <div v-for="(item, index) in setting" :key="index">
                    <div class="warp-border" v-if="item.type === 'slider'">
                        <p>{{ item.name }}</p>
                        <ul>
                            <el-slider :min="item.min" :max="item.max" :step="item.step" v-model="item.value"
                                @change="changeValue" :show-input="false" :show-input-controls="false"></el-slider>
                            <el-input-number v-model="item.value" @change="changeValue" controls-position="right"
                                :min="item.min" :max="item.max" :step="item.step"></el-input-number>
                        </ul>
                    </div>
                    <div v-else-if="item.type === 'radio'">
                        <div class="warp-inspection">
                            <span>{{ item.name }}</span>
                            <div>
                                <el-radio-group v-model="item.value" @change="changeValue">
                                    <el-radio v-for="(op, i) in item.options" :key="i" :value="op.value"
                                        :label="op.value">{{ op.label }}</el-radio>
                                </el-radio-group>
                            </div>
                        </div>
                    </div>
                </div>
            </el-scrollbar>

            <div class="warp-footer">
                <div @click="handleReset">重置</div>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { onBeforeUnmount, onMounted, ref } from 'vue';
import { CircleClose } from '@element-plus/icons-vue'
const emit = defineEmits(['close'])
const value = ref(5)
const opacity = ref(0.5)
const setting = ref(window.sealAPI._clip.getSetting().setting)
onMounted(() => {
    window.sealAPI._clip.resetPARAM(
        window.sealAPI._clip.getSetting().curType,
        JSON.parse(JSON.stringify(setting.value))
    )
    console.log('settingsetting', setting);

})
const handleReset = function () {
    setting.value = window.sealAPI._clip.resetSetting().setting;
    changeValue()
}
const changeValue = function () {
    let curOa = window.sealAPI._clip.getSetting().curOa;

    if (!curOa.rocationy) {
        for (let i = 0; i < setting.value.length; i++) {
            curOa[setting.value[i].key] = setting.value[i].value;
        }
    } else {
        for (let i = 0; i < setting.value.length; i++) {
            curOa[setting.value[i].key] = curOa.rocationy - setting.value[i].value;
        }
    }
    window.sealAPI._clip.update();
}
const changeValueName = function (value: any) {
    let curOa = window.sealAPI._clip.getSetting().curOa;
    curOa["name"] = value;
}

const closeClip = function () {
    emit("close")
}
//  const handleClose=async function() {
//     await $parent.cancelClip();
//     $parent.handleClose();
//   }

onBeforeUnmount(async () => {
    // closeClip()
})

</script>

<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";

.clip .func-warp {
    /deep/.el-scrollbar__wrap {
        max-height: 720px;
        margin-bottom: 0 !important;
        overflow-x: hidden;

        .el-scrollbar__view {
            >div {
                margin-bottom: 20px;

                p {
                    font-size: 16px;
                    color: #ffffff;
                    margin-bottom: 8px;
                }

                ul {
                    display: flex;
                    justify-content: space-between;
                    align-items: center;

                    .el-slider {
                        width: calc(100% - 125px);
                        margin-left: 10px;
                    }

                    .el-input-number {
                        width: 95px;
                    }
                }
            }

            .warp-border {
                border-bottom: 1px solid rgba(255, 255, 255, 0.1);
                padding-bottom: 10px;
            }

            .warp-inspection {
                padding: 10px 0;
                box-sizing: border-box;
                font-size: 16px;
                color: #ffffff;
                display: flex;
                align-items: center;
                justify-content: space-between;

                div {
                    .el-radio {
                        color: #ffffff;
                    }

                    .el-radio__input.is-checked+.el-radio__label {
                        color: #ffffff;
                    }
                }
            }
        }
    }

    .warp-footer {
        display: flex;
        justify-content: center;

        div {
            width: 100%;
            line-height: 38px;
            cursor: pointer;
            border-radius: 5px;
            border: 1px solid #029eff;
            text-align: center;
        }
    }
}
</style>