<template>
    <div>
        <!--头部开始-->
        <div style="height: 40px; background-color: #3c7fff; display: flex; align-items: center;">
            <div style="width: 200px; display: flex; align-items: center; padding-left: 10px;">
                <img style="width: 15px;" src="@/assets/firefox.png" alt="">
                <div style="width: 200px; display: flex; align-items: center; font-size: 15px; color: white; margin-left: 5px;">后台管理系统</div>
            </div>
            <div style="flex : 1;"></div>
            <div style="width: fit-content; display: flex; align-items: center; padding-right: 10px;">
                <img :src="data.user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt="" style="width: 30px; height: 30px; border-radius: 50%;">
                <span style="color: white; margin-left: 5px;">{{ data.user.name }}</span> 
            </div>
        </div>
        <!--头部结束-->

        <!--下面部分开始-->
        <div style="display: flex;">
            <!--左侧导航菜单开始-->
            <div style="width: 150px; border-right: 1px solid #ddd; min-height: calc(100vh - 60px);">
                <el-menu router :default-active="$router.currentRoute.value.path" :default-openeds="['1']" style="border: 0;">
                    <el-menu-item index="/Manager/Home">
                        <el-icon><House /></el-icon>
                        系统首页
                    </el-menu-item>
                    <el-menu-item index="/Manager/Data">
                        <el-icon><DataAnalysis /></el-icon>
                        数据统计
                    </el-menu-item> 
                    <el-menu-item index="/Manager/Article"  v-if="data.user.role=='ADMIN'">
                        <el-icon><Reading /></el-icon>
                        文章管理
                    </el-menu-item> 
                    <el-menu-item index="/Manager/Department"  v-if="data.user.role=='ADMIN'">
                        <el-icon><OfficeBuilding /></el-icon>
                        部门管理
                    </el-menu-item> 
                    <el-sub-menu index="1"  v-if="data.user.role=='ADMIN'">
                        <template #title>
                            <el-icon><User /></el-icon>
                            <span>用户管理</span>
                        </template>
                        <el-menu-item index="/Manager/Admin">管理员信息</el-menu-item>
                        <el-menu-item index="/Manager/Employee">员工信息</el-menu-item>
                    </el-sub-menu>
                    <el-menu-item index="/Manager/person">
                        <el-icon><UserFilled /></el-icon>
                        个人信息
                    </el-menu-item>   
                    <el-menu-item index="/Manager/password">
                        <el-icon><Lock /></el-icon>
                        修改密码
                    </el-menu-item> 
                    <el-menu-item @click="logout">
                        <el-icon><SwitchButton /></el-icon>
                        退出登录
                    </el-menu-item>
                </el-menu>
            </div>
            <!--左侧导航菜单结束-->

            <!--右侧内容开始-->
            <div style="flex: 1; background-color: #f5f7ff; padding: 10px;">
                <RouterView  @updateUser="updateUser"/>
            </div>
            <!--右侧内容结束-->
        </div>
        <!--下面部分结束-->
    </div>
</template>

<script setup>
import { reactive } from 'vue';
import { RouterView } from 'vue-router';

const data = reactive({
    user: JSON.parse(localStorage.getItem('xmuser'))
})

const logout = () => {
    localStorage.removeItem('xmuser')
    location.href = '/login'
}

const updateUser = () => {
    data.user = JSON.parse(localStorage.getItem('xmuser'))
}
</script>

<style scoped>
.el-menu .is-active {
    background-color: #e6ecf7 !important;
}
.el-sub-menu__title {
    background-color: white !important;
}
</style>
