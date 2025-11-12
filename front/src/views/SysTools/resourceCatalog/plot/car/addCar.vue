<!--
 * @FileDescription: 三维标注->新增标签
 * @Author: yuanhaijun
 * @Date: 2022.11.28
 * @LastEditors: Alex
 * @LastEditTime: 2024-06-20 16:04:22
 -->
<template>
  <div>
    <div v-if="isShowAdd" class="cloud-func add">
      <div class="func-title">
        模型编辑
        <el-icon class="" @click.stop="handleClose" style="cursor: pointer">
          <CircleClose />
        </el-icon>
      </div>

      <div class="func-warp">
        <div class="warp-left">
          <div class="panel-warp">
            <!-- 右上角小按钮 -->
            <div class="panel-icon" v-show="isShowDel">
              <div @click="handleDelLabel">
                <svg-icon icon-class="cloud-del" class-name="icon"></svg-icon>
              </div>
            </div>
            <!-- 默认显示 -->
            <div class="panel-add" v-show="isShowDef">
              <div class="panel-add-list" @click="handleChangeLabel">
                <svg-icon
                  icon-class="cloud-folder"
                  class-name="icon"
                ></svg-icon>
                <p>默认警车</p>
              </div>
              <!-- <div class="panel-add-list" @click="handleAddImage">
                              <svg-icon icon-class="cloud-add" class-name="icon"></svg-icon>
                              <p>上传标签</p>
                          </div> -->
            </div>
            <div id="canvasDiv" class="panel-canvas" v-show="isShowCanvas">
              <canvas id="preview"></canvas>
            </div>
            <!-- 标签选择 -->
            <div class="panel-material" v-show="isShowMaterial">
              <div class="panel-material-warp">
                <el-scrollbar style="height: 100%">
                  <div
                    class="material-list"
                    @click="handleLabel(item)"
                    v-for="(item, index) in labelList"
                    :key="index"
                  >
                    <ul>
                      <img :src="item.src" alt="" srcset="" />
                    </ul>
                  </div>
                </el-scrollbar>
              </div>
            </div>
          </div>
          <div
            class="panel-footer"
            @click="handelCoordinate"
            v-if="isShowCoordinate"
          >
            <span>在场景中拾取坐标</span>
            <svg-icon
              icon-class="cloud-coordinate"
              class-name="icon"
            ></svg-icon>
          </div>
          <div v-else>
            <div class="list-title">
              模型坐标
              <div @click="rePick">
                <span>重新拾取</span>
                <svg-icon
                  icon-class="cloud-coordinate"
                  class-name="icon"
                ></svg-icon>
              </div>
            </div>
            <div class="list-warp">
              <ul>
                <el-input-number
                  v-model="option.translation[0]"
                  controls-position="right"
                  @change="changeTranslation"
                ></el-input-number>
                <span>X</span>
              </ul>
              <ul>
                <el-input-number
                  v-model="option.translation[1]"
                  controls-position="right"
                  @change="changeTranslation"
                ></el-input-number>
                <span>X</span>
              </ul>
              <ul>
                <el-input-number
                  v-model="option.translation[2]"
                  controls-position="right"
                  @change="changeTranslation"
                ></el-input-number>
                <span>Z</span>
              </ul>
              <ul>
                <el-input-number
                  v-model="centerX"
                  controls-position="right"
                ></el-input-number>
                <span>偏移</span>
              </ul>
            </div>
          </div>
        </div>
        <div class="warp-right">
          <div class="label">模型内容</div>
          <el-input
            ref="textarea"
            type="textarea"
            :rows="4"
            resize="none"
            placeholder="请输入标签内容"
            v-model="labelContent"
            @change="handleContent"
          >
          </el-input>
          <div class="label">字符</div>
          <el-select
            v-model="value"
            @change="handleChangeFont"
            placeholder="请选择"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
          <div class="label">
            <div class="font">
              <svg-icon
                icon-class="cloud-font-size"
                class-name="icon"
              ></svg-icon>
              <el-input v-model="fontsize" @change="handleChangeFont">
              </el-input>
            </div>
            <el-color-picker
              @change="handleColorChange"
              v-model="color1"
            ></el-color-picker>
          </div>
          <div class="label">模型比例</div>
          <div class="proportion">
            <el-slider
              :min="10"
              :max="100"
              :step="10"
              v-model="valueNmber"
              :show-input="false"
              :show-input-controls="false"
              @change="changeZoom"
            ></el-slider>
            <el-input-number
              v-model="valueNmber"
              controls-position="right"
              :max="100"
              :min="10"
              :step="10"
              @change="changeZoom"
            ></el-input-number>
          </div>
        </div>
      </div>

      <div class="warp-footer">
        <div @click="handleClose">取消</div>
        <div
          :class="['primary', { disable: !submitShow }]"
          @click="handleSubmit"
        >
          确定
        </div>
      </div>
    </div>

    <!-- 上传标签 -->
    <!-- <upload-tmpl v-if="isShowUpload" /> -->
  </div>
