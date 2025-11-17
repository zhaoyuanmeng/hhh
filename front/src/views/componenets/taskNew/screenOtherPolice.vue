<template>
  <div class="task_police_box">
    <div class="add_Node">
      <div class="add_Node_item" @click="addNodeFun">添加节点</div>
    </div>
    <el-collapse v-model="activeName" accordion @change="changeActive">
      <el-collapse-item :title="`${item}`" :name="item" v-for="(item, index) in activeList" :key="index">
        <div class="taskPolice">
          <div class="edit_box">
            <div class="edit_btn" @click="editDialog(item)" v-if="activeName === '基本情况'">
              编辑
            </div>
            <div v-else-if="activeName === '隐患排查'" style="display: flex; align-items: center">
              <div class="edit_btn" @click="editDialog(item)">编辑</div>
              <div @click="getCood1(item)" class="taskViewCoodr1"></div>
            </div>
            <div v-else style="display: flex; align-items: center">
              <div class="edit_btn" @click="deployment(item)" v-if="activeName === '警力部署'" style="margin-right: 8px">
                一键部警
              </div>
              <div class="edit_btn" @click="edit(item)">编辑</div>
              <div @click="getCood(item)" class="taskViewCoodr1"></div>
            </div>
          </div>
          <div class="data_content">
            <el-row v-if="activeName === '应急力量'">
              <el-col :span="24" v-for="(option, index) in policArr" :key="index">
                <div class="all_data" style="margin-bottom: 20px; padding: 16px">
                  <div class="item">
                    <div class="left">责任领导：{{ option.data.zrld }}</div>
                    <div class="right"></div>
                  </div>
                  <div class="item">
                    <div class="left">责任单位：{{ option.data.zrdw }}</div>
                    <div class="right">电话：{{ option.data.phone }}</div>
                  </div>
                  <div class="item">
                    <div class="left" style="width: 20% !important; flex: none !important">
                      备注信息：
                    </div>
                    <div class="right" v-html="option.data.bzxx"></div>
                  </div>
                  <div class="item">
                    <div class="left">
                      应急力量：{{ option.data.emergency }}
                    </div>
                    <div class="right"></div>
                  </div>
                  <div>
                    <div v-for="(yj, idx) in option.data.emergencyOfPosition">
                      <span>{{ yj.weizhi }}部署：</span>
                      <span>{{ yj.leixing }}</span>
                      <span style="color: aqua">{{ yj.num }}</span>人
                    </div>
                  </div>
                </div>
              </el-col>
            </el-row>
            <el-row v-if="activeName === '应急避险点' || activeName === '应急医院'">
              <el-col :span="24" v-for="(option, index) in policArr" :key="index">
                <div class="all_data" style="margin-bottom: 20px; padding: 16px">
                  <div class="item">
                    <div class="left">责任领导：{{ option.data.zrld }}</div>
                    <div class="right"></div>
                  </div>
                  <div class="item">
                    <div class="left">责任单位：{{ option.data.zrdw }}</div>
                    <div class="right">电话：{{ option.data.phone }}</div>
                  </div>
                  <div class="item">
                    <div class="left">
                      {{
                        activeName === "应急避险点" ? "应急避险点" : "应急医院"
                      }}：<span style="color: aqua">{{ option.data.bxd }}</span>
                    </div>
                    <div class="right"></div>
                  </div>
                  <div class="item">
                    <div class="left">联系人：{{ option.data.lxr }}</div>
                    <div class="right">联系方式：{{ option.data.lxfs }}</div>
                  </div>
                  <div class="item">
                    <div class="left" style=" flex: none">
                      {{
                        activeName === "应急避险点" ? "避险点" : "医院"
                      }}路线：
                    </div>
                    <div class="right" v-html="option.data.line"></div>
                  </div>
                  <!-- <div v-if="option.data.pointNames">
                    <div class="item" v-for="(p,point) in option.data.pointNames">
                      <div class="left">{{activeName}}{{point+1}}：<span style="margin-left: 4px;">{{ p }}</span></div>
                    </div>
                  </div>
                  <div v-if="option.data.lineNames">
                    <div class="item" v-for="(p,point) in option.data.lineNames">
                      <div class="left">{{activeName}}路线{{point+1}}：<span style="margin-left: 4px;">{{ p }}</span></div>
                    </div>
                  </div> -->
                </div>
              </el-col>
            </el-row>
            <el-row v-if="activeName === '基本情况' || activeName === '隐患排查'">
              <el-col :span="24">
                <div style="padding: 0 30px" v-html="collapseData.msg"></div>
              </el-col>
              <el-col :span="24">
                <div class="basicInfo_box" v-if="activeName === '基本情况'">
                  <div class="title_name">路线统计</div>
                  <div class="basicData">
                    路线距离：{{
                      Number(basicLines.totalLength).toFixed(2)
                    }}公里
                  </div>
                  <div class="title_name">路线详情</div>
                  <div class="basicData" v-for="(item, index) in basicLines.detail" style="cursor: pointer"
                    @click="jumpLine(item)">
                    {{ item.name }}（{{ Number(item.length).toFixed(2) }}公里）
                  </div>
                </div>
              </el-col>
            </el-row>
            <el-row v-if="activeName === '警力部署'" class="police_setting">
              <el-col :span="24" v-for="(option, index) in policArr" :key="index">
                <div class="all_data" style="margin-top: 16px">
                  <div class="basicinfo_box" style="border: 1px solid #274ccf;margin-bottom: 10px;padding: 8px;">
                    <div class="item">
                      <div class="left">责任领导：{{ option.data.zrld }}</div>
                      <div class="right"></div>
                    </div>
                    <div class="item">
                      <div class="left">责任单位：{{ option.data.zrdw }}</div>
                      <div class="right">电话：{{ option.data.phone }}</div>
                    </div>
                    <div class="item">
                      <div class="left" style="width: 20% !important; flex: none !important">
                        备注信息：
                      </div>
                      <div class="right" v-html="option.data.bzxx"></div>
                    </div>
                    <div class="item" style="align-items: baseline; margin-bottom: 10px">
                      <div class="left_1" style="width: 90px; color: #00ceff">
                        警力部署：
                      </div>
                      <div class="right_1" style="width: 100%">
                        共部署警力<span style="color: aqua">{{
                          option.policeTypeStatistics.total
                        }}</span>人， 其中现场执勤警力<span style="color: aqua">{{
  option.policeTypeStatistics.onduty
}}</span>人， 应急处突警力<span style="color: aqua">{{
  option.policeTypeStatistics.emergency
}}</span>人。<br /><span v-for="(all, a) in option.postStatistics" :key="a">
                          {{ all.post
                          }}<span style="color: aqua">{{ all.num }}</span>人<span
                            v-if="a !== option.postStatistics.length - 1">、</span></span>
                      </div>
                    </div>
                  </div>

                  <div class="child_box" v-for="(child, idx) in option.extDataList" :key="idx" style="
                      margin-bottom: 10px;
                      border: 1px solid #274ccf;
                      padding: 8px;
                    ">
                    <div class="fxmc" style="width: 100%;">
                      <div class="color_custom"
                        style="color: aqua;display: flex;align-items: center;justify-content: space-between;width:100%">
                        {{ child.policeData.fangxian }}
                        <div v-if="child.policeData.fangxian">
                          <el-icon style="margin-right: 4px;cursor: pointer;" v-if="!child.show" title="展开"
                            @click.stop="showHideCard(index, idx, true)">
                            <ArrowDownBold />
                          </el-icon>
                          <el-icon style="margin-right: 4px;cursor: pointer;" v-else>
                            <ArrowUpBold title="收缩" @click.stop="showHideCard(index, idx, false)" />
                          </el-icon>
                        </div>

                      </div>
                    </div>
                    <div class="fxmc_tj" style="margin-bottom: 8px" v-if="child.policeData.fangxian ||
                      !allGroupsHaveValues(child.policeData.groupData)
                      ">
                      共部署警力<span style="color: aqua">{{
                        child.policeData.policeTypeOfLine.total
                      }}</span>人，其中现场执勤警力<span style="color: aqua">{{
  child.policeData.policeTypeOfLine.onduty
}}</span>人、应急处突警力<span style="color: aqua">{{
  child.policeData.policeTypeOfLine.emergency
}}</span>人。<br /><span v-for="(all, a) in child.policeData.postStatistics" :key="a">
                        {{ all.post
                        }}<span style="color: aqua">{{ all.num }}</span>人<span v-if="a !== child.policeData.postStatistics.length - 1
  ">、</span></span>
                    </div>

                    <div class="group_list" v-for="(group, index) in child.policeData.groupData"
                      style="margin-bottom: 10px" v-show="child.show || !child.policeData.fangxian">
                      <div class="group_name" style="color: aqua; font-size: 16px" v-if="group.group">
                        {{ group.group }}
                        <div class="right_text" @click="editGroupFun(group)">
                          编辑
                        </div>
                      </div>
                      <div class="right_1" v-if="group.group" style="margin-bottom: 8px">
                        共部署警力<span style="color: aqua">{{
                          group.policeNum
                        }}</span>人，其中<span v-for="(types, index) in group.policeTypeOfGroup" :key="index">
                          {{ `${types.post}`
                          }}<span style="color: aqua">{{ types.num }}人</span><span
                            v-if="index !== group.policeTypeOfGroup.length - 1">、</span>
                        </span>
                      </div>
                      <div class="item" style="margin-bottom: 8px">
                        <div class="left" style="width: 24% !important; flex: none !important" v-if="group.groupDesc">
                          备注信息：
                        </div>
                        <div class="right" v-html="group.groupDesc"></div>
                      </div>
                      <div class="police_box">
                        <div v-for="(last, a) in group.jinglibushu" :key="a">
                          <div class="item_police" style="display: flex; cursor: pointer"
                            @click="focusMarkerShowHide(last)" :class="{ activeColor: clickId === last.id }">
                            <div class="left">{{ last.weizhi }}部署：</div>
                            <div class="right" style="display: flex; flex-wrap: wrap; flex: 1">
                              <span>{{ last.leixing }}</span><span class="color_custom">{{ last.num }}</span>人
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
      </el-collapse-item>
    </el-collapse>

    <!-- 自定义节点 -->
    <el-collapse v-model="activeName" accordion @change="changeActive">
      <el-collapse-item :title="`${item.name}`" :name="item.name" v-for="(item, index) in addActiveList" :key="index">
        <template #title>
          <div style="
              display: flex;
              align-items: center;
              justify-content: space-between;
              width: 95%;
            ">
            {{ item.name }}
            <el-icon @click.stop="delNode(item.id)">
              <Delete />
            </el-icon>
          </div>
        </template>
        <div class="taskPolice">
          <div class="edit_box">
            <div class="edit_btn" @click="editDialog(item)">编辑</div>
          </div>
        </div>
        <div class="data_content">
          <el-row>
            <el-col :span="24">
              <div style="padding: 0 30px" v-html="collapseData.msg"></div>
            </el-col>
          </el-row>
        </div>
      </el-collapse-item>
    </el-collapse>
  </div>
  <!-- 警力部署编辑弹框 -->
  <el-dialog v-model="openModal" width="686px" @close="cancel" align-center :destory-on-close="false"
    :close-on-click-modal="false" append-to-body class="my_Dialog_text">
    <template #header>
      <div class="heard_name">
        <div class="d_name">{{ activeName }}</div>
      </div>
    </template>
    <el-form ref="customForm" :inline="true" :model="dialogForm" :label-suffix="'：'" :label-width="100"
      class="form_custom_class" style="width: 95%">
      <el-row v-if="activeName === '应急避险点' || activeName === '应急医院'">
        <div class="dialog_police">
          <div class="left"></div>
          <div class="right" @click="y_a_import">应急预案库导入</div>
        </div>
      </el-row>
      <el-row v-if="openModal">
        <FormCustom v-for="(items, index) in dialogFormData" :key="index" :obj="dialogForm" :option="items"
          @changeCotrl="functionEvents" @changeDialogCtrl="functionEvents1" />
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer" style="text-align: center">
        <el-button type="primary" @click="submitForm" style="background: #274eef">确 定</el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog v-model="openBol" width="400px" @close="htmlData = ''" align-center :destory-on-close="false"
    :close-on-click-modal="false" append-to-body class="my_Dialog_text">
    <template #header>
      <div class="heard_name">
        <div class="d_name">{{ activeName }}</div>
      </div>
    </template>
    <div class="text_box">
      <WangEditor :initialValue="htmlData" :editorHeight="'250px'" @handleChange="(e) => {
          htmlData = e;
        }
        " v-if="openBol" />
      <!-- <el-input
        v-model="htmlData"
        :rows="10"
        type="textarea"
        placeholder="请输入..."
      /> -->
    </div>
    <template #footer>
      <div class="dialog-footer">
        <div class="sub_btn sure_btn" @click="submitBasicInfo">确定</div>
      </div>
    </template>
  </el-dialog>

  <!-- 应急预案库弹框列表 -->
  <el-dialog v-model="openYAModal" width="686px" @close="cancelYaModal" align-center :destory-on-close="false"
    :close-on-click-modal="false" append-to-body class="my_Dialog_text">
    <template #header>
      <div class="heard_name">
        <div class="d_name">应急预案库列表</div>
      </div>
    </template>
    <div class="table_list">
      <el-radio-group v-model="selectedRow" style="width: 100%">
        <el-table :data="tableData" style="width: 100%" height="400px" highlight-current-row>
          <el-table-column type="index" width="50"> </el-table-column>
          <el-table-column prop="name" label="名称"> </el-table-column>
          <el-table-column prop="type" label="类型" width="180">
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template #default="{ row }">
              <el-radio :value="row.id" @click="selectRow(row)">选择</el-radio>
            </template>
          </el-table-column>
        </el-table>
      </el-radio-group>
    </div>
    <template #footer>
      <div class="dialog-footer" style="text-align: center">
        <el-button type="primary" @click="getRowData" style="background: #274eef">确 定</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 新增节点 -->
  <el-dialog v-model="nodeBol" width="400px" @close="nodeName = ''" align-center :destory-on-close="false"
    :close-on-click-modal="false" append-to-body class="my_Dialog_text">
    <template #header>
      <div class="heard_name">
        <div class="d_name">添加节点</div>
      </div>
    </template>
    <div class="text_box">
      <el-input v-model="nodeName" placeholder="请输入节点名称..." />
    </div>
    <template #footer>
      <div class="dialog-footer">
        <div class="sub_btn sure_btn" @click="submitNodeName">确定</div>
      </div>
    </template>
  </el-dialog>

  <!-- 修改分组名称以及备注信息 -->
  <el-dialog v-model="groupBol" width="600px" align-center :destory-on-close="false" :close-on-click-modal="false"
    append-to-body class="my_Dialog_text">
    <template #header>
      <div class="heard_name">
        <div class="d_name">警力部署</div>
      </div>
    </template>
    <el-form ref="customFormGroup" :inline="true" :model="groupFrom" :label-suffix="'：'" :label-width="100"
      class="form_custom_class" style="width: 95%">
      <el-row>
        <el-col :span="24">
          <el-form-item label="分组名称" prop="group" :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
            style="width: 100%">
            <el-input v-model="groupFrom.group" placeholder="请输入..."></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注信息" style="width: 100%">
            <div style="flex: 1">
              <WangEditor :initialValue="groupFrom.groupDesc" :editorHeight="'200px'" @handleChange="handleChangeEdit"
                v-if="groupBol" />
            </div>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <div class="sub_btn sure_btn" @click="groupSubmit">确定</div>
      </div>
    </template>
  </el-dialog>

  <!-- 一键部警弹框 -->
  <el-dialog v-model="openOnePolice" width="700px" @close="closePoliceDialog" align-center :destory-on-close="false"
    :close-on-click-modal="false" append-to-body class="my_Dialog_text">
    <template #header>
      <div class="heard_name">
        <div class="d_name">一键部警</div>
      </div>
    </template>
    <div class="table_list_custom">
      <div class="item">
        <div class="yslx">要素类型：</div>
        <div class="policeType">警力类型：</div>
        <div class="policeNum">警力数量：</div>
      </div>
      <div class="item" v-for="(item, index) in savePoliceData" :key="index">
        <div class="yslx">
          <el-input v-model="item.featureTypName" disabled style="width: 100%"></el-input>
        </div>
        <div class="policeType">
          <el-select v-model="item.userData" placeholder="请选择警力类型" style="width: 100%" clearable>
            <el-option v-for="(item, index) in policeList" :key="index" :label="item.name" :value="item.name" />
          </el-select>
        </div>
        <div class="policeNum">
          <el-input v-model="item.num" placeholder="请输入..." style="width: 90px"
            @blur="validatePositiveInteger(event, index)"></el-input><span style="margin-left: 4px">人</span>
        </div>
      </div>
    </div>
    <!-- AI--分组 -->
    <div class="group-manager" style=" padding-top: 10px;">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
        <h3 style="margin: 0;color: #fff;">距离分组规则</h3>
        <el-button size="small" @click="handleAddGroup">+ 新增分组</el-button>
      </div>

      <!-- 分组列表 -->
      <div class="group-list">
        <div class="group-item header">
          <div class="col name">分组名称</div>
          <div class="col range">距离范围（公里）</div>
          <div class="col action">操作</div>
        </div>
        <div class="group-item" v-for="(rule, index) in groupRules" :key="index">
          <div class="col name">{{ rule.name }}</div>
          <div class="col range">{{ rule.start }} - {{ rule.end }}</div>
          <div class="col action">
            <el-button size="small" icon="Edit" @click="handleEditGroup(index)"></el-button>
            <el-button size="small" icon="Delete" type="danger" @click="handleDeleteGroup(index)"></el-button>
          </div>
        </div>
        <div v-if="groupRules.length === 0" class="empty-tip">
          暂无分组规则，点击"新增分组"添加
        </div>
      </div>

      <!-- 分组编辑表单 -->
      <el-dialog v-model="groupDialogVisible" title="编辑分组规则" class="group-edit-dialog" :close-on-click-modal="false"
        :close-on-press-escape="false" :append-to-body="true">
        <el-form :model="currentGroup" ref="groupForm" :rules="{
          name: [{ required: true, message: '请输入分组名称', trigger: 'blur' }],
          start: [{ required: true, type: 'number', message: '请输入起点', trigger: 'blur' }],
          end: [
            { required: true, type: 'number', message: '请输入终点', trigger: 'blur' },
            { validator: validateGroupEnd, trigger: 'blur' }
          ]
        }">
          <el-form-item label="分组名称" prop="name">
            <el-input v-model="currentGroup.name"></el-input>
          </el-form-item>
          <el-row :gutter="10">
            <el-col :span="12">
              <el-form-item label="起点距离" prop="start">
                <el-input v-model.number="currentGroup.start" suffix="米"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="终点距离" prop="end">
                <el-input v-model.number="currentGroup.end" suffix="米"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <template #footer>
          <el-button @click="groupDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveGroup">保存</el-button>
        </template>
      </el-dialog>
    </div>

    <!-- 分组编辑表单 -->
    <el-dialog v-model="groupDialogVisible" title="编辑分组规则" width="400px">
      <el-form :model="currentGroup" ref="groupForm" :rules="{
        name: [{ required: true, message: '请输入分组名称', trigger: 'blur' }],
        start: [{ required: true, type: 'number', message: '请输入起点', trigger: 'blur' }],
        end: [
          { required: true, type: 'number', message: '请输入终点', trigger: 'blur' },
          { validator: validateGroupEnd, trigger: 'blur' }
        ]
      }">
        <el-form-item label="分组名称" prop="name">
          <el-input v-model="currentGroup.name"></el-input>
        </el-form-item>
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="起点距离" prop="start">
              <el-input v-model.number="currentGroup.start" suffix="米"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="终点距离" prop="end">
              <el-input v-model.number="currentGroup.end" suffix="米"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- end -->
      <template #footer>
        <div class="dialog-footer" style="text-align: center">
          <el-button type="primary" @click="closePoliceDialog">取 消</el-button>
          <el-button type="primary" @click="addPoliceMarker" style="background: #274eef">创 建</el-button>
        </div>
      </template>
    </el-dialog>

    <template #footer>
      <div class="dialog-footer" style="text-align: center">
        <el-button type="primary" @click="closePoliceDialog">取 消</el-button>
        <el-button
          type="primary"
          @click="addPoliceMarker"
          style="background: #274eef"
          >创 建</el-button
        >
      </div>
    </template>
  </el-dialog>
