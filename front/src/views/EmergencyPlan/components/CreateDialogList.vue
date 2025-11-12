<template>
  <el-dialog v-if="open" v-model="open" :destory-on-close="false" :close-on-click-modal="false" width="714">
    <template #header>
      <div class="header-box">
        <div class="title-box">
          <img src="@/assets/emergencyplan/h_icon.png" alt="">
          <span class="title">创建应急预案</span>
        </div>
      </div>
    </template>
    <div class="content">
      <el-form :model="form" label-width="auto">
        <el-form-item label="预案名称：">
          <el-input v-model="form.name" placeholder="请输入预案名称" />
        </el-form-item>
        <el-form-item label="点位类型：">
          <el-radio-group v-model="form.resource">
            <el-radio value="Sponsor">高速</el-radio>
            <el-radio value="Venue">高铁</el-radio>
            <el-radio value="Venu">城市道路</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="地点选择：">
          <el-select v-model="form.region" placeholder="请选择位置信息">
            <el-option label="地点1" value="shanghai" />
            <el-option label="地点2" value="beijing" />
          </el-select>
        </el-form-item>
        <el-form-item label="等级选择：">
          <el-select v-model="form.region" placeholder="请选择等级">
            <el-option label="一级" value="shanghai" />
            <el-option label="耳机" value="beijing" />
          </el-select>
        </el-form-item>
        <el-form-item label="责任人：">
          <el-input v-model="form.name" placeholder="请输入责任人" />
        </el-form-item>
        <el-form-item label="联系方式：">
          <el-input v-model="form.name" placeholder="请输入联系方式" />
        </el-form-item>
        <el-form-item label="预案描述：">
          <el-input v-model="form.desc" type="textarea" :rows="5" placeholder="请输入预案描述" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div class="footer-box">
        <div class="btn cancel-btn" @click="handleCancle">取消</div>
        <div class="btn save-btn" @click="handleSave">保存</div>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed } from 'vue'

// store
import useEmergencyStore from '@/store/modules/emergencyPlan'
const EmergencyStore = useEmergencyStore()
const curContent = computed(() => EmergencyStore.curContent)

const open = ref(false)
const form = ref({
  name: '',
  region: '',
  date1: '',
  date2: '',
  delivery: false,
  type: [],
  resource: 'Sponsor',
  desc: '',
})

// 取消
const handleCancle = () => {
  open.value = false
}

// 保存
const handleSave = () => {

}

const openDialog = () => {
  open.value = true
}

defineExpose({
  openDialog
})

</script>

<style lang="scss" scoped>
.header-box {
  display: flex;
  align-items: center;
  padding-bottom: 8px;
  background: url('@/assets/emergencyplan/h_line.png') no-repeat bottom;

  .title-box {
    display: flex;
    align-items: center;

    .title {
      font-family: YouSheBiaoTiHei;
      font-size: 17px;
      margin-left: 8px;
    }
  }
}

.footer-box {
  display: flex;
  justify-content: flex-end;
  padding: 0 80px;

  .btn {
    width: 88px;
    height: 36px;
    line-height: 36px;
    text-align: center;
  }

  .cancel-btn {
    background-color: rgba(31, 76, 152, 0.8677);
    margin-right: 16px;
  }

  .save-btn {
    background-color: #274EEF;
  }
}
</style>