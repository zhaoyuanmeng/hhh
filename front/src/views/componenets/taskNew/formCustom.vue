<template>
    <el-col :span="option.zwls > 1 ? 24 : 12">
      <el-form-item :prop="option.cczd" :rules="option.dialogRules ? option.dialogRules : []"
        style="width:100%">
        <template #label>
          <span>
            {{ option.zdmc }}
          </span>
        </template>
        <!-- 输入框类型 -->
        <el-input v-if="option.zdlx === 0" v-model="obj[option.cczd]" class="custom-input" :maxlength="option.maxlength"
          :disabled="option.sfzd || option.disabled" placeholder="请输入" show-word-limit/>
  
        <!-- 文本域 -->
        <!-- <el-input v-if="option.zdlx === 3" v-model="obj[option.cczd]" class="custom-textarea" :maxlength="option.maxlength"
           placeholder="请输入" :rows="option.type?8:3" type="textarea" show-word-limit
           /> -->
           <div v-if="option.zdlx === 3" style="flex: 1;">
            <WangEditor :initialValue="obj[option.cczd]" :editorHeight="option.type?'300px':'100px'" @handleChange="(e)=>{obj[option.cczd]=e}"/>
           </div>
  
        <!-- 数字输入框 -->
        <!-- <el-input v-if="option.zdlx === 4" v-model.lazy="obj[option.cczd]" class="custom-input" :maxlength="option.zdcd"
          :disabled="option.sfzd || option.disabled" placeholder="请输入" :class="option.disabled ? 'no_chose' : ''" @blur="formatInput(obj[option.cczd],option)"/> -->
  
        <!-- 日期格式的开始时间 -->
        <!-- <el-date-picker v-if="option.zdlx === '2'" v-model="obj[option.cczd]" :value-format="option.rqgs"
          :type="option.timeType" :format="option.rqgs" :placeholder="option.zdmc"
          :disabled="option.sfzd || option.disabled" :picker-options="pickerOptionsStart"
          @change="events(events, option, 'time')" :editable="false" :class="option.disabled ? 'no_chose' : ''"
          style="width:100%" /> -->
  
        <!-- 下拉列表简单的单选和多选 -->
        <el-select v-model="obj[option.cczd]" placeholder="请选择"
          v-if="option.zdlx === 1" :clearable="!option.yxdx"
          :multiple="option.yxdx" collapse-tags collapse-tags-tooltip @change="events(events, option, 'select')" style="width:100%">
          <el-option v-for="item in option.data" :key="item.value" :label="item.name" :value="item.value"
            @click="itemEvents(item, option, option.yxdx)" />
        </el-select>
  
        <!-- 下拉框之树形结构数据多选 -->
        <!-- <el-tree-select v-model="obj[option.cczd]" :data="option.data" placeholder="请选择" :clearable="!option.yxdx"
          ref="treeSelectDom" :check-strictly="option.isParentSelect"
          :props="{ label: 'name', children: 'children', value: 'id' }" :disabled="option.sfzd || option.disabled"
          collapse-tags collapse-tags-tooltip :render-after-expand="false" :multiple="option.yxdx"
          :show-checkbox="option.yxdx" v-if="option.zdlx === '1' && option.ifZtree && option.xsms !== '1'" node-key="id"
          :class="option.disabled ? 'no_chose' : ''" style="width:100%" @check="handleNodeClick"
          @change="events(events, option, 'treeSelect')" /> -->
  
  
        <!-- 弹框选择数据 -->
        <!-- <el-input v-model="obj[option.cczdName]" placeholder="搜索带出" readonly :title="obj[option.cczdName]"
          v-if="option.zdlx === '1' && option.xsms === '1'" @focus="openSelectPeople(option)"
          :disabled="option.sfzd || option.disabled">
          <template #append v-if="!option.disabled">
            <el-button :icon="Search" @click="openSelectPeople(option)" />
          </template>
        </el-input> -->
      </el-form-item>
    </el-col>
    <!-- <el-dialog v-model="centerDialogVisible" title="" width="400" center class="selectDialog" v-if="centerDialogVisible">
      <template #header>
        <div class="my-header" style="text-align:left !important">
          <div class="class_title">{{ '选择' + dialogTitle }}</div>
        </div>
      </template>
      <div style="margin-bottom:10px">
        <el-input v-model="filterText" style="width: 200px" placeholder="关键字搜索" clearable>
          <template #append>
            <el-button :icon="Search" @click="searchText(filterText, option.data)" />
          </template>
        </el-input>
        <span style="margin-left:10px" v-if="showBtn">{{ lengthIndex }}/{{ countNum }}</span>
        <el-button type="primary" size="small" :icon="Bottom" style="margin-left:10px" v-if="showBtn"
          @click="changeHighlight('down', option.data)" />
        <el-button type="primary" size="small" v-if="showBtn" @click="changeHighlight('up', option.data)">
          <el-icon>
            <Top />
          </el-icon>
        </el-button>
      </div>
      <div class="data_content">
        <el-tree ref="treeRef" style="max-width: 400px" class="filter-tree" :data="option.data"
          :filter-node-method="filterNode1" :check-strictly="option.isParentSelect"
          :default-expanded-keys="showSelect.split(',')" :default-checked-keys="showSelect.split(',')"
          :props="{ label: 'name', children: 'children', value: 'id' }" :render-after-expand="false" :multiple="option.yxdx"
          :show-checkbox="option.yxdx" node-key="id" @check="handleNodeClick1" v-if="option.yxdx">
          <template #default="{ node, data }">
            <span class="custom-tree-node">
              <span :class="node.text" :style="{ 'color': node.light ? 'red' : '' }" :id="data.id">{{ node.label }}</span>
            </span>
          </template>
        </el-tree>
        <el-tree ref="treeRef" style="max-width: 400px" class="filter-tree" :data="option.data"
          :check-strictly="option.isParentSelect" highlight-current :filter-node-method="filterNode1"
          :current-node-key="showSelect" :default-expanded-keys="showSelect.split(',')"
          :props="{ label: 'name', children: 'children', value: 'id' }" :render-after-expand="false" :multiple="false"
          :show-checkbox="false" node-key="id" @node-click="events($event, option, 'tree')" v-else>
          <template #default="{ node, data }">
            <span class="custom-tree-node">
              <span :class="node.text" :style="{ 'color': node.light ? 'red' : '' }" :id="data.id">{{ node.label }}</span>
            </span>
          </template>
        </el-tree>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="centerDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="clearDataSure(dialogSelectItem)" v-if="option.yxdx">确定</el-button>
          <el-button type="primary" @click="clearData(option)">
            清空
          </el-button>
        </div>
      </template>
    </el-dialog> -->
  </template>
  <script setup>
  import dayjs from "dayjs";
  import { computed, reactive, ref,watch,getCurrentInstance,defineProps  } from "vue";
  import { Bottom, Search } from '@element-plus/icons-vue'
  import WangEditor from '@/components/WangEditor';
  const emit = defineEmits(["changeCotrl", "changeDialogCtrl"]);
  const listData = ref([])
  const props = defineProps({
    // 必传参数,绑定form的变量
    obj: {
      type: Object,
      required: true
    },
    option: {
      type: Object,
      defalut: function () {
        return {}
      }
    }
  });
  watch(() => props.option, val => {
    if (val) {
      // listData.value.push(val)
    }
  }, { immediate: true, deep: true });
  const pickerOptionsStart = ref({})
  const pickerOptionsEnd = ref({})
  const centerDialogVisible = ref(false)
  const filterText = ref('')
  const showSelect = ref('')
  const dialogTitle = ref('')
  const dialogSelectItem = ref({})
  const countNum = ref(0)
  const lengthIndex = ref(0)
  const nodeList = ref([])
  const nodeIDs = ref([])
  const showBtn = ref(false)
  const { proxy } = getCurrentInstance();
  // watch(filterText, (val) => {
  //   proxy.$refs.treeRef.filter(val)
  // })
  const formatInput = (event,item) => {
    if(item.xsws&&item.xsws!=='0'){
        // 仅允许输入数字和小数点
    props.obj[item.cczd] = event.replace(/[^\d.]/g, '')
          // 保证只有一个小数点
          .replace(/^\./g, '')
          .replace(/\.{2,}/g, '.')
          // 当输入为正数且没有小数点时，自动补.0
          .replace(/^(\d+)$/, '$1.0');
    }
  }
  const handleIdCardInput = (idCard) => {
    if (props.option.zdbs === 'SFZJH') {
      // 自动产生出生日期
      if (idCard.length >= 14) {
        const bornStr = idCard.substr(6, 4) + '-' + idCard.substr(10, 2) + '-' + idCard.substr(12, 2)
        console.log(bornStr)
        props.obj['CSRQ'] = bornStr
      }else{
        props.obj['CSRQ'] = ''
      }
  }
  }
  const handleIdCardChange = (idCard) => {
    if (props.option.zdbs === 'SFZJH') {
      var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
      if (reg.test(idCard) === false) {
        proxy.$modal.msgError('身份证号输入不合法')
        props.obj[props.option.cczd] = ''
      }
    }
  }
  const searchText = (val, data) => {
    proxy.$refs.treeRef.filter(val)
    const datas = flattenArray(data)
    if (!val) {
      showBtn.value = false
      const nodeData = []
      datas.forEach(item => {
        let node = proxy.$refs.treeRef.getNode(item.id)
        nodeData.push(node)
      })
      nodeData.map((item, index) => {
        nodeData[index].light = false
      })
      return
    }
  
    let nodes = []
    datas.forEach(item => {
      if (item.name.indexOf(val) !== -1) {
        nodes.push(item)
      }
    })
    nodeIDs.value = nodes
    countNum.value = nodes.length
    lengthIndex.value = 1
    let nodeData = []
    if (nodes && nodes.length > 0) {
      nodes.forEach(item => {
        let node = proxy.$refs.treeRef.getNode(item.id)
        nodeData.push(node)
      })
    }
    nodeList.value = nodeData
    nodeData.map((item, index) => {
      nodeData[0].light = true
    })
    if (nodes && nodes.length > 0) {
      if (proxy.$refs.treeRef) {
        let node = document.getElementById(nodes[0].id)
        proxy.$nextTick(() => {
          node.scrollIntoView()
        })
      }
    }
  
  }
  const events = (option, item, type) => {
    //时间选择器
    if (type === 'time') {
      changeStartEnd(props.obj, item)
    }
    // 下拉框
    if (type === 'select') {
      emit('changeCotrl', item)
    }
    // 弹框
    if (type === 'tree') {
      // 父节点可以选择
      if (item.isParentSelect) {
          centerDialogVisible.value = false
          item.dialogName = option.name
          item.dialogData = option.id
          emit('changeDialogCtrl', item)
      } else {
        if (option.children) {
  
        } else {
          // if()
          centerDialogVisible.value = false
          item.dialogName = option.name
          item.dialogData = option.id
          emit('changeDialogCtrl', item)
        }
      }
  
    }
    // 树形下拉框
    if (type === 'treeSelect') {
      emit('changeCotrl', item)
    }
  }
  const clearData = (item) => {
    showSelect.value = ''
    centerDialogVisible.value = false
    item.dialogName = ''
    item.dialogData = ''
    emit('changeDialogCtrl', item)
  }
  const clearDataSure = (item) => {
    centerDialogVisible.value = false
    emit('changeDialogCtrl', item)
  }
  const openSelectPeople = (item) => {
    filterText.value = ''
    showBtn.value = false
    dialogSelectItem.value = item
    dialogTitle.value = item.zdmc
    centerDialogVisible.value = true
    if (props.obj[item.cczdName] && props.obj[item.cczd]) {
      dialogSelectItem.value.dialogData = toStringIfArray(props.obj[item.cczd])
      dialogSelectItem.value.dialogName = props.obj[item.cczdName]
      showSelect.value = toStringIfArray(props.obj[item.cczd])
    }
  }
  const toStringIfArray = (value) => {
    if (Array.isArray(value)) {
      return value.toString();
    }
    return value;
  }
  const filterNode = (value, data) => {
    if (!value) return true
    return data.name.includes(value)
  }
  const filterNode1 = (value, data, node) => {
    node.text = "";
    data.text = ""
    if (!value) return true;
    if (data.name.indexOf(value) !== -1) {
      const nodes = proxy.$refs.treeRef.getNode(data)
      node.text = "red"
      data.text = "red"
      showBtn.value = true
    }
    return data
  }
  const changeHighlight = (type, data) => {
    const datas = flattenArray(data)
    var sumFindNodes = nodeList.value
    var nodes = nodeIDs.value
    var index = lengthIndex.value
    var count = countNum.value
    if (count == 0) {
      return;
    }
    var _index = 0;
    if (type == "up") {
      if (index == 1) {
        _index = count;
      } else {
        _index = index - 1;
      }
      lengthIndex.value = _index
  
    } else {
      if (index == count) {
        _index = 1;
      } else {
        _index = index + 1;
      }
      lengthIndex.value = _index
    }
    if (proxy.$refs.treeRef) {
      sumFindNodes.map((item, index) => {
        if (index === (_index - 1)) {
          item.light = true
        } else {
          item.light = false
        }
      })
      let node = document.getElementById(nodes[_index - 1].id)
      proxy.$nextTick(() => {
        node.scrollIntoView()
      })
    }
  }
  function flattenArray(arr) {
    return arr.reduce((result, obj) => {
      const { children, ...otherProps } = obj;
      result.push(otherProps);
  
      if (children && Array.isArray(children)) {
        result.push(...flattenArray(children));
      }
  
      return result;
    }, []);
  }
  const itemEvents = (item, option, type) => {
    if (type) {
      if (props.obj[option.cczd] && props.obj[option.cczd].length > 0) {
        if (item.value === option.qfz) {
          props.obj[option.cczd] = [item.value]
          emit('changeCotrl', option)
        } else {
          let data = props.obj[option.cczd]
          for (let i = data.length - 1; i >= 0; i--) {
            if (data[i] === option.qfz) {
              data.splice(i, 1)
            }
          }
          props.obj[option.cczd] = data
          emit('changeCotrl', option)
        }
      }
    }
  }
  
  const handleNodeClick = (data, obj) => {
    if (obj.checkedKeys && obj.checkedKeys.length > 0) {
      if (data.value === 'CFLB@GJ@0') {
        props.obj['CFLB'] = [data.value]
        proxy.$refs.treeSelectDom.setCheckedKeys([data.value])
      } else {
        let datas = obj.checkedKeys
        for (let i = datas.length - 1; i >= 0; i--) {
          if (datas[i] === 'CFLB@GJ@0') {
            datas.splice(i, 1)
          }
        }
        props.obj['CFLB'] = datas
        proxy.$refs.treeSelectDom.setCheckedKeys(datas)
      }
    }
  }
  const handleNodeClick1 = (data, obj) => {
    console.log(data, obj,props.option.qfz)
    if (obj.checkedKeys && obj.checkedKeys.length > 0) {
      if (data.value === props.option.qfz) {
        proxy.$refs.treeRef.setCheckedKeys([data.value])
        dialogSelectItem.value.dialogName = data.name
        dialogSelectItem.value.dialogData = data.value
      } else {
        let datas = obj.checkedKeys
        let names = obj.checkedNodes.map(item => item.name)
        for (let i = datas.length - 1; i >= 0; i--) {
          if (datas[i] === props.option.qfz) {
            datas.splice(i, 1)
          }
        }
        for (let i = names.length - 1; i >= 0; i--) {
          if (names[i] === '0-无') {
            names.splice(i, 1)
          }
        }
        console.log(datas, names)
        dialogSelectItem.value.dialogName = names.join()
        dialogSelectItem.value.dialogData = datas.join()
        proxy.$refs.treeRef.setCheckedKeys(datas)
      }
    }else{
      showSelect.value = ''
      dialogSelectItem.value.dialogName = ''
      dialogSelectItem.value.dialogData = ''
    }
  
  }
  const changeStartEnd = (obj, item) => {
    console.log(obj, item)
    // 有后置时间字段和后置时间符号 后置时间字段有可能是数组
    if (item.hzsjzd && item.hzsjfh) {
      let biaoshi = item.hzsjfh
      let hzsjzdList = item.hzsjzd.split(',')
      console.log(biaoshi, hzsjzdList)
      try {
        let datas = hzsjzdList
        datas.forEach(function (time, index) {
          if (obj[time]) {
            if (biaoshi === '<') {
              if (obj[item.cczd] < obj[time]) {
              } else {
                proxy.$modal.msgError(`${item.zdmc}必须要小于${(obj[time])}`);
                throw new Error("ending");//报错，就跳出循环
              }
            }
            if (biaoshi === '<=') {
              if (obj[item.cczd] <= obj[time]) {
              } else {
                proxy.$modal.msgError(`${item.zdmc}必须要小于等于${(obj[time])}`);
                throw new Error("ending");//报错，就跳出循环
              }
            }
            if (biaoshi === '>') {
              if (obj[item.cczd] > obj[time]) {
  
              } else {
                proxy.$modal.msgError(`${item.zdmc}必须大于${(obj[time])}`);
                throw new Error("ending");//报错，就跳出循环
              }
            }
            if (biaoshi === '>=') {
              if (obj[item.cczd] >= obj[time]) {
  
              } else {
                proxy.$modal.msgError(`${item.zdmc}必须大于等于${(obj[time])}`);
                throw new Error("ending");//报错，就跳出循环
              }
            }
          }
        })
      } catch (e) {
        if (e.message == "ending") {
          obj[item.cczd] = '';
        }
      }
    }
  
    if (item.qzsjzd && item.qzsjfh) {
      let biaoshi = item.qzsjfh
      let qzsjzdList = item.qzsjzd.split(',')
      console.log(biaoshi, qzsjzdList)
      try {
        let datas = qzsjzdList
        datas.forEach(function (time, index) {
          if (obj[time]) {
            if (biaoshi === '<') {
              if (obj[item.cczd] < obj[time]) {
              } else {
                proxy.$modal.msgError(`${item.zdmc}必须要小于${(obj[time])}`);
                throw new Error("ending");//报错，就跳出循环
              }
            }
            if (biaoshi === '<=') {
              if (obj[item.cczd] <= obj[time]) {
              } else {
                proxy.$modal.msgError(`${item.zdmc}必须要小于等于${(obj[time])}`);
                throw new Error("ending");//报错，就跳出循环
              }
            }
            if (biaoshi === '>') {
              if (obj[item.cczd] > obj[time]) {
  
              } else {
                proxy.$modal.msgError(`${item.zdmc}必须大于${(obj[time])}`);
                throw new Error("ending");//报错，就跳出循环
              }
            }
            if (biaoshi === '>=') {
              if (obj[item.cczd] >= obj[time]) {
  
              } else {
                proxy.$modal.msgError(`${item.zdmc}必须大于等于${(obj[time])}`);
                throw new Error("ending");//报错，就跳出循环
              }
            }
          }
        })
      } catch (e) {
        if (e.message == "ending") {
          obj[item.cczd] = '';
        }
      }
    }
  }
  </script>
  
  <style scoped lang="scss"></style>
  <style lang="scss">
  .selectDialog .el-dialog__body {
    padding-top: 10px !important;
  }
  .el-form-item__label{
      color:#fff !important;
    }
  .selectDialog .el-dialog__body .data_content {
    height: 300px;
    overflow: auto;
  }
  
  .highlighted {
    background-color: yellow !important;
    /* 你可以自定义高亮颜色 */
  }
  
  .custom-tree-node .red {
    background-color: #fbec88 !important;
    /* 你可以自定义高亮颜色 */
  }
  .el-form-item__content {
      .el-input {
        border: 1px solid #5b6799;
        border-radius: 2px;
        .el-input__wrapper {
          background: rgba(0, 12, 78, 0.5);
          box-shadow: none;
        }
        .el-input__count-inner{
            background: rgba(0, 12, 78, 0.5);
          box-shadow: none;
        }
        .el-input__inner {
          font-size: 14px;
          color: #ffffff;
          opacity: 0.8;
        }
      }
      .el-textarea__inner{
        background: rgba(0, 12, 78, 0.5);
          box-shadow: none;
          border: 1px solid #5b6799;
        border-radius: 2px;
        color:#fff;
      }
      .el-input__count{
        background: rgba(0, 12, 78, 0.5);
          box-shadow: none;
      }
    }
  </style>