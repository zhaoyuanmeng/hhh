<template>
    <div class="analog-camera-container">
        <el-icon class="close-btn" @click="handleClose">
            <CircleClose />
        </el-icon>
        <!-- <i class="el-icon-close close-btn" @click="handleClose"></i> -->
        <ul class="actionButtons">
            <li v-for="(item, index) in actionList" :class="{ active: activeId == item.id }" :key="index"
                @click="btnEvent(item, index)">
                <img :src="item.src" :alt="item.name" />
            </li>
        </ul>
    </div>
</template>
<script>
import { CircleClose } from '@element-plus/icons-vue'
export default {
    components: {
        CircleClose
    },
    data() {
        return {
            actionList: [
                {
                    id: 1,
                    icon: "rise",
                    name: "上升",
                    action: "moveUp",
                    src: require("@/assets/images/analogAmeraBtns/moveUp.png"),
                },
                {
                    id: 2,
                    icon: "left_rotation",
                    name: "左转",
                    action: "turnLeft",
                    src: require("@/assets/images/analogAmeraBtns/turnLeft.png"),
                },
                {
                    id: 3,
                    icon: "go",
                    name: "前进",
                    action: "moveForward",
                    src: require("@/assets/images/analogAmeraBtns/moveForward.png"),
                },
                {
                    id: 4,
                    icon: "right_rotation",
                    name: "右转",
                    action: "turnRight",
                    src: require("@/assets/images/analogAmeraBtns/turnRight.png"),
                },
                {
                    id: 5,
                    icon: "up",
                    name: "抬头",
                    action: "turnUp",
                    src: require("@/assets/images/analogAmeraBtns/turnUp.png"),
                },
                {
                    id: 6,
                    icon: "drop",
                    name: "下降",
                    action: "moveDown",
                    src: require("@/assets/images/analogAmeraBtns/moveDown.png"),
                },
                {
                    id: 7,
                    icon: "left",
                    name: "左移",
                    action: "moveLeft",
                    src: require("@/assets/images/analogAmeraBtns/moveLeft.png"),
                },
                {
                    id: 8,
                    icon: "goback",
                    name: "后退",
                    action: "moveBackward",
                    src: require("@/assets/images/analogAmeraBtns/moveBackward.png"),
                },
                {
                    id: 9,
                    icon: "right",
                    name: "右移",
                    action: "moveRight",
                    src: require("@/assets/images/analogAmeraBtns/moveRight.png"),
                },
                {
                    id: 10,
                    icon: "down",
                    name: "低头",
                    action: "turnDown",
                    src: require("@/assets/images/analogAmeraBtns/turnDown.png"),
                },
            ],
            activeId: 0,
        };
    },
    beforeUnmount() {
        window.sealAPI._camera.stop();
        window.sealAPI._cameraTour.cancel();
    },
    methods: {
        handleClose() {
            // if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
            this.$emit('close')
        },
        btnEvent(item, index) {
            // 判断当前激活状态
            if (this.activeId == item.id) {
                this.activeId = -1;
                window.sealAPI._camera.stop();
            } else {
                this.activeId = item.id;
                if (item.action) window.sealAPI._camera[item.action]();
                else window.sealAPI._camera.stop();
            }
        },
    },
};
</script>
<style lang="scss" scoped>
.analog-camera-container {
    position: fixed;
    bottom: 50px;
    right: 50px;
    z-index: 5;
    background: rgba(0, 0, 0, 0);
    width: 320px;
    height: 110px;

    .close-btn {
        cursor: pointer;
        color: #fff;
        width: 50px;
        height: 50px;
        font-size: 20px;
        font-weight: 900;
        padding: 14px;
        border-radius: 6px;
        background: rgba(39, 46, 53, 0.9);
        position: absolute;
        right: 2px;
        top: -60px;
        text-align: center;
    }

    ul.actionButtons {
        height: 100%;
        display: flex;
        flex-wrap: wrap;
        align-content: space-between;
        /* flex-direction: row; */
        justify-content: space-between;

        li {
            cursor: pointer;
            display: inline-flex;
            background: linear-gradient(180deg,
                    rgba(21, 25, 37, 0.85) 0%,
                    rgba(26, 31, 46, 0.9) 100%);
            box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
                0px 0px 0px 0px rgba(0, 0, 0, 0.5),
                0px 11px 17px -11px rgba(7, 8, 15, 0.95);
            width: 50px;
            height: 50px;
            border-radius: 5px;
            color: #fff;

            img {
                margin: auto;
                width: 40px;
            }
        }

        li:nth-of-type(1),
        li:nth-of-type(6) {
            margin-right: 16px;
        }

        li:nth-of-type(5),
        li:nth-of-type(10) {
            margin-left: 16px;
        }

        .active {
            background: rgba($color: rgb(90, 90, 90), $alpha: 0.9) !important;
        }
    }
}
</style>
