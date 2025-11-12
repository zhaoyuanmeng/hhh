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
            视点
            <el-icon class="" @click.stop="handleClose">
                <CircleClose />
            </el-icon>
        </div>

        <div v-show="!isShowAddPanel" class="func-warp draw-list">
            <div class="warp-list">
                <el-scrollbar style="height: 100%">
                    <div class="list" v-for="(item, index) in cameraViews" :key="index">
                        <div class="list-left">
                            <el-icon @click="handleClickCamera(item)" class="icon">
                                <Camera />
                            </el-icon>
                            <!-- <i class="el-icon-camera-solid icon" @click="handleClickCamera(item)"></i> -->
                            <span>{{ item.name }}</span>
                        </div>
                        <el-dropdown placement="left" @command="handleCommand">
                            <span class="el-dropdown-link">
                                <svg-icon icon-class="cloud-more" class-name="icon"> </svg-icon>
                            </span>
                            <template v-slot:dropdown>
                                <el-dropdown-menu class="cloud-dropdown">
                                    <el-dropdown-item :command="{ type: '删除', item: item }">删除</el-dropdown-item>
                                </el-dropdown-menu>
                            </template>

                        </el-dropdown>
                    </div>
                </el-scrollbar>
            </div>
            <div class="button-wrap">
                <div @click="drawParam()"> <i class="el-icon-plus"></i> 新增视点 </div>
            </div>
        </div>

        <div v-show="isShowAddPanel" class="func-warp draw-form">
            <div class="warp-list">
                <p>视点名称</p>
                <el-input v-model="nameVP" placeholder="请输入名称" maxlength="200"></el-input>
            </div>
            <div class="button-wrap bg">
                <div @click="handleCancel"> 取消 </div>
                <div @click="handleSure" class="bg"> 确定 </div>
            </div>
        </div>
    </div>
</template>

<script>
import { get, post, put, deletes } from "@/utils/fetch";
import { Camera, CircleClose } from '@element-plus/icons-vue';
import { getBsCameraLocationList, saveBsCameraLocation, delByIdsBsCameraLocation } from '@/api/dataApi'
export default {
    data() {
        return {
            cameraViews: [],
            nameVP: "",
            isShowAddPanel: false,
        };
    },
    created() {
        this.initData()
    },
    components: {
        Camera,
        CircleClose
    },
    methods: {
        // 获取数据库视点
        async getViewPoint() {
            let data = sessionStorage.getItem("QXZS_viewPoint")
            return data ? JSON.parse(data) : []
        },
        // 删除数据库视点
        async deleteViewPoint(obj) {
            let index = this.cameraViews.findIndex(i => i.name === obj.name);
            this.cameraViews.splice(index, 1);
            sessionStorage.setItem("QXZS_viewPoint", JSON.stringify(this.cameraViews))
        },
        // 保存视点到后端数据库
        async saveViewPoint(obj) {
            let data = sessionStorage.getItem("QXZS_viewPoint")
            if (data) {
                data = JSON.parse(data)
                data.push(obj)
            } else {
                data = [obj]
            }
            sessionStorage.setItem("QXZS_viewPoint", JSON.stringify(data))

            this.isShowAddPanel = false;
            this.initData()
        },
        async initData() {
            this.cameraViews = await this.getViewPoint();
        },
        handleClickCamera(item) {
            window.sealAPI._camera.set_biz(item.visualAngle);
        },
        async handleCommand(command) {
            let { type, item } = command
            if (type === "删除") {
                this.deleteViewPoint(item);
            }
        },
        drawParam() {
            this.nameVP = "";
            this.isShowAddPanel = true
        },
        async handleSure() {
            if (!this.nameVP) return this.$message.warning("名称不能为空");
            let targetIndex = this.cameraViews.findIndex(
                (item) => item.name == this.nameVP
            );
            if (targetIndex >= 0 && targetIndex !== this.editIndex)
                return this.$message.warning("名称不能重复！");
            let params = {
                name: this.nameVP
            };

            // 保存当前视角
            let resV = await window.sealAPI._camera.get();
            resV = [resV].map(({ x, y, z, pitch, yaw }) => ({ x, y, z, pitch, yaw }));
            params.visualAngle = resV[0];

            this.saveViewPoint(params);
        },
        handleCancel() {
            this.isShowAddPanel = false;
        },
        handleClose() {
            // if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
            this.$emit('close')
        },
    },
};
</script>

<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";

.draw-list {
    .warp-list {
        overflow: hidden;

        .el-scrollbar__wrap {
            max-height: 70vh !important;
            margin-bottom: 0 !important;
            overflow-x: hidden;

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
</style>