</template>
<script setup>
import WangEditor from "@/components/WangEditor";
import JsonData from "./screenJson";
import FormCustom from "./formCustom.vue";
import {
  ref,
  computed,
  getCurrentInstance,
  reactive,
  onUnmounted,
  onBeforeUnmount,
  onMounted,
} from "vue";
import useTaskStore from "@/store/modules/taskStore";
import {
  searchNodePlanToScreen,
  saveScreenPlan,
  saveScreenPlanFX,
  getDrawDataForScreen,
  searchBasicLinesStatic,
  searchPlanNodeList,
  delPlanNodeList,
  updatePoliceGroup,
  getGSGtBasicList,
  quickToSetPolice,
} from "@/api/task/task";
import { sessionStorage } from "@/utils/storage";
import { getSceneDataForIdAndToDtaw } from "@/api/workCockpit/index.js";
import { drawLinesName } from "@/views/WorkCockpit/utils";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  getEmcyListdata,
  importYAData,
  getEmcyInfo,
} from "@/api/task/emergency";
import { getGroupRules, saveGroupRules, deleteGroupRule } from "@/api/groupRule";
import { dialogRules } from "@/utils/index";
import useFloorStore from "@/store/modules/floorStore";
import { drawScreenData } from "./util";
import { closeFloors } from "@/components/SmartMap/js/utils";
import { flattenTreeData, loadPicture } from "@/utils";
import useWorkCockpitStore from "@/store/modules/workCockpit.js";
import { drawContrlData } from "./util";
import emitter from "@/utils/emitter";
import { event } from "jquery";
const { proxy } = getCurrentInstance();
let policeBox = computed(() => useWorkCockpitStore().policeCheckBox);
onMounted(() => {
  emitter.on("refreshPolice", () => {
    console.log("刷新页面");
    activeName.value === "警力部署" && changeActive("警力部署");
  });
  getNodeList();
});
onBeforeUnmount(() => {
  emitter.off("refreshPolice");
  clearTimerFun();
});
onUnmounted(() => {
  emitter.off("refreshPolice");
  clearTimerFun();
});

