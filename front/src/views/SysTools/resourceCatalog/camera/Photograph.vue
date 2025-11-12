<!--
 * @FileDescription: 相机->视点
 * @Author: yuanhaijun
 * @Date: 2023.04.03
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2023.04.03
 -->
<template>
    <div class="cloud-func">
        <div class="func-title">
            快速截屏
            <el-icon class="" @click.stop="handleClose" style="cursor:pointer">
                <CircleClose />
            </el-icon>
        </div>
        <div class="func-warp draw-list">
            <div class="warp-list">
                <div class="paramas">
                    <el-radio-group v-model="imgType" class="ml-4">
                        <el-radio label="png" size="large">png</el-radio>
                        <el-radio label="jpeg" size="large">jpeg</el-radio>
                        <el-radio label="webp" size="large">webp</el-radio>
                    </el-radio-group>
                    <el-input-number v-model="imgQuality" :precision="0" :min="0" :max="100" :step="1"
                        @change="changeImgQuality" step-strictly v-show="imgType == 'jpeg'" />
                </div>
                <div class="warp-list">
                    <div class="paramasTitle">
                        <p>Width</p>
                        <p>Height</p>
                    </div>
                    <div class="input-box">
                        <el-input-number v-model="imgWidth" controls-position="right" :min="1" :step="1"
                            @change="handleChange"></el-input-number>
                        <el-input-number v-model="imgHeight" controls-position="right" :min="1" :step="1"
                            @change="handleChange"></el-input-number>
                    </div>
                </div>
                <div class="buttonNav">
                    <el-button type="primary" @click="handlePhotograph"><el-icon>
                            <Download />
                        </el-icon>截取场景</el-button>
                </div>
                <el-scrollbar style="height: 100%">
                    <div class="imageList">
                        <div class="item" v-for="(image, index) in photoList" :key="image">
                            <img :src="image" alt="">
                            <el-dropdown @command="handlephoto($event, index)">
                                <span class="el-dropdown-link">
                                    <el-icon style="color: #000;">
                                        <Setting />
                                    </el-icon>
                                </span>
                                <template #dropdown>
                                    <el-dropdown-menu>
                                        <el-dropdown-item command="保存截屏">
                                            保存截屏
                                        </el-dropdown-item>
                                        <el-dropdown-item command="删除截屏">
                                            删除截屏
                                        </el-dropdown-item>
                                    </el-dropdown-menu>
                                </template>
                            </el-dropdown>
                        </div>
                    </div>
                </el-scrollbar>
            </div>
        </div>

    </div>
</template>

<script>
import { get, post, put, deletes } from "@/utils/fetch";
import { CircleClose, Setting, Download } from '@element-plus/icons-vue';
export default {
    data() {
        return {
            activeId: '',
            value1: false,
            CompassStatus: false,
            photoList: [],
            artworkMasterList: [],
            imgType: 'png',
            imgQuality: 100,
            imgWidth: 1920,
            imgHeight: 1080
        };
    },
    created() {
    },
    components: {
        CircleClose,
        Setting
    },
    methods: {
        handleClose() {
            this.$emit('close')
        },
        interactiveSetup(item) {
            // alear('ccc')
            this.activeId = item.name
            __g.settingsPanel.setControlMode(...this.interactiveSetupList[item.id].data)
        },
        getCurCameraImage(isArtworkMaster) {
            var video = document
                .getElementById("player").querySelector('video');
            var canvas = document.createElement("canvas");
            // var scale = video.videoWidth / video.videoHeight;
            var scale = this.imgWidth / this.imgHeight
            if (isArtworkMaster) {
                // canvas.width = video.videoWidth;
                // canvas.height = video.videoHeight;
                canvas.width = this.imgWidth;
                canvas.height = this.imgHeight;
            } else {
                canvas.width = 300;
                canvas.height = parseInt(300 / scale);
            }
            canvas.getContext("2d").drawImage(video, 0, 0, canvas.width, canvas.height);
            // var base64 = canvas.toDataURL("image/png", 0.1);
            console.log(this.imgQuality / 100);
            var base64 = canvas.toDataURL("image/" + this.imgType, this.imgQuality / 100);
            return base64;
        },
        handlePhotograph() {
            this.photoList.push(this.getCurCameraImage(false))
            this.artworkMasterList.push(this.getCurCameraImage(true))
            this.artworkMasterList.reverse()
            this.photoList.reverse()
        },
        handlephoto(command, index) {
            if (command == '保存截屏') {
                const a = document.createElement('a')
                a.download = '场景' + index + 1
                // 设置图片地址
                a.href = this.artworkMasterList[index];
                a.click();
            } else {
                this.photoList.splice(index, 1)
                this.artworkMasterList.splice(index, 1)
            }
        }
    },
};
</script>

