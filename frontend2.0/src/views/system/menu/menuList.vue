
<template>
  <el-main>
    <!-- 新增按钮 -->
    <el-button type="success" size="small" @click="openAddWindow()" icon="el-icon-plus">新增</el-button>
    <!--表格-->
    <el-table
      :data="menuList"
      style="width: 100%;margin-top: 20px;"
      :height="tableHeight"
      row-key="id"
      border
      default-expand-all
      :tree-props="{children: 'children'}">
      <el-table-column prop="label" label="菜单名称" sortable width="180"></el-table-column>
      <el-table-column prop="type" label="菜单类型" sortable width="180" align="center">
        <template slot-scope="scope">
          <el-tag size="normal" v-if="scope.row.type === 0">目录</el-tag>
          <el-tag size="normal" type="success" v-else-if="scope.row.type === 1">菜单</el-tag>
          <el-tag size="normal" type="warning" v-else-if="scope.row.type === 2">按钮</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="icon" label="菜单图标" sortable width="180" align="center">
        <template slot-scope="scope">
          <i :class="scope.row.icon" v-if="scope.row.icon.includes('el-icon')"></i>
          <svg-icon v-else :icon-class="scope.row.icon"></svg-icon>
        </template>
      </el-table-column>
      <el-table-column prop="path" label="菜单路由" sortable width="180"></el-table-column>
      <el-table-column prop="mname" label="菜单名字" sortable width="180"></el-table-column>
      <el-table-column prop="url" label="菜单地址" sortable width="180"></el-table-column>
      <el-table-column label="操作" sortable width="180">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="onEdit(scope.row)" icon="el-icon-edit-outline">编辑</el-button>
          <el-button type="danger" size="mini" @click="onDelete(scope.row)" icon="el-icon-edit-outline">删除</el-button
        </template>
      </el-table-column>

    </el-table>
    <!--新增或修改的窗口-->
    <system-dialog :title="menuDialog.title" :visible="menuDialog.visible" :width="menuDialog.width" :height="menuDialog.height"
                   @onClose="onClose()" @onConfirm="onConfirm()">
      <div slot="content">
        <el-form ref="menu" :model="menuForm" :rules="rules" label-width="80px" :inline="true" size="small">
          <el-col span="24">
            <el-form-item label="菜单类型" prop="type">
              <el-radio-group v-model="menu.type" >
                <el-radio :label="0">目录</el-radio>
                <el-radio :label="1">菜单</el-radio>
                <el-radio :label="2">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-form-item label="所属菜单" size="small" prop="parentName">
            <el-input v-model="menu.parentName" :readonly="true" @click.native="selectParentMenu()"/>
          </el-form-item>
          <el-form-item label="菜单名称" size="small" prop="label">
            <el-input v-model="menu.label"/>
          </el-form-item>
          <el-form-item label="路由名称" size="small" prop="name" v-if="menu.type === 1">
            <el-input v-model="menu.name"/>
          </el-form-item>
          <el-form-item label="路由地址" size="small" prop="path" v-if="menu.type !== 2">
            <el-input v-model="menu.path"/>
          </el-form-item>
          <el-form-item label="组件路径" size="small" prop="url" v-if="menu.type === 1">
            <el-input v-model="menu.url"/>
          </el-form-item>
          <el-form-item label="权限字段" size="small" prop="code">
            <el-input v-model="menu.code"/>
          </el-form-item>
          <el-form-item label="菜单图标" size="small">
            <my-icon @selecticon="setIcon" ref="child"></my-icon>
          </el-form-item>
          <el-form-item label="菜单序号" size="small" prop="orderNum">
            <el-input v-model="menu.orderNum"/>
          </el-form-item>
        </el-form>
      </div>
    </system-dialog>

    <!-- 选择上级菜单 -->
    <system-dialog :title="parentDialog.title" :width="parentDialog.width" :visible="parentDialog.visible"
                   @onParentClose="onParentClose()" @onParentConfirm="onParentClose()">
      <div slot="content">
        <el-tree style="font-size: 14px;" ref="parentTree" :data="parentMenuList" :props="defaultProps" empty-text="暂无数据"
                 :show-checkbox="false" default-expand-all :highlight-current="true" :expand-on-click-node="false"
          @node-click="handleNodeClick">
          <div class="custom-tree-node" slot-scope="{ node, data}">
            <span v-if="data.children.length === 0">
              <i class="el-icon-document"
                 style="color: white;font-size: 18px"/>
            </span>
            <span v-else @click.stop="changIcon(data)">
              <svg-icon v-if="data.open" icon-class="add-s"/>
              <svg-icon v-else icon-class="sub-s"/>
            </span>
            <span style="margin-left: 3px">{{node.label}}</span>
          </div>
        </el-tree>
      </div>

    </system-dialog>
  </el-main>
