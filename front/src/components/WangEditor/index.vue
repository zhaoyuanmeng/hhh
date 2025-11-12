<template>
  <div style="width: 100%;">
    <div style="width: 100%;">
      <!-- <Toolbar
        :editor="editorRef"
        :defaultConfig="toolbarConfig"
        :mode="props.mode"
        style="border-bottom: 1px solid #ccc"
        v-show="false"
      /> -->
      <Editor
        :defaultConfig="editorConfig"
        :mode="mode"
        v-model="valueHtml"
        :style="{ height: editorHeight, overflowY: 'hidden' }"
        @onCreated="handleCreated"
        @onChange="handleChange"
        @onDestroyed="handleDestroyed"
        @onFocus="handleFocus"
        @onBlur="handleBlur"
        @customPaste="customPaste"
      />
    </div>
  </div>
</template>

<script setup>
  import '@wangeditor/editor/dist/css/style.css'
  import {
    onBeforeUnmount,
    ref,
    shallowRef,
    onMounted,
    nextTick,
  } from 'vue'
  import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
  import { DomEditor } from '@wangeditor/editor'
  // 接收父组件传递的参数
  const props = defineProps({
    initialValue: {
      type: String,
      default: '',
    },
    editorHeight: {
      type: String,
      default: '200px',
    },
    toolbarConfig: {
      type: Object,
      default: () => ({}),
    },
    editorConfig: {
      type: Object,
      default: () => ({ placeholder: '请输入内容...' }),
    },
    mode: {
      type: String,
      default: 'simple',
    },
  })
 
  // 编辑器实例，必须用 shallowRef，重要！
  const editorRef = shallowRef(null)
  // 内容 HTML
  const valueHtml = ref(props.initialValue)
  const emit = defineEmits([
    'handleChange',
    'handleFocus',
    'handleDestroyed',
    'handleBlur',
    'customPaste',
  ])
  // 模拟 ajax 异步获取内容
  onMounted(() => {
    // setTimeout(() => {
    //   valueHtml.value = '<p>模拟 Ajax 异步设置内容</p>'
    // }, 1500)
  })
  // 编辑器回调函数
  // const handleCreated = (editor: typeof Editor) => {
  const handleCreated = (editor) => {
    nextTick(() => {
      editorRef.value = editor // 记录 editor 实例，重要！
      const toolbar = DomEditor.getToolbar(editor)
      const curToolbarConfig = toolbar?.getConfig()
    })
  }
 
  // 触发事件实现双向绑定
  const handleChange = (editor) => {
   emit('handleChange', editor.getHtml())
  }
  // 卸载事件
  const handleDestroyed = (editor) => {
    emit('handleDestroyed', editor)
  }
  // 失去焦点事件
  const handleFocus = (editor) => {
    emit('handleFocus', editor)
  }
  const handleBlur = (editor) => {
    emit('handleBlur', editor)
  }
  // 粘贴事件
  const customPaste = (editor,event,callback) => {
    emit('customPaste', event, editor, callback)
  }
 
  // 组件销毁时，也及时销毁编辑器，重要！
  onBeforeUnmount(() => {
    const editor = editorRef.value
    if (editor == null) return
    editor.destroy()
  })
  // 提交事件给父组件
  // const emit = defineEmits(['editor'])
  // emit('editor', editorRef.value)
  // 导出
  defineExpose({
    editorRef,
  })
</script>

<style lang="scss">
:deep(.w-e-toolbar) {
  display: none !important;
}
:deep(.toolBar_box){
  display: none;
}
</style>
<style>
.w-e-text-container p{
  margin: 8px 0 !important;
}
.w-e-text-container{
  background: rgba(0, 12, 78, 0.5) !important;
    box-shadow: none;
    border: 1px solid #5b6799;
    border-radius: 2px;
    height: 98% !important;
    color: #fff !important;
}
</style>
