<template>
    <div class="msg-box" v-show="isShow" :class="[classNameList]"  :style="msgPositionStyle">
        <div class="msg-nav" v-show="isShowPopTitle">
            <div class="msg-label-div">
                <div class="tab_line"></div>
                <span class="msg-label">{{ title }}</span>
            </div>

            <img class="msg-close" src="@/assets/images/关闭.png" @click="closeHandle" />
            <!-- <i class="el-icon-close" @click="closeHandle" style="cursor: pointer;"></i> -->
        </div>
        <div class="msg-line"></div>
        <div class="msg-content">
            <slot></slot>
        </div>
    </div>
</template>

<script>
export default {
    name: "InfoPopup",
    data() {
        return {

        }
    },
    props: {
        isShow: {
            type: Boolean,
            default: false
        },
        isShowPopTitle: {
            type: Boolean,
            default: true
        },
        classNameList: {
            type: Array,
            default: () => {
                return ['pop_bg_facilities']
            }
        },
        title: {
            type: String,
            default: '信息框'
        },
        // 容器宽高设置
        msgWHStyle: {
            type: Object,
            default: () => {
                return {
                    width: '120%',
                    height: '140%',
                    left: '50%'
                }
            }
        },
        // 容器设置
        msgPositionStyle: {
            type: Object,
            default: () => {
                return {
                    width: '120%',
                    height: '140%',
                }
            }
        },
        imgStyle: {
            type: Object,
            default: () => {
                return {
                    width: '36px',
                    height: '36px',
                }
            }
        },
        popStyle: {
            type: Object,
            default: () => {
                return {

                }
            }
        }
    },
    methods: {
        closeHandle() {
            this.$emit('popupClose');
        }
    },
    mounted() {
    },
}
</script>

<style scoped lang="scss">
.msg-box {
    /*width: 100%;*/
    padding: 15px;
    position: fixed;
    @include Width(400);
    @include hHeight(600);
    /*left: 500px;*/
    font-size: 14px;
    z-index: 9999;
    border-radius: 15px;
    background-color: #344758;
    /* transform: scale(1.5); */
}

.msg-nav {
    width: 98%;
    margin: 0 auto;
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.msg-img {
    width: 16px;
    height: 18px;
}

.msg-label {
    font-size: 32px;
    font-family: "ShiShangZhongHeiJianTi";
    font-weight: 400;
    /* color: #33EAFF; */
    color: #FFFFFF;
    line-height: 20px;
    text-shadow: 0px 0px 3px #33B8FF;
    text-overflow: ellipsis;

}

.msg-content {
    height: calc(100% - 60px);
    width: 100%;
}

.tab_line {
    width: 2px;
    height: 24px;
    background: #31FFED;
    box-shadow: 0px 0px 4px 0px #31FFED;
    border-radius: 4px 4px 4px 4px;
    opacity: 1;
    margin-right: 10px;
}

.msg-label-div {
    display: flex;
    align-items: center;
}

.msg-close {
    width: 40px;
    height: 40px;
    cursor: pointer;
}

.msg-line {
    width: 98%;
    margin: 20px auto 0 auto;
    height: 0px;
    opacity: 0.3;
    border: 1px solid;
    border-image: linear-gradient(180deg, rgba(255, 255, 255, 1), rgba(255, 255, 255, 0)) 1 1;
}
</style>
