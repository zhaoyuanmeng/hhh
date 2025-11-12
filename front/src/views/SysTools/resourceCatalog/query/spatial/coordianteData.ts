
export const pointAndPolygonInfo = {
    point: [
        {
            coordinates: [],
            pointInfo: {
                id: '监控点',
                coordinate: [[492789.3125, 2491984.25, 2.31334], [492850, 2491984.5, 2.313516], [492867.71875, 2491993.25, 2.313223], [492867.53125, 2492041.75, 2.31291], [492853.65625, 2492073.25, 2.313047], [492817.34375, 2492098, 3.107793], [492776.875, 2492067, 2.233652], [492796.1875, 2492015.25, 2.313437], [492841.34375, 2492005.25, 2.313242], [492887.46875, 2491979, 2.31332], [492703.0625, 2492036.5, -0.886914]],//坐标位置
                coordinateType: 0,//默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
                anchors: [-25, 50],//锚点，设置Marker的整体偏移，取值规则和imageSize设置的宽高有关，图片的左上角会对准标注点的坐标位置。示例设置规则：x=-imageSize.width/2，y=imageSize.height
                imageSize: [50, 50],//图片的尺寸
                range: [1, 100000000],//可视范围
                // imagePath: window.location.origin + "/customTag/images/location.png",//显示图片路径
                imagePath: "@path:/customTag/images/location.png",//显示图片路径
                fixedSize: true,//图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false 

                text: '北京银行',//显示的文字 
                useTextAnimation: false,//关闭文字展开动画效果 打开会影响效率
                textRange: [1, 400],//文本可视范围[近裁距离, 远裁距离]
                textOffset: [0, 0],// 文本偏移
                textBackgroundColor: [0, 0, 1, 0.3],//文本背景颜色
                fontSize: 16,//字体大小
                fontOutlineSize: 1,//字体轮廓线大小
                fontColor: Color.White,//字体颜色
                fontOutlineColor: Color.Black,//字体轮廓线颜色

                showLine: false,//标注点下方是否显示垂直牵引线
                lineSize: [2, 100],//垂直牵引线宽度和高度[width, height]
                lineColor: Color.SpringGreen,//垂直牵引线颜色
                lineOffset: [0, 0],//垂直牵引线偏移

                autoHeight: false,// 自动判断下方是否有物体
                displayMode: 1,//智能显示模式  开发过程中请根据业务需求判断使用四种显示模式 
                clusterByImage: true,// 聚合时是否根据图片路径分类，即当多个marker的imagePath路径参数相同时按路径对marker分类聚合
                priority: 0,//避让优先级
                occlusionCull: false//是否参与遮挡剔除
            }
        }
    ]
}

export const pointName = []