</template>

<script>
import systemDialog from "@/components/system/systemDialog.vue";
import menu from "@/api/menu";
import {style} from "svgo/lib/svgo/jsAPI";
import myIcon from "@/components/system/myIcon.vue";
import objCopy from "@/utils/objCopy";

  export default {
    name: "menuList",
    computed: {
      rules: {
          type:[{required: true, message: '请选择菜单类型', trigger: 'change'}],
          parentName:[{required: true, message: '请选择所属菜单名称', trigger: 'change'}],
          label:[{required: true, message: '请输入菜单名称', trigger: 'blur'}],
          path:[{required: true, message: '请输入路由路径', trigger: 'blur'}],
          url:[{required: true, message: '请输入组件路径', trigger: 'blur'}],
          code:[{required: true, message: '请输入权限字段', trigger: 'blur'}],
          name:[{required: true, message: '请输入路由名称', trigger: 'blur'}],
      }
    },
    components:{
      systemDialog,
      myIcon
    },
    data(){
      return{
        menuList:[],
        tableHeight:0,
        menuDialog:{
          title:"新增菜单",
          visible:false,
          width:630,
          height:270
        },
        menu:{
          id:"",
          type:"",
          parentId:"",
          parentName:"",
          label:"",
          path:"",
          icon:"",
          url:"",
          code:"",
          orderNum:"",
          name:""
        },
        parentDialog:{
          title: "选择所属菜单",
          width: 280,
          height: 450,
          visible: false
        },
        defaultProps:{
          children:'children',
          label:'label'
        },
        parentMenuList:[],

      }
    },
    created() {
      this.search()

    },
    mounted() {
      this.$nextTick(() => {
        this.tableHeight = window.innerHeight - 180
      })
    },
    //查询菜单列表
    methods:{
      style() {
        return style
      },
      openAddWindow(){
        this.$resetForm("menuForm",this.menu);
        this.menuDialog.title = "新增菜单";
        this.menuDialog.visible = true;
        this.$nextTick(() => {
          this.$refs["child"].userChooseIcon = ""
        })
      },

      async selectParentMenu(){
        this.parentDialog.visible = true;
        let res = await menu.getParentMenuList();
        if (res.success){
          this.parentMenuList = res.data;
        }
      },
      async search(){
        let res = await menu.getMenuList();
        if (res.success){
          this.menuList = res.data;
        }
      },
      onClose(){
        this.menuDialog.visible = false;
      },
      onConfirm(){
        //表单验证
        this.$refs.menuForm.validate(async (valid) => {
          if (valid) {
            let res = null;
            //判断当前是新增还是修改
            if (this.menu.id === "") {
              //发生添加请求
              res = await menu.addMenu(this.menu);
            } else {
              //发生修改请求
              res = await menu.updateMenu(this.menu)
            }

            if (res.success) {
              this.$message.success(res.message);
              await this.search();
              this.menuDialog.visible = false;
            } else {
              this.$message.error(res.message);
            }
            this.menuDialog.visible = false;
          }
        })
      },
      onParentClose(){
        this.menuDialog.visible = false;
      },
      onParentConfirm(){
        this.menuDialog.visible = false;
      },
      //切换图标
      changIcon(data){
        data.open = !data.open
        this.$refs.parentTree.store.nodesMap[data.id].expanded = !data.open
      },
      handleNodeClick(data){
        this.menu.parentId = data.id;
        this.menu.parentName = data.label;
      },
      setIcon(icon){
        this.menu.icon = icon;
      },
      onEdit(row){
        //回现数据
        this.$objCopy(row, this.menu);
        this.menuDialog.title = "编辑菜单";
        this.menuDialog.visible = true;
        this.$nextTick(() => {
          this.$refs["child"].userChooseIcon = row.icon
        })
      },
      async onDelete(row) {
        //判断是否存在子菜单
        let res = await menu.checkMenu({id: row.id});
        if(!res.success){
          //提示不能删除
          this.$message.warning(res.message);
        }else {
          let confirm = await this.$myconfirm("确定要删除吗？");
          if(confirm){
            //发送删除请求
            let res = await menu.deleteMenu({id: row.id});
            //判断是否成功
            if (res.success){
              this.$message.success(res.message);
              await this.search();
            }else {
              this.$message.error(res.message);
            }
          }
        }
      }
    }
  }
</script>

<style scoped lang="scss">

</style>
