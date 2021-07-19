/**
 * 常用工具方法
 */

/**
 * 深克隆对象或数组
 */
export function deepClone(objOrArr) {
  return JSON.parse(JSON.stringify(objOrArr));
}

// 日期格式化
export function parseTime(time, pattern) {
  if (arguments.length === 0 || !time) {
    return null
  }
  const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
      time = parseInt(time)
    } else if (typeof time === 'string') {
      time = time.replace(new RegExp(/-/gm), '/');
    }
    if ((typeof time === 'number') && (time.toString().length === 10)) {
      time = time * 1000
    }
    date = new Date(time)
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
    let value = formatObj[key]
    // Note: getDay() returns 0 on Sunday
    if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value] }
    if (result.length > 0 && value < 10) {
      value = '0' + value
    }
    return value || 0
  })
  return time_str
}

// 表单重置
export function resetForm(refName) {
  if (this.$refs[refName]) {
    this.$refs[refName].resetFields();
  }
}

/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 * @param {*} rootId 根Id 默认 0
 */
export function handleTree(data, id, parentId, children, rootId) {
  id = id || 'id'
  parentId = parentId || 'parentId'
  children = children || 'children'
  rootId = rootId || Math.min.apply(Math, data.map(item => { return item[parentId] })) || 0
  // 对源数据深度克隆
  const cloneData = JSON.parse(JSON.stringify(data))
  // 循环所有项
  const treeData = cloneData.filter(father => {
    let branchArr = cloneData.filter(child => {
      // 返回每一项的子级数组
      return father[id] === child[parentId]
    });
    branchArr.length > 0 ? father.children = branchArr : '';
    // 返回第一层
    return father[parentId] === rootId;
  });
  return treeData != '' ? treeData : data;
}

// 获取字典名称
export function selectDictLabel(arr, i) {
  for (const item of arr) {
    if (item.dictValue === i) {
      return item.dictLabel;
    }
  }
  return undefined;
}

// 通过文件后缀获取对应的svg图标名称
const imageArr = ['jpg', 'jpeg', 'png', 'gif', 'tiff', 'bmp', 'psd'];
const wordArr = ['dox', 'docx'];
const excelArr = ['xls', 'xlsx'];
const pptArr = ['ppt', 'pptx'];
const audioArr = ['acc', 'mp3', 'amr'];
const videoArr = ['wmv', 'mp4', 'rmv', 'rmvb', 'flv', 'avi'];
export function getSvgByFileExt(fileName) {
  let ext = fileName.substr(fileName.lastIndexOf(".") + 1);
  if (imageArr.indexOf(ext) !== -1) {
    return 'image';
  } else if (wordArr.indexOf(ext) !== -1) {
    return 'word';
  } else if (excelArr.indexOf(ext) !== -1) {
    return 'excel';
  } else if (pptArr.indexOf(ext) !== -1) {
    return 'ppt';
  } else if (audioArr.indexOf(ext) !== -1) {
    return 'audio';
  } else if (videoArr.indexOf(ext) !== -1) {
    return 'video';
  } else {
    return ext;
  }
}

// 对象数组解析出属性数组
export function objArr2AttrArr(arr, key) {
  let r = [];
  for (let i = 0; i < arr.length; i++) {
    let item = arr[i];
    r.push(item[key]);
  }
  return r;
}

// 更换对象属性名
export function replaceNewAttrName(arr, sourcekeyarr, targetkeyarr) {
  for (let i = 0; i < arr.length; i++) {
    let item = arr[i];
    for (let j = 0; j < sourcekeyarr.length; j++) {
      let skey = sourcekeyarr[j];
      let tkey = targetkeyarr[j];
      item[tkey] = item[skey];
      delete item[skey];
    }
  }
  return arr;
}

/**
 * 递归查找所有父节点的id
 *
 * @param array 树结构数据
 * @param id 目标节点
 * @param idKey id的key
 * @param pidKey panretId的key
 * @param resultKey 返回那个字段的集合
 * @returns {[]}
 */
export function findParents(array, id, idKey, pidKey = 'parentId', resultKey = idKey) {
  let parentArray = [];
  if (array.length === 0) {
    return parentArray;
  }
  function recursion(arrayNew, id) {
    for (let i = 0; i < arrayNew.length; i++) {
      let node = arrayNew[i];
      if (node[idKey] === id) {
        parentArray.unshift(node[resultKey]);
        recursion(array, node[pidKey]);
        break;
      } else {
        if (node.children) {
          recursion(node.children, id);
        }
      }
    }
    return parentArray;
  }
  let arrayNew = array;
  parentArray = recursion(arrayNew, id);
  return parentArray;
}