</template>

<script>
import { loadPicture } from "@/utils";
import { mapState } from "pinia";
import { useSysToolsCimStore } from "@/store/modules/sysToolsCim";
import { CircleClose } from "@element-plus/icons-vue";
import { sessionStorage } from "@/utils/storage";
var isDraw = false;
var movePos;
function getPos(x, y) {
  var canvas = document.getElementById("preview");
  var box = canvas.getBoundingClientRect();
  return { x: x - box.left, y: y - box.top };
}
export default {
  name: "AddMark",
  data() {
    return {
      isShowAdd: true,
      isShowUpload: false,
      option: {
        translation: [0, 0, 0],
      },
      labelContent: "",
      options: [
        {
          value: "微软雅黑",
          label: "微软雅黑",
        },
        {
          value: "fangsong",
          label: "仿宋",
        },
      ],
      value: "微软雅黑",
      fontsize: 14,
      color1: "#000000",
      isShowDel: false, //删除按钮
      isShowDef: true, //默认显示
      isShowCanvas: false, //canvas绘制
      isShowMaterial: false, //标签列表
      labelList: [
        {
          src: loadPicture("./images/cloud/car/icon-1.png"),
        },
        {
          src: loadPicture("./images/cloud/car/icon-2.png"),
        },
        {
          src: loadPicture("./images/cloud/car/icon-3.png"),
        },
        {
          src: loadPicture("./images/cloud/car/icon-4.png"),
        },
        {
          src: loadPicture("./images/cloud/car/icon-5.png"),
        },
        {
          src: loadPicture("./images/cloud/car/icon-6.png"),
        },
      ],
      image: undefined,
      imageWidth: 0,
      imageHeight: 0,
      pDocument: null,
      pLeft: 0,
      pTop: 0,
      isShowCoordinate: true,
      isPick: true,
      valueNmber: 100,
      offsetX: 0,
      centerX: 0,
      submitShow: false,
      imageLoaded: false,
      coordinateCollected: false,
      tLeft: 0,
      tTop: 0,
      sourceSrc: "",
      isEdit: false,
      editItem: undefined,
      markerImg: {
        src: loadPicture("./images/dialog/弹窗@2x.png"),
      },
    };
  },
  components: { CircleClose },
  computed: {
    ...mapState(useSysToolsCimStore, ["eventSealAPI"]),
  },
  watch: {
    eventSealAPI: {
      indeterminate: false,
      deep: true,
      handler(val) {
        if (
          (val.Type === "TileLayer" || val.Type === "CustomObj") &&
          val.eventtype === "LeftMouseButtonClick"
        ) {
          this.getMouseClickPoint(val);
        }
      },
    },
  },
  methods: {
    changeTranslation(e) {
      console.log(e);
    },
    handleAddImage() {
      this.isShowUpload = true;
      this.isShowAdd = false;
    },
    handelCoordinate() {
      this.isShowCoordinate = false;
      this.isPick = true;
    },
    handleColorChange() {
      if (this.pDocument !== null) {
        this.pDocument.style.color = this.color1;
      }
    },
    handleChangeFont(e) {
      if (this.pDocument !== null) {
        this.pDocument.style.font = `normal ${this.fontsize}px ${this.value}`;
      }
    },
    // 删除标签
    handleDelLabel() {
      this.isShowDel = false;
      this.isShowCanvas = false;
      this.isShowDef = true;
    },
    handleContent() {
      if (this.pDocument === null) {
        this.pDocument = document.createElement("p");
        var canvas = document.getElementById("preview");
        this.pLeft = canvas.style.left;
        this.pTop = canvas.style.top;
        this.pDocument.style.left = this.pLeft;
        this.pDocument.style.top = this.pTop;
        this.pDocument.style.position = "absolute";
        this.pDocument.style.pointerEvents = "none";
        this.pDocument.style.verticalAlign = "top";
        var div = document.getElementById("canvasDiv");
        div.appendChild(this.pDocument);
      }

      this.pDocument.innerText = this.labelContent;
      this.pDocument.style.font = `normal ${this.fontsize}px ${this.value}`;
      this.submitShow =
        this.imageLoaded ||
        this.labelContent.length > 0 ||
        this.coordinateCollected;
    },
    drawPreViewImage(src) {
      var image = new Image();
      image.setAttribute("crossOrigin", "Anonymous");
      image.src = src;
      this.sourceSrc = src;
      this.image = image;
      var that = this;
      image.onload = function () {
        that.imageLoaded = true;
        that.submitShow =
          that.imageLoaded ||
          that.labelContent.length > 0 ||
          that.coordinateCollected;
        var canvas = document.getElementById("preview");
        var context = canvas.getContext("2d");
        context.imageSmoothingEnabled = true;
        that.imageWidth = image.width;
        that.imageHeight = image.height;
        canvas.width = that.imageWidth;
        canvas.height = that.imageHeight;
        canvas.style.backgroundColor = "transparent";
        canvas.style.width = that.imageWidth + "px";
        canvas.style.height = that.imageHeight + "px";
        context.clearRect(0, 0, that.imageWidth, that.imageHeight);
        context.drawImage(image, 0, 0, that.imageWidth, that.imageHeight);

        //鼠标点下
        canvas.onmousedown = function (e) {
          isDraw = true;
          movePos = getPos(e.clientX, e.clientY);
        };
        //鼠标移动
        canvas.onmousemove = function (e) {
          movePos = getPos(e.clientX, e.clientY);
          if (isDraw && that.pDocument) {
            that.pDocument.style.top = movePos.y + "px";
            that.pDocument.style.left = movePos.x + "px";
          }
        };
        //鼠标松开
        canvas.onmouseup = function (e) {
          isDraw = false;
        };
        //鼠标离开
        canvas.onmouseout = function (e) {
          isDraw = false;
        };
        //鼠标点击
        canvas.onclick = function (e) {
          that.centerX = e.offsetX;
          that.offsetX = canvas.width - 2 * e.offsetX;
        };
      };
    },
    keepImagesAndAfter(url) {
        const regex = /^https?:\/\/[^\/]+(\/images\/.*)$/i;
        return url.replace(regex, '$1');
      },
    // 选择标签
    handleLabel(item) {
      this.isShowCanvas = true;
      this.isShowDel = true;
      this.isShowMaterial = false;
      this.drawPreViewImage(item.src);
    },
    handleImage(src) {
      this.isShowCanvas = true;
      this.isShowDel = true;
      this.isShowMaterial = false;
      this.isShowDef = false;
      this.drawPreViewImage(src);
    },
    // 选择标签列表
    handleChangeLabel() {
      this.isShowDef = false;
      this.isShowMaterial = true;
    },
    handleSubmit() {
      // 先绘制底图
      var canvas = document.createElement("canvas");
      var context = canvas.getContext("2d");
      context.imageSmoothingEnabled = true;
      this.imageWidth = this.imageWidth;
      canvas.width = this.imageWidth;
      canvas.height = this.imageHeight;

      canvas.style.background = "rgba(255,255,255,0)";
      canvas.style.backgroundColor = "transparent";
      canvas.style.width = this.imageWidth + "px";
      canvas.style.height = this.imageHeight + "px";
      context.clearRect(0, 0, this.imageWidth, this.imageHeight);
      context.drawImage(this.image, 0, 0, this.imageWidth, this.imageHeight);

      // 根据窗体分辩率绘制文字
      var fontCanvas = document.createElement("canvas");
      var fontContext = fontCanvas.getContext("2d");
      var devicePixelRatio = window.devicePixelRatio || 1;
      var backingStoreRatio =
        fontContext.webkitBackingStorePixelRatio ||
        fontContext.mozBackingStorePixelRatio ||
        fontContext.msBackingStorePixelRatio ||
        fontContext.oBackingStorePixelRatio ||
        fontContext.backingStorePixelRatio ||
        1;
      var ratio = devicePixelRatio / backingStoreRatio;
      fontContext.imageSmoothingEnabled = true;
      fontCanvas.width = this.imageWidth * ratio;
      fontCanvas.height = this.imageHeight * ratio;
      fontCanvas.style.backgroundColor = "transparent";
      fontCanvas.style.width = this.imageWidth + "px";
      fontCanvas.style.height = this.imageHeight + "px";
      fontContext.fillStyle = this.color1; //填充颜色
      fontContext.textBaseline = "top";
      fontContext.font = "normal " + this.fontsize * ratio + "px " + this.value;
      var preCanvas = document.getElementById("preview");
      var box1 = preCanvas.getBoundingClientRect();
      if (this.pDocument !== null) {
        var box2 = this.pDocument.getBoundingClientRect();
        this.tLeft = box2.left - box1.left;
        this.tTop = box2.top - box1.top;
        this.pLeft = this.pDocument.style.left;
        this.pTop = this.pDocument.style.top;
      }
      fontContext.fillText(
        this.labelContent,
        this.tLeft * ratio,
        (this.tTop + 5) * ratio
      );
      fontContext.stroke();
      var dataUrl = fontCanvas.toDataURL("image/png", 1);
      var fontImage = new Image();
      fontImage.setAttribute("crossOrigin", "Anonymous");
      fontImage.src = dataUrl;
      var that = this;
      // 将文字与底图合并，得到最终的图片
      fontImage.onload = function () {
        context.drawImage(fontImage, 0, 0, that.imageWidth, that.imageHeight);
        var img = canvas.toDataURL("image/png", 1);
        var tagObject = {
          // id: `tag${new Date().getTime()}`,
          // userData:'police',
          // groupId:'police',
          // coordinate: that.option.translation,
          // imagePath: that.sourceSrc,
          // labelImage: img,
          // imageSize: [that.image.width, that.image.height],
          // text: that.labelContent,
          // range: [0, 8000],
          // type: 1,
          // ratio: that.valueNmber,
          // font: that.value,
          // fontSize: that.fontsize,
          // fontColor: that.color1,
          // pLeft: that.pLeft,
          // pTop: that.pTop,
          // tLeft: that.tLeft,
          // tTop: that.tTop,
          // originText: that.labelContent,
          id: `tag${new Date().getTime()}`,
          userData: "car",
          groupId: "car",
          coordinate: that.option.translation, //坐标位置
          anchors: [-(that.image.width / 2), that.image.height], //锚点，设置Marker的整体偏移，取值规则和imageSize设置的宽高有关，图片的左上角会对准标注点的坐标位置。示例设置规则：x=-imageSize.width/2，y=imageSize.height
          imageSize: [that.image.width, that.image.height], //图片的尺寸
          hoverImageSize: [that.image.width, that.image.height], //鼠标悬停时显示的图片尺寸
          range: [0, 18000], //可视范围
          // imagePath: that.sourceSrc, //显示图片路径
          imagePath1: that.keepImagesAndAfter(that.sourceSrc),//显示图片路径
        // imagePath:that.sourceSrc.replace(/http:\/\/[\d.]*:\d+\//, HostConfig.ImagePath + '\\'),
          imagePath:HostConfig.ImagePath+that.keepImagesAndAfter(that.sourceSrc),
          fixedSize: true, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
          text: that.labelContent, //显示的文字
          useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
          textRange: [1, 10000], //文本可视范围[近裁距离, 远裁距离]
          textOffset: [0, 0], // 文本偏移
          textBackgroundColor: Color.SpringGreen, //文本背景颜色
          fontSize: that.fontsize, //字体大小
          fontOutlineSize: 1, //字体轮廓线大小
          fontColor: that.color1, //字体颜色
          ratio: that.valueNmber,
          font: that.value,
          showLine: true, //标注点下方是否显示垂直牵引线
          lineSize: [1, 50], //垂直牵引线宽度和高度[width, height]
          lineColor: Color.SpringGreen, //垂直牵引线颜色
          lineOffset: [that.image.width / 2, 0], //垂直牵引线偏移
          autoHeight: false, // 自动判断下方是否有物体
          displayMode: 2, //智能显示模式  开发过程中请根据业务需求判断使用四种显示模式
          priority: 0, //避让优先级
          occlusionCull: true, //是否参与遮挡剔除
        };
        // that.addMarker(img)
        if (that.isEdit) {
          tagObject.id = that.editItem.id;
          // tagObject.delete = true;
        }
        if (that.isEdit) {
          window.__g.marker.update(tagObject);
          let data1 = sessionStorage.get("QXZS_3dMarker");
          let newData = data1.filter((item) => item.id !== tagObject.id);
          newData.push(tagObject);
          sessionStorage.set("QXZS_3dMarker", newData);
        } else {
          window.__g.marker.add(tagObject);
          let data = [];
          let data1 = sessionStorage.get("QXZS_3dMarker");
          if (data1 && data1.length > 0) {
            data = data1;
            data.push(tagObject);
          } else {
            data = [tagObject];
          }
          sessionStorage.set("QXZS_3dMarker", data);
        }
        that.$parent.setData();
        that.$parent.isShowAdd = false;
        // window.sealAPI._tag.addTag(tagObject).then((res) => {
        //     if (that.isEdit) {
        //         let data1 = sessionStorage.get("QXZS_3dMarker")
        //         console.log(tagObject, data1)
        //         let newData = data1.filter(item => item.id !== tagObject.id)
        //         newData.push(tagObject)
        //         sessionStorage.set("QXZS_3dMarker", newData)
        //     } else {
        //         let data = []
        //         let data1 = sessionStorage.get("QXZS_3dMarker")
        //         if (data1 && data1.length > 0) {
        //             data = data1
        //             data.push(tagObject)
        //         } else {
        //             data = [tagObject]
        //         }
        //         sessionStorage.set("QXZS_3dMarker", data)
        //     }
        //     // let data = []
        //     // let data1 = sessionStorage.get("QXZS_3dMarker")
        //     // if (data1 && data1.length > 0) {
        //     //     data = data1
        //     //     data.push(tagObject)
        //     // } else {
        //     //     data = [tagObject]
        //     // }
        //     // sessionStorage.set("QXZS_3dMarker", data)
        //     that.$parent.setData();
        //     that.$parent.isShowAdd = false;
        // });
      };
    },
    retainAfter(path, search) {
      const parts = path.split(search);
      return parts.length > 1 ? search + parts.slice(1).join('') : '';
    },
    setData(item) {
      item.fontColor = this.rgbaTo16color(this.arrColorTorgba(item.fontColor))
      this.editItem = item;
      this.isShowCanvas = true;
      this.isShowDel = true;
      this.isShowDef = false;
      this.isShowMaterial = false;
      this.isShowCoordinate = false;
      this.isEdit = true;
      this.labelContent = item.text;
      this.value = item.font;
      this.valueNmber = item.ratio;
      this.fontsize = item.fontSize;
      this.pLeft = item.pLeft;
      this.pTop = item.pTop;
      this.tLeft = item.tLeft;
      this.tTop = item.tTop;
      // this.sourceSrc = item.imagePath1;
      this.sourceSrc = this.retainAfter(item.imagePath,'/images')
      this.color1 = item.fontColor;
      this.imageLoaded = true;
      this.coordinateCollected = true;
      this.drawPreViewImage(this.sourceSrc);
      this.option.translation = item.coordinate;

      if (this.pDocument === null) {
        this.pDocument = document.createElement("p");
        this.pDocument.style.position = "absolute";
        this.pDocument.style.pointerEvents = "none";
        this.pDocument.style.verticalAlign = "top";
        var div = document.getElementById("canvasDiv");
        div.appendChild(this.pDocument);
      }

      this.pDocument.style.left = this.pLeft;
      this.pDocument.style.top = this.pTop;

      this.pDocument.innerText = this.labelContent;
      this.pDocument.style.color = item.fontColor;
      this.pDocument.style.font = `normal ${this.fontsize}px ${this.value}`;

      this.submitShow =
        this.imageLoaded ||
        this.labelContent.length > 0 ||
        this.coordinateCollected;
    },
    /**
     * 封装rgbaTo16color功能函数
     * 功能: 把rgba颜色(或rgb颜色)转成十六进制颜色
     */
    rgbaTo16color(color) {
      let val = color
        .replace(/rgba?\(/, "")
        .replace(/\)/, "")
        .replace(/[\s+]/g, "")
        .split(",");
      let a = parseFloat(val[3] || 1),
        r = Math.floor(a * parseInt(val[0]) + (1 - a) * 255),
        g = Math.floor(a * parseInt(val[1]) + (1 - a) * 255),
        b = Math.floor(a * parseInt(val[2]) + (1 - a) * 255);
      return (
        "#" +
        ("0" + r.toString(16)).slice(-2) +
        ("0" + g.toString(16)).slice(-2) +
        ("0" + b.toString(16)).slice(-2)
      );
    },
    arrColorTorgba(arrCol) {
      let rgba = [arrCol[0]*255,arrCol[1]*255,arrCol[2]*255,arrCol[3]]
      return `rgba(${rgba.join(',')})`
    },
    changeZoom(val) {
      // var canvas = document.getElementById("preview");
      // this.imageWidth = (canvas.width * val) / 100;
      // this.imageHeight = (canvas.height * val) / 100;
      // // canvas.width = this.imageWidth;
      // // canvas.height = this.imageHeight;
      // canvas.style.width = this.imageWidth + "px";
      // canvas.style.height = this.imageHeight + "px";
    },
    // 重新拾取
    rePick() {
      this.isPick = true;
    },
    // 获取点选位置
    getMouseClickPoint(data) {
      if (!this.isShowCoordinate && this.isPick) {
        this.option.translation = data.MouseClickPoint;
        this.option.translation[0] =
          Math.round(this.option.translation[0] * 100) / 100;
        this.option.translation[1] =
          Math.round(this.option.translation[1] * 100) / 100;
        this.option.translation[2] =
          Math.round(this.option.translation[2] * 100) / 100;
        this.coordinateCollected = true;
        this.submitShow =
          this.imageLoaded ||
          this.labelContent.length > 0 ||
          this.coordinateCollected;
        this.isPick = false;
      }
    },
    handleClose() {
      this.$parent.isShowAdd = false;
    },
    // async addMarker(markerImg) {
    //   await __g.marker.clear();
    //   //支持经纬度坐标和普通投影坐标两种类型
    //   let o1 = {
    //     id: "m1",
    //     groupId: "markerAdd",
    //     coordinate: [595758, 3334468, 24.995790481567383], //坐标位置
    //     coordinateType: 0, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
    //     anchors: [-25, 50], //锚点，设置Marker的整体偏移，取值规则和imageSize设置的宽高有关，图片的左上角会对准标注点的坐标位置。示例设置规则：x=-imageSize.width/2，y=imageSize.height
    //     imageSize: [50, 50], //图片的尺寸
    //     hoverImageSize: [50, 50], //鼠标悬停时显示的图片尺寸
    //     range: [1, 10000], //可视范围
    //     imagePath: attributeServer + "/customTag/images/顺丰@2x.png", //显示图片路径
    //     hoverImagePath: attributeServer + "/customTag/images/顺丰@2x.png", // 鼠标悬停时显示的图片路径
    //     // imagePath: markerImg,//显示图片路径
    //     //hoverImagePath: markerImg,// 鼠标悬停时显示的图片路径
    //     fixedSize: true, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false

    //     text: "北京银行", //显示的文字
    //     useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
    //     textRange: [1, 1000], //文本可视范围[近裁距离, 远裁距离]
    //     textOffset: [0, 0], // 文本偏移
    //     textBackgroundColor: Color.SpringGreen, //文本背景颜色
    //     fontSize: 24, //字体大小
    //     fontOutlineSize: 1, //字体轮廓线大小
    //     fontColor: Color.White, //字体颜色
    //     fontOutlineColor: Color.Black, //字体轮廓线颜色

    //     popupURL: window.location.origin + "/customTag/ctag.html", //弹窗HTML链接
    //     popupBackgroundColor: [1.0, 1.0, 1.0, 0.5], //弹窗背景颜色
    //     popupSize: [300, 300], //弹窗大小
    //     popupOffset: [0, 0], //弹窗偏移

    //     showLine: true, //标注点下方是否显示垂直牵引线
    //     lineSize: [2, 100], //垂直牵引线宽度和高度[width, height]
    //     lineColor: Color.SpringGreen, //垂直牵引线颜色
    //     lineOffset: [0, 0], //垂直牵引线偏移

    //     autoHidePopupWindow: true, //失去焦点后是否自动关闭弹出窗口
    //     autoHeight: false, // 自动判断下方是否有物体
    //     displayMode: 4, //智能显示模式  开发过程中请根据业务需求判断使用四种显示模式
    //     clusterByImage: true, // 聚合时是否根据图片路径分类，即当多个marker的imagePath路径参数相同时按路径对marker分类聚合
    //     priority: 0, //避让优先级
    //     occlusionCull: true, //是否参与遮挡剔除
    //   };

    //   let markerArr = [];
    //   markerArr.push(o1);
    //   //海量poi添加请使用批量添加 提供效率
    //   await __g.marker.add(markerArr);
    //   __g.marker.focus(o1.id, 100, 0);
    // },
  },
  mounted() {},
  beforeUnmount() {
    if (this.pDocument !== null) {
      var elem = document.getElementById("canvasDiv");
      if (elem) elem.removeChild(this.pDocument);
    }
  },
};
</script>