// AI--分组
// 分组弹窗开关

// 验证终点大于起点
const validateGroupEnd = (rule, value, callback) => {
  if (value <= currentGroup.value.start) {
    callback(new Error('终点距离必须大于起点距离'));
  } else {
    callback();
  }
};

// 2. 新增分组
const handleAddGroup = () => {
  editingGroupIndex.value = -1;
  currentGroup.value = { id: '', name: '', start: 0, end: 0 };
  groupDialogVisible.value = true;
};

// 3. 编辑分组
const handleEditGroup = (index) => {
  editingGroupIndex.value = index;
  const rule = groupRules.value[index];
  currentGroup.value = { ...rule }; // 复制当前分组数据
  groupDialogVisible.value = true;
};

// 4. 删除分组（调用后端接口）
const handleDeleteGroup = async (index) => {
  const rule = groupRules.value[index];
  ElMessageBox.confirm('确定删除该分组规则吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteGroupRule(rule.id);
      if (res.code === 0) {
        groupRules.value.splice(index, 1); // 从列表中移除
        ElMessage.success('删除成功');
      } else {
        ElMessage.error(`删除失败：${res.msg}`);
      }
    } catch (error) {
      ElMessage.error('删除接口请求失败');
      console.error('删除分组异常：', error);
    }
  });
};

