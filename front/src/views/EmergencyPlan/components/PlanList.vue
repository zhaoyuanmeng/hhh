<template>
  <div class="plan-list">
    <div class="count-box">
      <div class="total-count">
        <span class="label">应急预案总数</span>
        <span class="count">21</span>
        <span>个</span>
      </div>
      <div class="point-count">
        <span class="label">重点点位</span>
        <span class="count">4</span>
        <span>个</span>
      </div>
    </div>
    <div class="btn-box">
      <div class="btn-item" :class="curBtn === item.value ? 'active' : ''" v-for="item in brnList" :key="item.value"
        @click="handleClickBtn(item.value)">
        {{ item.label }}
      </div>
    </div>
    <div class="search-box">
      <el-select v-model="searchVal" placeholder="请选择" style="width: 256px">
        <el-option v-for="item in selOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-button class="create-btn" type="primary" @click="handleCreate">创建</el-button>
    </div>
    <el-scrollbar height="77%">
      <div class="list-box">
        <div class="list-item" v-for="(item, index) in listData" :key="item.id">
          <div class="left-info">
            <div class="title-box">
              <span class="index">{{ index + 1 }}</span>
              <span class="title">{{ item.title }}</span>
            </div>
            <div class="info-box">
              <p class="info">
                <span class="label">预案等级：</span>
                <span :class="colorMap[item.level]">{{ item.level }}</span>
              </p>
              <p class="info">
                <span class="label">负责人：</span>
                <span>{{ item.header }}</span>
              </p>
              <p class="info">
                <span class="label">地点：</span>
                <span>{{ item.location }}</span>
              </p>
            </div>
          </div>
          <div class="right-info">
            <div class="count-info">
              <span class="count">{{ item.count }}</span>
              <span>个</span>
            </div>
            <p class="view-detail" @click="handleViewDetail(item)">查看详情</p>
          </div>
        </div>
      </div>
    </el-scrollbar>
  </div>

  <CreateDialogList ref="createRef" />
</template>

<script setup>
import { ref, computed } from 'vue'
import CreateDialogList from './CreateDialogList.vue'
// store
import useEmergencyStore from '@/store/modules/emergencyPlan'
const EmergencyStore = useEmergencyStore()

const curBtn = ref('gs')
const brnList = ref([
  { label: '高速', value: 'gs' },
  { label: '高铁', value: 'gt' },
  { label: '现场', value: 'xc' },
  { label: '住地', value: 'zd' },
  { label: '城市道路', value: 'city' },
])
const handleClickBtn = (val) => {
  curBtn.value = val
}

const searchVal = ref('1')
const selOptions = ref([
  { label: '全部', value: '1' }
])

const listData = ref([
  { id: 1, title: '津雄高速一级预案', count: 4, level: '一级', header: '李思思', location: '津雄高速' },
  { id: 2, title: '津雄高速二级预案', count: 4, level: '二级', header: '卫一飞', location: '津雄高速' },
  { id: 3, title: '大广高速', count: 4, level: '三级', header: '李思思', location: '大广高速' },
  { id: 4, title: '京雄高速', count: 4, level: '三级', header: '王龙', location: '京雄高速' },
  { id: 5, title: '京港澳高速', count: 4, level: '一级', header: '李思思', location: '京港澳高速' },
])

const colorMap = {
  '一级': 'level-one',
  '二级': 'level-two',
  '三级': 'level-three',
}

// 查看详情
const handleViewDetail = (item) => {
  EmergencyStore.setCurContent('planDetail')
}

const createRef = ref(null)
const handleCreate = () => {
  createRef.value.openDialog()
}
</script>

<style lang="scss">
.plan-list {
  height: 100%;

  .count-box {
    display: flex;
    justify-content: space-between;

    .label {
      font-family: YouSheBiaoTiHei;
      font-size: 18px;
    }

    .count {
      font-family: D-DIN;
      font-size: 40px;
      color: #00CEFF;
      margin: 0 4px 0 9px;
    }
  }

  .btn-box {
    display: flex;
    gap: 4px;
    font-size: 14px;
    padding: 12px 0;
    border-bottom: 1px solid rgba($color: #2644AD, $alpha: .3);

    .btn-item {
      flex: 1;
      height: 40px;
      line-height: 40px;
      text-align: center;
      background: url('@/assets/emergencyplan/btn_normal.png');

      &:hover {
        background: url('@/assets/emergencyplan/btn_hover.png');
      }
    }

    .active {
      background: url('@/assets/emergencyplan/btn_press.png');
    }
  }

  .search-box {
    display: flex;
    align-items: center;
    gap: 12px;
    margin: 12px 0;

    .create-btn {
      width: 92px;
      height: 36px;
    }
  }

  .list-box {
    display: flex;
    flex-direction: column;
    gap: 8px;
    font-size: 14px;

    .list-item {
      height: 133px;
      background: url('@/assets/emergencyplan/list_border.png');
      display: flex;
      justify-content: space-between;
      padding: 12px 16px;
      box-sizing: border-box;

      .title-box {
        display: flex;
        align-items: center;
        margin-bottom: 16px;

        .index {
          display: inline-block;
          width: 16px;
          height: 16px;
          line-height: 16px;
          border-radius: 50%;
          text-align: center;
          font-size: 12px;
          font-weight: bold;
          background: linear-gradient(219deg, #00AEFF 15%, #2474FF 86%);
          margin-right: 8px;
        }

        .title {
          font-size: 18px;
          font-weight: bold;
          background: linear-gradient(180deg, #FFFFFF 4%, #70A3FF 100%);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          background-clip: text;
        }
      }

      .info-box {
        width: 120px;
        margin-left: 29px;

        .info {
          display: flex;
          justify-content: space-between;
          line-height: normal;
          margin-bottom: 6px;

          .label {
            color: rgba($color: #fff, $alpha: 0.45);
          }
        }

        .level-one,
        .level-two,
        .level-three {
          font-weight: bold;
        }

        .level-one {
          color: #FF6633;
        }

        .level-two {
          color: #FFE433;
        }

        .level-three {
          color: #33FF77;
        }
      }

      .right-info {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        padding-bottom: 8px;

        .count {
          font-family: D-DIN;
          font-size: 40px;
          color: #00CEFF;
          margin-right: 4px;
        }

        .view-detail {
          color: #00CEFF;
          cursor: pointer;

          &:hover {
            font-weight: bold;
          }
        }
      }
    }
  }
}
</style>