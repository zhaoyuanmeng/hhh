<!--
 * @FileDescription: 标绘->三维标注（标签）
 * @Author: yuanhaijun
 * @Date: 2022.11.28
 * @LastEditors: Alex
 * @LastEditTime: 2024-06-20 16:10:23
 -->
<template>
    <div>
        <div v-if="!isShowAdd" class="cloud-func tag">
            <div class="func-title">
                标签
                <!-- <span @click.stop="handleClose">
                                <i class="el-icon-close"></i>
                            </span> -->
                <el-icon class="" @click.stop="handleClose" style="cursor:pointer">
                    <CircleClose />
                </el-icon>
            </div>
            <div class="func-warp">
                <el-scrollbar>
                    <div  v-for="(item, index) in dataList" :key="index" @click.stop="handleClickLabel(item, index)" @dblclick.stop="handleDbClick(item, index)">
                        <div :class="['label-warp-list', { active: index == activeName }]" v-if="groupId===item.groupId">
                        <ul class="warp-list-top">
                            <li>{{ item.text }}</li>
                            <img :src="retainAfter(item.imagePath,'/images')" alt="" />
                            <!-- <img :src="item.imagePath1" alt="" /> -->
                        </ul>
                        <el-dropdown placement="left" @command="handleCommand">
                            <span class="el-dropdown-link">
                                <svg-icon icon-class="cloud-more" class-name="icon"> </svg-icon>
                            </span>
                            <template v-slot:dropdown>
                                <el-dropdown-menu class="cloud-dropdown">
                                    <el-dropdown-item :command="{ type: '编辑', item: item, index }">编辑</el-dropdown-item>
                                    <el-dropdown-item :command="{ type: '删除', item: item, index }">删除</el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                    </div>
                    </div>
                 
                </el-scrollbar>

                <div class="warp-footer">
                    <div @click="handleLabelAdd">新增标签</div>
                </div>
            </div>
        </div>

        <!-- 新增标签 -->
        <add-tmpl ref="add" v-if="isShowAdd" />
    </div>
</template>

<script>
import AddTmpl from "./common/Add.tmpl"
import { CircleClose } from '@element-plus/icons-vue'
import { sessionStorage } from '@/utils/storage'
// import {retainAfter} from '@/utils/index'
export default {
    name: "Mark",
    data() {
        return {
            isShowAdd: false,
            dataList: [],
            activeName: null,
            groupId:sessionStorage.get('groupId')||''
        };
    },
    components: { AddTmpl, CircleClose },
    created() {
        this.init();
    },
    methods: {
        retainAfter(path, search) {
      const parts = path.split(search);
      return parts.length > 1 ? search + parts.slice(1).join('') : '';
    },
        async handleCommand(command) {
            console.log(command)
            if (command.type === "编辑") {
                this.isShowAdd = true;
                this.$nextTick(() => {
                    this.$refs.add.setData(command.item);
                });
            } else if (command.type === "删除") {
                this.isShowAdd = true
                window.__g.marker.delete(command.item.id)
                // let datas = sessionStorage.get('QXZS_3dMarker')
                this.deletePointLine(command, command.index);
                // this.dataList.splice(command.index, 1)
                // await this.setData()
                console.log(this.dataList, ' this.dataList this.dataList');
                // console.log(command, 'commandcommandcommand');
                this.$forceUpdate()
            }
        },
        deletePointLine(data,index){
            this.dataList.splice(index, 1);
            sessionStorage.set("QXZS_3dMarker",this.dataList)
            this.isShowAdd = false
        },
        handleDbClick(item, index) {
            // window.sealAPI._tag.focus_biz(item);
            window.__g.marker.focus(item.id)
        },
        handleClickLabel(item, index) {
            this.activeName = index;
        },
        handleLabelAdd() {
            this.isShowAdd = true;
        },
        async setData() {
            // this.dataList = await window.sealAPI._tag.getTags();
            // let datas = await window.sealAPI._tag.getTags();
            let datas = sessionStorage.get('QXZS_3dMarker')
            // let items= datas.filter(item => item.groupId !== '3dMarkers') ||[]
            this.dataList = datas
            // this.$forceUpdate()
        },
        async init(){
            let datas = sessionStorage.get('QXZS_3dMarker')  
            this.dataList = datas
            if(datas&&datas.length>0){
            window.__g.marker.add(datas)
            }
            
        },
        async handleClose() {
            // if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
            this.$emit('close')
        },
    },
    mounted() { },
}
</script>

<style lang="scss" scoped>
@import "@/styles/func3.scss";

.tag .func-warp {
    :deep(.el-scrollbar__wrap) {
        max-height: 720px;
        margin-bottom: 0 !important;
        overflow-x: hidden;

        .label-warp-list {
            margin-bottom: 12px;
            height: 164px;
            position: relative;
            background: #4b5059;
            box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
                0px 0px 0px 0px rgba(0, 0, 0, 0.5);
            border-radius: 16px;
            border: 2px solid #4b5059;
            display: flex;
            flex-direction: column;
            justify-content: space-between;

            &.active {
                border-color: #02b2ff;
            }

            .warp-list-top {
                width: 100%;
                height: 100%;
                padding: 18px;
                padding-bottom: 0;
                box-sizing: border-box;
                text-align: center;
                overflow: hidden;

                li {
                    font-size: 18px;
                    font-weight: 400;
                    color: #ffffff;
                    text-align: left;
                }

                img {
                    width: auto;
                    height: auto;
                }
            }

            .el-dropdown {
                position: absolute;
                top: 18px;
                right: 18px;
            }
        }
    }

    .warp-footer {
        display: flex;
        justify-content: center;
        margin-top: 10px;

        div {
            width: 100%;
            line-height: 38px;
            cursor: pointer;
            border-radius: 5px;
            border: 1px solid #029eff;
            text-align: center;
        }
    }
    :deep(.el-scrollbar__view){
        height: 500px;
    }
}
</style>
