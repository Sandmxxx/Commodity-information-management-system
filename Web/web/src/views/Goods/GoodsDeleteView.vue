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

let flag = ref(0);

const del = async () => {
  console.log("delete")
  //to do:获取token测试
  await SYNC_POST("/goods/deleteGoods", {
    id: id.value
  }, async (response) => {
    console.log(response.data.statusMsg)
    if (response.status === 200 && response.data.statusMsg === 'success') {
      console.log("Delete successfully!")
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
    message: '删除商品成功！',
    type: 'success',
  })
}
const err = () => {
  ElMessage.error('删除商品失败！')
}

async function onDel() {
  dialogFormVisible.value = false
  await del()
  if (flag.value === 1) {
    suc()
  } else {
    err()
  }
}

</script>

<template>
  <el-button type="warning" @click="() => {dialogFormVisible = true}" round>
    删除商品
  </el-button>

  <el-dialog v-model="dialogFormVisible" title="删除商品" center width = "35%" draggable>
    <el-form
        :label-position="labelPosition"
        label-width="100px"
        :model="formLabelAlign"
        style="max-width: 460px"
    >
      <el-form-item label="商品编号">
        <el-input v-model="id" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="onDel()" >
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>

</style>