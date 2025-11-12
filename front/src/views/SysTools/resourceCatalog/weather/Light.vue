<!--
 * @FileDescription: 环境->光照
 * @Author: yuanhaijun
 * @Date: 2023.04.21
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2023.04.21
 -->
<template>
    <div class="cloud-func">
        <div class="func-title">
            光照
            <!-- <span @click.stop="handleClose">
                                <i class="el-icon-close"></i>
                            </span> -->
            <el-icon class="close-btn" @click.stop="handleClose">
                <CircleClose />
            </el-icon>
        </div>
        <el-form size="mini" :model="fModel" ref="fRef" class="func-warp">


            <div class="cf_title">通用</div>

            <el-form-item label="环境光" prop="ambientLightIntensity " class="cf_slider grey">
                <el-slider :min="0" :max="10" :step="0.01" v-model="fModel.ambientLightIntensity"
                    @change="changeForm('ambientLightIntensity')" :show-input="false"
                    :show-input-controls="false"></el-slider>
                <el-input-number size="mini" v-model="fModel.ambientLightIntensity" :min="0" :max="10" :step="0.01"
                    controls-position="right" @change="changeForm('ambientLightIntensity')"></el-input-number>
            </el-form-item>

            <el-form-item label="色温" prop="temperature " class="cf_slider grey">
                <el-slider :min="1700" :max="12000" :step="1" v-model="fModel.temperature"
                    @change="changeForm('temperature')" :show-input="false" :show-input-controls="false"></el-slider>
                <el-input-number size="mini" v-model="fModel.temperature" :min="1700" :max="12000" :step="1"
                    controls-position="right" @change="changeForm('temperature')"></el-input-number>
            </el-form-item>

            <el-form-item label="阴影质量" prop="shadowQuality " class="cf_slider grey">
                <el-slider :min="1" :max="5" :step="0.01" v-model="fModel.shadowQuality"
                    @change="changeForm('shadowQuality')" :show-input="false" :show-input-controls="false"></el-slider>
                <el-input-number size="mini" v-model="fModel.shadowQuality" :min="1" :max="5" :step="0.01"
                    controls-position="right" @change="changeForm('shadowQuality')"></el-input-number>
            </el-form-item>

            <el-form-item label="阴影距离" prop="shadowDistance " class="cf_slider grey">
                <el-slider :min="0" :step="1" v-model="fModel.shadowDistance" @change="changeForm('shadowDistance')"
                    :show-input="false" :show-input-controls="false"></el-slider>
                <el-input-number size="mini" v-model="fModel.shadowDistance" :min="0" :step="1" controls-position="right"
                    @change="changeForm('shadowDistance')"></el-input-number>
            </el-form-item>


            <div class="cf_title">太阳</div>

            <!-- not supported -->
            <el-form-item v-if="false" label="太阳尺寸" prop="sunSize " class="cf_slider grey">
                <el-slider :min="0" :max="100" :step="0.01" v-model="fModel.sunSize" @change="changeForm('sunSize')"
                    :show-input="false" :show-input-controls="false"></el-slider>
                <el-input-number size="mini" v-model="fModel.sunSize" :min="0" :max="100" :step="0.01"
                    controls-position="right" @change="changeForm('sunSize')"></el-input-number>
            </el-form-item>

            <el-form-item label="照射强度" prop="sunIntensity " class="cf_slider grey">
                <el-slider :min="0" :max="10" :step="0.01" v-model="fModel.sunIntensity"
                    @change="changeForm('sunIntensity')" :show-input="false" :show-input-controls="false"></el-slider>
                <el-input-number size="mini" v-model="fModel.sunIntensity" :min="0" :max="10" :step="0.01"
                    controls-position="right" @change="changeForm('sunIntensity')"></el-input-number>
            </el-form-item>


            <div class="cf_title">月亮</div>

            <!-- not supported -->
            <el-form-item v-if="false" label="月亮尺寸" prop="moonSize " class="cf_slider grey">
                <el-slider :min="0" :max="100" :step="0.01" v-model="fModel.moonSize" @change="changeForm('moonSize')"
                    :show-input="false" :show-input-controls="false"></el-slider>
                <el-input-number size="mini" v-model="fModel.moonSize" :min="0" :max="100" :step="0.01"
                    controls-position="right" @change="changeForm('moonSize')"></el-input-number>
            </el-form-item>

            <el-form-item label="照射强度" prop="moonIntensity " class="cf_slider grey">
                <el-slider :min="0" :max="100" :step="0.01" v-model="fModel.moonIntensity"
                    @change="changeForm('moonIntensity')" :show-input="false" :show-input-controls="false"></el-slider>
                <el-input-number size="mini" v-model="fModel.moonIntensity" :min="0" :max="100" :step="0.01"
                    controls-position="right" @change="changeForm('moonIntensity')"></el-input-number>
            </el-form-item>

            <el-form-item class="btn">
                <el-button @click="resetForm('AP')">重置本次调节</el-button>
                <el-button @click="resetForm('RESET')">重置原场景初始</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
