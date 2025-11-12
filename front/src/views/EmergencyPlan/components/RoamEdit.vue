<template>
  <div class="roam-edit">
    <div class="bread-crumb">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item @click="changeContent('planList')">预案库</el-breadcrumb-item>
        <el-breadcrumb-item @click="changeContent('planDetail')">预案详情</el-breadcrumb-item>
        <el-breadcrumb-item>漫游编辑</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="title-box">
      <span class="title">医疗应急预案</span>
      <span>(津雄高速应急预案)</span>
    </div>
    <!-- 列表 -->
    <div class="list-box">

    </div>
    <!-- 底部 -->
    <div class="footer-box">
      <div class="roam-time">
        <span>漫游时长设定：</span>
        <el-input-number v-model="roamTime" :min="0" :max="60">
          <template #decrease-icon>
            <el-icon color="#fff">
              <ArrowLeft />
            </el-icon>
          </template>
          <template #increase-icon>
            <el-icon color="#fff">
              <ArrowRight />
            </el-icon>
          </template>
        </el-input-number>
        <el-button type="primary" class="roam-view">漫游预览</el-button>
      </div>
      <div class="footer-btn">
        <div class="btn cancel-btn">取消</div>
        <div class="btn save-btn">保存</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ArrowLeft, ArrowRight } from '@element-plus/icons-vue'
// store
import useEmergencyStore from '@/store/modules/emergencyPlan'
const EmergencyStore = useEmergencyStore()

const roamTime = ref(3)
const changeContent = (content) => {
  EmergencyStore.setCurContent(content)
}
</script>

<style lang="scss" scoped>
.roam-edit {
  height: 100%;
  position: relative;

  .bread-crumb {
    padding-bottom: 12px;
    border-bottom: 1px solid rgba(38, 68, 173, 0.5);
  }

  .title-box {
    margin: 8px 0 12px;

    .title {
      font-size: 18px;
      font-weight: bold;
      background: linear-gradient(180deg, #FFFFFF 4%, #70A3FF 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      margin-right: 8px;
    }
  }

  .list-box {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
  }

  .footer-box {
    position: absolute;
    bottom: 0;
    width: 100%;
    padding-top: 16px;
    border-top: 1px solid rgba(38, 68, 173, 0.5);

    .roam-time {
      display: flex;
      align-items: center;
      justify-content: space-between;

      .roam-view {
        width: 72px;
        height: 32px;
        border-radius: 0;
        margin-left: 24px;
      }
    }

    .footer-btn {
      display: flex;
      gap: 8px;
      margin-top: 16px;

      .btn {
        flex: 1;
        height: 36px;
        line-height: 36px;
        text-align: center;
        background: rgba(31, 76, 152, 0.8677);
        cursor: pointer;
      }

      .save-btn {
        background: #274EEF;
      }
    }
  }
}

:deep(.el-input-number--small .el-input--small .el-input__wrapper) {
  height: 32px;
}

:deep(.el-input-number__decrease),
:deep(.el-input-number__increase) {
  background: #274EEF;
}

:deep(.el-input-number__decrease) {
  border-right: 0;
}

:deep(.el-input-number__increase) {
  border-left: 0;
}

:deep(.el-input-number--small .el-input-number__decrease),
:deep(.el-input-number--small .el-input-number__increase) {
  width: 18px;
}
</style>