// 5. 保存分组（新增/更新，调用后端接口）
const saveGroup = async () => {
  proxy.$refs.groupForm.validate(async (valid) => {
    if (valid) {
      // 构造后端需要的数据格式
      const ruleData = {
        id: currentGroup.value.id || '', // 编辑时带id，新增时为空
        groupName: currentGroup.value.name,
        startDistance: currentGroup.value.start,
        endDistance: currentGroup.value.end
      };

      try {
        const res = await saveGroupRules(
          screenInfo.value.id,
          [ruleData] // 单条数据也用数组（保持后端批量处理能力）
        );

        if (res.code === 0) {
          if (editingGroupIndex.value === -1) {
            // 新增：添加后端返回的带ID的数据
            groupRules.value.push({
              id: res.data?.[0]?.id || '', // 后端生成的ID
              name: currentGroup.value.name,
              start: currentGroup.value.start,
              end: currentGroup.value.end
            });
            ElMessage.success('新增成功');
          } else {
            // 更新：直接更新本地列表（无需替换ID）
            groupRules.value[editingGroupIndex.value] = {
              ...groupRules.value[editingGroupIndex.value],
              name: currentGroup.value.name,
              start: currentGroup.value.start,
              end: currentGroup.value.end
            };
            ElMessage.success('更新成功');
          }
          groupDialogVisible.value = false;
        } else {
          ElMessage.error(`保存失败：${res.msg}`);
        }
      } catch (error) {
        ElMessage.error('接口请求失败，请重试');
      }
    }
  });
};

// end

let taskInfo = computed(() => useTaskStore().taskInfo); // 任务信息信息
const basicLines = ref({});
const screenInfo = computed(() => useTaskStore().screenModalInfo);
let showPoliceBtn = computed(() => {
  return screenInfo.value.type === "1" && screenInfo.value.basicDataId;
});
console.log(screenInfo.value, taskInfo.value, "有无我想要的信息");
const activeName = ref("");
const selectVal = ref("");
const collapseData = ref({});
//预案模块star
const openModal = ref(false);
const openYAModal = ref(false);
const tableData = ref([]);
const selectedRow = ref(null);
const selectedRowData = ref({});
const drawDataList = ref([]);
//预案模块end
const dialogForm = ref({});
const dialogFormData = ref([]);
const updateNodeId = ref(null);
const policArr = ref([]); // 警力部署数据
const activeList = ref([
  "基本情况",
  // "隐患排查",
  "警力部署",
  "应急力量",
  "应急避险点",
  "应急医院",
]);
const addActiveList = ref([]);
const openBol = ref(false);
const htmlData = ref("");
// 分组规则相关状态
const groupDialogVisible = ref(false); // 分组编辑弹窗开关
const groupRules = ref([]); // 存储所有分组规则
const currentGroup = ref({ name: '', start: 0, end: 0 }); // 当前编辑的分组
const editingGroupIndex = ref(-1); // 编辑索引，-1表示新增
const nodeBol = ref(false);
const nodeName = ref("");
const groupBol = ref(false);
const groupFrom = ref({
  group: "",
  ids: "",
  groupDesc: "",
});
// 一键部警信息
const openOnePolice = ref(false);
const policeList = ref([
  {
    src: loadPicture("./images/cloud/police/icon-1.png"),
    name: "固定哨",
  },
  {
    src: loadPicture("./images/cloud/police/icon-0.png"),
    name: "交通哨",
  },
  {
    src: loadPicture("./images/cloud/police/icon-2.png"),
    name: "随卫哨",
  },
  {
    src: loadPicture("./images/cloud/police/icon-3.png"),
    name: "社控哨",
  },
  {
    src: loadPicture("./images/cloud/police/icon-4.png"),
    name: "昼夜哨",
  },
  {
    src: loadPicture("./images/cloud/police/icon-5.png"),
    name: "制高点哨",
  },
  {
    src: loadPicture("./images/cloud/police/icon-7.png"),
    name:
      useTaskStore().taskInfo.taskLevel === "一级加强"
        ? "快反力量"
        : "机动力量",
  },
  {
    src: loadPicture("./images/cloud/police/wrj.png"),
    name: "无人机反制",
  },
]);
const gsgtBasicList = ref([]);
const savePoliceData = ref([]);
// 添加节点
const addNodeFun = () => {
  nodeBol.value = true;
};
const validatePositiveInteger = (event, index) => {
  console.log(event, index);
  // 使用正则表达式来检查是否为正整数
  const positiveIntegerRegex = /^\d+$/;
  if (!positiveIntegerRegex.test(savePoliceData.value[index].num)) {
    // 如果不是正整数，则提示用户并清空输入
    proxy.$modal.msgWarning("请输入正整数");
    savePoliceData.value[index].num = "";
  }
};
// 一键部警
// const deployment = (item) => {
//   console.log(item);
//   openOnePolice.value = true;
//   getGSGtBasicList({ id: screenInfo.value.basicDataId }).then((res) => {
//     console.log(res);
//     gsgtBasicList.value = res.data;
//     if (res.data?.length) {
//       let arrs = [];
//       res.data.forEach((element) => {
//         let obj = {
//           featureTypeId: element.typeid,
//           featureTypName: element.typename,
//           userData: "",
//           num: "",
//         };
//         arrs.push(obj);
//       });
//       savePoliceData.value = arrs;
//     } else {
//       savePoliceData.value = [];
//     }
//   });
// };
// AI
// 一键部警  
const deployment = (item) => {
  console.log(item);
  openOnePolice.value = true;

  let params = { sceneId: screenInfo.value.id, planNode: "警力部署" };
  searchNodePlanToScreen(params).then((res) => {
    getGSGtBasicList({ id: screenInfo.value.basicDataId }).then((basicRes) => {
      gsgtBasicList.value = basicRes.data;
      // 新增：加载分组规则
      loadGroupRules(screenInfo.value.id);
      if (res.data && res.data.length > 0 && res.data[0].extDataList && res.data[0].extDataList.length > 0) {
        // 已有数据,需要回显  
        policArr.value = res.data;
        updateNodeId.value = res.data[0].extDataList[0].id;  // 注意:ID在extDataList[0]中  

        // 创建Map存储已保存的数据  
        const savedDataMap = new Map();

        // 从 extDataList[0].data.features 中提取完整数据  
        const extData = res.data[0].extDataList[0];
        if (extData.data && extData.data.features) {
          extData.data.features.forEach(feature => {
            savedDataMap.set(feature.featureTypeId, {
              userData: feature.userData,
              num: feature.num
            });
          });
        }

        // 基于基础列表创建完整表单,合并已保存数据  
        if (basicRes.data?.length) {
          let arrs = [];
          basicRes.data.forEach((element) => {
            const savedData = savedDataMap.get(element.typeid);
            let obj = {
              featureTypeId: element.typeid,
              featureTypName: element.typename,
              userData: savedData ? savedData.userData : "",
              num: savedData ? savedData.num : "",
            };
            arrs.push(obj);
          });
          savePoliceData.value = arrs;
        } else {
          savePoliceData.value = [];
        }
      } else {
        // 无数据,初始化基础列表  
        updateNodeId.value = null;
        initBasicList(basicRes.data);
      }
    });
  });
};
// 1. 加载分组规则（从后端获取）
const loadGroupRules = async (sceneId) => {
  try {
    const res = await getGroupRules(sceneId);
    if (res.code === 0) {
      // 适配后端数据格式（后端字段：id, groupName, startDistance, endDistance）
      groupRules.value = res.data.map(rule => ({
        id: rule.id,
        name: rule.groupName,
        start: rule.startDistance,
        end: rule.endDistance
      }));
    } else {
      ElMessage.warning(`加载分组规则失败：${res.msg}`);
      groupRules.value = [];
    }
  } catch (error) {
    ElMessage.error('分组规则加载失败，请重试');
    console.error('加载分组规则异常：', error);
    groupRules.value = [];
  }
};
// 初始化基础列表的辅助函数  
const initBasicList = (data) => {
  if (data?.length) {
    let arrs = [];
    data.forEach((element) => {
      let obj = {
        featureTypeId: element.typeid,
        featureTypName: element.typename,
        userData: "",
        num: "",
      };
      arrs.push(obj);
    });
    savePoliceData.value = arrs;
  } else {
    savePoliceData.value = [];
  }
};

