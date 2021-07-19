<template>
  <div class="app-container">
    <el-steps :active="stepsIndex" finish-status="success">
      <el-step title="基本信息"></el-step>
      <el-step title="商品参数"></el-step>
      <el-step title="规格属性"></el-step>
      <el-step title="SKU信息"></el-step>
      <el-step title="保存完成"></el-step>
    </el-steps>
    <div>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <!--基本信息-->
        <div v-if="stepsIndex == 0">
          <div class="form-step-content">
            <el-form-item label="商品名称" prop="itemName" :required="true">
              <el-input v-model="form.itemName" maxlength="200" placeholder="请输入"></el-input>
            </el-form-item>
            <el-form-item label="关键词" prop="keywords">
              <el-input v-model="form.keywords" maxlength="120" placeholder="请输入"></el-input>
            </el-form-item>
            <el-form-item label="分类" prop="catId" :required="true">
              <category-cascader
                :paths="catIdPath"
                @change="catIdChange"
                custom-class="w100"
              ></category-cascader>
            </el-form-item>
            <el-form-item label="品牌" prop="brandId" :required="true">
              <el-select
                v-model="form.brandId"
                clearable
                class="w100"
              >
                <el-option
                  v-for="item in brandOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="促销语" prop="promotion">
              <el-input v-model="form.promotion" placeholder="请输入"></el-input>
            </el-form-item>
            <el-form-item label="页面描述" prop="pageDesc">
              <el-input v-model="form.pageDesc" placeholder="请输入"></el-input>
            </el-form-item>
            <el-form-item label="商品图片" prop="imgs">
              <el-input v-model="form.imgs" placeholder="请输入"></el-input>
            </el-form-item>
            <el-form-item label="商品详情" prop="itemDesc">
              <tinymce v-model="form.itemClob.itemDesc" :height="300" />
            </el-form-item>
          </div>
        </div>
        <!--商品参数-->
        <div v-else-if="stepsIndex == 1">
          <div class="form-step-content">
            <el-tabs
              :active-name="tabsIndex"
              tab-position="left"
              @tab-click="handleTabsClick"
            >
              <!--              <el-tab-pane label="用户管理" name="0">用户管理</el-tab-pane>-->
              <!--              <el-tab-pane label="配置管理" name="1">配置管理</el-tab-pane>-->
              <el-tab-pane v-for="(item, index) in featureGroupOptions" :key="index" :label="item.label"
                           :name="index + '-' + index">
                <el-form-item v-for="(it, idx) in item.featureList" :key="index + '-' + idx" :label="it.label">
                  <el-select
                    v-model="form.paraList[it.realParaIndex].paraValue"
                    clearable
                    :multiple="it.inputType === 1"
                    @change="((value)=>{paraListChange(value, it.realParaIndex)})"
                    class="w100"
                  >
                    <el-option
                      v-for="(it2, idx2) in strToSelectOptions(it.selectValues)"
                      :key="it2.value + '-' + idx2"
                      :value="it2.value"
                      :label="it2.label"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-tab-pane>
            </el-tabs>
          </div>
        </div>
        <!--规格属性-->
        <div v-else-if="stepsIndex == 2">
          <div class="form-step-content">
            <div>请选择规格属性</div>
            <el-form-item
              v-for="(item, index) in specOptions"
              :key="index"
              :label="item.label"
            >
              <el-checkbox
                v-model="selectedSpecArr[index].selected[idx]"
                v-for="(it, idx) in strToSelectOptions(item.selectValues)"
                :key="index + '-' + idx"
                :label="it.label"
                :checked="selectedSpecArr[index].selected[idx]"
                @change="((value)=>{sepcListChange(value, index, idx)})"
              ></el-checkbox>
            </el-form-item>
          </div>
        </div>
        <!--SKU信息-->
        <div v-else-if="stepsIndex == 3">
          <div class="form-step-content">
            <el-table :data="form.skuList">
              <el-table-column type="index"></el-table-column>
              <el-table-column
                v-for="(item, index) in specOptions"
                :key="index"
                :label="item.featureName"
              >
                <template slot-scope="scope">
                  {{scope.row.specList[index].value}}
                </template>
              </el-table-column>
              <el-table-column prop="skuName" label="SKU名称">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.skuName"
                    maxlength="500"
                    placeholder="请输入名称"
                  ></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="skuPrice" label="价格" width="130">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.skuPrice"
                    type="number"
                    maxlength="20"
                    placeholder="请输入价格"
                    style="width: 120px;"
                  ></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="stockInventory" label="库存" width="130">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.stockInventory"
                    type="number"
                    placeholder="请输入库存"
                    style="width: 120px;"
                  ></el-input>
                </template>
              </el-table-column>
              <el-table-column type="expand">
                <template slot-scope="scope">
                  <el-form-item label="图片" v-for="(item,index) in scope.row.skuImgList" :key="index">
                    <el-image
                      :src="showImg(item.imgUrl)"
                    ></el-image>
                  </el-form-item>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
        <div v-else-if="stepsIndex == 4">
          <div class="form-step-content">
            <div style="text-align: center;">
              <svg-icon
                icon-class="kongxinduigou"
                style="color: green;width: 50px;height: 50px;">
              </svg-icon>
              <span style="display: block;line-height: 50px">
                商品信息提交成功
              </span>
            </div>
          </div>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
          :disabled="stepsIndex === 0"
          @click="previousStep"
        >上一步
        </el-button>
        <el-button
          :disabled="stepsIndex === 4"
          @click="nextStep"
        >下一步
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
// import Vue from 'vue';
import CategoryCascader from "@/components/Item/CategoryCascader";
import {getListByCatId} from "@/api/ecps/item/brand";
import {getListByCatId as getFeatureGroupListByCatId} from "@/api/ecps/item/feature-group";
import {getAllByCatId} from "@/api/ecps/item/feature";
import {add} from "@/api/ecps/item/item";
import Tinymce from "@/components/Tinymce/index";

