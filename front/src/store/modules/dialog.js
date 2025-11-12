/*
 * @Author: your name
 * @Date: 2021-11-18 11:13:17
 * @LastEditTime: 2024-06-19 10:48:06
 * @LastEditors: Alex
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \vue-ts\src\stores\user.ts
 */
// @ts-check
import { defineStore } from "pinia";

export const useDialogStore = defineStore({
    id: "Dialog",
    state: () => ({
        dialogVisible: false,
        XY: [0, 0],
        LouInfo: {},
        title: "详细信息",
        // 内容
        content: [],
        lastEvent: {},
        tag: "",
        tileLayer: "",
        shapefilelayer: {},
        showPipe: false,
        showAttribute: false,
        name: "",
        id: "",
        //数据存储
        e_data: {},
        roomInfoData: {},
        roomInfoShow: false,


        player2Flag: false,
        player1CameraLoaction: [],
        player2CameraLoaction: [],
        playerHover: 1,
        leftSelectLayerId: '',
        rightSelectLayerId: '',
        dialogInfoList: [
            {
                title: null,
                name:'',
                imgType:'png',
                list: [
                    {
                        paramsName: '运行时间统计',
                        value: '',
                    },
                    {
                        paramsName: '运行时长（H）',
                        value: '',
                    },
                    {
                        paramsName: '停运时长（H）',
                        value: '',
                    },
                ]
            },
            {
                title: '工况',
                list: [
                    {
                        paramsName: '故障情况',
                        value: '正常',
                    },
                    {
                        paramsName: '检修状态',
                        value: '无检修',
                    },
                    {
                        paramsName: '运行状态',
                        value: '进行中',
                    },
                ]
            },
            {
                title: '基本信息',
                list: [
                    {
                        paramsName: '厂家',
                        value: '康力',
                    },
                    {
                        paramsName: '型号',
                        value: 'KLWG',
                    },
                    {
                        paramsName: '客服电话',
                        value: '400-601-1099',
                    },
                    {
                        paramsName: '上次维修时间',
                        value: '400-601-1099',
                    },
                ]
            },
        ]
    }),
    actions: {
        setDialogInfoList(dialogInfoList) {
            this.$patch({
                dialogInfoList
            });
        },
        setRightSelectLayerId(rightSelectLayerId) {
            this.$patch({
                rightSelectLayerId
            });
        },
        setLeftSelectLayerId(leftSelectLayerId) {
            this.$patch({
                leftSelectLayerId
            });
        },
        setPlayerHover(playerHover) {
            this.$patch({
                playerHover
            });
        },
        setPlayer2Flag(player2Flag) {
            this.$patch({
                player2Flag
            });
        },
        setPlayer1CameraLoaction(player1CameraLoaction) {
            this.$patch({
                player1CameraLoaction
            });
        },
        setPlayer2CameraLoaction(player2CameraLoaction) {
            this.$patch({
                player2CameraLoaction
            });
        },
        currentRoomInfoData(roomInfoData, roomInfoShow) {
            this.$patch({
                roomInfoData,
                roomInfoShow
            });
        },
        setDialogVisible(dialogVisible) {
            this.$patch({
                dialogVisible,
            });
            //隐藏就销毁原有数据
            !dialogVisible &&
                this.$patch({
                    content: [],
                });
        },
        setXY(XY) {
            this.$patch({
                XY,
            });
        },
        setLastEvent(lastEvent) {
            this.$patch({
                lastEvent,
            });
        },
        setDialogInfo(LouInfo) {
            this.$patch({
                LouInfo,
            });
        },
        setTitle(title) {
            this.$patch({
                title,
            });
        },
        setContent(content) {
            console.log(content, "setContent");
            this.$patch({
                content,
            });
        },
        setTag(tag) {
            this.$patch({
                tag,
            });
        },
        setTileLayer(tileLayer) {
            this.$patch({
                tileLayer,
            });
        },
        setshapefilelayer(shapefilelayer) {
            this.$patch({
                shapefilelayer,
            });
        },
        setE_data(e_data) {
            this.$patch({
                e_data,
            });
        },
        setShowPipe(showPipe) {
            this.$patch({
                showPipe,
            });
        },
        setAttribute(showAttribute) {
            this.$patch({
                showAttribute,
            });
        },
        setName(name) {
            this.$patch({
                name,
            });
        },
        setId(id) {
            this.$patch({
                id,
            });
        },
    },
});
