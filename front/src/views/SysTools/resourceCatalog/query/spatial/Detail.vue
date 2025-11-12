<!-- 
 * @description: 空间查询->详情
 * @fileName: query/spatial/Detail.vue
 * @author: yuanhaijun
 * @date: 2023-03-22
!-->
<template>
    <div class="cloud-func">
        <div class="func-title">
            标签详情
            <span @click="handleClose">
                <i class="el-icon-close"></i>
            </span>
        </div>

        <el-form size="mini" label-position="top" :model="rowData" class="func-warp">
            <el-scrollbar>
                <el-form-item v-for="(value, key) in rowData" :key="key" :label="key">
                    {{ value || "" }}
                </el-form-item>
            </el-scrollbar>
            <el-form-item class="btn">
                <el-button @click="handleReturn">返回</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
export default {
    props: {
        rowData: {
            type: Object,
            default: () => { },
        },
    },
    data() {
        return {};
    },
    created() { },
    methods: {
        handleClose() {
            this.$parent.handleClose();
        },
        handleReturn() {
            this.$parent.isShowDetail = false;
            this.$parent.rowData = {};
            this.$parent.isShowExcel = true;
        },
    },
};
</script>

<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";

/deep/.func-warp {

    .el-scrollbar {
        min-height: 326px;
        max-height: 64vh;
        overflow-y: auto;
        margin: 18px 0;

        .el-scrollbar__wrap {
            width: 100%;
        }

        .el-form-item:last-child {
            margin-bottom: 0;
        }
    }

    .el-form-item__label {
        opacity: 0.6;
        padding-bottom: 0;
    }

    .el-form-item__content {
        color: #ffffff;
        line-height: normal;
    }
}
</style>