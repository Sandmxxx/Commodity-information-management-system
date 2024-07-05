<script setup lang="ts">
import {SYNC_POST} from "@/scripts/Axios";
import {reactive, ref} from "vue";

const dialogFormVisible = ref(false)

// const dialogFormVisible2 = ref(false)

import type {FormProps} from 'element-plus'

const labelPosition = ref<FormProps['labelPosition']>('right')

const formLabelAlign = reactive({
  name: '',
  region: '',
  type: '',
})


let flag = ref(0);
let channel = ref('')



const sub = async () => {
  console.log("sub")
  //to do:获取token测试
  await SYNC_POST("/goods/subscribe", {
    channel:channel.value
  }, async (response) => {
    if (response.status === 200 && response.data.statusMsg === 'success') {
      console.log("subscribe successfully!")
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
    message: '订阅成功！',
    type: 'success',
  })
}
const err = () => {
  ElMessage.error('订阅失败！')
}

async function onSub() {
  dialogFormVisible.value = false
  await sub()
  if (flag.value === 1) {
    suc()
  } else {
    err()
  }
}

const emit = defineEmits(['child-click'])
const ziChuanFu = () => {
  emit('child-click',channel.value)
  console.log("传给父节点：" + channel.value)
}


</script>

<template>
  <el-button type="primary" @click="() => {dialogFormVisible = true}" round>
    订阅频道
  </el-button>

  <el-dialog v-model="dialogFormVisible" title="订阅" center width = "35%" draggable>
    <el-form
        :label-position="labelPosition"
        label-width="140px"
        :model="formLabelAlign"
        style="max-width: 460px"
    >
      <el-form-item label="要订阅的频道：">
        <el-input v-model="channel" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="() => {onSub();ziChuanFu()}" >
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>


<!--  <el-button type="success" @click="() => {dialogFormVisible2 = true}" round>-->
<!--    取消订阅-->
<!--  </el-button>-->

<!--  <el-dialog v-model="dialogFormVisible2" title="订阅" center width = "35%">-->
<!--    <el-form-->
<!--        :label-position="labelPosition"-->
<!--        label-width="140px"-->
<!--        :model="formLabelAlign"-->
<!--        style="max-width: 460px"-->
<!--    >-->
<!--      <el-form-item label="要取消订阅的频道：">-->
<!--        <el-input v-model="channel2" />-->
<!--      </el-form-item>-->
<!--    </el-form>-->
<!--    <template #footer>-->
<!--      <span class="dialog-footer">-->
<!--        <el-button @click="dialogFormVisible2 = false">取消</el-button>-->
<!--        <el-button type="primary" @click="() => {unSub();delChannel();}" >-->
<!--          确定-->
<!--        </el-button>-->
<!--      </span>-->
<!--    </template>-->
<!--  </el-dialog>-->
</template>

<style scoped>

</style>