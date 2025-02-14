<template>
    <div>
        <div class="card" style="margin-bottom: 5px;">
            <el-input  style="width: 240px; margin-right: 10px" v-model="data.name" placeholder="请输入名称查询" :prefix-icon="Search"></el-input>
            <el-button type="primary" @click="load">查询</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>
        <div class="card" style="margin-bottom: 5px;">
            <el-button type="primary" @click="handleAdd">新增</el-button>
            <el-button type="warning" @click="delBatch">批量删除</el-button>
            <el-upload
                style="display: inline-block; margin:0 10px"
                action="http://localhost:9090/employee/import"
                :show-file-list="false"
                :on-success="importSuccess"
            >
                <el-button type="info">导入</el-button>
            </el-upload>
            <el-button type="success" @click="exportData">导出</el-button>
        </div>
        <div class="card" style="margin-bottom: 5px ;">
            <el-table :data="data.tableData" stripe @Selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" />
                <el-table-column label="账号" prop="username"/>
                <el-table-column label="头像">
                    <template #default="scope">
                        <img v-if="scope.row.avatar" :src="scope.row.avatar" style="width: 40px; height: 40px; border-radius: 50%;" />
                    </template>
                </el-table-column>/>
                <el-table-column label="名称" prop="name"/>
                <el-table-column label="性别" prop="sex"/>
                <el-table-column label="工号" prop="no"/>
                <el-table-column label="年龄" prop="age"/>
                <el-table-column label="个人介绍" prop="description" show-overflow-tooltip/>
                <el-table-column label="部门" prop="departmentName"/>
                <el-table-column label="操作">
                    <template #default="scope">
                        <el-button @click="handleEdit(scope.row)" type="primary" :icon="Edit"></el-button>
                        <el-button @click="Del(scope.row.id)" type="danger" :icon="Delete"></el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div style="margin-top: 10px ;">
                <el-pagination
                    @size-change="load"
                    @current-change="load"
                    v-model:current-page="data.pageNum"
                    v-model:page-size="data.pageSize"
                    :page-Sizes="[5, 10, 15, 20]"
                    background
                    layout="total, sizes, prev, pager, next, jumper"
                    :total = "data.total"
                />
            </div>
        </div>
            <el-dialog v-model="data.FormVisible" title="员工信息" width="500" destroy-on-close>
            <el-form  ref="formRef" :rules="data.rules" :model="data.form"  label-width = "80px" style="padding-right: 20px; padding-top: 20px;">
                <el-form-item label="账号" prop="username">
                    <el-input :disabled="data.form.id" v-model="data.form.username" autocomplete="off" placeholder = "请输入账号" />
                </el-form-item>
                <el-form-item label="部门">
                    <el-select style="width: 100%;"  v-model="data.form.departmentId">
                        <el-option v-for="item in data.departmentList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="头像">
                    <el-upload
                    action="http://localhost:9090/files/upload"
                    list-type="picture"
                    :on-success="handleAvatarSuccess"
                    >
                    <el-button type="primary">上传头像</el-button>
                    </el-upload>
                </el-form-item>
                <el-form-item label="名称" prop="name">
                    <el-input v-model="data.form.name" autocomplete="off" placeholder = "请输入名称" />
                </el-form-item>
                <el-form-item label="性别">
                    <el-radio-group v-model = "data.form.sex">
                        <el-radio value = '男' label = '男'></el-radio>
                        <el-radio value = '女' label = '女'></el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="工号" prop="no">
                    <el-input v-model="data.form.no" autocomplete="off" placeholder = "请输入工号" />
                </el-form-item>
                <el-form-item label="年龄">
                    <el-input-number style="width: 180px;" v-model="data.form.age" autocomplete="off" placeholder = "请输入年龄" />
                </el-form-item>
                <el-form-item label="个人介绍">
                    <el-input :rows="3" type = "textarea" v-model="data.form.description" autocomplete="off" placeholder = "请输入个人介绍" />
                </el-form-item>
            </el-form>
            <template #footer>
            <div class="dialog-footer">
                <el-button @click="data.FormVisible = false">取消</el-button>
                <el-button type="primary" @click="save">保存</el-button>
            </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import {reactive} from 'vue'
import { Edit,Delete,Search } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ref } from 'vue'

const data = reactive({
    name: null,
    tableData: [],
    pageNum : 1,
    pageSize : 10,
    total : 0,
    FormVisible: false,
    form: {},
    ids: [],
    departmentList:[],
    username: [],
    rules: {
        username: [
            { required: true, message: '请输入账号', trigger: 'blur' }
        ],
        name: [
            { required: true, message: '请输入名称', trigger: 'blur' }
        ],
        no: [
            { required: true, message: '请输入工号', trigger: 'blur' }
        ],
    }
})

const importSuccess = () =>{
    ElMessage.success("导入成功")
    load()
}

const exportData = () =>{
    window.open('http://localhost:9090/employee/export')
}

const handleAvatarSuccess = (res) => {
    data.form.avatar = res.data
}

request.get('/department/selectAll').then(res=>{
    data.departmentList = res.data
})

const formRef = ref(null)

const load = () => {
    request.get('/employee/selectPage', {
        params: {
            pageNum: data.pageNum,
            pageSize: data.pageSize,
            name: data.name
        }
    }).then(res => {
        data.tableData = res.data.list
        data.total = res.data.total
    })
}

load()

const reset = () => {
    data.name = null
    load()
}

const handleAdd = () => {
    data.FormVisible = true
    data.form = {}
}

const save = () => {
    formRef.value.validate((valid) => {
        if(valid) {
           data.form.id ? update() : add()
        }else {
            ElMessage.error('请检查输入项')
            return false
        }
    })
}

const add = () => {
    request.post('/employee/add', data.form).then(res => {
        if(res.code === '200') {
            data.FormVisible = false
            ElMessage.success('保存成功')
            load()
        }else {
            ElMessage.error('保存失败')
        }
    })
}

const update = () => {
    request.put('/employee/update', data.form).then(res => {
        if(res.code === '200') {
            data.FormVisible = false
            ElMessage.success('保存成功')
            load()
        }else {
            ElMessage.error('保存失败')
        }
    })
}

const handleEdit = (row) => {
    data.FormVisible = true
    data.form = JSON.parse(JSON.stringify(row))
}

const Del = (id) => {
    ElMessageBox.confirm('删除数据后无法恢复', '删除确认', {
        type: 'warning'
    }).then(() => {
        request.delete('/employee/delete/' + id).then(res => {
        if(res.code === '200') {
            ElMessage.success('删除成功')
            load()
        }else {
            ElMessage.error(res.msg)
        }
    })
    }).catch(() => {
        ElMessage.info('取消删除')
    })
}

const handleSelectionChange = (rows) => {
    data.ids = rows.map(row => row.id)
}

const delBatch = () => {
    if(data.ids.length === 0) {
        ElMessage.warning('请选择要删除的数据')
        return
    }
    ElMessageBox.confirm('删除数据后无法恢复', '删除确认', {
        type: 'warning'
    }).then(() => {
        request.delete('/employee/deleteBatch', {
            data: data.ids
        }).then(res => {
            if(res.code === '200') {
                ElMessage.success('删除成功')
                load()
            }else {
                ElMessage.error(res.msg)
            }
        })
    }).catch(() => {
        ElMessage.info('取消删除')
    })
}
</script>