import { onBeforeUnmount } from 'vue'
import { CircleClose } from '@element-plus/icons-vue'
import { mapState } from 'pinia'
import { useAirCityStore } from '@/stores/aircity'
export default {
    name: "Light",
    components: {
        CircleClose
    },
    data() {
        return {
            defaultParam: null, // 默认参数
            fModel: {},
            resetDefultParams: null
        };
    },
    computed: {
        ...mapState(useAirCityStore, ['weatherParams'])
    },
    mounted() {
        this.initParams();
        console.log(this.weatherParams, 'weatherParamsweatherParamsweatherParams');
        this.resetDefultParams = this.weatherParams
    },
    methods: {
        // 初始化天气相关的参数
        async initParams() {
            let res = null;
            if (this.defaultParam) {
                res = this.defaultParam;
            } else {
                res = await window.sealAPI._weather.getParams();
                this.defaultParam = res;
            }
            // 默认值
            // this.$set(this.fModel, 'ambientLightIntensity', res.ambientLightIntensity); // 环境光强度
            // this.$set(this.fModel, 'temperature', res.temperature); // 色温值
            // this.$set(this.fModel, 'shadowQuality', res.shadowQuality); // 阴影质量
            // this.$set(this.fModel, 'shadowDistance', res.shadowDistance); // 阴影可视距离
            this.fModel.ambientLightIntensity = res.ambientLightIntensity
            this.fModel.temperature = res.temperature
            this.fModel.shadowQuality = res.shadowQuality
            this.fModel.shadowDistance = res.shadowDistance
            // this.$set(this.fModel, 'sunSize', res.sunSize); // 太阳尺寸
            // this.$set(this.fModel, 'sunIntensity', res.sunIntensity); // 太阳光照射强度
            this.fModel.sunIntensity = res.sunIntensity
            // this.$set(this.fModel, 'moonSize', res.moonSize); // 月亮尺寸
            // this.$set(this.fModel, 'moonIntensity', res.moonIntensity); // 月亮光照射强度
            this.fModel.moonIntensity = res.moonIntensity

        },
        // 表单值变更
        async changeForm(opr) {
            await this.$refs.fRef.validate(async (valid) => {
                if (!valid) return false;

                switch (opr) {
                    case 'ambientLightIntensity':
                        await window.sealAPI._weather.setAmbientLightIntensity(this.fModel.ambientLightIntensity);
                        break;
                    case 'temperature':
                        await window.sealAPI._weather.setTemperature(this.fModel.temperature);
                        break;
                    case 'shadowQuality':
                        await window.sealAPI._weather.setShadowQuality(this.fModel.shadowQuality);
                        break;
                    case 'shadowDistance':
                        await window.sealAPI._weather.setShadowDistance(this.fModel.shadowDistance);
                        break;
                    case 'sunSize':
                        await window.sealAPI._weather.setSunSize(this.fModel.sunSize);
                        break;
                    case 'sunIntensity':
                        await window.sealAPI._weather.setSunIntensity(this.fModel.sunIntensity);
                        break;
                    case 'moonSize':
                        await window.sealAPI._weather.setMoonSize(this.fModel.moonSize);
                        break;
                    case 'moonIntensity':
                        await window.sealAPI._weather.setMoonIntensity(this.fModel.moonIntensity);
                        break;
                }
            });
        },
        async resetForm(opr) {
            // if (opr === "RTZ") {
            let resetType
            resetType = opr === 'AP' ? this.defaultParam : this.resetDefultParams
            for (let key in this.fModel) {
                let val = this.fModel[key];
                let originalValue = resetType[key]
                if (val !== originalValue) {
                    // this.$set(this.fModel, key, originalValue);
                    this.fModel[key] = originalValue
                    await this.changeForm(key);
                }
            }
            // }
        },
        handleClose() {
            // if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
            this.$emit('close')
        },
    },
    async onBeforeUnmount() {
        // await this.resetForm();
        await this.handleClose();
    },
};
</script>

<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";

.cf_title {
    color: #EEEEEE;
    margin-bottom: 24px;
}

/deep/.el-form-item {

    &.grey .el-form-item__label {
        font-size: 14px;
        line-height: 38px;
        opacity: 0.8;
    }

    &.cf_slider {
        .el-form-item__content {
            display: flex;
            align-items: center;

            .el-slider {
                width: 60%;
                margin-right: 16px;
            }

            .el-input-number {
                width: calc(40% - 24px);
            }
        }
    }

    .color-setting .el-form-item__content {
        display: flex;
        align-items: center;

        ul {
            display: flex;
            align-items: center;
            margin-right: 16px;
        }

        .el-input {
            width: 100px;
        }
    }
}
</style>