export default {
  name: "AddItem",
  components: {Tinymce, CategoryCascader},
  data() {
    return {
      stepsIndex: 0,
      form: {
        itemName: undefined,
        catId: undefined,
        brandId: undefined,
        keywords: undefined,
        promotion: undefined,
        imgs: undefined,
        pageDesc: undefined,
        itemClob: {
          itemId: undefined,
          itemDesc: undefined,
          packingList: undefined
        },
        paraList: [],
        skuList: [
          {
            skuName: undefined,
            skuPrice: undefined,
            stockInventory: undefined,
            sepcList: [],
            skuImgList: [
              {
                imgUrl: 'epcs/brand/76cbbc5ae6a1436db0ecb791ab7844b9.png',
                defaultImg: 1
              },
              {
                imgUrl: 'epcs/brand/76cbbc5ae6a1436db0ecb791ab7844b9.png',
                defaultImg: 0
              }
            ]
          }, {
            skuName: undefined,
            skuPrice: undefined,
            stockInventory: undefined
          }
        ]
      },
      rules: {
        itemName: [
          {required: true, message: "名称不能为空", trigger: 'blur'}
        ]
      },
      catIdPath: [],
      // 品牌
      brandOptions: [],
      // 属性组
      featureGroupOptions: [],
      // 规格属性
      specOptions: [],
      // 规格数组选择项生成
      tabsIndex: '0-0',
      // 规格选中项索引
      selectedSpecArr: [],
      // sku的规格（选中的规格）
      specOptionsNew: [],
      // sku的规格笛卡尔积
      calculationSpecData: []
    }
  },
  created() {
  },
  mounted() {
  },
  watch: {},
  methods: {
    // 下一步
    nextStep() {
      ++this.stepsIndex;
      if (this.stepsIndex === 3) {
        this.calculationSkuData();
      } else if (this.stepsIndex === 4) {
        // 提交
        this.submitItem();
      }
    },
    // 上一步
    previousStep() {
      --this.stepsIndex;
    },
    // 分类id切换
    catIdChange(v) {
      this.form.catId = v;
      // 加载品牌
      this.getBrandOptons(v);
      // 加载商品属性
      this.getFeatureGroupOptions(v);
      // 加载规格属性
      this.getSpecFeatureOptions(v);
    },
    // 获取品牌选项
    getBrandOptons(catId) {
      getListByCatId(catId).then(res => {
        this.brandOptions = res.data;
      })
    },
    // 获取属性组选项
    getFeatureGroupOptions(catId) {
      getFeatureGroupListByCatId(catId, {isSpec: 0, needFeatureList: 1}).then(res => {
        let data = this.deepClone(res.data);
        // 初始化表单属性项
        let paraArr = [];
        for (const item of data) {
          for (const item2 of item.featureList) {
            let o = {
              featureId: item2.featureId,
              paraValue: undefined
            };
            /*if (item2.inputType === 1) {
              o.paraValueArr = [];
            }*/
            paraArr.push(o);
            // 缓存表单paraList真实索引
            item2.realParaIndex = paraArr.length - 1;
          }
        }
        this.form.paraList = paraArr;
        this.featureGroupOptions = data;
      })
    },
    // 获取规格属性选项
    getSpecFeatureOptions(catId) {
      getAllByCatId(catId, {isSpec: 1}).then(res => {
        let data = this.deepClone(res.data);
        // 初始化表单规格项
        let specArr = [];
        let selectedSpecArrInit = [];
        for (const item of data) {
          let arr = item.selectValues.split(',');
          item.options = arr;
          let obja = {
            featureId: item.featureId
          };
          let selected = [];
          for (const it of arr) {
            specArr.push({
              featureId: item.featureId,
              sepcValue: it
            });
            selected.push(true);
          }
          obja.selected = selected;
          selectedSpecArrInit.push(obja);
        }
        // this.form.specList = specArr;
        this.specOptions = data;
        this.selectedSpecArr = selectedSpecArrInit;
      })
    },
    // 属性选项切换
    handleTabsClick(tab, event) {
      console.log(tab)
    },
    // 字符串属性值转select选项数组
    strToSelectOptions(str) {
      let arr = str.split(',');
      let r = [];
      for (const item of arr) {
        r.push({label: item, value: item});
      }
      return r;
    },
    // 监听属性选项值改变
    paraListChange(value, realParaIndex) {
      console.log(value, realParaIndex);
    },
    // 规格属性选项变化
    sepcListChange(value, index, idx) {
      console.log(value, index, idx);
    },
    // 笛卡尔积计算
    descartes(dataValues, result, layer, curList) {
      if (layer < dataValues.length - 1) {
        if (dataValues[layer].options.length == 0) {
          this.descartes(dataValues, result, layer + 1, curList);
        } else {
          for (let i = 0; i < dataValues[layer].options.length; i++) {
            let list = JSON.parse(JSON.stringify(curList));
            // console.log(list)
            let data = {
              featureName: dataValues[layer].featureName,
              featureId: dataValues[layer].featureId,
              value: dataValues[layer].options[i],
              specValue: dataValues[layer].options[i]
            };
            list.push(data);
            this.descartes(dataValues, result, layer + 1, list);
          }
        }
      } else if (layer == dataValues.length - 1) {
        if (dataValues[layer].options.length == 0) {
          result.push(curList);
        } else {
          for (let i = 0; i < dataValues[layer].options.length; i++) {
            let list = JSON.parse(JSON.stringify(curList));
            let data = {
              featureName: dataValues[layer].featureName,
              featureId: dataValues[layer].featureId,
              value: dataValues[layer].options[i],
              specValue: dataValues[layer].options[i]
            };
            list.push(data);
            result.push(list);
          }
        }
      }
    },
    // 计算SKU表格数据
    calculationSkuData() {
      // 笛卡尔积计算
      let specOptions = this.deepClone(this.specOptions);
      // 计算已选中的属性二维数组
      let specOptionsNew = [];
      for (let i = 0; i < specOptions.length; i++) {
        let spec = specOptions[i];
        let spec1 = this.deepClone(spec);
        delete spec1.options;
        let newOptions = [];
        // 重新计算options
        for (let j = 0; j < this.selectedSpecArr[i].selected.length; j++) {
          let b = this.selectedSpecArr[i].selected[j];
          if (b) {
            // 选中规格
            newOptions.push(spec.options[j]);
          }
        }
        spec1.options = newOptions;
        specOptionsNew.push(spec1);
      }
      this.specOptionsNew = specOptionsNew;
      // 计算笛卡尔积
      let result = [];
      this.descartes(specOptionsNew, result, 0, []);
      console.log(result)
      this.calculationSpecData = result;
      // 生成SKU表格数据
      let skuArr = [];
      for (const spec of this.calculationSpecData) {
        // 默认sku名称 start
        let tempSkuName = this.form.itemName;
        for (const it of spec) {
          tempSkuName += ' ' + it.value
        }
        // 默认sku名称 end
        let sku = {
          skuName: tempSkuName,
          skuPrice: undefined,
          stockInventory: undefined,
          specList: spec,
          skuImgList: [
            {
              imgUrl: 'epcs/brand/76cbbc5ae6a1436db0ecb791ab7844b9.png',
              defaultImg: 0
            }
          ]
        }
        skuArr.push(sku);
      }
      this.form.skuList = skuArr;
    },
    // 提交表单
    submitItem() {
      // 商品属性多选转,拼接
      let form = this.deepClone(this.form);
      for (const para of form.paraList) {
        if (para.paraValue instanceof Array) {
          para.paraValue = para.paraValue.join(',');
        }
      }
      add(form).then(res => {
        this.msgSuccess('保存成功');
      }).catch(() => {
        --this.stepsIndex;
      })
    }
  }
}
</script>

<style scoped>
.form-step-content {
  padding: 20px;
}

.dialog-footer {
  text-align: center;
  position: absolute;
  bottom: 0;
  width: 100%;
  padding: 20px;
}
</style>
