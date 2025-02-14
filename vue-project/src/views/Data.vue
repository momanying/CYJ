<template>
    <div>
     <el-row :gutter="10">
      <el-col :span="12" style="margin-bottom: 10px;">
          <div class="card" style="flex :1;height: 400px; padding: 20px;" id="bar"></div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 10px;">
          <div class="card" style="flex :1;height: 400px; padding: 20px;" id="line"></div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 10px;">
           <div class="card" style="flex :1;height: 400px;padding: 20px;" id="pie"></div>
        </el-col>
    </el-row>    
    </div>
</template>

<script setup>
import { reactive,onMounted } from 'vue'
import * as echarts from 'echarts'
import request from '@/utils/request';

const barOption = {
        title: {
          text: '各部门员工数量'
        },
        tooltip: {},
        legend: {
          data: ['人数']
        },
        xAxis: {
          data: []
        },
        yAxis: {},
        series: [
          {
            name: '人数',
            type: 'bar',
            data: [],
            itemStyle: {
              normal: {
                color: function(){
                  return "#" + Math.floor(Math.random()*(256*256*256-1)).toString(16);
                }
              }
            }
          }
        ]
      };

      const lineOption = {
        title: {
          text: '近7天发布文章的数量'
        },
        tooltip: {},
        legend: {
          trigger:'item'
        },
        xAxis: {
          data: []
        },
        yAxis: {},
        series: [
          {
            name: '发布数量',
            type: 'line',
            data: [],
            smooth: true
          }
        ]
      };

      const pieOption = {
        title: {
          text: '各部门员工数量比例图',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '员工数量',
            type: 'pie',
            radius: '50%',
            data: [],
            center:['50%','60%'],
            label: {
              formatter: '{b}: {@2012} ({d}%)'
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };

const data = reactive({
    
})

onMounted(()=>{
    const barChart = echarts.init(document.getElementById('bar'))

    request.get('/barData').then(res=>{
      barOption.xAxis.data = res.data.department
      barOption.series[0].data = res.data.count
      barChart.setOption(barOption)
    })

    const lineChart = echarts.init(document.getElementById('line'))

    request.get('/lineData').then(res=>{
      lineOption.xAxis.data = res.data.date
      lineOption.series[0].data = res.data.count
      lineChart.setOption(lineOption)
    })

    const pieChart = echarts.init(document.getElementById('pie'))
    request.get('/pieData').then(res=>{
      pieOption.series[0].data = res.data
      pieChart.setOption(pieOption)
    })
})

</script>

<style scoped>

</style>