<style lang="scss" scoped>
@import "@/styles/func3.scss";

.add {
  width: 658px;

  .func-warp {
    height: 466px;
    display: flex;

    .warp-left {
      width: 385px;

      .panel-warp {
        width: 380px;
        height: 320px;
        background: #4b5059;
        box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
          0px 0px 0px 0px rgba(0, 0, 0, 0.5);
        border-radius: 16px;
        border: 2px solid;
        border-color: rgba(2, 178, 255, 1);
        margin-bottom: 16px;
        padding: 4px 6px;
        box-sizing: border-box;
        position: relative;

        .panel-icon {
          cursor: pointer;
          height: 32px;
          background: rgba(255, 255, 255, 0.2);
          border-radius: 16px;
          position: absolute;
          z-index: 9;
          right: 6px;
          display: flex;

          > div {
            width: 32px;
            height: 32px;
            line-height: 32px;
            border-radius: 50%;
            text-align: center;
          }

          .icon {
            width: 16px;
            height: 16px;
          }
        }

        .panel-add {
          width: 100%;
          height: 100%;
          display: flex;
          align-items: center;

          .panel-add-list {
            flex: 1;
            height: 76px;
            text-align: center;
            color: #fff;

            .icon {
              cursor: pointer;
              width: 48px;
              height: 48px;
            }
          }

          .panel-add-list:first-child {
            border-right: 1px solid #646b6f;
          }
        }

        .panel-canvas {
          position: relative;
          display: inline-block;
        }

        .panel-material {
          width: 100%;
          height: 100%;
          padding: 45px 17px;
          box-sizing: border-box;

          .panel-material-warp {
            width: 100%;
            height: 100%;

            .el-scrollbar__wrap {
              overflow-x: hidden;

              .material-list {
                cursor: pointer;
                width: 100px;
                height: 100px;
                margin-bottom: 5px;
                margin-right: 6px;
                background: #363840;
                border-radius: 9px;
                border: 1px solid #63646a;
                display: inline-block;
                text-align: center;
                padding: 7px;
                box-sizing: border-box;
                overflow: hidden;

                ul {
                  width: 100%;
                  height: 100%;
                  display: flex;
                  justify-content: center;
                  align-items: center;

                  img {
                    width: auto;
                    height: auto;
                    max-width: 100%;
                    max-height: 100%;
                  }
                }
              }
            }
          }
        }
      }

      .panel-footer {
        cursor: pointer;
        width: 100%;
        height: 56px;
        border-radius: 6px;
        border: 1px solid #646b6f;
        display: flex;
        justify-content: center;
        align-items: center;
        font-size: 14px;
        color: #fff;
      }

      .list-title {
        font-size: 16px;
        color: #fff;
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 6px;

        div {
          cursor: pointer;
          font-size: 14px;
          color: #029eff;
          display: flex;
          align-items: center;
        }
      }

      .list-warp {
        display: flex;

        .el-input-number {
          width: 110px;
        }

        ul {
          white-space: nowrap;

          &:not(:last-child) {
            margin-right: 12px;
          }

          > span {
            font-size: 12px;
            font-weight: 400;
            color: #fff;
            margin-left: 6px;
          }
        }
      }
    }

    .warp-right {
      width: 225px;
      padding-left: 22px;

      .label {
        display: flex;
        justify-content: space-between;
        font-size: 16px;
        color: #fff;
        margin-bottom: 6px;

        &:not(:first-child) {
          margin-top: 12px;
        }

        .font {
          display: flex;
          align-items: center;
          padding-right: 12px;
        }

        .icon {
          width: 32px;
          height: 32px;
          margin-right: 6px;
        }
      }

      .proportion {
        display: flex;
        justify-content: space-between;

        .el-slider {
          width: calc(60% - 14px);
        }

        .el-input-number {
          width: 40%;
        }
      }
    }
  }

  .warp-footer {
    display: flex;
    justify-content: right;
    padding: 24px;
    border-top: 1px solid #ffffff33;

    div {
      min-width: 140px;
      line-height: 38px;
      cursor: pointer;
      border-radius: 5px;
      border: 1px solid #029eff;
      text-align: center;

      &.primary {
        background: #029eff;
        margin-left: 20px;
      }
    }
  }
}
</style>
