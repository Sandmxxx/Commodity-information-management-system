<template>
  <div id="app">
    <goods-view></goods-view>
  </div>
</template>

<script>
import GoodsView from "@/views/Goods/GoodsView.vue";
// import GoodsAddView from "@/views/Goods/GoodsAddView.vue";
// import TestView from "@/views/Goods/TestView.vue";

export default {
  components: {GoodsView},
  data(){
    return {
      "message":"hello world"
    }
  }
}
</script>
<style>

</style>