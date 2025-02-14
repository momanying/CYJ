<template>
    <div>
        <div>
            <el-input v-model="data1.input" style="width: 240px" placeholder="请输入内容" :prefix-icon="Search"/>{{ data1.input }}
            <el-input style="width: 200px;" :suffix-icon="Calendar"></el-input>
        </div>
    </div>
    <el-button @click="router.back()">
        返回首页
    </el-button>
    <el-date-picker
      style="margin-left: 50px;"
      v-model="data1.daterange"
      type="daterange"
      range-separator="To"
      start-placeholder="Start date"
      end-placeholder="End date"
      format="YYYY-MM-DD"
      value-format="YYYY-MM-DD"
    /> {{ data1.daterange?.length ? data1.daterange[0] : ''}} {{ data1.daterange?.length ? data1.daterange[1] : '' }}
</template>

<script setup>
import { reactive } from 'vue';
import { Search } from '@element-plus/icons-vue';
import { Calendar } from '@element-plus/icons-vue';
import router from '@/router/index.js';
import { onMounted } from 'vue'
const data1 = reactive({
    input : null,
    daterange: []
})
const data = reactive({
  id : router.currentRoute.value.query.id,
  name : router.currentRoute.value.query.name,
})
console.log("路由传参id"+data.id)
console.log("路由传参name"+data.name)
onMounted(() => {
  console.log('页面加载完成')
})
</script>