// end

// 显示隐藏面板
const showHideCard = (index, idx, bol) => {
  policArr.value[index].extDataList[idx].show = bol
}
// 创建警力
const addPoliceMarker = () => {
  if (savePoliceData.value?.length) {
    const filteredItems = savePoliceData.value.filter((item) =>
      Object.values(item).every(
        (value) => value !== null && value !== undefined && value !== ""
      )
    );
    dataCreat(filteredItems);
  }
};
// const dataCreat = (arr) => {
//   if (arr?.length) {
//     console.log(arr);
//     let params = {
//       taskId: taskInfo.value.id ? taskInfo.value.id : 'test',
//       sceneId: screenInfo.value.id,
//       basicDataId: screenInfo.value.basicDataId,
//       policeData: getPoliceMarker(arr),
//     };
//     quickToSetPolice(params).then((res) => {
//       // 数据存缓存
//       // let sessions = params.policeData.map(item=>item.drawData)
//       //   setLocalstrong(sessions)
//       if (res.code === 0) {
//         proxy.$modal.msgSuccess("部警成功");
//         activeName.value === "警力部署" && changeActive("警力部署");
//         // 把刚刚绘制的警力id传过去 以及绘制添加的警力图标
//         drawNewPoliceData(res.data);
//         let ids = res.data.map((item) => item.id);
//         emitter.emit("refreshResource", { name: "yjbj", idArr: ids });
//         openOnePolice.value = false;
//       }
//     });
//   }
// };

