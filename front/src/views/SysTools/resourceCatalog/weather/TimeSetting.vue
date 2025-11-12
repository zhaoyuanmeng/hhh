<!--
 * @FileDescription: 时间设置
 * @Author: yuanhaijun
 * @Date: 2023.03.16
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2023.03.16
 -->
<template>
    <div class="cloud-func">
        <div class="func-title">
            时间
            <!-- <span @click.stop="handleClose">
                            <i class="el-icon-close"></i>
                        </span> -->
            <el-icon class="" @click.stop="handleClose">
                <CircleClose />
            </el-icon>
        </div>
        <div class="time-set">
            <div class="switch-container">
                <div :class="{ active: curActive == 'day' }" @click="handleSwitch(12, true)">
                    <img src="@/assets/images/cloud/timer-day.png" />
                    <p>白天</p>
                </div>
                <div :class="{ active: curActive == 'night' }" @click="handleSwitch(21, true)">
                    <img src="@/assets/images/cloud/timer-night.png" />
                    <p>夜景</p>
                </div>
                <div :class="{ active: curActive == 'cus' }" @click="handleCustomize">
                    <img src="@/assets/images/cloud/timer-loop.png" />
                    <p>自定义</p>
                </div>
            </div>
            <template v-if="curActive == 'cus'">
                <el-row class="c_loop">
                    <el-col :span="10">
                        <el-checkbox v-model="fModel.loop" @change="handleCustomize">时间循环</el-checkbox>
                    </el-col>
                    <el-col :span="14">
                        <el-input-number v-model="fModel.minute" label="模拟时长（分钟）" controls-position="right" disabled>
                        </el-input-number>
                    </el-col>
                </el-row>
                <el-row class="c_loop">
                    <el-col :span="10">
                        <span>日期</span>
                    </el-col>
                    <el-col :span="14">
                        <el-date-picker v-model="fModel.date" type="date" @change="handleCustomize"> </el-date-picker>
                    </el-col>
                </el-row>
                <el-row class="c_loop">
                    <el-col :span="10"> <span>时间</span> </el-col>
                    <el-col :span="14">
                        <el-time-picker v-model="fModel.time" @change="handleCustomize"> </el-time-picker>
                    </el-col>
                </el-row>
                <el-slider v-model="fModel.hour" :step="1" :min="1" :max="23" :show-tooltip="false" @change="handleSwitch"
                    show-stops> </el-slider>
                <ul class="c_period">
                    <li v-for="(item, index) in list"
                        :class="fModel.hour >= item.hour[0] && fModel.hour <= item.hour[1] ? 'active' : ''" :key="index"
                        @click="setTimeBuyTag(item)">
                        {{ item.name }}
                    </li>
                </ul>
            </template>
        </div>
    </div>
</template>

