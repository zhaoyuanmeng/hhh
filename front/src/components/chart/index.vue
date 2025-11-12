<template>
  <div ref="chartRef" class="echarts-container"></div>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import * as echarts from "echarts";

const props = defineProps({
  options: {
    type: Array,
    default:[]
  },
  title:{
    type:String,
    default:'历年任务数量'
  }
});

const chartRef = ref(null);
let chartInstance = null;

const initChart = () => {
  if (chartInstance) {
    chartInstance.dispose();
  }
  chartInstance = echarts.init(chartRef.value);
  let datas = []
  let xNames = []
  if(props.options?.length){
    datas = props.options.map(item=>item.total)
    xNames = props.options.map(item=>item.year)
  }
  // let xNames= ["2019", "2020", "2021", "2022", "2023", "2024"]
  let option = {
    title: {
      text: props.title,
      x: "left",
      y: "0%",
      textStyle: {
        color: "#fff",
        fontSize: "14",
      },
    },
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "shadow",
      },
    },
    grid: {
      left: "3%",
      right: "4%",
      top:'15%',
      bottom: datas.length>6?"15%":"15%",
      containLabel: true,
    },
    xAxis: [
      {
        type: "category",
        data: xNames,
        axisLine: {
          lineStyle: {
            color: "rgba(255,255,255,1)",
          },
        },
        axisTick: {
          show: false,
        },
        axisLabel: {
          margin: 10,
          color: "#ffffff",
          textStyle: {
            fontSize: 12,
          },
        },
      },
    ],
    yAxis: [
      {
        type: "value",
        axisLabel: {
          formatter: "{value}",
          color: "#ffffff",
        },
        axisLine: {
          show: false,
          lineStyle: {
            color: "rgba(255,255,255,1)",
          },
        },
        splitLine: {
          lineStyle: {
            color: "rgba(255,255,255,0.12)",
          },
        },
      },
    ],
    dataZoom: [
      {
        show: datas.length>6?true:false,
        type: 'slider',
        height: 15,
        xAxisIndex: [0],
        bottom: "6%",
        start: datas.length>6?40:0,
        end: 100,
        filterMode: 'empty', // 过滤模式，'empty' 表示数据不可见时使用空距表示
        showDataShadow: false, // 是否显示数据阴影
        handleIcon:
          "path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z",
        handleSize: "100%",
        handleStyle: {
          color: "#d3dee5",
        },
        textStyle: {
          color: "#fff",
        },
        borderColor: "#90979c",
      }
    ],
    series: [
      {
        name: "任务数",
        type: "bar",
        barWidth: "30%",
        itemStyle: {
          normal: {
            color: new echarts.graphic.LinearGradient(
              0,
              0,
              0,
              1,
              [
                {
                  offset: 0,
                  color: "rgba(0,244,255,1)", // 0% 处的颜色
                },
                {
                  offset: 1,
                  color: "rgba(0,77,167,1)", // 100% 处的颜色
                },
              ],
              false
            ),
          },
        },
        data: datas,
      },
    ],
  };
  chartInstance.setOption(option);
};

onMounted(() => {
  initChart();
});

watch(
  () => props.options,
  (val) => {
   if(val&&val?.length){
    initChart();
   }
  },
  { deep: true }
);

// 暴露一个方法，用于组件外部可能需要的操作，例如 resize
defineExpose({
  resize: () => {
    chartInstance && chartInstance.resize();
  },
});
</script>

<style scoped>
.echarts-container {
  width: 100%;
  height: 100%;
}
</style>