// AI
const dataCreat = (arr) => {
  if (arr?.length) {
    console.log(arr);
    // 保存分组规则到本地存储
    sessionStorage.set(`groupRules_${screenInfo.value.id}`, JSON.stringify(groupRules.value));
    let params = {
      taskId: taskInfo.value.id ? taskInfo.value.id : 'test',
      sceneId: screenInfo.value.id,
      basicDataId: screenInfo.value.basicDataId,
      policeData: getPoliceMarker(arr), // 已包含 source: 'auto' 标识  
      // 不传递 groupRules  
      // groupRules: groupRules.value, // 提交分组规则
      groupRules: groupRules.value.map(rule => ({
        id: rule.id,
        groupName: rule.name,
        startDistance: rule.start,
        endDistance: rule.end
      }))
    };

    // 如果有updateNodeId,说明是更新操作  
    if (updateNodeId.value) {
      params.id = updateNodeId.value;
    }

    quickToSetPolice(params).then((res) => {
      if (res.code === 0) {
        proxy.$modal.msgSuccess(updateNodeId.value ? "更新成功" : "部警成功");
        activeName.value === "警力部署" && changeActive("警力部署");
        drawNewPoliceData(res.data);
        let ids = res.data.map((item) => item.id);
        emitter.emit("refreshResource", { name: "yjbj", idArr: ids });
        openOnePolice.value = false;
      }
    });
  }
};
// end
// 绘制刚刚部署的警力
const drawNewPoliceData = async (arrs) => {
  console.log(arrs);
  let g = window.__g;
  let markers = [];
  let uavList = [];
  let uavs = [];
  for (const item of arrs) {
    if (item.data.marker) {
      markers.push(item.data.marker);
    }
    if (item.data.customData) {
      uavList.push(item.data.customData);
    }
    if (item.data.uavData) {
      item.data.uavData.coordinate = item.data.marker.coordinate;
      uavs.push(item.data.uavData);
    }
  }
  let objList = uavList.map((item, index) => {
    return {
      ...item,
      location: markers[index].coordinate,
    };
  });
  g.radiationPoint.add(uavs);
  await g.marker3d.add(markers);
  await g.customObject.add(objList);
  // 进行贴合
  let markerAndObject = [];
  // 固定哨修改
  let changeMove = [];
  markers.forEach((item, index) => {
    if (item.userData === "固定哨") {
      changeMove.push({
        id: objList[index].id,
        functionName: "动画类型",
        parameters: [{ paramType: 5, paramValue: "站立" }],
      });
    }
    markerAndObject.push({
      marker3dId: item.id, //标注id
      objectId: objList[index].id, //自定义对象id
      offset: item.groupId === "uav" ? [0, 0, 1] : [0, 0, 2], //偏移量
    });
  });
  if (markerAndObject?.length) {
    g.marker3d.setAttachCustomObject(markerAndObject);
  }
  if (changeMove?.length) {
    g.customObject.callBPFunction(changeMove);
  }
};
//  封装警力数据
const getPoliceMarker = (arr) => {
  let list = [];
  let counter = 0;
  for (const item of arr) {
    let markerInfo;
    if (item.userData === "无人机反制") {
      let markerObj = {
        id: `police${new Date().getTime()}${++counter}`,
        groupId: "uav",
        userData: "无人机反制",
        source: 'auto', // 新增标识  
        coordinate: [0, 0, 0],
        coordinateType: 0,
        text: "无人机",
        textSize: 120,
        textColor: "#000080",
        textOutlineSize: 1,
        textOutlineColor: Color.Black,
        textFixed: false,
        fixedSize: true,
        textVisible: true,
        textLocation: [0, 0, 0.1],
        textRotation: [0, 90, 0],
        textScale: [1, 1, 1],
        pointName: "/JC_CustomAssets/EffectLibrary/Exhibition/Point/Point_A",
        pointVisible: true,
        pointScale: 1,
        range: [0, 2000],
        autoHeight: false,
        collision: true,
      };
      let uavObj = {
        id: markerObj.id,
        coordinate: [0, 0, 0],
        coordinateType: 0,
        radius: 100,
        rippleNumber: 5,
        color: "#130FEB",
        intensity: 0.5,
        autoHeight: false,
        userData: "无人机",
        groupId: "uav",
      };
      let customObj = {
        id: markerObj.id,
        pakFilePath: "@path:DTS_Library_6.1_240731.pak",
        assetPath: "/JC_CustomAssets/ObjectLibrary/Exhibition/交通工具/其他/无人机_1",
        location: [0, 0, 0],
        coordinateType: 0,
        rotation: [0, 0, 0],
        range: [0, 10000],
        groupId: "uav",
        userData: markerObj.id,
        localRotation: [0, 0, 0],
        scale: [1, 1, 1],
        isEffectRotation: true,
        smoothMotion: 1,
        supportAttach: true,
        visible: true,
      };
      let allObj = {
        id: markerObj.id,
        info: { num: Number(item.num), weizhi: "", fangxian: "" },
        customData: customObj,
        marker: markerObj,
        uavData: uavObj,
      };
      markerInfo = allObj;
    } else {
      let markerObj = {
        id: `police${new Date().getTime()}${++counter}`,
        userData: item.userData,
        source: 'auto', // 新增标识  
        groupId: "police",
        coordinate: [0, 0, 0],
        coordinateType: 0,
        text: item.userData,
        textSize: 120,
        textColor: "#000080",
        textOutlineSize: 1,
        textOutlineColor: Color.Black,
        textFixed: false,
        fixedSize: true,
        textVisible: true,
        textLocation: [0, 0, 0.1],
        textRotation: [0, 90, 0],
        textScale: [1, 1, 1],
        pointName: "/JC_CustomAssets/EffectLibrary/Exhibition/Point/Point_A",
        pointVisible: true,
        pointScale: 1,
        range: [0, 2000],
        autoHeight: false,
        collision: true,
      };
      let path, pak;
      if (item.userData === "交通哨") {
        pak = "@path:人物打包.pak";
        path = "/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/交警";
      } else if (item.userData === "快反力量" || item.userData === "机动力量") {
        pak = "@path:人物打包.pak";
        path = "/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/特种兵";
      } else if (item.userData === "固定哨") {
        pak = "@path:DTS_Library_6.1_240731.pak";
        path = "/JC_CustomAssets/RoleLibrary/Exhibition/动态人物/男角色_1";
      } else {
        pak = "@path:人物打包.pak";
        path = "/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/警察";
      }
      let customObj = {
        id: markerObj.id,
        pakFilePath: pak,
        assetPath: path,
        location: [0, 0, 0],
        coordinateType: 0,
        rotation: [0, 0, 0],
        range: [0, 10000],
        groupId: "police",
        userData: markerObj.id,
        localRotation: [0, 90, 0],
        scale: [1.3, 1.3, 1.3],
        isEffectRotation: true,
        smoothMotion: 1,
        supportAttach: true,
        visible: true,
      };
      let allObj = {
        id: markerObj.id,
        info: { num: Number(item.num), weizhi: "", fangxian: "" },
        customData: customObj,
        marker: markerObj,
      };
      markerInfo = allObj;
    }
    let obj = {
      drawData: {
        planNode: "警力部署",
        policeType:
          item.userData === "快反力量" || item.userData === "机动力量"
            ? "应急处突警力"
            : "执勤警力",
        type: markerInfo.marker.groupId,
        data: markerInfo,
      },
      featureTypeId: item.featureTypeId ? item.featureTypeId : null,
      featureTypName: item.featureTypName ? item.featureTypName : null,
      userData: item.userData ? item.userData : null,
      num: item.num ? item.num : null,
      source: 'auto', // 在最外层也添加标识  
    };
    list.push(obj);
  }
  return list;
};

const extractImagePath = (url) => {
  const parts = url.split("/images/");
  return parts.length > 1 ? "/images/" + parts[1] : null;
};
// 绘制场景标绘数据
const keepImagesAndAfter = (url) => {
  return extractImagePath(url);
};
const closePoliceDialog = () => {
  openOnePolice.value = false;
};
//判断是否需要头部统计
const allGroupsHaveValues = (objects) => {
  return objects.every(
    (obj) => obj.group !== null && obj.group !== undefined && obj.group !== ""
  );
};
// 编辑警力、应急力量分组以及备注信息
const editGroupFun = (item) => {
  console.log(item);
  groupBol.value = true;
  let idArr = item.jinglibushu.map((item) => item.id);
  groupFrom.value.group = item.group;
  groupFrom.value.ids = idArr.join(",");
  groupFrom.value.groupDesc = item.groupDesc;
};
const handleChangeEdit = (value) => {
  console.log("子组件触发事件", value);
  groupFrom.value.groupDesc = value;
};
// 确定分组
const groupSubmit = () => {
  proxy.$refs["customFormGroup"].validate((valid) => {
    if (valid) {
      console.log(groupFrom.value);
      updatePoliceGroup(groupFrom.value).then((res) => {
        if (res.code === 0) {
          ElMessage.success("已更新");
          changeActive("警力部署");
          groupBol.value = false;
        }
      });
    }
  });
};
// 删除节点
const delNode = (item) => {
  console.log(item);
  ElMessageBox.confirm("您确定要删除该节点?", "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      delPlanNodeList({ id: item }).then((res) => {
        if (res.code === 0) {
          proxy.$modal.msgSuccess("删除成功！");
          getNodeList();
          activeName.value = "";
        }
      });
    })
    .catch(() => {
      console.log("已取消");
    });
};
const getNodeList = () => {
  if (screenInfo.value.id) {
    let params = {
      sceneId: screenInfo.value.id,
      excludePlanNodes: activeList.value.join(","),
    };
    searchPlanNodeList(params).then((res) => {
      if (res.data?.length) {
        let datas = res.data.map((item) => {
          return { id: item.id, name: item.planNode };
        });
        // addActiveList.value = res.data.map(item=>item.planNode)
        addActiveList.value = datas;
      } else {
        addActiveList.value = [];
      }
    });
  }
};
// 添加节点名
const submitNodeName = () => {
  if (!nodeName.value) return proxy.$modal.msgWarning("节点名称不能为空");
  let data = {
    sceneId: screenInfo.value.id,
    planNode: nodeName.value,
  };
  saveScreenPlan(data).then((res) => {
    if (res.code === 0) {
      proxy.$modal.msgSuccess("添加成功");
      getNodeList();
      nodeBol.value = false;
    }
  });
};
// 具体岗哨警力部署警力信息地图联动
const markerTimer = ref(null);
const showFlag = ref(true);
const clickId = ref("");
const focusMarkerShowHide = async (item) => {
  let data = item;
  if (clickId.value === data.id) {
    let g = window.__g;
    clickId.value = "";
    return;
  }
  let g = window.__g;
  let info = await g.marker3d.get(data.id);
  if (info.result !== 0) {
    let str = `数据未绘制或者楼层内数据`;
    ElMessage.warning(str);
    return;
  }
  await g.marker3d.focus(data.id, 3, 1);
  clickId.value = data.id;
};
const clearTimerFun = async () => {
  let g = window.__g;
  if (clickId.value) {
    await g.marker.show(clickId.value);
    clickId.value = "";
  }
  if (markerTimer.value) {
    clearInterval(markerTimer.value);
    markerTimer.value = null;
    showFlag.value = true;
  }
};
const getCood1 = async (item) => {
  let g = window.__g;
  let cood = await g.camera.get();
  console.log(cood);
  let coodr = { viewData: cood.camera };
  let obj = collapseData.value;
  if (obj.viewData) {
    delete obj.viewData;
  }
  let data = {
    sceneId: screenInfo.value.id,
    planNode: item,
    data: { ...obj, ...coodr },
    id: updateNodeId.value || "",
  };
  saveScreenPlan(data).then((res) => {
    if (res.code === 0) {
      ElMessage.success(`${item}视角保存成功`);
      changeActive(item);
    }
  });
};
const getCood = async (item) => {
  let g = window.__g;
  let cood = await g.camera.get();
  console.log(cood);
  let coodr = { viewData: cood.camera };
  let obj = dialogForm.value;
  if (obj.viewData) {
    delete obj.viewData;
  }
  let data = {
    sceneId: screenInfo.value.id,
    planNode: item,
    data: { ...obj, ...coodr },
    id: updateNodeId.value || "",
  };
  saveScreenPlan(data).then((res) => {
    if (res.code === 0) {
      ElMessage.success(`${item}视角保存成功`);
      changeActive(item);
    }
  });
};

