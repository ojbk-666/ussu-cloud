<template>
  <div>
    <el-cascader
      v-model="value1"
      placeholder="请选择关联类别"
      :options="catOptions"
      :props="cascaderProps"
      filterable
      clearable
      :collapse-tags="false"
      @change="selectedChange"
      :class="customClass"
      :style="customStyle"
    ></el-cascader>
  </div>
</template>

<script>
import {getListTree} from "@/api/ecps/item/cat";
import {findParents} from '@/utils/ussu'

export default {
  name: "CategoryCascader",
  components: {},
  props: {
    paths: {
      type: Array,
      required: false,
      default: () => []
    },
    multiple: {
      type: Boolean,
      required: false,
      default: false
    },
    customClass: {
      type: String,
      required: false,
      default: undefined
    },
    customStyle: {
      type: String,
      required: false,
      default: undefined
    }
  },
  data() {
    return {
      catOptions: [],
      value1: this.paths,
      cascaderProps: {
        multiple: this.multiple,
        label: 'catName',
        value: 'catId'
      }
    }
  },
  watch: {
    paths(v) {
      this.value1 = this.paths;
    },
    value1(v) {
      this.$emit("update:paths", v);
    }
  },
  created() {
    this.getCatOptions();
  },
  methods: {
    getCatOptions() {
      getListTree().then(res => {
        this.catOptions = res.data;
      })
    },
    // 回显 根据value计算完整路径（向上递归查找）
    /*echoValue() {
      for (const v of this.value1) {
        let p = findParents(this.catOptions, v, 'catId', 'parentId');
        if (p && p.length > 0) {
          this.cascaderList.push(p);
        }
      }
    },*/
    selectedChange(v) {
      if (this.cascaderProps.multiple) {
        let arr = [];
        for (const item of v) {
          arr.push(item[item.length - 1]);
        }
        this.$emit('change', arr);
      } else {
        this.$emit('change', v[v.length - 1]);
      }
    }
  }
}
</script>

<style scoped>

</style>