<script >
import { onBeforeUnmount } from 'vue'
import { CircleClose } from '@element-plus/icons-vue'
export default {
    name: 'TimeSetting',
    components: {
        CircleClose
    },
    data() {
        return {
            isCustomize: false, // 是否自定义
            fModel: {
                loop: false, // 是否日夜循环
                date: new Date(),
                time: new Date(),
                hour: window.sealAPI._weather.getHour() || 12,
                minute: 3,
            },
            list: [
                // { name: "凌晨", hour: [1, 5] },
                { name: "早晨", hour: [6, 8] },
                // { name: "上午", hour: [9, 10] },
                { name: "中午", hour: [11, 13] },
                // { name: "下午", hour: [14, 16] },
                { name: "傍晚", hour: [17, 18] },
                { name: "夜晚", hour: [19, 23] },
            ],
            scrolling: false, // LOOP：是否开启滚动
            timer: null, // LOOP：定时器
        }
    },
    watch: {
        "fModel.loop"(val, old) {
            this.scrolling = val;
            if (val) {
                this.startScrolling();
            } else {
                this.stopScrolling();
            }
        },
        "fModel.time"(val) {
            this.handleCustomize()
        }
    },
    computed: {
        curActive() {
            if (this.isCustomize) return "cus";
            else {
                if (this.fModel.hour > 7 && this.fModel.hour < 18) return "day";
                else return "night";
            }
        },
    },
    mounted() {
        // 时间设置观察视角
        // window.sealAPI._camera.set(494333.75, 2490241.25, 476.352356, -13.394854, -126.175156, 1);
        window.sealAPI._weather.setDarkMode_biz(false);
        // this.handleSwitch(this.curActive === "day" ? 10 : 21, true);
    },
    methods: {
        // LOOP：开始滚动
        startScrolling() {
            let that = this;
            let PARM = {
                year: that.fModel.date.getFullYear(),
                month: that.fModel.date.getMonth() + 1,
                day: that.fModel.date.getDate(),
                hour: that.fModel.time.getHours(),
                minute: that.fModel.time.getMinutes(),
                seconds: that.fModel.time.getSeconds(),
            };
            let DT = new Date(`${PARM.year}-${PARM.month}-${PARM.day} ${PARM.hour}:${PARM.minute}:${PARM.seconds}`);
            that.timer = setInterval(() => {
                DT.setSeconds(480 * 1); // 480/s
                // that.$set(that.fModel, "date", new Date(DT.getFullYear(), DT.getMonth(), DT.getDate(), DT.getHours(), DT.getMinutes(), DT.getSeconds()));
                // that.$set(that.fModel, "time", new Date(DT.getFullYear(), DT.getMonth(), DT.getDate(), DT.getHours(), DT.getMinutes(), DT.getSeconds()));
                // that.$set(that.fModel, "hour", PARM.hour);
                that.fModel.data = new Date(DT.getFullYear(), DT.getMonth(), DT.getDate(), DT.getHours(), DT.getMinutes(), DT.getSeconds())
                that.fModel.time = new Date(DT.getFullYear(), DT.getMonth(), DT.getDate(), DT.getHours(), DT.getMinutes(), DT.getSeconds())
                that.fModel.hour = PARM.hour
            }, 1000);
        },
        // LOOP：停止滚动
        stopScrolling() {
            clearInterval(this.timer);
            this.$nextTick(() => {
                this.setDateTimeW(this.fModel.time, false);
            });
        },
        // API：设置日期时间
        setDateTimeW(DT = new Date(), dnLoop = false) {
            let PARM = {
                year: DT.getFullYear(),
                month: DT.getMonth() + 1,
                day: DT.getDate(),
                hour: DT.getHours(),
                minute: DT.getMinutes(),
                daynightLoop: dnLoop, // 是否日夜循环：0\false\关闭， 1\true\开启
            };
            window.sealAPI._weather.setDateTime(PARM.year, PARM.month, PARM.day, PARM.hour, PARM.minute, PARM.daynightLoop);
        },
        // 切换白天、夜景、自定义等
        handleSwitch(hour, flag, DT) {
            // LOOP：slider、li、checkbox清除定时
            // if (this.scrolling) {
            //     // flag：slider(undefined)、li("")、others(true/false)
            //     if (flag === "") {
            //         // this.$set(this.fModel, "loop", false);
            //         this.fModel.loop = false
            //     } else {
            //         return;
            //     }
            // }
            console.log(hour, flag, DT, 'hour, flag, DThour, flag, DT');
            this.isCustomize = !flag
            this.fModel.hour = hour
            if (!DT) {
                DT = new Date();
                DT.setHours(hour);
            }
            // LNK：slider、li（时间）
            if (flag === undefined || flag === "") {
                this.fModel.time = DT
            }
            this.$nextTick(() => {
                let dnLoop = this.isCustomize && this.fModel.loop;
                this.setDateTimeW(DT, dnLoop);
                // this.setDateTimeW(DT, false);
            });
        },
        // OPR：自定义
        handleCustomize() {
            let PARM = {
                year: this.fModel.date.getFullYear(),
                month: this.fModel.date.getMonth() + 1,
                day: this.fModel.date.getDate(),
                hour: this.fModel.time.getHours(),
                minute: this.fModel.time.getMinutes(),
                seconds: this.fModel.time.getSeconds(),
            };
            let DT = new Date(`${PARM.year}-${PARM.month}-${PARM.day} ${PARM.hour}:${PARM.minute}:${PARM.seconds}`);
            let _hour = PARM.hour + (PARM.minute < 30 ? 0 : 1);
            console.log(_hour, '_hour_hour_hour_hour_hour_hour_hour_hour');
            this.handleSwitch(_hour, false, DT);
        },
        // C：早晨、中午、傍晚、夜晚等
        setTimeBuyTag(item) {
            let _hour = Math.ceil((item.hour[0] + item.hour[1]) / 2);
            this.handleSwitch(_hour, "");
        },
        handleClose() {
            // LOOP：清除定时
            if (this.scrolling) this.fModel.loop = false
            this.$nextTick(() => {
                // if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
                this.$emit('close')
            });
        },
    },
    async onBeforeUnmount() {
        await this.handleClose();
    }
}
</script>

<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";

.time-set {
    padding: 10px 25px;
    padding-bottom: 20px;
    box-sizing: border-box;
    font-size: 16px;
    font-weight: 400;
    color: #ffffff;

    ul {
        display: flex;
        justify-content: space-between;
        font-size: 12px;
        font-weight: 400;
        color: #9b9ea3;

        .active {
            color: #ffffff;
        }
    }

    .switch-container {
        padding: 16px 0 20px 0;
        display: flex;
        justify-content: space-between;

        div {
            text-align: center;
            border-radius: 8px;
            border: solid 2px rgba(46, 52, 65, 0.8);
            background: rgba(46, 52, 65, 0.8);
            width: 70px;
            height: 70px;
            cursor: pointer;
            display: inline-flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;

            img {
                height: 26px;
                margin-bottom: 5px;
            }

            p {
                font-size: 12px;
                color: #fff;
            }
        }

        .active {
            border-color: rgba(39, 120, 169, 0.8);
            background: rgba(83, 92, 131, 0.8);
        }
    }

    .c_loop {
        display: flex;
        align-items: center;
        margin-bottom: 16px;

        &>.el-col:first-child {
            color: #9b9ea3;
            font-size: 14px;
        }

        /deep/ {
            .el-checkbox {
                color: #9b9ea3 !important;
            }

            .el-input-number,
            .el-date-editor {
                width: 100% !important;
                background: #494d52;
                border-radius: 4px;
                border: 1px solid #646b6f;

                .el-input-number__decrease,
                .el-input-number__increase {
                    background: #ffffff0d;
                    border-color: #ffffff1a;
                    color: #ffffff99;
                }

                .el-input__inner {
                    border: none;
                    font-size: 14px;
                    color: #ffffff;
                    background: transparent;
                }
            }
        }

        span {
            color: #2CE1FF;
        }
    }

    .el-slider {
        border-top: 1px solid #646b6f;
    }
}

.el-checkbox__label {
    color: #2CE1FF;
}
</style>