const changeActive = (e) => {
  clearTimerFun();
  if (e) {
    if (e === "基本情况") {
      searchBasicLinesStatic({ sceneId: screenInfo.value.id }).then((res) => {
        basicLines.value = res.data;
      });
    }
    let params = { sceneId: screenInfo.value.id, planNode: e };
    selectVal.value = e;
    searchNodePlanToScreen(params).then((res) => {
      if (res.data && res.data.length > 0) {
        updateNodeId.value = res.data[0].id;
        collapseData.value = res.data[0].data;
        if (res.data[0].data.viewData) {
          let g = window.__g;
          g.camera.set(res.data[0].data.viewData, 1.5);
        }
        if (e !== "基本情况" || e !== "隐患排查") {
          if (res.data[0].data) {
            dialogForm.value = res.data[0].data;
          }
          dialogFormData.value = JsonData[e];
          if (res.data?.length) {
            for (const item of res.data) {
              for (const child of item.extDataList) {
                child.show = false
              }
            }
          }
          policArr.value = res.data;
        }
      } else {
        collapseData.value = {};
        updateNodeId.value = null;
        dialogForm.value = {};
        dialogFormData.value = [];
        policArr.value = [];
      }
    });
  }
};

// 应急预案库导入
const y_a_import = () => {
  openYAModal.value = true;
  console.log(screenInfo.value, activeName.value);
  getEmcyListdata({
    basicDataId: screenInfo.value.basicDataId,
    type: activeName.value,
  }).then((res) => {
    console.log(res);
    tableData.value = res.data;
  });
};

const selectRow = (row) => {
  console.log(row);
  selectedRowData.value = row.data;
  getEmcyInfo({ id: row.id }).then((res) => {
    console.log(res);
    if (res.data.drawDataList?.length) {
      let arr = [];
      for (const item of res.data.drawDataList) {
        let obj = {
          taskId: screenInfo.value.taskId,
          sceneId: screenInfo.value.id,
          planNode: activeName.value,
          type: item.type,
          data: item.data,
        };
        arr.push(obj);
      }
      drawDataList.value = arr;
    } else {
      ElMessage.warning("当前方案下没有标绘数据");
      drawDataList.value = [];
    }
  });
};
const getRowData = () => {
  console.log(selectedRowData.value);
  dialogForm.value = selectedRowData.value;
  cancelYaModal();
};
const cancelYaModal = () => {
  openYAModal.value = false;
  selectedRow.value = null;
  tableData.value = [];
  selectedRowData.value = {};
};

const editDialog = (item) => {
  openBol.value = true;
  htmlData.value = collapseData.value?.msg;
};
const submitBasicInfo = () => {
  console.log(collapseData.value);
  if (!htmlData.value) return proxy.$modal.msgWarning("不能为空");
  let objs;
  if (collapseData.value.viewData) {
    objs = { msg: htmlData.value, viewData: collapseData.value.viewData };
  } else {
    objs = { msg: htmlData.value };
  }
  let data = {
    sceneId: screenInfo.value.id,
    // planNode: '基本情况',
    planNode: activeName.value,
    data: objs,
    id: updateNodeId.value,
  };
  saveScreenPlan(data).then((res) => {
    if (res.code === 0) {
      proxy.$modal.msgSuccess("编辑成功");
      changeActive(activeName.value);
      openBol.value = false;
    }
  });
};
const edit = (item) => {
  console.log(item, collapseData.value);
  openModal.value = true;
  // 封装数据
  getData(JsonData[item], collapseData.value);
};
const submitForm = () => {
  proxy.$refs["customForm"].validate((valid) => {
    if (valid) {
      if (drawDataList.value?.length) {
        importYAData(drawDataList.value).then((res) => {
          if (res.code === 0) {
            drawDataList.value = [];
            // 更新资源
            emitter.emit("refreshResource", "update");
          }
        });
      }
      let data = {
        sceneId: screenInfo.value.id,
        planNode: activeName.value,
        data: dialogForm.value,
        id: updateNodeId.value,
      };
      saveScreenPlan(data).then((res) => {
        if (res.code === 0) {
          openModal.value = false;
          changeActive(activeName.value);
        }
      });
    }
  });
};
const cancel = () => {
  resetForm("customForm");
  dialogForm.value = dialogForm.value ? dialogForm.value : {};
  dialogFormData.value = dialogFormData.value ? dialogFormData.value : [];
  changeActive(activeName.value);
};
// 表单重置
const resetForm = (refName) => {
  if (proxy.$refs[refName]) {
    proxy.$refs[refName].resetFields();
  }
};
const functionEvents = () => { };
const functionEvents1 = () => { };
const getData = (arr, formObj) => {
  if (arr === undefined || arr === null || arr.length === 0) {
    return;
  }
  let param = [];
  arr.map((item) => {
    let obj = item;
    if (item.required) {
      if (item.zdlx === 0 || item.zdlx === 3 || item.zdlx === 4) {
        obj.dialogRules = dialogRules("blur");
      }
      if (item.zdlx === 1 || item.zdlx === 2) {
        obj.dialogRules = dialogRules("change");
      }
    }
    if (formObj) {
      dialogForm.value = formObj;
    } else {
      if (item.yxdx) {
        dialogForm.value[item.cczd] = [];
      } else {
        dialogForm.value[item.cczd] = "";
      }
    }
    param.push(obj);
  });
  dialogFormData.value = param;
};
// 路线跳转
const jumpLine = async (item) => {
  let g = window.__g;
  let res = await g.polyline.get(item.id);
  if (res.result === 0) {
    g.polyline.focus(item.id, 50, 0.5);
  } else {
    ElMessage.warning("地图上暂无该路线，请先勾选绘制");
  }
};
</script>
<style lang="scss" scope>
.task_police_box {
  margin-top: 7px;

  .add_Node {
    display: flex;
    align-items: center;
    justify-content: end;
    margin-bottom: 10px;
    margin-right: 20px;

    .add_Node_item {
      height: 30px;
      width: 80px;
      text-align: center;
      line-height: 30px;
      background: rgba(35, 101, 255, 0.898);
      cursor: pointer;
      font-size: 14px;
    }
  }

  .el-collapse-item__header {
    background: rgba(28, 53, 155, 0.5) !important;
    padding-left: 40px !important;
  }

  .el-collapse-item__arrow {
    position: absolute;
    left: 20px !important;
  }

  .edit_box {
    display: flex;
    align-items: center;
    justify-content: end;
    margin-right: 20px;
    margin-top: 16px;

    .edit_btn {
      cursor: pointer;
      width: 84px;
      height: 32px;
      background: #274eef;
      line-height: 32px;
      text-align: center;
      right: 20px;
    }
  }

  .form_custom_class {
    .el-form-item__label {
      color: #fff !important;
    }
  }

  .data_content {
    .show_data {
      display: flex;
      padding: 0 20px;
      height: 30px;
      align-items: center;

      .left_name {
        min-width: 60px;
      }
    }
  }

  .police_setting {
    padding: 0 16px;

    .fxmc {
      font-weight: bold;
      font-size: 16px;
      display: flex;
      align-items: center;
      justify-content: space-between;

      .edit_btn {
        width: 60px;
        height: 25px;
        line-height: 25px;
        text-align: center;
        color: #fff;
        background: #386ef2;
        cursor: pointer;
        font-size: 12px;
      }
    }

    .phone_p {
      display: flex;
      align-items: center;
      height: 24px;

      .policeSet {
        flex: 1;
        font-size: 14px;
      }
    }

    .unit {
      line-height: 24px;
    }

    .police_box {
      .title_police {
        font-size: 14px;
        line-height: 24px;
      }

      .police_item {
        display: flex;
        align-items: center;
        padding-left: 30px;
        height: 30px;

        .location {
          width: 50%;
        }

        .policeName {
          width: 30%;
        }

        .policeNum {
          flex: 1;
        }
      }
    }
  }
}

