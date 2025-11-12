<!-- /*
 * @FileDescription: 相机->无人机
 * @Author: mikey.zhaopeng 
 * @Date: 2022-11-23 17:11:10 
 * @Last Modified by:   mikey.zhaopeng 
 * @Last Modified time: 2022-11-23 17:11:10 
 */ -->
<template>
    <div></div>
</template>

<script>
export default {
    name: 'UAV',
    props: {
        cmptData: {
            type: Object,
            default: () => {
                return {
                    mode: '' // 空间交互类型 - 无人机
                }
            }
        }
    },
    mounted() {
        this.startUAV(this.cmptData.mode)
    },
    methods: {
        startUAV(mode) {
            window.sealAPI._misc.exitReportMode().then(() => {
                // let dark = window.sealAPI._weather.getDarkMode();
                // if (dark) {
                //     window.sealAPI._weather.setDarkMode_biz(true);
                // }
                window.sealAPI._settings.setInteractiveMode(mode);
                window.sealAPI._settings.setMainUIVisibility(false);
                __g.settings.setCampassVisible(false);
            });
        },
        async cancelUAV() {
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
        await this.cancelUAV();
        await this.handleClose();
    }
}
</script>

<style lang="scss" scoped></style>