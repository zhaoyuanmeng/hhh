/**
 * 函数防抖(debounce) 
 * --> 在事件被触发n秒后再执行回调，如果在这n秒内又被触发，则重新计时。
 * @param Function fn 延时调用函数
 * @param Number delay 延迟多长时间
 * @param Boolean immediate 
 * @return Function 延迟执行的方法
 */
function debounce(func, delay = 300, immediate = false) {
    let timer = null;
    return function () {
        if (timer) {
            clearTimeout(timer);
        }
        if (immediate && !timer) {
            func.apply(this, arguments);
        }
        timer = setTimeout(() => {
            func.apply(this, arguments);
        }, delay);
    };
}

/**
 * 函数节流(throttle) 
 * --> 规定在一个单位时间内，只能触发一次函数。如果这个单位时间内触发多次函数，只有一次生效。
 * @param Function fn 延时调用函数
 * @param Number delay 延迟多长时间
 * @return Function 延迟执行的方法
 */
function throttle(fn, delay = 300) {
    let timer = null;
    return function () {
        clearTimeout(timer);
        timer = setTimeout(function () {
            fn();
        }, delay);
    }
}

function copyData2Childrens(data, copyData, array) {
    if (Array.isArray(data)) {
        data.forEach(item => {
            if (item.childrens) {
                copyData2Childrens(item.childrens, copyData, array)
            }
            if (item.datas) {
                if (!item.childrens) {
                    item.childrens = [];
                }
                if (copyData) {
                    array.push(...item.datas)
                }
                item.childrens = item.childrens.concat(item.datas)
            }
        })
    } else if (Object.prototype.toString.call(data) === '[object Object]') {
        if (data.childrens) copyData2Childrens(data.childrens, copyData, array)

        if (data.datas) {
            if (!data.childrens) data.childrens = []
            if (copyData) array.push(...data.datas)
            data.childres = data.childrens.concat(data.datas)
        }
    }
}

function fileSizsTransform(bytes) {
    // if (isNaN(size)) return '0MB';
    // return `${Math.round(Number(size) / 1024 / 1024 * 100) / 100}MB`;
    if (bytes === 0) return '0 B';
    var k = 1024,
        sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
        i = Math.floor(Math.log(bytes) / Math.log(k));
    return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];
}


/**
 * 数据导出为excel 
 * @param rows obj数组
 */
const tableToExcel = (rows) => {
    let objKeys = Object.keys(rows[0])
    let str = '<tr>';
    objKeys.forEach(key => {
        str += `<td>${key}</td>`
    })
    str += `</tr >`

    // 循环遍历，每行加入tr标签，每个单元格加td标签
    for (let i = 0; i < rows.length; i++) {
        str += '<tr>';

        for (const key in rows[i]) {
            // 增加\t为了不让表格显示科学计数法或者其他格式
            let value = rows[i][key]
            if (typeof value == "object") {
                value = JSON.stringify(value)
            }
            str += `<td>${value + '\t'}</td>`;
        }
        str += '</tr>';
    }

    // Worksheet名
    const worksheet = 'Sheet1'
    const uri = 'data:application/vnd.ms-excel;base64,';

    // 下载的表格模板数据
    const template = `<html xmlns:o="urn:schemas-microsoft-com:office:office" 
	xmlns:x="urn:schemas-microsoft-com:office:excel" 
	xmlns="http://www.w3.org/TR/REC-html40">
	<head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet>
	<x:Name>${worksheet}</x:Name>
	<x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet>
	</x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]-->
	</head><body><table>${str}</table></body></html>`;

    // 输出base64编码
    const base64 = s => window.btoa(unescape(encodeURIComponent(s)));

    // 下载模板
    window.location.href = uri + base64(template);

}

export { debounce, throttle, fileSizsTransform, copyData2Childrens, tableToExcel }