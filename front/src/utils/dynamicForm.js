import $ from 'jquery';
const UPLOAD_BASE_URL = '/imgPath'
import { ElMessageBox,ElMessage } from 'element-plus'
import { nextTick,h } from "vue";
export default class FormCreateUtil {
  constructor(form) {
    this.form = form;
  }
  formatRules(data = []) {
    return data.map(item => {
      let formItem = this.commonField(item);
      console.log(formItem)
      switch (formItem.type) {
        case 'input':
          Object.assign(formItem, this.elInput());
          break;
          // case 'select':
          // Object.assign(formItem, this.elSelect(formItem));
          // break;
        case 'textarea':
          Object.assign(formItem, this.elInputTextArea());
          break;
        case 'group':
          Object.assign(formItem, this.elComponentList(formItem));
          break;
        case 'InputNumber':
          Object.assign(formItem, this.elInputNumber());
          break;
        case 'photo':
          Object.assign(formItem, this.elUpload());
          break;
        case 'DatePicker':
          Object.assign(formItem, this.elDatePicker());
          break;
        case 'video':
          Object.assign(formItem, this.elUploadVideo());
          break;
        // case 'point':
        //   Object.assign(formItem, this.pointMark());
        //   break;
        case 'hidden':
          break;
        default:
          Object.assign(formItem, ...this.defaultItem());
      }
      return formItem;
    });
  }
  static handleRule(column) {
    return column.map((item) => {
      return {
        type: 'input',
        field: 'test1',
        props: {
          type: 'text'
        },
        options: [],
        col: {
          span: 24
        },
        validate: [
          {
            required: true,
            message: `${item.title && item.title.replace(/[：:]/g, '')}为必填`,
            trigger: 'blur'
          }
        ],
        ...item
      };
    });
  }
  // 提取表单公共格式
  commonField(formItem) {
    let {
      default_value: value,
      field_id: field,
      field_name: title,
      field_type: type,
      mandatory: required,
      listFild: child,
      remark
    } = formItem, validate;
    switch (required) {
      case '0':
        validate = [{
          required: true,
          message: title + '为必填'
        }];
        break;
      case '1':
        if(type==='video'){
          validate = [];
        }else{
          validate = [];
        }
        break;
      case '2':
        type = 'hidden';
        break;
    }
    return {
      type,
      title,
      field,
      required,
      value,
      validate,
      remark,
      child
    };
  }
  // 重置表单
  resetForm() {
    this.form.resetFields();
    nextTick(() => {
      this.form.clearValidate();
    });
  }
  // 输入框
  elInput() {
    return {
      props: {
        type: 'text'
      }
    };
  }
  // 下拉框
  elSelect(item){
    const jsonStr = item.remark
    // 移除所有反斜杠并解析为数组
const cleanedStr = jsonStr.replace(/\\/g, '');
const array = JSON.parse(cleanedStr);
    console.log(array)
    const list = array.map(item => ({
      label: item.dictLabel,
      value: item.dictValue
    }));
    return {
      type: "select",
      value:'',
      options: list,
      props: {
          multiple: false
      },
  }
  }
  // 文本域
  elInputTextArea() {
    return {
      type: 'input',
      props: {
        type: 'textarea'
      }
    };
  }
  // 输入框数字
  elInputNumber() {
    return {
      props: {
        type: 'InputNumber'
      }
    };
  }
  // 二级列表
  elComponentList(rulesOption) {
    let rules = rulesOption.child.map(item => {
      return {
        type: item.field_type,
        field: item.field_id,
        title: item.field_name
      };
    });
    return {
      props: {
        type: 'group',
        rules: rules
      }
    };
  }
  // 日期选择器
  elDatePicker() {
    return {
      props: {
        type: 'datetime',
        format: 'YYYY/MM/DD HH:mm:ss',
        placeholder: '请选择日期'
      }
    };
  }
  // 图片上传
  elUpload() {
    return {
      type: 'upload',
      value: [],
      props: {
        headers: {'x-auth-token': localStorage.getItem('token')},
        type: 'select',
        uploadType: 'image',
        action: import.meta.env.VITE_APP_BASE_API + '/specialServiceArchives/uploadArchivesIcon',
        name: 'file',
        multiple: true,
        accept: 'image/*',
        limit: 5,
        onSuccess: (res, file) => {
          if(res.code===0){
            console.log(this.form)
            let baseUrl = UPLOAD_BASE_URL; 
            file.url = baseUrl + res.data;
          }
        }
      }
    };
  }
  // 视频上传
  elUploadVideo() {
    return {
      type: 'upload',
      value: [],
      props: {
        headers: {'x-auth-token': localStorage.getItem('token')},
        multiple: false,
        uploadType: 'video',
        listType:'picture-card',
        limit: 1,
        maxSize: 512000, // 限制为 500MB
        action: import.meta.env.VITE_APP_BASE_API + '/specialServiceArchives/uploadArchivesIcon',
        name: 'file',
        accept: 'video/*',
        beforeUpload:(file)=>{
          console.log(file)
          const maxSize = 500 * 1024 * 1024; // 500MB
          if (file.size > maxSize) {
            ElMessage.error("文件大小不能超过500MB");
            return false; // 阻止上传 ‌:ml-citation{ref="1,6" data="citationList"}
          }
          return true;
        },
        onSuccess: (response, file) => {
        const url = UPLOAD_BASE_URL + response.data;
        file.url = url
        this.value = [url]
        nextTick(()=>{
          // 使用 jQuery 替换 img 标签为 video 标签
          let videoHtml = $('._fc-upload ._fc-exceed').find('.el-upload-list__item-thumbnail')
          console.log(videoHtml)
        $(videoHtml).each((index, img) => {
          const video = $('<video controls></video>')
            .attr('src', url)
            .attr('alt', img.alt)
            .css({
              width: '100%',
              height: '100%'
            });
          $(img).replaceWith(video);
        });
        })
        },
        onPreview: (file) => {
          var videoSrc = $('._fc-upload ._fc-exceed .el-upload-list').find('video').attr('src');
          ElMessageBox({
            title: '视频预览',
            showConfirmButton:false,
            message:  h('video', {
              class: 'custom_Video',
              src: videoSrc,
              controls: true,
              alt: '视频预览',
              style: {
                width: '100%',
                height: '100%'
              }
            })
          })
        }

      },
    // 自定义上传列表的渲染
    slots: {
      // 自定义上传列表中的文件渲染
      file: ({ file }) => {
        setTimeout(()=>{
        // 使用 video 标签替换默认的 img 标签
        return  h('video', {
          class: 'custom_Video',
          src: file.url,
          controls: true,
          alt: '视频预览',
          style: {
            width: '100%',
            height: '100%'
          }
        })
        },2000)
  
      }
    }
    };
  }
  defaultItem() {
    return [{
      type: 'hidden',
      field: 'geoJson',
      value: {}
    }, {
      type: 'hidden',
      field: 'geoType',
      value: {}
    }];
  }
  pointMark() {
    const objPoint = {
      // '线': 'line',
      '圆': 'round',
      // '矩形': 'rectangle',
      '多边形': 'area'
    };
    let buttonItem = Object.keys(objPoint).map((item) => {
      return {
        type: 'el-button',
        // props: {
        //   type: 'primary'
        // },
        children: [item],
        on: {
          click: (e) => {
            setTimeout(() => {
              // new Vue().$eventBus.$emit('eventMapDrawDialog', {
              //   type: objPoint[item]
              // });
            });
          }
        }
      };
    });
    return {
      type: 'el-button-group',
      children: buttonItem
    };
  }
}