.dialog_police {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  color: #fff;
  margin-bottom: 12px;

  .left {
    padding-left: 30px;
  }

  .right {
    width: 95px;
    height: 25px;
    opacity: 1;
    background: #386ef2;
    text-align: center;
    line-height: 25px;
    cursor: pointer;
  }
}

.dialog_police_item {
  width: 100%;
  display: flex;
  align-items: center;
  height: 36px;
  margin-bottom: 12px;
  padding-left: 100px;
  color: #fff;

  .left {
    width: 55%;
  }

  .center {
    width: 30%;
  }

  .right {
    flex: 1;
  }
}

.activeColor {
  color: #00ceff;
  // background: #00CEFF;
  // color:#00CEFF;
}

.taskViewCoodr1 {
  width: 20px;
  height: 20px;
  background: url("@/assets/basic/视角锁定@1x.png") no-repeat;
  background-size: 100% 100%;
  cursor: pointer;
  margin-left: 16px;
}

.basicInfo_box {
  padding: 0px 30px;

  .title_name {
    color: #00ceff;
    font-weight: bold;
    font-size: 14px;
    margin-top: 10px;
  }

  .basicData {
    color: #fff;
    font-size: 14px;
    margin-top: 6px;
  }
}

.group_list {
  .group_name {
    display: flex;
    align-items: center;
    justify-content: space-between;

    .right_text {
      font-size: 13px;
      margin-right: 4px;
      cursor: pointer;
    }
  }
}

.all_data {
  .item {
    display: flex;
    align-items: center;

    .left {
      flex: 1;
    }

    .right {
      flex: 1;
    }
  }
}
</style>
<style lang="scss">
.my_Dialog_text {
  .el-dialog__body {
    overflow: visible !important;
  }
}

// 分组编辑弹窗整体样式
.group-edit-dialog {
  background: #151c5d; // 背景色
  color: #fff; // 文字颜色

  .el-dialog__header {
    border-bottom: 1px solid #0f1554;

    .el-dialog__title {
      color: #fff;
      font-size: 16px;
    }
  }

  .el-dialog__body {
    padding: 20px;

    .el-form {
      .el-form-item {
        margin-bottom: 15px;

        .el-form-item__label {
          color: #b0b6ff; // 标签文字颜色（浅紫色，与背景区分）
        }

        .el-input__inner {
          background: #0f1554; // 输入框背景色
          border: 1px solid #1a227d;
          color: #fff;

          &::placeholder {
            color: #b0b6ff;
          }
        }

        .el-form-item__error {
          color: #ef4444;
        }
      }
    }
  }

  .el-dialog__footer {
    border-top: 1px solid #0f1554;

    .el-button {
      background: #0f1554;
      border: 1px solid #1a227d;
      color: #fff;

      &:hover {
        background: #1a227d;
      }

      &.el-button--primary {
        background: #165dff;
        border-color: #165dff;

        &:hover {
          background: #3b82f6;
        }
      }
    }
  }
}

// 分组列表样式（与基础列表风格统一）
.group-list {
  border-radius: 4px;
  max-height: 150px;
  overflow-y: auto;
  margin-top: 10px;

  .group-item {
    display: flex;
    padding: 10px 15px;
    font-size: 14px;

    &:last-child {
      border-bottom: none;
    }

    &.header {
      font-weight: 500;
      background: #151c5d; // 背景色
      color: #fff; // 文字颜色
    }

    .col {
      line-height: 32px; // 与输入框高度对齐

      &.name {
        flex: 2;
        color: #fff;
      }

      &.range {
        flex: 2;
        color: #fff;
      }

      &.action {
        flex: 1;
        text-align: right;
      }
    }
  }

  .empty-tip {
    padding: 20px;
    text-align: center;
    color: #fff;
    font-size: 14px;
    background: #151c5d;
  }
}

.edit_fx_name {
  font-size: 16.91px;
  margin: 10px 0;
  color: #ffffff;
  padding-left: 24px;
  padding-bottom: 10px;
}

:deep(.table_list) {
  .item {
    display: flex;
    height: 30px;
    align-items: center;

    .yslx,
    .policeType {
      width: 40%;
      margin-right: 20px;
    }

    .policeNum {
      flex: 1;
    }
  }
}

.table_list_custom {
  max-height: 400px;
  overflow: auto;

  .el-input {
    border: 1px solid #5b6799;
    border-radius: 2px;

    .el-input__wrapper {
      background: rgba(0, 12, 78, 0.5);
      box-shadow: none;
    }

    .el-input__inner {
      font-size: 14px;
      color: #ffffff;
      opacity: 1;
    }
  }

  .el-input.is-disabled .el-input__inner {
    -webkit-text-fill-color: #fff;
  }

  .el-select__wrapper {
    background: rgba(0, 12, 78, 0.5);
    box-shadow: none;
    border: 1px solid #5b6799;

    .el-select__selected-item {
      color: #ffffff;
      opacity: 1;
    }

    .is-transparent {
      color: #a8abb2;
    }
  }
}

.table_list_custom .item {
  display: flex;
  height: 36px;
  align-items: center;
  color: #fff;
}

.table_list_custom .item .yslx {
  width: 40%;
  margin-right: 20px;
}

.table_list_custom .policeType {
  width: 30%;
  margin-right: 20px;
}

.table_list_custom .policeNum {
  flex: 1;
}
</style>
