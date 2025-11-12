<template>
  <div class="promotional">
    <div class="panelclose" @click="electclose()"></div>
    <video
      id="video-item"
      class="video"
      preload="auto"
      controls
      muted
      autoplay
      object-fit="fill"
    >
      <source type="video/mp4" src="/video/TQ视频.mp4" />
    </video>
  </div>
</template>

<script setup>
import useWorkCockpitStore from '@/store/modules/workCockpit.js'
const electclose = () => {
  useWorkCockpitStore().set_showPlayVideo(false)
};
</script>

<style lang="scss" scoped>
.promotional {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 50%;
  z-index: 100;
  transform: translateX(-50%);
  background-color: rgba(6, 8, 8, 0.9);
}
#video-item {
  width: 100%;
  height: 100%;
  object-fit: fill;
}
.panelclose {
  position: absolute;
  right: 30px;
  top: 30px;
  z-index: 101;
  background: url("/video/close_nor.png") no-repeat;
  width: 50px;
  height: 50px;
  background-size: 50px 50px;
  cursor: pointer;
  &:hover {
    background: url("/video/close_hover.png") no-repeat;
    background-size: 50px 50px;
  }
}
</style>
