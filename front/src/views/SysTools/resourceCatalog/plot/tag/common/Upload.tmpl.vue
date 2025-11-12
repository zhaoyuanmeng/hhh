<!--
 * @FileDescription: 三维标注->标签（上传）
 * @Author: yuanhaijun
 * @Date: 2022.11.28
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2022.11.28
 -->
<template>
    <div class="cloud-func upload">
        <div class="func-title">
            地址
            <span @click.stop="handleClose">
                <i class="el-icon-close"></i>
            </span>
        </div>
        <div class="func-warp">
            <div class="warp-list">
                <p>地址名称</p>
                <el-input v-model="input" placeholder="请输入"></el-input>
            </div>

            <div class="warp-footer">
                <div @click="handleClose">取消</div>
                <div class="primary" @click="handleSure">确定</div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "Upload",
    data() {
        return {
            input: "",
        };
    },
    mounted() { },
    methods: {
        handleSure() {
            if (!this.input) return this.$message.warning("地址不能为空");

            this.$parent.handleImage(this.input);
            this.handleClose();
        },
        handleClose() {
            this.$parent.isShowUpload = false;
            this.$parent.isShowAdd = true;
        },
    },
    beforeUnmount() { },
};
</script>

<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";

.upload .func-warp {
    .warp-list {
        margin-bottom: 24px;

        p {
            font-size: 16px;
            color: #9b9ea3;
            margin-bottom: 6px;
        }
    }

    .warp-footer {
        display: flex;
        justify-content: center;

        div {
            min-width: 120px;
            line-height: 38px;
            cursor: pointer;
            border-radius: 5px;
            border: 1px solid #029eff;
            text-align: center;

            &.primary {
                background: #029eff;
                margin-left: 20px;
            }
        }
    }
}</style>