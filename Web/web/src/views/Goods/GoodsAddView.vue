<script setup lang="ts">
import {SYNC_POST} from "@/scripts/Axios";
import {reactive, ref} from "vue";

const dialogFormVisible = ref(false)

import type {FormProps} from 'element-plus'

const labelPosition = ref<FormProps['labelPosition']>('right')

const formLabelAlign = reactive({
  name: '',
  region: '',
  type: '',
})


const goodsDetail = reactive({
  id: '',
  name: '',
  brand: '',
  type: '',
  unit: '',
  price: '',
  time: ''
})

let flag = ref(0);

const save = async () => {
  console.log("save")
  //to do:获取token测试
  await SYNC_POST("/goods/addGoods", {
    id: goodsDetail.id,
    name: goodsDetail.name,
    brand: goodsDetail.brand,
    type: goodsDetail.type,
    unit: goodsDetail.unit,
    price: goodsDetail.price,
    time: goodsDetail.time
  }, async (response) => {
    if (response.status === 200 && response.data.statusMsg === 'success') {
      console.log("Save successfully!")
      flag.value = 1;
      console.log("flag=" + flag.value);
    }else {
      flag.value = 0;
      console.log("error!")
      console.log("flag=" + flag.value);
    }
  })
}
import { ElMessage } from 'element-plus'

const suc = () => {
  ElMessage({
    message: '新增商品成功！',
    type: 'success',
  })
}
const err = () => {
  ElMessage.error('新增商品失败！')
}

async function onSave() {
  dialogFormVisible.value = false
  await save()
  if (flag.value === 1) {
    suc()
  } else {
    err()
  }
}

</script>

<template>
  <el-button type="warning" @click="() => {dialogFormVisible = true}" round>
    增加商品
  </el-button>

  <el-dialog v-model="dialogFormVisible" title="增加商品" center width = "35%" draggable>
    <el-form
        :label-position="labelPosition"
        label-width="100px"
        :model="formLabelAlign"
        style="max-width: 460px"
    >
      <el-form-item label="商品编号">
        <el-input v-model="goodsDetail.id" />
      </el-form-item>
      <el-form-item label="商品名称">
        <el-input v-model="goodsDetail.name" />
      </el-form-item>
      <el-form-item label="商品品牌">
        <el-input v-model="goodsDetail.brand" />
      </el-form-item>
      <el-form-item label="商品类型">
        <el-input v-model="goodsDetail.type" />
      </el-form-item>
      <el-form-item label="商品单位">
        <el-input v-model="goodsDetail.unit" />
      </el-form-item>
      <el-form-item label="商品价格">
        <el-input v-model="goodsDetail.price" />
      </el-form-item>
      <el-form-item label="过期时间">
        <el-input v-model="goodsDetail.time" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="onSave()" >
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>

</style>