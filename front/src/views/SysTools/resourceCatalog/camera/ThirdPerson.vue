<!-- /*
 * @FileDescription: 相机->第三人称
 * @Author: zhangxue 
 * @Date: 2022-11-23 17:11:10 
 * @Last Modified by:   zhangxue 
 * @Last Modified time: 2022-11-23 17:11:10 
 */ -->
<template>
    <div></div>
</template>

<script>
import { mapState } from 'pinia'
import { useSysToolsCimStore } from '@/stores/sysToolsCim'
export default {
    name: 'ThirdPerson',
    props: {
        cmptData: {
            type: Object,
            default: () => {
                return {
                    mode: '' // 空间交互类型 - 第三人称
                }
            }
        }
    },
    computed: {
        ...mapState(useSysToolsCimStore, ['eventSealAPI']),
    },
    watch: {
        eventSealAPI: {
            indeterminate: true,
            deep: true,
            handler(val) {
                if (val.eventtype === 'CameraChanged' && window.showThridPerson) {
                    console.log('第三人称漫游第三人称漫游');
                    // 展示 第三人称漫游
                    window.sealAPI._settings.setInteractiveMode(1)
                    window.showThridPerson = false
                }
            },
        },
    },
    mounted() {
        this.thirdPerson(this.cmptData.mode)
    },
    methods: {
        thirdPerson(mode) {
            window.sealAPI._misc.exitReportMode().then(async () => {
                let dark = window.sealAPI._weather.getDarkMode();
                if (dark) {
                    // window.sealAPI._weather.setDarkMode_biz(false);
                }
                let cameraView = await window.sealAPI._camera.get();
                /* let cameraView = {
                    x: 492141.46875,
                    y: 2492548.25,
                    z: 22.807634,
                    pitch: -16.984617,
                    yaw: -7.863207
                }; */
                window.showThridPerson = true;
                window.sealAPI._camera.set(
                    cameraView.x,
                    cameraView.y,
                    cameraView.z,
                    cameraView.pitch,
                    cameraView.yaw,
                    2
                );
                window.sealAPI._settings.setMainUIVisibility(false);
                __g.settings.setCampassVisible(false);
            });
        },
        async cancleThirdPerson() {
            await window.sealAPI._settings.setInteractiveMode(0);
            window.sealAPI._settings.setMainUIVisibility(false);
            __g.settings.setCampassVisible(false);
        },
        handleClose() {
            // if (this.$parent.hasOwnProperty('funcCMPT')) this.$parent.closeCMPT();
            this.$emit('close')
        }
    },
    async beforeUnmount() {
        await this.cancleThirdPerson();
        await this.handleClose();
    }
}
</script>

<style lang="scss" scoped></style>