<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";



.ischeck {
    color: #029eff;
}

.draw-list {
    .warp-list {
        max-height: 555px;

        .paramas {
            width: 100%;
            text-align: center;
            margin-bottom: 10px;
            color: #ffffff;

            /deep/ .el-input-number__increase {
                background-color: transparent;
            }

            /deep/ .el-input-number__decrease {
                background-color: transparent;
            }

            /deep/ .el-icon {
                color: #ffffff;
            }
        }

        .el-scrollbar__wrap {
            margin-bottom: 0 !important;
            max-height: 340px;
            overflow-x: hidden;
            overflow: scroll;

            .list {
                line-height: 50px;
                display: flex;
                align-items: center;
                justify-content: space-between;
                border-bottom: 1px solid rgba(255, 255, 255, 0.1);
                padding: 0;

                .list-left {
                    font-size: 16px;
                    color: #ffffff;
                    display: flex;
                    align-items: center;

                    .icon {
                        width: 24px;
                        height: 24px;
                        font-size: 24px;
                    }

                    span {
                        margin-left: 12px;
                    }
                }

                .el-dropdown span {
                    display: flex;
                    align-items: center;
                }

                .icon {
                    cursor: pointer;
                    width: 24px;
                    height: 24px;
                }

                .icon-del {
                    display: flex;
                    align-items: center;
                    position: relative;

                    ul {
                        display: none;
                        width: 68px;
                        height: 40px;
                        line-height: 40px;
                        background: #5d5f61;
                        position: absolute;
                        top: -8px;
                        right: 20px;
                        border-radius: 4px;
                        text-align: center;
                        font-size: 16px;
                        color: white;

                        span {
                            cursor: pointer;
                        }
                    }
                }

                .icon-del:hover {
                    ul {
                        display: block;
                    }
                }
            }
        }

        .imageList {
            max-height: 340px;
            overflow: scroll;

            .item {
                position: relative;
                width: 320px;

                // height: ;
                img {
                    width: 300px;
                }
            }
        }

        .el-dropdown {
            width: 50px;
            height: 50px;
            position: absolute;
            z-index: 100;
            top: 0px;
            font-size: 30px;
            right: 25px;
            text-align: center;
            line-height: 50px;
            background-color: rgba($color: #ffffff, $alpha: 0.3);
        }

        .el-button--primary {
            margin-bottom: 10px;
        }

        .buttonNav {
            width: 100%;
            text-align: center;
        }
    }

}

.draw-form {
    .warp-list {
        p {
            margin-bottom: 8px;
        }
    }

    .button-wrap {
        margin: 20px 0 10px 0;
    }
}

/deep/.button-wrap {
    display: flex;
    justify-content: center;

    &.bg>div {

        &:nth-child(1) {
            color: #ffffff;
            border-color: #979797;
        }

        &:nth-child(2) {
            color: #029eff;
            border-color: #029eff;
        }
    }

    &>div {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 116px;
        height: 38px;
        border-radius: 5px;
        // border: 1px solid #DCDFE6;
        border: 1px solid #2CE1FF;
        cursor: pointer;
        margin: 0 14px;

        i.el-icon-plus {
            margin-right: 4px;
        }
    }
}

.paramasTitle {
    display: flex;
    width: 100%;
    justify-content: space-around;
    margin-bottom: 10px;
}

.input-box {
    display: flex;
    margin-bottom: 10px;
}

::v-deep.is-checked {
    color: #2CE1FF;
}
</style>
