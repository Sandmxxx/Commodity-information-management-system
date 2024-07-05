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

let id = ref('')
let time = ref('')

let flag = ref(0);

const setExpire = async () => {
  console.log("setExpire")
  //to do:获取token测试
  await SYNC_POST("/goods/setExpire", {
    id: id.value,
    time:time.value
  }, async (response) => {
    console.log("设置过期时间状态：" + response.data.statusMsg)
    if (response.status === 200 && response.data.statusMsg === 'success') {
      console.log("setExpire successfully!")
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
    message: '设置商品过期时间成功！',
    type: 'success',
  })
}
const err = () => {
  ElMessage.error('设置商品过期时间失败！')
}

async function onSetExpire() {
  dialogFormVisible.value = false
  await setExpire()
  if (flag.value === 1) {
    suc()
  } else {
    err()
  }
}

</script>

<template>
  <el-button type="warning" @click="() => {dialogFormVisible = true}" round>
    设置商品过期时间
  </el-button>

  <el-dialog v-model="dialogFormVisible" title="设置商品过期时间" center width = "35%" draggable>
    <el-form
        :label-position="labelPosition"
        label-width="140px"
        :model="formLabelAlign"
        style="max-width: 460px"
    >
      <el-form-item label="商品编号">
        <el-input v-model="id" />
      </el-form-item>
      <el-form-item label="过期时间">
        <el-input v-model="time" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="onSetExpire()" >
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>

</style>