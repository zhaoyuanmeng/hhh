/*
 * @Author: your name
 * @Date: 2022-03-30 10:19:03
 * @LastEditTime: 2024-06-19 10:45:30
 * @LastEditors: Alex
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \DTSWEEKLY_ZHGK\src\stores\tools.ts
 */
import { defineStore } from "pinia";
export const useSysToolsStore = defineStore({
    id: "SysTools",
    state: () => ({
        // 剖切标志： 0 默认 1 纵剖切 2 横剖切 3 体剖切 4 挖洞
        clipFlag: 0,
        // 剖切点
        clipPos: [],
        isShowTools: false,
    }),

    actions: {
        async SetClipFlag(pyload) {
            this.$patch({
                clipFlag: pyload,
            })
        },
        async SetClipPos(pyload) {
            this.$patch({
                clipPos: pyload,
            })
        },
        async SetIsShowTools(pyload) {
            this.$patch({
                isShowTools: pyload,
            })
        },
    },
});
